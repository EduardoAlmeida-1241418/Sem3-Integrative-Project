package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.wagonRelated.WagonType;

import java.util.*;

public class WagonTypeStoreInMemory implements Persistable {

    private static final Map<Integer, WagonType> wagonTypes = new HashMap<>();

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        WagonType wagonType = (WagonType) object;

        if (wagonType == null || wagonType.getId() <= 0) {
            throw new IllegalArgumentException("Invalid wagon type or ID.");
        }

        wagonTypes.put(wagonType.getId(), wagonType);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        WagonType wagonType = (WagonType) object;

        if (!wagonTypes.containsKey(wagonType.getId())) {
            throw new NoSuchElementException("Wagon type not found: " + wagonType.getId());
        }

        wagonTypes.remove(wagonType.getId());
        return true;
    }

    @Override
    public WagonType findById(DatabaseConnection databaseConnection, String id) {
        int key = Integer.parseInt(id);
        WagonType wagonType = wagonTypes.get(key);

        if (wagonType == null) {
            throw new NoSuchElementException("Wagon type not found: " + id);
        }

        return wagonType;
    }

    public boolean existsByKey(int id) {
        return wagonTypes.containsKey(id);
    }

    public Collection<WagonType> findAll() {
        return Collections.unmodifiableCollection(wagonTypes.values());
    }

    public void clear() {
        wagonTypes.clear();
    }

    public int count() {
        return wagonTypes.size();
    }
}
