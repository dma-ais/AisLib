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
package dk.dma.ais.lib;

import dk.dma.ais.utils.aisbus.AisBusLauncher;
import dk.dma.ais.utils.filter.AisFilter;
import dk.dma.commons.app.CliCommandList;

/**
 * The command line interface to AIS utilities
 * 
 * @author Kasper Nielsen
 */
public class Main {

    public static void main(String[] args) throws Exception {
        CliCommandList c = new CliCommandList("AisLib");
        c.add(FileDump.class, "filedump", "Reads data from AIS datasources and stores data into text files");
        c.add(AisFilter.class, "filter", "Command line tool to do various AIS reading, filtering and writing");
        c.add(AisBusLauncher.class, "aisbus", "AisBus launcher application");
        c.add(FileConvert.class, "fileconvert", "converts a list of aisfiles into a different format");
        c.add(AisGapsToCSV.class, "aisgapstocsv", "finds timegaps in a directory of aisdata and outputs a csv file with utc timestamps per every second of missing data");
        c.invoke(args);
    }

}
