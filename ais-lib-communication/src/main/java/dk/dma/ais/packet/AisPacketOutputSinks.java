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

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.commons.util.io.OutputStreamSink;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.util.function.Predicate;
import dk.dma.enav.util.function.Supplier;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static dk.dma.commons.util.io.IoUtil.writeAscii;

/**
 * Common sink that can be used to convert AIS packets to text.
 *
 * @author Kasper Nielsen
 */
public class AisPacketOutputSinks {

    /** A thread local with a default text format. */
    static final ThreadLocal<SimpleDateFormat> DEFAULT_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        }
    };

    static final ThreadLocal<DecimalFormat> POSITION_FORMATTER = new ThreadLocal<DecimalFormat>() {
        protected DecimalFormat initialValue() {
            return new DecimalFormat("###.#####");
        }
    };

    /** A sink that writes an ais packet to an output stream. Using the default multi-line format. */
    public static final OutputStreamSink<AisPacket> OUTPUT_TO_TEXT = new OutputStreamSink<AisPacket>() {
        @Override
        public void process(OutputStream stream, AisPacket message, long count) throws IOException {
            stream.write(message.getStringMessage().getBytes(StandardCharsets.US_ASCII));
            stream.write('\n');
        }
    };

    /**
     * A sink that writes an AIS packet the past track as a JSON to an output stream. The implementation is a little bit
     * special. Because we are stateless, ending tags are written when the next package is received. Or the end is
     * reached.
     */
    public static final OutputStreamSink<AisPacket> PAST_TRACK_JSON = new OutputStreamSink<AisPacket>() {

        @Override
        public void process(OutputStream stream, AisPacket p, long count) throws IOException {
            DecimalFormat df = POSITION_FORMATTER.get();
            AisMessage m = p.tryGetAisMessage();
            if (m instanceof IVesselPositionMessage) {
                IVesselPositionMessage im = (IVesselPositionMessage) m;
                Position pos = m.getValidPosition();

                StringBuilder sb = new StringBuilder();
                if (count > 1) {
                    sb.append("  },");
                }
                sb.append("\n  \"point\": {\n");
                sb.append("    \"timestamp\": ").append(p.getBestTimestamp()).append(",\n");
                sb.append("    \"lon\": ").append(df.format(pos.getLongitude())).append(",\n");
                sb.append("    \"lat\": ").append(df.format(pos.getLatitude())).append(",\n");
                sb.append("    \"sog\": ").append(im.getSog()).append(",\n");
                sb.append("    \"cog\": ").append(im.getCog()).append(",\n");
                sb.append("    \"heading\": ").append(im.getTrueHeading()).append("\n");
                writeAscii(sb, stream);
            }
        }

        /** {@inheritDoc} */
        @Override
        public void footer(OutputStream stream, long count) throws IOException {
            if (count > 0) { // write the closing tag, unless we have never written anything.
                writeAscii("  }\n", stream);
            }
            writeAscii("}}", stream);
        }

        /** {@inheritDoc} */
        @Override
        public void header(OutputStream stream) throws IOException {
            stream.write("{\"track\": {".getBytes(StandardCharsets.US_ASCII));
        }
    };

    /** A sink that writes an AIS packet to an output stream. Printing only the actual sentence . */
    public static final OutputStreamSink<AisPacket> OUTPUT_PREFIXED_SENTENCES = new OutputStreamSink<AisPacket>() {

        @Override
        public void process(OutputStream stream, AisPacket message, long count) throws IOException {
            List<String> rawSentences = message.getVdm().getRawSentences();
            String dateTimeStr = DEFAULT_DATE_FORMAT.get().format(message.getVdm().getTimestamp());
            byte[] b = dateTimeStr.getBytes(StandardCharsets.US_ASCII);
            for (String sentence : rawSentences) {
                stream.write(b);
                stream.write(',');
                writeAscii(sentence, stream);
                stream.write('\n');
            }
        }
    };

    /** A sink that writes an AIS packet to an output stream. Using the default multi-line format. */
    public static final OutputStreamSink<AisPacket> OUTPUT_TO_HTML = new OutputStreamSink<AisPacket>() {
        @Override
        public void process(OutputStream stream, AisPacket message, long count) throws IOException {
            for (String str : message.getStringMessage().split("\r\n")) {
                str = str.replace("<", "&lt;");
                str = str.replace(">", "&gt;");
                str = str.replace("/", "&qout;");
                str = str.replace("&", "&amp;");
                writeAscii(str, stream);
                writeAscii("</br>", stream);
                stream.write('\n');
            }
        }
    };
    
    /** A sink that transforms ais stream into kml. */
    public static final OutputStreamSink<AisPacket> OUTPUT_TO_KML() {
        return new AisPacketKMLOutputSink();
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
    static List<AisPacket> filterPacketsByTime(Iterable<AisPacket> packets, long start, long end) {
        ArrayList<AisPacket> result = new ArrayList<>();
        for (AisPacket p : packets) {
            if (start <= p.getBestTimestamp() && p.getBestTimestamp() < end) {
                result.add(p);
            }
        }
        return result;
    }

    // currently unused
    static List<AisPacket> readFromFile(Path p) throws IOException {
        final ConcurrentLinkedQueue<AisPacket> list = new ConcurrentLinkedQueue<>();
        AisPacket packet;
        try (AisPacketReader r = new AisPacketReader(Files.newInputStream(p))) {
            while ((packet = r.readPacket()) != null) {
                list.add(packet);
            }
        }
        return new ArrayList<>(list);
    }

    public static OutputStreamSink<AisPacket> newTableSink(String columns, boolean writeHeader) {
        return new AisPacketOutputSinkTable(columns, writeHeader);
    }

    public static OutputStreamSink<AisPacket> newTableSink(String columns, boolean writeHeader, String seperator) {
        return new AisPacketOutputSinkTable(columns, writeHeader, seperator);
    }

    public static OutputStreamSink<AisPacket> newKmlSink() {
        return new AisPacketKMLOutputSink();
    }

    public static OutputStreamSink<AisPacket> newKmlSink(Predicate<? super AisPacket> filter) {
        return new AisPacketKMLOutputSink(filter);
    }

    public static OutputStreamSink<AisPacket> newKmlSink(Predicate<? super AisPacket> filter, Predicate<? super AisPacket> isPrimaryTarget, Predicate<? super AisPacket> isSecondaryTarget, Predicate<? super AisPacket> triggerSnapshot, Supplier<? extends String> snapshotDescriptionSupplier, Supplier<? extends Integer> movementInterpolationStep) {
        return new AisPacketKMLOutputSink(filter, isPrimaryTarget, isSecondaryTarget, triggerSnapshot, snapshotDescriptionSupplier, movementInterpolationStep);
    }

    public static OutputStreamSink<AisPacket> newKmlSink(Predicate<? super AisPacket> filter, Predicate<? super AisPacket> isPrimaryTarget, Predicate<? super AisPacket> isSecondaryTarget, Predicate<? super AisPacket> triggerSnapshot, Supplier<? extends String> snapshotDescriptionSupplier, Supplier<? extends Integer> movementInterpolationStep, Supplier<? extends String> supplyTitle, Supplier<? extends String> supplyDescription) {
        return new AisPacketKMLOutputSink(filter, isPrimaryTarget, isSecondaryTarget, triggerSnapshot, snapshotDescriptionSupplier, movementInterpolationStep, supplyTitle, supplyDescription);
    }

}
