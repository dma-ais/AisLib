/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.dma.ais.packet;

import java.util.function.Predicate;
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

        @Override
        public Predicate<AisPacketSource> visitSourceIdIn(final ExpressionFilterParser.SourceIdInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketSourceFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacketSource> visitSourceBasestation(@NotNull ExpressionFilterParser.SourceBasestationContext ctx) {
            return createFilterPredicateForComparison(AisPacketSourceFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacketSource> visitSourceBasestationIn(@NotNull ExpressionFilterParser.SourceBasestationInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketSourceFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacketSource> visitSourceCountryIn(@NotNull ExpressionFilterParser.SourceCountryInContext ctx) {
            return createFilterPredicateForListOfCountry(AisPacketSourceFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacketSource> visitSourceTypeIn(@NotNull ExpressionFilterParser.SourceTypeInContext ctx) {
            return createFilterPredicateForListOfSourceType(AisPacketSourceFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacketSource> visitSourceRegionIn(@NotNull ExpressionFilterParser.SourceRegionInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketSourceFilters.class, null, ctx);
        }
    }
}
