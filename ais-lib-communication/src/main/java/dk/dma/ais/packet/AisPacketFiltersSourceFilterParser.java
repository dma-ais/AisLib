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

import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceBaseStation;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceCountry;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceId;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceRegion;
import static dk.dma.ais.packet.AisPacketFilters.filterOnSourceType;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.TerminalNode;

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
        return tree.expr().accept(new SourceFilterToPredicateVisitor());
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
            // System.err.println("line " + line + ":" + charPositionInLine + " at " + offendingSymbol + ": " + msg);
        }
    }

    static class SourceFilterToPredicateVisitor extends SourceFilterBaseVisitor<Predicate<AisPacket>> {

        @Override
        public Predicate<AisPacket> visitOrAnd(OrAndContext ctx) {
            return ctx.op.getType() == SourceFilterParser.AND ? visit(ctx.expr(0)).and(visit(ctx.expr(1))) : visit(
                    ctx.expr(0)).or(visit(ctx.expr(1)));
        }

        @Override
        public Predicate<AisPacket> visitParens(ParensContext ctx) {
            final Predicate<AisPacket> p = visit(ctx.expr());
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
            return checkNegate(ctx.equalityTest(), filterOnSourceBaseStation(readArrays(ctx.idList().ID())));
        }

        @Override
        public Predicate<AisPacket> visitSourceCountry(SourceCountryContext ctx) {
            List<Country> countries = Country.findAllByCode(readArrays(ctx.idList().ID()));
            return checkNegate(ctx.equalityTest(),
                    filterOnSourceCountry(countries.toArray(new Country[countries.size()])));
        }

        @Override
        public Predicate<AisPacket> visitSourceId(final SourceIdContext ctx) {
            return checkNegate(ctx.equalityTest(), filterOnSourceId(readArrays(ctx.idList().ID())));
        }

        @Override
        public Predicate<AisPacket> visitSourceRegion(SourceRegionContext ctx) {
            return checkNegate(ctx.equalityTest(), filterOnSourceRegion(readArrays(ctx.idList().ID())));
        }

        @Override
        public Predicate<AisPacket> visitSourceType(SourceTypeContext ctx) {
            return checkNegate(ctx.equalityTest(), filterOnSourceType(SourceType.fromString(ctx.ID().getText())));
        }

        public Predicate<AisPacket> checkNegate(EqualityTestContext context, Predicate<AisPacket> p) {
            String text = context.getChild(0).getText();
            return text.equals("!=") ? p.negate() : p;
        }

        private static String[] readArrays(Iterable<TerminalNode> iter) {
            ArrayList<String> list = new ArrayList<>();
            for (TerminalNode t : iter) {
                list.add(t.getText());
            }
            return list.toArray(new String[list.size()]);
        }
    }
}
