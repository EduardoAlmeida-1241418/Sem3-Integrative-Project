package pt.ipp.isep.dei.domain;

/**
 * Enumeration representing types of measurement or packaging units for products.
 * Each constant includes a unique identifier and a descriptive name.
 */
public enum UnitType {

    /** Represents a bag unit type. */
    BAG(4, "Bag"),

    /** Represents a bottle unit type. */
    BOTTLE(2, "Bottle"),

    /** Represents a box unit type. */
    BOX(5, "Box"),

    /** Represents a pack unit type. */
    PACK(1, "Pack"),

    /** Represents a single unit type. */
    UNIT(3, "Unit");

    /** Unique numeric identifier for the unit type. */
    private int id;

    /** Descriptive name of the unit type. */
    private String unitTypeName;

    /**
     * Constructs a UnitType instance.
     *
     * @param id unique identifier of the unit type
     * @param unitTypeName descriptive name of the unit type
     */
    UnitType(int id, String unitTypeName) {
        this.id = id;
        this.unitTypeName = unitTypeName;
    }

    /** @return the unique identifier of the unit type */
    public int getId() {
        return id;
    }

    /** @return the descriptive name of the unit type */
    public String getUnitTypeName() {
        return unitTypeName;
    }
}
