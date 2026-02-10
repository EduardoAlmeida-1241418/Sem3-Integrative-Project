package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.Maker;

import java.util.*;

public class MakerStoreInMemory implements Persistable {

    private static final Map<String, Maker> makers = new HashMap<>();

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Maker maker = (Maker) object;

        if (maker == null || maker.getId() <= 0) {
            throw new IllegalArgumentException("Invalid maker or ID.");
        }

        makers.put(String.valueOf(maker.getId()), maker);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Maker maker = (Maker) object;

        if (maker == null || maker.getId() <= 0) {
            throw new IllegalArgumentException("Invalid maker or ID.");
        }

        String key = String.valueOf(maker.getId());

        if (!makers.containsKey(key)) {
            throw new NoSuchElementException("Maker not found: " + key);
        }

        makers.remove(key);
        return true;
    }

    @Override
    public Maker findById(DatabaseConnection databaseConnection, String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Invalid ID.");
        }

        Maker maker = makers.get(id);

        if (maker == null) {
            throw new NoSuchElementException("Maker not found: " + id);
        }

        return maker;
    }

    public boolean existsByKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Invalid key.");
        }
        return makers.containsKey(key);
    }

    public Collection<Maker> findAll() {
        return Collections.unmodifiableCollection(makers.values());
    }

    public int count() {
        return makers.size();
    }

    public void clear() {
        makers.clear();
    }
}
