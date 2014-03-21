/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.ais.packet;

import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.enav.model.Country;
import dk.dma.enav.util.function.Predicate;
import dk.dma.internal.ais.generated.parser.sourcefilter.SourceFilterBaseVisitor;
import dk.dma.internal.ais.generated.parser.sourcefilter.SourceFilterLexer;
import dk.dma.internal.ais.generated.parser.sourcefilter.SourceFilterParser;
import dk.dma.internal.ais.generated.parser.sourcefilter.SourceFilterParser.EqualityTestContext;
import dk.dma.internal.ais.generated.parser.sourcefilter.SourceFilterParser.OrAndContext;
import dk.dma.internal.ais.generated.parser.sourcefilter.SourceFilterParser.ParensContext;
import dk.dma.internal.ais.generated.parser.sourcefilter.SourceFilterParser.ProgContext;
import dk.dma.internal.ais.generated.parser.sourcefilter.SourceFilterParser.SourceBasestationContext;
import dk.dma.internal.ais.generated.parser.sourcefilter.SourceFilterParser.SourceCountryContext;
import dk.dma.internal.ais.generated.parser.sourcefilter.SourceFilterParser.SourceIdContext;
import dk.dma.internal.ais.generated.parser.sourcefilter.SourceFilterParser.SourceRegionContext;
import dk.dma.internal.ais.generated.parser.sourcefilter.SourceFilterParser.SourceTypeContext;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceBaseStation;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceCountry;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceId;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceRegion;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceType;
import static java.util.Objects.requireNonNull;

/**
 * 
 * @author Kasper Nielsen
 */
class AisPacketFiltersSourceFilterParser {
    static Predicate<AisPacket> parseSourceFilter(String filter) {
        ANTLRInputStream input = new ANTLRInputStream(requireNonNull(filter));
        SourceFilterLexer lexer = new SourceFilterLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SourceFilterParser parser = new SourceFilterParser(tokens);

        // Better errors
        lexer.removeErrorListeners();
        parser.removeErrorListeners();
        lexer.addErrorListener(new VerboseListener());
        parser.addErrorListener(new VerboseListener());

        ProgContext tree = parser.prog();
        return tree.filterExpression().accept(new SourceFilterToPredicateVisitor());
    }

    static class VerboseListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
                String msg, RecognitionException e) {
            throw new IllegalArgumentException(msg + " @ character " + charPositionInLine);
            // if (recognizer instanceof Parser)
            // List<String> stack = ((Parser) recognizer).getRuleInvocationStack();
            // Collections.reverse(stack);
            // System.err.println("rule stack: " + stack);
            // System.err.println("line " + line + ":" + charPositionInLine + " at " + offendingSymbol + ": " + sentenceStr);
        }
    }

    static class SourceFilterToPredicateVisitor extends SourceFilterBaseVisitor<Predicate<AisPacket>> {

        @Override
        public Predicate<AisPacket> visitOrAnd(OrAndContext ctx) {
            return ctx.op.getType() == SourceFilterParser.AND ? visit(ctx.filterExpression(0)).and(visit(ctx.filterExpression(1))) : visit(
                    ctx.filterExpression(0)).or(visit(ctx.filterExpression(1)));
        }

        @Override
        public Predicate<AisPacket> visitParens(ParensContext ctx) {
            final Predicate<AisPacket> p = visit(ctx.filterExpression());
            return new Predicate<AisPacket>() {
                public boolean test(AisPacket element) {
                    return p.test(element);
                }

                public String toString() {
                    return "(" + p.toString() + ")";
                }
            };
        }

        @Override
        public Predicate<AisPacket> visitSourceBasestation(SourceBasestationContext ctx) {
            return checkNegate(ctx.equalityTest(), filterOnSourceBaseStation(readValueListAsStringArray(ctx.valueList().value())));
        }

        @Override
        public Predicate<AisPacket> visitSourceCountry(SourceCountryContext ctx) {
            List<Country> countries = Country.findAllByCode(readValueListAsStringArray(ctx.valueList().value()));
            return checkNegate(ctx.equalityTest(),
                    filterOnSourceCountry(countries.toArray(new Country[countries.size()])));
        }

        @Override
        public Predicate<AisPacket> visitSourceId(final SourceIdContext ctx) {
            return checkNegate(ctx.equalityTest(), filterOnSourceId(readValueListAsStringArray(ctx.valueList().value())));
        }

        @Override
        public Predicate<AisPacket> visitSourceRegion(SourceRegionContext ctx) {
            return checkNegate(ctx.equalityTest(), filterOnSourceRegion(readValueListAsStringArray(ctx.valueList().value())));
        }

        @Override
        public Predicate<AisPacket> visitSourceType(SourceTypeContext ctx) {
            return checkNegate(ctx.equalityTest(), filterOnSourceType(readValueListAsSourceTypeArray(ctx.valueList().value())));
        }

        @Override public Predicate<AisPacket> visitMessageId(@NotNull SourceFilterParser.MessageIdContext ctx) {
            return createFilterPredicate(ctx.getStart(), ctx.operator(), ctx.valueSpec());
        }

        @Override public Predicate<AisPacket> visitMessageMmsi(@NotNull SourceFilterParser.MessageMmsiContext ctx) {
            return createFilterPredicate(ctx.getStart(), ctx.operator(), ctx.valueSpec());
        }

        @Override public Predicate<AisPacket> visitMessageImo(@NotNull SourceFilterParser.MessageImoContext ctx) {
            return createFilterPredicate(ctx.getStart(), ctx.operator(), ctx.valueSpec());
        }

        @Override public Predicate<AisPacket> visitMessageName(@NotNull SourceFilterParser.MessageNameContext ctx) {
            return createFilterPredicate(ctx.getStart(), ctx.operator(), ctx.valueSpec());
        }

        @Override public Predicate<AisPacket> visitMessageSpeedOverGround(@NotNull SourceFilterParser.MessageSpeedOverGroundContext ctx) {
            return createFilterPredicate(ctx.getStart(), ctx.comparison(), ctx.FLOAT() != null ? ctx.FLOAT() : ctx.INT());
        }

        @Override public Predicate<AisPacket> visitMessageCourseOverGround(@NotNull SourceFilterParser.MessageCourseOverGroundContext ctx) {
            return createFilterPredicate(ctx.getStart(), ctx.comparison(), ctx.FLOAT() != null ? ctx.FLOAT() : ctx.INT());
        }

        @Override public Predicate<AisPacket> visitMessageDraught(@NotNull SourceFilterParser.MessageDraughtContext ctx) {
            return createFilterPredicate(ctx.getStart(), ctx.comparison(), ctx.FLOAT() != null ? ctx.FLOAT() : ctx.INT());
        }

        @Override public Predicate<AisPacket> visitMessageHeading(@NotNull SourceFilterParser.MessageHeadingContext ctx) {
            return createFilterPredicate(ctx.getStart(), ctx.operator(), ctx.valueSpec());
        }

        @Override public Predicate<AisPacket> visitMessageLongitude(@NotNull SourceFilterParser.MessageLongitudeContext ctx) {
            return createFilterPredicate(ctx.getStart(), ctx.comparison(), ctx.FLOAT() != null ? ctx.FLOAT() : ctx.INT());
        }

        @Override public Predicate<AisPacket> visitMessageLatitude(@NotNull SourceFilterParser.MessageLatitudeContext ctx) {
            return createFilterPredicate(ctx.getStart(), ctx.comparison(), ctx.FLOAT() != null ? ctx.FLOAT() : ctx.INT());
        }

        @Override
        public Predicate<AisPacket> visitAisMessagetype(@NotNull SourceFilterParser.AisMessagetypeContext ctx) {
            return new Predicate<AisPacket>() {
                @Override
                public boolean test(AisPacket element) {
                    return true;
                }
            };
        }

        public Predicate<AisPacket> checkNegate(EqualityTestContext context, Predicate<AisPacket> p) {
            String text = context.getChild(0).getText();
            return text.equals("!=") ? p.negate() : p;
        }

        private static String[] readValueListAsStringArray(List<SourceFilterParser.ValueContext> list) {
            ArrayList<String> r = new ArrayList<>();
            Iterator<SourceFilterParser.ValueContext> i = list.iterator();
            while (i.hasNext()) {
                SourceFilterParser.ValueContext value = i.next();
                r.add(value.getText());
            }
            return r.toArray(new String[r.size()]);
        }

        private static SourceType[] readValueListAsSourceTypeArray(List<SourceFilterParser.ValueContext> list) {
            ArrayList<SourceType> r = new ArrayList<>();
            Iterator<SourceFilterParser.ValueContext> i = list.iterator();
            while (i.hasNext()) {
                SourceFilterParser.ValueContext value = i.next();
                r.add(SourceType.fromString(value.getText()));
            }
            return r.toArray(new SourceType[r.size()]);
        }

        private static int[] readValueListAsIntArray(List<SourceFilterParser.ValueContext> list) {
            ArrayList<Integer> r = new ArrayList<>();
            Iterator<SourceFilterParser.ValueContext> iterator = list.iterator();
            while (iterator.hasNext()) {
                SourceFilterParser.ValueContext value = iterator.next();
                r.add(Integer.valueOf(value.getText()));
            }
            int n = r.size();
            int[] primitiveList = new int[n];
            for (int i=0; i<n; i++) {
                primitiveList[i] = r.get(i);
            }
            return primitiveList;
        }


        private static Object extractValue(SourceFilterParser.ValueContext valueCtx) {
            if (valueCtx.FLOAT() != null) {
                return Float.valueOf(valueCtx.FLOAT().getText());
            } else if (valueCtx.INT() != null) {
                return Integer.valueOf(valueCtx.INT().getText());
            } else if (valueCtx.SIXBITSTRING() != null) {
                return valueCtx.SIXBITSTRING().getText();
            } else {
                return null;
            }
        }

        private static Class<?> mapFieldTokenToValueClass(Object value) {
            return value.getClass();
        }

        private static String mapFieldTokenToFilterFactoryMethodName(String fieldToken) {
            switch(fieldToken) {
                case "m.id":
                    return "filterOnMessageId";
                case "m.mmsi":
                    return "filterOnMessageMmsi";
                case "m.imo":
                    return "filterOnMessageImo";
                case "m.name":
                    return "filterOnMessageName";
                case "m.sog":
                    return "filterOnMessageSpeedOverGround";
                case "m.cog":
                    return "filterOnMessageCourseOverGround";
                case "m.hdg":
                    return "filterOnMessageTrueHeading";
                case "m.lon":
                    return "filterOnMessageLongitude";
                case "m.lat":
                    return "filterOnMessageLatitude";
                case "m.draught":
                    return "filterOnMessageDraught";
            }
            throw new IllegalArgumentException("No mapping to predicate name for field " + fieldToken);
        }

        /**
         * Create a filter predicate for comparisons given that the right-hand-side is of type float.
         * @param field
         * @param comparisonCtx
         * @param aFloat
         * @return
         */
        private Predicate<AisPacket> createFilterPredicate(Token field, SourceFilterParser.ComparisonContext comparisonCtx, TerminalNode aFloat) {
            return createFilterPredicate(field, comparisonCtx, Float.valueOf(aFloat.getText()), Float.class);
        }

        /**
         * Create a filter predicate for comparisons by deducing the type of the right-hand-side value.
         * @param field
         * @param comparisonCtx
         * @param valueCtx
         * @return
         */
        private static Predicate<AisPacket> createFilterPredicate(Token field, SourceFilterParser.ComparisonContext comparisonCtx, SourceFilterParser.ValueContext valueCtx) {
            Predicate<AisPacket> filter = null;

            if (valueCtx != null) {
                Object value = extractValue(valueCtx);
                Class<?> valueClass = mapFieldTokenToValueClass(value);

                filter = createFilterPredicate(field, comparisonCtx, value, valueClass);
            } else {
                throw new IllegalArgumentException("Can only use comparison of value, not valueList or valueRange.");
            }
            return filter;
        }

        /**
         * Create a filter predicate for comparisons given the field, comparison type, value, and value class.
         * @param field
         * @param comparisonCtx
         * @param value
         * @param valueClass
         * @return
         */
        private static Predicate<AisPacket> createFilterPredicate(Token field, SourceFilterParser.ComparisonContext comparisonCtx, Object value, Class<?> valueClass) {
            Predicate<AisPacket> filter = null;
            String operator = comparisonCtx.getText();
            String fieldToken = field.getText();
            String filterFactoryMethodName = mapFieldTokenToFilterFactoryMethodName(fieldToken);
            try {
                Method filterFactoryMethod = AisPacketFilters.class.getDeclaredMethod(filterFactoryMethodName, AisPacketFilters.Operator.class, valueClass);

                if ("=".equals(operator)) {
                    filter = (Predicate<AisPacket>) filterFactoryMethod.invoke(null, AisPacketFilters.Operator.EQUALS, value);
                } else if ("!=".equals(operator)) {
                    filter = (Predicate<AisPacket>) filterFactoryMethod.invoke(null, AisPacketFilters.Operator.NOT_EQUALS, value);
                } else if (">".equals(operator)) {
                    filter = (Predicate<AisPacket>) filterFactoryMethod.invoke(null, AisPacketFilters.Operator.GREATER_THAN, value);
                } else if (">=".equals(operator)) {
                    filter = (Predicate<AisPacket>) filterFactoryMethod.invoke(null, AisPacketFilters.Operator.GREATER_THAN_OR_EQUALS, value);
                } else if ("<".equals(operator)) {
                    filter = (Predicate<AisPacket>) filterFactoryMethod.invoke(null, AisPacketFilters.Operator.LESS_THAN, value);
                } else if ("<=".equals(operator)) {
                    filter = (Predicate<AisPacket>) filterFactoryMethod.invoke(null, AisPacketFilters.Operator.LESS_THAN_OR_EQUALS, value);
                } else {
                    throw new IllegalArgumentException("Operator " + operator + " not implemented.");
                }
            } catch (InvocationTargetException e) {
                e.printStackTrace(System.err);
            } catch (NoSuchMethodException e) {
                e.printStackTrace(System.err);
            } catch (IllegalAccessException e) {
                e.printStackTrace(System.err);
            }
            return filter;
        }

        /**
         * Create a filter predicate for 'in-list' (of integers).
         * @param field
         * @param valueListCtx
         * @return
         */
        private static Predicate<AisPacket> createFilterPredicate(Token field, SourceFilterParser.ValueListContext valueListCtx) {
            Predicate<AisPacket> filter = null;
            try {
                int[] list = readValueListAsIntArray(valueListCtx.value());
                String fieldToken = field.getText();
                String filterFactoryMethodName = mapFieldTokenToFilterFactoryMethodName(fieldToken) + "InList";
                Method filterFactoryMethod = AisPacketFilters.class.getDeclaredMethod(filterFactoryMethodName, int[].class);
                filter = (Predicate<AisPacket>) filterFactoryMethod.invoke(null, list);
            } catch (NoSuchMethodException e) {
                e.printStackTrace(System.err);
            } catch (InvocationTargetException e) {
                e.printStackTrace(System.err);
            } catch (IllegalAccessException e) {
                e.printStackTrace(System.err);
            }
            return filter;
        }

        /**
         * Create a filter predicate for 'in-range' of integers.
         * @param field
         * @param valueRangeCtx
         * @return
         */
        private static Predicate<AisPacket> createFilterPredicate(Token field, SourceFilterParser.ValueRangeContext valueRangeCtx) {
            Predicate<AisPacket> filter = null;
            try {
                int min = Integer.valueOf(valueRangeCtx.value(0).getText());
                int max = Integer.valueOf(valueRangeCtx.value(1).getText());
                String fieldToken = field.getText();
                String filterFactoryMethodName = mapFieldTokenToFilterFactoryMethodName(fieldToken) + "InRange";
                Method filterFactoryMethod = AisPacketFilters.class.getDeclaredMethod(filterFactoryMethodName, int.class, int.class);
                filter = (Predicate<AisPacket>) filterFactoryMethod.invoke(null, min, max);
            } catch (NoSuchMethodException e) {
                e.printStackTrace(System.err);
            } catch (InvocationTargetException e) {
                e.printStackTrace(System.err);
            } catch (IllegalAccessException e) {
                e.printStackTrace(System.err);
            }
            return filter;
        }

        /**
         * Create a filter predicate.
         * @param field
         * @param operatorCtx
         * @param valueSpecCtx
         * @return
         */
        private static Predicate<AisPacket> createFilterPredicate(Token field, SourceFilterParser.OperatorContext operatorCtx, SourceFilterParser.ValueSpecContext valueSpecCtx) {
            Predicate<AisPacket> filter = null;

            SourceFilterParser.ComparisonContext comparisonCtx = operatorCtx.comparison();
            SourceFilterParser.InListOrRangeContext inListOrRangeCtx = operatorCtx.inListOrRange();

            if (comparisonCtx != null) {
                filter = createFilterPredicate(field, comparisonCtx, valueSpecCtx.value());
            } else if (inListOrRangeCtx != null) {
                SourceFilterParser.ValueListContext valueListCtx = valueSpecCtx.valueList();
                SourceFilterParser.ValueRangeContext valueRangeCtx = valueSpecCtx.valueRange();

                if (valueListCtx != null) {
                    filter = createFilterPredicate(field, valueListCtx);
                } else if (valueRangeCtx != null) {
                    filter = createFilterPredicate(field, valueRangeCtx);
                } else {
                    throw new IllegalArgumentException("Neither list or range; program not complete?");
                }
            }

            return filter;
        }
    }
}
