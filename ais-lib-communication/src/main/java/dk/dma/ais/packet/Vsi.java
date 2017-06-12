package dk.dma.ais.packet;

import dk.dma.ais.sentence.CommentBlock;
import dk.dma.ais.sentence.EncapsulatedSentence;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.SentenceLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a VSI sentence.
 */
public class Vsi extends EncapsulatedSentence {
    private static final Logger LOG = LoggerFactory.getLogger(Vsi.class);
    private static final int SIGNAL_STRENGTH_DEFAULT_VALUE = -10000;

    private int signalStrength;
    private double latitude;
    private double longitude;

    @Override
    public int parse(SentenceLine sentenceLine) throws SentenceException {
        if (sentenceLine.getPrefix().length() > 0 && CommentBlock.hasCommentBlock(sentenceLine.getPrefix())) {
            addSingleCommentBlock(sentenceLine.getPrefix());
        }

        try {
            signalStrength = Integer.valueOf(sentenceLine.getFields().get(5));
        } catch (NumberFormatException e) {
            signalStrength = SIGNAL_STRENGTH_DEFAULT_VALUE;
            LOG.error("Invalid value for signal strength in VSI sentence: [{}]. Using default value [{}]", sentenceLine, SIGNAL_STRENGTH_DEFAULT_VALUE);
        }

        return 0;
    }

    @Override
    public String getEncoded() {
        return null;
    }

    public int getSignalStrength() {
        return signalStrength;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
