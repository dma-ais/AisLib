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
package dk.dma.enav.ais.proprietary;

/**
 * An implementation of the IProprietaryFactory for DMA proprietary sentences
 */
public class DmaFactory implements IProprietaryFactory {

    @Override
    public boolean match(String line) {
        return (line.indexOf("$PDMA,") >= 0);
    }

    @Override
    public IProprietaryTag getTag(String line) {

        DmaSourceTag dmaSourceTag = new DmaSourceTag();

        try {
            dmaSourceTag.parse(line);
        } catch (Exception e) {
            return null;
        }

        return dmaSourceTag;
    }

}
