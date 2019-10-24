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
package dk.dma.ais.bus;

import org.slf4j.Logger;

/**
 * Class that down samples the logging of overflow
 */
public class OverflowLogger {

    private long lastLogging;
    private final long interval;
    private final Logger logger;

    /**
     * Instantiates a new Overflow logger.
     *
     * @param logger the logger
     */
    public OverflowLogger(Logger logger) {
        this(logger, 10000);
    }

    /**
     * Instantiates a new Overflow logger.
     *
     * @param logger   the logger
     * @param interval the interval
     */
    public OverflowLogger(Logger logger, long interval) {
        this.logger = logger;
        this.interval = interval;
    }

    /**
     * Log.
     *
     * @param message the message
     */
    public void log(String message) {
        long now = System.currentTimeMillis();
        if (now - lastLogging > interval) {
            logger.error(message);
            lastLogging = now;
        }
    }

}
