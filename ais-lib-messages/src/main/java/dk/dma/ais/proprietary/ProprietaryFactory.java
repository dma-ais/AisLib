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
package dk.dma.ais.proprietary;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import dk.dma.ais.sentence.SentenceLine;

/**
 * 
 * @author Kasper Nielsen
 */
public abstract class ProprietaryFactory {

    static Map<String, ProprietaryFactory> ALL_FACTORIES;

    static {
        Map<String, ProprietaryFactory> map = new HashMap<>();
        for (ProprietaryFactory f : ServiceLoader.load(ProprietaryFactory.class)) {
            // We should probably check that we do not have two factories with the same name
            map.put(f.prefix, f);
        }
        ALL_FACTORIES = Collections.unmodifiableMap(map);
    }

    private final String prefix;

    public ProprietaryFactory(String prefix) {
        this.prefix = requireNonNull(prefix);
        if (prefix.length() != 3) {
            throw new IllegalArgumentException("Prefix length must be exactly 3, was '" + prefix + "'");
        }
    }

    public String getPrefix() {
        return prefix;
    }

    /**
     * Return the tag for line, if matches, otherwise null is returned
     * 
     * @param line
     * @return
     */
    public abstract IProprietaryTag getTag(SentenceLine sl);

    public static Collection<ProprietaryFactory> getAllFactories() {
        return ALL_FACTORIES.values();
    }

    public static IProprietaryTag parseTag(SentenceLine sl) {
        ProprietaryFactory psf = match(sl.getSentenceHead());
        return psf == null ? null : psf.getTag(sl);
    }

    public static boolean isProprietaryTag(String line) {
        return line != null && line.length() >= 5 && line.startsWith("$P");
    }

    public static ProprietaryFactory match(String line) {
        if (isProprietaryTag(line)) {
            String p = line.substring(2, 5);
            return ALL_FACTORIES.get(p);
        }
        return null;
    }
}
