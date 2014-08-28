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
package dk.dma.ais.packet;

import static java.util.Objects.requireNonNull;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisPosition;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.commons.util.io.OutputStreamSink;

/**
 * 
 * @author Kasper Nielsen
 */
class AisPacketOutputSinkTable extends OutputStreamSink<AisPacket> {

    /** Just a placeholder for NULL in the methods map. */
    private static final Method NULL = AisPacketOutputSinkTable.class.getDeclaredMethods()[0];

    /** The column we are writing. */
    private final String[] columns;

    /** A cache of methods. */
    private final ConcurrentHashMap<Map.Entry<Class<?>, String>, Method> methods = new ConcurrentHashMap<>();

    /** A date formatter. */
    private final DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

    /** A date time formatter for utc. */
    private final DateTimeFormatter utc = new DateTimeFormatterBuilder().appendDayOfMonth(2).appendLiteral('-')
            .appendMonthOfYear(2).appendLiteral('-').appendYear(2, 2).appendLiteral(' ').appendHourOfDay(2)
            .appendLiteral(':').appendMinuteOfHour(2).appendLiteral(':').appendSecondOfMinute(2).toFormatter();

    final byte[] separator;

    final boolean writeHeader;

    public AisPacketOutputSinkTable(String format, boolean writeHeader) {
        this(format, writeHeader, ";");
    }

    public AisPacketOutputSinkTable(String format, boolean writeHeader, String separator) {
        columns = format.split(";");
        this.writeHeader = writeHeader;
        this.separator = requireNonNull(separator).getBytes(StandardCharsets.US_ASCII);
    }

    /** {@inheritDoc} */
    @Override
    public void process(OutputStream stream, AisPacket packet, long count) throws IOException {
        AisMessage m = packet.tryGetAisMessage();
        if (m != null) {
            for (int i = 0; i < columns.length; i++) {
                String c = columns[i];
                // Special columns
                if (c.equals("n")) {
                    stream.write(Long.toString(count).getBytes(StandardCharsets.US_ASCII));
                } else if (c.equals("timestamp")) {
                    stream.write(Long.toString(packet.getBestTimestamp()).getBytes(StandardCharsets.US_ASCII));
                } else if (c.equals("utc")) {
                    DateTime dateTime = new DateTime(new Date(packet.getBestTimestamp()));
                    String str = utc.print(dateTime);
                    stream.write(str.getBytes(StandardCharsets.US_ASCII));
                } else if (c.equals("time")) {
                    DateTime dateTime = new DateTime(new Date(packet.getBestTimestamp()));
                    String str = fmt.print(dateTime);
                    stream.write(str.getBytes(StandardCharsets.US_ASCII));
                } else if (c.equals("mmsi")) {
                    String str = Integer.toString(m.getUserId());
                    stream.write(str.getBytes(StandardCharsets.US_ASCII));
                } else if ((c.equals("lat") || c.equals("lon")) && m instanceof IPositionMessage) {
                    AisPosition pos = ((IPositionMessage) m).getPos();
                    if (pos != null) {
                        if (c.equals("lat")) {
                            stream.write(Double.toString(pos.getLatitudeDouble()).getBytes(StandardCharsets.US_ASCII));
                        } else {
                            stream.write(Double.toString(pos.getLongitudeDouble()).getBytes(StandardCharsets.US_ASCII));
                        }
                    }
                } else {
                    // Okay use reflection

                    Method g = findGetter(c, m.getClass());
                    if (g != null) {
                        try {
                            Object o = g.invoke(m);
                            String s = o.toString();
                            stream.write(s.getBytes(StandardCharsets.US_ASCII));
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            throw new IOException(e);
                        }
                    }
                }
                // Write separator for all but the last column
                if (i < columns.length - 1) {
                    stream.write(separator);
                }
            }
            stream.write('\n');
        }
    }

    /** {@inheritDoc} */
    @Override
    public void header(OutputStream stream) throws IOException {
        // Writes the name of each column as the header
        if (writeHeader) {
            for (int i = 0; i < columns.length; i++) {
                stream.write(columns[i].getBytes(StandardCharsets.US_ASCII));
                if (i < columns.length - 1) {
                    stream.write(separator);
                }
            }
            stream.write('\n');
        }
    }

    private Method findGetter(String nameOfColumn, Class<?> type) throws IOException {
        Entry<Class<?>, String> key = new SimpleImmutableEntry<Class<?>, String>(type, nameOfColumn);
        Method m = methods.get(key);
        if (m == null) {
            m = NULL;
            BeanInfo info = null;
            try {
                info = Introspector.getBeanInfo(type);
            } catch (IntrospectionException e) {
                throw new IOException(e);
            }
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                if (nameOfColumn.equals(pd.getName())) {
                    m = pd.getReadMethod();
                }
            }
            methods.put(key, m);
        }
        return m == NULL ? null : m;
    }
}
