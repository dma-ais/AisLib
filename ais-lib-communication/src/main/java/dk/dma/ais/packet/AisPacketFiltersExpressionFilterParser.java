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
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterParser.SourceBasestationContext;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Calendar;

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
        // Visitors related to source
        //

        @Override
        public Predicate<AisPacket> visitSourceIdIn(@NotNull ExpressionFilterParser.SourceIdInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitSourceBasestation(@NotNull SourceBasestationContext ctx) {
            return createFilterPredicateForComparison(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitSourceBasestationIn(@NotNull ExpressionFilterParser.SourceBasestationInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }
        
        @Override
        public Predicate<AisPacket> visitSourceCountryIn(@NotNull ExpressionFilterParser.SourceCountryInContext ctx) {
            return createFilterPredicateForListOfCountry(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitSourceTypeIn(@NotNull ExpressionFilterParser.SourceTypeInContext ctx) {
            return createFilterPredicateForListOfSourceType(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitSourceRegionIn(@NotNull ExpressionFilterParser.SourceRegionInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }

        //
        // Visitors related to message contents
        //

        @Override
        public Predicate<AisPacket> visitMessageTimeYear(@NotNull ExpressionFilterParser.MessageTimeYearContext ctx) {
            return createFilterPredicateForComparisonOfMessageReceiveTime(AisPacketFilters.class, null, ctx, Calendar.YEAR);
        }

        @Override
        public Predicate<AisPacket> visitMessageTimeYearIn(@NotNull ExpressionFilterParser.MessageTimeYearInContext ctx) {
            return createFilterPredicateForRangeOrListOfMessageReceiveTime(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageTimeMonth(@NotNull ExpressionFilterParser.MessageTimeMonthContext ctx) {
            return createFilterPredicateForComparisonOfMonth(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageTimeMonthIn(@NotNull ExpressionFilterParser.MessageTimeMonthInContext ctx) {
            return createFilterPredicateForRangeOrListOfMonth(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageTimeDay(@NotNull ExpressionFilterParser.MessageTimeDayContext ctx) {
            return createFilterPredicateForComparisonOfMessageReceiveTime(AisPacketFilters.class, null, ctx, Calendar.DAY_OF_MONTH);
        }

        @Override
        public Predicate<AisPacket> visitMessageTimeDayIn(@NotNull ExpressionFilterParser.MessageTimeDayInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageTimeWeekday(@NotNull ExpressionFilterParser.MessageTimeWeekdayContext ctx) {
            return createFilterPredicateForComparisonOfWeekday(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageTimeWeekdayIn(@NotNull ExpressionFilterParser.MessageTimeWeekdayInContext ctx) {
            return createFilterPredicateForRangeOrListOfWeekday(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageTimeHour(@NotNull ExpressionFilterParser.MessageTimeHourContext ctx) {
            return createFilterPredicateForComparisonOfMessageReceiveTime(AisPacketFilters.class, null, ctx, Calendar.HOUR_OF_DAY);
        }

        @Override
        public Predicate<AisPacket> visitMessageTimeHourIn(@NotNull ExpressionFilterParser.MessageTimeHourInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageTimeMinute(@NotNull ExpressionFilterParser.MessageTimeMinuteContext ctx) {
            return createFilterPredicateForComparisonOfMessageReceiveTime(AisPacketFilters.class, null, ctx, Calendar.MINUTE);
        }

        @Override
        public Predicate<AisPacket> visitMessageTimeMinuteIn(@NotNull ExpressionFilterParser.MessageTimeMinuteInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }

        // ---

        @Override
        public Predicate<AisPacket> visitMessageId(@NotNull ExpressionFilterParser.MessageIdContext ctx) {
            return createFilterPredicateForComparison(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageIdIn(@NotNull ExpressionFilterParser.MessageIdInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageMmsi(@NotNull ExpressionFilterParser.MessageMmsiContext ctx) {
            return createFilterPredicateForComparison(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageMmsiIn(@NotNull ExpressionFilterParser.MessageMmsiInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageImo(@NotNull ExpressionFilterParser.MessageImoContext ctx) {
            return createFilterPredicateForComparison(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageImoIn(@NotNull ExpressionFilterParser.MessageImoInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageShiptype(@NotNull ExpressionFilterParser.MessageShiptypeContext ctx) {
            return createFilterPredicateForComparisonOfShipType(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageShiptypeIn(@NotNull ExpressionFilterParser.MessageShiptypeInContext ctx) {
            return createFilterPredicateForListOfShipType(AisPacketFilters.class, null, ctx);
        }

        
        @Override
        public Predicate<AisPacket> visitMessageCountryIn(@NotNull ExpressionFilterParser.MessageCountryInContext ctx) {
            return createFilterPredicateForListOfCountry(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageNavigationalStatus(@NotNull ExpressionFilterParser.MessageNavigationalStatusContext ctx) {
            return createFilterPredicateForComparisonOfNavigationalStatus(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageNavigationalStatusIn(@NotNull ExpressionFilterParser.MessageNavigationalStatusInContext ctx) {
            return createFilterPredicateForListOfNavigationalStatus(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageName(@NotNull ExpressionFilterParser.MessageNameContext ctx) {
            return createFilterPredicateForStringComparison(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageNameIn(@NotNull ExpressionFilterParser.MessageNameInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageCallsign(@NotNull ExpressionFilterParser.MessageCallsignContext ctx) {
            return createFilterPredicateForStringComparison(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageCallsignIn(@NotNull ExpressionFilterParser.MessageCallsignInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageSpeedOverGround(@NotNull ExpressionFilterParser.MessageSpeedOverGroundContext ctx) {
            return createFilterPredicateForComparison(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageSpeedOverGroundIn(@NotNull ExpressionFilterParser.MessageSpeedOverGroundInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageCourseOverGround(@NotNull ExpressionFilterParser.MessageCourseOverGroundContext ctx) {
            return createFilterPredicateForComparison(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageCourseOverGroundIn(@NotNull ExpressionFilterParser.MessageCourseOverGroundInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageLatitude(@NotNull ExpressionFilterParser.MessageLatitudeContext ctx) {
            return createFilterPredicateForComparison(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageLatitudeIn(@NotNull ExpressionFilterParser.MessageLatitudeInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageLongitude(@NotNull ExpressionFilterParser.MessageLongitudeContext ctx) {
            return createFilterPredicateForComparison(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageLongitudeIn(@NotNull ExpressionFilterParser.MessageLongitudeInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageTrueHeading(@NotNull ExpressionFilterParser.MessageTrueHeadingContext ctx) {
            return createFilterPredicateForComparison(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageTrueHeadingIn(@NotNull ExpressionFilterParser.MessageTrueHeadingInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageDraught(@NotNull ExpressionFilterParser.MessageDraughtContext ctx) {
            return createFilterPredicateForComparison(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessageDraughtIn(@NotNull ExpressionFilterParser.MessageDraughtInContext ctx) {
            return createFilterPredicateForRangeOrList(AisPacketFilters.class, null, ctx);
        }

        @Override
        public Predicate<AisPacket> visitMessagePositionInside(@NotNull ExpressionFilterParser.MessagePositionInsideContext ctx) {
            return createFilterPredicateForPositionWithin(AisPacketFilters.class, null, ctx);
        }

        //
        // Visitors related to target
        //

        @Override
        public Predicate<AisPacket> visitTargetImo(@NotNull ExpressionFilterParser.TargetImoContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForComparison(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetImoIn(@NotNull ExpressionFilterParser.TargetImoInContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForRangeOrList(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetShiptype(@NotNull ExpressionFilterParser.TargetShiptypeContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForComparisonOfShipType(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetShiptypeIn(@NotNull ExpressionFilterParser.TargetShiptypeInContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForListOfShipType(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }
        
        @Override
        public Predicate<AisPacket> visitTargetCountryIn(@NotNull ExpressionFilterParser.TargetCountryInContext ctx) {
            return createFilterPredicateForListOfCountry(AisPacketFilters.class, null, ctx);
        }


        @Override
        public Predicate<AisPacket> visitTargetNavigationalStatus(@NotNull ExpressionFilterParser.TargetNavigationalStatusContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForComparisonOfNavigationalStatus(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetNavigationalStatusIn(@NotNull ExpressionFilterParser.TargetNavigationalStatusInContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForListOfNavigationalStatus(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetSpeedOverGround(@NotNull ExpressionFilterParser.TargetSpeedOverGroundContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForComparison(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetSpeedOverGroundIn(@NotNull ExpressionFilterParser.TargetSpeedOverGroundInContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForRangeOrList(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetCourseOverGround(@NotNull ExpressionFilterParser.TargetCourseOverGroundContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForComparison(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetCourseOverGroundIn(@NotNull ExpressionFilterParser.TargetCourseOverGroundInContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForRangeOrList(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetTrueHeading(@NotNull ExpressionFilterParser.TargetTrueHeadingContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForComparison(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetTrueHeadingIn(@NotNull ExpressionFilterParser.TargetTrueHeadingInContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForRangeOrList(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetDraught(@NotNull ExpressionFilterParser.TargetDraughtContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForComparison(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetDraughtIn(@NotNull ExpressionFilterParser.TargetDraughtInContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForRangeOrList(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetLatitude(@NotNull ExpressionFilterParser.TargetLatitudeContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForComparison(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetLatitudeIn(@NotNull ExpressionFilterParser.TargetLatitudeInContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForRangeOrList(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetLongitude(@NotNull ExpressionFilterParser.TargetLongitudeContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForComparison(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetLongitudeIn(@NotNull ExpressionFilterParser.TargetLongitudeInContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForRangeOrList(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetName(@NotNull ExpressionFilterParser.TargetNameContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForStringComparison(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetNameIn(@NotNull ExpressionFilterParser.TargetNameInContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForRangeOrList(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetCallsign(@NotNull ExpressionFilterParser.TargetCallsignContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForStringComparison(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetCallsignIn(@NotNull ExpressionFilterParser.TargetCallsignInContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForRangeOrList(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        @Override
        public Predicate<AisPacket> visitTargetPositionInside(@NotNull ExpressionFilterParser.TargetPositionInsideContext ctx) {
            setupStatefulFilterPredicateFactory();
            return createFilterPredicateForPositionWithin(statefulFilterPredicateFactory.getClass(), statefulFilterPredicateFactory, ctx);
        }

        private FilterPredicateFactory statefulFilterPredicateFactory;

        private void setupStatefulFilterPredicateFactory() {
            if (statefulFilterPredicateFactory == null) {
                statefulFilterPredicateFactory = new AisPacketFiltersStateful();
            }
        }
    }
}
