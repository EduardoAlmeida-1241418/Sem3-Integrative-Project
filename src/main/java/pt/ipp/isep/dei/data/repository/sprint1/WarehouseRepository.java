package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.Warehouse;

import java.util.*;

/**
 * Repository responsible for managing {@link Warehouse} entities.
 * Provides CRUD operations and ensures uniqueness of warehouses based on their ID.
 */
public class WarehouseRepository {

    /** Internal map storing warehouses, indexed by their unique ID. */
    private final Map<String, Warehouse> warehouses = new TreeMap<>();

    /**
     * Adds a new warehouse to the repository.
     *
     * @param warehouse the warehouse to add
     * @throws IllegalArgumentException if the warehouse or its ID is null or empty
     * @throws IllegalStateException if a warehouse with the same ID already exists
     */
    public void add(Warehouse warehouse) {
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse cannot be null.");
        }
        String id = warehouse.getWarehouseID();
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Warehouse ID cannot be null or empty.");
        }
        if (warehouses.containsKey(id)) {
            throw new IllegalStateException("A warehouse with the same ID already exists: " + id);
        }
        warehouses.put(id, warehouse);
    }

    /**
     * Finds a warehouse by its unique key.
     *
     * @param key the warehouse key
     * @return the corresponding {@link Warehouse} object
     * @throws IllegalArgumentException if the key is null or empty
     * @throws NoSuchElementException if no warehouse exists with the given key
     */
    public Warehouse findByKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Warehouse key cannot be null or empty.");
        }
        Warehouse warehouse = warehouses.get(key);
        if (warehouse == null) {
            throw new NoSuchElementException("Warehouse not found with key: " + key);
        }
        return warehouse;
    }

    /**
     * Checks whether a warehouse with the specified key exists.
     *
     * @param key the warehouse key
     * @return true if a warehouse exists with the given key, false otherwise
     * @throws IllegalArgumentException if the key is null or empty
     */
    public boolean existsByKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Warehouse key cannot be null or empty.");
        }
        return warehouses.containsKey(key);
    }

    /**
     * Removes a warehouse from the repository by its key.
     *
     * @param key the warehouse key
     * @throws IllegalArgumentException if the key is null or empty
     * @throws NoSuchElementException if no warehouse exists with the given key
     */
    public void remove(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Warehouse key cannot be null or empty.");
        }
        if (!warehouses.containsKey(key)) {
            throw new NoSuchElementException("Warehouse not found with key: " + key);
        }
        warehouses.remove(key);
    }

    /**
     * Retrieves all warehouses stored in the repository.
     *
     * @return an unmodifiable collection of all warehouses
     */
    public Collection<Warehouse> findAll() {
        return Collections.unmodifiableCollection(warehouses.values());
    }

    /**
     * Removes all warehouses from the repository.
     */
    public void clear() {
        warehouses.clear();
    }

    /**
     * Counts the total number of warehouses currently stored.
     *
     * @return total count of warehouses
     */
    public int count() {
        return warehouses.size();
    }
}
