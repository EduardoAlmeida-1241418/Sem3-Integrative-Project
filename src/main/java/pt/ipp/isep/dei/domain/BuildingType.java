package pt.ipp.isep.dei.domain;

/**
 * Enumeration that represents different types of buildings.
 * Each building type has an associated identifier and description.
 */
public enum BuildingType {

    /** Warehouse building type */
    WAREHOUSE(1, "Warehouse"),
    /** Refrigerated area building type */
    REFRIGERATED_AREA(2, "Refrigerated Area"),
    /** Grain silo building type */
    GRAIN_SILO(3, "Grain Silo");

    /** Identifier of the building type */
    private int id;
    /** Description of the building type */
    private String description;

    /**
     * Constructs a building type with an identifier and description.
     *
     * @param id building type identifier
     * @param description building type description
     */
    BuildingType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Returns the identifier of the building type.
     *
     * @return building type id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the description of the building type.
     *
     * @return building type description
     */
    public String getDescription() {
        return description;
    }
}
