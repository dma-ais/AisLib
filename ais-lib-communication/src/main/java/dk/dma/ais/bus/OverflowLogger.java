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
package dk.dma.ais.bus;

import org.slf4j.Logger;

/**
 * Class that down samples the logging of overflow
 */
public class OverflowLogger {

    private long lastLogging;
    private final long interval;
    private final Logger logger;
    
    public OverflowLogger(Logger logger) {
        this(logger, 10000);
    }

    public OverflowLogger(Logger logger, long interval) {
        this.logger = logger;
        this.interval = interval;
    }
    
    public void log(String message) {
        long now = System.currentTimeMillis();
        if (now - lastLogging > interval) {
            logger.error(message);
            lastLogging = now;
        }
    }

}
