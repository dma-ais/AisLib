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
