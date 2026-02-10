package LAPR.SPRINT3.scheduleGenerator_Tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.data.memory.GeneralScheduleStoreInMemory;
import pt.ipp.isep.dei.data.memory.TrainStoreInMemory;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.DateTime;
import pt.ipp.isep.dei.domain.Facility;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.TimeInterval;
import pt.ipp.isep.dei.domain.schedule.*;
import pt.ipp.isep.dei.domain.trainRelated.Train;

import java.util.ArrayList;
import java.util.List;


class CreationOfSchedule_Tests {

    private GeneralScheduleStoreInMemory scheduleStoreInMemory;

    @BeforeEach
    void setUp() {
        scheduleStoreInMemory = new GeneralScheduleStoreInMemory();
        scheduleStoreInMemory.clear();
    }

    @Test
    void shouldCopyOnlyDispatchedTrainEvents() {

        // ---------- Arrange ----------
        DateTime trainDateTime = new DateTime(
                new Date(1, 1, 2024),
                new Time(9, 0)
        );

        Train dispatchedTrain = new Train(null, trainDateTime, null, List.of());
        dispatchedTrain.setDispatched(true);

        Train notDispatchedTrain = new Train(null, trainDateTime, null, List.of());
        notDispatchedTrain.setDispatched(false);

        Facility facility = new Facility(1, "Porto");

        ScheduleEvent dispatchedEvent = new ScheduleEvent(
                new TimeInterval(
                        new DateTime(new Date(1, 1, 2024), new Time(10, 0)),
                        new DateTime(new Date(1, 1, 2024), new Time(11, 0))
                ),
                facility,
                facility,
                dispatchedTrain,
                ScheduleEventType.MOVEMENT
        );

        ScheduleEvent notDispatchedEvent = new ScheduleEvent(
                new TimeInterval(
                        new DateTime(new Date(1, 1, 2024), new Time(12, 0)),
                        new DateTime(new Date(1, 1, 2024), new Time(13, 0))
                ),
                facility,
                facility,
                notDispatchedTrain,
                ScheduleEventType.MOVEMENT
        );

        GeneralSchedule oldSchedule = new GeneralSchedule();
        oldSchedule.addEvent(dispatchedEvent);
        oldSchedule.addEvent(notDispatchedEvent);

        scheduleStoreInMemory.save(null, oldSchedule);

        // ---------- Act ----------
        TrainStoreInMemory trainStoreInMemory = new TrainStoreInMemory();


        ScheduleGenerator scheduleGenerator = new ScheduleGenerator(new ArrayList<>(trainStoreInMemory.findAll()));
        scheduleGenerator.generateNewSchedule();

        GeneralSchedule newSchedule = scheduleStoreInMemory.findLatest();

        System.out.println("\n=== ALL TrainSchedules in newSchedule ===");
        for (TrainSchedule ts : newSchedule.getScheduleForTrainList()) {
            Train train = ts.getTrain();
            System.out.println("Train ID: " + train.getId() +
                    " | Dispatched: " + train.isDispatched() +
                    " | DateTime: " + train.getDateTime() +
                    " | Locomotives: " + (train.getLocomotives() != null ? train.getLocomotives().size() : 0));
            System.out.println("Events:");
            for (ScheduleEvent e : ts.getScheduleEvents()) {
                System.out.println("  - Type: " + e.getScheduleEventType() +
                        " | Start: " + (e.getStartPosition() != null ? e.getStartPosition().name() : "null") +
                        " | End: " + (e.getEndPosition() != null ? e.getEndPosition().name() : "null") +
                        " | Interval: " + e.getTimeInterval() +
                        " | Train Dispatched: " + e.getTrain().isDispatched());
            }
            System.out.println();
        }

        System.out.println("=== Dispatched TrainSchedules ONLY ===");
        List<TrainSchedule> dispatchedSchedules = newSchedule.getScheduleForTrainList()
                .stream()
                .filter(ts -> ts.getTrain().isDispatched())
                .toList();

        for (TrainSchedule ts : dispatchedSchedules) {
            Train train = ts.getTrain();
            System.out.println("Train ID: " + train.getId() +
                    " | Dispatched: " + train.isDispatched() +
                    " | Events count: " + ts.getScheduleEvents().size());
            for (ScheduleEvent e : ts.getScheduleEvents()) {
                System.out.println("  - Type: " + e.getScheduleEventType() +
                        " | Start: " + (e.getStartPosition() != null ? e.getStartPosition().name() : "null") +
                        " | End: " + (e.getEndPosition() != null ? e.getEndPosition().name() : "null") +
                        " | Interval: " + e.getTimeInterval());
            }
            System.out.println();
        }

    }

}
