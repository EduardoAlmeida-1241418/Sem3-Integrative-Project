package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.Dimensions;

import java.util.*;

public class DimensionsStoreInMemory implements Persistable {

    private static final Map<String, Dimensions> dimensionsMap = new HashMap<>();

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Dimensions dim = (Dimensions) object;

        if (dim == null || dim.getId() <= 0) {
            throw new IllegalArgumentException("Invalid dimensions or ID.");
        }

        dimensionsMap.put(String.valueOf(dim.getId()), dim);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Dimensions dim = (Dimensions) object;

        if (dim == null || dim.getId() <= 0) {
            throw new IllegalArgumentException("Invalid dimensions or ID.");
        }

        if (!dimensionsMap.containsKey(String.valueOf(dim.getId()))) {
            throw new NoSuchElementException("Dimensions not found: " + dim.getId());
        }

        dimensionsMap.remove(String.valueOf(dim.getId()));
        return true;
    }

    @Override
    public Dimensions findById(DatabaseConnection databaseConnection, String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Invalid ID.");
        }

        Dimensions dim = dimensionsMap.get(id);

        if (dim == null) {
            throw new NoSuchElementException("Dimensions not found: " + id);
        }

        return dim;
    }

    public boolean existsByKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Invalid key.");
        }
        return dimensionsMap.containsKey(key);
    }

    public Collection<Dimensions> findAll() {
        return Collections.unmodifiableCollection(dimensionsMap.values());
    }

    public int count() {
        return dimensionsMap.size();
    }

    public void clear() {
        dimensionsMap.clear();
    }
}
