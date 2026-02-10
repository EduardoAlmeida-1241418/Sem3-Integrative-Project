package pt.ipp.isep.dei.data.repository.sprint3;

import pt.ipp.isep.dei.domain.ESINF.StationEsinf;

import java.util.*;

/**
 * Repository responsible for managing {@link StationEsinf} entities.
 * Provides CRUD operations and ensures station uniqueness based on ID.
 */
public class StationEsinf3Repository {

    /** Internal map storing stations, indexed by their unique ID. */
    private final Map<String, StationEsinf> stations = new HashMap<>();

    /**
     * Adds a new station to the repository.
     *
     * @param station the station to add
     * @throws IllegalArgumentException if the station or its ID is null or empty
     * @throws IllegalStateException if a station with the same ID already exists
     */
    public void add(StationEsinf station) {
        if (station == null) {
            throw new IllegalArgumentException("StationEsinf cannot be null.");
        }

        String id = station.getId();

        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("StationEsinf ID cannot be null or empty.");
        }

        if (stations.containsKey(id)) {
            throw new IllegalStateException("A StationEsinf with the same ID already exists: " + id);
        }

        stations.put(id, station);
    }

    /**
     * Finds a station by its unique identifier.
     *
     * @param stationId the station ID
     * @return the corresponding {@link StationEsinf}
     * @throws IllegalArgumentException if the ID is null or empty
     * @throws NoSuchElementException if no station exists with the given ID
     */
    public StationEsinf findById(String stationId) {
        if (stationId == null || stationId.isEmpty()) {
            throw new IllegalArgumentException("StationEsinf ID cannot be null or empty.");
        }

        StationEsinf station = stations.get(stationId);

        if (station == null) {
            throw new NoSuchElementException("StationEsinf not found with ID: " + stationId);
        }

        return station;
    }

    /**
     * Checks whether a station with the specified ID exists.
     *
     * @param stationId the station ID
     * @return true if the station exists, false otherwise
     * @throws IllegalArgumentException if the ID is null or empty
     */
    public boolean existsById(String stationId) {
        if (stationId == null || stationId.isEmpty()) {
            throw new IllegalArgumentException("StationEsinf ID cannot be null or empty.");
        }

        return stations.containsKey(stationId);
    }

    /**
     * Removes a station from the repository by its ID.
     *
     * @param stationId the station ID
     * @throws IllegalArgumentException if the ID is null or empty
     * @throws NoSuchElementException if no station exists with the given ID
     */
    public void remove(String stationId) {
        if (stationId == null || stationId.isEmpty()) {
            throw new IllegalArgumentException("StationEsinf ID cannot be null or empty.");
        }

        if (!stations.containsKey(stationId)) {
            throw new NoSuchElementException("StationEsinf not found with ID: " + stationId);
        }

        stations.remove(stationId);
    }

    /**
     * Retrieves all stations stored in the repository.
     *
     * @return an unmodifiable collection of all stations
     */
    public Collection<StationEsinf> findAll() {
        return Collections.unmodifiableCollection(stations.values());
    }

    /**
     * Removes all stations from the repository.
     */
    public void clear() {
        stations.clear();
    }

    /**
     * Counts the total number of stations currently stored.
     *
     * @return total count of stations
     */
    public int count() {
        return stations.size();
    }
}
