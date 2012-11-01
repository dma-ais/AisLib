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
package dk.dma.ais.sentence;

/**
 * Abstract base class for parametric sentences like Abk
 */
public abstract class ParametricSentence extends Sentence {

    public ParametricSentence() {
        super();
    }

    /**
     * Base parse method to be used by extending classes
     */
    @Override
    protected void baseParse(String line) throws SentenceException {
        super.baseParse(line);

        // Should at least have three fields
        if (fields.length < 3) {
            throw new SentenceException("Sentence have less than three fields");
        }

    }

}
