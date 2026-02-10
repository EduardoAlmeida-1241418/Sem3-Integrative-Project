package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.trackRelated.RailwayLineSegment;

import java.util.*;

/**
 * Repository responsible for managing {@link RailwayLineSegment} entities.
 * Provides CRUD operations and ensures segment uniqueness based on ID.
 */
public class RailwayLineSegmentRepository {

    /** Internal map storing railway line segments, indexed by their unique ID. */
    private final Map<Integer, RailwayLineSegment> segments = new HashMap<>();

    /**
     * Adds a new railway line segment to the repository.
     *
     * @param segment the segment to add
     * @throws IllegalArgumentException if the segment is null
     * @throws IllegalStateException if a segment with the same ID already exists
     */
    public void add(RailwayLineSegment segment) {
        if (segment == null) {
            throw new IllegalArgumentException("RailwayLineSegment cannot be null.");
        }

        int id = segment.getId();

        if (segments.containsKey(id)) {
            throw new IllegalStateException("A RailwayLineSegment with the same ID already exists: " + id);
        }

        segments.put(id, segment);
    }

    /**
     * Finds a railway line segment by its unique identifier.
     *
     * @param segmentId the segment ID
     * @return the corresponding {@link RailwayLineSegment}
     * @throws NoSuchElementException if no segment exists with the given ID
     */
    public RailwayLineSegment findById(int segmentId) {
        RailwayLineSegment segment = segments.get(segmentId);

        if (segment == null) {
            throw new NoSuchElementException("RailwayLineSegment not found with ID: " + segmentId);
        }

        return segment;
    }

    /**
     * Checks whether a railway line segment with the specified ID exists.
     *
     * @param segmentId the segment ID
     * @return true if the segment exists, false otherwise
     */
    public boolean existsById(int segmentId) {
        return segments.containsKey(segmentId);
    }

    /**
     * Removes a railway line segment from the repository by its ID.
     *
     * @param segmentId the segment ID
     * @throws NoSuchElementException if no segment exists with the given ID
     */
    public void remove(int segmentId) {
        if (!segments.containsKey(segmentId)) {
            throw new NoSuchElementException("RailwayLineSegment not found with ID: " + segmentId);
        }

        segments.remove(segmentId);
    }

    /**
     * Retrieves all railway line segments stored in the repository.
     *
     * @return an unmodifiable collection of all segments
     */
    public Collection<RailwayLineSegment> findAll() {
        return Collections.unmodifiableCollection(segments.values());
    }

    /**
     * Removes all railway line segments from the repository.
     */
    public void clear() {
        segments.clear();
    }

    /**
     * Counts the total number of railway line segments currently stored.
     *
     * @return total count of segments
     */
    public int count() {
        return segments.size();
    }
}
