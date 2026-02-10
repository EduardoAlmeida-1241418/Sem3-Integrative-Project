package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.Facility;

import java.util.*;

public class FacilityStoreInMemory implements Persistable {

    private static final Map<Integer, Facility> facilities = new HashMap<>();

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Facility facility = (Facility) object;

        facilities.put(facility.getId(), facility);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Facility facility = (Facility) object;

        if (!facilities.containsKey(facility.getId())) {
            throw new NoSuchElementException("Facility not found: " + facility.getId());
        }

        facilities.remove(facility.getId());
        return true;
    }

    @Override
    public Facility findById(DatabaseConnection databaseConnection, String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Invalid ID.");
        }

        int intId;
        try {
            intId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID must be an integer: " + id);
        }

        Facility facility = facilities.get(intId);

        if (facility == null) {
            throw new NoSuchElementException("Facility not found: " + id);
        }

        return facility;
    }

    public boolean existsByKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Invalid key.");
        }
        return facilities.containsKey(key);
    }

    public List<Facility> findAll() {
        return new ArrayList<>(facilities.values());
    }

    public int count() {
        return facilities.size();
    }

    public void clear() {
        facilities.clear();
    }
}
