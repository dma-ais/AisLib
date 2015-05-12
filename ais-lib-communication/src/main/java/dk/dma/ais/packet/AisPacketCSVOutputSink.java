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

import com.google.common.collect.Lists;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.message.IDimensionMessage;
import dk.dma.ais.message.INameMessage;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.ais.message.ShipTypeCargo;
import dk.dma.commons.util.io.OutputStreamSink;
import dk.dma.enav.model.geometry.Position;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.GuardedBy;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Transform AisPackets into json objects with only specific columns
 * @author Jens Tuxen
 *
 */
public class AisPacketCSVOutputSink extends OutputStreamSink<AisPacket> {

    private static final Logger LOG = LoggerFactory.getLogger(AisPacketParser.class);
    { LOG.info("AisPacketCSVOutputSink created."); }

    /** The column we are writing. */
    private final String[] columns;

    private  DecimalFormat df = new DecimalFormat("###.00000");


    @GuardedBy("csvLock")
    private CSVPrinter csv;

    private Lock csvLock = new ReentrantLock();

    public AisPacketCSVOutputSink() {
        this("timestamp_pretty;timestamp;targetType;mmsi;msgid;lat;lon;sog;cog;draught;name;dimBow;dimPort;dimStarboard;dimStern;shipType;shipCargo;destination;eta;imo;callsign", ";");
    }

    public AisPacketCSVOutputSink(String format) {
        this(format,";");
    }

    public AisPacketCSVOutputSink(String format, String separator) {
       columns = format.split(separator);
    }

    @Override
    public void process(OutputStream stream, AisPacket packet, long count) throws IOException {

        csvLock.lock();
        try {
            if (csv == null) {
                csv = new CSVPrinter(new OutputStreamWriter(stream), CSVFormat.DEFAULT.withHeader(columns));
            }

            AisMessage m = packet.tryGetAisMessage();
            if (m != null) {
                AisStaticCommon common = null;
                ShipTypeCargo stc = null;
                if (m instanceof AisStaticCommon) {
                    common = (AisStaticCommon) m;
                    stc = new ShipTypeCargo(common.getShipType());
                }

                IPositionMessage im = null;
                Position pos = m.getValidPosition();
                if (m instanceof IPositionMessage) {
                    im = (IPositionMessage) m;
                }

                AisMessage5 m5 = null;
                if (m instanceof AisMessage5) {
                    m5 = (AisMessage5) m;
                }

                IVesselPositionMessage vpm = null;
                if (m instanceof IVesselPositionMessage) {
                    vpm = (IVesselPositionMessage) m;
                }

                IDimensionMessage dm = null;
                if (m instanceof IDimensionMessage) {
                    dm = (IDimensionMessage) m;
                }

                INameMessage nm = null;
                if (m instanceof INameMessage) {
                    nm = (INameMessage) m;
                }

                List fields = Lists.newArrayList();

                for (String column : columns) {
                    switch (column) {
                        case "msgid":
                            fields.add(m.getMsgId());
                            break;
                        case "mmsi":
                            fields.add(m.getUserId());
                            break;
                        case "lat":
                            if (im != null && pos != null) {
                                fields.add((float) pos.getLatitude());
                            } else {
                                fields.add("null");
                            }
                            break;
                        case "lon":
                            if (im != null && pos != null) {
                                fields.add((float) pos.getLongitude());
                            } else {
                                fields.add("null");
                            }
                            break;
                        case "targetType":
                            fields.add(m.getTargetType());
                            break;
                        case "sog":
                            if (vpm != null) {
                                fields.add(vpm.getSog());
                            } else {
                                fields.add("null");
                            }
                            break;
                        case "cog":
                            if (vpm != null) {
                                fields.add(vpm.getCog());

                            } else {
                                fields.add("null");
                            }
                            break;
                        case "trueHeading":
                            if (vpm != null) {
                                fields.add(vpm.getTrueHeading());
                                break;
                            } else {
                                fields.add("null");
                            }
                        case "draught":
                            if (m5 != null) {
                                fields.add(m5.getDraught());
                            } else {
                                fields.add("null");
                            }
                            break;
                        case "timestamp":
                            fields.add(packet.getBestTimestamp());
                            break;
                        case "timestamp_pretty":
                            fields.add(DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(packet.getBestTimestamp()), ZoneId.of("UTC"))));
                            break;
                        case "name":
                            if (nm != null) {
                                fields.add(AisMessage.trimText(nm.getName()));
                            } else {
                                fields.add("null");
                            }
                            break;
                        case "dimBow":
                            if (dm != null) {
                                fields.add(dm.getDimBow());
                            } else {
                                fields.add("null");
                            }
                            break;
                        case "dimPort":
                            if (dm != null) {
                                fields.add(dm.getDimPort());
                            } else {
                                fields.add("null");
                            }
                            break;
                        case "dimStarboard":
                            if (dm != null) {
                                fields.add(dm.getDimStarboard());
                            } else {
                                fields.add("null");
                            }
                            break;
                        case "dimStern":
                            if (dm != null) {
                                fields.add(dm.getDimStern());

                            } else {
                                fields.add("null");
                            }
                            break;
                        case "shipType":
                            if (stc != null) {
                                fields.add(stc.getShipType().toString());
                            } else {
                                fields.add("null");
                            }
                            break;
                        case "shipCargo":
                            if (stc != null) {
                                fields.add(stc.getShipCargo().toString());
                            } else {
                                fields.add("null");
                            }
                            break;
                        case "callsign":
                            if (common != null) {
                                fields.add(AisMessage.trimText(common.getCallsign()));
                            } else {
                                fields.add("null");
                            }
                            break;
                        case "imo":
                            if (m5 != null) {
                                fields.add(m5.getImo());
                            } else {
                                fields.add("null");
                            }
                            break;
                        case "destination":
                            if (m5 != null) {
                                fields.add(AisMessage.trimText(m5.getDest()));
                            } else {
                                fields.add("null");
                            }
                            break;
                        case "eta":
                            if (m5 != null) {
                                fields.add(m5.getEta());
                            } else {
                                fields.add("null");
                            }
                            break;
                        default:
                            fields.add("null");
                    }
                }
                csv.printRecord(fields);
            }
        } finally {
            csvLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void footer(OutputStream stream, long count) throws IOException {
        if (csv != null) {
            csv.flush();
            csv.close();
            csv = null;
        }
    }

}
