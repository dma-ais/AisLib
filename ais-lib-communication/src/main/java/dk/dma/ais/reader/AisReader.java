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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketInputStream;
import dk.dma.ais.queue.BlockingMessageQueue;
import dk.dma.ais.queue.IMessageQueue;
import dk.dma.ais.queue.IQueueEntryHandler;
import dk.dma.ais.queue.MessageQueueOverflowException;
import dk.dma.ais.queue.MessageQueueReader;
import dk.dma.ais.sentence.Abk;
import dk.dma.commons.management.ManagedAttribute;
import dk.dma.commons.management.ManagedResource;
import dk.dma.commons.util.io.OutputStreamSink;
import dk.dma.enav.util.function.Consumer;
import dk.dma.enav.util.function.Predicate;

/**
 * Abstract base for classes reading from an AIS source. Also handles ABK and a number of proprietary sentences.
 */
@ManagedResource
public abstract class AisReader extends Thread {

    static final Logger LOG = LoggerFactory.getLogger(AisReader.class);

    public enum Status {
        CONNECTED, DISCONNECTED
    };

    /** Flag that indicates the reader should shutdown */
    final CountDownLatch shutdownLatch = new CountDownLatch(1);

    /** List receivers for the AIS messages. */
    protected final CopyOnWriteArrayList<Consumer<AisMessage>> handlers = new CopyOnWriteArrayList<>();

    /** List of packet handlers. */
    protected final CopyOnWriteArrayList<Consumer<? super AisPacket>> packetHandlers = new CopyOnWriteArrayList<>();

    /** A pool of sending threads. A sending thread handles the sending and reception of ABK message. */
    protected final SendThreadPool sendThreadPool = new SendThreadPool();

    /** The number of bytes read by this reader. */
    private final AtomicLong bytesRead = new AtomicLong();

    /** The number of bytes written by this reader. */
    private final AtomicLong bytesWritten = new AtomicLong();

    /** The number of lines read by this reader. */
    private final AtomicLong linesRead = new AtomicLong();

    /** The source id. */
    private String sourceId;

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
    public void registerHandler(Consumer<AisMessage> aisHandler) {
        handlers.add(aisHandler);
    }

    /**
     * Add a packet handler
     * 
     * @param packetHandler
     */
    public void registerOutputStreamSink(final OutputStreamSink<AisPacket> oss, final OutputStream output) {
        requireNonNull(oss);
        requireNonNull(output);
        packetHandlers.add(new Consumer<AisPacket>() {
            @Override
            public void accept(AisPacket t) {
                try {
                    oss.process(output, t);
                } catch (IOException e) {
                    LOG.error("Error processing packet:" + t.getStringMessage(), e);
                }
            }
        });
    }

    /**
     * Add a packet handler
     * 
     * @param packetHandler
     */
    public void registerPacketHandler(final Predicate<? super AisPacket> predicate,
            final Consumer<? super AisPacket> packetConsumer) {
        requireNonNull(predicate);
        requireNonNull(packetConsumer);
        registerPacketHandler(new Consumer<AisPacket>() {
            @Override
            public void accept(AisPacket t) {
                if (predicate.test(t)) {
                    packetConsumer.accept(t);
                }
            }
        });
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
     * Method to send addressed or broadcast AIS messages (ABM or BBM).
     * 
     * @param sendRequest
     * @param resultListener
     *            A class to handle the result when it is ready.
     */
    public abstract void send(SendRequest sendRequest, Consumer<Abk> resultListener) throws SendException;

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
    @ManagedAttribute
    public abstract Status getStatus();

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

    /**
     * The main read loop
     * 
     * @param stream
     *            the generic input stream to read from
     * @throws IOException
     */
    protected void readLoop(InputStream stream) throws IOException {
        AisPacketInputStream s = new AisPacketInputStream(stream) {
            @Override
            protected void handleAbk(Abk abk) {
                sendThreadPool.handleAbk(abk);
            }
        };
        AisPacket packet = null;
        while ((packet = s.readPacket(sourceId)) != null) {
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
    }

    /**
     * Stop the reader
     */
    public void stopReader() {
        shutdownLatch.countDown();
        this.interrupt();
    }

    protected boolean isShutdown() {
        return shutdownLatch.getCount() == 0;
    }

    @ManagedAttribute
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

}
