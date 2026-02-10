package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.trackRelated.Siding;

import java.util.*;

public class SidingStoreInMemory implements Persistable {

    private static final Map<Integer, Siding> sidings = new HashMap<>();

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Siding siding = (Siding) object;

        if (siding == null || siding.getId() <= 0) {
            throw new IllegalArgumentException("Invalid siding or ID.");
        }

        sidings.put(siding.getId(), siding);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Siding siding = (Siding) object;

        if (siding == null || siding.getId() <= 0) {
            throw new IllegalArgumentException("Invalid siding or ID.");
        }

        if (!sidings.containsKey(siding.getId())) {
            throw new NoSuchElementException("Siding not found: " + siding.getId());
        }

        sidings.remove(siding.getId());
        return true;
    }

    @Override
    public Siding findById(DatabaseConnection databaseConnection, String id) {
        int key = Integer.parseInt(id);

        if (!sidings.containsKey(key)) {
            throw new NoSuchElementException("Siding not found: " + id);
        }

        return sidings.get(key);
    }

    public Collection<Siding> findAll() {
        return Collections.unmodifiableCollection(sidings.values());
    }

    public boolean exists(int id) {
        return sidings.containsKey(id);
    }

    public void clear() {
        sidings.clear();
    }

    public int count() {
        return sidings.size();
    }
}
