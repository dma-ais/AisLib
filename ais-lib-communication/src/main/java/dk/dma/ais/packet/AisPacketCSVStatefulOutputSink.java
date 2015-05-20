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

import dk.dma.ais.data.AisClassAStatic;
import dk.dma.ais.data.AisTarget;
import dk.dma.ais.data.AisTargetDimensions;
import dk.dma.ais.data.AisVesselPosition;
import dk.dma.ais.data.AisVesselStatic;
import dk.dma.ais.data.AisVesselTarget;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.tracker.targetTracker.TargetInfo;
import dk.dma.ais.tracker.targetTracker.TargetTracker;
import dk.dma.enav.model.geometry.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Transform AisPackets into stateful csv objects with only specific columns
 *
 * @author Thomas Borg Salling
 */
public class AisPacketCSVStatefulOutputSink extends AisPacketCSVOutputSink {

    private static final Logger LOG = LoggerFactory.getLogger(AisPacketCSVStatefulOutputSink.class);
    private Function<AisMessage, Object> aisMessageObjectFunction;

    {
        LOG.info("AisPacketCSVStatefulOutputSink created.");
    }

    private TargetTracker tracker = new TargetTracker();

    public AisPacketCSVStatefulOutputSink() {
        this("timestamp_pretty;timestamp;targetType;mmsi;msgid;posacc;lat;lon;sog;cog;draught;name;dimBow;dimPort;dimStarboard;dimStern;shipTypeCargoTypeCode;shipType;shipCargo;destination;eta;imo;callsign", ";");
    }

    public AisPacketCSVStatefulOutputSink(String format) {
        this(format, ";");
    }

    public AisPacketCSVStatefulOutputSink(String format, String separator) {
        super(format, separator);
        setNullValueForPositionAccuracy(cachedPositionAccuracy);
        setNullValueForLatitude(cachedLatitude);
        setNullValueForLongitude(cachedLongitude);
        setNullValueForSog(cachedSog);
        setNullValueForCog(cachedCog);
        setNullValueForTrueHeading(cachedTrueHeading);
        setNullValueForDraught(cachedDraught);
        setNullValueForName(cachedName);
        setNullValueForDimBow(cachedDimBow);
        setNullValueForDimPort(cachedDimPort);
        setNullValueForDimStarboard(cachedDimStarboard);
        setNullValueForDimStern(cachedDimStern);
        setNullValueForShipTypeCargoTypeCode(cachedShipTypeCargoTypeCode);
        setNullValueForShipType(cachedShipType);
        setNullValueForShipCargo(cachedShipCargo);
        setNullValueForCallsign(cachedCallsign);
        setNullValueForImo(cachedImo);
        setNullValueForDestination(cachedDestination);
        setNullValueForEta(cachedEta);
    }

    @Override
    public void process(OutputStream stream, AisPacket packet, long count) throws IOException {
        tracker.update(packet);
        if (LOG.isDebugEnabled() && count % 100000 == 0) {
            LOG.info("Tracking " + tracker.count(p -> true) + " tracks.");
        }
        super.process(stream, packet, count);
    }

    private Object extractFromDynamics(AisMessage m, Function<AisVesselPosition, Object> getter) {
        TargetInfo targetInfo = tracker.get(m.getUserId());
        if (targetInfo != null) {
            AisTarget aisTarget = targetInfo.getAisTarget();
            if (aisTarget instanceof AisVesselTarget) {
                AisVesselPosition vesselPosition = ((AisVesselTarget) aisTarget).getVesselPosition();
                if (vesselPosition != null) {
                    Object value = getter.apply(vesselPosition);
                    if (value != null) {
                        if (value instanceof Double) {
                            value = ((Double) value).floatValue();
                        }
                        return value;
                    }
                }
            }
        }
        return NULL_INDICATOR;
    }

    private Object extractFromPosition(AisMessage m, Function<Position, Object> getter) {
        return extractFromDynamics(m, dynamics -> {
            Position position = dynamics.getPos();
            return position == null ? NULL_INDICATOR : getter.apply(position);
        });
    }

    private Object extractFromStatic(AisMessage m, Function<AisVesselStatic, Object> getter) {
        TargetInfo targetInfo = tracker.get(m.getUserId());
        if (targetInfo != null) {
            AisTarget aisTarget = targetInfo.getAisTarget();
            if (aisTarget instanceof AisVesselTarget && ((AisVesselTarget) aisTarget).getVesselStatic() != null) {
                return getter.apply(((AisVesselTarget) aisTarget).getVesselStatic());
            }
        }
        return NULL_INDICATOR;
    }

    private Object extractFromDimensions(AisMessage m, Function<AisTargetDimensions, Object> getter) {
        return extractFromStatic(m, s -> {
            AisTargetDimensions dimensions = s.getDimensions();
            if (dimensions != null) {
                return getter.apply(dimensions);
            } else {
                return NULL_INDICATOR;
            }
        });
    }

    private Function<AisMessage, Object> cachedPositionAccuracy = m -> extractFromDynamics(m, dynamics -> dynamics.getPosAcc());

    private Function<AisMessage, Object> cachedLatitude = m -> extractFromPosition(m, position -> position.getLatitude());

    private Function<AisMessage, Object> cachedLongitude = m -> extractFromPosition(m, position -> position.getLatitude());

    private Function<AisMessage, Object> cachedSog = m -> extractFromDynamics(m, dynamics -> dynamics.getSog());

    private Function<AisMessage, Object> cachedCog = m -> extractFromDynamics(m, dynamics -> dynamics.getCog());

    private Function<AisMessage, Object> cachedTrueHeading = m -> extractFromDynamics(m, dynamics -> dynamics.getHeading());

    private Function<AisMessage, Object> cachedDimBow = m -> extractFromDimensions(m, d -> d.getDimBow());

    private Function<AisMessage, Object> cachedDimPort = m -> extractFromDimensions(m, d -> d.getDimPort());

    private Function<AisMessage, Object> cachedDimStarboard = m -> extractFromDimensions(m, d -> d.getDimStarboard());

    private Function<AisMessage, Object> cachedDimStern = m -> extractFromDimensions(m, d -> d.getDimStern());

    private Function<AisMessage, Object> cachedShipTypeCargoTypeCode = m -> extractFromStatic(m, s -> s.getShipTypeCargo().getCode());

    private Function<AisMessage, Object> cachedShipType = m -> extractFromStatic(m, s -> s.getShipTypeCargo().getShipType().toString());

    private Function<AisMessage, Object> cachedShipCargo = m -> extractFromStatic(m, s -> s.getShipTypeCargo().getShipCargo().toString());

    private Function<AisMessage, Object> cachedCallsign = m -> extractFromStatic(m, s -> s.getCallsign());

    private Function<AisMessage, Object> cachedName = m -> extractFromStatic(m, s -> {
        String name = s.getName();
        name = name != null ? name.trim() : null;
        return isBlank(name) ? NULL_INDICATOR : name;
    });

    private Function<AisMessage, Object> cachedDraught = m -> extractFromStatic(m, s -> {
        if (s instanceof AisClassAStatic) {
            Double draught = ((AisClassAStatic) s).getDraught();
            if (draught != null) {
                return draught;
            }
        }
        return NULL_INDICATOR;
    });

    private Function<AisMessage, Object> cachedImo = m -> extractFromStatic(m, s -> {
        if (s instanceof AisClassAStatic) {
            Integer imo = ((AisClassAStatic) s).getImoNo();
            if (imo != null && imo > 0) {
                return imo;
            }
        }
        return NULL_INDICATOR;
    });

    private Function<AisMessage, Object> cachedDestination = m -> extractFromStatic(m, s -> {
        if (s instanceof AisClassAStatic) {
            String destination = ((AisClassAStatic) s).getDestination();
            if (!isBlank(destination)) {
                return destination;
            }
        }
        return NULL_INDICATOR;
    });

    private Function<AisMessage, Object> cachedEta = m -> extractFromStatic(m, s -> {
        if (s instanceof AisClassAStatic) {
            Date eta = ((AisClassAStatic) s).getEta();
            if (eta != null) {
                return formatEta(eta);
            }
        }
        return NULL_INDICATOR;
    });

}
