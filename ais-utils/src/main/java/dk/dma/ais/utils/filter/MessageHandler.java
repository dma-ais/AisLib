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
package dk.dma.ais.utils.filter;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.handler.IAisHandler;
import dk.dma.ais.message.AisBinaryMessage;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.binary.AisApplicationMessage;
import dk.dma.ais.proprietary.DmaSourceTag;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.ais.proprietary.IProprietaryTag;

public class MessageHandler implements IAisHandler {

    private volatile boolean stop = false;

    private PrintStream out;
    private FilterSettings filter;
    private boolean dumpParsed = false;
    private long start = 0;
    private long end = 0;
    private long msgCount = 0;
    private long bytes = 0;
    private boolean replayTag = false;

    public MessageHandler(FilterSettings filter, PrintStream out) {
        this.filter = filter;
        this.out = out;
    }

    @Override
    public void receive(AisMessage aisMessage) {

        end = System.currentTimeMillis();
        if (start == 0) {
            start = end;
        }

        Long baseMMSI = -1L;
        String country = "";
        String region = "";

        // Get source tag properties
        IProprietarySourceTag sourceTag = aisMessage.getSourceTag();
        if (sourceTag != null) {
            baseMMSI = sourceTag.getBaseMmsi();
            if (sourceTag.getCountry() != null) {
                country = sourceTag.getCountry().getTwoLetter();
            }
            if (sourceTag.getRegion() != null) {
                region = sourceTag.getRegion();
            }
        }
        if (region.equals("")) {
            region = "0";
        }

        // Maybe check for start date
        if (filter.getStartDate() != null && sourceTag.getTimestamp() != null) {
            if (sourceTag.getTimestamp().before(filter.getStartDate())) {
                return;
            }
        }

        // Maybe check for end date
        if (filter.getEndDate() != null && sourceTag.getTimestamp() != null) {
            if (sourceTag.getTimestamp().after(filter.getEndDate())) {
                System.exit(0);
            }
        }

        // Maybe check for base station MMSI
        if (filter.getBaseStations().size() > 0) {
            if (!filter.getBaseStations().contains(baseMMSI)) {
                return;
            }
        }

        // Maybe check for country
        if (filter.getCountries().size() > 0) {
            if (!filter.getCountries().contains(country)) {
                return;
            }
        }

        // Maybe check for region
        if (filter.getRegions().size() > 0) {
            if (!filter.getRegions().contains(region)) {
                return;
            }
        }

        if (stop) {
            return;
        }

        // Count message
        msgCount++;

        // Print replay line
        if (replayTag) {
            DmaSourceTag dmaSourceTag = new DmaSourceTag();
            dmaSourceTag.setSourceName("AISFILTER");
            dmaSourceTag.setTimestamp(new Date());
            aisMessage.setTag(dmaSourceTag);
        }

        // Print tag line
        if (aisMessage.getTags() != null) {
            for (IProprietaryTag tag : aisMessage.getTags()) {
                bytes += tag.getSentence().length() + 2;
                out.println(tag.getSentence());
                // Maybe print parsed
                if (dumpParsed) {
                    out.println("+-- " + tag.toString());
                }
            }
        }
        // Print original line
        String orgLines = aisMessage.getVdm().getOrgLinesJoined();
        bytes += orgLines.length() + 2;
        out.println(orgLines);

        // Maybe print parsed
        if (dumpParsed) {
            out.println("+-- " + aisMessage.toString());
            // Check for binary message
            if (aisMessage.getMsgId() == 6 || aisMessage.getMsgId() == 8) {
                AisBinaryMessage binaryMessage = (AisBinaryMessage) aisMessage;
                try {
                    AisApplicationMessage appMessage = binaryMessage.getApplicationMessage();
                    out.println("+-- " + appMessage);
                } catch (SixbitException e) {}

            }
        }

    }

    public boolean isReplayTag() {
        return replayTag;
    }

    public void setReplayTag(boolean replayTag) {
        this.replayTag = replayTag;
    }

    public void setDumpParsed(boolean dumpParsed) {
        this.dumpParsed = dumpParsed;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void printStats() {
        long elapsed = end - start;
        double elapsedSecs = elapsed / 1000.0;
        double msgPerMin = msgCount / (elapsedSecs / 60.0);
        long kbytes = bytes / 1000;

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        System.out.println("\n");
        System.out.println("Elapsed  : " + df.format(new Date(elapsed)));
        System.out.println("Messages : " + msgCount);
        System.out.println("Msg/min  : " + String.format("%.2f", msgPerMin));
        System.out.println("KBytes   : " + kbytes);
        System.out.println("KB/s     : " + String.format("%.2f", kbytes / elapsedSecs));
        System.out.println("Kbps     : " + String.format("%.2f", kbytes * 8.0 / elapsedSecs));
    }

}
