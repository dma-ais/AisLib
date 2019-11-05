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

import dk.dma.ais.proprietary.ProprietaryFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.Deque;
import java.util.Optional;

/**
 * Exception class for exceptions related to sentences
 */
public class SentenceException extends Exception {

    private static final long serialVersionUID = 1L;

    private final Deque<String> sentenceTrace;

    /**
     * Instantiates a new Sentence exception.
     */
    public SentenceException() {
        sentenceTrace = null;
    }

    /**
     * Instantiates a new Sentence exception.
     *
     * @param msg the msg
     */
    public SentenceException(String msg) {
        super(msg);
        sentenceTrace = null;
    }

    /**
     * Instantiates a new Sentence exception.
     *
     * @param msg           the msg
     * @param sentenceTrace the sentence trace
     */
    public SentenceException(String msg, Deque<String> sentenceTrace) {
        super(msg + "\nSentence trace:\n---\n" + StringUtils.join(sentenceTrace, "\n") + "\n---\n");
        this.sentenceTrace = sentenceTrace;
    }

    /**
     * Instantiates a new Sentence exception.
     *
     * @param e             the e
     * @param sentenceTrace the sentence trace
     */
    public SentenceException(SentenceException e, Deque<String> sentenceTrace) {
        this(e.getMessage(), sentenceTrace);
    }

    /**
     * Gets possible proprietary tag.
     *
     * @return the possible proprietary tag
     */
    public String getPossibleProprietaryTag() {
        String possibleProprietaryTag = null;

        if (sentenceTrace != null) {
            Optional<String> stringOptional = sentenceTrace.stream().filter(line -> ProprietaryFactory.isProprietaryTag(line)).reduce((p, c) -> c);
            if (stringOptional != null && stringOptional.isPresent()) {
                possibleProprietaryTag = stringOptional.get();
            }
        }
        return possibleProprietaryTag;
    }

}
