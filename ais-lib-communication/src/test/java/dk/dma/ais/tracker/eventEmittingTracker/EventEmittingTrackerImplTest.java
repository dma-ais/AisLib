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

package dk.dma.ais.tracker.eventEmittingTracker;

import com.google.common.eventbus.Subscribe;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage24;
import dk.dma.ais.message.AisMessage3;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.AisPosition;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import dk.dma.ais.tracker.eventEmittingTracker.events.CellChangedEvent;
import dk.dma.ais.tracker.eventEmittingTracker.events.PositionChangedEvent;
import dk.dma.ais.tracker.eventEmittingTracker.events.TimeEvent;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.model.geometry.PositionTime;
import dk.dma.enav.model.geometry.grid.Cell;
import dk.dma.enav.model.geometry.grid.Grid;
import net.maritimecloud.util.SpeedUnit;
import net.maritimecloud.util.geometry.PositionReader;
import net.maritimecloud.util.geometry.PositionReaderSimulator;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EventEmittingTrackerImplTest {

    final static int MMSI = 12345678;

    final String[] NMEA_TEST_STRINGS = {
        // GatehouseSourceTag [baseMmsi=2190067, country=DK, region=, timestamp=Thu Apr 10 15:30:28 CEST 2014]
        // [msgId=1, repeat=0, userId=219000606, cog=2010, navStatus=0, pos=(33024811,6011092) = (33024811,6011092), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=108, spare=0, syncState=1, trueHeading=200, utcSec=60, slotTimeout=6, subMessage=1063]
        "$PGHP,1,2014,4,10,13,30,28,385,219,,2190067,1,12*26\r\n" +
        "!BSVDM,1,1,,A,13@ng7P01dPeo6`OOc:onVAp0p@W,0*12",

        // GatehouseSourceTag [baseMmsi=2190067, country=DK, region=, timestamp=Thu Apr 10 15:30:29 CEST 2014]
        // [msgId=5, repeat=0, userId=219000606, callsign=OWNM@@@, dest=BOEJDEN-FYNSHAV@@@@@, dimBow=12, dimPort=8, dimStarboard=4, dimStern=58, draught=30, dte=0, eta=67584, imo=8222824, name=FRIGG SYDFYEN@@@@@@@, posType=1, shipType=61, spare=0, version=0]
        "$PGHP,1,2014,4,10,13,30,29,165,219,,2190067,1,28*22\r\n" +
        "!BSVDM,2,1,1,A,53@ng7P1uN6PuLpl000I8TLN1=T@ITDp0000000u1Pr844@P07PSiBQ1,0*7B\r\n" +
        "!BSVDM,2,2,1,A,CcAVCTj0EP00000,2*53",

        // GatehouseSourceTag [baseMmsi=2190067, country=DK, region=, timestamp=Thu Apr 10 15:30:38 CEST 2014]
        // [msgId=1, repeat=0, userId=219000606, cog=2010, navStatus=0, pos=(33024530,6010902) = (33024530,6010902), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=108, spare=0, syncState=1, trueHeading=200, utcSec=60, slotTimeout=2, subMessage=1427]
        "$PGHP,1,2014,4,10,13,30,38,88,219,,2190067,1,26*1E\r\n" +
        "!BSVDM,1,1,,B,13@ng7P01dPeo0dOOb4WnVAp0`FC,0*26",

        // GatehouseSourceTag [baseMmsi=2190075, country=DK, region=, timestamp=Thu Apr 10 15:30:49 CEST 2014]
        // [msgId=1, repeat=0, userId=219000606, cog=2010, navStatus=0, pos=(33024198,6010667) = (33024198,6010667), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=108, spare=0, syncState=1, trueHeading=200, utcSec=60, slotTimeout=4, subMessage=1852]
        "$PGHP,1,2014,4,10,13,30,49,429,219,,2190075,1,2D*56\r\n" +
        "!BSVDM,1,1,,A,13@ng7P01dPenqFOO`iWnVAp0hLt,0*2D",

        // GatehouseSourceTag [baseMmsi=2190067, country=DK, region=, timestamp=Thu Apr 10 15:30:59 CEST 2014]
        // [msgId=1, repeat=0, userId=219000606, cog=2010, navStatus=0, pos=(33023918,6010471) = (33023918,6010471), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=108, spare=0, syncState=1, trueHeading=200, utcSec=60, slotTimeout=1, subMessage=12528]
        "$PGHP,1,2014,4,10,13,30,59,428,219,,2190067,1,2D*55\r\n" +
        "!BSVDM,1,1,,B,13@ng7P01dPenk>OOWcWnVAp0W3h,0*2D",

        // GatehouseSourceTag [baseMmsi=2190067, country=DK, region=, timestamp=Thu Apr 10 15:31:09 CEST 2014]
        // [msgId=1, repeat=0, userId=219000606, cog=2020, navStatus=0, pos=(33023639,6010274) = (33023639,6010274), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=108, spare=0, syncState=1, trueHeading=200, utcSec=60, slotTimeout=2, subMessage=348]
        "$PGHP,1,2014,4,10,13,31,9,318,219,,2190067,1,4F*61\r\n" +
        "!BSVDM,1,1,,A,13@ng7P01dPene4OOVUoq6Ap0`5L,0*4F",

        // GatehouseSourceTag [baseMmsi=2190067, country=DK, region=, timestamp=Thu Apr 10 15:31:18 CEST 2014]
        // [msgId=1, repeat=0, userId=219000606, cog=2010, navStatus=0, pos=(33023417,6010117) = (33023417,6010117), posAcc=1, raim=0, specialManIndicator=0, rot=0, sog=108, spare=0, syncState=1, trueHeading=199, utcSec=60, slotTimeout=0, subMessage=2263]
        "$PGHP,1,2014,4,10,13,31,18,678,219,,2190067,1,03*23\r\n" +
        "!BSVDM,1,1,,B,13@ng7P01dPen`:OOUfGnV?p0PSG,0*03"
    };

    final Grid grid = Grid.createSize(100);

    /**
     * Test that grid cell change events are fired by the tracker when a simulated track is moving
     * north under the Great Belt bridge.
     *
     * Assumes grid size 100m.
     * Track starts in cell id 24686212289 (55°20'13.7"N,11°02'21.8"E) - (55°20'10.5"N,11°02'25.1"E)
     */
    @Test
    public void testGridChangeEventsFired() {
        // Starting position in the center of cell 24686212289
        Queue<AisMessage> messageQueue = initQueueOfAisMessages(MMSI);
        final int expectedNumberOfCellChangeEvents = messageQueue.size() - 1 /* minus the static msg */;

        // Create object under test
        final EventEmittingTrackerImpl tracker = new EventEmittingTrackerImpl(grid);

        // Wire up test subscriber
        // (discussion: https://code.google.com/p/guava-libraries/issues/detail?id=875)
        TestSubscriber testSubscriber = new TestSubscriber();
        tracker.registerSubscriber(testSubscriber);

        // Set up our expectations
        final long[] expectedSequenceOfCells = {
                24686212289L,
                24686613039L,
                24687013789L,
                24687414539L,
                24687815289L,
                24688216039L,
                24688616789L,
                24689017539L,
                24689418289L,
                24689819039L,
                24690219789L,
                24690620539L
        };
        int nextExpectedCellId = 0;

        // Play scenario through tracker
        long firstTimestamp = System.currentTimeMillis();
        int timeStep = 0;

        // Run test scenario and assert results
        assertEquals(0, tracker.getNumberOfTracks());

        while (!messageQueue.isEmpty()) {
            AisMessage message = messageQueue.remove();
            Date messageTimestamp = new Date(firstTimestamp + (timeStep++ * 10000)); // 10 secs between msgs
            System.out.println(messageTimestamp + ": " + message);
            tracker.update(messageTimestamp.getTime(), message);
            if (message instanceof IPositionMessage) {
                assertEquals(expectedSequenceOfCells[nextExpectedCellId++], testSubscriber.getCurrentCellId());
            }
        }

        assertEquals(expectedNumberOfCellChangeEvents, testSubscriber.getNumberOfCellIdChangedEventsReceived());
        assertEquals(1, tracker.getNumberOfTracks());
    }

    @Test
    public void testGridChangeEventsAreNotFiredForBlackListedMMSIs() {
        Queue<AisMessage> messageQueue = initQueueOfAisMessages(MMSI);
        final int expectedNumberOfCellChangeEvents = messageQueue.size() - 1 /* minus the static msg */;

        // Create object under test
        final EventEmittingTrackerImpl tracker = new EventEmittingTrackerImpl(grid, 1, MMSI, 3);

        // Wire up test subscriber
        // (discussion: https://code.google.com/p/guava-libraries/issues/detail?id=875)
        TestSubscriber testSubscriber = new TestSubscriber();
        tracker.registerSubscriber(testSubscriber);

        // Play scenario through tracker
        long firstTimestamp = System.currentTimeMillis();
        int timeStep = 0;

        // Run test scenario and assert results
        assertEquals(0, tracker.getNumberOfTracks());

        while (!messageQueue.isEmpty()) {
            AisMessage message = messageQueue.remove();
            Date messageTimestamp = new Date(firstTimestamp + (timeStep++ * 10000)); // 10 secs between msgs
            System.out.println(messageTimestamp + ": " + message);

            tracker.update(messageTimestamp.getTime(), message);

            assertEquals(0, tracker.getNumberOfTracks());
            assertFalse(testSubscriber.isPositionChangedEventFired());
            assertFalse(testSubscriber.isCellIdChangedEventFired());
            assertEquals(0, testSubscriber.getNumberOfCellIdChangedEventsReceived());
            assertEquals(0, testSubscriber.getNumberOfPositionChangedEventsReceived());
            assertEquals(null, testSubscriber.currentPosition);
        }
    }

    private Queue<AisMessage> initQueueOfAisMessages(int mmsi) {
        // Starting position in the center of cell 24686212289
        Position startingPosition = Position.create((55.33714285714286 + 55.33624454148472)/2, (11.039401122894573 + 11.040299438552713)/2);
        System.out.println("Starting position: " + startingPosition);
        Cell startingCell = grid.getCell(startingPosition);
        assertEquals(24686212289L, startingCell.getCellId());
        AisPosition aisStartingPosition = new AisPosition(startingPosition);

        // Create initial static and voyage data message
        Queue<AisMessage> messageQueue = new LinkedList<>();
        AisMessage5 message5 = createAisMessage5(mmsi);
        messageQueue.add(message5);

        // Create series of position reports for passing under the bridge (north-going)
        AisMessage3 firstPositionMessage = createAisMessage3(MMSI, aisStartingPosition);
        messageQueue.add(firstPositionMessage);

        Position prevGeoLocation = firstPositionMessage.getPos().getGeoLocation();
        final double step = grid.getResolution();
        for (int i = 0; i < 10; i++) {
            AisMessage3 positionMessage = cloneAisMessage3(mmsi, firstPositionMessage);
            Position nextGeoLocation = Position.create(prevGeoLocation.getLatitude() + step, prevGeoLocation.getLongitude());
            AisPosition nextPosition = new AisPosition(nextGeoLocation);
            positionMessage.setPos(nextPosition);
            System.out.println("Next position: " + nextGeoLocation);
            messageQueue.add(positionMessage);
            prevGeoLocation = positionMessage.getPos().getGeoLocation();
        }
        return messageQueue;
    }

    /**
     * Test that events are fired by the tracker at the correct times, when a
     * when track movement is simulated by PositionReaderSimulator.
     *
     * The test scenario includes that no AIS message of type 5 (with static content
     * is received as the first message). Only position reports are received for the
     * track.
     *
     * Assumes grid size 200m.
     * Track starts in cell id 6169552395L (55 19.161N, 011 02.457E)
     */
    @Test
    public void testEventsFiredForKnownSimulatedPatternWhenOnlyPositionReportsAreReceivedForTheTrack() throws SentenceException, AisMessageException, SixbitException {
        testEventsFiredForKnownSimulatedPattern(false);
    }

    /**
     * Test that events are fired by the tracker at the correct times, when a
     * when track movement is simulated by PositionReaderSimulator.
     *
     * The test scenario includes that an AIS message of type 5 (with static content)
     * is received as the first message.
     *
     * Assumes grid size 200m.
     * Track starts in cell id 6169552395L (55 19.161N, 011 02.457E)
     */
    @Test
    public void testEventsFiredForKnownSimulatedPatternWhenStaticMessageIsReceivedFirst() throws SentenceException, AisMessageException, SixbitException {
        testEventsFiredForKnownSimulatedPattern(true);
    }

    private void testEventsFiredForKnownSimulatedPattern(final boolean initTrackerWithAisMessage5) throws SentenceException, AisMessageException, SixbitException {
        // Setup test data
        final int speedKnots = 20;

        PositionReaderSimulator simulator = new PositionReaderSimulator();
        simulator.setSpeedFixed(speedKnots, SpeedUnit.KNOTS);
        simulator.setTimeSourceFixedSlice(5000); /* Interpolation never kicks in */

        net.maritimecloud.util.geometry.Position southOfGreatBeltBridge = net.maritimecloud.util.geometry.Position.create(55.31889216347121, 11.04098609182333); // Cell 6169552395
        net.maritimecloud.util.geometry.Position northOfGreatBeltBridge = net.maritimecloud.util.geometry.Position.create(55.37667465016155, 11.03926947805367); // Cell ?
        PositionReader reader = simulator.forRoute(southOfGreatBeltBridge, northOfGreatBeltBridge);

        Date timestamp = new Date(5000L);

        // Create or mock dependencies
        final Grid grid = Grid.createSize(200);

        // Create object under test
        final EventEmittingTrackerImpl tracker = new EventEmittingTrackerImpl(grid);

        // Wire up test subscriber
        // (discussion: https://code.google.com/p/guava-libraries/issues/detail?id=875)
        TestSubscriber testSubscriber = new TestSubscriber();
        tracker.registerSubscriber(testSubscriber);

        // Init tracker with static ship info
        if (initTrackerWithAisMessage5) {
            AisMessage5 aisMessage5 = new AisMessage5();
            aisMessage5.setRepeat(0);
            aisMessage5.setUserId(211223120);
            aisMessage5.setImo(1234567);
            aisMessage5.setDest("HORSENS");
            aisMessage5.setDraught(60);
            aisMessage5.setDte(0);
            aisMessage5.setPosType(1);
            aisMessage5.setCallsign("OY1234");
            aisMessage5.setName("NO NAME");
            aisMessage5.setEta(31012013);
            aisMessage5.setDimBow(18);
            aisMessage5.setDimPort(3);
            aisMessage5.setDimStarboard(3);
            aisMessage5.setDimStern(12);
            aisMessage5.setShipType(5);
            tracker.update(timestamp.getTime(), aisMessage5);
        }

        // Prepare position message
        Vdm vdm = new Vdm();
        vdm.parse("!BSVDM,1,1,,A,339L2D0OhC0fW:lO5Sa:>8=J00s1,0*7E");

        AisPositionMessage aisMessage3 = new AisMessage3(vdm);
        aisMessage3.setCog(0);
        aisMessage3.setSog(speedKnots);

        // Run test scenario and assert results
        long[] expectedCellIds = {
                6169552395L, // #0
                6169552395L,
                6169752770L,
                6169752770L,
                6169752770L,
                6169752770L, // #5
                6169953145L,
                6169953145L,
                6169953145L,
                6169953145L,
                6170153520L, // #10
                6170153520L,
                6170153520L,
                6170153520L,
                6170353895L,
                6170353895L, // #15
                6170353895L,
                6170353895L,
                6170554270L,
                6170554270L,
                6170554270L, // #20
                6170754645L,
                6170754645L,
                6170754645L,
                6170754645L,
                6170955020L, // #25
                6170955020L,
                6170955020L,
                6170955020L,
                6171155395L,
                6171155395L, // #30
                6171155395L,
                6171155395L,
                6171355770L,
                6171355770L,
                6171355770L, // #35
                6171355770L,
                6171556145L,
                6171556145L,
                6171556145L,
                6171556145L, // #40
                6171756520L,
                6171756520L,
                6171756520L,
                6171756520L,
                6171956895L, // #45
                6171956895L,
                6171956895L,
                6171956895L,
                6172157270L,
                6172157269L, /* under the bridge */
                6172157269L, /* under the bridge */
                6172157269L, /* under the bridge */
                6172357644L,
                6172357644L,
                6172357644L, // #55
                6172558019L,
                6172558019L,
                6172558019L,
                6172558019L,
                6172758394L, // #60
                6172758394L,
                6172758394L,
                6172758394L,
                6172958769L,
                6172958769L, // #65
                6172958769L,
                6172958769L,
                6173159144L,
                6173159144L, // #70
                6173159144L,
                6173159144L,
                6173359519L,
                6173359519L,
                6173359519L, // #75
                6173359519L,
                6173559894L,
                6173559894L,
                6173559894L,
                6173559894L, // #80
                6173760269L,
                6173760269L,
                6173760269L,
                6173760269L,
                6173960644L,
                6173960644L,
                6173960644L,
                6173960644L,
                6174161019L,
                6174161019L,
                6174161019L,
                6174361394L,
                6174361394L,
                6174361394L,
                6174361394L,
                6174561769L,
                6174561769L,
                6174561769L,
                6174561769L,
                6174762144L,
                6174762144L,
                6174762144L,
                6174762144L,
                6174962519L,
                6174962519L,
                6174962519L,
                6174962519L,
                6175162894L,
                6175162894L,
                6175162894L,
                6175162894L,
                6175363269L,
                6175363269L,
                6175363269L,
                6175363269L,
                6175563644L,
                6175563644L,
                6175563644L,
                6175563644L,
                6175764019L,
                6175764019L,
                6175764019L,
                6175964394L,
                6175964394L,
                6175964394L
        };

        boolean[] expectedCellIdChangedEventFired = {
                true,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                true,
                false,
                false,
                true,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                true,
                false,
                false,
        };

        assertEquals(expectedCellIds.length, expectedCellIdChangedEventFired.length);

        int i = 0;
        dk.dma.enav.model.geometry.Position position;

        do {
            System.out.println("i: " + i);

            net.maritimecloud.util.geometry.PositionTime currentPosition = reader.getCurrentPosition();
            long oldTimestamp = timestamp.getTime();
            long newTimestamp = currentPosition.getTime();
            assertEquals(oldTimestamp + 5000, newTimestamp);

            timestamp = new Date(newTimestamp);
            position = dk.dma.enav.model.geometry.Position.create(currentPosition.getLatitude(), currentPosition.getLongitude());

            AisPosition aisPosition = new AisPosition(position);
            aisMessage3.setPos(aisPosition);

            testSubscriber.resetFiredStatus();
            tracker.update(newTimestamp, aisMessage3);
            System.out.println("---");

            assertTrue(testSubscriber.isPositionChangedEventFired());
            assertEquals(expectedCellIdChangedEventFired[i], testSubscriber.isCellIdChangedEventFired());
            assertEquals(expectedCellIds[i], testSubscriber.getCurrentCellId());

            i++;
        } while (position.getLatitude() < 55.3766);
    }

    /**
     * Test that grid cell change events are not fired by the tracker when a simulated track is moving
     * inside the same cell.
     *
     * Assumes grid size 100m.
     * Track starts in cell id 24686212289 (55°20'13.7"N,11°02'21.8"E) - (55°20'10.5"N,11°02'25.1"E)
     */
    @Test
    public void testGridChangeEventsNotFiredForMovementsInsideSameCell() {
        // Starting position in the center of cell 24686212289
        Position startingPosition = Position.create((55.33714285714286 + 55.33624454148472)/2, (11.039401122894573 + 11.040299438552713)/2);
        System.out.println("Starting position: " + startingPosition);
        Cell startingCell = grid.getCell(startingPosition);
        assertEquals(24686212289L, startingCell.getCellId());
        dk.dma.ais.message.AisPosition aisStartingPosition = new AisPosition(startingPosition);

        // Create initial static and voyage data message
        Queue<AisMessage> messageQueue = new LinkedList<>();
        AisMessage5 message5 = createAisMessage5(MMSI);
        messageQueue.add(message5);

        // Create series of position reports for passing under the bridge (north-going)
        AisMessage3 firstPositionMessage = createAisMessage3(MMSI, aisStartingPosition);
        messageQueue.add(firstPositionMessage);

        Position prevGeoLocation = firstPositionMessage.getPos().getGeoLocation();
        final double step = grid.getResolution() / 25;
        for (int i = 0; i < 10; i++) {
            AisMessage3 positionMessage = cloneAisMessage3(MMSI, firstPositionMessage);
            Position nextGeoLocation = Position.create(prevGeoLocation.getLatitude() + step, prevGeoLocation.getLongitude());
            AisPosition nextPosition = new AisPosition(nextGeoLocation);
            positionMessage.setPos(nextPosition);
            System.out.println("Next position: " + nextGeoLocation);
            messageQueue.add(positionMessage);
            prevGeoLocation = positionMessage.getPos().getGeoLocation();
        }

        // Create object under test
        final EventEmittingTrackerImpl tracker = new EventEmittingTrackerImpl(grid);

        // Wire up test subscriber
        // (discussion: https://code.google.com/p/guava-libraries/issues/detail?id=875)
        TestSubscriber testSubscriber = new TestSubscriber();
        tracker.registerSubscriber(testSubscriber);

        // Play scenario through tracker
        long firstTimestamp = System.currentTimeMillis();
        int timeStep = 0;

        // Run test scenario and assert results
        assertEquals(0, tracker.getNumberOfTracks());

        while (!messageQueue.isEmpty()) {
            AisMessage message = messageQueue.remove();
            Date messageTimestamp = new Date(firstTimestamp + (timeStep++ * 10000)); // 10 secs between msgs
            System.out.println(messageTimestamp + ": " + message);
            tracker.update(messageTimestamp.getTime(), message);
            if (message instanceof IPositionMessage) {
                assertEquals(startingCell.getCellId(), testSubscriber.getCurrentCellId());
            }
        }

        assertEquals(1, testSubscriber.getNumberOfCellIdChangedEventsReceived());
        assertEquals(1, tracker.getNumberOfTracks());
    }

    /**
     * Test that TimeEvents are fired by the tracker at periodic intervals in the data stream.
     */
    @Test
    public void testTimeEventsFired() {
        // Create object under test
        final EventEmittingTrackerImpl tracker = new EventEmittingTrackerImpl(grid);

        // Wire up test subscriber
        // (discussion: https://code.google.com/p/guava-libraries/issues/detail?id=875)
        TestSubscriber testSubscriber = new TestSubscriber();
        tracker.registerSubscriber(testSubscriber);

        // Play events through tracker
        long firstTimestamp = System.currentTimeMillis();
        int timeStep = EventEmittingTrackerImpl.TIME_EVENT_PERIOD_MILLIS / 2 - 1;

        assertEquals(0, testSubscriber.getNumberOfTimeEventsReceived());
        tracker.update(firstTimestamp, new AisMessage24());
        assertEquals(1, testSubscriber.getNumberOfTimeEventsReceived());
        tracker.update(firstTimestamp + 1 * timeStep, new AisMessage24());
        assertEquals(1, testSubscriber.getNumberOfTimeEventsReceived());
        tracker.update(firstTimestamp + 2 * timeStep, new AisMessage24());
        assertEquals(1, testSubscriber.getNumberOfTimeEventsReceived());
        tracker.update(firstTimestamp + 3 * timeStep, new AisMessage24());
        assertEquals(2, testSubscriber.getNumberOfTimeEventsReceived());
        tracker.update(firstTimestamp + 4 * timeStep, new AisMessage24());
        assertEquals(2, testSubscriber.getNumberOfTimeEventsReceived());
        tracker.update(firstTimestamp + 5 * timeStep, new AisMessage24());
        assertEquals(2, testSubscriber.getNumberOfTimeEventsReceived());
        tracker.update(firstTimestamp + 6 * timeStep, new AisMessage24());
        assertEquals(3, testSubscriber.getNumberOfTimeEventsReceived());
    }

    /**
     *  Test that Track.getTimeOfLastUpdate is updated on every update
     */
    @Test
    public void testTrackTimestampIsUpdatedOnUpdates() {
        AisPacket[] packets = new AisPacket[NMEA_TEST_STRINGS.length];
        for (int i = 0; i < NMEA_TEST_STRINGS.length; i++) {
            packets[i] = AisPacket.from(NMEA_TEST_STRINGS[i]);
        }

        // Create object under test
        final EventEmittingTrackerImpl tracker = new EventEmittingTrackerImpl(grid);

        for (AisPacket packet : packets) {
            tracker.update(packet);
            Track track = tracker.tracks.get(219000606);
            assertEquals(packet.getBestTimestamp(), track.getTimeOfLastUpdate());
        }
    }

    @Test
    public void testCalculateInterpolatedPositions() {
        Date now = new Date(System.currentTimeMillis());

        long t1 = now.getTime();
        PositionTime pt1 = PositionTime.create(55, 10, t1);

        long t2 = t1 + 50*1000;
        PositionTime pt2 = PositionTime.create(60, 15, t2);

        Map<Long,Position> interpolatedPositions = EventEmittingTrackerImpl.calculateInterpolatedPositions(pt1, pt2);

        assertEquals(4, interpolatedPositions.size());
        Set<Long> interpolationTimestamps = interpolatedPositions.keySet();

        // Assert timestamps
        assertEquals((Long) (t1 + (long) EventEmittingTrackerImpl.INTERPOLATION_TIME_STEP_MILLIS), interpolationTimestamps.toArray()[0]);
        assertEquals((Long) (t1 + (long) EventEmittingTrackerImpl.INTERPOLATION_TIME_STEP_MILLIS * 2), interpolationTimestamps.toArray()[1]);
        assertEquals((Long) (t1 + (long) EventEmittingTrackerImpl.INTERPOLATION_TIME_STEP_MILLIS*3), interpolationTimestamps.toArray()[2]);
        assertEquals((Long) (t1 + (long) EventEmittingTrackerImpl.INTERPOLATION_TIME_STEP_MILLIS * 4), interpolationTimestamps.toArray()[3]);

        // Assert positions
        assertEquals(Position.create(56, 11), interpolatedPositions.get(interpolationTimestamps.toArray()[0]));
        assertEquals(Position.create(57, 12), interpolatedPositions.get(interpolationTimestamps.toArray()[1]));
        assertEquals(Position.create(58, 13), interpolatedPositions.get(interpolationTimestamps.toArray()[2]));
        assertEquals(Position.create(59, 14), interpolatedPositions.get(interpolationTimestamps.toArray()[3]));
        // assertEquals(p2, interpolatedPositions.get(interpolationTimestamps.toArray()[4]));
    }

    /**
     *  Test that interpolation is not used if less than 10 secs between messages
     */
    @Test
    public void testTrackIsNotInterpolated() {
        testInterpolation(EventEmittingTrackerImpl.TRACK_INTERPOLATION_REQUIRED_MILLIS - 1000, 2 /* initial + second */ );
    }

    /**
     *  Test that interpolation is used if more than 10 secs between messages
     */
    @Test
    public void testTrackIsInterpolated() {
        testInterpolation(EventEmittingTrackerImpl.TRACK_INTERPOLATION_REQUIRED_MILLIS + 1000, 5 /* initial + second + 3 interpolated */);
    }

    @Test
    public void testAisPacketsAreStored() {
        AisPacket[] packets = new AisPacket[NMEA_TEST_STRINGS.length];
        for (int i = 0; i < NMEA_TEST_STRINGS.length; i++) {
            packets[i] = AisPacket.from(NMEA_TEST_STRINGS[i]);
        }

        final EventEmittingTrackerImpl tracker = new EventEmittingTrackerImpl(grid);

        // Wire up test subscriber
        // (discussion: https://code.google.com/p/guava-libraries/issues/detail?id=875)
        TestPositionEventSubscriber testSubscriber = new TestPositionEventSubscriber();
        tracker.registerSubscriber(testSubscriber);

        for (AisPacket packet : packets) {
            tracker.update(packet);
        }

        // Test that exactly one track is created
        assertEquals(1, tracker.tracks.size());
        Track track = tracker.tracks.get(219000606);
        assertNotNull(track);

        // Test tracking reports of this track
        assertEquals(6, testSubscriber.getNumberOfPositionChangedEventsReceived());

        // Test newest tracking report
        TrackingReport newestTrackingReport = track.getNewestTrackingReport();
        assertTrue(newestTrackingReport instanceof AisTrackingReport);
        AisTrackingReport aisTrackingReport = (AisTrackingReport) newestTrackingReport;
        assertEquals(1397136678678L, aisTrackingReport.getTimestamp());
        assertEquals(Position.create(55.039028333333334, 10.016861666666667), aisTrackingReport.getPosition());
        assertEquals(10.8, aisTrackingReport.getSpeedOverGround(), 1e-6);
        assertEquals(201.0, aisTrackingReport.getCourseOverGround(), 1e-6);

        // Test all raw AisPacket are stored with the track
        List<TrackingReport> trackingReports = track.getTrackingReports();
        assertEquals(6, trackingReports.size());
        trackingReports.forEach( r -> assertTrue(r instanceof AisTrackingReport));
        assertEquals(NMEA_TEST_STRINGS[0], ((AisTrackingReport) trackingReports.get(0)).getPacket().getStringMessage());
        assertEquals(NMEA_TEST_STRINGS[2], ((AisTrackingReport) trackingReports.get(1)).getPacket().getStringMessage());
        assertEquals(NMEA_TEST_STRINGS[3], ((AisTrackingReport) trackingReports.get(2)).getPacket().getStringMessage());
        assertEquals(NMEA_TEST_STRINGS[4], ((AisTrackingReport) trackingReports.get(3)).getPacket().getStringMessage());
        assertEquals(NMEA_TEST_STRINGS[5], ((AisTrackingReport) trackingReports.get(4)).getPacket().getStringMessage());
        assertEquals(NMEA_TEST_STRINGS[6], ((AisTrackingReport) trackingReports.get(5)).getPacket().getStringMessage());

        // Test the static
        assertEquals(NMEA_TEST_STRINGS[1], track.getLastStaticReport().getStringMessage());
    }

    /**
     * Test that all position between two time-distant AIS position messages are marked as interpolated.
     */
    @Test
    public void testTrackingPositionsAreMarkedAsInterpolated() throws SentenceException, AisMessageException, SixbitException {
        AisPacket[] packets = new AisPacket[NMEA_TEST_STRINGS.length];
        for (int i = 0; i < NMEA_TEST_STRINGS.length; i++) {
            packets[i] = AisPacket.from(NMEA_TEST_STRINGS[i]);
        }

        final int n1 = 0;
        final int n2 = 6;

        assertTrue(packets[n2].getBestTimestamp() - packets[n1].getBestTimestamp() > EventEmittingTrackerImpl.TRACK_INTERPOLATION_REQUIRED_MILLIS);

        // Create or mock dependencies
        final Grid grid = Grid.createSize(200);
        final EventEmittingTrackerImpl tracker = new EventEmittingTrackerImpl(grid);

        // Wire up test subscriber
        // (discussion: https://code.google.com/p/guava-libraries/issues/detail?id=875)
        TestPositionEventSubscriber testSubscriber = new TestPositionEventSubscriber();
        tracker.registerSubscriber(testSubscriber);
        testSubscriber.resetFiredStatus();

        // Insert first position update
        tracker.update(packets[n1]);
        assertTrue(testSubscriber.isPositionChangedEventFired());
        assertEquals(1, testSubscriber.getNumberOfPositionChangedEventsReceived());

        // Insert second position update
        tracker.update(packets[n2]);
        assertTrue(testSubscriber.isPositionChangedEventFired());
        assertEquals(7, testSubscriber.getNumberOfPositionChangedEventsReceived());

        // Assert results
        List<Boolean> positionTypes = testSubscriber.getPositionChangedEvents();

        Boolean[] pos = positionTypes.toArray(new Boolean[0]);
        assertEquals(false, pos[0]);
        assertEquals(true,  pos[1]);
        assertEquals(true,  pos[2]);
        assertEquals(true,  pos[3]);
        assertEquals(true,  pos[4]);
        assertEquals(true,  pos[5]);
        assertEquals(false, pos[6]);
    }

    /**
     *  Test that interpolation is used between 2 position messages 50 seconds apart - even though there
     *  is a static message half way between
     */
    @Test
    public void testTrackIsInterpolatedEvenThoughStaticMessageIsBetweenToPositionUpdates() {
        // Create object under test
        final EventEmittingTrackerImpl tracker = new EventEmittingTrackerImpl(grid);

        // Wire up test subscriber
        // (discussion: https://code.google.com/p/guava-libraries/issues/detail?id=875)
        TestSubscriber testSubscriber = new TestSubscriber();
        tracker.registerSubscriber(testSubscriber);

        // Scenario
        AisPosition p1 = new AisPosition(Position.create(55, 10));
        AisPosition p3 = new AisPosition(Position.create(56, 11));

        long currentTimeMillis = System.currentTimeMillis();
        Date t1 = new Date(currentTimeMillis);
        Date t2 = new Date(currentTimeMillis + (EventEmittingTrackerImpl.TRACK_INTERPOLATION_REQUIRED_MILLIS-1000)*1);
        Date t3 = new Date(currentTimeMillis + (EventEmittingTrackerImpl.TRACK_INTERPOLATION_REQUIRED_MILLIS-1000)*2);

        AisMessage messageSeq1 = createAisMessage3(MMSI, p1);
        AisMessage messageSeq2 = createAisMessage5(MMSI);
        AisMessage messageSeq3 = createAisMessage3(MMSI, p3);

        tracker.update(t1.getTime(), messageSeq1);
        tracker.update(t2.getTime(), messageSeq2);
        tracker.update(t3.getTime(), messageSeq3);

        // Assert result
        int expectedNumberOfPositionChangeEvents = 2 /* p1, p3 */+ /* interpolated */(int) ((t3.getTime() - t1.getTime())/ EventEmittingTrackerImpl.INTERPOLATION_TIME_STEP_MILLIS);
        assertEquals(expectedNumberOfPositionChangeEvents, testSubscriber.getNumberOfPositionChangedEventsReceived());
        assertEquals(Position.create(56, 11), testSubscriber.getCurrentPosition());
    }

    @Test
    public void testTrackIsStale() {
        assertFalse(EventEmittingTrackerImpl.isTrackStale(0, 0, EventEmittingTrackerImpl.TRACK_STALE_MILLIS - 1));
        assertFalse(EventEmittingTrackerImpl.isTrackStale(0, 0, EventEmittingTrackerImpl.TRACK_STALE_MILLIS + 1));

        long now = new Date(System.currentTimeMillis()).getTime();

        assertFalse(EventEmittingTrackerImpl.isTrackStale(now, now, now + EventEmittingTrackerImpl.TRACK_STALE_MILLIS - 1));
        assertTrue(EventEmittingTrackerImpl.isTrackStale(now, now, now + EventEmittingTrackerImpl.TRACK_STALE_MILLIS + 1));

        assertFalse(EventEmittingTrackerImpl.isTrackStale(1, now, now + EventEmittingTrackerImpl.TRACK_STALE_MILLIS - 1));
        assertTrue(EventEmittingTrackerImpl.isTrackStale(1, now, now + EventEmittingTrackerImpl.TRACK_STALE_MILLIS + 1));

        assertFalse(EventEmittingTrackerImpl.isTrackStale(now, 1, now + EventEmittingTrackerImpl.TRACK_STALE_MILLIS - 1));
        assertTrue(EventEmittingTrackerImpl.isTrackStale(now, 1, now + EventEmittingTrackerImpl.TRACK_STALE_MILLIS + 1));
    }

    @Test
    public void testCanProcessStaleTracks() {
        // Create object under test
        final EventEmittingTrackerImpl tracker = new EventEmittingTrackerImpl(grid);

        // Prepare test data
        Position startingPosition = Position.create((55.33714285714286 + 55.33624454148472)/2, (11.039401122894573 + 11.040299438552713)/2);
        dk.dma.ais.message.AisPosition aisPosition = new AisPosition(startingPosition);
        AisMessage3 positionMessage = createAisMessage3(MMSI, aisPosition);

        Date t1 = new Date(System.currentTimeMillis());
        Date t2 = new Date(t1.getTime() + EventEmittingTrackerImpl.TRACK_STALE_MILLIS + 60000);

        // Execute test where track goes stale
        tracker.update(t1.getTime(), positionMessage);
        tracker.update(t2.getTime(), positionMessage);

        // No exceptions are expected
    }

    private void testInterpolation(long millisBetweenMessages, int expectedNumberOfPositionChangeEvents) {
        // Create object under test
        final EventEmittingTrackerImpl tracker = new EventEmittingTrackerImpl(grid);

        // Prepare test data
        Position startingPosition = Position.create((55.33714285714286 + 55.33624454148472)/2, (11.039401122894573 + 11.040299438552713)/2);
        dk.dma.ais.message.AisPosition aisStartingPosition = new AisPosition(startingPosition);
        AisMessage3 firstPositionMessage = createAisMessage3(MMSI, aisStartingPosition);

        Position secondPosition = Position.create((55.33714285714286 + 55.35624454148472)/2, (11.038401122894573 + 10.890299438552713)/2);
        dk.dma.ais.message.AisPosition aisSecondPosition = new AisPosition(secondPosition);
        AisMessage3 secondPositionMessage = createAisMessage3(MMSI, aisSecondPosition);

        Date firstTimestamp = new Date(System.currentTimeMillis());
        Date secondTimestamp = new Date(System.currentTimeMillis() + millisBetweenMessages);

        System.out.println("Starting at " + firstTimestamp.getTime() + " in " + startingPosition);
        System.out.println("Ending at " + secondTimestamp.getTime() + " in " + secondPosition);

        // Wire up test subscriber
        // (discussion: https://code.google.com/p/guava-libraries/issues/detail?id=875)
        TestSubscriber testSubscriber = new TestSubscriber();
        tracker.registerSubscriber(testSubscriber);

        // Execute test
        tracker.update(firstTimestamp.getTime(), firstPositionMessage);
        tracker.update(secondTimestamp.getTime(), secondPositionMessage);

        // Assert result
        assertEquals(expectedNumberOfPositionChangeEvents, testSubscriber.getNumberOfPositionChangedEventsReceived());

        assertEquals(secondPosition.getLatitude(), testSubscriber.getCurrentPosition().getLatitude(), 1e-6);
        assertEquals(secondPosition.getLongitude(), testSubscriber.getCurrentPosition().getLongitude(), 1e-6);
    }

    private static AisMessage3 cloneAisMessage3(int mmsi, AisMessage3 msgToClone) {
        AisMessage3 message = new AisMessage3();
        message.setUserId(mmsi);
        message.setCog(msgToClone.getCog());
        message.setNavStatus(msgToClone.getNavStatus());
        message.setRot(msgToClone.getRot());
        message.setSog(msgToClone.getSog());
        return message;
    }

    private static AisMessage3 createAisMessage3(int mmsi, AisPosition aisStartingPosition) {
        AisMessage3 positionMessage = new AisMessage3();
        positionMessage.setUserId(mmsi);
        positionMessage.setPos(aisStartingPosition);
        positionMessage.setCog(1);
        positionMessage.setNavStatus(0);
        positionMessage.setRot(0);
        positionMessage.setSog(10);
        return positionMessage;
    }

    private static AisMessage5 createAisMessage5(int mmsi) {
        AisMessage5 message5 = new AisMessage5();
        message5.setUserId(mmsi);
        message5.setDest("SKAGEN");
        message5.setCallsign("OY1234");
        message5.setImo(1234567);
        return message5;
    }

    public class TestSubscriber {

        private long currentCellId;
        private Position currentPosition;

        private int numberOfTimeEventsReceived;
        private int numberOfCellIdChangedEventsReceived;
        private int numberOfPositionChangedEventsReceived;

        private boolean timeEventFired;
        private boolean cellIdChangedEventFired;
        private boolean positionChangedEventFired;

        public void resetFiredStatus() {
            cellIdChangedEventFired = false;
            positionChangedEventFired = false;
        }

        @Subscribe
        public void onTimeMark(TimeEvent event) {
            System.out.println("TimeEvent");
            timeEventFired = true;
            numberOfTimeEventsReceived++;
        }

        @Subscribe
        public void onCellIdChanged(CellChangedEvent event) {
            System.out.println("CellChangedEvent");
            cellIdChangedEventFired = true;

            numberOfCellIdChangedEventsReceived++;
            currentCellId = (long) event.getTrack().getProperty(Track.CELL_ID);
            System.out.println("We are now in cell: " + currentCellId);
            assertTrackTimestamps(event.getTrack());
        }

        @Subscribe
        public void onPositionChanged(PositionChangedEvent event) {
            System.out.println("PositionChangedEvent");
            positionChangedEventFired = true;

            numberOfPositionChangedEventsReceived++;
            currentPosition = event.getTrack().getNewestTrackingReport().getPosition();
            System.out.println("We are now in position: " + currentPosition);
            assertTrackTimestamps(event.getTrack());
        }

        private void assertTrackTimestamps(Track track) {
            // TIMESTAMP_ANY_UPDATE is never allowed to fall behind TIMESTAMP_POSITION_UPDATE
            long anyUpdate = track.getTimeOfLastUpdate();
            long posUpdate = track.getNewestTrackingReport().getTimestamp();
            assertTrue(anyUpdate >= posUpdate);
        }

        private long getTimestampFromTrack(Track track, String timestampType) {
            Long timestamp = (Long) track.getProperty(timestampType);
            if (timestamp == null) {
                timestamp = 0L;
            }
            return timestamp;
        }

        public long getCurrentCellId() {
            return currentCellId;
        }

        public Position getCurrentPosition() {
            return currentPosition;
        }

        public int getNumberOfCellIdChangedEventsReceived() {
            return numberOfCellIdChangedEventsReceived;
        }

        public int getNumberOfTimeEventsReceived() {
            return numberOfTimeEventsReceived;
        }

        public int getNumberOfPositionChangedEventsReceived() {
            return numberOfPositionChangedEventsReceived;
        }

        public boolean isCellIdChangedEventFired() {
            return cellIdChangedEventFired;
        }

        public boolean isPositionChangedEventFired() {
            return positionChangedEventFired;
        }
    }

    public class TestPositionEventSubscriber {
        private List<Boolean> positionIsInterpolated = new LinkedList<>();

        public void resetFiredStatus() {
            positionIsInterpolated = new LinkedList<>();
        }

        @Subscribe
        public void onPositionChanged(PositionChangedEvent event) {
            System.out.println("PositionChangedEvent");
            positionIsInterpolated.add(new Boolean(event.getTrack().getNewestTrackingReport() instanceof InterpolatedTrackingReport));
        }

        public int getNumberOfPositionChangedEventsReceived() {
            return positionIsInterpolated.size();
        }

        public boolean isPositionChangedEventFired() {
            return positionIsInterpolated.size() > 0;
        }

        public List<Boolean> getPositionChangedEvents() {
            return positionIsInterpolated;
        }
    }

}
