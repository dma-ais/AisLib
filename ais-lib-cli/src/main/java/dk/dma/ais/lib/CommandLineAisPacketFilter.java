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
package dk.dma.ais.lib;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

import com.beust.jcommander.Parameter;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.packet.AisPacket;

/**
 * This class provides common functionality for filtering {@link AisPacket AIS packets}.
 *
 * @author Kasper Nielsen
 */
public class CommandLineAisPacketFilter implements Predicate<AisPacket> {

    @Parameter(names = "-start", description = "[Filter] Start date (inclusive), format == yyyy-MM-dd")
    private volatile Date start;

    @Parameter(names = "-stop", description = "[Filter] Stop date (exclusive), format == yyyy-MM-dd")
    private volatile Date stop;

    @Parameter(names = "-messagetype", description = "[Filter] The Ais message type")
    private final List<Integer> messageTypes = new CopyOnWriteArrayList<>();

    private boolean onlyValidMessages;

    String countryList;

    // specify source
    // specify country
    // TimeOfDay??
    // include packets that are not proper ais messages
    // host

    /** {@inheritDoc} */
    @Override
    public boolean test(AisPacket element) {
        AisMessage m = element.tryGetAisMessage();
        if (m == null) {
            // We can probably only handle source filtering?
            return start == null && stop != null && messageTypes.isEmpty() && onlyValidMessages;
        }
        if (!messageTypes.isEmpty() && !messageTypes.contains(m.getMsgId())) {
            return false;
        }
        return true;
    }

    public static Predicate<AisPacket> timestampFilter(long start, long end) {
        return null;
    }
}
