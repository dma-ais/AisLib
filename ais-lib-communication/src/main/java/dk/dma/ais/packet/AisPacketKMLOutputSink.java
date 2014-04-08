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

import de.micromata.opengis.kml.v_2_2_0.AltitudeMode;
import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;
import dk.dma.ais.tracker.ScenarioTracker;
import dk.dma.ais.utils.coordinates.CoordinateConverter;
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

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

/**
 * This class receives AisPacket and use them to build a scenario
 *
 * When the sink is closed it dumps the entire target state to the output stream in KML format.
 *
 */
class AisPacketKMLOutputSink extends OutputStreamSink<AisPacket> implements AutoCloseable, Closeable {

    /**
     * The output stream which KML output will be written to.
     */
    private final OutputStream outputStream;

    /**
     * The tracker which will be used to build the scenario that will be written as KML.
     */
    private final ScenarioTracker scenarioTracker = new ScenarioTracker();

    /**
     * Only AisPackets passing this filter will be passed to the scenarioTracker.
     */
    private final Predicate<AisPacket> filter;

    /**
     * Style only
     */
    private final Predicate<AisPacket> style1Packets;
    private final Predicate<AisPacket> style2Packets;
    private final Predicate<AisPacket> style3Packets;

    private static final String STYLE1_TAG = "Ship1Style";
    private static final String STYLE2_TAG = "Ship2Style";
    private static final String STYLE3_TAG = "Ship3Style";

    public AisPacketKMLOutputSink(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.filter = AisPacketFilters.ACCEPT;
        this.style1Packets = AisPacketFilters.REJECT;
        this.style2Packets = AisPacketFilters.REJECT;
        this.style3Packets = AisPacketFilters.REJECT;
    }

    /**
     * Create a sink that writes KML contents to outputStream - but build the scenario only from
     * AisPackets which comply with the filter predicate.
     *
     * @param outputStream the stream to write the KML output to.
     * @param filter a filter predicate for pre-filtering of AisPackets.
     */
    public AisPacketKMLOutputSink(OutputStream outputStream, Predicate<AisPacket> filter) {
        this.outputStream = outputStream;
        this.filter = filter;
        this.style1Packets = AisPacketFilters.REJECT;
        this.style2Packets = AisPacketFilters.REJECT;
        this.style3Packets = AisPacketFilters.REJECT;
    }

    /**
     * Create a sink that writes KML contents to outputStream - but build the scenario only from
     * AisPackets which comply with the filter predicate.
     *
     * @param outputStream the stream to write the KML output to.
     * @param filter a filter predicate for pre-filtering of AisPackets.
     * @param style1Packets Apply primary KML styling to packets which pass this predicate.
     * @param style2Packets Apply secondary KML styling to packets which pass this predicate.
     * @param style3Packets Apply tertiary KML styling to packets which pass this predicate.
     */
    public AisPacketKMLOutputSink(OutputStream outputStream, Predicate<AisPacket> filter, Predicate<AisPacket> style1Packets, Predicate<AisPacket> style2Packets, Predicate<AisPacket> style3Packets) {
        this.outputStream = outputStream;
        this.filter = filter;
        this.style1Packets = style1Packets;
        this.style2Packets = style2Packets;
        this.style3Packets = style3Packets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(OutputStream stream, AisPacket packet, long count) throws IOException {
        if (filter.test(packet)) {
            scenarioTracker.update(packet);

            if (style1Packets.test(packet)) {
                scenarioTracker.tagTarget(packet.tryGetAisMessage().getUserId(), STYLE1_TAG);
            }
            if (style2Packets.test(packet)) {
                scenarioTracker.tagTarget(packet.tryGetAisMessage().getUserId(), STYLE2_TAG);
            }
            if (style3Packets.test(packet)) {
                scenarioTracker.tagTarget(packet.tryGetAisMessage().getUserId(), STYLE3_TAG);
            }
        }
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
        }, AisPacketFilters.ACCEPT, AisPacketFilters.REJECT, AisPacketFilters.REJECT
        )) {
            reader.writeTo(fis, kmlOutputSink);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        Kml kml = createKml();
        kml.marshal(outputStream);
        outputStream.close();
    }

    private Kml createKml() throws IOException {
        Kml kml = new Kml();

        Document document = kml.createAndSetDocument()
                .withDescription("Scenario starting " + scenarioTracker.scenarioBegin()
                        + " and ending " + scenarioTracker.scenarioEnd())
                .withName("Abnormal event")
                .withOpen(true);

        // Create all ship styles
        createKmlStyles(document);

        Folder rootFolder = document.createAndAddFolder().withName(scenarioTracker.scenarioBegin().toString());

        // Generate situation folder
        createKmlSituationFolder(rootFolder);

        // Generate tracks folder
        createKmlTracksFolder(rootFolder);

        // Generate movements folder
        createKmlMovementsFolder(rootFolder);

        return kml;
    }

    private void createKmlStyles(Document document) {
        Style ship1Style = document
                .createAndAddStyle()
                .withId(STYLE1_TAG);
        ship1Style.createAndSetLineStyle()
            .withWidth(2)
            .withColor("ff00ff00");
        ship1Style.createAndSetPolyStyle()
            .withColor("ff00ff00");

        Style ship2Style = document
                .createAndAddStyle()
                .withId(STYLE2_TAG);
        ship2Style.createAndSetLineStyle()
            .withWidth(2)
            .withColor("ff0000ff");
        ship2Style.createAndSetPolyStyle()
            .withColor("ff0000ff");

        Style ship3Style = document
                .createAndAddStyle()
                .withId(STYLE3_TAG);
        ship3Style.createAndSetLineStyle()
            .withWidth(2)
            .withColor("ff7fffff");
        ship3Style.createAndSetPolyStyle()
            .withColor("ff7fffff");
    }

    private void createKmlSituationFolder(Folder kmlNode) {
        kmlNode.createAndAddFolder()
            .withName("Situation")
            .withOpen(false)
            .withVisibility(false);
    }

    private void createKmlMovementsFolder(Folder kmlNode) {
        Set<ScenarioTracker.Target> targets = scenarioTracker.getTargets();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Folder movementFolder = kmlNode.createAndAddFolder()
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
                            .withVisibility(true)
                            .withId(target.getMmsi() + "-" + c++).withName(target.getName());

                    Boundary boundary = placemark.createAndSetPolygon().createAndSetOuterBoundaryIs();
                    boundary.setLinearRing(
                            createKmlShipGeometry(
                                    boundary,
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

                    addStyles(placemark, target);
                }
            }
        }
    }

    private void createKmlTracksFolder(Folder kmlNode) {
        Set<ScenarioTracker.Target> targets = scenarioTracker.getTargets();

        Folder tracksFolder = kmlNode.createAndAddFolder()
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
                addStyles(placemark, target);
            }
        }
    }

    private void addStyles(Placemark placemark, ScenarioTracker.Target target) {
        if (target.isTagged(STYLE1_TAG)) {
            placemark.withStyleUrl("#" + STYLE1_TAG);
        }
        if (target.isTagged(STYLE2_TAG)) {
            placemark.withStyleUrl("#" + STYLE2_TAG);
        }
        if (target.isTagged(STYLE3_TAG)) {
            placemark.withStyleUrl("#" + STYLE3_TAG);
        }
    }

    /**
     * Create a KML geometry to symbolize a ship at the given position, at the given heading and with the
     * given dimensions.
     *
     * @param parentPlacemark
     * @param lat Ship's positional latitude in degrees.
     * @param lon Ship's positional longitude in degrees.
     * @param heading Ship's heading in degrees; 0 being north, 90 being east.
     * @param toBow Distance in meters from ship's position reference to ship's bow.
     * @param toStern Distance in meters from ship's position reference to ship's stern.
     * @param toPort Distance in meters from ship's position reference to port side at maximum beam.
     * @param toStarbord Distance in meters from ship's position reference to starboard side at maximum beam.
     * @return
     */
    private static LinearRing createKmlShipGeometry(Boundary parentPlacemark, double lat, double lon, int heading, int toBow /* A */, int toStern /* B */, int toPort /* C */, int toStarbord /* D */) {
        // If the ship dimensions are not found then create a small ship
        if (toBow < 0 || toStern < 0) {
            toBow = 20;
            toStern = 4;
        }
        if (toPort < 0 || toStarbord < 0) {
            toPort = (int) ((toBow + toStern) / 6.5);
            toStarbord = toPort;
        }

        int szA = toBow;
        int szB = toStern;
        int szC = toPort;
        int szD = toStarbord;

        // The ship consists of 5 points which are stored in shipPnts()
        // To begin with the points are in meters
        Point[] points = new Point[5];
        
        points[0] = new Point(); points[0].x = -szB;                         points[0].y = szC;                 // stern port
        points[1] = new Point(); points[1].x = points[0].x + 0.85*(szA+szB); points[1].y = points[0].y;
        points[2] = new Point(); points[2].x = szA;                          points[2].y = szC - (szC+szD)/2.0; // bow
        points[3] = new Point(); points[3].x = points[1].x;                  points[3].y = -szD;
        points[4] = new Point(); points[4].x = -szB;                         points[4].y = -szD;                // stern starboard

        // Rotate ship. Each ship has its own coordinate system with
        // origin in the ais-position of the ship
        double theta = toRadians(CoordinateConverter.compass2cartesian(heading));

        for (Point point : points) {
            double x = point.x * cos(theta) + point.y * sin(theta);
            double y = point.x * sin(theta) + point.y * cos(theta);
            point.x = x;
            point.y = y;
        }

        // Convert ship coordinates into geographic coordinates and a KML geometry
        LinearRing shipGeometry = parentPlacemark
            .createAndSetLinearRing()
            .withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);

        CoordinateConverter coordinateConverter = new CoordinateConverter(lon, lat);
        for (Point point : points) {
            shipGeometry.addToCoordinates(coordinateConverter.x2Lon(point.x, point.y), coordinateConverter.y2Lat(point.x, point.y));
        }

        return shipGeometry;
    }

    private static final class Point { double x, y; }

}
