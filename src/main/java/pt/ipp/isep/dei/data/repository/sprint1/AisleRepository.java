package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.Aisle;

import java.util.*;

/**
 * Repository responsible for managing {@link Aisle} entities.
 * Provides basic CRUD operations and validation for aisle storage and retrieval.
 */
public class AisleRepository {

    /** Internal map storing aisles indexed by their unique identifiers. */
    private final Map<String, Aisle> aisles = new TreeMap<>();

    /**
     * Adds a new aisle to the repository.
     *
     * @param aisle the aisle to add
     * @throws IllegalArgumentException if the aisle or its ID is null or empty
     * @throws IllegalStateException if an aisle with the same ID already exists
     */
    public void add(Aisle aisle) {
        if (aisle == null) {
            throw new IllegalArgumentException("Aisle cannot be null.");
        }
        String id = aisle.getAisleID();
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Aisle ID cannot be null or empty.");
        }
        if (aisles.containsKey(id)) {
            throw new IllegalStateException("An aisle with the same ID already exists: " + id);
        }
        aisles.put(id, aisle);
    }

    /**
     * Finds an aisle by its unique key.
     *
     * @param key the aisle ID
     * @return the corresponding {@link Aisle} object
     * @throws IllegalArgumentException if the key is null or empty
     * @throws NoSuchElementException if no aisle is found for the given key
     */
    public Aisle findByKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Aisle key cannot be null or empty.");
        }
        Aisle aisle = aisles.get(key);
        if (aisle == null) {
            throw new NoSuchElementException("Aisle not found with key: " + key);
        }
        return aisle;
    }

    /**
     * Checks whether an aisle with the specified key exists.
     *
     * @param key the aisle ID
     * @return true if the aisle exists, false otherwise
     * @throws IllegalArgumentException if the key is null or empty
     */
    public boolean existsByKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Aisle key cannot be null or empty.");
        }
        return aisles.containsKey(key);
    }

    /**
     * Removes an aisle from the repository by its key.
     *
     * @param key the aisle ID
     * @throws IllegalArgumentException if the key is null or empty
     * @throws NoSuchElementException if no aisle exists for the given key
     */
    public void remove(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Aisle key cannot be null or empty.");
        }
        if (!aisles.containsKey(key)) {
            throw new NoSuchElementException("Aisle not found with key: " + key);
        }
        aisles.remove(key);
    }

    /**
     * Retrieves all aisles in the repository.
     *
     * @return unmodifiable collection of all stored aisles
     */
    public Collection<Aisle> findAll() {
        return Collections.unmodifiableCollection(aisles.values());
    }

    /**
     * Removes all aisles from the repository.
     */
    public void clear() {
        aisles.clear();
    }

    /**
     * Counts the number of aisles currently stored.
     *
     * @return total number of aisles
     */
    public int count() {
        return aisles.size();
    }
}
