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
package dk.dma.ais.packet;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;

import dk.dma.ais.internal.parser.sourcefilter.SourceFilterBaseVisitor;
import dk.dma.ais.internal.parser.sourcefilter.SourceFilterLexer;
import dk.dma.ais.internal.parser.sourcefilter.SourceFilterParser;
import dk.dma.ais.internal.parser.sourcefilter.SourceFilterParser.OrAndContext;
import dk.dma.ais.internal.parser.sourcefilter.SourceFilterParser.ParensContext;
import dk.dma.ais.internal.parser.sourcefilter.SourceFilterParser.ProgContext;
import dk.dma.ais.internal.parser.sourcefilter.SourceFilterParser.SourceBasestationContext;
import dk.dma.ais.internal.parser.sourcefilter.SourceFilterParser.SourceCountryContext;
import dk.dma.ais.internal.parser.sourcefilter.SourceFilterParser.SourceIdContext;
import dk.dma.ais.internal.parser.sourcefilter.SourceFilterParser.SourceRegionContext;
import dk.dma.ais.internal.parser.sourcefilter.SourceFilterParser.SourceTypeContext;
import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.enav.model.Country;
import dk.dma.enav.util.function.Predicate;

/**
 * 
 * @author Kasper Nielsen
 */
public class AisPacketFilters {

    public static Predicate<AisPacket> parseSourceFilter(String filter) {
        ANTLRInputStream input = new ANTLRInputStream(requireNonNull(filter));
        SourceFilterLexer lexer = new SourceFilterLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SourceFilterParser parser = new SourceFilterParser(tokens);
        ProgContext tree = parser.prog();
        return tree.accept(new SourceFilterImpl());
    }

    public static Predicate<AisPacket> filterOnSourceType(final SourceType sourceType) {
        requireNonNull(sourceType, "sourceType is null");
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                return sourceType == p.getTags().getSourceType();
            }

            public String toString() {
                return "sourceType = " + sourceType;
            }
        };
    }

    public static Predicate<AisPacket> filterOnSourceCountry(final Country... countries) {
        final Country[] c = check(countries);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                Country country = p.getTags().getSourceCountry();
                return country != null && Arrays.binarySearch(c, country) != -1;
            }

            public String toString() {
                return "sourceCountry = " + skipBrackets(Arrays.toString(c));
            }
        };
    }

    public static Predicate<AisPacket> filterOnSourceBaseStation(final String... ids) {
        final int[] bss = new int[ids.length];
        for (int i = 0; i < bss.length; i++) {
            bss[i] = Integer.valueOf(ids[i]);
        }
        return filterOnSourceBaseStation(bss);
    }

    public static Predicate<AisPacket> filterOnSourceBaseStation(final int... ids) {
        final int[] copy = ids.clone();
        Arrays.sort(copy);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                Integer sourceBs = p.getTags().getSourceBs();
                return sourceBs != null && Arrays.binarySearch(copy, sourceBs) != -1;
            }

            public String toString() {
                return "sourceBaseStation = " + skipBrackets(Arrays.toString(copy));
            }
        };
    }

    public static Predicate<AisPacket> filterOnSourceId(final String... ids) {
        final String[] s = check(ids);
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                String sourceId = p.getTags().getSourceId();
                return sourceId != null && Arrays.binarySearch(s, sourceId) != -1;
            }

            public String toString() {
                return "sourceId = " + skipBrackets(Arrays.toString(s));
            }
        };
    }

    public static Predicate<AisPacket> filterOnSourceRegion(final String... regions) {
        final String[] s = check(regions);
        requireNonNull(regions, "regions is null");
        return new Predicate<AisPacket>() {
            public boolean test(AisPacket p) {
                IProprietarySourceTag sourceTag = p.getVdm().getSourceTag();
                String region = sourceTag == null ? null : sourceTag.getRegion();
                return region != null && Arrays.binarySearch(s, region) != -1;
            }

            public String toString() {
                return "sourceRegion = " + skipBrackets(Arrays.toString(s));
            }
        };
    }

    static String skipBrackets(String s) {
        return s.length() < 2 ? "" : s.substring(1, s.length() - 1);
    }

    @SafeVarargs
    static <T> T[] check(T... elements) {
        T[] s = elements.clone();
        Arrays.sort(s);
        // Check for nulls
        return s;
    }

    public static void main(String[] args) {
        System.out.println(filterOnSourceRegion(""));
    }

    static class SourceFilterImpl extends SourceFilterBaseVisitor<Predicate<AisPacket>> {

        @Override
        public Predicate<AisPacket> visitOrAnd(OrAndContext ctx) {
            return ctx.op.getType() == SourceFilterParser.AND ? visit(ctx.expr(0)).and(visit(ctx.expr(1))) : visit(
                    ctx.expr(0)).or(visit(ctx.expr(1)));
        }

        @Override
        public Predicate<AisPacket> visitParens(ParensContext ctx) {
            final Predicate<AisPacket> p = visit(ctx.expr());
            return new Predicate<AisPacket>() {
                public boolean test(AisPacket element) {
                    return p.test(element);
                }

                public String toString() {
                    return "(" + p.toString() + ")";
                }
            };
        }

        @Override
        public Predicate<AisPacket> visitSourceBasestation(SourceBasestationContext ctx) {
            return filterOnSourceBaseStation(readArrays(ctx.intList().INT()));
        }

        @Override
        public Predicate<AisPacket> visitSourceCountry(SourceCountryContext ctx) {
            return filterOnSourceCountry(Country.findAllByCode(readArrays(ctx.idList().ID())).toArray(new Country[0]));
        }

        @Override
        public Predicate<AisPacket> visitSourceId(final SourceIdContext ctx) {
            return filterOnSourceId(readArrays(ctx.idList().ID()));
        }

        @Override
        public Predicate<AisPacket> visitSourceRegion(SourceRegionContext ctx) {
            return filterOnSourceId(readArrays(ctx.idList().ID()));
        }

        @Override
        public Predicate<AisPacket> visitSourceType(SourceTypeContext ctx) {
            final SourceType type = SourceType.fromString(ctx.ID().getText());
            return filterOnSourceType(type);
        }

        private static String[] readArrays(Iterable<TerminalNode> iter) {
            ArrayList<String> list = new ArrayList<>();
            for (TerminalNode t : iter) {
                list.add(t.getText());
            }
            return list.toArray(new String[list.size()]);
        }
    }
}
