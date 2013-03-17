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
package dk.dma.ais.transform;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.sentence.Vdm;

/**
 * Transformer that strips everything from the packet except the raw VDM sentences 
 */
public class CropVdmTransformer implements IAisPacketTransformer {

    @Override
    public AisPacket transform(AisPacket packet) {
        List<String> newLines = new ArrayList<>();
        List<String> orgLines = AisPacketTaggingTransformer.cropSentences(packet.getStringMessageLines(), true);
        for (String line : orgLines) {
            if (Vdm.isVdm(line)) {
                newLines.add(line);
            }
        }
        return new AisPacket(StringUtils.join(newLines, "\r\n"), packet.getReceiveTimestamp());
    }
    
}
