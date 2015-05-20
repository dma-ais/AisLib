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

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.sentence.Sentence;
import dk.dma.ais.sentence.SentenceException;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Transformer that changes VDM/VDO to either VDM or VDO based on a given MMSI. If talker is given the talker is also
 * changed for own messages.
 */
@ThreadSafe
public class VdmVdoTransformer implements IAisPacketTransformer {

    private static final Pattern VDM_PATTERN = Pattern.compile("!..VD(M|O)", Pattern.DOTALL);

    private final int ownMmsi;
    private final String ownTalker;

    public VdmVdoTransformer(int ownMmsi) {
        this(ownMmsi, null);
    }

    public VdmVdoTransformer(int ownMmsi, String ownTalker) {
        this.ownMmsi = ownMmsi;
        this.ownTalker = ownTalker;
    }

    @Override
    public AisPacket transform(AisPacket packet) {
        AisMessage message = packet.tryGetAisMessage();
        if (message == null) {
            return null;
        }
        boolean own = message.getUserId() == ownMmsi;

        // No need to change
        if (message.getVdm().isOwnMessage() == own) {
            return packet;
        }

        // Split lines
        List<String> lines = packet.getStringMessageLines();
        for (int i = 0; i < lines.size(); i++) {
            // Find where VDO/VDM starts and replace last character
            String line = lines.get(i);
            Matcher matcher = VDM_PATTERN.matcher(line);
            if (!matcher.find()) {
                continue;
            }
            int start = matcher.start();
            line = line.substring(0, start + 5) + (own ? 'O' : 'M') + line.substring(start + 6);

            // Maybe do talker transformation
            if (own && ownTalker != null) {
                line = line.substring(0, start + 1) + ownTalker + line.substring(start + 3);
            }

            // Calculate checksum
            int checksum;
            try {
                checksum = Sentence.getChecksum(line.substring(start));
            } catch (SentenceException e) {
                e.printStackTrace();
                return null;
            }
            String strChecksum = Sentence.getStringChecksum(checksum);

            // Find where checksum starts and replace checksum
            start = line.indexOf('*', start);
            line = line.substring(0, start + 1) + strChecksum + line.substring(start + 3);

            lines.set(i, line);
        }

        return AisPacket.from(StringUtils.join(lines, "\r\n"));
    }

}
