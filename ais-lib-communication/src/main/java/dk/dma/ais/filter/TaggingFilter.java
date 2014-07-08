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

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketTags;

/**
 * Filter based on a given tagging. Non null fields in the tagging much
 * be matched by the packet tagging.
 */
@ThreadSafe
public class TaggingFilter implements IPacketFilter {

    private final AisPacketTags filterTagging;

    public TaggingFilter(AisPacketTags filterTagging) {
        this.filterTagging = filterTagging;
    }

    @Override
    public boolean rejectedByFilter(AisPacket packet) {
        return !filterTagging.filterMatch(packet.getTags());
    }

}
