package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLineSegment;

import java.util.*;

public class RailwayLineSegmentStoreInMemory implements Persistable {

    private static final Map<Integer, RailwayLineSegment> segments = new TreeMap<>();

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        RailwayLineSegment seg = (RailwayLineSegment) object;

        if (seg == null || seg.getId() <= 0) {
            throw new IllegalArgumentException("Invalid segment or ID.");
        }

        segments.put(seg.getId(), seg);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        RailwayLineSegment seg = (RailwayLineSegment) object;

        if (seg == null || seg.getId() <= 0) {
            throw new IllegalArgumentException("Invalid segment or ID.");
        }

        if (!segments.containsKey(seg.getId())) {
            throw new NoSuchElementException("Segment not found: " + seg.getId());
        }

        segments.remove(seg.getId());
        return true;
    }

    @Override
    public RailwayLineSegment findById(DatabaseConnection databaseConnection, String id) {
        int key = Integer.parseInt(id);

        if (!segments.containsKey(key)) {
            throw new NoSuchElementException("Segment not found: " + id);
        }

        return segments.get(key);
    }

    public List<RailwayLineSegment> findAll() {
        return new ArrayList<>(segments.values());
    }

    public boolean existsByKey(int id) {
        return segments.containsKey(id);
    }

    public int count() {
        return segments.size();
    }

    public void clear() {
        segments.clear();
    }
}
