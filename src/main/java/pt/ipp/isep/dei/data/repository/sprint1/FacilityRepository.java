package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.Facility;

import java.util.*;

/**
 * Repository responsible for managing {@link Facility} entities.
 * Provides CRUD operations and ensures facility uniqueness based on ID.
 */
public class FacilityRepository {

    /** Internal map storing facilities, indexed by their unique ID. */
    private final Map<Integer, Facility> facilities = new HashMap<>();

    /**
     * Adds a new facility to the repository.
     *
     * @param facility the facility to add
     * @throws IllegalArgumentException if the facility is null
     * @throws IllegalStateException if a facility with the same ID already exists
     */
    public void add(Facility facility) {
        if (facility == null) {
            throw new IllegalArgumentException("Facility cannot be null.");
        }

        int id = facility.getId();

        if (facilities.containsKey(id)) {
            throw new IllegalStateException("A facility with the same ID already exists: " + id);
        }

        facilities.put(id, facility);
    }

    /**
     * Finds a facility by its unique identifier.
     *
     * @param facilityId the facility ID
     * @return the corresponding {@link Facility} object
     * @throws NoSuchElementException if no facility exists with the given ID
     */
    public Facility findById(int facilityId) {
        Facility facility = facilities.get(facilityId);

        if (facility == null) {
            throw new NoSuchElementException("Facility not found with ID: " + facilityId);
        }

        return facility;
    }

    /**
     * Checks whether a facility with the specified ID exists.
     *
     * @param facilityId the facility ID
     * @return true if the facility exists, false otherwise
     */
    public boolean existsById(int facilityId) {
        return facilities.containsKey(facilityId);
    }

    /**
     * Removes a facility from the repository by its ID.
     *
     * @param facilityId the facility ID
     * @throws NoSuchElementException if no facility exists with the given ID
     */
    public void remove(int facilityId) {
        if (!facilities.containsKey(facilityId)) {
            throw new NoSuchElementException("Facility not found with ID: " + facilityId);
        }
        facilities.remove(facilityId);
    }

    /**
     * Retrieves all facilities stored in the repository.
     *
     * @return an unmodifiable collection of all facilities
     */
    public Collection<Facility> findAll() {
        return Collections.unmodifiableCollection(facilities.values());
    }

    /**
     * Removes all facilities from the repository.
     */
    public void clear() {
        facilities.clear();
    }

    /**
     * Counts the total number of facilities currently stored.
     *
     * @return total count of facilities
     */
    public int count() {
        return facilities.size();
    }
}
