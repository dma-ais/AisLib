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
package dk.dma.ais.bus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.queue.BlockingMessageQueue;
import dk.dma.ais.queue.IMessageQueue;
import dk.dma.ais.queue.IQueueEntryHandler;
import dk.dma.ais.queue.MessageQueueOverflowException;
import dk.dma.ais.queue.MessageQueueReader;

@ThreadSafe
public abstract class AisBusConsumer extends AisBusSocket implements IQueueEntryHandler<AisBusElement> {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final OverflowLogger overflowLogger = new OverflowLogger(LOG);

    @GuardedBy("this")
    private MessageQueueReader<AisBusElement> consumerThread;
    @GuardedBy("this")
    private int consumerQueueSize = 10000;
    @GuardedBy("this")
    private int consumerPullMaxElements = 1000;

    public AisBusConsumer() {
        super();
    }

    public AisBusConsumer(boolean blocking) {
        super(blocking);
    }

    /**
     * Receive elements from the queue
     */
    @Override
    public final void receive(AisBusElement queueElement) {
        // Do filtering, transformation and filtering
        AisPacket packet = handleReceived(queueElement.getPacket());
        if (packet == null) {
            return;
        }
        queueElement.setPacket(packet);
        receiveFiltered(queueElement);
    }

    /**
     * Push elements onto the queue
     * 
     * @param element
     */
    public final void push(AisBusElement element) {
        try {
            if (blocking) {
                consumerThread.getQueue().put(element);
            } else {
                consumerThread.getQueue().push(element);
            }
        } catch (MessageQueueOverflowException e) {
            status.overflow();
            overflowLogger.log("Consumer overflow [rate=" + status.getOverflowRate() + " packet/sec]");
        } catch (InterruptedException e) {

        }
    }

    @Override
    public synchronized void init() {
        // Create consumer queue
        IMessageQueue<AisBusElement> consumerQueue = new BlockingMessageQueue<>(consumerQueueSize);
        // Make consumer thread
        consumerThread = new MessageQueueReader<>(this, consumerQueue, consumerPullMaxElements);
        super.init();
    }

    @Override
    public synchronized void start() {
        // Start consumerThread
        consumerThread.start();
        super.start();
    }

    @Override
    public void cancel() {
        // Stop consumer thread
        consumerThread.cancel();
        try {
            consumerThread.join(5000);
        } catch (InterruptedException e) {
        }
    }

    /**
     * All consumers must implement a method to get the filtered packet
     * 
     * @param queueElement
     */
    public abstract void receiveFiltered(AisBusElement queueElement);

    public synchronized void setConsumerQueueSize(int consumerQueueSize) {
        this.consumerQueueSize = consumerQueueSize;
    }

    public synchronized void setConsumerPullMaxElements(int consumerPullMaxElements) {
        this.consumerPullMaxElements = consumerPullMaxElements;
    }

}
