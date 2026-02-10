package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.LocomotiveModel;

import java.util.*;

/**
 * Repository responsible for managing {@link LocomotiveModel} entities.
 * Provides CRUD operations and ensures locomotive model uniqueness based on ID.
 */
public class LocomotiveModelRepository {

    /** Internal map storing locomotive models, indexed by their unique ID. */
    private final Map<Integer, LocomotiveModel> models = new HashMap<>();

    /**
     * Adds a new locomotive model to the repository.
     *
     * @param model the locomotive model to add
     * @throws IllegalArgumentException if the model is null
     * @throws IllegalStateException if a model with the same ID already exists
     */
    public void add(LocomotiveModel model) {
        if (model == null) {
            throw new IllegalArgumentException("LocomotiveModel cannot be null.");
        }

        int id = model.getId();

        if (models.containsKey(id)) {
            throw new IllegalStateException("A LocomotiveModel with the same ID already exists: " + id);
        }

        models.put(id, model);
    }

    /**
     * Finds a locomotive model by its unique identifier.
     *
     * @param modelId the model ID
     * @return the corresponding {@link LocomotiveModel}
     * @throws NoSuchElementException if no model exists with the given ID
     */
    public LocomotiveModel findById(int modelId) {
        LocomotiveModel model = models.get(modelId);

        if (model == null) {
            throw new NoSuchElementException("LocomotiveModel not found with ID: " + modelId);
        }

        return model;
    }

    /**
     * Checks whether a model with the specified ID exists.
     *
     * @param modelId the model ID
     * @return true if the model exists, false otherwise
     */
    public boolean existsById(int modelId) {
        return models.containsKey(modelId);
    }

    /**
     * Removes a locomotive model from the repository by its ID.
     *
     * @param modelId the model ID
     * @throws NoSuchElementException if no model exists with the given ID
     */
    public void remove(int modelId) {
        if (!models.containsKey(modelId)) {
            throw new NoSuchElementException("LocomotiveModel not found with ID: " + modelId);
        }

        models.remove(modelId);
    }

    /**
     * Retrieves all locomotive models stored in the repository.
     *
     * @return an unmodifiable collection of all locomotive models
     */
    public Collection<LocomotiveModel> findAll() {
        return Collections.unmodifiableCollection(models.values());
    }

    /**
     * Removes all locomotive models from the repository.
     */
    public void clear() {
        models.clear();
    }

    /**
     * Counts the total number of locomotive models currently stored.
     *
     * @return total count of locomotive models
     */
    public int count() {
        return models.size();
    }

    public boolean existsByName(String name) {
        for (LocomotiveModel model : models.values()) {
            if (model.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public LocomotiveModel findByName(String name) {
        for (LocomotiveModel model : models.values()) {
            if (model.getName().equalsIgnoreCase(name)) {
                return model;
            }
        }
        throw new NoSuchElementException("LocomotiveModel not found with name: " + name);
    }

    public int getNextId() {
        for (int i = 1; ; i++) {
            if (!models.containsKey(i)) {
                return i;
            }
        }
    }
}
