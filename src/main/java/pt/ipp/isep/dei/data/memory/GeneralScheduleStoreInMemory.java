package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.schedule.GeneralSchedule;

import java.util.*;

public class GeneralScheduleStoreInMemory implements Persistable {

    private static final TreeMap<String, GeneralSchedule> generalSchedules = new TreeMap<>();

    private static final PriorityQueue<Integer> generalScheduleIds = new PriorityQueue<>();
    private static final PriorityQueue<Integer> schedulesEventIds = new PriorityQueue<>();
    private static final PriorityQueue<Integer> jointRailwayLineSegmentIds = new PriorityQueue<>();
    private static final PriorityQueue<Integer> segmentLineDividedIds = new PriorityQueue<>();

    @Override
    public boolean save(DatabaseConnection db, Object obj) {
        GeneralSchedule schedule = (GeneralSchedule) obj;
        generalSchedules.put(String.valueOf(schedule.getId()), schedule);
        addGeneralScheduleId(schedule.getId());
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection db, Object obj) {
        GeneralSchedule schedule = (GeneralSchedule) obj;
        generalSchedules.remove(String.valueOf(schedule.getId()));
        return true;
    }

    @Override
    public GeneralSchedule findById(DatabaseConnection db, String id) {
        return generalSchedules.get(id);
    }

    public List<GeneralSchedule> findAll() {
        return new ArrayList<>(generalSchedules.values());
    }

    public GeneralSchedule findLatest() {
        int maxId = 0;
        GeneralSchedule latestSchedule = null;
        for (GeneralSchedule schedule : generalSchedules.values()) {
            if (schedule.getId() > maxId) {
                maxId = schedule.getId();
                latestSchedule = schedule;
            }
        }
        return latestSchedule;
    }

    public void clear() {
        generalSchedules.clear();
        generalScheduleIds.clear();
        schedulesEventIds.clear();
        jointRailwayLineSegmentIds.clear();
        segmentLineDividedIds.clear();
    }

    public boolean existsGeneralScheduleWithId(int id) {
        return generalSchedules.containsKey(String.valueOf(id));
    }

    public boolean existsScheduleEventWithId(int id) {
        return schedulesEventIds.contains(id);
    }

    public boolean existsJointRailwayLineSegmentWithId(int id) {
        return jointRailwayLineSegmentIds.contains(id);
    }

    public boolean existsSegmentLineDividedWithId(int id) {
        return segmentLineDividedIds.contains(id);
    }

    public void addGeneralScheduleId(int id) {
        generalScheduleIds.add(id);
    }

    public void addScheduleEventId(int id) {
        schedulesEventIds.add(id);
    }

    public void addJointRailwayLineSegmentId(int id) {
        jointRailwayLineSegmentIds.add(id);
    }

    public void addSegmentLineDividedId(int id) {
        segmentLineDividedIds.add(id);
    }

    public int getNextGeneralScheduleId() {
        Set<Integer> usedIds = new HashSet<>(generalScheduleIds);

        int expected = 1;
        while (usedIds.contains(expected)) {
            expected++;
        }
        return expected;
    }


    public int getNextScheduleEventId() {
        Set<Integer> usedIds = new HashSet<>(schedulesEventIds);

        int expected = 1;
        while (usedIds.contains(expected)) {
            expected++;
        }
        return expected;
    }

    public int getNextJointRailwayLineSegmentId() {
        Set<Integer> usedIds = new HashSet<>(jointRailwayLineSegmentIds);

        int expected = 1;
        while (usedIds.contains(expected)) {
            expected++;
        }
        return expected;
    }

    public int getNextSegmentLineDividedId() {
        Set<Integer> usedIds = new HashSet<>(segmentLineDividedIds);

        int expected = 1;
        while (usedIds.contains(expected)) {
            expected++;
        }
        return expected;
    }
}
