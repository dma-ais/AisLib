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
package dk.dma.ais.sentence;


/**
 * Addressed Binary Acknowledgement as defined by IEC 61162
 */
public class Abk extends ParametricSentence {

    /**
     * Type of acknowledgement
     */
    public enum Result {
        /**
         * Addressed success result.
         */
        ADDRESSED_SUCCESS(0),
        /**
         * Addressed no acknowledge result.
         */
        ADDRESSED_NO_ACKNOWLEDGE(1),
        /**
         * Could not broadcast result.
         */
        COULD_NOT_BROADCAST(2),
        /**
         * Broadcast sent result.
         */
        BROADCAST_SENT(3),
        /**
         * Late reception result.
         */
        LATE_RECEPTION(4);
        private int res;

        private Result(int res) {
            this.res = res;
        }

        /**
         * Gets res.
         *
         * @return the res
         */
        public int getRes() {
            return res;
        }

        /**
         * Parse int result.
         *
         * @param res the res
         * @return the result
         */
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

    /**
     * Instantiates a new Abk.
     */
    public Abk() {
        super();
        this.destination = 0;
        this.formatter = "ABK";
    }

    /**
     * Is abk boolean.
     *
     * @param line the line
     * @return the boolean
     */
    public static boolean isAbk(String line) {
        return line.indexOf("$AIABK") >= 0;
    }

    /**
     * Parse method. Will always return 0 as sentence will always be in a single line.
     */
    @Override
    public int parse(SentenceLine sl) throws SentenceException {
        // Do common parsing
        super.baseParse(sl);

        // Check ABK formatter
        if (!this.formatter.equals("ABK")) {
            throw new SentenceException("Not ABK sentence");
        }

        // Check that there at least 5 fields
        if (sl.getFields().size() < 5) {
            throw new SentenceException("Sentence does not have at least 5 fields");
        }

        // Destination, may be null for broadcasts
        if (sl.getFields().get(1).length() > 0) {
            destination = Sentence.parseInt(sl.getFields().get(1));
        }

        // Channel, relaxed may be null
        if (sl.getFields().get(2).length() > 0) {
            this.channel = sl.getFields().get(2).charAt(0);
        } else {
            this.channel = 0;
        }

        // Message id
        msgId = Sentence.parseInt(sl.getFields().get(3));

        // Sequence
        sequence = Sentence.parseInt(sl.getFields().get(4));

        // Result
        result = Result.parseInt(Sentence.parseInt(sl.getFields().get(5)));

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
        encodedFields.add(channel == null ? "" : Character.toString(channel));
        encodedFields.add(Integer.toString(msgId));
        encodedFields.add(Integer.toString(sequence));
        encodedFields.add(Integer.toString(result.getRes()));
        return finalEncode();
    }

    /**
     * Returns if result indicates success
     *
     * @return success boolean
     */
    public boolean isSuccess() {
        return getResult() == Result.ADDRESSED_SUCCESS || getResult() == Result.BROADCAST_SENT;
    }

    /**
     * Gets destination.
     *
     * @return the destination
     */
    public int getDestination() {
        return destination;
    }

    /**
     * Sets destination.
     *
     * @param destination the destination
     */
    public void setDestination(int destination) {
        this.destination = destination;
    }

    /**
     * Gets channel.
     *
     * @return the channel
     */
    public Character getChannel() {
        return channel;
    }

    /**
     * Sets channel.
     *
     * @param channel the channel
     */
    public void setChannel(Character channel) {
        this.channel = channel;
    }

    /**
     * Gets msg id.
     *
     * @return the msg id
     */
    public int getMsgId() {
        return msgId;
    }

    /**
     * Sets msg id.
     *
     * @param msgId the msg id
     */
    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    /**
     * Gets sequence.
     *
     * @return the sequence
     */
    public int getSequence() {
        return sequence;
    }

    /**
     * Sets sequence.
     *
     * @param sequence the sequence
     */
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    /**
     * Gets result.
     *
     * @return the result
     */
    public Result getResult() {
        return result;
    }

    /**
     * Sets result.
     *
     * @param result the result
     */
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
