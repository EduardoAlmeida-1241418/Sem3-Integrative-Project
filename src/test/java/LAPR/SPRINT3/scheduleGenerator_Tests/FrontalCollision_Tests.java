package LAPR.SPRINT3.scheduleGenerator_Tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipp.isep.dei.data.memory.*;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.schedule.GeneralSchedule;
import pt.ipp.isep.dei.domain.schedule.ScheduleEventType;
import pt.ipp.isep.dei.domain.schedule.ScheduleGenerator;
import pt.ipp.isep.dei.domain.trackRelated.*;
import pt.ipp.isep.dei.domain.trainRelated.Train;
import pt.ipp.isep.dei.domain.transportationRelated.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FrontalCollision_Tests {

    private static final Logger log = LoggerFactory.getLogger(FrontalCollision_Tests.class);
    private Facility A, B;

    @BeforeEach
    void setUp() {

        // ===== CLEAR STORES =====
        new FacilityStoreInMemory().clear();
        new RailwayLineStoreInMemory().clear();
        new RailwayLineSegmentStoreInMemory().clear();
        new RouteStoreInMemory().clear();
        new TrainStoreInMemory().clear();
        new GeneralScheduleStoreInMemory().clear();

        // ===== FACILITIES =====
        FacilityStoreInMemory facilityStore = new FacilityStoreInMemory();
        A = new Facility(1, "A");
        B = new Facility(2, "B");

        facilityStore.save(null, A);
        facilityStore.save(null, B);

    }

    // Colisao em single line com siding
    @Test
    void detectFrontalCollisionDoubleSingleTrack() {
        // ===== SINGLE TRACK SEGMENT =====
        RailwayLineSegment segmentAB =
                new RailwayLineSegment(1, false, 10_000, 5_000, 1, 80, 1);

        RailwayLineSegment segmentAB2 =
                new RailwayLineSegment(2, false, 10_000, 3_000, 1, 80, 1);


        new RailwayLineSegmentStoreInMemory().save(null, segmentAB);
        new RailwayLineSegmentStoreInMemory().save(null, segmentAB2);

        // ===== SINGLE LINE A-B =====
        RailwayLine lineAB = new RailwayLine(1, "A-B", 1, 2, "PT");

        lineAB.addSegment(segmentAB, 1);
        lineAB.addSegment(segmentAB2, 2);

        new RailwayLineStoreInMemory().save(null, lineAB);
        segmentAB.addRailwayLine(lineAB);
        segmentAB2.addRailwayLine(lineAB);


        // ===== FREIGHTS =====
        Freight freightAB =
                new Freight(1, List.of(), A, B, new Date(25, 12, 2025));

        Freight freightBA =
                new Freight(2, List.of(), B, A, new Date(25, 12, 2025));

        // ===== ROUTES =====
        Path pathAB = new Path(List.of(A, B));
        Path pathBA = new Path(List.of(B, A));

        Route routeAB = new Route(List.of(freightAB), pathAB, 1);
        Route routeBA = new Route(List.of(freightBA), pathBA, 2);

        // ===== OPERATOR =====
        Operator operator = new Operator("OP1", "JOAO", "JOTA");

        // ===== LOCOMOTIVE MODEL =====
        LocomotiveModel model = new LocomotiveModel(
                1,
                "TEST_MODEL",
                1500,
                20_000,
                1.0,
                8,
                120,
                80.0,
                2,
                null,
                null,
                null,
                List.of(),
                0,
                0,
                0
        );

        Locomotive locomotive =
                new Locomotive(1, "LOCO_TEST", 2020, model, operator);

        // ===== TRAINS (MESMA HORA) =====
        DateTime dt =
                new DateTime(new Date(25, 12, 2025), new Time(10, 0));

        TrainStoreInMemory trainStore = new TrainStoreInMemory();

        Train t1 = new Train(operator, dt, routeAB, List.of(locomotive));
        trainStore.save(null, t1);

        Train t2 = new Train(operator, dt, routeBA, List.of(locomotive));
        trainStore.save(null, t2);

        // ===== GENERATE SCHEDULE =====
        ScheduleGenerator generator =
                new ScheduleGenerator(List.of(t1, t2));

        GeneralSchedule schedule =
                generator.generateNewSchedule();

        // ===== PRINT SCHEDULE (USANDO toString) =====
        System.out.println("\n========================== SCHEDULE RESULT ==========================");

        schedule.getScheduleForTrainList().forEach(trainSchedule -> {
            System.out.println(trainSchedule.toString());

            trainSchedule.getScheduleEvents().forEach(event ->
                    System.out.println(event.toString() + "\n")
            );
        });

        System.out.println("=====================================================================\n");

        // ===== ASSERT =====
        boolean hasWaiting =
                schedule.getAllEvents().stream()
                        .anyMatch(e ->
                                e.getScheduleEventType() == ScheduleEventType.WAITING ||
                                        e.getScheduleEventType() == ScheduleEventType.WAITING_IN_STATION ||
                                        e.getScheduleEventType() == ScheduleEventType.WAITING_FOR_ASSEMBLE
                        );

        assertTrue(
                hasWaiting,
                "Dois comboios no mesmo single-track A-B, à mesma hora, devem causar WAITING"
        );
    }

    // Colisao em single line sem siding
    @Test
    void detectFrontalCollisionSingleTrack() {
        // ===== SINGLE TRACK SEGMENT =====
        RailwayLineSegment segmentAB =
                new RailwayLineSegment(1, false, 10_000, 5_000, 1, 80, 1);

        new RailwayLineSegmentStoreInMemory().save(null, segmentAB);

        // ===== SINGLE LINE A-B =====
        RailwayLine lineAB = new RailwayLine(1, "A-B", 1, 2, "PT");

        lineAB.addSegment(segmentAB, 1);

        new RailwayLineStoreInMemory().save(null, lineAB);
        segmentAB.addRailwayLine(lineAB);


        // ===== FREIGHTS =====
        Freight freightAB =
                new Freight(1, List.of(), A, B, new Date(25, 12, 2025));

        Freight freightBA =
                new Freight(2, List.of(), B, A, new Date(25, 12, 2025));

        // ===== ROUTES =====
        Path pathAB = new Path(List.of(A, B));
        Path pathBA = new Path(List.of(B, A));

        Route routeAB = new Route(List.of(freightAB), pathAB, 1);
        Route routeBA = new Route(List.of(freightBA), pathBA, 2);

        // ===== OPERATOR =====
        Operator operator = new Operator("OP1", "JOAO", "JOTA");

        // ===== LOCOMOTIVE MODEL =====
        LocomotiveModel model = new LocomotiveModel(
                1,
                "TEST_MODEL",
                1500,
                20_000,
                1.0,
                8,
                120,
                80.0,
                2,
                null,
                null,
                null,
                List.of(),
                0,
                0,
                0
        );

        Locomotive locomotive =
                new Locomotive(1, "LOCO_TEST", 2020, model, operator);

        // ===== TRAINS (MESMA HORA) =====
        DateTime dt =
                new DateTime(new Date(25, 12, 2025), new Time(10, 0));

        TrainStoreInMemory trainStore = new TrainStoreInMemory();

        Train t1 = new Train(operator, dt, routeAB, List.of(locomotive));
        trainStore.save(null, t1);

        Train t2 = new Train(operator, dt.plusSeconds(1), routeBA, List.of(locomotive));
        trainStore.save(null, t2);

        // ===== GENERATE SCHEDULE =====
        ScheduleGenerator generator =
                new ScheduleGenerator(List.of(t1, t2));

        GeneralSchedule schedule =
                generator.generateNewSchedule();

        // ===== PRINT SCHEDULE (USANDO toString) =====
        System.out.println("\n================= SCHEDULE RESULT =================");

        schedule.getScheduleForTrainList().forEach(trainSchedule -> {
            System.out.println(trainSchedule.toString());

            trainSchedule.getScheduleEvents().forEach(event ->
                    System.out.println(event.toString() + "\n")
            );
        });

        System.out.println("===================================================\n");

        // ===== ASSERT =====
        boolean hasWaiting =
                schedule.getAllEvents().stream()
                        .anyMatch(e ->
                                e.getScheduleEventType() == ScheduleEventType.WAITING ||
                                        e.getScheduleEventType() == ScheduleEventType.WAITING_IN_STATION ||
                                        e.getScheduleEventType() == ScheduleEventType.WAITING_FOR_ASSEMBLE
                        );

        assertTrue(
                hasWaiting,
                "Dois comboios no mesmo single-track A-B, à mesma hora, devem causar WAITING"
        );
    }

    // Colisao em single line com siding
    @Test
    void shouldDetectFrontalCollisionOnSingleTrackAB() {
        // ===== SINGLE TRACK SEGMENT =====
        RailwayLineSegment segmentAB =
                new RailwayLineSegment(1, false, 10_000, 5_000, 1, 80, 1);

        Siding sidingAB = new Siding(1, 1000, 100);
        segmentAB.setSiding(sidingAB);

        new RailwayLineSegmentStoreInMemory().save(null, segmentAB);

        // ===== SINGLE LINE A-B =====
        RailwayLine lineAB = new RailwayLine(1, "A-B", 1, 2, "PT");
        lineAB.addSegment(segmentAB, 1);

        new RailwayLineStoreInMemory().save(null, lineAB);
        segmentAB.addRailwayLine(lineAB);



        // ===== FREIGHTS =====
        Freight freightAB =
                new Freight(1, List.of(), A, B, new Date(25, 12, 2025));

        Freight freightBA =
                new Freight(2, List.of(), B, A, new Date(25, 12, 2025));

        // ===== ROUTES =====
        Path pathAB = new Path(List.of(A, B));
        Path pathBA = new Path(List.of(B, A));

        Route routeAB = new Route(List.of(freightAB), pathAB, 1);
        Route routeBA = new Route(List.of(freightBA), pathBA, 2);

        // ===== OPERATOR =====
        Operator operator = new Operator("OP1", "JOAO", "JOTA");

        // ===== LOCOMOTIVE MODEL =====
        LocomotiveModel model = new LocomotiveModel(
                1,
                "TEST_MODEL",
                1500,
                20_000,
                1.0,
                8,
                120,
                80.0,
                2,
                null,
                null,
                null,
                List.of(),
                0,
                0,
                0
        );

        Locomotive locomotive =
                new Locomotive(1, "LOCO_TEST", 2020, model, operator);

        // ===== TRAINS (MESMA HORA) =====
        DateTime dt =
                new DateTime(new Date(25, 12, 2025), new Time(10, 0));

        TrainStoreInMemory trainStore = new TrainStoreInMemory();

        Train t1 = new Train(operator, dt, routeAB, List.of(locomotive));
        trainStore.save(null, t1);

        Train t2 = new Train(operator, dt, routeBA, List.of(locomotive));
        trainStore.save(null, t2);

        // ===== GENERATE SCHEDULE =====
        ScheduleGenerator generator =
                new ScheduleGenerator(List.of(t1, t2));

        GeneralSchedule schedule =
                generator.generateNewSchedule();

        // ===== PRINT SCHEDULE (USANDO toString) =====
        System.out.println("\n================= SCHEDULE RESULT =================");

        schedule.getScheduleForTrainList().forEach(trainSchedule -> {
            System.out.println(trainSchedule.toString());

            trainSchedule.getScheduleEvents().forEach(event ->
                    System.out.println(event.toString() + "\n")
            );
        });

        System.out.println("===================================================\n");

        // ===== ASSERT =====
        boolean hasWaiting =
                schedule.getAllEvents().stream()
                        .anyMatch(e ->
                                e.getScheduleEventType() == ScheduleEventType.WAITING ||
                                        e.getScheduleEventType() == ScheduleEventType.WAITING_IN_STATION ||
                                        e.getScheduleEventType() == ScheduleEventType.WAITING_FOR_ASSEMBLE
                        );

        assertTrue(
                hasWaiting,
                "Dois comboios no mesmo single-track A-B, à mesma hora, devem causar WAITING"
        );
    }

    // Sem colisões - Linha dupla
    @Test
    void dontDetectFrontalCollisionDoubleTrack() {
        // ===== SINGLE TRACK SEGMENT =====
        RailwayLineSegment segmentAB =
                new RailwayLineSegment(1, false, 10_000, 5_000, 2, 80, 1);

        new RailwayLineSegmentStoreInMemory().save(null, segmentAB);

        // ===== SINGLE LINE A-B =====
        RailwayLine lineAB = new RailwayLine(1, "A-B", 1, 2, "PT");

        lineAB.addSegment(segmentAB, 1);

        new RailwayLineStoreInMemory().save(null, lineAB);
        segmentAB.addRailwayLine(lineAB);


        // ===== FREIGHTS =====
        Freight freightAB =
                new Freight(1, List.of(), A, B, new Date(25, 12, 2025));

        Freight freightBA =
                new Freight(2, List.of(), B, A, new Date(25, 12, 2025));

        // ===== ROUTES =====
        Path pathAB = new Path(List.of(A, B));
        Path pathBA = new Path(List.of(B, A));

        Route routeAB = new Route(List.of(freightAB), pathAB, 1);
        Route routeBA = new Route(List.of(freightBA), pathBA, 2);

        // ===== OPERATOR =====
        Operator operator = new Operator("OP1", "JOAO", "JOTA");

        // ===== LOCOMOTIVE MODEL =====
        LocomotiveModel model = new LocomotiveModel(
                1,
                "TEST_MODEL",
                1500,
                20_000,
                1.0,
                8,
                120,
                80.0,
                2,
                null,
                null,
                null,
                List.of(),
                0,
                0,
                0
        );

        Locomotive locomotive =
                new Locomotive(1, "LOCO_TEST", 2020, model, operator);

        // ===== TRAINS (MESMA HORA) =====
        DateTime dt =
                new DateTime(new Date(25, 12, 2025), new Time(10, 0));

        TrainStoreInMemory trainStore = new TrainStoreInMemory();

        Train t1 = new Train(operator, dt, routeAB, List.of(locomotive));
        trainStore.save(null, t1);

        Train t2 = new Train(operator, dt, routeBA, List.of(locomotive));
        trainStore.save(null, t2);

        // ===== GENERATE SCHEDULE =====
        ScheduleGenerator generator =
                new ScheduleGenerator(List.of(t1, t2));

        GeneralSchedule schedule =
                generator.generateNewSchedule();

        // ===== PRINT SCHEDULE (USANDO toString) =====
        System.out.println("\n================= SCHEDULE RESULT =================");

        schedule.getScheduleForTrainList().forEach(trainSchedule -> {
            System.out.println(trainSchedule.toString());

            trainSchedule.getScheduleEvents().forEach(event ->
                    System.out.println(event.toString() + "\n")
            );
        });

        System.out.println("===================================================\n");

        // ===== ASSERT =====
        boolean hasWaiting = !
                schedule.getAllEvents().stream()
                        .anyMatch(e ->
                                e.getScheduleEventType() == ScheduleEventType.WAITING ||
                                        e.getScheduleEventType() == ScheduleEventType.WAITING_IN_STATION ||
                                        e.getScheduleEventType() == ScheduleEventType.WAITING_FOR_ASSEMBLE
                        );

        assertTrue(
                hasWaiting,
                "Dois comboios no mesmo single-track A-B, à mesma hora, não devem causar WAITING"
        );
    }

    // Single line with 2 trains in the same direction - No Collision
    @Test
    void dontDetectFrontalCollisionSameDirection() {
        // ===== SINGLE TRACK SEGMENT =====
        RailwayLineSegment segmentAB =
                new RailwayLineSegment(1, false, 10_000, 5_000, 1, 80, 1);

        new RailwayLineSegmentStoreInMemory().save(null, segmentAB);

        // ===== SINGLE LINE A-B =====
        RailwayLine lineAB = new RailwayLine(1, "A-B", 1, 2, "PT");

        lineAB.addSegment(segmentAB, 1);

        new RailwayLineStoreInMemory().save(null, lineAB);
        segmentAB.addRailwayLine(lineAB);


        // ===== FREIGHTS =====
        Freight freightAB =
                new Freight(1, List.of(), A, B, new Date(25, 12, 2025));

        Freight freightBA =
                new Freight(2, List.of(), A, B, new Date(25, 12, 2025));

        // ===== ROUTES =====
        Path pathAB = new Path(List.of(A, B));
        Path pathBA = new Path(List.of(A, B));

        Route routeAB = new Route(List.of(freightAB), pathAB, 1);
        Route routeBA = new Route(List.of(freightBA), pathBA, 2);

        // ===== OPERATOR =====
        Operator operator = new Operator("OP1", "JOAO", "JOTA");

        // ===== LOCOMOTIVE MODEL =====
        LocomotiveModel model = new LocomotiveModel(
                1,
                "TEST_MODEL",
                1500,
                20_000,
                1.0,
                8,
                120,
                80.0,
                2,
                null,
                null,
                null,
                List.of(),
                0,
                0,
                0
        );

        Locomotive locomotive =
                new Locomotive(1, "LOCO_TEST", 2020, model, operator);

        // ===== TRAINS (MESMA HORA) =====
        DateTime dt =
                new DateTime(new Date(25, 12, 2025), new Time(10, 0));

        TrainStoreInMemory trainStore = new TrainStoreInMemory();

        Train t1 = new Train(operator, dt, routeAB, List.of(locomotive));
        trainStore.save(null, t1);

        Train t2 = new Train(operator, dt, routeBA, List.of(locomotive));
        trainStore.save(null, t2);

        // ===== GENERATE SCHEDULE =====
        ScheduleGenerator generator =
                new ScheduleGenerator(List.of(t1, t2));

        GeneralSchedule schedule =
                generator.generateNewSchedule();

        // ===== PRINT SCHEDULE (USANDO toString) =====
        System.out.println("\n================= SCHEDULE RESULT =================");

        schedule.getScheduleForTrainList().forEach(trainSchedule -> {
            System.out.println(trainSchedule.toString());

            trainSchedule.getScheduleEvents().forEach(event ->
                    System.out.println(event.toString() + "\n")
            );
        });

        System.out.println("===================================================\n");

        // ===== ASSERT =====
        boolean hasWaiting = !
                schedule.getAllEvents().stream()
                        .anyMatch(e ->
                                e.getScheduleEventType() == ScheduleEventType.WAITING ||
                                        e.getScheduleEventType() == ScheduleEventType.WAITING_IN_STATION ||
                                        e.getScheduleEventType() == ScheduleEventType.WAITING_FOR_ASSEMBLE
                        );

        assertTrue(
                hasWaiting,
                "Dois comboios no mesmo single-track A-B, à mesma hora, não devem causar WAITING"
        );
    }

}
