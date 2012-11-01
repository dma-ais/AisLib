package dk.dma.enav.ais.decode;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.enav.ais.message.AisMessage;
import dk.dma.enav.ais.message.AisMessage4;
import dk.dma.enav.ais.proprietary.IProprietarySourceTag;
import dk.dma.enav.messaging.MessageHandler;
import dk.dma.enav.messaging.MessageMetadata;

/**
 * Example AIS message handler that saves MMSI for the base stations observed
 * 
 * For debug out adjust log4j.xml level
 * 
 * @author obo
 * 
 */
public class BaseReportHandler implements MessageHandler<AisMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseReportHandler.class);

    private final Set<Long> baseStations = new HashSet<>();
    private final Set<Long> baseStationOrigins = new HashSet<>();

    public void process(AisMessage aisMessage, MessageMetadata medatadata) {
        // Try to get proprietary source tag and evaluate base station origin
        IProprietarySourceTag sourceTag = aisMessage.getSourceTag();
        if (sourceTag != null) {
            baseStationOrigins.add(sourceTag.getBaseMmsi());
            LOG.debug("Observed base station origins: " + baseStationOrigins.size());
        }

        // Only handle base station reports
        if (aisMessage.getMsgId() != 4) {
            return;
        }

        // Cast to message 4 and save user id in hash set
        AisMessage4 msg4 = (AisMessage4) aisMessage;
        baseStations.add(msg4.getUserId());
        LOG.debug("Observed base stations: " + baseStations.size());
    }

    public Set<Long> getBaseStations() {
        return baseStations;
    }

    public Set<Long> getBaseStationOrigins() {
        return baseStationOrigins;
    }

}
