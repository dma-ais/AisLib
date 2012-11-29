/* Copyright (c) 2011 Danish Maritime Safety Administration
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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.proprietary.DmaSourceTag;
import dk.dma.ais.proprietary.IProprietaryFactory;
import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.ais.queue.AisMessageQueue;
import dk.dma.ais.queue.AisMessageQueueOverflowException;
import dk.dma.ais.queue.AisMessageQueueReader;
import dk.dma.ais.queue.IAisMessageQueue;
import dk.dma.ais.queue.IAisQueueEntryHandler;
import dk.dma.ais.sentence.Abk;
import dk.dma.ais.sentence.Sentence;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import dk.dma.enav.messaging.MaritimeMessageHandler;
import dk.dma.enav.messaging.MaritimeMessageMetadata;

/**
 * Abstract base for classes reading from an AIS source. Also handles ABK and a number of proprietary sentences.
 */
public abstract class AisReader extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(AisReader.class);

    // Temporary hack to allow the insertion of the DMA tag to indicate source
    private boolean addDmaTag = false;
    private String sourceName = null;

    public enum Status {
        CONNECTED, DISCONNECTED
    };

    /**
     * List receivers for the AIS messages
     */
    protected List<MaritimeMessageHandler<AisMessage>> handlers = new ArrayList<>();

    /**
     * List of receiver queues
     */
    protected List<IAisMessageQueue> messageQueues = new ArrayList<>();

    /**
     * List of proprietary factories
     */
    protected List<IProprietaryFactory> proprietaryFactories = new ArrayList<>();

    /**
     * A pool of sending threads. A sending thread handles the sending and reception of ABK message.
     */
    protected SendThreadPool sendThreadPool = new SendThreadPool();

    /**
     * A received VDO/VDM
     */
    protected Vdm vdm = new Vdm();

    /**
     * Possible proprietary tags for current VDM
     */
    protected Deque<IProprietaryTag> tags = new ArrayDeque<>();

    /**
     * Add an AIS handler
     * 
     * @param aisHandler
     */
    public void registerHandler(MaritimeMessageHandler<AisMessage> aisHandler) {
        handlers.add(aisHandler);
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
     * Add a proprietary factory
     * 
     * @param proprietaryFactory
     */
    public void addProprietaryFactory(IProprietaryFactory proprietaryFactory) {
        proprietaryFactories.add(proprietaryFactory);
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
    public abstract void close();

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
            out.write(str.getBytes());
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
        AisMessage message;

        // LOG.info("line: " + line);

        // Ignore everything else than sentences
        if (!Sentence.hasSentence(line)) {
            tags.clear();
            return;
        }

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
            tags.clear();
            return;
        }

        // Check if proprietary line
        if (Sentence.hasProprietarySentence(line)) {
            // Go through factories to find one that fits
            for (IProprietaryFactory factory : proprietaryFactories) {
                if (factory.match(line)) {
                    tags.add(factory.getTag(line));
                }
            }
            return;
        }

        // Check for VDM
        if (!Vdm.isVdm(line)) {
            tags.clear();
            return;
        }

        try {
            int result = vdm.parse(line);
            // LOG.info("result = " + result);
            if (result == 0) {
                // Complete message
                message = AisMessage.getInstance(vdm);
                // Maybe add temporary DMA tag
                if (addDmaTag && sourceName != null) {
                    DmaSourceTag dmaSourceTag = new DmaSourceTag();
                    dmaSourceTag.setSourceName(sourceName);
                    tags.add(dmaSourceTag);
                }
                if (tags.size() > 0) {
                    message.setTags(new LinkedList<>(tags));
                }
                for (MaritimeMessageHandler<AisMessage> aisHandler : handlers) {
                    aisHandler.handle(message, new MaritimeMessageMetadata());
                }
                for (IAisMessageQueue queue : messageQueues) {
                    try {
                        queue.push(message);
                    } catch (AisMessageQueueOverflowException e) {
                        LOG.error("Message queue overflow, dropping message: " + e.getMessage());
                    }
                }
            } else {
                // result = 1: Wait for more data
                return;
            }
        } catch (SentenceException se) {
            LOG.debug("Sentence error: " + se.getMessage() + " line: " + line + " tag: "
                    + (tags.size() > 0 ? tags.peekLast() : "null"));
        } catch (AisMessageException ae) {
            LOG.debug("AIS message error: " + ae.getMessage() + " line: " + line + " tag: "
                    + (tags.size() > 0 ? tags.peekLast() : "null"));
        } catch (SixbitException se) {
            LOG.debug("Six bit error: " + se.getMessage() + " line: " + line + " tag: "
                    + (tags.size() > 0 ? tags.peekLast() : "null"));
        }

        vdm = new Vdm();
        tags.clear();
    }

    /**
     * The main read loop
     * 
     * @param stream
     *            the generic input stream to read from
     * @throws IOException
     */
    protected void readLoop(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;

        while ((line = reader.readLine()) != null) {
            handleLine(line);
        }

    }

    public boolean isAddDmaTag() {
        return addDmaTag;
    }

    public void setAddDmaTag(boolean addDmaTag) {
        this.addDmaTag = addDmaTag;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

}
