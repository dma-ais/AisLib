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
package dk.dma.ais.packet;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.message.AisTargetType;
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.ais.message.NavigationalStatus;
import dk.dma.ais.message.ShipTypeCargo;
import dk.dma.commons.util.io.OutputStreamSink;
import dk.dma.enav.model.geometry.Position;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
    
    /**
     * A sink that writes ais messages as JSON to an output stream. Each Line is a valid json object.
     */
    public static OutputStreamSink<AisPacket> jsonMessageSink() {
        return new OutputStreamSink<AisPacket>() {

            @Override
            public void process(OutputStream stream, AisPacket p, long count) throws IOException {
                
                AisMessage m = p.tryGetAisMessage();
                
                if (m == null) {
                    return;
                }
                
                StringBuilder sb = new StringBuilder();
                
                sb.append("{");
                sb.append("\"mmsi\":").append(m.getUserId()).append(',');
                sb.append("\"msgId\":").append(m.getMsgId()).append(',');
                
                Position pos = m.getValidPosition();
                if (pos != null) {
                    DecimalFormat df = POSITION_FORMATTER.get();
                    sb.append("\"lat\":").append(df.format(pos.getLatitude())).append(',');
                    sb.append("\"lon\":").append(df.format(pos.getLongitude())).append(',');
                }
                
                if (m instanceof AisPositionMessage) {
                    AisPositionMessage im = (AisPositionMessage)m; 
                    sb.append("\"sog\":").append(im.getSog()).append(',');
                    sb.append("\"cog\":").append(im.getCog()).append(',');
                    sb.append("\"trueHeading\":").append(im.getTrueHeading()).append(',');     
                }
                
                if (m.getTargetType() != null) {
                    sb.append("\"targetType\":").append('\"').append(m.getTargetType().toString()).append("\",");
                }
                
                
                
                if (m instanceof AisStaticCommon) {
                    AisStaticCommon asm = (AisStaticCommon)m;
                    
                    sb.append("\"name\":").append('\"').append(AisMessage.trimText(asm.getName())).append("\",");
                    sb.append("\"dimBow\":").append(asm.getDimBow()).append(',');
                    sb.append("\"dimPort\":").append(asm.getDimPort()).append(",");
                    sb.append("\"dimStarboard\":").append(asm.getDimStarboard()).append(",");
                    sb.append("\"dimStern\":").append(asm.getDimStern()).append(",");
                    ShipTypeCargo stc = new ShipTypeCargo(asm.getShipType());
                    sb.append("\"shipType\":\"").append(stc.getShipType().toString()).append("\",");
                    sb.append("\"cargo\":\"").append(stc.getShipCargo().toString()).append("\",");
                    sb.append("\"callsign\":\"").append(AisMessage.trimText(asm.getCallsign())).append("\",");
                    
                }
                
                sb.append("\"timestamp\":").append(p.getBestTimestamp()).append("}\r\n");
                
                writeAscii(sb, stream);            
            }
    
            /** {@inheritDoc} */
            @Override
            public void footer(OutputStream stream, long count) throws IOException {
                //no footer
            }
    
            /** {@inheritDoc} */
            @Override
            public void header(OutputStream stream) throws IOException {
                //no header
            }
        };
    }
        
    /**
     * A sink that writes static AIS packets as JSON to an output stream. 
     */
    public static OutputStreamSink<AisPacket> jsonStaticListSink() {
    
        return new OutputStreamSink<AisPacket>() {
            private boolean first = true; 
            
            @Override
            public void process(OutputStream stream, AisPacket p, long count) throws IOException {
                
                AisMessage m = p.tryGetAisMessage();
                
                if (m instanceof AisStaticCommon) {
                    
                    StringBuilder sb = new StringBuilder();
                    if (!first) {
                        sb.append(",\n");
                    }
                 
                    AisStaticCommon common = (AisStaticCommon) m;
                    sb.append("\"").append(common.getUserId()).append("\": [");
                    sb.append(common.getUserId()).append(",");
                    sb.append("\"").append(AisMessage.trimText(common.getName())).append("\",");
                    sb.append(common.getDimBow()).append(",");
                    sb.append(common.getDimPort()).append(",");
                    sb.append(common.getDimStarboard()).append(",");
                    sb.append(common.getDimStern()).append(",");
                    ShipTypeCargo stc = new ShipTypeCargo(common.getShipType());
                    sb.append('\"').append(stc.getShipType().toString()).append("\",");
                    sb.append('\"').append(stc.getShipCargo().toString()).append("\",");
                    sb.append("\"").append(AisMessage.trimText(common.getCallsign())).append("\",");
                    sb.append(p.getBestTimestamp()).append(",");                
                    sb.append('\"').append(common.getTargetType().toString()).append("\"]");
                    writeAscii(sb, stream);
                    first = false;
                }
                
                
            }
    
            /** {@inheritDoc} */
            @Override
            public void footer(OutputStream stream, long count) throws IOException {
                if (!first) { // write the closing tag, unless we have never written anything.
                    writeAscii("  }\n", stream);
                }
                writeAscii("}}", stream);
            }
    
            /** {@inheritDoc} */
            @Override
            public void header(OutputStream stream) throws IOException {
                stream.write("{\"static\": {\n  \"headers\": [\"mmsi\",\"name\",\"dimBow\",\"dimPort\",\"dimStarboard\",\"dimStern\",\"shipType\",\"shipCargo\",\"callsign\",\"timestamp\",\"targetType\"],\n  \"vessels\": {".getBytes(StandardCharsets.US_ASCII));
            }
        };
    }
    
    
    /**
     * A sink that writes an AIS POSITION packets as JSON to an output stream.
     */
    public static OutputStreamSink<AisPacket> jsonPosListSink() {
        return new OutputStreamSink<AisPacket>() {
            private boolean first = true; 
            @Override
            public void process(OutputStream stream, AisPacket p, long count) throws IOException {
                AisMessage m = p.tryGetAisMessage();
                DecimalFormat df = POSITION_FORMATTER.get();
                
                if (m instanceof AisPositionMessage) {
                    AisPositionMessage im = (AisPositionMessage) m;
                    Position pos = m.getValidPosition();
                    
                    if (pos == null) {
                        return;
                    }
                    
                    StringBuilder sb = new StringBuilder();
                    if (!first) {
                        sb.append(",\n");
                        
                    }
                 
                    sb.append('\"').append(im.getUserId()).append("\": [");
                    sb.append(im.getUserId()).append(",");
                    sb.append(p.getBestTimestamp()).append(",");
                    sb.append(df.format(pos.getLatitude())).append(",");
                    sb.append(df.format(pos.getLongitude())).append(",");                
                    sb.append(im.getSog()).append(",");
                    sb.append(im.getCog()).append(",");
                    sb.append(im.getTrueHeading()).append("]"); 
                    writeAscii(sb, stream);
                    first = false;
                }
                
                
            }
    
            /** {@inheritDoc} */
            @Override
            public void footer(OutputStream stream, long count) throws IOException {
                if (!first) { // write the closing tag, unless we have never written anything.
                    writeAscii("  }\n", stream);
                }
                writeAscii("}}", stream);
            }
    
            /** {@inheritDoc} */
            @Override
            public void header(OutputStream stream) throws IOException {
                stream.write("{\"dynamic\": {\n  \"headers\": [\"mmsi\",\"timestamp\",\"lat\",\"lon\",\"sog\",\"cog\",\"heading\"],\n  \"vessels\": {".getBytes(StandardCharsets.US_ASCII));
            }
        };
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

    public static OutputStreamSink<AisPacket> newKmlSink(Predicate<? super AisPacket> filter, boolean createSituationFolder, boolean createMovementsFolder, boolean createTracksFolder, Predicate<? super AisPacket> isPrimaryTarget, Predicate<? super AisPacket> isSecondaryTarget, Predicate<? super AisPacket> triggerSnapshot, Supplier<? extends String> snapshotDescriptionSupplier, Supplier<? extends Integer> movementInterpolationStep, BiFunction<? super ShipTypeCargo, ? super NavigationalStatus, ? extends String> iconHrefSupplier) {
        return new AisPacketKMLOutputSink(filter, createSituationFolder, createMovementsFolder, createTracksFolder, isPrimaryTarget, isSecondaryTarget, triggerSnapshot, snapshotDescriptionSupplier, movementInterpolationStep, iconHrefSupplier);
    }

    public static OutputStreamSink<AisPacket> newKmlSink(Predicate<? super AisPacket> filter, boolean createSituationFolder, boolean createMovementsFolder, boolean createTracksFolder, Predicate<? super AisPacket> isPrimaryTarget, Predicate<? super AisPacket> isSecondaryTarget, Predicate<? super AisPacket> triggerSnapshot, Supplier<? extends String> snapshotDescriptionSupplier, Supplier<? extends Integer> movementInterpolationStep, Supplier<? extends String> supplyTitle, Supplier<? extends String> supplyDescription, BiFunction<? super ShipTypeCargo, ? super NavigationalStatus, ? extends String> iconHrefSupplier) {
        return new AisPacketKMLOutputSink(filter, createSituationFolder, createMovementsFolder, createTracksFolder, isPrimaryTarget, isSecondaryTarget, triggerSnapshot, snapshotDescriptionSupplier, movementInterpolationStep, supplyTitle, supplyDescription, iconHrefSupplier);
    }

    public static OutputStreamSink<AisPacket> newKmzSink() {
        return new AisPacketKMZOutputSink();
    }

    public static OutputStreamSink<AisPacket> newKmzSink(Predicate<? super AisPacket> filter) {
        return new AisPacketKMZOutputSink(filter);
    }

    public static OutputStreamSink<AisPacket> newKmzSink(Predicate<? super AisPacket> filter, boolean createSituationFolder, boolean createMovementsFolder, boolean createTracksFolder, Predicate<? super AisPacket> isPrimaryTarget, Predicate<? super AisPacket> isSecondaryTarget, Predicate<? super AisPacket> triggerSnapshot, Supplier<? extends String> snapshotDescriptionSupplier, Supplier<? extends Integer> movementInterpolationStep, BiFunction<? super ShipTypeCargo, ? super NavigationalStatus, ? extends String> iconHrefSupplier) {
        return new AisPacketKMZOutputSink(filter, createSituationFolder, createMovementsFolder, createTracksFolder, isPrimaryTarget, isSecondaryTarget, triggerSnapshot, snapshotDescriptionSupplier, movementInterpolationStep, iconHrefSupplier);
    }

    public static OutputStreamSink<AisPacket> newKmzSink(Predicate<? super AisPacket> filter, boolean createSituationFolder, boolean createMovementsFolder, boolean createTracksFolder, Predicate<? super AisPacket> isPrimaryTarget, Predicate<? super AisPacket> isSecondaryTarget, Predicate<? super AisPacket> triggerSnapshot, Supplier<? extends String> snapshotDescriptionSupplier, Supplier<? extends Integer> movementInterpolationStep, Supplier<? extends String> supplyTitle, Supplier<? extends String> supplyDescription, BiFunction<? super ShipTypeCargo, ? super NavigationalStatus, ? extends String> iconHrefSupplier) {
        return new AisPacketKMZOutputSink(filter, createSituationFolder, createMovementsFolder, createTracksFolder, isPrimaryTarget, isSecondaryTarget, triggerSnapshot, snapshotDescriptionSupplier, movementInterpolationStep, supplyTitle, supplyDescription, iconHrefSupplier);
    }

}
