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
