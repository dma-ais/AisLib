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

import com.google.common.collect.Sets;
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
import dk.dma.enav.model.geometry.BoundingBox;
import dk.dma.enav.util.function.Predicate;
import net.jcip.annotations.NotThreadSafe;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
@NotThreadSafe
class AisPacketKMLOutputSink extends OutputStreamSink<AisPacket> {

    /** The tracker which will be used to build the scenario that will be written as KML. */
    private final ScenarioTracker scenarioTracker = new ScenarioTracker();

    /** Only AisPackets passing this filter will be passed to the scenarioTracker. */
    private final Predicate<? super AisPacket> filter;

    private final Predicate<? super AisPacket> isPrimaryTarget;
    private final Predicate<? super AisPacket> isSecondaryTarget;
    private final Predicate<? super AisPacket> isTertiaryTarget;
    private final Predicate<? super AisPacket> triggerSnapshot;

    private static final String STYLE1_TAG = "Ship1Style";
    private static final String STYLE2_TAG = "Ship2Style";
    private static final String STYLE3_TAG = "Ship3Style";

    private final Set<Long> snapshotTimes = Sets.newTreeSet();

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public AisPacketKMLOutputSink() {
        this.filter = Predicate.TRUE;
        this.isPrimaryTarget = Predicate.FALSE;
        this.isSecondaryTarget = Predicate.FALSE;
        this.isTertiaryTarget = Predicate.FALSE;
        this.triggerSnapshot = Predicate.FALSE;
    }

    /**
     * Create a sink that writes KML contents to outputStream - but build the scenario only from
     * AisPackets which comply with the filter predicate.
     *
     * @param filter a filter predicate for pre-filtering of AisPackets.
     */
    public AisPacketKMLOutputSink(Predicate<? super AisPacket> filter) {
        this.filter = filter;
        this.isPrimaryTarget = Predicate.FALSE;
        this.isSecondaryTarget = Predicate.FALSE;
        this.isTertiaryTarget = Predicate.FALSE;
        this.triggerSnapshot = Predicate.FALSE;
    }

    /**
     * Create a sink that writes KML contents to outputStream - but build the scenario only from
     * AisPackets which comply with the filter predicate.
     *
     * @param filter a filter predicate for pre-filtering of AisPackets before they are passed to the tracker.
     * @param isPrimaryTarget Apply primary KML styling to targets which are updated by packets that pass this predicate.
     * @param isSecondaryTarget Apply secondary KML styling to targets which are updated by packets that pass this predicate.
     * @param isTertiaryTarget Apply tertiary KML styling to targets which are updated by packets that pass this predicate.
     */
    public AisPacketKMLOutputSink(Predicate<? super AisPacket> filter, Predicate<? super AisPacket> isPrimaryTarget, Predicate<? super AisPacket> isSecondaryTarget, Predicate<? super AisPacket> isTertiaryTarget, Predicate<? super AisPacket> triggerSnapshot) {
        this.filter = filter;
        this.isPrimaryTarget = isPrimaryTarget;
        this.isSecondaryTarget = isSecondaryTarget;
        this.isTertiaryTarget = isTertiaryTarget;
        this.triggerSnapshot = triggerSnapshot;
    }

    /** {@inheritDoc} */
    @Override
    public void process(OutputStream stream, AisPacket packet, long count) throws IOException {
        if (filter.test(packet)) {
            scenarioTracker.update(packet);

            if (isPrimaryTarget.test(packet)) {
                scenarioTracker.tagTarget(packet.tryGetAisMessage().getUserId(), STYLE1_TAG);
            }
            if (isSecondaryTarget.test(packet)) {
                scenarioTracker.tagTarget(packet.tryGetAisMessage().getUserId(), STYLE2_TAG);
            }
            if (isTertiaryTarget.test(packet)) {
                scenarioTracker.tagTarget(packet.tryGetAisMessage().getUserId(), STYLE3_TAG);
            }
            if (triggerSnapshot.test(packet)) {
                this.snapshotTimes.add(packet.getBestTimestamp());
            }
        }
    }

    public void footer(OutputStream outputStream, long count) throws IOException {
        Kml kml = createKml();
        kml.marshal(outputStream);
    }

    public static void main(String[] args) throws IOException {
        Predicate<AisPacket> filter = new Predicate<AisPacket>() {
            @Override
            public boolean test(AisPacket aisPacket) {
                return aisPacket.tryGetAisMessage().getUserId() == 477325700;
            }
        };

        AisPacketKMLOutputSink kmlOutputSink = new AisPacketKMLOutputSink(filter, Predicate.TRUE, Predicate.FALSE, Predicate.FALSE, Predicate.FALSE);

        try (FileOutputStream fos = new FileOutputStream(Paths.get("/Users/tbsalling/Desktop/test.kml").toFile())) {
            AisPacketReader reader = AisPacketReader.createFromFile(Paths.get("/Users/tbsalling/Desktop/ais-sample.txt"), true);
            reader.writeTo(fos, kmlOutputSink);
        }
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

        // Generate bounding box
        createKmlBoundingBox(rootFolder);

        // Generate situation folder
        if (snapshotTimes.size() >= 1) {
            createKmlSituationFolder(rootFolder, snapshotTimes.iterator().next());

            if (snapshotTimes.size() > 1) {
                System.err.println("Only generates KML snapshot folder for first timestamp marked.");
            }
        }

        // Generate tracks folder
        createKmlTracksFolder(rootFolder, new Predicate<ScenarioTracker.Target>() {
            @Override
            public boolean test(ScenarioTracker.Target target) {
                return target.isTagged(STYLE1_TAG) || target.isTagged(STYLE2_TAG);
            }
        });

        // Generate movements folder
        createKmlMovementsFolder(rootFolder);

        return kml;
    }

    private void createKmlStyles(Document document) {
        // For colors - http://www.zonums.com/gmaps/kml_color/
        document
            .createAndAddStyle()
            .withId("bbox")
            .createAndSetLineStyle()
                .withColor("cccc00b0")
                .withWidth(2.5);

        Style shipDefaultStyle = document
            .createAndAddStyle()
            .withId("ShipDefaultStyle");
        shipDefaultStyle.createAndSetLineStyle()
                .withWidth(2)
                .withColor("2014F0FA");
        shipDefaultStyle.createAndSetPolyStyle()
                .withColor("FF14F0FA");

        Style ship1Style = document
                .createAndAddStyle()
                .withId(STYLE1_TAG);
        ship1Style.createAndSetLineStyle()
            .withWidth(2)
            .withColor("8000ff00");
        ship1Style.createAndSetPolyStyle()
            .withColor("ff00ff00");

        Style ship2Style = document
                .createAndAddStyle()
                .withId(STYLE2_TAG);
        ship2Style.createAndSetLineStyle()
            .withWidth(2)
            .withColor("800000ff");
        ship2Style.createAndSetPolyStyle()
            .withColor("ff0000ff");

        Style ship3Style = document
                .createAndAddStyle()
                .withId(STYLE3_TAG);
        ship3Style.createAndSetLineStyle()
            .withWidth(2)
            .withColor("807fffff");
        ship3Style.createAndSetPolyStyle()
            .withColor("ff7fffff");
    }

    private void createKmlBoundingBox(Folder kmlNode) {
        BoundingBox bbox = scenarioTracker.boundingBox();

        kmlNode.createAndAddPlacemark()
            .withId("bbox")
            .withStyleUrl("#bbox")
            .withName("Bounding box")
            .withVisibility(true)
            .createAndSetLinearRing()
                .addToCoordinates(bbox.getMaxLon(), bbox.getMaxLat())
                .addToCoordinates(bbox.getMaxLon(), bbox.getMinLat())
                .addToCoordinates(bbox.getMinLon(), bbox.getMinLat())
                .addToCoordinates(bbox.getMinLon(), bbox.getMaxLat())
                .addToCoordinates(bbox.getMaxLon(), bbox.getMaxLat());
    }

    private void createKmlSituationFolder(Folder kmlNode, long atTime) {
        Folder situationFolder = kmlNode.createAndAddFolder()
                .withName("Situation")
                .withDescription(new Date(atTime).toString())
                .withOpen(false)
                .withVisibility(false);

        Set<ScenarioTracker.Target> targets = scenarioTracker.getTargetsHavingPositionUpdates();

        for (ScenarioTracker.Target target : targets) {
            ScenarioTracker.Target.PositionReport positionReport = target.getPositionReportAt(new Date(atTime));
            createKmlShipPlacemark(situationFolder, target.getMmsi(), target.getName(), positionReport.getTimestamp(), positionReport.getTimestamp() + 7000, positionReport.getLatitude(), positionReport.getLongitude(), positionReport.getHeading(), target.getToBow(), target.getToStern(), target.getToPort(), target.getToStarboard(), getStyle(target));
        }
    }

    private void createKmlMovementsFolder(Folder kmlNode) {
        Set<ScenarioTracker.Target> targets = scenarioTracker.getTargetsHavingPositionUpdates();

        Folder movementFolder = kmlNode.createAndAddFolder()
                .withName("Movements")
                .withOpen(true)
                .withVisibility(false);

        for (ScenarioTracker.Target target : targets) {
            int c = 0;
            Set<ScenarioTracker.Target.PositionReport> positionReportReports = target.getPositionReports();
            if (positionReportReports.size() > 0) {
                Folder targetFolder = movementFolder.createAndAddFolder().withName(target.getName()).withDescription("Movements for MMSI " + target.getMmsi());

                for (ScenarioTracker.Target.PositionReport positionReport : positionReportReports) {
                    createKmlShipPlacemark(targetFolder, target.getMmsi(), target.getName(), positionReport.getTimestamp(), positionReport.getTimestamp() + 7000, positionReport.getLatitude(), positionReport.getLongitude(), positionReport.getHeading(),
                            target.getToBow(), target.getToStern(), target.getToPort(), target.getToStarboard(), getStyle(target));
                }
            }
        }
    }

    private void createKmlTracksFolder(Folder kmlNode, Predicate<ScenarioTracker.Target> trackFor) {
        Set<ScenarioTracker.Target> targets = scenarioTracker.getTargetsHavingPositionUpdates();

        Folder tracksFolder = kmlNode.createAndAddFolder()
                .withName("Tracks")
                .withOpen(false)
                .withVisibility(false);

        for (ScenarioTracker.Target target : targets) {
            if (trackFor.test(target)) {
                Set<ScenarioTracker.Target.PositionReport> positionReportReports = target.getPositionReports();
                if (positionReportReports.size() > 0) {
                    Placemark placemark = tracksFolder.createAndAddPlacemark().withId(target.getMmsi()).withName(target.getName());
                    LineString lineString = placemark.createAndSetLineString();
                    for (ScenarioTracker.Target.PositionReport positionReport : positionReportReports) {
                        lineString.addToCoordinates(positionReport.getLongitude(), positionReport.getLatitude());
                    }
                    placemark.withStyleUrl(getStyle(target));
                }
            }
        }
    }

    private static String getStyle(ScenarioTracker.Target target) {
        if (target.isTagged(STYLE1_TAG)) {
            return STYLE1_TAG;
        } else if (target.isTagged(STYLE2_TAG)) {
            return STYLE2_TAG;
        } else if (target.isTagged(STYLE3_TAG)) {
            return STYLE3_TAG;
        } else {
            return "ShipDefaultStyle";
        }
    }

    private static void createKmlShipPlacemark(Folder targetFolder, String mmsi, String name, long timespanBegin, long timespanEnd, float latitude, float longitude, int heading, int toBow, int toStern, int toPort, int toStarboard, String style) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Copenhagen"));
        calendar.setTimeInMillis(timespanBegin);
        String begin = DATE_FORMAT.format(calendar.getTime());
        calendar.setTimeInMillis(timespanEnd);
        String end = DATE_FORMAT.format(calendar.getTime());

        Placemark placemark = targetFolder
                .createAndAddPlacemark()
                .withVisibility(true)
                .withId(mmsi)
                .withName(name)
                .withStyleUrl("#" + style);

        placemark.createAndSetTimeSpan()
                .withBegin(begin)
                .withEnd(end);

        createKmlShipGeometry(placemark, latitude, longitude, heading, toBow, toStern, toPort, toStarboard);
    }

    /**
     * Create a KML geometry to symbolize a ship at the given position, at the given heading and with the
     * given dimensions.
     *
     * @param placemark Parent node
     * @param lat Ship's positional latitude in degrees.
     * @param lon Ship's positional longitude in degrees.
     * @param heading Ship's heading in degrees; 0 being north, 90 being east.
     * @param toBow Distance in meters from ship's position reference to ship's bow.
     * @param toStern Distance in meters from ship's position reference to ship's stern.
     * @param toPort Distance in meters from ship's position reference to port side at maximum beam.
     * @param toStarbord Distance in meters from ship's position reference to starboard side at maximum beam.
     * @return
     */
    private static Boundary createKmlShipGeometry(Placemark placemark, double lat, double lon, int heading, int toBow /* A */, int toStern /* B */, int toPort /* C */, int toStarbord /* D */) {
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
        LinearRing shipGeometry = placemark
            .createAndSetLinearRing()
            .withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);

        CoordinateConverter coordinateConverter = new CoordinateConverter(lon, lat);
        for (Point point : points) {
            shipGeometry.addToCoordinates(coordinateConverter.x2Lon(point.x, point.y), coordinateConverter.y2Lat(point.x, point.y));
        }

        Boundary boundary = placemark.createAndSetPolygon().createAndSetOuterBoundaryIs();
        boundary.setLinearRing(shipGeometry);

        return boundary;
    }

    private static final class Point { double x, y; }

}
