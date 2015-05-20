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
package dk.dma.ais.transform;

import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.sentence.Vdm;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Transformer that strips everything from the packet except the raw VDM sentences
 */
@ThreadSafe
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
        return AisPacket.from(StringUtils.join(newLines, "\r\n"));
    }

}
