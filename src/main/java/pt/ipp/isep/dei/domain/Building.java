package pt.ipp.isep.dei.domain;

/**
 * Represents a building.
 * A building is identified by an id and has an associated name.
 */
public class Building {

    /** Unique identifier of the building */
    private String id;
    /** Name of the building */
    private String name;

    /**
     * Constructs a building with the given identifier and name.
     *
     * @param id building identifier
     * @param name building name
     */
    public Building(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the building identifier.
     *
     * @return building id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the building identifier.
     *
     * @param id building id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the building name.
     *
     * @return building name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the building name.
     *
     * @param name building name
     */
    public void setName(String name) {
        this.name = name;
    }
}
