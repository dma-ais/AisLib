/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
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
	 * Visit a parse tree produced by {@link SourceFilterParser#equalityTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityTest(@NotNull SourceFilterParser.EqualityTestContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceId(@NotNull SourceFilterParser.SourceIdContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#idList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdList(@NotNull SourceFilterParser.IdListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(@NotNull SourceFilterParser.ProgContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceRegion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceRegion(@NotNull SourceFilterParser.SourceRegionContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceCountry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceCountry(@NotNull SourceFilterParser.SourceCountryContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceType(@NotNull SourceFilterParser.SourceTypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SourceFilterParser#sourceBasestation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceBasestation(@NotNull SourceFilterParser.SourceBasestationContext ctx);

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
}
