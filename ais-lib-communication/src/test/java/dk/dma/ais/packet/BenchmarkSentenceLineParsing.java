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

import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.SentenceLine;

/**
 * 
 * @author Kasper Nielsen
 */
public class BenchmarkSentenceLineParsing {

    public static void main(String[] args) throws Exception {
        URL url = ClassLoader.getSystemResource("replay_dump.txt");
        Assert.assertNotNull(url);
        System.out.println(url.toExternalForm());
        File f = new File(url.toExternalForm().replace("file:", ""));
        System.out.println(f);
        List<String> list = Files.readLines(f, StandardCharsets.US_ASCII);

        // Warm up
        for (int i = 0; i < 10000; i++) {
            for (String s : list) {
                SentenceLine sl = new SentenceLine(s);
                if (!sl.isChecksumMatch()) {
                    throw new SentenceException("Checksum failed");
                }
            }
        }
        Thread.sleep(100);

        long l = 0;
        long now = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            for (String s : list) {
                SentenceLine sl = new SentenceLine(s);
                l++;
                if (!sl.isChecksumMatch()) {
                    throw new SentenceException("Checksum failed");
                }
            }
        }
        double total = (System.nanoTime() - now) / 1000d / 1000 / 1000;
        System.out.println("Total processed:" + l + ", " + l / total + " lines/second");
    }
}
