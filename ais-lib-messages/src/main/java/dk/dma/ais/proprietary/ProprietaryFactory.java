/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
