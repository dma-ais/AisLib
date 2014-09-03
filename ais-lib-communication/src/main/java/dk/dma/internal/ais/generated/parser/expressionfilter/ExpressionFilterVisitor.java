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

// Generated from ExpressionFilter.g4 by ANTLR 4.2
package dk.dma.internal.ais.generated.parser.expressionfilter;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ExpressionFilterParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ExpressionFilterVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageTrueHeading}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTrueHeading(@NotNull ExpressionFilterParser.MessageTrueHeadingContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageTrueHeadingIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTrueHeadingIn(@NotNull ExpressionFilterParser.MessageTrueHeadingInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#parens}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(@NotNull ExpressionFilterParser.ParensContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageTimeMinute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTimeMinute(@NotNull ExpressionFilterParser.MessageTimeMinuteContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageTimeYear}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTimeYear(@NotNull ExpressionFilterParser.MessageTimeYearContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#intList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntList(@NotNull ExpressionFilterParser.IntListContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#bbox}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBbox(@NotNull ExpressionFilterParser.BboxContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageNavigationalStatus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageNavigationalStatus(@NotNull ExpressionFilterParser.MessageNavigationalStatusContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#compareTo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompareTo(@NotNull ExpressionFilterParser.CompareToContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageTimeMinuteIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTimeMinuteIn(@NotNull ExpressionFilterParser.MessageTimeMinuteInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#sourceBasestation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceBasestation(@NotNull ExpressionFilterParser.SourceBasestationContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetNavigationalStatusIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetNavigationalStatusIn(@NotNull ExpressionFilterParser.TargetNavigationalStatusInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetLatitudeIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetLatitudeIn(@NotNull ExpressionFilterParser.TargetLatitudeInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetCountryIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetCountryIn(@NotNull ExpressionFilterParser.TargetCountryInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(@NotNull ExpressionFilterParser.NumberContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetCallsign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetCallsign(@NotNull ExpressionFilterParser.TargetCallsignContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageTimeMonthIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTimeMonthIn(@NotNull ExpressionFilterParser.MessageTimeMonthInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#sourceBasestationIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceBasestationIn(@NotNull ExpressionFilterParser.SourceBasestationInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageImoIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageImoIn(@NotNull ExpressionFilterParser.MessageImoInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetImo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetImo(@NotNull ExpressionFilterParser.TargetImoContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageTimeWeekday}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTimeWeekday(@NotNull ExpressionFilterParser.MessageTimeWeekdayContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageCountryIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageCountryIn(@NotNull ExpressionFilterParser.MessageCountryInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetSpeedOverGround}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetSpeedOverGround(@NotNull ExpressionFilterParser.TargetSpeedOverGroundContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetTrueHeading}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetTrueHeading(@NotNull ExpressionFilterParser.TargetTrueHeadingContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetTrueHeadingIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetTrueHeadingIn(@NotNull ExpressionFilterParser.TargetTrueHeadingInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#in}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIn(@NotNull ExpressionFilterParser.InContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageLatitudeIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageLatitudeIn(@NotNull ExpressionFilterParser.MessageLatitudeInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageId(@NotNull ExpressionFilterParser.MessageIdContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageCallsign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageCallsign(@NotNull ExpressionFilterParser.MessageCallsignContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageSpeedOverGround}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageSpeedOverGround(@NotNull ExpressionFilterParser.MessageSpeedOverGroundContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetNameIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetNameIn(@NotNull ExpressionFilterParser.TargetNameInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageCourseOverGroundIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageCourseOverGroundIn(@NotNull ExpressionFilterParser.MessageCourseOverGroundInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageDraught}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageDraught(@NotNull ExpressionFilterParser.MessageDraughtContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageMmsiIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageMmsiIn(@NotNull ExpressionFilterParser.MessageMmsiInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageTimeHour}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTimeHour(@NotNull ExpressionFilterParser.MessageTimeHourContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageTimeWeekdayIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTimeWeekdayIn(@NotNull ExpressionFilterParser.MessageTimeWeekdayInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#sourceCountryIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceCountryIn(@NotNull ExpressionFilterParser.SourceCountryInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageIdIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageIdIn(@NotNull ExpressionFilterParser.MessageIdInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetDraught}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetDraught(@NotNull ExpressionFilterParser.TargetDraughtContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetImoIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetImoIn(@NotNull ExpressionFilterParser.TargetImoInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#circle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCircle(@NotNull ExpressionFilterParser.CircleContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageTimeYearIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTimeYearIn(@NotNull ExpressionFilterParser.MessageTimeYearInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageName(@NotNull ExpressionFilterParser.MessageNameContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageTimeHourIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTimeHourIn(@NotNull ExpressionFilterParser.MessageTimeHourInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetNavigationalStatus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetNavigationalStatus(@NotNull ExpressionFilterParser.TargetNavigationalStatusContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetName(@NotNull ExpressionFilterParser.TargetNameContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageLatitude}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageLatitude(@NotNull ExpressionFilterParser.MessageLatitudeContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(@NotNull ExpressionFilterParser.StringContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageDraughtIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageDraughtIn(@NotNull ExpressionFilterParser.MessageDraughtInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetCourseOverGroundIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetCourseOverGroundIn(@NotNull ExpressionFilterParser.TargetCourseOverGroundInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageTimeDay}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTimeDay(@NotNull ExpressionFilterParser.MessageTimeDayContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageCourseOverGround}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageCourseOverGround(@NotNull ExpressionFilterParser.MessageCourseOverGroundContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messagePositionInside}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessagePositionInside(@NotNull ExpressionFilterParser.MessagePositionInsideContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageShiptypeIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageShiptypeIn(@NotNull ExpressionFilterParser.MessageShiptypeInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#numberRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberRange(@NotNull ExpressionFilterParser.NumberRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetCourseOverGround}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetCourseOverGround(@NotNull ExpressionFilterParser.TargetCourseOverGroundContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetPositionInside}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetPositionInside(@NotNull ExpressionFilterParser.TargetPositionInsideContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageNavigationalStatusIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageNavigationalStatusIn(@NotNull ExpressionFilterParser.MessageNavigationalStatusInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetSpeedOverGroundIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetSpeedOverGroundIn(@NotNull ExpressionFilterParser.TargetSpeedOverGroundInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetDraughtIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetDraughtIn(@NotNull ExpressionFilterParser.TargetDraughtInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#OrAnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrAnd(@NotNull ExpressionFilterParser.OrAndContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetLatitude}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetLatitude(@NotNull ExpressionFilterParser.TargetLatitudeContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetShiptypeIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetShiptypeIn(@NotNull ExpressionFilterParser.TargetShiptypeInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageTimeDayIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTimeDayIn(@NotNull ExpressionFilterParser.MessageTimeDayInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#AisMessagetype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAisMessagetype(@NotNull ExpressionFilterParser.AisMessagetypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#sourceIdIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceIdIn(@NotNull ExpressionFilterParser.SourceIdInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#sourceRegionIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceRegionIn(@NotNull ExpressionFilterParser.SourceRegionInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageCallsignIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageCallsignIn(@NotNull ExpressionFilterParser.MessageCallsignInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#intRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntRange(@NotNull ExpressionFilterParser.IntRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageLongitudeIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageLongitudeIn(@NotNull ExpressionFilterParser.MessageLongitudeInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#sourceTypeIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceTypeIn(@NotNull ExpressionFilterParser.SourceTypeInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageNameIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageNameIn(@NotNull ExpressionFilterParser.MessageNameInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageSpeedOverGroundIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageSpeedOverGroundIn(@NotNull ExpressionFilterParser.MessageSpeedOverGroundInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageMmsi}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageMmsi(@NotNull ExpressionFilterParser.MessageMmsiContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilter(@NotNull ExpressionFilterParser.FilterContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetShiptype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetShiptype(@NotNull ExpressionFilterParser.TargetShiptypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageLongitude}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageLongitude(@NotNull ExpressionFilterParser.MessageLongitudeContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageTimeMonth}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTimeMonth(@NotNull ExpressionFilterParser.MessageTimeMonthContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetLongitude}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetLongitude(@NotNull ExpressionFilterParser.TargetLongitudeContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#stringList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringList(@NotNull ExpressionFilterParser.StringListContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#notin}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotin(@NotNull ExpressionFilterParser.NotinContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetCallsignIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetCallsignIn(@NotNull ExpressionFilterParser.TargetCallsignInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageShiptype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageShiptype(@NotNull ExpressionFilterParser.MessageShiptypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageImo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageImo(@NotNull ExpressionFilterParser.MessageImoContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#targetLongitudeIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetLongitudeIn(@NotNull ExpressionFilterParser.TargetLongitudeInContext ctx);
}