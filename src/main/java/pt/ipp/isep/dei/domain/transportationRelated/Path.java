package pt.ipp.isep.dei.domain.transportationRelated;

import pt.ipp.isep.dei.data.memory.PathStoreInMemory;
import pt.ipp.isep.dei.domain.Facility;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a path composed of a sequence of facilities.
 * A path has a unique identifier and maintains an ordered list of facilities.
 */
public class Path {

    /** In-memory store for path identifiers */
    private PathStoreInMemory pathStoreInMemory = new PathStoreInMemory();

    /** Unique identifier of the path */
    private int id;
    /** List of facilities that compose the path */
    private List<Facility> facilities;

    /**
     * Constructs a path with a specific identifier.
     * Validates if the identifier already exists.
     *
     * @param id path identifier
     * @throws IllegalArgumentException if the id already exists
     */
    public Path(int id) {
        this.id = id;
        if (pathStoreInMemory.exists(id)) {
            throw new IllegalArgumentException("Path with ID " + id + " already exists.");
        }
        this.facilities = new ArrayList<>();
    }

    /**
     * Constructs a path with an automatically generated identifier
     * and an initial list of facilities.
     *
     * @param facilities list of facilities
     */
    public Path(List<Facility> facilities) {
        this.id = pathStoreInMemory.getNextId();
        this.facilities = new ArrayList<>(facilities);
    }

    /**
     * Returns the path identifier.
     *
     * @return path id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the list of facilities in the path.
     *
     * @return facilities list
     */
    public List<Facility> getFacilities() {
        return facilities;
    }

    /**
     * Adds a facility to the path.
     *
     * @param facility facility to add
     */
    public void addFacility(Facility facility) {
        facilities.add(facility);
    }

    /**
     * Sets the list of facilities in the path.
     *
     * @param facilities facilities list
     */
    public void setFacilities(List<Facility> facilities) {
        this.facilities = new ArrayList<>(facilities);
    }
}
