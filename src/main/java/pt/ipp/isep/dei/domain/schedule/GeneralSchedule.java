package pt.ipp.isep.dei.domain.schedule;

import pt.ipp.isep.dei.data.memory.GeneralScheduleStoreInMemory;
import pt.ipp.isep.dei.domain.DateTime;
import pt.ipp.isep.dei.domain.TimeInterval;
import pt.ipp.isep.dei.domain.Tree.Interval.IntervalTree;
import pt.ipp.isep.dei.domain.trackRelated.TrackLocation;
import pt.ipp.isep.dei.domain.trainRelated.Train;

import java.util.*;

/**
 * GlobalSchedule
 *
 * - TrackLocation -> IntervalTree (ocupação temporal)
 * - Train -> TrainSchedule (timeline do comboio)
 *
 * Permite overlaps.
 * Um único ponto de escrita.
 */
public class GeneralSchedule {

    private TreeMap<TrackLocation, IntervalTree> schedulesByLocation = new TreeMap<>(Comparator.comparing(TrackLocation::name));
    private TreeMap<Train, TrainSchedule> schedulesByTrain = new TreeMap<>();
    private int id;

    public GeneralSchedule() {
        this.id = new GeneralScheduleStoreInMemory().getNextGeneralScheduleId();
        for (GeneralSchedule gs : new GeneralScheduleStoreInMemory().findAll()) {
            if (gs.getId() == this.id) {
                throw new IllegalArgumentException("GeneralSchedule with id " + id + " already exists.");
            }
        }
        new GeneralScheduleStoreInMemory().addGeneralScheduleId(id);
    }

    public GeneralSchedule(int id) {
        this.id = id;
        if (new GeneralScheduleStoreInMemory().existsGeneralScheduleWithId(id)) {
            throw new IllegalArgumentException("GeneralSchedule with id " + id + " already exists.");
        }
        new GeneralScheduleStoreInMemory().addGeneralScheduleId(id);
    }

    /* =====================================================
       Add / Remove
       ===================================================== */

    public void addEvent(ScheduleEvent event) {
        ScheduleEvent clonedEvent = new ScheduleEvent(event);

        Objects.requireNonNull(clonedEvent, "ScheduleEvent cannot be null");

        clonedEvent.setGeneralSchedule(this);

        addToLocation(clonedEvent);
        addToTrain(clonedEvent);
    }

    public void removeEvent(ScheduleEvent event) {
        Objects.requireNonNull(event, "ScheduleEvent cannot be null");

        removeFromLocation(event);
        removeFromTrain(event);
    }

    /* =====================================================
       Queries
       ===================================================== */

    public List<ScheduleEvent> findOverlaps(
            TrackLocation location,
            TimeInterval interval
    ) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(interval);

        IntervalTree tree = schedulesByLocation.get(location);
        if (tree == null)
            return List.of();

        return tree.findAllOverlaps(interval);
    }

    public Collection<ScheduleEvent> getScheduleForTrain(Train train) {
        Objects.requireNonNull(train);

        TrainSchedule ts = schedulesByTrain.get(train);
        if (ts == null)
            return List.of();

        return ts.getScheduleEvents();
    }

    /* =====================================================
       Internal helpers
       ===================================================== */

    private void addToLocation(ScheduleEvent event) {
        TrackLocation location = event.getStartPosition();

        schedulesByLocation
                .computeIfAbsent(location, l -> new IntervalTree())
                .insert(event);
    }

    private void removeFromLocation(ScheduleEvent event) {
        TrackLocation location = event.getStartPosition();

        IntervalTree tree = schedulesByLocation.get(location);
        if (tree == null)
            return;

        tree.remove(event);

        if (tree.isEmpty()) {
            schedulesByLocation.remove(location);
        }
    }

    private void addToTrain(ScheduleEvent event) {
        Train train = event.getTrain();

        schedulesByTrain
                .computeIfAbsent(train, t -> new TrainSchedule(train))
                .addEvent(event);
    }

    private void removeFromTrain(ScheduleEvent event) {
        Train train = event.getTrain();

        TrainSchedule ts = schedulesByTrain.get(train);
        if (ts == null)
            return;

        ts.removeEvent(event);

        if (ts.getScheduleEvents().isEmpty()) {
            schedulesByTrain.remove(train);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Collection<ScheduleEvent> findActiveEventsAt(DateTime dateTime) {
        Objects.requireNonNull(dateTime);

        Set<ScheduleEvent> activeEvents = new LinkedHashSet<>();

        for (IntervalTree tree : schedulesByLocation.values()) {
            for (ScheduleEvent event : tree.getAll()) {
                if (event.getTimeInterval().contains(dateTime)) {
                    activeEvents.add(event);
                }
            }
        }

        return activeEvents;
    }

    public List<TrainSchedule> getScheduleForTrainList(){
        return new ArrayList<>(schedulesByTrain.values());
    }

    public List<TrainSchedule> getAllTrainSchedules() {
        return new ArrayList<>(schedulesByTrain.values());
    }

    public List<ScheduleEvent> getAllEvents() {

        List<ScheduleEvent> events = new ArrayList<>();

        for (TrainSchedule ts : schedulesByTrain.values()) {
            for (ScheduleEvent event : ts.getScheduleEvents()) {
                events.add(event);
            }
        }
        return events;
    }

    public List<ScheduleEvent> getEventsUpToMostRecentFirst(DateTime dateTime) {
        Objects.requireNonNull(dateTime);

        List<ScheduleEvent> result = new ArrayList<>();

        for (TrainSchedule ts : schedulesByTrain.values()) {
            for (ScheduleEvent event : ts.getScheduleEvents()) {

                if (!event.getStartDateTime().isAfter(dateTime)) {
                    result.add(event);
                }
            }
        }

        result.sort(Comparator.comparing(ScheduleEvent::getStartDateTime).reversed());

        return result;
    }



    public List<ScheduleEvent> getAllScheduleEvents() {

        List<ScheduleEvent> events = new ArrayList<>();

        for (TrainSchedule ts : schedulesByTrain.values()) {
            events.addAll(ts.getScheduleEvents());
        }

        events.sort(Comparator.comparing(ScheduleEvent::getStartDateTime));

        return events;
    }

}
