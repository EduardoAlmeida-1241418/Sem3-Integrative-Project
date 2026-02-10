package pt.ipp.isep.dei.data.memory;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.store.Persistable;
import pt.ipp.isep.dei.comparator.AlphaNumericComparator;
import pt.ipp.isep.dei.domain.LocomotiveModel;

import java.util.*;

public class LocomotiveModelStoreInMemory implements Persistable {

    private static final Map<String, LocomotiveModel> models = new TreeMap<>(new AlphaNumericComparator());

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        LocomotiveModel model = (LocomotiveModel) object;

        if (model == null || model.getId() <= 0) {
            throw new IllegalArgumentException("Invalid locomotive model or ID.");
        }

        models.put(String.valueOf(model.getId()), model);
        return true;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        LocomotiveModel model = (LocomotiveModel) object;

        if (model == null || model.getId() <= 0) {
            throw new IllegalArgumentException("Invalid locomotive model or ID.");
        }

        String key = String.valueOf(model.getId());

        if (!models.containsKey(key)) {
            throw new NoSuchElementException("Locomotive model not found: " + key);
        }

        models.remove(key);
        return true;
    }

    @Override
    public LocomotiveModel findById(DatabaseConnection databaseConnection, String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Invalid ID.");
        }

        LocomotiveModel model = models.get(id);

        if (model == null) {
            throw new NoSuchElementException("Locomotive model not found: " + id);
        }

        return model;
    }

    public boolean existsByKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Invalid key.");
        }
        return models.containsKey(key);
    }

    public Collection<LocomotiveModel> findAll() {
        return Collections.unmodifiableCollection(models.values());
    }

    public void clear() {
        models.clear();
    }

    public int count() {
        return models.size();
    }
}
