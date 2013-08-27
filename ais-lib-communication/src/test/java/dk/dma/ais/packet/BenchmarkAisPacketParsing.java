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
package dk.dma.ais.packet;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.Assert;

import com.google.common.io.Files;

/**
 * 
 * @author Kasper Nielsen
 */
public class BenchmarkAisPacketParsing {

    public static void main(String[] args) throws Exception {
        URL url = ClassLoader.getSystemResource("replay_dump.txt");
        Assert.assertNotNull(url);
        System.out.println(url.toExternalForm());
        File f = new File(url.toExternalForm().replace("file:", ""));
        System.out.println(f);
        List<String> list = Files.readLines(f, StandardCharsets.US_ASCII);

        // Warm up
        AisPacketParser apr = new AisPacketParser();
        for (int i = 0; i < 10000; i++) {
            for (String s : list) {
                AisPacket p = apr.readLine(s);
                if (p != null) {
                    p.getAisMessage();
                }
            }
        }
        Thread.sleep(100);
        //System.in.read();
        System.out.println("ok starting");
        long packets = 0;
        long lines = 0;
        long now = System.nanoTime();
        for (int i = 0; i < 20000; i++) {
            for (String s : list) {
                AisPacket p = apr.readLine(s);
                if (p != null) {
                    packets++;
                    p.getAisMessage();
                }
                lines++;
            }
        }
        double total = (System.nanoTime() - now) / 1000d / 1000 / 1000;
        System.out.println("Total processed:" + packets + " (lines: " + lines + "), " + packets / total
                + " packets/second");
    }
}
