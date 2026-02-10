package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.wagonRelated.Wagon;

import java.util.*;

/**
 * Repository responsible for managing {@link Wagon} entities.
 * Provides CRUD operations and ensures wagon uniqueness based on ID.
 */
public class WagonRepository {

    /** Internal map storing wagons, indexed by their unique ID. */
    private final Map<Integer, Wagon> wagons = new HashMap<>();

    /**
     * Adds a new wagon to the repository.
     *
     * @param wagon the wagon to add
     * @throws IllegalArgumentException if the wagon or its ID is null or empty
     * @throws IllegalStateException if a wagon with the same ID already exists
     */
    public void add(Wagon wagon) {
        if (wagon == null) {
            throw new IllegalArgumentException("Wagon cannot be null.");
        }
        int id = wagon.getWagonID();
        wagons.put(id, wagon);
    }

    /**
     * Finds a wagon by its unique identifier.
     *
     * @param wagonID the wagon ID
     * @return the corresponding {@link Wagon} object
     * @throws IllegalArgumentException if the ID is null or empty
     * @throws NoSuchElementException if no wagon exists with the given ID
     */
    public Wagon findById(int wagonID) {
        Wagon wagon = wagons.get(wagonID);
        if (wagon == null) {
            throw new NoSuchElementException("Wagon not found with ID: " + wagonID);
        }
        return wagon;
    }

    /**
     * Checks whether a wagon with the specified ID exists.
     *
     * @param wagonID the wagon ID
     * @return true if the wagon exists, false otherwise
     * @throws IllegalArgumentException if the ID is null or empty
     */
    public boolean existsById(int wagonID) {
        return wagons.containsKey(wagonID);
    }

    /**
     * Removes a wagon from the repository by its ID.
     *
     * @param wagonID the wagon ID
     * @throws IllegalArgumentException if the ID is null or empty
     * @throws NoSuchElementException if no wagon exists with the given ID
     */
    public void remove(int wagonID) {
        if (!wagons.containsKey(wagonID)) {
            throw new NoSuchElementException("Wagon not found with ID: " + wagonID);
        }
        wagons.remove(wagonID);
    }

    /**
     * Retrieves all wagons stored in the repository.
     *
     * @return an unmodifiable collection of all wagons
     */
    public Collection<Wagon> findAll() {
        return Collections.unmodifiableCollection(wagons.values());
    }

    /**
     * Removes all wagons from the repository.
     */
    public void clear() {
        wagons.clear();
    }

    /**
     * Counts the total number of wagons currently stored.
     *
     * @return total count of wagons
     */
    public int count() {
        return wagons.size();
    }
}
