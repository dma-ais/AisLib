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
import dk.dma.enav.model.geometry.Area;
import dk.dma.enav.model.geometry.BoundingBox;
import dk.dma.enav.model.geometry.Circle;
import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.util.function.Predicate;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterBaseVisitor;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterParser;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterParser.OrAndContext;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterParser.ParensContext;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterParser.SourceBasestationContext;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;
import java.util.Set;

/**
 * @author Kasper Nielsen
 */
class AisPacketFiltersExpressionFilterParser extends ExpressionFilterParserBase {

    static Predicate<AisPacket> parseExpressionFilter(String filter) {
        return createFilterContext(filter).filterExpression().accept(new ExpressionFilterToPredicateVisitor());
    }

    static class ExpressionFilterToPredicateVisitor extends ExpressionFilterBaseVisitor<Predicate<AisPacket>> {

        @Override
        public Predicate<AisPacket> visitOrAnd(OrAndContext ctx) {
            return ctx.op.getType() ==
                    ExpressionFilterParser.AND ?
                    visit(ctx.filterExpression(0)).and(visit(ctx.filterExpression(1))) :
                    visit(ctx.filterExpression(0)).or(visit(ctx.filterExpression(1)));
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

        //
        // Tokens related to source
        //

        @Override
        public Predicate<AisPacket> visitSourceIdIn(@NotNull ExpressionFilterParser.SourceIdInContext ctx) {
            String fieldName = ctx.getStart().getText();
            String[] strings = extractStrings(ctx.stringList().string());
            Predicate<AisPacket> filter = createFilterPredicateForList(AisPacketFilters.class, fieldName, strings);
            return checkNegate(ctx, filter);
        }

        @Override
        public Predicate<AisPacket> visitSourceBasestation(@NotNull SourceBasestationContext ctx) {
            return createFilterPredicateForIntComparison(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitSourceBasestationIn(@NotNull ExpressionFilterParser.SourceBasestationInContext ctx) {
            return createFilterPredicateForIntRangeOrIntList(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitSourceCountryIn(@NotNull ExpressionFilterParser.SourceCountryInContext ctx) {
            String fieldName = ctx.getStart().getText();
            String[] strings = extractStrings(ctx.stringList().string());
            Country[] countries = getCountries(strings);
            Predicate<AisPacket> filter = createFilterPredicateForList(AisPacketFilters.class, fieldName, countries);
            return checkNegate(ctx, filter);
        }

        @Override
        public Predicate<AisPacket> visitSourceTypeIn(@NotNull ExpressionFilterParser.SourceTypeInContext ctx) {
            String fieldName = ctx.getStart().getText();
            String[] strings = extractStrings(ctx.stringList().string());
            SourceType[] sourceTypes = getSourceTypes(strings);
            Predicate<AisPacket> filter = createFilterPredicateForList(AisPacketFilters.class, fieldName, sourceTypes);
            return checkNegate(ctx, filter);
        }

        @Override
        public Predicate<AisPacket> visitSourceRegionIn(@NotNull ExpressionFilterParser.SourceRegionInContext ctx) {
            String fieldName = ctx.getStart().getText();
            String[] strings = extractStrings(ctx.stringList().string());
            Predicate<AisPacket> filter = createFilterPredicateForList(AisPacketFilters.class, fieldName, strings);
            return checkNegate(ctx, filter);
        }

        //
        // Tokens related to message contents
        //

        @Override
        public Predicate<AisPacket> visitMessageTime(@NotNull ExpressionFilterParser.MessageTimeContext ctx) {
            return createFilterPredicateForTimeComparison(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageTimeIn(@NotNull ExpressionFilterParser.MessageTimeInContext ctx) {
            Predicate<AisPacket> filter = createFilterPredicateForIntRangeOrIntList(AisPacketFilters.class, ctx);
            if (filter == null && ctx.stringList() != null) {
                String fieldName = ctx.getStart().getText();
                String[] strings = extractStrings(ctx.stringList().string());
                Set<Integer> intList = null;
                if ("m.month".equalsIgnoreCase(fieldName)) {
                    intList = getMonths(strings);
                } else if ("m.dow".equalsIgnoreCase(fieldName)) {
                    intList = getWeekdays(strings);
                }
                filter = checkNegate(ctx, createFilterPredicateForList(AisPacketFilters.class, fieldName, intList.toArray(new Integer[intList.size()])));
            }
            return filter;
        }

        @Override
        public Predicate<AisPacket> visitMessageId(@NotNull ExpressionFilterParser.MessageIdContext ctx) {
            return createFilterPredicateForIntComparison(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageIdIn(@NotNull ExpressionFilterParser.MessageIdInContext ctx) {
            return createFilterPredicateForIntRangeOrIntList(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageMmsi(@NotNull ExpressionFilterParser.MessageMmsiContext ctx) {
            return createFilterPredicateForIntComparison(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageMmsiIn(@NotNull ExpressionFilterParser.MessageMmsiInContext ctx) {
            return createFilterPredicateForIntRangeOrIntList(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageImo(@NotNull ExpressionFilterParser.MessageImoContext ctx) {
            return createFilterPredicateForIntComparison(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageImoIn(@NotNull ExpressionFilterParser.MessageImoInContext ctx) {
            return createFilterPredicateForIntRangeOrIntList(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageShiptype(@NotNull ExpressionFilterParser.MessageShiptypeContext ctx) {
            String fieldName = ctx.getStart().getText();
            String operator = ctx.compareTo().getText();
            String value = extractString(ctx.string());
            try {
                return createFilterPredicateForComparison(AisPacketFilters.class, fieldName, operator, Integer.valueOf(value));
            } catch (NumberFormatException e) {
                Set<Integer> shipTypes = getShipTypes(value);
                if (!operator.equals("=")) {
                    throw new IllegalArgumentException("Sorry, only = operator currently supported.");
                }
                return createFilterPredicateForList(AisPacketFilters.class, fieldName, shipTypes.toArray(new Integer[shipTypes.size()]));
            }
        }

        @Override
        public Predicate<AisPacket> visitMessageShiptypeIn(@NotNull ExpressionFilterParser.MessageShiptypeInContext ctx) {
            Predicate<AisPacket> filter = createFilterPredicateForIntRangeOrIntList(AisPacketFilters.class, ctx);
            if (filter == null && ctx.stringList() != null) {
                String fieldName = ctx.getStart().getText();
                String[] strings = extractStrings(ctx.stringList().string());
                Set<Integer> shipTypes = getShipTypes(strings);
                filter = checkNegate(ctx, createFilterPredicateForList(AisPacketFilters.class, fieldName, shipTypes.toArray(new Integer[shipTypes.size()])));
            }
            return filter;
        }

        @Override
        public Predicate<AisPacket> visitMessageNavigationalStatus(@NotNull ExpressionFilterParser.MessageNavigationalStatusContext ctx) {
            String fieldName = ctx.getStart().getText();
            String operator = ctx.compareTo().getText();
            String value = extractString(ctx.string());
            try {
                return createFilterPredicateForComparison(AisPacketFilters.class, fieldName, operator, Integer.valueOf(value));
            } catch (NumberFormatException e) {
                Set<Integer> navstats = getNavigationalStatuses(new String[]{value});
                if (!operator.equals("=")) {
                    throw new IllegalArgumentException("Sorry, only = operator currently supported.");
                }
                return createFilterPredicateForList(AisPacketFilters.class, fieldName, navstats.toArray(new Integer[navstats.size()]));
            }
        }

        @Override
        public Predicate<AisPacket> visitMessageNavigationalStatusIn(@NotNull ExpressionFilterParser.MessageNavigationalStatusInContext ctx) {
            Predicate<AisPacket> filter = createFilterPredicateForIntRangeOrIntList(AisPacketFilters.class, ctx);
            if (filter == null && ctx.stringList() != null) {
                String fieldName = ctx.getStart().getText();
                String[] strings = extractStrings(ctx.stringList().string());
                Set<Integer> navstats = getNavigationalStatuses(strings);
                filter = checkNegate(ctx, createFilterPredicateForList(AisPacketFilters.class, fieldName, navstats.toArray(new Integer[navstats.size()])));
            }
            return filter;
        }

        @Override
        public Predicate<AisPacket> visitMessageName(@NotNull ExpressionFilterParser.MessageNameContext ctx) {
            return createFilterPredicateForStringComparison(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageNameIn(@NotNull ExpressionFilterParser.MessageNameInContext ctx) {
            String fieldName = ctx.getStart().getText();
            String[] strings = extractStrings(ctx.stringList().string());
            return createFilterPredicateForList(AisPacketFilters.class, fieldName, strings);
        }

        @Override
        public Predicate<AisPacket> visitMessageCallsign(@NotNull ExpressionFilterParser.MessageCallsignContext ctx) {
            return createFilterPredicateForStringComparison(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageCallsignIn(@NotNull ExpressionFilterParser.MessageCallsignInContext ctx) {
            String fieldName = ctx.getStart().getText();
            String[] strings = extractStrings(ctx.stringList().string());
            return createFilterPredicateForList(AisPacketFilters.class, fieldName, strings);
        }

        @Override
        public Predicate<AisPacket> visitMessageSpeedOverGround(@NotNull ExpressionFilterParser.MessageSpeedOverGroundContext ctx) {
            return createFilterPredicateForNumberComparison(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageSpeedOverGroundIn(@NotNull ExpressionFilterParser.MessageSpeedOverGroundInContext ctx) {
            return createFilterPredicateForNumberRange(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageCourseOverGround(@NotNull ExpressionFilterParser.MessageCourseOverGroundContext ctx) {
            return createFilterPredicateForNumberComparison(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageCourseOverGroundIn(@NotNull ExpressionFilterParser.MessageCourseOverGroundInContext ctx) {
            return createFilterPredicateForNumberRange(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageLatitude(@NotNull ExpressionFilterParser.MessageLatitudeContext ctx) {
            return createFilterPredicateForNumberComparison(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageLatitudeIn(@NotNull ExpressionFilterParser.MessageLatitudeInContext ctx) {
            return createFilterPredicateForNumberRange(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageLongitude(@NotNull ExpressionFilterParser.MessageLongitudeContext ctx) {
            return createFilterPredicateForNumberComparison(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageLongitudeIn(@NotNull ExpressionFilterParser.MessageLongitudeInContext ctx) {
            return createFilterPredicateForNumberRange(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageTrueHeading(@NotNull ExpressionFilterParser.MessageTrueHeadingContext ctx) {
            return createFilterPredicateForIntComparison(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageTrueHeadingIn(@NotNull ExpressionFilterParser.MessageTrueHeadingInContext ctx) {
            return createFilterPredicateForIntRangeOrIntList(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageDraught(@NotNull ExpressionFilterParser.MessageDraughtContext ctx) {
            return createFilterPredicateForNumberComparison(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageDraughtIn(@NotNull ExpressionFilterParser.MessageDraughtInContext ctx) {
            return createFilterPredicateForNumberRange(AisPacketFilters.class, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessagePositionInside(@NotNull ExpressionFilterParser.MessagePositionInsideContext ctx) {
            Predicate<AisPacket> filter = null;
            Area area = null;

            if (ctx.bbox() != null) {
                List<ExpressionFilterParser.NumberContext> params = ctx.bbox().number();
                float latitude1 = Float.valueOf(params.get(0).getText());
                float longitude1 = Float.valueOf(params.get(1).getText());
                float latitude2 = Float.valueOf(params.get(2).getText());
                float longitude2 = Float.valueOf(params.get(3).getText());
                Position corner1 = Position.create(latitude1, longitude1);
                Position corner2 = Position.create(latitude2, longitude2);
                area = BoundingBox.create(corner1, corner2, CoordinateSystem.CARTESIAN);
            } else if (ctx.circle() != null) {
                List<ExpressionFilterParser.NumberContext> params = ctx.circle().number();
                float latitude = Float.valueOf(params.get(0).getText());
                float longitude = Float.valueOf(params.get(1).getText());
                float radius = Float.valueOf(params.get(2).getText());
                area = new Circle(latitude, longitude, radius, CoordinateSystem.CARTESIAN);
            }

            if (area != null) {
                filter = AisPacketFilters.filterOnMessagePositionWithin(area);
            }

            return filter;
        }
    }
}
