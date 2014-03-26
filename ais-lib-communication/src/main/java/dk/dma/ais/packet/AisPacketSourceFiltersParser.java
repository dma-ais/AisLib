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

import dk.dma.enav.model.Country;
import dk.dma.enav.util.function.Predicate;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterBaseVisitor;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterParser;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterParser.OrAndContext;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterParser.ParensContext;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * @author Kasper Nielsen
 */
class AisPacketSourceFiltersParser extends ExpressionFilterParserBase {
    static Predicate<AisPacketSource> parseSourceFilter(String filter) {
        return createFilterContext(filter).filterExpression().accept(new SourceFilterToPredicateVisitor());
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

        // 's.id' (in|notin) stringList
        @Override
        public Predicate<AisPacketSource> visitSourceIdIn(final ExpressionFilterParser.SourceIdInContext ctx) {
            String fieldName = ctx.getStart().getText();
            String[] strings = extractStrings(ctx.stringList().string());
            Predicate<AisPacketSource> filter = createFilterPredicateForList(AisPacketSourceFilters.class, fieldName, strings);
            return checkNegate(ctx, filter);
        }

        // 's.bs' compareTo INT
        @Override
        public Predicate<AisPacketSource> visitSourceBasestation(@NotNull ExpressionFilterParser.SourceBasestationContext ctx) {
            return createFilterPredicateForIntComparison(AisPacketSourceFilters.class, ctx);
        }

        // 's.bs' (in|notin) (intRange|intList)
        @Override
        public Predicate<AisPacketSource> visitSourceBasestationIn(@NotNull ExpressionFilterParser.SourceBasestationInContext ctx) {
            return createFilterPredicateForIntRangeOrIntList(AisPacketSourceFilters.class, ctx);
        }

        // 's.country' (in|notin) stringList
        @Override
        public Predicate<AisPacketSource> visitSourceCountryIn(@NotNull ExpressionFilterParser.SourceCountryInContext ctx) {
            String fieldName = ctx.getStart().getText();
            String[] strings = extractStrings(ctx.stringList().string());
            Country[] countries = getCountries(strings);
            Predicate<AisPacketSource> filter = createFilterPredicateForList(AisPacketSourceFilters.class, fieldName, countries);
            return checkNegate(ctx, filter);
        }

        // 's.type' (in|notin) stringList
        @Override
        public Predicate<AisPacketSource> visitSourceTypeIn(@NotNull ExpressionFilterParser.SourceTypeInContext ctx) {
            String fieldName = ctx.getStart().getText();
            String[] strings = extractStrings(ctx.stringList().string());
            AisPacketTags.SourceType[] sourceTypes = getSourceTypes(strings);
            Predicate<AisPacketSource> filter = createFilterPredicateForList(AisPacketSourceFilters.class, fieldName, sourceTypes);
            return checkNegate(ctx, filter);
        }

        // 's.region' (in|notin) stringList
        @Override
        public Predicate<AisPacketSource> visitSourceRegionIn(@NotNull ExpressionFilterParser.SourceRegionInContext ctx) {
            String fieldName = ctx.getStart().getText();
            String[] strings = extractStrings(ctx.stringList().string());
            Predicate<AisPacketSource> filter = createFilterPredicateForList(AisPacketSourceFilters.class, fieldName, strings);
            return checkNegate(ctx, filter);
        }
    }
}
