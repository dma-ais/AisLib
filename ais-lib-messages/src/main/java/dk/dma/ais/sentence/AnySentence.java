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

import java.util.ArrayList;
import java.util.List;

import dk.dma.ais.binary.SixbitException;

/**
 * Class to represent any sentence
 */
public class AnySentence extends Sentence {

    List<String> anyFields = new ArrayList<>();

    @Override
    public int parse(String line) throws SentenceException, SixbitException {
        // Do common parsing
        super.baseParse(line);
        return 0;
    }

    @Override
    public String getEncoded() {
        super.encode();
        // Collections.copy(encodedFields, anyFields);
        for (String field : anyFields) {
            encodedFields.add(field);
        }
        return super.finalEncode();
    }

    public void addField(String field) {
        anyFields.add(field);
    }

}
