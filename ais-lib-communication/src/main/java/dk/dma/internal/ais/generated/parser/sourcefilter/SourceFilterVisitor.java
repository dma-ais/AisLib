// Generated from SourceFilter.g4 by ANTLR 4.2
package dk.dma.internal.ais.generated.parser.sourcefilter;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SourceFilterParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SourceFilterVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceId(@NotNull SourceFilterParser.SourceIdContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageTrueHeading}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTrueHeading(@NotNull SourceFilterParser.MessageTrueHeadingContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#parens}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(@NotNull SourceFilterParser.ParensContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageImoInRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageImoInRange(@NotNull SourceFilterParser.MessageImoInRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageNameInList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageNameInList(@NotNull SourceFilterParser.MessageNameInListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#intList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntList(@NotNull SourceFilterParser.IntListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageNavigationalStatus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageNavigationalStatus(@NotNull SourceFilterParser.MessageNavigationalStatusContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#compareTo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompareTo(@NotNull SourceFilterParser.CompareToContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceBasestation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceBasestation(@NotNull SourceFilterParser.SourceBasestationContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(@NotNull SourceFilterParser.NumberContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageSpeedOverGroundInRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageSpeedOverGroundInRange(@NotNull SourceFilterParser.MessageSpeedOverGroundInRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageNavigationalStatusLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageNavigationalStatusLabel(@NotNull SourceFilterParser.MessageNavigationalStatusLabelContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#equalityTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityTest(@NotNull SourceFilterParser.EqualityTestContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#inListOrRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInListOrRange(@NotNull SourceFilterParser.InListOrRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageShiptypeInList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageShiptypeInList(@NotNull SourceFilterParser.MessageShiptypeInListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageMmsiInList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageMmsiInList(@NotNull SourceFilterParser.MessageMmsiInListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageLongitudeInRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageLongitudeInRange(@NotNull SourceFilterParser.MessageLongitudeInRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageId(@NotNull SourceFilterParser.MessageIdContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageShiptypeInRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageShiptypeInRange(@NotNull SourceFilterParser.MessageShiptypeInRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageCallsign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageCallsign(@NotNull SourceFilterParser.MessageCallsignContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageSpeedOverGround}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageSpeedOverGround(@NotNull SourceFilterParser.MessageSpeedOverGroundContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageShiptypeLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageShiptypeLabel(@NotNull SourceFilterParser.MessageShiptypeLabelContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageNavigationalStatusInList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageNavigationalStatusInList(@NotNull SourceFilterParser.MessageNavigationalStatusInListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageDraught}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageDraught(@NotNull SourceFilterParser.MessageDraughtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceType(@NotNull SourceFilterParser.SourceTypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageImoInList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageImoInList(@NotNull SourceFilterParser.MessageImoInListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageIdInList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageIdInList(@NotNull SourceFilterParser.MessageIdInListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageTrueHeadingInRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTrueHeadingInRange(@NotNull SourceFilterParser.MessageTrueHeadingInRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageNavigationalStatusInRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageNavigationalStatusInRange(@NotNull SourceFilterParser.MessageNavigationalStatusInRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageDraughtInRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageDraughtInRange(@NotNull SourceFilterParser.MessageDraughtInRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#complies}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplies(@NotNull SourceFilterParser.CompliesContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageName(@NotNull SourceFilterParser.MessageNameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceCountry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceCountry(@NotNull SourceFilterParser.SourceCountryContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageLatitude}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageLatitude(@NotNull SourceFilterParser.MessageLatitudeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(@NotNull SourceFilterParser.StringContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageIdInRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageIdInRange(@NotNull SourceFilterParser.MessageIdInRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageCourseOverGround}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageCourseOverGround(@NotNull SourceFilterParser.MessageCourseOverGroundContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#numberRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberRange(@NotNull SourceFilterParser.NumberRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#OrAnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrAnd(@NotNull SourceFilterParser.OrAndContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(@NotNull SourceFilterParser.ValueContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#AisMessagetype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAisMessagetype(@NotNull SourceFilterParser.AisMessagetypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#intRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntRange(@NotNull SourceFilterParser.IntRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageCallsignInList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageCallsignInList(@NotNull SourceFilterParser.MessageCallsignInListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageCourseOverGroundInRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageCourseOverGroundInRange(@NotNull SourceFilterParser.MessageCourseOverGroundInRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(@NotNull SourceFilterParser.ProgContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageLatitudeInRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageLatitudeInRange(@NotNull SourceFilterParser.MessageLatitudeInRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageMmsi}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageMmsi(@NotNull SourceFilterParser.MessageMmsiContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageLongitude}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageLongitude(@NotNull SourceFilterParser.MessageLongitudeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageShiptypeInLabelList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageShiptypeInLabelList(@NotNull SourceFilterParser.MessageShiptypeInLabelListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#stringList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringList(@NotNull SourceFilterParser.StringListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageNavigationalStatusInLabelList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageNavigationalStatusInLabelList(@NotNull SourceFilterParser.MessageNavigationalStatusInLabelListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#valueList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueList(@NotNull SourceFilterParser.ValueListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceRegion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceRegion(@NotNull SourceFilterParser.SourceRegionContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageShiptype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageShiptype(@NotNull SourceFilterParser.MessageShiptypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageImo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageImo(@NotNull SourceFilterParser.MessageImoContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageMmsiInRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageMmsiInRange(@NotNull SourceFilterParser.MessageMmsiInRangeContext ctx);
}