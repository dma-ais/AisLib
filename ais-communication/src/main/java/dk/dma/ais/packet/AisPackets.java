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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import dk.dma.app.io.OutputStreamSink;

/**
 * 
 * @author Kasper Nielsen
 */
public class AisPackets {

    /** A sink that writes an ais packet to an output stream. Using the default multi-line format. */
    public static final OutputStreamSink<AisPacket> OUTPUT_TO_TEXT = new OutputStreamSink<AisPacket>() {
        @Override
        public void process(OutputStream stream, AisPacket message) throws IOException {
            stream.write(message.getStringMessage().getBytes(StandardCharsets.US_ASCII));
            stream.write('\n');
        }
    };

    /** A sink that writes an ais packet to an output stream. Using the default multi-line format. */
    public static final OutputStreamSink<AisPacket> OUTPUT_TO_HTML = new OutputStreamSink<AisPacket>() {
        @Override
        public void process(OutputStream stream, AisPacket message) throws IOException {
            for (String str : message.getStringMessage().split("\r\n")) {
                str = str.replace("<", "&lt;");
                str = str.replace(">", "&gt;");
                str = str.replace("/", "&qout;");
                str = str.replace("&", "&amp;");
                stream.write(str.getBytes(StandardCharsets.US_ASCII));
                stream.write("</br>".getBytes(StandardCharsets.US_ASCII));
                stream.write('\n');
            }
        }
    };

    /**
     * Filters a list of packets according to their timestamp.
     * 
     * @param packets
     *            a list of packets
     * @param start
     *            inclusive start
     * @param end
     *            exclusive end
     * @return
     */
    public static List<AisPacket> filterPacketsByTime(Iterable<AisPacket> packets, long start, long end) {
        ArrayList<AisPacket> result = new ArrayList<>();
        for (AisPacket p : packets) {
            if (start <= p.getBestTimestamp() && p.getBestTimestamp() < end) {
                result.add(p);
            }
        }
        return result;
    }
}
