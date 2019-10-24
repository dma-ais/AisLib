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
package dk.dma.ais.reader;

import com.google.common.net.HostAndPort;
import org.apache.commons.lang3.StringUtils;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import static java.util.Objects.requireNonNull;

/**
 * Factory and utility methods for {@link AisReader}, {@link AisTcpReader}, {@link AisUdpReader},
 * and {@link AisReaderGroup} classes defined in this package.
 *
 * @author Kasper Nielsen
 */
public class AisReaders {

    /**
     * Equivalent to {@link #parseSource(String)} except that it will parse an array of sources. Returning a map making
     * sure there all source names are unique
     *
     * @param name    the name
     * @param sources the sources
     * @return the ais reader group
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
     * @see #getDefaultSources() #getDefaultSources()#getDefaultSources()
     */
    public static AisReaderGroup createGroupFromDefaultsSource() {
        return createGroup("defaults", Arrays.asList(getDefaultSources()));
    }

    /**
     * Creates a {@link AisTcpReader} from a list of one or more hosts. On the form: host1:port1,...,hostN:portN
     *
     * @param commaHostPort the comma host port
     * @return the ais tcp reader
     */
    public static AisTcpReader createReader(String commaHostPort) {
        AisTcpReader r = new AisTcpReader();
        String[] hostPorts = StringUtils.split(commaHostPort, ",");
        for (String hp : hostPorts) {
            r.addHostPort(HostAndPort.fromString(hp));
        }
        return r;
    }

    /**
     * Create reader ais tcp reader.
     *
     * @param hostname the hostname
     * @param port     the port
     * @return the ais tcp reader
     */
    public static AisTcpReader createReader(String hostname, int port) {
        AisTcpReader r = new AisTcpReader();
        r.addHostPort(HostAndPort.fromParts(hostname, port));
        return r;
    }

    /**
     * Creates a {@link AisUdpReader} listening on port for any interface
     *
     * @param port the port
     * @return ais udp reader
     */
    public static AisUdpReader createUdpReader(int port) {
        return createUdpReader(null, port);
    }

    /**
     * Creates a {@link AisUdpReader} listening on ip and port
     *
     * @param address the address
     * @param port    the port
     * @return ais udp reader
     */
    public static AisUdpReader createUdpReader(String address, int port) {
        return new AisUdpReader(address, port);
    }

    /**
     * Create reader from input stream ais reader.
     *
     * @param inputStream the input stream
     * @return the ais reader
     */
    public static AisReader createReaderFromInputStream(InputStream inputStream) {
        return new AisStreamReader(inputStream);
    }

    /**
     * Creates a {@link AisReader} from a file. If the file has '.zip' or '.gz' suffix the file will treated as a zip file or
     * gzip file respectively.
     *
     * @param filename the filename
     * @return ais reader
     * @throws IOException the io exception
     */
    public static AisReader createReaderFromFile(String filename) throws IOException {
        return new AisStreamReader(createFileInputStream(requireNonNull(filename)));
    }

    /**
     * Reader that recursively reads files matching pattern in directories with root dir
     *
     * @param dir       the dir
     * @param pattern   E.g. *.txt or *.gz
     * @param recursive Apply to all sub directories depth first
     * @return ais directory reader
     * @throws IOException the io exception
     */
    public static AisDirectoryReader createDirectoryReader(String dir, String pattern, boolean recursive) throws IOException {
        return new AisDirectoryReader(dir, pattern, recursive);
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

    /**
     * Manage group.
     *
     * @param group the group
     * @throws JMException the jm exception
     */
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
     * @param fullSource the full source
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

    /**
     * Create file input stream input stream.
     *
     * @param filename the filename
     * @return the input stream
     * @throws IOException the io exception
     */
    static InputStream createFileInputStream(String filename) throws IOException {
        InputStream in = new FileInputStream(filename);
        if (filename.endsWith(".gz")) {
            in = new GZIPInputStream(in);
        } else if (filename.endsWith(".zip")) {
            // TODO: currently only reads the first zip entry
            in = new ZipInputStream(in);
            ((ZipInputStream)in).getNextEntry();            
        }
        
        
        return in;
    }
    
}
