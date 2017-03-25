package dk.dma.ais.packet;

import dk.dma.ais.sentence.CommentBlock;
import dk.dma.ais.sentence.EncapsulatedSentence;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.SentenceLine;

/**
 * Represents a VSI sentence.
 */
public class Vsi extends EncapsulatedSentence {
    private int signalStrength;
    private double latitude;
    private double longitude;

    @Override
    public int parse(SentenceLine sentenceLine) throws SentenceException {
        if (sentenceLine.getPrefix().length() > 0 && CommentBlock.hasCommentBlock(sentenceLine.getPrefix())) {
            addSingleCommentBlock(sentenceLine.getPrefix());
        }

        signalStrength = Integer.valueOf(sentenceLine.getFields().get(5));

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
