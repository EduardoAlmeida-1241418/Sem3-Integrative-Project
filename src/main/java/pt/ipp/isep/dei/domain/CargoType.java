package pt.ipp.isep.dei.domain;

/**
 * Enumeration that represents the different types of cargo.
 * Each cargo type has an associated identifier and description.
 */
public enum CargoType {

    /** Liquid cargo type */
    LIQUIDS(1, "Liquids"),
    /** Chemical cargo type */
    CHEMICALS(2, "Chemicals"),
    /** Fuel cargo type */
    FUEL(3, "Fuel"),
    /** Coal cargo type */
    COAL(4, "Coal"),
    /** Grain cargo type */
    GRAINS(5, "Grains"),
    /** Mineral cargo type */
    MINERALS(6, "Minerals"),
    /** Perishable goods cargo type */
    PERISHABLE_GOODS(7, "Perishable goods");

    /** Identifier of the cargo type */
    private int id;
    /** Description of the cargo type */
    private String description;

    /**
     * Constructs a cargo type with an identifier and description.
     *
     * @param id cargo type identifier
     * @param description cargo type description
     */
    CargoType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Returns the identifier of the cargo type.
     *
     * @return cargo type id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the description of the cargo type.
     *
     * @return cargo type description
     */
    public String getDescription() {
        return description;
    }
}
