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
package dk.dma.ais.utils.aisbus;

import java.io.FileNotFoundException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
import com.google.inject.Injector;

import dk.dma.ais.bus.AisBus;
import dk.dma.ais.bus.AisBusComponent;
import dk.dma.ais.bus.AisBusConsumer;
import dk.dma.ais.bus.AisBusFactory;
import dk.dma.ais.bus.AisBusProvider;
import dk.dma.commons.app.AbstractDaemon;

/**
 * AisBus launcher application
 */
public class AisBusLauncher extends AbstractDaemon {

    private static final Logger LOG = LoggerFactory.getLogger(AisBusLauncher.class);

    @Parameter(names = "-file", description = "AisBus configuration file")
    String confFile = "aisbus.xml";

    private AisBus aisBus;

    @Override
    protected void runDaemon(Injector injector) throws Exception {
        LOG.info("Starting AisBusLauncher with configuration: " + confFile);
        try {
            aisBus = AisBusFactory.get(confFile);
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage());
            return;
        }
        LOG.info("Starting AisBus");
        aisBus.start();
        LOG.info("Starting consumers");
        aisBus.startConsumers();
        LOG.info("Starting providers");
        aisBus.startProviders();
        
        Thread.sleep(2000);

        while (true) {            
            LOG.info(StringUtils.leftPad("", 100, '-'));
            ratePrint(aisBus, null);
            for (AisBusProvider provider : aisBus.getProviders()) {
                ratePrint(provider, provider.getName());
            }
            for (AisBusConsumer consumer : aisBus.getConsumers()) {
                ratePrint(consumer, consumer.getName());
            }
            LOG.info(StringUtils.leftPad("", 100, '-'));
            Thread.sleep(60000);
        }

    }

    private void ratePrint(AisBusComponent comp, String name) {
        if (name == null) {
            name = comp.getClass().getSimpleName();
        }
        LOG.info(String.format("%-20s %s", name, comp.rateReport()));
    }

    @Override
    protected void shutdown() {
        LOG.info("Shutdown");
        // TODO close AisBus
        super.shutdown();
    }

    public static void main(String[] args) throws Exception {
        new AisBusLauncher().execute(args);
    }

}
