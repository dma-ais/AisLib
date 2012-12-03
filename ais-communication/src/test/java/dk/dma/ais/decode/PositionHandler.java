package dk.dma.ais.decode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.handler.IAisHandler;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisPositionMessage;

public class PositionHandler implements IAisHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PositionHandler.class);

    /**
     * Method to handle incoming AIS messages
     * 
     * For debug out adjust log4j.xml level
     */
    public void receive(AisMessage aisMessage) {
        // Ignore everything but position reports
        if (aisMessage.getMsgId() > 3) {
            return;
        }

        // Just consider the position part
        AisPositionMessage aisPosMessage = (AisPositionMessage) aisMessage;
        LOG.debug(aisPosMessage.toString());

    }

}
