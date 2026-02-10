package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.comparator.AlphaNumericComparator;
import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.domain.wagonRelated.WagonModel;

import java.util.*;

public class WagonModelStoreInMemory implements Persistable {

    private static final Map<String, WagonModel> models =
            new TreeMap<>(new AlphaNumericComparator());

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        WagonModel model = (WagonModel) object;

        if (model == null || model.getId() <= 0) {
            throw new IllegalArgumentException("Invalid wagon model or ID.");
        }

        models.put(String.valueOf(model.getId()), model);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        WagonModel model = (WagonModel) object;

        if (model == null || model.getId() <= 0) {
            throw new IllegalArgumentException("Invalid wagon model or ID.");
        }

        String key = String.valueOf(model.getId());

        if (!models.containsKey(key)) {
            throw new NoSuchElementException("Wagon model not found: " + key);
        }

        models.remove(key);
        return true;
    }

    @Override
    public WagonModel findById(DatabaseConnection databaseConnection, String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Invalid ID.");
        }

        WagonModel model = models.get(id);

        if (model == null) {
            throw new NoSuchElementException("Wagon model not found: " + id);
        }

        return model;
    }

    public boolean existsByKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Invalid key.");
        }
        return models.containsKey(key);
    }

    public Collection<WagonModel> findAll() {
        return Collections.unmodifiableCollection(models.values());
    }

    public void clear() {
        models.clear();
    }

    public int count() {
        return models.size();
    }
}
