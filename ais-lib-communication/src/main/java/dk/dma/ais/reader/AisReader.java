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
package dk.dma.ais.reader;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketReader;
import dk.dma.ais.packet.AisPacketStream;
import dk.dma.ais.packet.AisPacketTags;
import dk.dma.ais.queue.BlockingMessageQueue;
import dk.dma.ais.queue.IMessageQueue;
import dk.dma.ais.queue.IQueueEntryHandler;
import dk.dma.ais.queue.MessageQueueOverflowException;
import dk.dma.ais.queue.MessageQueueReader;
import dk.dma.ais.sentence.Abk;
import dk.dma.ais.transform.AisPacketTaggingTransformer;
import dk.dma.ais.transform.AisPacketTaggingTransformer.Policy;
import dk.dma.commons.management.ManagedAttribute;
import dk.dma.commons.management.ManagedResource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * Abstract base for classes reading from an AIS source. Also handles ABK and a number of proprietary sentences.
 */
@ManagedResource
public abstract class AisReader extends Thread {

    static final Logger LOG = LoggerFactory.getLogger(AisReader.class);

    /** The number of bytes read by this reader. */
    private final AtomicLong bytesRead = new AtomicLong();;

    /** The number of bytes written by this reader. */
    private final AtomicLong bytesWritten = new AtomicLong();

    /** List receivers for the AIS messages. */
    protected final CopyOnWriteArrayList<Consumer<AisMessage>> handlers = new CopyOnWriteArrayList<>();

    /** The number of lines read by this reader. */
    private final AtomicLong linesRead = new AtomicLong();

    /** List of packet handlers. */
    protected final CopyOnWriteArrayList<Consumer<? super AisPacket>> packetHandlers = new CopyOnWriteArrayList<>();

    /** A pool of sending threads. A sending thread handles the sending and reception of ABK message. */
    protected final SendThreadPool sendThreadPool = new SendThreadPool();

    /** Flag that indicates the reader should shutdown */
    final CountDownLatch shutdownLatch = new CountDownLatch(1);

    /** The source id. */
    private String sourceId;
    
    /** Transformer adding source id */
    private AisPacketTaggingTransformer transformer;

    /**
     * The method to do the actual sending
     * 
     * @param sendRequest
     * @param resultListener
     * @param out
     * @throws SendException
     */
    protected void doSend(SendRequest sendRequest, Consumer<Abk> resultListener, OutputStream out) throws SendException {
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

    @ManagedAttribute
    public long getNumberOfBytesRead() {
        return bytesRead.get();
    }

    @ManagedAttribute
    public long getNumberOfBytesWritten() {
        return bytesWritten.get();
    }

    @ManagedAttribute
    public long getNumberOfLinesRead() {
        return linesRead.get();
    }

    @ManagedAttribute
    public String getSourceId() {
        return sourceId;
    }

    /**
     * Get the status of the connection, either connected or disconnected
     * 
     * @return status
     */
    @ManagedAttribute
    public abstract Status getStatus();

    protected boolean isShutdown() {
        return shutdownLatch.getCount() == 0;
    }

    /**
     * The main read loop to use if sentences in stream
     * 
     * @param stream
     *            the generic input stream to read from
     * @throws IOException
     */
    protected void readLoop(InputStream stream) throws IOException {
        try (AisPacketReader s = new AisPacketReader(stream) {
            @Override
            protected void handleAbk(Abk abk) {
                sendThreadPool.handleAbk(abk);
            }
        }) {
            AisPacket packet = null;
            while ((packet = s.readPacket()) != null) {
                distribute(packet);
            }
        }
    }
    
    protected void distribute(AisPacket packet) {
        linesRead.incrementAndGet();
        
        if (transformer != null) { // tag the packet with the source if non-null
            packet = transformer.transform(packet);
        }

        for (Consumer<? super AisPacket> packetHandler : packetHandlers) {
            packetHandler.accept(packet);
        }

        // Distribute AIS message
        if (handlers.size() > 0) {
            AisMessage message = null;
            // Parse AIS message
            try {
                message = packet.getAisMessage();
            } catch (AisMessageException me) {
                LOG.info("AIS message exception: " + me.getMessage() + " vdm: "
                        + packet.getVdm().getOrgLinesJoined());
            } catch (SixbitException se) {
                LOG.info("Sixbit error: " + se.getMessage() + " vdm: " + packet.getVdm().getOrgLinesJoined());
            }
            if (message != null) { // Distribute message
                for (Consumer<AisMessage> aisHandler : handlers) {
                    aisHandler.accept(message);
                }
            }
        }
    }

    /**
     * Add an AIS handler
     * 
     * @param aisHandler
     */
    public void registerHandler(Consumer<AisMessage> aisHandler) {
        handlers.add(aisHandler);
    }

    /**
     * Add a packet handler
     * 
     * @param packetHandler
     */
    public void registerPacketHandler(Consumer<? super AisPacket> packetConsumer) {
        packetHandlers.add(packetConsumer);
    }

    /**
     * Add a queue for receiving messages
     * 
     * @param queue
     */
    public void registerQueue(final IMessageQueue<AisMessage> queue) {
        requireNonNull(queue);
        registerHandler(new Consumer<AisMessage>() {
            @Override
            public void accept(AisMessage message) {
                try {
                    queue.push(message);
                } catch (MessageQueueOverflowException e) {
                    LOG.error("Message queue overflow, dropping message: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Make a new queue and reader for the queue. Start and attach to to given handler.
     * 
     * @param handler
     */
    public void registerQueueHandler(IQueueEntryHandler<AisMessage> handler) {
        MessageQueueReader<AisMessage> queueReader = new MessageQueueReader<>(handler,
                new BlockingMessageQueue<AisMessage>());
        registerQueue(queueReader.getQueue());
        queueReader.start();
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
     * Method to send addressed or broadcast AIS messages (ABM or BBM).
     * 
     * @param sendRequest
     * @param resultListener
     *            A class to handle the result when it is ready.
     */
    public abstract void send(SendRequest sendRequest, Consumer<Abk> resultListener) throws SendException;

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
        if (sourceId != null) {
            AisPacketTags tagging = new AisPacketTags();
            tagging.setSourceId(sourceId);
            transformer = new AisPacketTaggingTransformer(Policy.PREPEND_MISSING, tagging);
        } else {
            transformer = null;
        }
    }

    /**
     * Stop the reader
     */
    public void stopReader() {
        shutdownLatch.countDown();
        this.interrupt();
    }

    /**
     * Returns a ais packet stream.
     * 
     * @return the stream
     */
    public AisPacketStream stream() {
        final AisPacketStream s = AisPacketStream.newStream();
        registerPacketHandler(new Consumer<AisPacket>() {
            public void accept(AisPacket p) {
                s.add(p);
            }
        });
        return s.immutableStream();// Only adds from reader
    }

    public enum Status {
        CONNECTED, DISCONNECTED
    }
}
