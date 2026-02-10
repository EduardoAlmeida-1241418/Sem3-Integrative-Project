package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.comparator.BayOrderComparator;
import pt.ipp.isep.dei.domain.Bay;

import java.util.*;

/**
 * Repository responsible for managing {@link Bay} entities.
 * Provides CRUD operations and ensures validation and ordering based on {@link BayOrderComparator}.
 */
public class BayRepository {

    /** Internal map storing bays, ordered by {@link BayOrderComparator}. */
    private final Map<String, Bay> bays = new TreeMap<>(new BayOrderComparator());

    /**
     * Adds a new bay to the repository.
     *
     * @param bay the bay to add
     * @throws IllegalArgumentException if the bay or its ID is null or empty
     * @throws IllegalStateException if a bay with the same ID already exists
     */
    public void add(Bay bay) {
        if (bay == null) {
            throw new IllegalArgumentException("Bay cannot be null.");
        }
        String id = bay.getBayId();
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Bay ID cannot be null or empty.");
        }
        if (bays.containsKey(id)) {
            throw new IllegalStateException("A bay with the same ID already exists: " + id);
        }
        bays.put(id, bay);
    }

    /**
     * Finds a bay by its unique key.
     *
     * @param key the bay ID
     * @return the corresponding {@link Bay} object
     * @throws IllegalArgumentException if the key is null or empty
     * @throws NoSuchElementException if no bay exists for the given key
     */
    public Bay findByKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Bay key cannot be null or empty.");
        }
        Bay bay = bays.get(key);
        if (bay == null) {
            throw new NoSuchElementException("Bay not found with key: " + key);
        }
        return bay;
    }

    /**
     * Checks whether a bay with the specified key exists.
     *
     * @param key the bay ID
     * @return true if the bay exists, false otherwise
     * @throws IllegalArgumentException if the key is null or empty
     */
    public boolean existsByKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Bay key cannot be null or empty.");
        }
        return bays.containsKey(key);
    }

    /**
     * Removes a bay from the repository by its key.
     *
     * @param key the bay ID
     * @throws IllegalArgumentException if the key is null or empty
     * @throws NoSuchElementException if no bay exists for the given key
     */
    public void remove(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Bay key cannot be null or empty.");
        }
        if (!bays.containsKey(key)) {
            throw new NoSuchElementException("Bay not found with key: " + key);
        }
        bays.remove(key);
    }

    /**
     * Retrieves all bays in the repository.
     *
     * @return an unmodifiable collection of all bays
     */
    public Collection<Bay> findAll() {
        return Collections.unmodifiableCollection(bays.values());
    }

    /**
     * Removes all bays from the repository.
     */
    public void clear() {
        bays.clear();
    }

    /**
     * Returns the number of bays stored in the repository.
     *
     * @return total count of bays
     */
    public int count() {
        return bays.size();
    }
}
