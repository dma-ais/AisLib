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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.micromata.opengis.kml.v_2_2_0.AltitudeMode;
import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Camera;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;
import dk.dma.ais.message.NavigationalStatus;
import dk.dma.ais.message.ShipTypeCargo;
import dk.dma.ais.tracker.scenarioTracker.ScenarioTracker;
import dk.dma.commons.util.io.OutputStreamSink;
import dk.dma.enav.model.geometry.BoundingBox;
import dk.dma.enav.model.geometry.Ellipse;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.util.CoordinateConverter;
import dk.dma.enav.util.compass.CompassUtils;
import dk.dma.enav.util.geometry.Point;
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static dk.dma.enav.safety.SafetyZones.safetyZone;
import static java.lang.Math.min;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This class receives AisPacket and use them to build a scenario
 *
 * When the sink is closed it dumps the entire target state to the output stream in KML format.
 *
 * TODO Even though the triggerSnapshot predicate encourages generation of multiple snapshots, only one is currently
 * supported.
 *
 * @author Thomas Borg Salling <tbsalling@tbsalling.dk>
 */
@NotThreadSafe
class AisPacketKMLOutputSink extends OutputStreamSink<AisPacket> {

    private static final Logger LOG = LoggerFactory.getLogger(AisPacketKMLOutputSink.class);

    static {
        LOG.debug("AisPacketKMLOutputSink loaded.");
    }

    {
        LOG.debug(getClass().getSimpleName() + " created (" + this + ").");
    }

    /**
     * The tracker which will be used to build the scenario that will be written as KML.
     */
    private final ScenarioTracker scenarioTracker = new ScenarioTracker();

    /**
     * Only AisPackets passing this filter will be passed to the scenarioTracker.
     */
    private final Predicate<? super AisPacket> filter;

    /** Include the 'situation' folder in the generated KML */
    final boolean createSituationFolder;

    /** Include the 'movements' folder in the generated KML */
    final boolean createMovementsFolder;

    /** Include the 'tracks' folder in the generated KML */
    final boolean createTracksFolder;

    private final Predicate<? super AisPacket> isPrimaryTarget;

    private final Predicate<? super AisPacket> isSecondaryTarget;

    /**
     * This predicate returns true when a packet should trigger a snapshot
     */
    private final Predicate<? super AisPacket> triggerSnapshot;

    /**
     * This function supplies an HTML description of a snapshot triggered by predicate 'triggerSnapshot'
     */
    private final Supplier<? extends String> snapshotDescriptionSupplier;

    /**
     * KML folder title
     */
    private final Supplier<? extends String> title;

    /**
     * KML folder description
     */
    private final Supplier<? extends String> description;

    /**
     * KML movement folder interpolation step
     */
    private final Supplier<? extends Integer> movementInterpolationStep;

    /**
     * KML vessel icon href
     */
    private final BiFunction<? super ShipTypeCargo, ? super NavigationalStatus, ? extends String> iconHrefSupplier;

    private static final String KML_STYLE_PRIMARY_SHIP = "Ship1Style";

    private static final String KML_STYLE_SECONDARY_SHIP = "Ship2Style";

    private static final String KML_STYLE_OTHER_SHIP = "ShipDefaultStyle";

    private static final String KML_STYLE_EXTENSION_ESTIMATED_POSITION = "Estimated";

    private static final String KML_COLOR_PRIMARY_SHIP = "00ff00";

    private static final String KML_COLOR_SECONDARY_SHIP = "0000ff";

    private static final String KML_COLOR_OTHER_SHIP = "14f0fa";

    private static final String HTML_COLOR_PRIMARY_SHIP = "#00ff00";

    private static final String HTML_COLOR_SECONDARY_SHIP = "#ff0000";

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

    /**
     * Timespan for KML positions in situation and movement folders
     */
    private static final int KML_POSITION_TIMESPAN_SECS = 1;

    public AisPacketKMLOutputSink() {
        this.filter = e -> true;
        this.createSituationFolder = true;
        this.createMovementsFolder = true;
        this.createTracksFolder = true;
        this.isPrimaryTarget = e -> false;
        this.isSecondaryTarget = e -> false;
        this.triggerSnapshot = e -> false;
        this.snapshotDescriptionSupplier = null;
        this.title = defaultTitleSupplier;
        this.description = defaultDescriptionSupplier;
        this.movementInterpolationStep = defaultMovementInterpolationStepSupplier;
        this.iconHrefSupplier = defaultIconHrefSupplier;
    }

    /**
     * Create a sink that writes KML contents to outputStream - but build the scenario only from AisPackets which comply
     * with the filter predicate.
     *
     * @param filter
     *            a filter predicate for pre-filtering of AisPackets.
     */
    public AisPacketKMLOutputSink(Predicate<? super AisPacket> filter) {
        this.filter = filter;
        this.createSituationFolder = true;
        this.createMovementsFolder = true;
        this.createTracksFolder = true;
        this.isPrimaryTarget = e -> false;
        this.isSecondaryTarget = e -> false;
        this.triggerSnapshot = e -> false;
        this.snapshotDescriptionSupplier = null;
        this.title = defaultTitleSupplier;
        this.description = defaultDescriptionSupplier;
        this.movementInterpolationStep = defaultMovementInterpolationStepSupplier;
        this.iconHrefSupplier = defaultIconHrefSupplier;
    }

    /**
     * Create a sink that writes KML contents to outputStream - but build the scenario only from AisPackets which comply
     * with the filter predicate.
     *
     * @param filter
     *            a filter predicate for pre-filtering of AisPackets before they are passed to the tracker.
     * @param createSituationFolder
     *            create and populate the 'situation' folder in the generated KML
     * @param createMovementsFolder
     *            create and populate the 'movements' folder in the generated KML
     * @param createTracksFolder
     *            create and populate the 'tracks' folder in the generated KML
     * @param isPrimaryTarget
     *            Apply primary KML styling to targets which are updated by packets that pass this predicate.
     * @param isSecondaryTarget
     *            Apply secondary KML styling to targets which are updated by packets that pass this predicate.
     */
    public AisPacketKMLOutputSink(Predicate<? super AisPacket> filter, boolean createSituationFolder,
            boolean createMovementsFolder, boolean createTracksFolder, Predicate<? super AisPacket> isPrimaryTarget,
            Predicate<? super AisPacket> isSecondaryTarget, Predicate<? super AisPacket> triggerSnapshot,
            Supplier<? extends String> snapshotDescriptionSupplier,
            Supplier<? extends Integer> movementInterpolationStepSupplier,
            BiFunction<? super ShipTypeCargo, ? super NavigationalStatus, ? extends String> iconHrefSupplier) {
        this.filter = filter;
        this.createSituationFolder = createSituationFolder;
        this.createMovementsFolder = createMovementsFolder;
        this.createTracksFolder = createTracksFolder;
        this.isPrimaryTarget = isPrimaryTarget;
        this.isSecondaryTarget = isSecondaryTarget;
        this.triggerSnapshot = triggerSnapshot;
        this.snapshotDescriptionSupplier = snapshotDescriptionSupplier;
        this.title = defaultTitleSupplier;
        this.description = defaultDescriptionSupplier;
        this.movementInterpolationStep = movementInterpolationStepSupplier == null ? defaultMovementInterpolationStepSupplier
                : movementInterpolationStepSupplier;
        this.iconHrefSupplier = (BiFunction<? super ShipTypeCargo, ? super NavigationalStatus, ? extends String>) (iconHrefSupplier == null ? defaultIconHrefSupplier
                : iconHrefSupplier);
    }

    /**
     * Create a sink that writes KML contents to outputStream - but build the scenario only from AisPackets which comply
     * with the filter predicate.
     *
     * @param filter
     *            a filter predicate for pre-filtering of AisPackets before they are passed to the tracker.
     * @param createSituationFolder
     *            create and populate the 'situation' folder in the generated KML
     * @param createMovementsFolder
     *            create and populate the 'movements' folder in the generated KML
     * @param createTracksFolder
     *            create and populate the 'tracks' folder in the generated KML
     * @param isPrimaryTarget
     *            Apply primary KML styling to targets which are updated by packets that pass this predicate.
     * @param isSecondaryTarget
     *            Apply secondary KML styling to targets which are updated by packets that pass this predicate.
     * @param supplyTitle
     *            Supplier of KML folder title
     * @param supplyDescription
     *            Supplier of KML folder description
     */
    public AisPacketKMLOutputSink(Predicate<? super AisPacket> filter, boolean createSituationFolder,
            boolean createMovementsFolder, boolean createTracksFolder, Predicate<? super AisPacket> isPrimaryTarget,
            Predicate<? super AisPacket> isSecondaryTarget, Predicate<? super AisPacket> triggerSnapshot,
            Supplier<? extends String> snapshotDescriptionSupplier,
            Supplier<? extends Integer> movementInterpolationStepSupplier, Supplier<? extends String> supplyTitle,
            Supplier<? extends String> supplyDescription,
            BiFunction<? super ShipTypeCargo, ? super NavigationalStatus, ? extends String> iconHrefSupplier) {
        this.filter = filter;
        this.createSituationFolder = createSituationFolder;
        this.createMovementsFolder = createMovementsFolder;
        this.createTracksFolder = createTracksFolder;
        this.isPrimaryTarget = isPrimaryTarget;
        this.isSecondaryTarget = isSecondaryTarget;
        this.triggerSnapshot = triggerSnapshot;
        this.snapshotDescriptionSupplier = snapshotDescriptionSupplier;
        this.title = supplyTitle == null ? defaultTitleSupplier : supplyTitle;
        this.description = supplyDescription == null ? defaultDescriptionSupplier : supplyDescription;
        this.movementInterpolationStep = movementInterpolationStepSupplier == null ? defaultMovementInterpolationStepSupplier
                : movementInterpolationStepSupplier;
        this.iconHrefSupplier = (BiFunction<? super ShipTypeCargo, ? super NavigationalStatus, ? extends String>) (iconHrefSupplier == null ? defaultIconHrefSupplier
                : iconHrefSupplier);
        System.out.println(toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(OutputStream stream, AisPacket packet, long count) throws IOException {
        if (filter.test(packet)) {
            scenarioTracker.update(packet);

            if (isPrimaryTarget.test(packet)) {
                scenarioTracker.tagTarget(packet.tryGetAisMessage().getUserId(), KML_STYLE_PRIMARY_SHIP);
            }
            if (isSecondaryTarget.test(packet)) {
                scenarioTracker.tagTarget(packet.tryGetAisMessage().getUserId(), KML_STYLE_SECONDARY_SHIP);
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
            AisPacketReader reader = AisPacketReader.createFromFile(
                    Paths.get("/Users/tbsalling/Desktop/ais-sample.txt"), true);
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
            StringBuffer description = new StringBuffer();
            Date scenarioBegin = scenarioTracker.scenarioBegin();
            Date scenarioEnd = scenarioTracker.scenarioEnd();
            if (scenarioBegin != null) {
                description.append("Scenario");
                description.append(" starting ");
                description.append(scenarioBegin.toString());
                if (scenarioEnd != null) {
                    description.append(" and ending ");
                    description.append(scenarioEnd.toString());
                }
            }
            return description.toString();
        }
    };

    private Supplier<Integer> defaultMovementInterpolationStepSupplier = new Supplier<Integer>() {
        @Override
        public Integer get() {
            return null;
        }
    };

    private BiFunction<ShipTypeCargo, NavigationalStatus, String> defaultIconHrefSupplier = new BiFunction<ShipTypeCargo, NavigationalStatus, String>() {
        @Override
        public String apply(ShipTypeCargo shipTypeCargo, NavigationalStatus navigationalStatus) {
            return "http://earth.google.com/images/kml-icons/track-directional/track-0.png";
        }
    };

    protected Kml createKml() throws IOException {
        Kml kml = new Kml();

        Document document = kml.createAndSetDocument().withOpen(true);

        String docDesc = description.get();
        if (!isBlank(docDesc)) {
            document.withDescription(docDesc);
        }

        String docTitle = title.get();
        if (!isBlank(docTitle)) {
            document.withName(docTitle);
        }

        Camera camera = document.createAndSetCamera().withAltitude(2000).withHeading(0).withTilt(0)
                .withAltitudeMode(AltitudeMode.ABSOLUTE);

        if (scenarioTracker.boundingBox() != null) {
            camera.withLatitude(
                    (scenarioTracker.boundingBox().getMaxLat() + scenarioTracker.boundingBox().getMinLat()) / 2.0)
                    .withLongitude(
                            (scenarioTracker.boundingBox().getMaxLon() + scenarioTracker.boundingBox().getMinLon()) / 2.0);
        }

        // Create all ship styles
        createKmlStyles(document);

        Folder rootFolder = document.createAndAddFolder().withName("Vessel scenario").withOpen(true)
                .withVisibility(true);

        StringBuffer rootFolderName = new StringBuffer();
        Date scenarioBegin = scenarioTracker.scenarioBegin();
        Date scenarioEnd = scenarioTracker.scenarioEnd();
        if (scenarioBegin != null) {
            rootFolderName.append(scenarioBegin.toString());
            if (scenarioEnd != null) {
                rootFolderName.append(" - ");
            }
        }
        if (scenarioEnd != null) {
            rootFolderName.append(scenarioEnd.toString());
        }
        rootFolder.withDescription(rootFolderName.toString());

        // Generate bounding box
        createKmlBoundingBox(rootFolder);

        // Generate situation folder
        if (createSituationFolder && snapshotTimes.size() >= 1) {
            createKmlSituationFolder(rootFolder, snapshotTimes.iterator().next());

            if (snapshotTimes.size() > 1) {
                System.err.println("Only generates KML snapshot folder for first timestamp marked.");
            }
        }

        // Generate tracks folder
        if (createTracksFolder) {
            createKmlTracksFolder(rootFolder, e -> true);
        }

        // Generate movements folder
        if (createMovementsFolder) {
            createKmlMovementsAndIconsFolders(rootFolder);
        }

        return kml;
    }

    private static void createKmlStyles(Document document) {
        // For colors - http://www.zonums.com/gmaps/kml_color/
        document.createAndAddStyle().withId("bbox").createAndSetLineStyle().withColor("cccc00b0").withWidth(2.5);

        createStyle(document, KML_STYLE_OTHER_SHIP, 2, "60" + KML_COLOR_OTHER_SHIP, "FF" + KML_COLOR_OTHER_SHIP);
        createStyle(document, KML_STYLE_PRIMARY_SHIP, 2, "80" + KML_COLOR_PRIMARY_SHIP, "ff" + KML_COLOR_PRIMARY_SHIP);
        createStyle(document, KML_STYLE_SECONDARY_SHIP, 2, "80" + KML_COLOR_SECONDARY_SHIP, "ff"
                + KML_COLOR_SECONDARY_SHIP);
        createStyle(document, KML_STYLE_PRIMARY_SHIP + KML_STYLE_EXTENSION_ESTIMATED_POSITION, 2, "60"
                + KML_COLOR_PRIMARY_SHIP, "80" + KML_COLOR_PRIMARY_SHIP);
        createStyle(document, KML_STYLE_SECONDARY_SHIP + KML_STYLE_EXTENSION_ESTIMATED_POSITION, 2, "60"
                + KML_COLOR_SECONDARY_SHIP, "80" + KML_COLOR_SECONDARY_SHIP);
    }

    private static void createStyle(Document document, String styleName, int width, String lineColor, String polyColor) {
        Style style = document.createAndAddStyle().withId(styleName);
        style.createAndSetLineStyle().withWidth(width).withColor(lineColor);
        style.createAndSetPolyStyle().withColor(polyColor);
    }

    private void createKmlBoundingBox(Folder kmlNode) {
        BoundingBox bbox = scenarioTracker.boundingBox();

        if (bbox != null) {
            kmlNode.createAndAddPlacemark().withId("bbox").withStyleUrl("#bbox").withName("Bounding box")
            .withVisibility(true).createAndSetLinearRing().addToCoordinates(bbox.getMaxLon(), bbox.getMaxLat())
                    .addToCoordinates(bbox.getMaxLon(), bbox.getMinLat())
                    .addToCoordinates(bbox.getMinLon(), bbox.getMinLat())
                    .addToCoordinates(bbox.getMinLon(), bbox.getMaxLat())
                    .addToCoordinates(bbox.getMaxLon(), bbox.getMaxLat());
        }
    }

    private void createKmlSituationFolder(Folder kmlNode, long atTime) {
        Folder situationFolder = kmlNode.createAndAddFolder().withName("Situation")
                .withDescription(new Date(atTime).toString()).withOpen(false).withVisibility(false);

        BoundingBox bbox = scenarioTracker.boundingBox();
        Set<ScenarioTracker.Target> targets = scenarioTracker.getTargetsHavingPositionUpdates();

        final Date t = new Date(atTime);

        ScenarioTracker.Target primaryTarget = null, secondaryTarget = null;
        ScenarioTracker.Target.PositionReport primaryPositionReport = null, secondaryPositionReport = null;

        for (ScenarioTracker.Target target : targets) {
            ScenarioTracker.Target.PositionReport estimatedPosition = target.getPositionReportAt(t, 10);
            if (estimatedPosition != null && bbox.contains(estimatedPosition.getPositionTime())) {
                createKmlShipShapePlacemark(situationFolder, String.valueOf(target.getMmsi()), target.getName(), null, null,
                        estimatedPosition.getLatitude(), estimatedPosition.getLongitude(), estimatedPosition.getCog(),
                        estimatedPosition.getSog(), estimatedPosition.getHeading(), target.getToBow(),
                        target.getToStern(), target.getToPort(), target.getToStarboard(),
                        target.isTagged(KML_STYLE_PRIMARY_SHIP), getStyle(target, false), true);
                createKmlShipIconPlacemark(situationFolder, target.getShipTypeCargo(),
                        estimatedPosition.getNavigationalStatus(), null, null, estimatedPosition.getLatitude(),
                        estimatedPosition.getLongitude(), estimatedPosition.getCog(), "<h2>Vessel details</h2>"
                                + generateHtmlShipDescription(target, estimatedPosition, null, null), true);
                if (primaryTarget == null && target.isTagged(KML_STYLE_PRIMARY_SHIP)) {
                    primaryTarget = target;
                    primaryPositionReport = estimatedPosition;
                }
                if (secondaryTarget == null && target.isTagged(KML_STYLE_SECONDARY_SHIP)) {
                    secondaryTarget = target;
                    secondaryPositionReport = estimatedPosition;
                }
            }
        }
        createSituationPlacemark(situationFolder, primaryTarget, primaryPositionReport, secondaryTarget,
                secondaryPositionReport);
    }

    private void createKmlMovementsAndIconsFolders(Folder kmlNode) {
        Set<ScenarioTracker.Target> targets = scenarioTracker.getTargetsHavingPositionUpdates();

        final boolean useInterpolation = movementInterpolationStep.get() != null && movementInterpolationStep.get() > 0;

        Folder movementFolder = kmlNode.createAndAddFolder().withName("Movements").withOpen(false)
                .withVisibility(false);

        Folder iconFolder = kmlNode.createAndAddFolder().withName("Icons").withOpen(false).withVisibility(false);

        for (ScenarioTracker.Target target : targets) {
            Folder targetShipShapeFolder = movementFolder.createAndAddFolder().withName(target.getName())
                    .withDescription("Movements for MMSI " + target.getMmsi());
            Folder targetShipIconFolder = iconFolder.createAndAddFolder().withName(target.getName())
                    .withDescription("Icons for MMSI " + target.getMmsi());
            if (useInterpolation) {
                final long t1 = target.timeOfFirstPositionReport().getTime();
                final long t2 = target.timeOfLastPositionReport().getTime();
                final int dt = movementInterpolationStep.get() * 1000;
                for (long t = t1; t <= t2; t += dt) {
                    ScenarioTracker.Target.PositionReport positionReport = target.getPositionReportAt(new Date(t),
                            KML_POSITION_TIMESPAN_SECS);
                    createKmlShipShapePlacemark(targetShipShapeFolder, String.valueOf(target.getMmsi()), target.getName(), t
                            - (dt - 100), t, positionReport.getLatitude(), positionReport.getLongitude(),
                            positionReport.getCog(), positionReport.getSog(), positionReport.getHeading(),
                            target.getToBow(), target.getToStern(), target.getToPort(), target.getToStarboard(), false,
                            getStyle(target, positionReport.isEstimated()), false);
                    createKmlShipIconPlacemark(targetShipIconFolder, target.getShipTypeCargo(),
                            positionReport.getNavigationalStatus(), t - (dt - 100), t, positionReport.getLatitude(),
                            positionReport.getLongitude(), positionReport.getCog(), "<h2>Vessel details</h2>"
                                    + generateHtmlShipDescription(target, positionReport, null, null), false);
                }
            } else {
                Set<ScenarioTracker.Target.PositionReport> positionReports = target.getPositionReports();

                ScenarioTracker.Target.PositionReport[] positionReportsArray = positionReports
                        .toArray(new ScenarioTracker.Target.PositionReport[positionReports.size()]);
                for (int i = 0; i < positionReportsArray.length; i++) {
                    ScenarioTracker.Target.PositionReport positionReport = positionReportsArray[i];
                    ScenarioTracker.Target.PositionReport nextPositionReport = null;
                    if (i < positionReportsArray.length - 1) {
                        nextPositionReport = positionReportsArray[i + 1];
                    }

                    final long maxTimespan = 10000L;
                    final long timespan = min(nextPositionReport != null ? nextPositionReport.getTimestamp()
                            - positionReport.getTimestamp() - 1 : maxTimespan, maxTimespan);

                    createKmlShipShapePlacemark(targetShipShapeFolder, String.valueOf(target.getMmsi()), target.getName(),
                            positionReport.getTimestamp(), positionReport.getTimestamp() + timespan,
                            positionReport.getLatitude(), positionReport.getLongitude(), positionReport.getCog(),
                            positionReport.getSog(), positionReport.getHeading(), target.getToBow(),
                            target.getToStern(), target.getToPort(), target.getToStarboard(), false,
                            getStyle(target, positionReport.isEstimated()), false);
                    createKmlShipIconPlacemark(targetShipIconFolder, target.getShipTypeCargo(),
                            positionReport.getNavigationalStatus(), positionReport.getTimestamp(),
                            positionReport.getTimestamp() + timespan, positionReport.getLatitude(),
                            positionReport.getLongitude(), positionReport.getCog(), "<h2>Vessel details</h2>"
                                    + generateHtmlShipDescription(target, positionReport, null, null), false);
                }
            }
        }
    }

    private String generateHtmlEventDescription() {
        return snapshotDescriptionSupplier.get();
    }

    private String generateHtmlShipDescription(ScenarioTracker.Target target1,
            ScenarioTracker.Target.PositionReport positionReport1, ScenarioTracker.Target target2,
            ScenarioTracker.Target.PositionReport positionReport2) {
        String dtg1 = null, dtg2 = null;
        if (positionReport1 != null) {
            calendar.setTimeInMillis(positionReport1.getTimestamp());
            dtg1 = DATE_FORMAT_DTG.format(calendar.getTime()).toUpperCase();
        }
        if (positionReport2 != null) {
            calendar.setTimeInMillis(positionReport2.getTimestamp());
            dtg2 = DATE_FORMAT_DTG.format(calendar.getTime()).toUpperCase();
        }

        String ref1 = null, ref2 = null;
        if (target1 != null) {
            ref1 = "<a href=\"http://www.marinetraffic.com/en/ais/details/ships/" + target1.getMmsi()
                    + "\">marinetraffic.com</a>";
        }
        if (target2 != null) {
            ref2 = "<a href=\"http://www.marinetraffic.com/en/ais/details/ships/" + target2.getMmsi()
                    + "\">marinetraffic.com</a>";
        }

        StringBuffer desc = new StringBuffer(512);
        desc.append("<div><table width=\"300\">");
        if (target1 != null && target2 != null) {
            generateHtmlTableRow(desc, "", "<h4 style=\"color:" + HTML_COLOR_PRIMARY_SHIP + ";\">Primary</h4>",
                    "<h4 style=\"color:" + HTML_COLOR_SECONDARY_SHIP + ";\">Secondary</h4>", "");
            generateHtmlTableRow(desc, "", "", "", "");
        }
        generateHtmlTableRow(desc, "NAME", target1 == null ? null : target1.getName(),
                target2 == null ? null : target2.getName(), "");
        generateHtmlTableRow(desc, "MMSI", target1 == null ? null : target1.getMmsi(),
                target2 == null ? null : target2.getMmsi(), "");
        generateHtmlTableRow(desc, "IMO", target1 == null ? null : target1.getImo(),
                target2 == null ? null : target2.getImo(), "");
        generateHtmlTableRow(desc, "TYPE", target1 == null ? null : target1.getShipTypeAsString(),
                target2 == null ? null : target2.getShipTypeAsString(), "");
        generateHtmlTableRow(desc, "LOA", target1 == null ? null : target1.getToBow() + target1.getToStern(),
                target2 == null ? null : target2.getToBow() + target2.getToStern(), "m");
        generateHtmlTableRow(desc, "BEAM", target1 == null ? null : target1.getToStarboard() + target1.getToPort(),
                target2 == null ? null : target2.getToStarboard() + target2.getToPort(), "m");
        desc.append("<tr><td><hr></td><td><hr></td><td><hr></td></tr>");
        generateHtmlTableRow(desc, "DST", target1 == null ? null : target1.getDestination(), target2 == null ? null
                : target2.getDestination(), "");
        generateHtmlTableRow(desc, "CARGO", target1 == null ? null : target1.getCargoTypeAsString(),
                target2 == null ? null : target2.getCargoTypeAsString(), "");
        desc.append("<tr><td><hr></td><td><hr></td><td><hr></td></tr>");
        if (positionReport1 != null || positionReport2 != null) {
            generateHtmlTableRow(desc, "DTG", dtg1, dtg2, "");
            generateHtmlTableRow(desc, "POS", positionReport1 == null ? null : positionReport1.getPositionTime(),
                    positionReport2 == null ? null : positionReport2.getPositionTime(), "");
            generateHtmlTableRow(desc, "SRC", positionReport1 == null ? null
                    : positionReport1.isEstimated() ? "Estimated" : "AIS", positionReport2 == null ? null
                            : positionReport2.isEstimated() ? "Estimated" : "AIS", "");
            generateHtmlTableRow(desc, "HDG", positionReport1 == null ? null : positionReport1.getHeading(),
                    positionReport2 == null ? null : positionReport2.getHeading(), "deg");
            generateHtmlTableRow(desc, "COG", positionReport1 == null ? null : Float.valueOf(positionReport1.getCog())
                    .intValue(), positionReport2 == null ? null : Float.valueOf(positionReport2.getCog()).intValue(),
                    "deg");
            generateHtmlTableRow(desc, "SOG", positionReport1 == null ? null : positionReport1.getSog(),
                    positionReport2 == null ? null : positionReport2.getSog(), "kts");
            generateHtmlTableRow(desc, "NAV", positionReport1 == null ? null : positionReport1.getNavigationalStatus(),
                    positionReport2 == null ? null : positionReport2.getNavigationalStatus(), "");
            desc.append("<tr><td><hr></td><td><hr></td><td><hr></td></tr>");
        }
        generateHtmlTableRow(desc, "REF", ref1, ref2, "");
        desc.append("</table></div>");

        return desc.toString();
    }

    private static void generateHtmlTableRow(StringBuffer table, String legend, Object c1, Object c2, String unit) {
        table.append("<tr><td>");
        table.append(legend);
        table.append("</td><td>");
        table.append(c1 == null ? "" : c1);
        table.append(c1 == null ? "" : " ");
        table.append(c1 == null ? "" : unit);
        table.append("</td><td>");
        table.append(c2 == null ? "" : c2);
        table.append(c2 == null ? "" : " ");
        table.append(c2 == null ? "" : unit);
        table.append("</td></tr>");
    }

    private void createKmlTracksFolder(Folder kmlNode, Predicate<? super ScenarioTracker.Target> trackFor) {
        Set<ScenarioTracker.Target> targets = scenarioTracker.getTargetsHavingPositionUpdates();

        Folder tracksFolder = kmlNode.createAndAddFolder().withName("Tracks").withOpen(false).withVisibility(false);

        Folder cargoTracksFolder = null;
        Folder tankersTracksFolder = null;
        Folder passengerTracksFolder = null;
        Folder fishingTracksFolder = null;
        Folder classBTracksFolder = null;
        Folder otherTracksFolder = null;

        for (ScenarioTracker.Target target : targets) {
            if (trackFor.test(target)) {
                Set<ScenarioTracker.Target.PositionReport> positionReportReports = target.getPositionReports();
                if (positionReportReports.size() > 0) {

                    Folder folder = null;

                    ShipTypeCargo shipTypeCargo = target.getShipTypeCargo();
                    if (shipTypeCargo != null) {
                        ShipTypeCargo.ShipType shipType = shipTypeCargo.getShipType();
                        switch (shipType) {
                        case CARGO:
                            if (cargoTracksFolder == null) {
                                cargoTracksFolder = tracksFolder.createAndAddFolder().withName("Cargo").withOpen(false)
                                        .withVisibility(false);
                            }
                            folder = cargoTracksFolder;
                            break;
                        case TANKER:
                            if (tankersTracksFolder == null) {
                                tankersTracksFolder = tracksFolder.createAndAddFolder().withName("Tankers")
                                        .withOpen(false).withVisibility(false);
                            }
                            folder = tankersTracksFolder;
                            break;
                        case PASSENGER:
                            if (passengerTracksFolder == null) {
                                passengerTracksFolder = tracksFolder.createAndAddFolder().withName("Passenger")
                                        .withOpen(false).withVisibility(false);
                            }
                            folder = passengerTracksFolder;
                            break;
                        case FISHING:
                            if (fishingTracksFolder == null) {
                                fishingTracksFolder = tracksFolder.createAndAddFolder().withName("Fishing")
                                        .withOpen(false).withVisibility(false);
                            }
                            folder = fishingTracksFolder;
                            break;
                        case PLEASURE:
                        case SAILING:
                            if (classBTracksFolder == null) {
                                classBTracksFolder = tracksFolder.createAndAddFolder().withName("Class B")
                                        .withOpen(false).withVisibility(false);
                            }
                            folder = classBTracksFolder;
                            break;
                        }
                    }
                    if (folder == null) {
                        if (otherTracksFolder == null) {
                            otherTracksFolder = tracksFolder.createAndAddFolder().withName("Other").withOpen(false)
                                    .withVisibility(false);
                        }
                        folder = otherTracksFolder;
                    }

                    Placemark placemark = folder.createAndAddPlacemark().withId(String.valueOf(target.getMmsi()))
                            .withName(target.getName());

                    Style style = placemark.createAndAddStyle().withId("_" + target.getName() + "TrackStyle");
                    style.createAndSetBalloonStyle().withText(
                            "<h2>Vessel details</h2>" + generateHtmlShipDescription(target, null, null, null));

                    placemark.withStyleUrl(getStyle(target, false));

                    LineString lineString = placemark.createAndSetLineString();
                    for (ScenarioTracker.Target.PositionReport positionReport : positionReportReports) {
                        lineString.addToCoordinates(positionReport.getLongitude(), positionReport.getLatitude());
                    }
                }
            }
        }
    }

    private static String getStyle(ScenarioTracker.Target target, boolean estimatedPosition) {
        if (target.isTagged(KML_STYLE_PRIMARY_SHIP)) {
            return KML_STYLE_PRIMARY_SHIP + (estimatedPosition ? KML_STYLE_EXTENSION_ESTIMATED_POSITION : "");
        } else if (target.isTagged(KML_STYLE_SECONDARY_SHIP)) {
            return KML_STYLE_SECONDARY_SHIP + (estimatedPosition ? KML_STYLE_EXTENSION_ESTIMATED_POSITION : "");
        } else {
            return KML_STYLE_OTHER_SHIP;
        }
    }

    private void createKmlShipShapePlacemark(Folder targetFolder, String mmsi, String name, Long timespanBegin,
            Long timespanEnd, double latitude, double longitude, float cog, float sog, int heading, float toBow,
            float toStern, float toPort, float toStarboard, boolean safetyZoneEllipse, String style, boolean visible) {
        String begin = null;
        String end = null;
        if (timespanBegin != null && timespanEnd != null) {
            calendar.setTimeInMillis(timespanBegin);
            begin = DATE_FORMAT.format(calendar.getTime());
            calendar.setTimeInMillis(timespanEnd);
            end = DATE_FORMAT.format(calendar.getTime());
        }

        Placemark placemarkForShipShape = targetFolder.createAndAddPlacemark().withVisibility(visible).withId(mmsi)
                .withName(name).withStyleUrl("#" + style);

        if (begin != null && end != null) {
            placemarkForShipShape.createAndSetTimeSpan().withBegin(begin).withEnd(end);
        }

        addKmlShipGeometry(placemarkForShipShape, latitude, longitude, heading, toBow, toStern, toPort, toStarboard);

        if (safetyZoneEllipse) {
            Placemark placemarkForEllipse = targetFolder.createAndAddPlacemark().withVisibility(visible)
                    .withId(mmsi + "-ellipse").withName(name + "'s ellipse").withStyleUrl("#" + style);

            if (begin != null && end != null) {
                placemarkForEllipse.createAndSetTimeSpan().withBegin(begin).withEnd(end);
            }

            addKmlEllipseGeometry(placemarkForEllipse, latitude, longitude, cog, sog, toStern + toBow, toPort
                    + toStarboard, toStern, toStarboard);
        }
    }

    private void createKmlShipIconPlacemark(Folder targetFolder, ShipTypeCargo shipTypeCargo,
            NavigationalStatus navigationalStatus, Long timespanBegin, Long timespanEnd, double latitude,
            double longitude, float cog, String description, boolean visible) {
        String begin = null;
        String end = null;
        if (timespanBegin != null && timespanEnd != null) {
            calendar.setTimeInMillis(timespanBegin);
            begin = DATE_FORMAT.format(calendar.getTime());
            calendar.setTimeInMillis(timespanEnd);
            end = DATE_FORMAT.format(calendar.getTime());
        }

        Placemark placemarkForShipIcon = targetFolder.createAndAddPlacemark().withVisibility(visible)
                .withDescription("");

        Style style = placemarkForShipIcon.createAndAddStyle().withId("shipIconStyle");

        style.createAndSetBalloonStyle().withText(description);

        style.createAndSetIconStyle().withScale(1.0).withHeading((int) cog).createAndSetIcon()
                .withHref(iconHrefSupplier.apply(shipTypeCargo, navigationalStatus));

        placemarkForShipIcon.createAndSetPoint()
                .withCoordinates(Lists.newArrayList(new Coordinate(longitude, latitude)))
                .withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);

        if (begin != null && end != null) {
            placemarkForShipIcon.createAndSetTimeSpan().withBegin(begin).withEnd(end);
        }
    }

    private void createSituationPlacemark(Folder targetFolder, ScenarioTracker.Target primaryTarget,
            ScenarioTracker.Target.PositionReport primaryPositionReport, ScenarioTracker.Target secondaryTarget,
            ScenarioTracker.Target.PositionReport secondaryPositionReport) {

        Placemark placemarkForSituationIcon = targetFolder.createAndAddPlacemark().withDescription(
                snapshotDescriptionSupplier.get());

        Style style = placemarkForSituationIcon.createAndAddStyle().withId("situationIconStyle");

        style.createAndSetBalloonStyle().withText(
                "<h2>Situation</h2>"
                        + generateHtmlEventDescription()
                        + "<h2>Involved vessels</h2>"
                        + generateHtmlShipDescription(primaryTarget, primaryPositionReport, secondaryTarget,
                                secondaryPositionReport));

        style.createAndSetIconStyle().withScale(1.0).createAndSetIcon()
        .withHref("http://maps.google.com/mapfiles/kml/pal3/icon33.png");

        placemarkForSituationIcon
        .createAndSetPoint()
                .withCoordinates(
                Lists.newArrayList(new Coordinate(primaryPositionReport.getLongitude(), primaryPositionReport
                        .getLatitude()))).withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);
    }

    /**
     * Create a KML geometry to symbolize a safety zone ellipses.
     */
    private static void addKmlEllipseGeometry(Placemark placemark, double latitude, double longitude, float cog,
            float sog, float loa, float beam, float dimStern, float dimStarbord) {
        Position p = Position.create(latitude, longitude);
        Ellipse safetyZone = safetyZone(p, p, cog, sog, loa, beam, dimStern, dimStarbord);

        List<Position> perimeter = safetyZone.samplePerimeter(64);

        // Convert points into geographic coordinates and a KML geometry
        LinearRing shipGeometry = placemark.createAndSetLinearRing().withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);

        for (Position position : perimeter) {
            shipGeometry.addToCoordinates(position.getLongitude(), position.getLatitude());
        }
        // Close linear ring
        shipGeometry.addToCoordinates(perimeter.get(0).getLongitude(), perimeter.get(0).getLatitude());
    }

    /**
     * Create a KML geometry to symbolize a ship at the given position, at the given heading and with the given
     * dimensions.
     *
     * @param placemark
     *            Parent node
     * @param lat
     *            Ship's positional latitude in degrees.
     * @param lon
     *            Ship's positional longitude in degrees.
     * @param heading
     *            Ship's heading in degrees; 0 being north, 90 being east.
     * @param toBow
     *            Distance in meters from ship's position reference to ship's bow.
     * @param toStern
     *            Distance in meters from ship's position reference to ship's stern.
     * @param toPort
     *            Distance in meters from ship's position reference to port side at maximum beam.
     * @param toStarbord
     *            Distance in meters from ship's position reference to starboard side at maximum beam.
     * @return
     */
    private static void addKmlShipGeometry(Placemark placemark, double lat, double lon, float heading,
            float toBow /* A */, float toStern /* B */, float toPort /* C */, float toStarbord /* D */) {
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
        Point[] points = new Point[] { new Point(-szB, szC), // stern port
                new Point(-szB + 0.85 * (szA + szB), szC), new Point(szA, szC - (szC + szD) / 2.0), // bow
                new Point(-szB + 0.85 * (szA + szB), -szD), new Point(-szB, -szD) // stern starboard
        };

        // Rotate ship. Each ship has its own coordinate system with
        // origin in the ais-position of the ship
        double thetaDeg = CompassUtils.compass2cartesian(heading);
        for (int i = 0; i < points.length; i++) {
            points[i] = points[i].rotate(Point.ORIGIN, thetaDeg);
        }

        // Convert ship coordinates into geographic coordinates and a KML geometry
        LinearRing shipGeometry = placemark.createAndSetLinearRing().withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);

        CoordinateConverter coordinateConverter = new CoordinateConverter(lon, lat);
        Boundary boundary = placemark.createAndSetPolygon().createAndSetOuterBoundaryIs();

        boundary.setLinearRing(shipGeometry);
        for (Point point : points) {
            shipGeometry.addToCoordinates(coordinateConverter.x2Lon(point.getX(), point.getY()),
                    coordinateConverter.y2Lat(point.getX(), point.getY()));
        }
    }
}
