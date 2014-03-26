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
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageName(@NotNull ExpressionFilterParser.MessageNameContext ctx);

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
	 * Visit a parse tree produced by {@link ExpressionFilterParser#intList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntList(@NotNull ExpressionFilterParser.IntListContext ctx);

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
	 * Visit a parse tree produced by {@link ExpressionFilterParser#sourceBasestation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceBasestation(@NotNull ExpressionFilterParser.SourceBasestationContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageCourseOverGround}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageCourseOverGround(@NotNull ExpressionFilterParser.MessageCourseOverGroundContext ctx);

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
	 * Visit a parse tree produced by {@link ExpressionFilterParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(@NotNull ExpressionFilterParser.NumberContext ctx);

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
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageNavigationalStatusIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageNavigationalStatusIn(@NotNull ExpressionFilterParser.MessageNavigationalStatusInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#OrAnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrAnd(@NotNull ExpressionFilterParser.OrAndContext ctx);

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
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageCourseOverGroundIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageCourseOverGroundIn(@NotNull ExpressionFilterParser.MessageCourseOverGroundInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageLongitude}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageLongitude(@NotNull ExpressionFilterParser.MessageLongitudeContext ctx);

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
	 * Visit a parse tree produced by {@link ExpressionFilterParser#sourceCountryIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceCountryIn(@NotNull ExpressionFilterParser.SourceCountryInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#stringList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringList(@NotNull ExpressionFilterParser.StringListContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#messageIdIn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageIdIn(@NotNull ExpressionFilterParser.MessageIdInContext ctx);

	/**
	 * Visit a parse tree produced by {@link ExpressionFilterParser#notin}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotin(@NotNull ExpressionFilterParser.NotinContext ctx);

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
}