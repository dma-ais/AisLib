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
	 * Visit a parse tree produced by {@link SourceFilterParser#messageName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageName(@NotNull SourceFilterParser.MessageNameContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageTrueHeading}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTrueHeading(@NotNull SourceFilterParser.MessageTrueHeadingContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageTrueHeadingIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageTrueHeadingIn(@NotNull SourceFilterParser.MessageTrueHeadingInContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#parens}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(@NotNull SourceFilterParser.ParensContext ctx);

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
	 * Visit a parse tree produced by {@link SourceFilterParser#messageDraughtIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageDraughtIn(@NotNull SourceFilterParser.MessageDraughtInContext ctx);

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
	 * Visit a parse tree produced by {@link SourceFilterParser#messageCourseOverGround}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageCourseOverGround(@NotNull SourceFilterParser.MessageCourseOverGroundContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageShiptypeIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageShiptypeIn(@NotNull SourceFilterParser.MessageShiptypeInContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#numberRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberRange(@NotNull SourceFilterParser.NumberRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(@NotNull SourceFilterParser.NumberContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceBasestationIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceBasestationIn(@NotNull SourceFilterParser.SourceBasestationInContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageImoIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageImoIn(@NotNull SourceFilterParser.MessageImoInContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageNavigationalStatusIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageNavigationalStatusIn(@NotNull SourceFilterParser.MessageNavigationalStatusInContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#OrAnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrAnd(@NotNull SourceFilterParser.OrAndContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#AisMessagetype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAisMessagetype(@NotNull SourceFilterParser.AisMessagetypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceIdIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceIdIn(@NotNull SourceFilterParser.SourceIdInContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceRegionIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceRegionIn(@NotNull SourceFilterParser.SourceRegionInContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageCallsignIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageCallsignIn(@NotNull SourceFilterParser.MessageCallsignInContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#intRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntRange(@NotNull SourceFilterParser.IntRangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageLongitudeIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageLongitudeIn(@NotNull SourceFilterParser.MessageLongitudeInContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceTypeIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceTypeIn(@NotNull SourceFilterParser.SourceTypeInContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#in}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIn(@NotNull SourceFilterParser.InContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageLatitudeIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageLatitudeIn(@NotNull SourceFilterParser.MessageLatitudeInContext ctx);

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
	 * Visit a parse tree produced by {@link SourceFilterParser#messageNameIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageNameIn(@NotNull SourceFilterParser.MessageNameInContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageSpeedOverGroundIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageSpeedOverGroundIn(@NotNull SourceFilterParser.MessageSpeedOverGroundInContext ctx);

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
	 * Visit a parse tree produced by {@link SourceFilterParser#messageCourseOverGroundIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageCourseOverGroundIn(@NotNull SourceFilterParser.MessageCourseOverGroundInContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageLongitude}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageLongitude(@NotNull SourceFilterParser.MessageLongitudeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageDraught}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageDraught(@NotNull SourceFilterParser.MessageDraughtContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageMmsiIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageMmsiIn(@NotNull SourceFilterParser.MessageMmsiInContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceCountryIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceCountryIn(@NotNull SourceFilterParser.SourceCountryInContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#stringList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringList(@NotNull SourceFilterParser.StringListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageIdIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageIdIn(@NotNull SourceFilterParser.MessageIdInContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#notin}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotin(@NotNull SourceFilterParser.NotinContext ctx);

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