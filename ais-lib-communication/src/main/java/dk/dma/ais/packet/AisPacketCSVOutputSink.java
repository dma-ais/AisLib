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
import dk.dma.ais.message.AisTargetType;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Transform AisPackets into csv objects with only specific columns
 * @author Thomas Borg Salling
 *
 */
public class AisPacketCSVOutputSink extends OutputStreamSink<AisPacket> {

    private static final Logger LOG = LoggerFactory.getLogger(AisPacketCSVOutputSink.class);
    { LOG.info("AisPacketCSVOutputSink created."); }

    /** The column we are writing. */
    private final String[] columns;

    @GuardedBy("csvLock")
    private CSVPrinter csv;

    private Lock csvLock = new ReentrantLock();

    protected static final String NULL_INDICATOR = "null";
    
    public AisPacketCSVOutputSink() {
        this("timestamp_pretty;timestamp;targetType;mmsi;msgid;posacc;lat;lon;sog;cog;draught;name;dimBow;dimPort;dimStarboard;dimStern;shipTypeCargoTypeCode;shipType;shipCargo;destination;eta;imo;callsign", ";");
    }

    public AisPacketCSVOutputSink(String format) {
        this(format, ";");
    }

    public AisPacketCSVOutputSink(String format, String separator) {
        LOG.info("Exporting CSV columns: " + format);
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
                            addTimestamp(fields, packet);
                            break;
                        case "timestamp_pretty":
                            addTimestampPretty(fields, packet);
                            break;
                        case "msgid":
                            addMsgId(fields, m);
                            break;
                        case "mmsi":
                            addMmsi(fields, m);
                            break;
                        case "posacc":
                            addPositionAccuracy(fields, m);
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
                        case "shipTypeCargoTypeCode":
                            addShipTypeCargoTypeCode(fields, m);
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
                            LOG.warn("Unknown column: " + column);
                            fields.add(NULL_INDICATOR);
                    }
                }

                csv.printRecord(fields);
            }
        } finally {
            csvLock.unlock();
        }
    }

    // ---

    private void addTimestamp(List fields, AisPacket packet) {
        final long timestamp = packet.getBestTimestamp();
        if (timestamp >= 0)
            fields.add(timestamp);
        else
            fields.add("null");
    }

    private void addTimestampPretty(List fields, AisPacket packet) {
        final long timestamp = packet.getBestTimestamp();
        if (timestamp >= 0)
            fields.add(DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss").format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC"))));
        else
            fields.add("null");
    }

    private void addMmsi(List fields, AisMessage m) {
        fields.add(m.getUserId());
    }

    private void addMsgId(List fields, AisMessage m) {
        fields.add(m.getMsgId());
    }

    private void addPositionAccuracy(List fields, AisMessage m) {
        if (m instanceof IPositionMessage) {
            fields.add(((IPositionMessage) m).getPosAcc());
        } else {
            fields.add(nullValueForPositionAccuracy.apply(m));
        }
    }

    private void addLatitude(List fields, AisMessage m) {
        if (m instanceof IPositionMessage) {
            AisPosition pos = ((IPositionMessage) m).getPos();
            if (pos != null) {
                fields.add((float) pos.getLatitudeDouble());
            } else {
                fields.add(nullValueForLatitude.apply(m));
            }
        } else {
            fields.add(nullValueForLatitude.apply(m));
        }
    }

    private void addLongitude(List fields, AisMessage m) {
        if (m instanceof IPositionMessage) {
            AisPosition pos = ((IPositionMessage) m).getPos();
            if (pos != null) {
                fields.add((float) pos.getLongitudeDouble());
            } else {
                fields.add(nullValueForLongitude.apply(m));
            }
        } else {
            fields.add(nullValueForLongitude.apply(m));
        }
    }

    private void addTargetType(List fields, AisMessage m) {
        AisTargetType targetType = m.getTargetType();
        fields.add(targetType != null ? targetType.toString() : nullValueForTargetType.apply(m));
    }

    private void addSog(List fields, AisMessage m) {
        if (m instanceof IVesselPositionMessage) {
            fields.add(((IVesselPositionMessage) m).getSog()/10f);
        } else {
            fields.add(nullValueForSog.apply(m));
        }
    }

    private void addCog(List fields, AisMessage m) {
        if (m instanceof IVesselPositionMessage) {
            fields.add(((IVesselPositionMessage) m).getCog()/10f);
        } else {
            fields.add(nullValueForCog.apply(m));
        }
    }

    private void addTrueHeading(List fields, AisMessage m) {
        if (m instanceof IVesselPositionMessage) {
            fields.add(((IVesselPositionMessage) m).getTrueHeading());
        } else {
            fields.add(nullValueForTrueHeading.apply(m));
        }
    }

    private void addDraught(List fields, AisMessage m) {
        if (m instanceof AisMessage5) {
            fields.add(((AisMessage5) m).getDraught()/10f);
        } else {
            fields.add(nullValueForDraught.apply(m));
        }
    }

    private void addName(List fields, AisMessage m) {
        if (m instanceof INameMessage) {
            String name = AisMessage.trimText(((INameMessage) m).getName());
            if (!isBlank(name)) {
                fields.add(name);
            } else {
                fields.add(nullValueForName.apply(m));
            }
        } else {
            fields.add(nullValueForName.apply(m));
        }
    }

    private void addDimBow(List fields, AisMessage m) {
        if (m instanceof IDimensionMessage) {
            fields.add(((IDimensionMessage) m).getDimBow());
        } else {
            fields.add(nullValueForDimBow.apply(m));
        }
    }

    private void addDimPort(List fields, AisMessage m) {
        if (m instanceof IDimensionMessage) {
            fields.add(((IDimensionMessage) m).getDimPort());
        } else {
            fields.add(nullValueForDimPort.apply(m));
        }
    }

    private void addDimStarboard(List fields, AisMessage m) {
        if (m instanceof IDimensionMessage) {
            fields.add(((IDimensionMessage) m).getDimStarboard());
        } else {
            fields.add(nullValueForDimStarboard.apply(m));
        }
    }

    private void addDimStern(List fields, AisMessage m) {
        if (m instanceof IDimensionMessage) {
            fields.add(((IDimensionMessage) m).getDimStern());
        } else {
            fields.add(nullValueForDimStern.apply(m));
        }
    }

    private void addShipTypeCargoTypeCode(List fields, AisMessage m) {
        if (m instanceof AisStaticCommon) {
            fields.add(((AisStaticCommon) m).getShipType());
        } else {
            fields.add(nullValueForShipTypeCargoTypeCode.apply(m));
        }
    }

    private void addShipType(List fields, AisMessage m) {
        if (m instanceof AisStaticCommon) {
            ShipTypeCargo stc = new ShipTypeCargo(((AisStaticCommon) m).getShipType());
            fields.add(stc.getShipType().toString());
        } else {
            fields.add(nullValueForShipType.apply(m));
        }
    }

    private void addShipCargo(List fields, AisMessage m) {
        if (m instanceof AisStaticCommon) {
            ShipTypeCargo stc = new ShipTypeCargo(((AisStaticCommon) m).getShipType());
            fields.add(stc.getShipCargo().toString());
        } else {
            fields.add(nullValueForShipCargo.apply(m));
        }
    }

    private void addCallsign(List fields, AisMessage m) {
        if (m instanceof AisStaticCommon) {
            fields.add(AisMessage.trimText(((AisStaticCommon) m).getCallsign()));
        } else {
            fields.add(nullValueForCallsign.apply(m));
        }
    }

    private void addImo(List fields, AisMessage m) {
        if (m instanceof AisMessage5) {
            long imo = ((AisMessage5) m).getImo();
            if (imo > 0) {
                fields.add(imo);
            } else {
                fields.add(nullValueForImo.apply(m));
            }
        } else {
            fields.add(nullValueForImo.apply(m));
        }
    }

    private void addDestination(List fields, AisMessage m) {
        if (m instanceof AisMessage5) {
            String destination = AisMessage.trimText(((AisMessage5) m).getDest());
            if (!isBlank(destination)) {
                fields.add(destination);
            } else {
                fields.add(nullValueForDestination.apply(m));
            }
        } else {
            fields.add(nullValueForDestination.apply(m));
        }
    }

    private void addEta(List fields, AisMessage m) {
        if (m instanceof AisMessage5) {
            fields.add(formatEta(((AisMessage5) m).getEta()));
        } else {
            fields.add(nullValueForEta.apply(m));
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

    // ---

    protected String formatEta(long eta) {
        /*
        Estimated time of arrival; MMDDHHMM UTC
        Bits 19-16: month; 1-12; 0 = not available = default
        Bits 15-11: day; 1-31; 0 = not available = default
        Bits 10-6: hour; 0-23; 24 = not available = default
        Bits 5-0: minute; 0-59; 60 = not available = default
        For SAR aircraft, the use of this field may be decided by the responsible administration
        */

        int minute = (int) (eta & 63);
        int hour = (int) ((eta >> 6) & 31);
        int day = (int) ((eta >> 11) & 31);
        int month = (int) ((eta >> 16) & 15);
        return formatEta(day, month, hour, minute);
    }

    protected String formatEta(Date eta) {
        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(eta.getTime()), ZoneId.systemDefault());
        return formatEta(time.getDayOfMonth(), time.getMonthValue(), time.getHour(), time.getMinute());
    }

    protected String formatEta(int day, int month, int hour, int minute) {
        return String.format("%02d/%02d %02d:%02d UTC", day, month, hour, minute);
    }

    // ---

    protected void setNullValueForPositionAccuracy(Function<AisMessage, Object> nullValueForPositionAccuracy) {
        this.nullValueForPositionAccuracy = nullValueForPositionAccuracy;
    }

    protected void setNullValueForLatitude(Function<AisMessage, Object> nullValueForLatitude) {
        this.nullValueForLatitude = nullValueForLatitude;
    }

    protected void setNullValueForLongitude(Function<AisMessage, Object> nullValueForLongitude) {
        this.nullValueForLongitude = nullValueForLongitude;
    }

    protected void setNullValueForSog(Function<AisMessage, Object> nullValueForSog) {
        this.nullValueForSog = nullValueForSog;
    }

    protected void setNullValueForCog(Function<AisMessage, Object> nullValueForCog) {
        this.nullValueForCog = nullValueForCog;
    }

    protected void setNullValueForTrueHeading(Function<AisMessage, Object> nullValueForTrueHeading) {
        this.nullValueForTrueHeading = nullValueForTrueHeading;
    }

    protected void setNullValueForDraught(Function<AisMessage, Object> nullValueForDraught) {
        this.nullValueForDraught = nullValueForDraught;
    }

    protected void setNullValueForName(Function<AisMessage, Object> nullValueForName) {
        this.nullValueForName = nullValueForName;
    }

    protected void setNullValueForDimBow(Function<AisMessage, Object> nullValueForDimBow) {
        this.nullValueForDimBow = nullValueForDimBow;
    }

    protected void setNullValueForDimPort(Function<AisMessage, Object> nullValueForDimPort) {
        this.nullValueForDimPort = nullValueForDimPort;
    }

    protected void setNullValueForDimStarboard(Function<AisMessage, Object> nullValueForDimStarboard) {
        this.nullValueForDimStarboard = nullValueForDimStarboard;
    }

    protected void setNullValueForDimStern(Function<AisMessage, Object> nullValueForDimStern) {
        this.nullValueForDimStern = nullValueForDimStern;
    }

    protected void setNullValueForShipTypeCargoTypeCode(Function<AisMessage, Object> nullValueForShipTypeCargoTypeCode) {
        this.nullValueForShipTypeCargoTypeCode = nullValueForShipTypeCargoTypeCode;
    }
    protected void setNullValueForShipType(Function<AisMessage, Object> nullValueForShipType) {
        this.nullValueForShipType = nullValueForShipType;
    }

    protected void setNullValueForShipCargo(Function<AisMessage, Object> nullValueForShipCargo) {
        this.nullValueForShipCargo = nullValueForShipCargo;
    }

    protected void setNullValueForCallsign(Function<AisMessage, Object> nullValueForCallsign) {
        this.nullValueForCallsign = nullValueForCallsign;
    }

    protected void setNullValueForImo(Function<AisMessage, Object> nullValueForImo) {
        this.nullValueForImo = nullValueForImo;
    }

    protected void setNullValueForDestination(Function<AisMessage, Object> nullValueForDestination) {
        this.nullValueForDestination = nullValueForDestination;
    }

    protected void setNullValueForEta(Function<AisMessage, Object> nullValueForEta) {
        this.nullValueForEta = nullValueForEta;
    }

    // ---

    private Function<AisMessage,Object> nullValueForTargetType = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForPositionAccuracy = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForLatitude = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForLongitude = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForSog = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForCog = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForTrueHeading = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForDraught = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForName = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForDimBow = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForDimPort = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForDimStarboard = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForDimStern = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForShipTypeCargoTypeCode = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForShipType = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForShipCargo = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForCallsign = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForImo = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForDestination = m -> NULL_INDICATOR;
    private Function<AisMessage,Object> nullValueForEta = m -> NULL_INDICATOR;

}
