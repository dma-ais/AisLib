package dk.dma.ais.bus;

import dk.dma.ais.queue.IQueueEntryHandler;

public abstract class AisBusConsumer extends AisBusSocket implements IQueueEntryHandler<AisBusEntry> {

    public AisBusConsumer(AisBus aisBus) {
        super(aisBus);
    }

}
