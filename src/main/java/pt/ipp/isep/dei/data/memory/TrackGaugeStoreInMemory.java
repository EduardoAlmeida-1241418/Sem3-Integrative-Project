package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.trackRelated.TrackGauge;

import java.util.*;

public class TrackGaugeStoreInMemory implements Persistable {

    private static final Map<String, TrackGauge> gauges = new HashMap<>();

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        TrackGauge gauge = (TrackGauge) object;

        if (gauge == null || gauge.getId() <= 0) {
            throw new IllegalArgumentException("Invalid track gauge or ID.");
        }

        gauges.put(String.valueOf(gauge.getId()), gauge);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        TrackGauge gauge = (TrackGauge) object;

        if (gauge == null || gauge.getId() <= 0) {
            throw new IllegalArgumentException("Invalid track gauge or ID.");
        }

        String key = String.valueOf(gauge.getId());

        if (!gauges.containsKey(key)) {
            throw new NoSuchElementException("Track gauge not found: " + key);
        }

        gauges.remove(key);
        return true;
    }

    @Override
    public TrackGauge findById(DatabaseConnection databaseConnection, String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Invalid ID.");
        }

        TrackGauge gauge = gauges.get(id);

        if (gauge == null) {
            throw new NoSuchElementException("Track gauge not found: " + id);
        }

        return gauge;
    }

    public boolean existsByKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Invalid key.");
        }
        return gauges.containsKey(key);
    }

    public Collection<TrackGauge> findAll() {
        return Collections.unmodifiableCollection(gauges.values());
    }

    public int count() {
        return gauges.size();
    }

    public void clear() {
        gauges.clear();
    }
}
