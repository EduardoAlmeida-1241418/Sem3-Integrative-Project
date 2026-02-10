package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.Locomotive;

import java.util.*;

/**
 * Repository responsible for managing {@link Locomotive} entities.
 * Provides CRUD operations and ensures locomotive uniqueness based on ID.
 */
public class LocomotiveRepository {

    /** Internal map storing locomotives, indexed by their unique ID. */
    private final Map<Integer, Locomotive> locomotives = new HashMap<>();

    /**
     * Adds a new locomotive to the repository.
     *
     * @param locomotive the locomotive to add
     * @throws IllegalArgumentException if the locomotive is null
     * @throws IllegalStateException if a locomotive with the same ID already exists
     */
    public void add(Locomotive locomotive) {
        if (locomotive == null) {
            throw new IllegalArgumentException("Locomotive cannot be null.");
        }

        int id = locomotive.getId();

        if (locomotives.containsKey(id)) {
            throw new IllegalStateException("A Locomotive with the same ID already exists: " + id);
        }

        locomotives.put(id, locomotive);
    }

    /**
     * Finds a locomotive by its unique identifier.
     *
     * @param locomotiveId the locomotive ID
     * @return the corresponding {@link Locomotive}
     * @throws NoSuchElementException if no locomotive exists with the given ID
     */
    public Locomotive findById(int locomotiveId) {
        Locomotive locomotive = locomotives.get(locomotiveId);

        if (locomotive == null) {
            throw new NoSuchElementException("Locomotive not found with ID: " + locomotiveId);
        }

        return locomotive;
    }

    /**
     * Checks whether a locomotive with the specified ID exists.
     *
     * @param locomotiveId the locomotive ID
     * @return true if the locomotive exists, false otherwise
     */
    public boolean existsById(int locomotiveId) {
        return locomotives.containsKey(locomotiveId);
    }

    /**
     * Removes a locomotive from the repository by its ID.
     *
     * @param locomotiveId the locomotive ID
     * @throws NoSuchElementException if no locomotive exists with the given ID
     */
    public void remove(int locomotiveId) {
        if (!locomotives.containsKey(locomotiveId)) {
            throw new NoSuchElementException("Locomotive not found with ID: " + locomotiveId);
        }

        locomotives.remove(locomotiveId);
    }

    /**
     * Retrieves all locomotives stored in the repository.
     *
     * @return an unmodifiable collection of all locomotives
     */
    public Collection<Locomotive> findAll() {
        return Collections.unmodifiableCollection(locomotives.values());
    }

    /**
     * Removes all locomotives from the repository.
     */
    public void clear() {
        locomotives.clear();
    }

    /**
     * Counts the total number of locomotives currently stored.
     *
     * @return total count of locomotives
     */
    public int count() {
        return locomotives.size();
    }
}
