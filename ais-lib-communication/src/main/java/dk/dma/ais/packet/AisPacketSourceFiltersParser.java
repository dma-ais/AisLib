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

import dk.dma.enav.util.function.Predicate;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterBaseVisitor;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterLexer;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterParser;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterParser.OrAndContext;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterParser.ParensContext;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterParser.ProgContext;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import static java.util.Objects.requireNonNull;

/**
 * 
 * @author Kasper Nielsen
 */
class AisPacketSourceFiltersParser {
    static Predicate<AisPacketSource> parseSourceFilter(String filter) {
        ANTLRInputStream input = new ANTLRInputStream(requireNonNull(filter));
        ExpressionFilterLexer lexer = new ExpressionFilterLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExpressionFilterParser parser = new ExpressionFilterParser(tokens);

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

    static class SourceFilterToPredicateVisitor extends ExpressionFilterBaseVisitor<Predicate<AisPacketSource>> {

        @Override
        public Predicate<AisPacketSource> visitOrAnd(OrAndContext ctx) {
            return ctx.op.getType() == ExpressionFilterParser.AND ? visit(ctx.filterExpression(0)).and(visit(ctx.filterExpression(1))) : visit(
                    ctx.filterExpression(0)).or(visit(ctx.filterExpression(1)));
        }

        @Override
        public Predicate<AisPacketSource> visitParens(ParensContext ctx) {
            final Predicate<AisPacketSource> p = visit(ctx.filterExpression());
            return new Predicate<AisPacketSource>() {
                public boolean test(AisPacketSource element) {
                    return p.test(element);
                }

                public String toString() {
                    return "(" + p.toString() + ")";
                }
            };
        }
            /*
        @Override
        public Predicate<AisPacketSource> visitSourceBasestation(SourceBasestationContext ctx) {
            return checkNegate(ctx.equalityTest(), filterOnSourceBaseStationInList(readArrays(ctx.valueList().value())));
        }

        @Override
        public Predicate<AisPacketSource> visitSourceCountry(SourceCountryContext ctx) {
            List<Country> countries = Country.findAllByCode(readArrays(ctx.valueList().value()));
            return checkNegate(ctx.equalityTest(),
                    filterOnSourceCountry(countries.toArray(new Country[countries.size()])));
        }

        @Override
        public Predicate<AisPacketSource> visitSourceId(final SourceIdContext ctx) {
            return checkNegate(ctx.equalityTest(), filterOnSourceId(readArrays(ctx.valueList().value())));
        }

        @Override
        public Predicate<AisPacketSource> visitSourceRegion(SourceRegionContext ctx) {
            return checkNegate(ctx.equalityTest(), filterOnSourceRegion(readArrays(ctx.valueList().value())));
        }

        @Override
        public Predicate<AisPacketSource> visitSourceType(SourceTypeContext ctx) {
            return checkNegate(ctx.equalityTest(), filterOnSourceTypeInList(SourceType.fromString(ctx.valueList().getText())));
        }



        public Predicate<AisPacketSource> checkNegate(EqualityTestContext context, Predicate<AisPacketSource> p) {
            String text = context.getChild(0).getText();
            return text.equals("!=") ? p.negate() : p;
        }

        private static String[] readArrays(Iterable<ExpressionFilterParser.ValueContext> iter) {
            ArrayList<String> list = new ArrayList<>();
            for (ExpressionFilterParser.ValueContext vc : iter) {
                list.add(vc.getText());
            }
            return list.toArray(new String[list.size()]);
        }           */
    }
}
