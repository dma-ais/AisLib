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
package dk.dma.ais.filter;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketFilters;

import java.util.function.Predicate;

/**
 * Filtering using string expression
 * @see dk.dma.ais.packet.AisPacketFiltersParseHelper 
 */
public class ExpressionFilter implements IPacketFilter {
    
    final Predicate<AisPacket> predicate;
    
    public ExpressionFilter(String filter) {
        this.predicate = AisPacketFilters.parseExpressionFilter(filter);
    }

    @Override
    public boolean rejectedByFilter(AisPacket packet) {
        return !predicate.test(packet);
    }

}
