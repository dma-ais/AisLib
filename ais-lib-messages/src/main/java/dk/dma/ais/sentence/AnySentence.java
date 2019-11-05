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

import java.util.ArrayList;
import java.util.List;

import dk.dma.ais.binary.SixbitException;

/**
 * Class to represent any sentence
 */
public class AnySentence extends Sentence {

    /**
     * The Any fields.
     */
    List<String> anyFields = new ArrayList<>();

    @Override
    public int parse(SentenceLine sl) throws SentenceException, SixbitException {
        // Do common parsing
        super.baseParse(sl);
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

    /**
     * Add field.
     *
     * @param field the field
     */
    public void addField(String field) {
        anyFields.add(field);
    }

}
