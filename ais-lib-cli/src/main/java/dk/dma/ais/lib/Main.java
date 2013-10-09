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

import dk.dma.commons.app.CliCommandList;
import dk.dma.commons.app.CliCommandList.Command;

/**
 * The command line interface to AisStore.
 * 
 * @author Kasper Nielsen
 */
public class Main {

    public static void main(String[] args) throws Exception {
        CliCommandList c = new CliCommandList("AisLib");
        c.add(FileDump.class, "filedump", "Reads data from AIS datasources and stores data into text files");
        c.add("bus", "AisBus....", new Command() {
            public void execute(String[] args) throws Exception {
                FileDump.main(args);
            }
        });
        c.invoke(args);
    }

}
