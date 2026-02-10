package pt.ipp.isep.dei.data.repository.sprint3;

import pt.ipp.isep.dei.domain.ESINF.RailwayLineEsinf;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;

import java.util.*;

/**
 * Repository responsible for managing {@link RailwayLineEsinf} entities.
 * Provides CRUD operations and ensures line uniqueness based on departure->arrival station IDs.
 */
public class RailwayLineEsinfRepository {

    /** Internal map storing railway lines, indexed by a composite key "departureId->arrivalId". */
    private final Map<String, RailwayLineEsinf> lines = new HashMap<>();

    /**
     * Generates a unique key for a railway line based on its stations.
     */
    private String generateKey(RailwayLineEsinf line) {
        StationEsinf dep = line.getDepartureStation();
        StationEsinf arr = line.getArrivalStation();

        return dep.getId() + "->" + arr.getId();
    }

    /**
     * Adds a new railway line to the repository.
     *
     * @param line the railway line to add
     * @throws IllegalArgumentException if the line or its stations are null
     * @throws IllegalStateException if a line with the same ID already exists
     */
    public void add(RailwayLineEsinf line) {
        if (line == null) {
            throw new IllegalArgumentException("RailwayLineEsinf cannot be null.");
        }
        if (line.getDepartureStation() == null || line.getArrivalStation() == null) {
            throw new IllegalArgumentException("Stations in RailwayLineEsinf cannot be null.");
        }

        String key = generateKey(line);

        if (lines.containsKey(key)) {
            throw new IllegalStateException("A RailwayLineEsinf with the same stations already exists: " + key);
        }

        lines.put(key, line);
    }

    /**
     * Finds a railway line by a composite station key.
     *
     * @param key formatted as "departureId->arrivalId"
     * @return the corresponding {@link RailwayLineEsinf}
     * @throws IllegalArgumentException if the key is null or empty
     * @throws NoSuchElementException if no line exists with the given key
     */
    public RailwayLineEsinf findById(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("RailwayLineEsinf key cannot be null or empty.");
        }

        RailwayLineEsinf line = lines.get(key);

        if (line == null) {
            throw new NoSuchElementException("RailwayLineEsinf not found with key: " + key);
        }

        return line;
    }

    /**
     * Checks whether a railway line exists by key.
     *
     * @param key formatted as "departureId->arrivalId"
     * @return true if the line exists, false otherwise
     */
    public boolean existsById(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("RailwayLineEsinf key cannot be null or empty.");
        }

        return lines.containsKey(key);
    }

    /**
     * Removes a railway line from the repository by its key.
     *
     * @param key formatted as "departureId->arrivalId"
     * @throws IllegalArgumentException if the key is null or empty
     * @throws NoSuchElementException if no line exists with the given key
     */
    public void remove(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("RailwayLineEsinf key cannot be null or empty.");
        }

        if (!lines.containsKey(key)) {
            throw new NoSuchElementException("RailwayLineEsinf not found with key: " + key);
        }

        lines.remove(key);
    }

    /**
     * Retrieves all railway lines stored in the repository.
     *
     * @return an unmodifiable collection of all lines
     */
    public Collection<RailwayLineEsinf> findAll() {
        return Collections.unmodifiableCollection(lines.values());
    }

    /**
     * Removes all railway lines from the repository.
     */
    public void clear() {
        lines.clear();
    }

    /**
     * Counts the total number of railway lines currently stored.
     *
     * @return total count of lines
     */
    public int count() {
        return lines.size();
    }
}
