// Generated from SourceFilter.g4 by ANTLR 4.1
package dk.dma.ais.internal.parser.sourcefilter;
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
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceBasestation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceBasestation(@NotNull SourceFilterParser.SourceBasestationContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#intList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntList(@NotNull SourceFilterParser.IntListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#OrAnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrAnd(@NotNull SourceFilterParser.OrAndContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#parens}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(@NotNull SourceFilterParser.ParensContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceType(@NotNull SourceFilterParser.SourceTypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceCountry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceCountry(@NotNull SourceFilterParser.SourceCountryContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceRegion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceRegion(@NotNull SourceFilterParser.SourceRegionContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#idList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdList(@NotNull SourceFilterParser.IdListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceId(@NotNull SourceFilterParser.SourceIdContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(@NotNull SourceFilterParser.ProgContext ctx);
}