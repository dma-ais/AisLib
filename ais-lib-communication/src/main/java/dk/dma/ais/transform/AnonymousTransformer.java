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

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisBinaryMessage;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.message.binary.AisApplicationMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.ais.sentence.CommentBlock;
import dk.dma.ais.sentence.Vdm;
import dk.dma.enav.model.Country;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * Transformer that anonymizes ais packets
 */
@ThreadSafe
public class AnonymousTransformer implements IAisPacketTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(AnonymousTransformer.class);

    /**
     * List of available MID's
     */
    private static final ArrayList<Integer> MID_LIST = new ArrayList<>(Country.getMidMap().keySet());

    /**
     * List of random names
     */
    private static final ArrayList<String> NAME_LIST = new ArrayList<>();

    /**
     * Location of random names file
     */
    private static final String LOCATION = AnonymousTransformer.class.getPackage().getName().replace(".", "/")
            + "/names.txt";

    /**
     * Load names and make random order
     */
    static {
        URL url = ClassLoader.getSystemResource(LOCATION);
        if (url == null) {
            url = Thread.currentThread().getContextClassLoader().getResource(LOCATION);
        }
        if (url == null) {
            throw new Error("Could not locate " + LOCATION + " on classpath");
        }

        try {
            InputStreamReader in = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(in);
            String line;
            Set<String> names = new HashSet<>();
            while ((line = reader.readLine()) != null) {
                names.add(line.trim().toUpperCase());
            }
            NAME_LIST.addAll(names);
            Collections.shuffle(NAME_LIST);
        } catch (IOException e) {
            throw new Error("Failed to load random names list: " + e.getMessage());
        }
    }

    /**
     * Map from MMSI to anonymized data
     */
    @GuardedBy("this")
    private final Map<Integer, AnonData> anonDataMap = new HashMap<>();

    /**
     * Counter to keep track of MMSI to anonymous MMSI map
     */
    @GuardedBy("this")
    private int counter;

    /**
     * Random number generator
     */
    private final Random rand;

    public AnonymousTransformer() {
        rand = new Random(System.currentTimeMillis());
    }

    @Override
    public AisPacket transform(AisPacket packet) {
        // Get AIS message or discard
        AisMessage message = packet.tryGetAisMessage();
        if (message == null) {
            return null;
        }

        // Get anonymized data for target
        AnonData anonData = getAnonData(message);

        // Transformation for all mesasges
        anonymize(message, anonData);

        // Transformation for general static data
        if (message instanceof AisStaticCommon) {
            anonymize((AisStaticCommon) message, anonData);
        }

        // Transformation for class A static data
        if (message instanceof AisMessage5) {
            anonymize((AisMessage5) message, anonData);
        }

        return createPacket(message, packet);
    }

    private synchronized AnonData getAnonData(AisMessage message) {
        // Try to get from map
        AnonData anonData = anonDataMap.get(message.getUserId());
        if (anonData != null) {
            return anonData;
        }
        // Make anonymized data
        String name = makeName(counter);
        int mmsi = makeMmsi(counter++);
        String callsign = makeCallsign();
        String destination = makeDestination();
        int imoNo = counter;

        anonData = new AnonData(mmsi, name, imoNo, callsign, destination);
        anonDataMap.put(message.getUserId(), anonData);
        return anonData;
    }

    /**
     * Make MMSI from random country and counter
     * 
     * @param id
     * @return
     */
    private int makeMmsi(int id) {
        return MID_LIST.get(rand.nextInt(MID_LIST.size())) * 1000000 + id;
    }

    /**
     * Get random name from list of names
     * 
     * @return
     */
    private String makeName(int id) {
        return NAME_LIST.get(id % NAME_LIST.size());
    }

    /**
     * Make random callsign
     * 
     * @return
     */
    private String makeCallsign() {
        return UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }

    /**
     * Make random destination
     */
    private String makeDestination() {
        return "N/A";
    }

    /**
     * Do transformation basic for all AIS messages
     * 
     * @param message
     */
    private void anonymize(AisMessage message, AnonData anonData) {
        message.setUserId(anonData.getMmsi());
    }

    /**
     * 
     * @param posMessage
     */
    private void anonymize(AisStaticCommon message, AnonData anonData) {
        // Change name
        message.setName(anonData.getName());
        // Callsign
        message.setCallsign(anonData.getCallsign());
    }

    private void anonymize(AisMessage5 message, AnonData anonData) {
        // IMO
        message.setImo(anonData.getImoNo());
        // Dest
        message.setDest(anonData.getDestination());
    }

    /**
     * Make a new packet from old packet and
     * 
     * @param message
     * @param packet
     * @return
     */
    private AisPacket createPacket(AisMessage message, AisPacket packet) {
        List<String> lines = new ArrayList<>(4);
        int sequence = message.getVdm().getSequence();

        // Handle binary message
        if (message instanceof AisBinaryMessage) {
            try {
                AisApplicationMessage appMessage = ((AisBinaryMessage) message).getApplicationMessage();
                if (appMessage == null) {
                    return null;
                }
            } catch (SixbitException e) {
                return null;
            }
        }

        String[] sentences;
        try {
            sentences = Vdm.createSentences(message, sequence);
        } catch (UnsupportedOperationException e) {
            LOG.debug("Encoding not supported by message type " + message.getMsgId());
            return null;
        } catch (SixbitException e) {
            LOG.error("Failed to encode AIS message: " + message + ": " + e.getMessage());
            return null;
        }

        // Add proprietary tags (or not?)
        if (message.getTags() != null) {
            for (IProprietaryTag tag : message.getTags()) {
                lines.add(tag.getSentence());
            }
        }

        // Add comment blocks
        CommentBlock cb = message.getVdm().getCommentBlock();
        if (cb != null) {
            lines.add(cb.encode());
        }

        // Add sentences
        for (String sentence : sentences) {
            lines.add(sentence);
        }

        return AisPacket.from(StringUtils.join(lines, "\r\n"));
    }

    /**
     * Class holding anonymized data for target
     */
    private class AnonData {
        private final int mmsi;
        private final String name;
        private final int imoNo;
        private final String callsign;
        private final String destination;

        public AnonData(int mmsi, String name, int imoNo, String callsign, String destination) {
            this.mmsi = mmsi;
            this.name = name;
            this.imoNo = imoNo;
            this.callsign = callsign;
            this.destination = destination;
        }

        public int getMmsi() {
            return mmsi;
        }

        public String getName() {
            return name;
        }

        public int getImoNo() {
            return imoNo;
        }

        public String getCallsign() {
            return callsign;
        }

        public synchronized String getDestination() {
            return destination;
        }

    }

}
