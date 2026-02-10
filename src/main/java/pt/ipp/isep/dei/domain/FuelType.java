package pt.ipp.isep.dei.domain;

/**
 * Enumeration that represents the types of fuel.
 * Each fuel type has an associated identifier and description.
 */
public enum FuelType {

    /** Diesel fuel type */
    DIESEL(1, "Diesel"),
    /** Electric fuel type */
    ELECTRIC(2, "Electric");

    /** Identifier of the fuel type */
    private final int id;
    /** Description of the fuel type */
    private final String description;

    /**
     * Constructs a fuel type with an identifier and description.
     *
     * @param id fuel type identifier
     * @param description fuel type description
     */
    FuelType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Returns the identifier of the fuel type.
     *
     * @return fuel type id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the description of the fuel type.
     *
     * @return fuel type description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the fuel type that matches the given identifier.
     *
     * @param id fuel type identifier
     * @return matching FuelType or null if none is found
     */
    public static FuelType fromId(int id) {
        for (FuelType f : values()) {
            if (f.id == id) return f;
        }
        return null;
    }

    /**
     * Returns the string representation of the fuel type.
     *
     * @return fuel type description
     */
    @Override
    public String toString() {
        return description;
    }
}
