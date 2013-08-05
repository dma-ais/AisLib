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
package dk.dma.ais.reader;

import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.commons.lang.StringUtils;

import com.google.common.net.HostAndPort;

/**
 * Factory and utility methods for {@link AisReader}, {@link AisTcpReader}, and {@link AisReaderGroup} classes defined
 * in this package.
 * 
 * @author Kasper Nielsen
 */
public class AisReaders {

    /**
     * Equivalent to {@link #parseSource(String)} except that it will parse an array of sources. Returning a map making
     * sure there all source names are unique
     */
    public static AisReaderGroup createGroup(String name, List<String> sources) {
        Map<String, AisTcpReader> readers = new HashMap<>();
        for (String s : sources) {
            AisTcpReader r = parseSource(s);
            if (readers.put(r.getSourceId(), r) != null) {
                // Make sure its unique
                throw new Error("More than one reader specified with the same source id (id =" + r.getSourceId()
                        + "), source string = " + sources);
            }
        }
        AisReaderGroup g = new AisReaderGroup(name);
        for (AisTcpReader r : readers.values()) {
            g.add(r);
        }
        return g;
    }

    /**
     * Creates a default group from reader available in the system property ais.default.sources
     * 
     * @return a new reader group
     * @see #getDefaultSources()
     */
    public static AisReaderGroup createGroupFromDefaultsSource() {
        return createGroup("defaults", Arrays.asList(getDefaultSources()));
    }

    /**
     * Creates a {@link AisTcpReader} from a list of one or more hosts. On the form: host1:port1,...,hostN:portN
     * 
     * @param commaHostPort
     */
    public static AisTcpReader createReader(String commaHostPort) {
        AisTcpReader r = new AisTcpReader();
        String[] hostPorts = StringUtils.split(commaHostPort, ",");
        for (String hp : hostPorts) {
            r.addHostPort(HostAndPort.fromString(hp));
        }
        return r;
    }

    public static AisTcpReader createReader(String hostname, int port) {
        AisTcpReader r = new AisTcpReader();
        r.addHostPort(HostAndPort.fromParts(hostname, port));
        return r;
    }

    public static AisReader createReaderFromInputStream(InputStream inputStream) {
        return new AisStreamReader(inputStream);
    }

    /**
     * Returns the default sources configured for the system. The default sources can be set using enviroment variable
     * AIS_DEFAULT_SOURCES.
     * 
     * @return the default sources configured for the system
     */
    public static String[] getDefaultSources() {
        String p = System.getProperty("AIS_DEFAULT_SOURCES");
        if (p == null) {
            p = System.getenv("AIS_DEFAULT_SOURCES");
            if (p == null) {
                throw new IllegalStateException("The property AIS_DEFAULT_SOURCES has not been set");
            }
        }
        return p.split(";");
    }

    public static void manageGroup(AisReaderGroup group) throws JMException {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName mxbeanName = new ObjectName("dk.dma.ais.readers.group." + group.name + ":name=Group");
        mbs.registerMBean(new AisReaderGroupMXBeanImpl(group), mxbeanName);
        for (AisTcpReader r : group.readers.values()) {
            mxbeanName = new ObjectName("dk.dma.ais.readers.group." + group.name + ":source=Source-" + r.getSourceId());
            mbs.registerMBean(new AisReaderMXBeanImpl(r), mxbeanName);

        }
    }

    /**
     * Parses the string and returns either an {@link AisTcpReader} if only one hostname is found or a
     * {@link RoundRobinAisTcpReader} if more than 1 host name is found. Example
     * "mySource=ais163.sealan.dk:65262,ais167.sealan.dk:65261" will return a RoundRobinAisTcpReader alternating between
     * the two sources if one is down.
     * 
     * @param fullSource
     *            the full source
     * @return a ais reader
     */
    static AisTcpReader parseSource(String fullSource) {
        try (Scanner s = new Scanner(fullSource);) {
            s.useDelimiter("\\s*=\\s*");
            if (!s.hasNext()) {
                throw new IllegalArgumentException("Source must be of the format src=host:port,host:port, was "
                        + fullSource);
            }
            String src = s.next();
            if (!s.hasNext()) {
                throw new IllegalArgumentException(
                        "A list of hostname:ports must follow the source (format src=host:port,host:port), was "
                                + fullSource);
            }
            AisTcpReader r = new AisTcpReader();
            try (Scanner s1 = new Scanner(s.next())) {
                s1.useDelimiter("\\s*,\\s*");
                if (!s1.hasNext()) {
                    throw new IllegalArgumentException(
                            "Source must have at least one host:port (format src=host:port,host:port), was "
                                    + fullSource);
                }
                while (s1.hasNext()) {
                    r.addHostPort(HostAndPort.fromString(s1.next()));
                }
            }
            r.setSourceId(src);
            return r;
        }
    }
}
