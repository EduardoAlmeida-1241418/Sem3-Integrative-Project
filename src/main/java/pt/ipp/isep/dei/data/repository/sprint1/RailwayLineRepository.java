package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.trackRelated.RailwayLine;

import java.util.*;

/**
 * Repository responsible for managing {@link RailwayLine} entities.
 * Provides CRUD operations and ensures railway line uniqueness based on ID.
 */
public class RailwayLineRepository {

    /** Internal map storing railway lines, indexed by their unique ID. */
    private final Map<Integer, RailwayLine> railwayLines = new HashMap<>();

    /**
     * Adds a new railway line to the repository.
     *
     * @param railwayLine the railway line to add
     * @throws IllegalArgumentException if the railway line is null
     * @throws IllegalStateException if a railway line with the same ID already exists
     */
    public void add(RailwayLine railwayLine) {
        if (railwayLine == null) {
            throw new IllegalArgumentException("RailwayLine cannot be null.");
        }

        int id = railwayLine.getId();

        if (railwayLines.containsKey(id)) {
            throw new IllegalStateException("A RailwayLine with the same ID already exists: " + id);
        }

        railwayLines.put(id, railwayLine);
    }

    /**
     * Finds a railway line by its unique identifier.
     *
     * @param railwayLineId the railway line ID
     * @return the corresponding {@link RailwayLine}
     * @throws NoSuchElementException if no railway line exists with the given ID
     */
    public RailwayLine findById(int railwayLineId) {
        RailwayLine line = railwayLines.get(railwayLineId);

        if (line == null) {
            throw new NoSuchElementException("RailwayLine not found with ID: " + railwayLineId);
        }

        return line;
    }

    /**
     * Checks whether a railway line with the specified ID exists.
     *
     * @param railwayLineId the railway line ID
     * @return true if the railway line exists, false otherwise
     */
    public boolean existsById(int railwayLineId) {
        return railwayLines.containsKey(railwayLineId);
    }

    /**
     * Removes a railway line from the repository by its ID.
     *
     * @param railwayLineId the railway line ID
     * @throws NoSuchElementException if no railway line exists with the given ID
     */
    public void remove(int railwayLineId) {
        if (!railwayLines.containsKey(railwayLineId)) {
            throw new NoSuchElementException("RailwayLine not found with ID: " + railwayLineId);
        }

        railwayLines.remove(railwayLineId);
    }

    /**
     * Retrieves all railway lines stored in the repository.
     *
     * @return an unmodifiable collection of all railway lines
     */
    public Collection<RailwayLine> findAll() {
        return Collections.unmodifiableCollection(railwayLines.values());
    }

    /**
     * Removes all railway lines from the repository.
     */
    public void clear() {
        railwayLines.clear();
    }

    /**
     * Counts the total number of railway lines currently stored.
     *
     * @return total count of railway lines
     */
    public int count() {
        return railwayLines.size();
    }
}
