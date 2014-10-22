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

import static dk.dma.commons.util.io.IoUtil.writeAscii;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.message.ShipTypeCargo;
import dk.dma.commons.util.io.IoUtil;
import dk.dma.commons.util.io.OutputStreamSink;
import dk.dma.enav.model.geometry.Position;

/**
 * Transform AisPackets into json objects with only specific columns
 * @author Jens Tuxen
 *
 */
public class AisPacketOutputSinkJsonObject extends OutputStreamSink<AisPacket> {
    
    /** The column we are writing. */
    private final String[] columns;
    
    final byte[] separator;
    
    private DecimalFormat df = new DecimalFormat("###.#####");
    
    private boolean first = true;
    
    public static final String ALLCOLUMNS = "mmsi;timestamp;lat;lon;sog;cog;name;dimBow;dimPort;dimStarboard;dimStern;shipType;shipCargo;callsign;targetType";

    private String objectName;
    
    public AisPacketOutputSinkJsonObject(String format) {
        this(format,";","data");
    }
    
    public AisPacketOutputSinkJsonObject(String format, String separator, String objectName) {
       columns = format.split(separator);
       this.objectName = objectName;
       this.separator = requireNonNull(separator).getBytes(StandardCharsets.US_ASCII);
    }

    @Override
    public void process(OutputStream stream, AisPacket message, long count)
            throws IOException {
     
        AisMessage m = message.tryGetAisMessage();
        if (m != null) {
            StringBuilder sb = new StringBuilder();
            if (!first) {
                sb.append(",\n");
            }
            first = false;
            
            
            sb.append('[');
            
            AisStaticCommon common = null;
            ShipTypeCargo stc = null;
            if (m instanceof AisStaticCommon) {
                common = (AisStaticCommon)m;
                stc = new ShipTypeCargo(common.getShipType());
            }
            
            AisPositionMessage im = null;
            Position pos = m.getValidPosition();
            if (m instanceof AisPositionMessage) {
                im = (AisPositionMessage)m;
            }
            
            for (int i= 0; i< columns.length; i++) {
                String c = columns[i];
                
                switch(c) {
                case "mmsi":
                    sb.append('\"').append(m.getUserId()).append('\"');
                    break;
                case "lat":
                    if (im != null && pos != null) {
                        sb.append(df.format(pos.getLatitude()));

                    } else {
                        sb.append("null");   
                    }
                    break;
                case "lon":
                    if (im != null && pos != null) {
                        sb.append(df.format(pos.getLongitude()));
                    } else {
                        sb.append("null");
                    }
                    break;
                case "targetType":
                    sb.append('\"').append(m.getTargetType().toString()).append("\"");
                    break;
                case "sog":
                    if (im != null) {
                        sb.append(im.getSog());
                    } else {
                        sb.append("null");
                    }
                    break;
                case "cog":
                    if (im != null) {
                        sb.append(im.getCog());
                        
                    } else {
                        sb.append("null");
                    }
                    break;
                case "trueHeading":
                    if (im != null) {
                        sb.append(im.getTrueHeading());
                        break;
                    } else {
                        sb.append("null");
                    }
                case "timestamp":
                    sb.append(message.getBestTimestamp());
                    break;
                case "name":
                    if (common != null) {
                        sb.append("\"").append(AisMessage.trimText(common.getName())).append("\"");
                    } else {
                        sb.append("null");
                    }
                    break;
                case "dimBow":
                    if (common != null) {
                        sb.append(common.getDimBow());
                    } else {
                        sb.append("null");
                    }
                    break;
                case "dimPort":
                    if (common != null) {
                        sb.append(common.getDimPort());
                    } else {
                        sb.append("null");
                    }
                    break;
                case "dimStarboard":
                    if (common != null) {
                        sb.append(common.getDimStarboard());
                    } else {
                        sb.append("null");
                    }
                    break;
                case "dimStern":
                    if (common != null) {
                        sb.append(common.getDimStern());
                        
                    } else {
                        sb.append("null");
                    }
                    break;                    
                case "shipType":
                    if (stc != null) {
                        sb.append('\"').append(stc.getShipType().toString()).append("\"");
                    } else {
                        sb.append("null");
                    }
                    break;
                case "shipCargo":
                    if (stc != null) {
                        sb.append('\"').append(stc.getShipCargo().toString()).append("\"");
                    } else {
                        sb.append("null");
                    }             
                    break;
                case "callsign":
                    if (common != null) {
                        sb.append("\"").append(AisMessage.trimText(common.getCallsign())).append("\"");
                    } else {
                        sb.append("null");
                    }
                    break;
                default:
                    sb.append("null");
                }
                
                
                if (i < (columns.length-1)) {
                    sb.append(',');
                }
            }
            
            sb.append("]");
            
            IoUtil.writeAscii(sb, stream);
        }
    }
    
    

    /** {@inheritDoc} */
    @Override
    public void header(OutputStream stream) throws IOException {
        // Writes the name of each column as the header

        StringBuilder sb = new StringBuilder();
        
        sb.append("{\"").append(objectName).append("\": {\n  \"headers\": [");
        
        for (int i = 0; i < columns.length-1; i++) {
            sb.append('\"').append(columns[i]).append("\",");
        }
        // no "," on last element
        sb.append('\"').append(columns[columns.length-1]).append('\"');
        
        sb.append("],\n  \"targets\": [");
        
        IoUtil.writeAscii(sb, stream);

    }
    
    /** {@inheritDoc} */
    @Override
    public void footer(OutputStream stream, long count) throws IOException {
        writeAscii("]\n}}", stream);
    }
    
    
    
    
    
    
    
    

}
