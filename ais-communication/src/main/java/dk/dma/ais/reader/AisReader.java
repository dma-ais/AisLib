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
package dk.dma.ais.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.proprietary.DmaSourceTag;
import dk.dma.ais.queue.AisMessageQueue;
import dk.dma.ais.queue.AisMessageQueueOverflowException;
import dk.dma.ais.queue.AisMessageQueueReader;
import dk.dma.ais.queue.IAisMessageQueue;
import dk.dma.ais.queue.IAisQueueEntryHandler;
import dk.dma.ais.sentence.Abk;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.commons.management.ManagedAttribute;
import dk.dma.commons.util.io.CountingInputStream;
import dk.dma.enav.messaging.MaritimeMessageHandler;

/**
 * Abstract base for classes reading from an AIS source. Also handles ABK and a number of proprietary sentences.
 */
public abstract class AisReader extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(AisReader.class);

    // Temporary hack to allow the insertion of the DMA tag to indicate source
    private boolean addDmaTag;

    public enum Status {
        CONNECTED, DISCONNECTED
    };

    /** Reader to parse lines and deliver complete AIS packets. */
    protected final AisPacketReader packetReader = new AisPacketReader();

    /** List receivers for the AIS messages. */
    protected final List<MaritimeMessageHandler<AisMessage>> handlers = new ArrayList<>();

    /** List of receiver queues. */
    protected final List<IAisMessageQueue> messageQueues = new ArrayList<>();

    /** List of packet handlers. */
    protected final List<IAisPacketHandler> packetHandlers = new ArrayList<>();

    /** A pool of sending threads. A sending thread handles the sending and reception of ABK message. */
    protected final SendThreadPool sendThreadPool = new SendThreadPool();

    /** The number of bytes read by this reader. */
    private final AtomicLong bytesRead = new AtomicLong();

    /** The number of bytes written by this reader. */
    private final AtomicLong bytesWritten = new AtomicLong();

    /** The number of lines read by this reader. */
    private final AtomicLong linesRead = new AtomicLong();

    @ManagedAttribute
    public long getNumberOfBytesWritten() {
        return bytesWritten.get();
    }

    @ManagedAttribute
    public long getNumberOfBytesRead() {
        return bytesRead.get();
    }

    @ManagedAttribute
    public long getNumberOfLinesRead() {
        return linesRead.get();
    }


    /**
     * Add an AIS handler
     * 
     * @param aisHandler
     */
    public void registerHandler(MaritimeMessageHandler<AisMessage> aisHandler) {
        handlers.add(aisHandler);
    }

    /**
     * Add a packet handler
     * 
     * @param packetHandler
     */
    public void registerPacketHandler(IAisPacketHandler packetHandler) {
        packetHandlers.add(packetHandler);
    }

    /**
     * Add a queue for receiving messages
     * 
     * @param queue
     */
    public void registerQueue(IAisMessageQueue queue) {
        messageQueues.add(queue);
    }

    /**
     * Make a new queue and reader for the queue. Start and attach to to given handler.
     * 
     * @param handler
     */
    public void registerQueueHandler(IAisQueueEntryHandler handler) {
        AisMessageQueueReader queueReader = new AisMessageQueueReader(handler, new AisMessageQueue());
        registerQueue(queueReader.getQueue());
        queueReader.start();
    }

    /**
     * Method to send addressed or broadcast AIS messages (ABM or BBM).
     * 
     * @param sendRequest
     * @param resultListener
     *            A class to handle the result when it is ready.
     */
    public abstract void send(SendRequest sendRequest, ISendResultListener resultListener) throws SendException;

    /**
     * Blocking method to send message in an easy way
     * 
     * @param aisMessage
     * @param sequence
     * @param destination
     * @param timeout
     * @return
     * @throws InterruptedException
     * @throws SendException
     */
    public Abk send(AisMessage aisMessage, int sequence, int destination, int timeout) throws SendException,
    InterruptedException {
        SendRequest sendRequest = new SendRequest(aisMessage, sequence, destination);
        ClientSendThread clientSendThread = new ClientSendThread(this, sendRequest);
        return clientSendThread.send();
    }

    /**
     * Sending with 60 sec default timeout
     * 
     * @param aisMessage
     * @param sequence
     * @param destination
     * @return
     * @throws SendException
     * @throws InterruptedException
     */
    public Abk send(AisMessage aisMessage, int sequence, int destination) throws SendException, InterruptedException {
        return send(aisMessage, sequence, destination, 60000);
    }

    /**
     * Get the status of the connection, either connected or disconnected
     * 
     * @return status
     */
    public abstract Status getStatus();

    /**
     * Stop the reading thread
     */
    public abstract void stopReader();

    /**
     * The method to do the actual sending
     * 
     * @param sendRequest
     * @param resultListener
     * @param out
     * @throws SendException
     */
    protected void doSend(SendRequest sendRequest, ISendResultListener resultListener, OutputStream out)
            throws SendException {
        if (out == null) {
            throw new SendException("Not connected");
        }

        // Get sentences
        String[] sentences = sendRequest.createSentences();

        // Create and start thread
        SendThread sendThread = sendThreadPool.createSendThread(sendRequest, resultListener);

        // Write to out
        String str = StringUtils.join(sentences, "\r\n") + "\r\n";
        LOG.debug("Sending:\n" + str);
        try {
            byte[] bytes = str.getBytes();
            out.write(bytes);
            bytesWritten.addAndGet(bytes.length);
        } catch (IOException e) {
            throw new SendException("Could not send AIS message: " + e.getMessage());
        }

        // Start send thread
        sendThread.start();
    }

    /**
     * Handle a received line
     * 
     * @param line
     */
    protected void handleLine(String line) {
        linesRead.incrementAndGet();
        // Check for ABK
        if (Abk.isAbk(line)) {
            LOG.debug("Received ABK: " + line);
            Abk abk = new Abk();
            try {
                abk.parse(line);
                sendThreadPool.handleAbk(abk);
            } catch (Exception e) {
                LOG.error("Failed to parse ABK: " + line + ": " + e.getMessage());
            }
            packetReader.newVdm();
            return;
        }

        AisPacket packet;
        try {
            packet = packetReader.readLine(line);
        } catch (SentenceException se) {
            LOG.info("Sentence error: " + se.getMessage() + " line: " + line);
            return;
        }

        // No complete packet yet
        if (packet == null) {
            return;
        }

        // Maybe add temporary DMA tag
        if (addDmaTag && packetReader.getSourceName() != null) {
            DmaSourceTag dmaSourceTag = new DmaSourceTag();
            dmaSourceTag.setSourceName(packetReader.getSourceName());
            packet.getVdm().setTag(dmaSourceTag);
        }

        // Distribute packet
        for (IAisPacketHandler packetHandler : packetHandlers) {
            packetHandler.receivePacket(packet);
        }

        // Distribute AIS message
        if (handlers.size() > 0 || messageQueues.size() > 0) {
            AisMessage message = null;
            // Parse AIS message
            try {
                message = AisMessage.getInstance(packet.getVdm());
            } catch (AisMessageException me) {
                LOG.info("AIS message exception: " + me.getMessage() + " vdm: " + packet.getVdm().getOrgLinesJoined());
            } catch (SixbitException se) {
                LOG.info("Sixbit error: " + se.getMessage() + " vdm: " + packet.getVdm().getOrgLinesJoined());
            }
            if (message == null) {
                return;
            }

            // Distribute message
            for (MaritimeMessageHandler<AisMessage> aisHandler : handlers) {
                aisHandler.handle(message);
            }
            for (IAisMessageQueue queue : messageQueues) {
                try {
                    queue.push(message);
                } catch (AisMessageQueueOverflowException e) {
                    LOG.error("Message queue overflow, dropping message: " + e.getMessage());
                }
            }
        }

    }

    /**
     * The main read loop
     * 
     * @param stream
     *            the generic input stream to read from
     * @throws IOException
     */
    protected void readLoop(InputStream stream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new CountingInputStream(stream, bytesRead)))) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                handleLine(line);
            }
        }
    }

    public boolean isAddDmaTag() {
        return addDmaTag;
    }

    public void setAddDmaTag(boolean addDmaTag) {
        this.addDmaTag = addDmaTag;
    }

    public String getSourceName() {
        return packetReader.getSourceName();
    }

    public void setSourceName(String sourceName) {
        packetReader.setSourceName(sourceName);
    }
}
