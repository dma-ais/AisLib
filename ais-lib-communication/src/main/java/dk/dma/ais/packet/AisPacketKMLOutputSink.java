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
import com.google.common.collect.Sets;
import de.micromata.opengis.kml.v_2_2_0.AltitudeMode;
import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;
import dk.dma.ais.tracker.ScenarioTracker;
import dk.dma.commons.util.io.OutputStreamSink;
import dk.dma.enav.model.geometry.BoundingBox;
import dk.dma.enav.model.geometry.Ellipse;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.util.CoordinateConverter;
import dk.dma.enav.util.function.Predicate;
import dk.dma.enav.util.function.Supplier;
import dk.dma.enav.util.geometry.Point;
import net.jcip.annotations.NotThreadSafe;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import static dk.dma.enav.safety.SafetyZones.safetyZone;
import static java.lang.Math.min;

/**
 * This class receives AisPacket and use them to build a scenario
 *
 * When the sink is closed it dumps the entire target state to the output stream in KML format.
 *
 * TODO Even though the triggerSnapshot predicate encourages generation of multiple snapshots, only one is currently supported.
 */
@NotThreadSafe
class AisPacketKMLOutputSink extends OutputStreamSink<AisPacket> {

    /** The tracker which will be used to build the scenario that will be written as KML. */
    private final ScenarioTracker scenarioTracker = new ScenarioTracker();

    /** Only AisPackets passing this filter will be passed to the scenarioTracker. */
    private final Predicate<? super AisPacket> filter;

    private final Predicate<? super AisPacket> isPrimaryTarget;
    private final Predicate<? super AisPacket> isSecondaryTarget;

    /** This predicate returns true when a packet should trigger a snapshot */
    private final Predicate<? super AisPacket> triggerSnapshot;

    /** This function supplies an HTML description of a snapshot triggered by predicate 'triggerSnapshot' */
    private final Supplier<? extends String> snapshotDescriptionSupplier;

    /** KML folder title */
    private final Supplier<? extends String> title;

    /** KML folder description */
    private final Supplier<? extends String> description;

    /** KML movement folder interpolation step */
    private final Supplier<? extends Integer> movementInterpolationStep;

    private static final String STYLE1_TAG = "Ship1Style";
    private static final String STYLE2_TAG = "Ship2Style";
    private static final String STYLE3_TAG = "Ship3Style";

    private static final String ESTIMATED_EXTENSION = "Estimated";

    private final Set<Long> snapshotTimes = Sets.newTreeSet();

    private final Calendar calendar = Calendar.getInstance();

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private static final SimpleDateFormat DATE_FORMAT_DTG = new SimpleDateFormat("ddHHmm'Z' MMM yy");
    static {
        DATE_FORMAT_DTG.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /** Timespan for KML positions in situation and movement folders */
    private static final int KML_POSITION_TIMESPAN_SECS = 1;

    public AisPacketKMLOutputSink() {
        this.filter = Predicate.TRUE;
        this.isPrimaryTarget = Predicate.FALSE;
        this.isSecondaryTarget = Predicate.FALSE;
        this.triggerSnapshot = Predicate.FALSE;
        this.snapshotDescriptionSupplier = null;
        this.title = defaultTitleSupplier;
        this.description = defaultDescriptionSupplier;
        this.movementInterpolationStep = defaultMovementInterpolationStepSupplier;
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
        this.triggerSnapshot = Predicate.FALSE;
        this.snapshotDescriptionSupplier = null;
        this.title = defaultTitleSupplier;
        this.description = defaultDescriptionSupplier;
        this.movementInterpolationStep = defaultMovementInterpolationStepSupplier;
    }

    /**
     * Create a sink that writes KML contents to outputStream - but build the scenario only from
     * AisPackets which comply with the filter predicate.
     *
     * @param filter a filter predicate for pre-filtering of AisPackets before they are passed to the tracker.
     * @param isPrimaryTarget Apply primary KML styling to targets which are updated by packets that pass this predicate.
     * @param isSecondaryTarget Apply secondary KML styling to targets which are updated by packets that pass this predicate.
     */
    public AisPacketKMLOutputSink(Predicate<? super AisPacket> filter, Predicate<? super AisPacket> isPrimaryTarget, Predicate<? super AisPacket> isSecondaryTarget, Predicate<? super AisPacket> triggerSnapshot, Supplier<? extends String> snapshotDescriptionSupplier, Supplier<? extends Integer> movementInterpolationStepSupplier) {
        this.filter = filter;
        this.isPrimaryTarget = isPrimaryTarget;
        this.isSecondaryTarget = isSecondaryTarget;
        this.triggerSnapshot = triggerSnapshot;
        this.snapshotDescriptionSupplier = snapshotDescriptionSupplier;
        this.title = defaultTitleSupplier;
        this.description = defaultDescriptionSupplier;
        this.movementInterpolationStep = movementInterpolationStepSupplier == null ? defaultMovementInterpolationStepSupplier : movementInterpolationStepSupplier;

    }

    /**
     * Create a sink that writes KML contents to outputStream - but build the scenario only from
     * AisPackets which comply with the filter predicate.
     *
     * @param filter a filter predicate for pre-filtering of AisPackets before they are passed to the tracker.
     * @param isPrimaryTarget Apply primary KML styling to targets which are updated by packets that pass this predicate.
     * @param isSecondaryTarget Apply secondary KML styling to targets which are updated by packets that pass this predicate.
     * @param supplyTitle Supplier of KML folder title
     * @param supplyDescription Supplier of KML folder description
     */
    public AisPacketKMLOutputSink(Predicate<? super AisPacket> filter, Predicate<? super AisPacket> isPrimaryTarget, Predicate<? super AisPacket> isSecondaryTarget, Predicate<? super AisPacket> triggerSnapshot, Supplier<? extends String> snapshotDescriptionSupplier, Supplier<? extends Integer> movementInterpolationStepSupplier, Supplier<? extends String> supplyTitle, Supplier<? extends String> supplyDescription) {
        this.filter = filter;
        this.isPrimaryTarget = isPrimaryTarget;
        this.isSecondaryTarget = isSecondaryTarget;
        this.triggerSnapshot = triggerSnapshot;
        this.snapshotDescriptionSupplier = snapshotDescriptionSupplier;
        this.title = supplyTitle == null ? defaultTitleSupplier : supplyTitle;
        this.description = supplyDescription == null ? defaultDescriptionSupplier : supplyDescription;
        this.movementInterpolationStep = movementInterpolationStepSupplier == null ? defaultMovementInterpolationStepSupplier : movementInterpolationStepSupplier;
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

        AisPacketKMLOutputSink kmlOutputSink = new AisPacketKMLOutputSink(filter);

        try (FileOutputStream fos = new FileOutputStream(Paths.get("/Users/tbsalling/Desktop/test.kml").toFile())) {
            AisPacketReader reader = AisPacketReader.createFromFile(Paths.get("/Users/tbsalling/Desktop/ais-sample.txt"), true);
            reader.writeTo(fos, kmlOutputSink);
        }
    }

    private Supplier<String> defaultTitleSupplier = new Supplier<String>() {
        @Override
        public String get() {
            return "Abnormal event";
        }
    };

    private Supplier<String> defaultDescriptionSupplier = new Supplier<String>() {
        @Override
        public String get() {
            return "Scenario starting " + scenarioTracker.scenarioBegin() + " and ending " + scenarioTracker.scenarioEnd();
        }
    };

    private Supplier<Integer> defaultMovementInterpolationStepSupplier = new Supplier<Integer>() {
        @Override
        public Integer get() {
            return null;
        }
    };

    private Kml createKml() throws IOException {
        Kml kml = new Kml();

        Document document = kml.createAndSetDocument()
            .withDescription(description.get())
            .withName(title.get())
            .withOpen(true);

        document.createAndSetCamera()
                .withAltitude(2000)
                .withHeading(0)
                .withTilt(0)
                .withLatitude((scenarioTracker.boundingBox().getMaxLat() + scenarioTracker.boundingBox().getMinLat())/2.0)
                .withLongitude((scenarioTracker.boundingBox().getMaxLon() + scenarioTracker.boundingBox().getMinLon())/2.0)
                .withAltitudeMode(AltitudeMode.ABSOLUTE);

        // Create all ship styles
        createKmlStyles(document);

        Folder rootFolder = document.createAndAddFolder().withName(scenarioTracker.scenarioBegin().toString() + " - " + scenarioTracker.scenarioEnd().toString());

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
        createKmlMovementsAndIconsFolders(rootFolder);

        return kml;
    }

    private static void createKmlStyles(Document document) {
        // For colors - http://www.zonums.com/gmaps/kml_color/
        document
            .createAndAddStyle()
            .withId("bbox")
            .createAndSetLineStyle()
                .withColor("cccc00b0")
                .withWidth(2.5);

        createStyle(document, "ShipDefaultStyle", 2, "2014F0FA", "FF14F0FA");

        createStyle(document, STYLE1_TAG, 2, "8000ff00", "ff00ff00");
        createStyle(document, STYLE2_TAG, 2, "800000ff", "ff0000ff");
        createStyle(document, STYLE3_TAG, 2, "807fffff", "ff7fffff");

        createStyle(document, STYLE1_TAG + ESTIMATED_EXTENSION, 2, "4000ff00", "8000ff00");
        createStyle(document, STYLE2_TAG + ESTIMATED_EXTENSION, 2, "400000ff", "800000ff");
        createStyle(document, STYLE3_TAG + ESTIMATED_EXTENSION, 2, "407fffff", "807fffff");
    }

    private static void createStyle(Document document, String styleName, int width, String lineColor, String polyColor) {
        Style style = document
            .createAndAddStyle()
            .withId(styleName);
        style.createAndSetLineStyle()
            .withWidth(width)
            .withColor(lineColor);
        style.createAndSetPolyStyle()
            .withColor(polyColor);
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

        BoundingBox bbox = scenarioTracker.boundingBox();
        Set<ScenarioTracker.Target> targets = scenarioTracker.getTargetsHavingPositionUpdates();

        final Date t = new Date(atTime);
        for (ScenarioTracker.Target target : targets) {
            ScenarioTracker.Target.PositionReport estimatedPosition = target.getPositionReportAt(t, 10);
            if (estimatedPosition != null && bbox.contains(estimatedPosition.getPositionTime())) {
                createKmlShipShapePlacemark(
                        situationFolder,
                        target.getMmsi(),
                        target.getName(),
                        null, // estimatedPosition.getTimestamp(),
                        null, // estimatedPosition.getTimestamp() + KML_POSITION_TIMESPAN_SECS*1000 - 1,
                        estimatedPosition.getLatitude(),
                        estimatedPosition.getLongitude(),
                        estimatedPosition.getCog(),
                        estimatedPosition.getSog(),
                        estimatedPosition.getHeading(),
                        target.getToBow(),
                        target.getToStern(),
                        target.getToPort(),
                        target.getToStarboard(),
                        target.isTagged(STYLE1_TAG),
                        getStyle(target, false)
                );
                createKmlShipIconPlacemark(
                        situationFolder,
                        null, // estimatedPosition.getTimestamp(),
                        null, // estimatedPosition.getTimestamp() + KML_POSITION_TIMESPAN_SECS*1000 - 1,
                        estimatedPosition.getLatitude(),
                        estimatedPosition.getLongitude(),
                        estimatedPosition.getCog(),
                        getShipDescription(target, estimatedPosition)
                );
                if (target.isTagged(STYLE1_TAG)) {
                    createSituationPlacemark(
                            situationFolder,
                            estimatedPosition.getLatitude(),
                            estimatedPosition.getLongitude()
                    );
                }
            }
        }
    }

    private void createKmlMovementsAndIconsFolders(Folder kmlNode) {
        Set<ScenarioTracker.Target> targets = scenarioTracker.getTargetsHavingPositionUpdates();

        final boolean useInterpolation = movementInterpolationStep.get() != null && movementInterpolationStep.get() > 0;

        Folder movementFolder = kmlNode
            .createAndAddFolder()
                .withName("Movements")
                .withOpen(false)
                .withVisibility(false);

        Folder iconFolder = kmlNode
            .createAndAddFolder()
                .withName("Icons")
                .withOpen(false)
                .withVisibility(false);

        for (ScenarioTracker.Target target : targets) {
            Folder targetShipShapeFolder = movementFolder.createAndAddFolder().withName(target.getName()).withDescription("Movements for MMSI " + target.getMmsi());
            Folder targetShipIconFolder = iconFolder.createAndAddFolder().withName(target.getName()).withDescription("Icons for MMSI " + target.getMmsi());
            if (useInterpolation) {
                final long t1 = target.timeOfFirstPositionReport().getTime();
                final long t2 = target.timeOfLastPositionReport().getTime();
                final int dt = movementInterpolationStep.get()*1000;
                for (long t = t1; t <= t2; t += dt) {
                    ScenarioTracker.Target.PositionReport positionReport = target.getPositionReportAt(new Date(t), KML_POSITION_TIMESPAN_SECS);
                    createKmlShipShapePlacemark(
                        targetShipShapeFolder,
                        target.getMmsi(),
                        target.getName(),
                        t - (dt - 100),
                        t,
                        positionReport.getLatitude(),
                        positionReport.getLongitude(),
                        positionReport.getCog(),
                        positionReport.getSog(),
                        positionReport.getHeading(),
                        target.getToBow(),
                        target.getToStern(),
                        target.getToPort(),
                        target.getToStarboard(),
                        false,
                        getStyle(target, positionReport.isEstimated())
                    );
                    createKmlShipIconPlacemark(
                        targetShipIconFolder,
                        t - (dt - 100),
                        t,
                        positionReport.getLatitude(),
                        positionReport.getLongitude(),
                        positionReport.getCog(),
                        getShipDescription(target, positionReport)
                    );
                }
            } else {
                Set<ScenarioTracker.Target.PositionReport> positionReports = target.getPositionReports();

                ScenarioTracker.Target.PositionReport[] positionReportsArray = positionReports.toArray(new ScenarioTracker.Target.PositionReport[positionReports.size()]);
                for (int i = 0; i < positionReportsArray.length; i++) {
                    ScenarioTracker.Target.PositionReport positionReport = positionReportsArray[i];
                    ScenarioTracker.Target.PositionReport nextPositionReport = null;
                    if (i < positionReportsArray.length - 1) {
                        nextPositionReport = positionReportsArray[i+1];
                    }

                    final long maxTimespan = 10000L;
                    long timespan = min(nextPositionReport!=null ? nextPositionReport.getTimestamp() - positionReport.getTimestamp() - 1 : maxTimespan, maxTimespan);

                    createKmlShipShapePlacemark(
                            targetShipShapeFolder,
                            target.getMmsi(),
                            target.getName(),
                            positionReport.getTimestamp(),
                            positionReport.getTimestamp() + timespan,
                            positionReport.getLatitude(),
                            positionReport.getLongitude(),
                            positionReport.getCog(),
                            positionReport.getSog(),
                            positionReport.getHeading(),
                            target.getToBow(),
                            target.getToStern(),
                            target.getToPort(),
                            target.getToStarboard(),
                            false,
                            getStyle(target, positionReport.isEstimated())
                    );
                    createKmlShipIconPlacemark(
                            targetShipIconFolder,
                            positionReport.getTimestamp(),
                            positionReport.getTimestamp() + timespan,
                            positionReport.getLatitude(),
                            positionReport.getLongitude(),
                            positionReport.getCog(),
                            getShipDescription(target, positionReport)
                    );
                }
            }
        }
    }

    private String getShipDescription(ScenarioTracker.Target target, ScenarioTracker.Target.PositionReport positionReport) {
        StringBuffer desc = new StringBuffer(512);

        desc.append("<h2>");
        desc.append(target.getName());
        desc.append("</h2>");

        calendar.setTimeInMillis(positionReport.getTimestamp());

        desc.append("<table width=\"300\">");
        addKmlTableRow(desc, "MMSI", target.getMmsi(), "");
        addKmlTableRow(desc, "IMO", target.getImo(), "");
        addKmlTableRow(desc, "TYPE", target.getShipType(), "");
        addKmlTableRow(desc, "LOA", target.getToBow() + target.getToStern(), "m");
        addKmlTableRow(desc, "BEAM", target.getToStarboard() + target.getToPort(), "m");
        desc.append("<tr><td><hr></td><td><hr></td></tr>");
        addKmlTableRow(desc, "DST", target.getDestination(), "");
        addKmlTableRow(desc, "CARGO", target.getCargoType(), "");
        desc.append("<tr><td><hr></td><td><hr></td></tr>");
        addKmlTableRow(desc, "DTG", DATE_FORMAT_DTG.format(calendar.getTime()).toUpperCase(), "");
        addKmlTableRow(desc, "POS", positionReport.getPositionTime().toString(), "");
        addKmlTableRow(desc, "SRC", positionReport.isEstimated() ? "Estimated":"AIS", "");
        addKmlTableRow(desc, "HDG", positionReport.getHeading(), "deg");
        addKmlTableRow(desc, "COG", Float.valueOf(positionReport.getCog()).intValue(), "deg");
        addKmlTableRow(desc, "SOG", positionReport.getSog(), "kts");
        addKmlTableRow(desc, "NAV", positionReport.getNavigationalStatus(), "");
        desc.append("</table>");

        desc.append("<p><a href=\"http://www.marinetraffic.com/en/ais/details/ships/");
        desc.append(target.getMmsi());
        desc.append("\">Lookup on marinetraffic.com</a></p>");

        return desc.toString();
    }

    private static void addKmlTableRow(StringBuffer table, Object c1, Object c2, Object c3) {
        table.append("<tr><td>");
        table.append(c1);
        table.append("</td><td>");
        table.append(c2);
        table.append(" ");
        table.append(c3);
        table.append("</td></tr>");
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
                    placemark.withStyleUrl(getStyle(target, false));
                }
            }
        }
    }

    private static String getStyle(ScenarioTracker.Target target, boolean estimatedPosition) {
        if (target.isTagged(STYLE1_TAG)) {
            return STYLE1_TAG + (estimatedPosition ? ESTIMATED_EXTENSION : "");
        } else if (target.isTagged(STYLE2_TAG)) {
            return STYLE2_TAG + (estimatedPosition ? ESTIMATED_EXTENSION : "");
        } else if (target.isTagged(STYLE3_TAG)) {
            return STYLE3_TAG + (estimatedPosition ? ESTIMATED_EXTENSION : "");
        } else {
            return "ShipDefaultStyle";
        }
    }

    private void createKmlShipShapePlacemark(Folder targetFolder, String mmsi, String name, Long timespanBegin, Long timespanEnd, double latitude, double longitude, float cog, float sog, int heading, float toBow, float toStern, float toPort, float toStarboard, boolean safetyZoneEllipse, String style) {
        String begin = null;
        String end = null;
        if (timespanBegin != null && timespanEnd != null) {
            calendar.setTimeInMillis(timespanBegin);
            begin = DATE_FORMAT.format(calendar.getTime());
            calendar.setTimeInMillis(timespanEnd);
            end = DATE_FORMAT.format(calendar.getTime());
        }

        Placemark placemarkForShipShape = targetFolder
            .createAndAddPlacemark()
                .withVisibility(true)
                .withId(mmsi)
                .withName(name)
                .withStyleUrl("#" + style);

        if (begin != null && end != null) {
            placemarkForShipShape.createAndSetTimeSpan()
                .withBegin(begin)
                .withEnd(end);
        }

        addKmlShipGeometry(placemarkForShipShape, latitude, longitude, heading, toBow, toStern, toPort, toStarboard);

        if (safetyZoneEllipse) {
            Placemark placemarkForEllipse = targetFolder
                .createAndAddPlacemark()
                    .withVisibility(true)
                    .withId(mmsi + "-ellipse")
                    .withName(name + "'s ellipse")
                    .withStyleUrl("#" + style);

            if (begin != null && end != null) {
                placemarkForEllipse.createAndSetTimeSpan()
                    .withBegin(begin)
                    .withEnd(end);
            }

            addKmlEllipseGeometry(placemarkForEllipse, latitude, longitude, cog, sog, toStern+toBow, toPort+toStarboard, toStern, toStarboard);
        }
    }

    private void createKmlShipIconPlacemark(Folder targetFolder, Long timespanBegin, Long timespanEnd, double latitude, double longitude, float cog, String description) {
        String begin = null;
        String end = null;
        if (timespanBegin != null && timespanEnd != null) {
            calendar.setTimeInMillis(timespanBegin);
            begin = DATE_FORMAT.format(calendar.getTime());
            calendar.setTimeInMillis(timespanEnd);
            end = DATE_FORMAT.format(calendar.getTime());
        }

        Placemark placemarkForShipIcon = targetFolder
            .createAndAddPlacemark()
                .withDescription("");

        Style style = placemarkForShipIcon
            .createAndAddStyle()
                .withId("shipIconStyle");

        style.createAndSetBalloonStyle()
            .withText(description);

        style.createAndSetIconStyle()
            .withScale(1.0)
            .withHeading((int) cog)
            .createAndSetIcon()
                .withHref("http://earth.google.com/images/kml-icons/track-directional/track-0.png");

        placemarkForShipIcon
            .createAndSetPoint()
                .withCoordinates(Lists.newArrayList(new Coordinate(longitude, latitude)))
                .withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);

        if (begin != null && end != null) {
            placemarkForShipIcon
                .createAndSetTimeSpan()
                    .withBegin(begin)
                    .withEnd(end);
        }
    }

    private void createSituationPlacemark(Folder targetFolder, double latitude, double longitude) {
        Placemark placemarkForSituationIcon = targetFolder
            .createAndAddPlacemark()
                .withDescription(snapshotDescriptionSupplier.get());

        Style style = placemarkForSituationIcon
            .createAndAddStyle()
                .withId("situationIconStyle");

        style
            .createAndSetBalloonStyle()
                .withText(snapshotDescriptionSupplier.get());

        style.createAndSetIconStyle()
                .withScale(1.0)
                .createAndSetIcon()
                    .withHref("http://maps.google.com/mapfiles/kml/pal3/icon33.png");

        placemarkForSituationIcon
            .createAndSetPoint()
                .withCoordinates(Lists.newArrayList(new Coordinate(longitude, latitude)))
                .withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);
    }

    /**
     * Create a KML geometry to symbolize a safety zone ellipses.
     */
    private static void addKmlEllipseGeometry(Placemark placemark, double latitude, double longitude, float cog, float sog, float loa, float beam, float dimStern, float dimStarbord) {
        Position p = Position.create(latitude, longitude);
        Ellipse safetyZone = safetyZone(p, p, cog, sog, loa, beam, dimStern, dimStarbord);

        List<Position> perimeter = safetyZone.samplePerimeter(64);

        // Convert points into geographic coordinates and a KML geometry
        LinearRing shipGeometry = placemark
                .createAndSetLinearRing()
                .withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);

        for (Position position : perimeter) {
            shipGeometry.addToCoordinates(position.getLongitude(), position.getLatitude());
        }
        // Close linear ring
        shipGeometry.addToCoordinates(perimeter.get(0).getLongitude(), perimeter.get(0).getLatitude());
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
    private static void addKmlShipGeometry(Placemark placemark, double lat, double lon, float heading, float toBow /* A */, float toStern /* B */, float toPort /* C */, float toStarbord /* D */) {
        // If the ship dimensions are not found then create a small ship
        if (toBow < 0 || toStern < 0) {
            toBow = 20;
            toStern = 4;
        }
        if (toPort < 0 || toStarbord < 0) {
            toPort = (int) ((toBow + toStern) / 6.5);
            toStarbord = toPort;
        }

        float szA = toBow;
        float szB = toStern;
        float szC = toPort;
        float szD = toStarbord;

        // The ship consists of 5 points which are stored in shipPnts()
        // To begin with the points are in meters
        Point[] points = new Point[] {
            new Point(-szB, szC),                     // stern port
            new Point(-szB + 0.85*(szA + szB), szC),
            new Point(szA, szC - (szC + szD)/2.0),    // bow
            new Point(-szB + 0.85*(szA + szB), -szD),
            new Point(-szB, -szD)                     // stern starboard
        };

        // Rotate ship. Each ship has its own coordinate system with
        // origin in the ais-position of the ship
        double thetaDeg = CoordinateConverter.compass2cartesian(heading);
        for (int i = 0; i < points.length; i++) {
            points[i] = points[i].rotate(Point.ORIGIN, thetaDeg);
        }

        // Convert ship coordinates into geographic coordinates and a KML geometry
        LinearRing shipGeometry = placemark
            .createAndSetLinearRing()
            .withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);

        CoordinateConverter coordinateConverter = new CoordinateConverter(lon, lat);
        Boundary boundary = placemark.createAndSetPolygon().createAndSetOuterBoundaryIs();

        boundary.setLinearRing(shipGeometry);
        for (Point point : points) {
            shipGeometry.addToCoordinates(coordinateConverter.x2Lon(point.getX(), point.getY()), coordinateConverter.y2Lat(point.getX(), point.getY()));
        }
    }
}
