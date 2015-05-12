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
import dk.dma.ais.message.AisPosition;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.message.IDimensionMessage;
import dk.dma.ais.message.INameMessage;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.ais.message.ShipTypeCargo;
import dk.dma.commons.util.io.OutputStreamSink;
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

    private static final Logger LOG = LoggerFactory.getLogger(AisPacketCSVOutputSink.class);
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
                List fields = Lists.newArrayList();

                for (String column : columns) {
                    switch (column) {
                        case "timestamp":
                            fields.add(packet.getBestTimestamp());
                            break;
                        case "timestamp_pretty":
                            fields.add(DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(packet.getBestTimestamp()), ZoneId.of("UTC"))));
                            break;
                        case "msgid":
                            addMsgId(fields, m);
                            break;
                        case "mmsi":
                            addMmsi(fields, m);
                            break;
                        case "lat":
                            addLatitude(fields, m);
                            break;
                        case "lon":
                            addLongitude(fields, m);
                            break;
                        case "targetType":
                            addTargetType(fields, m);
                            break;
                        case "sog":
                            addSog(fields, m);
                            break;
                        case "cog":
                            addCog(fields, m);
                            break;
                        case "trueHeading":
                            addTrueHeading(fields, m);
                            break;
                        case "draught":
                            addDraught(fields, m);
                            break;
                        case "name":
                            addName(fields, m);
                            break;
                        case "dimBow":
                            addDimBow(fields, m);
                            break;
                        case "dimPort":
                            addDimPort(fields, m);
                            break;
                        case "dimStarboard":
                            addDimStarboard(fields, m);
                            break;
                        case "dimStern":
                            addDimStern(fields, m);
                            break;
                        case "shipType":
                            addShipType(fields, m);
                            break;
                        case "shipCargo":
                            addShipCargo(fields, m);
                            break;
                        case "callsign":
                            addCallsign(fields, m);
                            break;
                        case "imo":
                            addImo(fields, m);
                            break;
                        case "destination":
                            addDestination(fields, m);
                            break;
                        case "eta":
                            addEta(fields, m);
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

    // ---

    protected void addMmsi(List fields, AisMessage m) {
        fields.add(m.getUserId());
    }

    protected void addMsgId(List fields, AisMessage m) {
        fields.add(m.getMsgId());
    }

    protected void addLatitude(List fields, AisMessage m) {
        if (m instanceof IPositionMessage) {
            AisPosition pos = ((IPositionMessage) m).getPos();
            if (pos != null) {
                fields.add((float) pos.getLatitude());
            } else {
                fields.add("null");
            }
        } else {
            fields.add("null");
        }
    }

    protected void addLongitude(List fields, AisMessage m) {
        if (m instanceof IPositionMessage) {
            AisPosition pos = ((IPositionMessage) m).getPos();
            if (pos != null) {
                fields.add((float) pos.getLongitude());
            } else {
                fields.add("null");
            }
        } else {
            fields.add("null");
        }
    }

    protected void addTargetType(List fields, AisMessage m) {
        fields.add(m.getTargetType());
    }

    protected void addSog(List fields, AisMessage m) {
        if (m instanceof IVesselPositionMessage) {
            fields.add(((IVesselPositionMessage) m).getSog()/10f);
        } else {
            fields.add("null");
        }
    }

    protected void addCog(List fields, AisMessage m) {
        if (m instanceof IVesselPositionMessage) {
            fields.add(((IVesselPositionMessage) m).getCog()/10f);
        } else {
            fields.add("null");
        }
    }

    protected void addTrueHeading(List fields, AisMessage m) {
        if (m instanceof IVesselPositionMessage) {
            fields.add(((IVesselPositionMessage) m).getTrueHeading());
        } else {
            fields.add("null");
        }
    }

    protected void addDraught(List fields, AisMessage m) {
        if (m instanceof AisMessage5) {
            fields.add(((AisMessage5) m).getDraught()/10f);
        } else {
            fields.add("null");
        }
    }

    protected void addName(List fields, AisMessage m) {
        if (m instanceof INameMessage) {
            fields.add(AisMessage.trimText(((INameMessage) m).getName()));
        } else {
            fields.add("null");
        }
    }

    protected void addDimBow(List fields, AisMessage m) {
        if (m instanceof IDimensionMessage) {
            fields.add(((IDimensionMessage) m).getDimBow());
        } else {
            fields.add("null");
        }
    }

    protected void addDimPort(List fields, AisMessage m) {
        if (m instanceof IDimensionMessage) {
            fields.add(((IDimensionMessage) m).getDimPort());
        } else {
            fields.add("null");
        }
    }

    protected void addDimStarboard(List fields, AisMessage m) {
        if (m instanceof IDimensionMessage) {
            fields.add(((IDimensionMessage) m).getDimStarboard());
        } else {
            fields.add("null");
        }
    }

    protected void addDimStern(List fields, AisMessage m) {
        if (m instanceof IDimensionMessage) {
            fields.add(((IDimensionMessage) m).getDimStern());
        } else {
            fields.add("null");
        }
    }

    protected void addShipType(List fields, AisMessage m) {
        if (m instanceof AisStaticCommon) {
            ShipTypeCargo stc = new ShipTypeCargo(((AisStaticCommon) m).getShipType());
            fields.add(stc.getShipType().toString());
        } else {
            fields.add("null");
        }
    }

    protected void addShipCargo(List fields, AisMessage m) {
        if (m instanceof AisStaticCommon) {
            ShipTypeCargo stc = new ShipTypeCargo(((AisStaticCommon) m).getShipType());
            fields.add(stc.getShipCargo().toString());
        } else {
            fields.add("null");
        }
    }

    protected void addCallsign(List fields, AisMessage m) {
        if (m instanceof AisStaticCommon) {
            fields.add(AisMessage.trimText(((AisStaticCommon) m).getCallsign()));
        } else {
            fields.add("null");
        }
    }

    protected void addImo(List fields, AisMessage m) {
        if (m instanceof AisMessage5) {
            fields.add(((AisMessage5) m).getImo());
        } else {
            fields.add("null");
        }
    }

    protected void addDestination(List fields, AisMessage m) {
        if (m instanceof AisMessage5) {
            fields.add(AisMessage.trimText(((AisMessage5) m).getDest()));
        } else {
            fields.add("null");
        }
    }

    protected void addEta(List fields, AisMessage m) {
        if (m instanceof AisMessage5) {
            long eta = ((AisMessage5) m).getEta();
            int minute = (int) (eta & 63);
            int hour = (int) ((eta >> 6) & 31);
            int day = (int) ((eta >> 11) & 31);
            int month = (int) ((eta >> 16) & 15);

            fields.add(String.format("%02d/%02d %02d:%02d UTC", day, month, hour, minute));
        } else {
            fields.add("null");
        }

        /*
        Estimated time of arrival; MMDDHHMM UTC
        Bits 19-16: month; 1-12; 0 = not available = default
        Bits 15-11: day; 1-31; 0 = not available = default
        Bits 10-6: hour; 0-23; 24 = not available = default
        Bits 5-0: minute; 0-59; 60 = not available = default
        For SAR aircraft, the use of this field may be decided by the responsible administration
        */
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
