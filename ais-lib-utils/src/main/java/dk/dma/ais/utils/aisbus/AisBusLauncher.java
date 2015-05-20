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
package dk.dma.ais.utils.aisbus;

import com.beust.jcommander.Parameter;
import com.google.inject.Injector;
import dk.dma.ais.bus.AisBus;
import dk.dma.ais.bus.AisBusComponent;
import dk.dma.ais.bus.AisBusConsumer;
import dk.dma.ais.bus.AisBusFactory;
import dk.dma.ais.bus.AisBusProvider;
import dk.dma.ais.bus.consumer.TcpServerConsumer;
import dk.dma.ais.bus.provider.TcpServerProvider;
import dk.dma.ais.bus.tcp.TcpClient;
import dk.dma.commons.app.AbstractDaemon;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * AisBus launcher application
 */
public class AisBusLauncher extends AbstractDaemon {

    static final Logger LOG = LoggerFactory.getLogger(AisBusLauncher.class);

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
            LOG.debug(StringUtils.leftPad("", 100, '-'));
            ratePrint(aisBus, null);
            for (AisBusProvider provider : aisBus.getProviders()) {
                ratePrint(provider, provider.getName());
                if (provider instanceof TcpServerProvider) {
                    for (TcpClient tcpClient : ((TcpServerProvider) provider).getServer().getClients()) {
                        ratePrint(tcpClient);
                    }
                }
            }
            for (AisBusConsumer consumer : aisBus.getConsumers()) {
                ratePrint(consumer, consumer.getName());
                if (consumer instanceof TcpServerConsumer) {
                    for (TcpClient tcpClient : ((TcpServerConsumer) consumer).getServer().getClients()) {
                        ratePrint(tcpClient);
                    }
                }
            }
            LOG.debug(StringUtils.leftPad("", 100, '-'));
            Thread.sleep(60000);
        }

    }

    private void ratePrint(TcpClient tcpClient) {
        LOG.debug(String.format("     %-15s %s", tcpClient.getRemoteHost(), tcpClient.rateReport()));
    }

    private void ratePrint(AisBusComponent comp, String name) {
        if (name == null) {
            name = comp.getClass().getSimpleName();
        }
        ratePrint(comp.rateReport(), name);
    }

    private void ratePrint(String report, String name) {
        LOG.debug(String.format("%-20s %s", name, report));
    }

    @Override
    protected void preShutdown() {
        if (aisBus != null) {
            LOG.info("Shutting down AisBus");
            aisBus.cancel();
            LOG.info("Shutting down done!");
        }
        super.preShutdown();
    }

    public static void main(String[] args) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                LOG.error("Uncaught exception in thread " + t.getClass().getCanonicalName() + ": " + e.getMessage(), e);
                System.exit(-1);
            }
        });
        new AisBusLauncher().execute(args);
    }

}
