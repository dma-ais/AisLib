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
package dk.dma.ais.message;

import junit.framework.Assert;

import org.junit.Test;

import dk.dma.ais.proprietary.ProprietaryFactory;

public class ProprietaryTest {

    @Test
    public void pTest() {
        Assert.assertNotNull(ProprietaryFactory.parseTag("$PGHP,1,2010,6,11,11,46,11,874,276,0,,1,55*2C"));
    }
}
