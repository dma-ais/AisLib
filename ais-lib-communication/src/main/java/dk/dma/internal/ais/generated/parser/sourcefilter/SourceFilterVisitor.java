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
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceTypeInStringList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceTypeInStringList(@NotNull SourceFilterParser.SourceTypeInStringListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#intList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntList(@NotNull SourceFilterParser.IntListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageCallsignInStringList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageCallsignInStringList(@NotNull SourceFilterParser.MessageCallsignInStringListContext ctx);

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
	 * Visit a parse tree produced by {@link SourceFilterParser#messageDraughtInNumberRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageDraughtInNumberRange(@NotNull SourceFilterParser.MessageDraughtInNumberRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(@NotNull SourceFilterParser.NumberContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageNameInStringList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageNameInStringList(@NotNull SourceFilterParser.MessageNameInStringListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageShiptypeInIntRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageShiptypeInIntRange(@NotNull SourceFilterParser.MessageShiptypeInIntRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#in}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIn(@NotNull SourceFilterParser.InContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageNavigationalStatusInIntList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageNavigationalStatusInIntList(@NotNull SourceFilterParser.MessageNavigationalStatusInIntListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageId(@NotNull SourceFilterParser.MessageIdContext ctx);

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
	 * Visit a parse tree produced by {@link SourceFilterParser#messageLatitudeInNumberRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageLatitudeInNumberRange(@NotNull SourceFilterParser.MessageLatitudeInNumberRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageLongitudeInNumberRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageLongitudeInNumberRange(@NotNull SourceFilterParser.MessageLongitudeInNumberRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageNavigationalStatusInStringList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageNavigationalStatusInStringList(@NotNull SourceFilterParser.MessageNavigationalStatusInStringListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageDraught}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageDraught(@NotNull SourceFilterParser.MessageDraughtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageMmsiInIntRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageMmsiInIntRange(@NotNull SourceFilterParser.MessageMmsiInIntRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceCountryInStringList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceCountryInStringList(@NotNull SourceFilterParser.SourceCountryInStringListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageMmsiInIntList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageMmsiInIntList(@NotNull SourceFilterParser.MessageMmsiInIntListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageName(@NotNull SourceFilterParser.MessageNameContext ctx);

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
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceIdInStringList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceIdInStringList(@NotNull SourceFilterParser.SourceIdInStringListContext ctx);

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
	 * Visit a parse tree produced by {@link SourceFilterParser#messageSpeedOverGroundInNumberRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageSpeedOverGroundInNumberRange(@NotNull SourceFilterParser.MessageSpeedOverGroundInNumberRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageNavigationalStatusInIntRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageNavigationalStatusInIntRange(@NotNull SourceFilterParser.MessageNavigationalStatusInIntRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageTrueHeadingInNumberRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTrueHeadingInNumberRange(@NotNull SourceFilterParser.MessageTrueHeadingInNumberRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#OrAnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrAnd(@NotNull SourceFilterParser.OrAndContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceBasestationInIntList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceBasestationInIntList(@NotNull SourceFilterParser.SourceBasestationInIntListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#AisMessagetype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAisMessagetype(@NotNull SourceFilterParser.AisMessagetypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageShiptypeInStringList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageShiptypeInStringList(@NotNull SourceFilterParser.MessageShiptypeInStringListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#intRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntRange(@NotNull SourceFilterParser.IntRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageIdInIntRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageIdInIntRange(@NotNull SourceFilterParser.MessageIdInIntRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageImoInIntList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageImoInIntList(@NotNull SourceFilterParser.MessageImoInIntListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(@NotNull SourceFilterParser.ProgContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageMmsi}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageMmsi(@NotNull SourceFilterParser.MessageMmsiContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageCourseOverGroundInNumberRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageCourseOverGroundInNumberRange(@NotNull SourceFilterParser.MessageCourseOverGroundInNumberRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageLongitude}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageLongitude(@NotNull SourceFilterParser.MessageLongitudeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#stringList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringList(@NotNull SourceFilterParser.StringListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceRegionInStringList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceRegionInStringList(@NotNull SourceFilterParser.SourceRegionInStringListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageShiptypeInIntList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageShiptypeInIntList(@NotNull SourceFilterParser.MessageShiptypeInIntListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageIdInIntList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageIdInIntList(@NotNull SourceFilterParser.MessageIdInIntListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageImoInIntRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageImoInIntRange(@NotNull SourceFilterParser.MessageImoInIntRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#notin}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotin(@NotNull SourceFilterParser.NotinContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceBasestationInIntRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceBasestationInIntRange(@NotNull SourceFilterParser.SourceBasestationInIntRangeContext ctx);

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
}