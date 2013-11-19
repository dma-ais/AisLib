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
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.filter.IPacketFilter;
import dk.dma.ais.message.AisBinaryMessage;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.binary.AisApplicationMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.ais.sentence.Vdm;
import dk.dma.enav.util.function.Consumer;

public class MessageHandler implements Consumer<AisPacket> {

    private final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    private volatile boolean stop;

    private final PrintStream out;
    private final FilterSettings filter;
    private boolean dumpParsed;
    private long start;
    private long end;
    private long msgCount;
    private long bytes;

    private final List<IPacketFilter> filters = new CopyOnWriteArrayList<>();

    public MessageHandler(FilterSettings filter, PrintStream out) {
        this.filter = filter;
        this.out = out;
    }

    @Override
    public void accept(AisPacket packet) {

        end = System.currentTimeMillis();
        if (start == 0) {
            start = end;
        }

        for (IPacketFilter filter : filters) {
            if (filter.rejectedByFilter(packet)) {
                return;
            }
        }

        Integer baseMMSI = -1;
        String country = "";
        String region = "";

        Vdm vdm = packet.getVdm();
        if (vdm == null) {
            return;
        }

        // Get source tag properties
        IProprietarySourceTag sourceTag = vdm.getSourceTag();
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
        Date timestamp = vdm.getTimestamp();
        if (filter.getStartDate() != null && timestamp != null) {
            if (timestamp.before(filter.getStartDate())) {
                return;
            }
        }

        // Maybe check for end date
        if (filter.getEndDate() != null && timestamp != null) {
            if (timestamp.after(filter.getEndDate())) {
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

        // Print tag line packet
        out.println(packet.getStringMessage());
        
        // Count bytes
        bytes += packet.getStringMessage().length();

        // Maybe print parsed
        if (dumpParsed) {
            if (timestamp != null) {
                out.println("+ timetamp " + timestampFormat.format(timestamp));
            }
            if (vdm.getTags() != null) {
                for (IProprietaryTag tag : vdm.getTags()) {
                    out.println("+ " + tag.toString());
                }
            }
            AisMessage aisMessage = packet.tryGetAisMessage();
            if (aisMessage != null) {
                out.println("+ " + aisMessage.toString());
            } else {
                out.println("+ AIS message could not be parsed");
            }

            // Check for binary message
            if (aisMessage instanceof AisBinaryMessage) {
                AisBinaryMessage binaryMessage = (AisBinaryMessage) aisMessage;
                try {
                    AisApplicationMessage appMessage = binaryMessage.getApplicationMessage();
                    out.println(appMessage);
                } catch (SixbitException e) {
                }
            }
            out.println("---------------------------");
        }

    }

    public List<IPacketFilter> getFilters() {
        return filters;
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
        System.out.println("Msg/min  : " + String.format(Locale.US, "%.2f", msgPerMin));
        System.out.println("KBytes   : " + kbytes);
        System.out.println("KB/s     : " + String.format(Locale.US, "%.2f", kbytes / elapsedSecs));
        System.out.println("Kbps     : " + String.format(Locale.US, "%.2f", kbytes * 8.0 / elapsedSecs));
    }

}
