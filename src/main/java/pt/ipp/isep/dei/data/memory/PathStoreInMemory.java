package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.transportationRelated.Path;

import java.util.*;

public class PathStoreInMemory implements Persistable {

    private static final  Map<Integer, Path> paths = new TreeMap<>();

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Path path = (Path) object;

        if (path == null || path.getId() <= 0) {
            throw new IllegalArgumentException("Invalid path or ID.");
        }

        paths.put(path.getId(), path);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Path path = (Path) object;

        if (path == null || path.getId() <= 0) {
            throw new IllegalArgumentException("Invalid path or ID.");
        }

        if (!paths.containsKey(path.getId())) {
            throw new NoSuchElementException("Path not found: " + path.getId());
        }

        paths.remove(path.getId());
        return true;
    }

    @Override
    public Path findById(DatabaseConnection databaseConnection, String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Invalid ID.");
        }

        Path path = paths.get(Integer.parseInt(id));

        if (path == null) {
            throw new NoSuchElementException("Path not found: " + id);
        }

        return path;
    }

    public Collection<Path> findAll() {
        return Collections.unmodifiableCollection(paths.values());
    }

    public boolean exists(int id) {
        return paths.containsKey(id);
    }

    public void clear() {
        paths.clear();
    }

    public int count() {
        return paths.size();
    }

    public int getNextId() {
        for (int i = 1; ; i++) {
            if (!paths.containsKey(i)) {
                return i;
            }
        }
    }
}
