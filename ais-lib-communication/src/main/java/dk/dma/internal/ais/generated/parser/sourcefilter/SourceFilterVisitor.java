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
	 * Visit a parse tree produced by {@link SourceFilterParser#comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparison(@NotNull SourceFilterParser.ComparisonContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#comparesToInt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparesToInt(@NotNull SourceFilterParser.ComparesToIntContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#idList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdList(@NotNull SourceFilterParser.IdListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceBasestation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceBasestation(@NotNull SourceFilterParser.SourceBasestationContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(@NotNull SourceFilterParser.ProgContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#equalityTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityTest(@NotNull SourceFilterParser.EqualityTestContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceType(@NotNull SourceFilterParser.SourceTypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceRegion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceRegion(@NotNull SourceFilterParser.SourceRegionContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#OrAnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrAnd(@NotNull SourceFilterParser.OrAndContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#inList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInList(@NotNull SourceFilterParser.InListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#inIntList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInIntList(@NotNull SourceFilterParser.InIntListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#AisMessagetype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAisMessagetype(@NotNull SourceFilterParser.AisMessagetypeContext ctx);
}