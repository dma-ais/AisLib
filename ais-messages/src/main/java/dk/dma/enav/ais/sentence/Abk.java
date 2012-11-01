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
package dk.dma.enav.ais.sentence;

import dk.dma.enav.ais.binary.SixbitException;

/**
 * Addressed Binary Acknowledgement as defined by IEC 61162
 */
public class Abk extends ParametricSentence {

    /**
     * Type of acknowledgement
     */
    public enum Result {
        ADDRESSED_SUCCESS(0), ADDRESSED_NO_ACKNOWLEDGE(1), COULD_NOT_BROADCAST(2), BROADCAST_SENT(3), LATE_RECEPTION(4);
        private int res;

        private Result(int res) {
            this.res = res;
        }

        public int getRes() {
            return res;
        }

        public static Result parseInt(int res) {
            switch (res) {
            case 0:
                return ADDRESSED_SUCCESS;
            case 1:
                return ADDRESSED_NO_ACKNOWLEDGE;
            case 2:
                return COULD_NOT_BROADCAST;
            case 3:
                return BROADCAST_SENT;
            case 4:
                return LATE_RECEPTION;
            default:
                return null;
            }
        }
    }

    private int destination;
    private Character channel;
    private int msgId;
    private int sequence;
    private Result result;

    public Abk() {
        super();
        this.destination = 0;
        this.formatter = "ABK";
    }

    public static boolean isAbk(String line) {
        return (line.indexOf("$AIABK") >= 0);
    }

    /**
     * Parse method. Will always return 0 as sentence will always be in a single line.
     */
    @Override
    public int parse(String line) throws SentenceException, SixbitException {
        // Do common parsing
        super.baseParse(line);

        // Check ABK formatter
        if (!this.formatter.equals("ABK")) {
            throw new SentenceException("Not ABK sentence");
        }

        // Check that there at least 5 fields
        if (fields.length < 5) {
            throw new SentenceException("Sentence does not have at least 5 fields");
        }

        // Destination, may be null for broadcasts
        if (fields[1].length() > 0) {
            destination = Sentence.parseInt(fields[1]);
        }

        // Channel, relaxed may be null
        if (fields[2].length() > 0) {
            this.channel = fields[2].charAt(0);
        } else {
            this.channel = 0;
        }

        // Message id
        msgId = Sentence.parseInt(fields[3]);

        // Sequence
        sequence = Sentence.parseInt(fields[4]);

        // Result
        result = Result.parseInt(Sentence.parseInt(fields[5]));

        return 0;
    }

    /**
     * Get the encoded sentence
     */
    @Override
    public String getEncoded() {
        super.encode();
        encodedFields.set(0, "$AIABK");
        encodedFields.add(destination == 0 ? "" : Integer.toString(destination));
        encodedFields.add((channel == null) ? "" : Character.toString(channel));
        encodedFields.add(Integer.toString(msgId));
        encodedFields.add(Integer.toString(sequence));
        encodedFields.add(Integer.toString(result.getRes()));
        return finalEncode();
    }

    /**
     * Returns if result indicates success
     * 
     * @return success
     */
    public boolean isSuccess() {
        return (getResult() == Result.ADDRESSED_SUCCESS || getResult() == Result.BROADCAST_SENT);
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public Character getChannel() {
        return channel;
    }

    public void setChannel(Character channel) {
        this.channel = channel;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Abk [channel=");
        builder.append(channel);
        builder.append(", destination=");
        builder.append(destination);
        builder.append(", msgId=");
        builder.append(msgId);
        builder.append(", result=");
        builder.append(result);
        builder.append(", sequence=");
        builder.append(sequence);
        builder.append("]");
        return builder.toString();
    }

}
