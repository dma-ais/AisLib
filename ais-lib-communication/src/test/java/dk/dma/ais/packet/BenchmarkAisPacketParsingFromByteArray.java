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
package dk.dma.ais.packet;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.google.common.io.Files;

/**
 * 
 * @author Kasper Nielsen
 */
public class BenchmarkAisPacketParsingFromByteArray {

    public static void main(String[] args) throws Exception {
        URL url = ClassLoader.getSystemResource("replay_dump.txt");
        Assert.assertNotNull(url);
        System.out.println(url.toExternalForm());
        File f = new File(url.toExternalForm().replace("file:", ""));
        System.out.println(f);
        List<String> list = Files.readLines(f, StandardCharsets.US_ASCII);

        List<byte[]> data = new ArrayList<>();
        AisPacketParser apr = new AisPacketParser();
        for (String s : list) {
            AisPacket p = apr.readLine(s);
            if (p != null) {
                data.add(p.toByteArray());
            }
        }
        // Warm up

        for (int i = 0; i < 10000; i++) {
            for (byte[] b : data) {
                AisPacket p = AisPacket.fromByteArray(b);
                p.getAisMessage();
            }
        }
        Thread.sleep(100);
        long l = 0;
        long now = System.nanoTime();
        for (int i = 0; i < 20000; i++) {
            for (byte[] b : data) {
                AisPacket p = AisPacket.fromByteArray(b);
                l++;
                p.getAisMessage();
            }
        }
        double totalTime = System.nanoTime() - now;
        double total = totalTime / 1000d / 1000d / 1000d;
        System.out.println("Total processed:" + l + ", " + l / total + " packets/second");
    }
}
