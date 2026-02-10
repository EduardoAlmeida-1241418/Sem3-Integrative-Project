package pt.ipp.isep.dei.domain;

/**
 * Enumeration that represents the types of facilities.
 * Each facility type has an associated identifier and description.
 */
public enum FacilityType {

    /** Station facility type */
    STATION(1, "Station"),
    /** Freight yard facility type */
    FREIGHT_YARD(2, "Freight Yard"),
    /** Terminal facility type */
    TERMINAL(3, "Terminal");

    /** Identifier of the facility type */
    private int id;
    /** Description of the facility type */
    private String description;

    /**
     * Constructs a facility type with an identifier and description.
     *
     * @param id facility type identifier
     * @param description facility type description
     */
    FacilityType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Returns the identifier of the facility type.
     *
     * @return facility type id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the description of the facility type.
     *
     * @return facility type description
     */
    public String getDescription() {
        return description;
    }
}
