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
package dk.dma.ais.sentence;

import java.util.Deque;

import org.apache.commons.lang.StringUtils;

/**
 * Exception class for exceptions related to sentences
 */
public class SentenceException extends Exception {

    private static final long serialVersionUID = 1L;

    public SentenceException() {
    }

    public SentenceException(String msg) {
        super(msg);
    } 

    public SentenceException(String msg, Deque<String> sentenceTrace) {
        this(msg + "\nSentence trace:\n---\n" + StringUtils.join(sentenceTrace, "\n") + "\n---\n");
    }

    public SentenceException(SentenceException e, Deque<String> sentenceTrace) {
        this(e.getMessage(), sentenceTrace);
    }
}
