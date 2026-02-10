package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;

import java.util.*;

/**
 * Repository responsible for managing {@link TrolleyModel} entities.
 * Provides CRUD operations and ensures case-insensitive ordering by model name.
 */
public class TrolleyModelRepository {

    /** Internal map storing trolley models, ordered case-insensitively by name. */
    private final Map<String, TrolleyModel> trolleyModels = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Adds a new trolley model to the repository.
     *
     * @param trolleyModel the trolley model to add
     * @throws IllegalArgumentException if the trolley model is null
     * @throws IllegalStateException if a trolley model with the same name already exists
     */
    public void add(TrolleyModel trolleyModel) {
        if (trolleyModel == null) {
            throw new IllegalArgumentException("Trolley model cannot be null.");
        }

        String nameKey = trolleyModel.getName();
        if (trolleyModels.containsKey(nameKey)) {
            throw new IllegalStateException("This trolley model already exists.");
        }

        trolleyModels.put(nameKey, trolleyModel);
    }

    /**
     * Finds a trolley model by its unique name.
     *
     * @param name the name of the trolley model
     * @return the corresponding {@link TrolleyModel}, or {@code null} if not found
     * @throws IllegalArgumentException if the name is null or empty
     */
    public TrolleyModel findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        return trolleyModels.get(name);
    }

    /**
     * Retrieves all trolley models stored in the repository.
     *
     * @return an unmodifiable list of all trolley models
     */
    public List<TrolleyModel> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(trolleyModels.values()));
    }

    /**
     * Removes a trolley model from the repository.
     *
     * @param trolleyModel the trolley model to remove
     * @return true if the model was successfully removed, false otherwise
     * @throws IllegalArgumentException if the trolley model is null
     * @throws NoSuchElementException if the trolley model does not exist in the repository
     */
    public boolean remove(TrolleyModel trolleyModel) {
        if (trolleyModel == null) {
            throw new IllegalArgumentException("Trolley model cannot be null.");
        }

        String nameKey = trolleyModel.getName();
        if (!trolleyModels.containsKey(nameKey)) {
            throw new NoSuchElementException("Trolley model not found.");
        }

        return trolleyModels.remove(nameKey) != null;
    }

    /**
     * Checks whether a given trolley model already exists in the repository.
     *
     * @param trolleyModel the trolley model to check
     * @return true if the model exists, false otherwise
     * @throws IllegalArgumentException if the trolley model is null
     */
    public boolean exists(TrolleyModel trolleyModel) {
        if (trolleyModel == null) {
            throw new IllegalArgumentException("Trolley model cannot be null.");
        }

        return trolleyModels.containsKey(trolleyModel.getName());
    }

    /**
     * Removes all trolley models from the repository.
     */
    public void clear() {
        trolleyModels.clear();
    }

    /**
     * Counts the total number of trolley models currently stored.
     *
     * @return total count of trolley models
     */
    public int count() {
        return trolleyModels.size();
    }
}
