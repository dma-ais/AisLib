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
package dk.dma.ais.packet;

import com.google.common.collect.Lists;
import de.micromata.opengis.kml.v_2_2_0.AltitudeMode;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import dk.dma.ais.tracker.ScenarioTracker;
import dk.dma.commons.util.io.OutputStreamSink;
import dk.dma.enav.util.function.Predicate;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.TimeZone;

/**
 * This class receives AisPacket and use them to build a scenario
 *
 * When the sink is closed it dumps the entire target state in KML format to an output stream.
 *
 */
class AisPacketKMLOutputSink extends OutputStreamSink<AisPacket> implements AutoCloseable, Closeable {

    private final OutputStream outputStream;

    private final ScenarioTracker scenarioTracker = new ScenarioTracker();

    private final Predicate<AisPacket> filter;

    public AisPacketKMLOutputSink(OutputStream outputStream) {
        this.outputStream = outputStream;
        filter = null;
    }

    public AisPacketKMLOutputSink(OutputStream outputStream, Predicate<AisPacket> filter) {
        this.outputStream = outputStream;
        this.filter = filter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void header(OutputStream stream) throws IOException {
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public void process(OutputStream stream, AisPacket packet, long count) throws IOException {
        if (filter == null || filter.test(packet)) {
            scenarioTracker.update(packet);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void footer(OutputStream stream, long count) throws IOException {
    }

    public static void main(String[] args) throws IOException {
        Path i = Paths.get("/Users/tbsalling/Documents/tbsalling.dk/Udvikling/SFS/demo/sprint-1/AisAbnormal/ais-ab-stat-builder/data/ais-sample.txt");
        Path o = Paths.get("/Users/tbsalling/Desktop/test.kml");
        FileOutputStream fis = new FileOutputStream(o.toFile());
        AisPacketReader reader = AisPacketReader.createFromFile(i, true);
        try (AisPacketKMLOutputSink kmlOutputSink = new AisPacketKMLOutputSink(fis, new Predicate<AisPacket>() {
            @Override
            public boolean test(AisPacket aisPacket) {
                return aisPacket.tryGetAisMessage().getUserId() == 477325700;
            }
        })) {
            reader.writeTo(fis, kmlOutputSink);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        dumpScenarioToKmlFile();
        outputStream.close();
    }

    /**
     *
     * @param parentPlacemark
     * @param lat
     * @param lon
     * @param heading
     * @param toBow
     * @param toStern
     * @param toPort
     * @param toStarbord
     * @return
     */
    private static Geometry getShipGeometry(Placemark parentPlacemark, double lat, double lon, int heading, int toBow, int toStern, int toPort, int toStarbord) {
        Point shipShape = parentPlacemark
                .createAndSetPoint()
                .withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND)
                .withCoordinates(Lists.newArrayList(new Coordinate(lon, lat)));

        return shipShape;
    }

    private void dumpScenarioToKmlFile() throws IOException {
        Kml kml = new Kml();

        Document document = kml.createAndSetDocument()
                .withDescription("Event starting " + scenarioTracker.scenarioBegin() + " and ending " + scenarioTracker.scenarioEnd())
                .withName("Abnormal event")
                .withOpen(true);

        // Create all ship styles
        Set<ScenarioTracker.Target> targets = scenarioTracker.getTargets();
        for (ScenarioTracker.Target target : targets) {
            document
            .createAndAddStyle()
                .withId(target.getMmsi())
                .createAndSetLineStyle()
                    .withWidth(2)
                    .withColor("ff00ff00");

        }

        //  Folder rootFolder = document.createAndAddFolder().withName(scenarioTracker.scenarioBegin().toString());

        // Generate situation folder
        Folder situationFolder = document.createAndAddFolder()
                .withName("Situation")
                .withOpen(false)
                .withVisibility(false);

        // Generate tracks folder
        Folder tracksFolder = document.createAndAddFolder()
                .withName("Tracks")
                .withOpen(false)
                .withVisibility(false);

        for (ScenarioTracker.Target target : targets) {
            Set<ScenarioTracker.Target.PositionReport> positionReportReports = target.getPositionReports();
            if (positionReportReports.size() > 0) {
                Placemark placemark = tracksFolder.createAndAddPlacemark().withId(target.getMmsi()).withName(target.getName());
                LineString lineString = placemark.createAndSetLineString();
                for (ScenarioTracker.Target.PositionReport positionReport : positionReportReports) {
                    lineString.addToCoordinates(positionReport.getLongitude(), positionReport.getLatitude());
                }
            }
        }

        // Generate movements folder
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Folder movementFolder = document.createAndAddFolder()
                .withName("Movements")
                .withOpen(true)
                .withVisibility(false);

        for (ScenarioTracker.Target target : targets) {
            int c = 0;
            Set<ScenarioTracker.Target.PositionReport> positionReportReports = target.getPositionReports();
            if (positionReportReports.size() > 0) {
                for (ScenarioTracker.Target.PositionReport positionReport : positionReportReports) {
                    Placemark placemark = movementFolder
                            .createAndAddPlacemark()
                            .withId(target.getMmsi() + "-" + c++).withName(target.getName());

                    placemark.setGeometry(
                            getShipGeometry(
                                    placemark,
                                    positionReport.getLatitude(), positionReport.getLongitude(), positionReport.getHeading(),
                                    target.getToBow(), target.getToStern(), target.getToPort(), target.getToStarboard()
                            )
                    );

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeZone(TimeZone.getTimeZone("Europe/Copenhagen"));
                    calendar.setTimeInMillis(positionReport.getTimestamp());
                    String begin = dateFormat.format(calendar.getTime());
                    calendar.setTimeInMillis(positionReport.getTimestamp() + 7000);
                    String end = dateFormat.format(calendar.getTime());

                    placemark.createAndSetTimeSpan()
                            .withBegin(begin)
                            .withEnd(end);
                }
            }
        }

        // Generate the KML
        kml.marshal(outputStream);
        outputStream.close();
    }
}
