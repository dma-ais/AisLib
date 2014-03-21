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
	 * Visit a parse tree produced by {@link SourceFilterParser#messageHeading}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageHeading(@NotNull SourceFilterParser.MessageHeadingContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#valueRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueRange(@NotNull SourceFilterParser.ValueRangeContext ctx);

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
	 * Visit a parse tree produced by {@link SourceFilterParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(@NotNull SourceFilterParser.OperatorContext ctx);

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
	 * Visit a parse tree produced by {@link SourceFilterParser#valueSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueSpec(@NotNull SourceFilterParser.ValueSpecContext ctx);

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
	 * Visit a parse tree produced by {@link SourceFilterParser#comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparison(@NotNull SourceFilterParser.ComparisonContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageId(@NotNull SourceFilterParser.MessageIdContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#messageSpeedOverGround}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageSpeedOverGround(@NotNull SourceFilterParser.MessageSpeedOverGroundContext ctx);

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
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceType(@NotNull SourceFilterParser.SourceTypeContext ctx);

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
	 * Visit a parse tree produced by {@link SourceFilterParser#messageImo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageImo(@NotNull SourceFilterParser.MessageImoContext ctx);
}