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
package dk.dma.ais.queue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class QueueTest {

    @Test
    public void testPushPull() throws MessageQueueOverflowException, InterruptedException {
        IMessageQueue<Integer> q = new BlockingMessageQueue<>();
        for (int i = 0; i < 1000; i++) {
            q.push(i);
        }
        List<Integer> list = new ArrayList<>();
        list = q.pull(list, 100);
        Assert.assertEquals(list.size(), 100);

        list.clear();
        list = q.pull(list, 1);
        Assert.assertEquals(list.size(), 1);

    }

}
