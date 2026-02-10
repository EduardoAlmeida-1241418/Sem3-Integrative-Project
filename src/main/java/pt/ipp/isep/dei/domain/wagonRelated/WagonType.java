package pt.ipp.isep.dei.domain.wagonRelated;

import pt.ipp.isep.dei.domain.CargoType;

import java.util.List;

/**
 * Represents a wagon type.
 * A wagon type defines the category of a wagon and
 * the types of cargo it can transport.
 */
public class WagonType {

    /** Unique identifier of the wagon type */
    private int id;
    /** Name of the wagon type */
    private String name;

    /** List of cargo types supported by this wagon type */
    private List<CargoType> cargoTypes;

    /**
     * Constructs a wagon type with an identifier and name.
     *
     * @param id wagon type identifier
     * @param name wagon type name
     */
    public WagonType(int id, String name) {
        this.id = id;
        this.name = name;
        this.cargoTypes = cargoTypes;
    }

    /**
     * Adds a cargo type to this wagon type.
     *
     * @param cargoType cargo type to add
     */
    public void addCargoType(CargoType cargoType) {
        this.cargoTypes.add(cargoType);
    }

    /**
     * Returns the wagon type identifier.
     *
     * @return wagon type id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the wagon type identifier.
     *
     * @param id wagon type id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the wagon type name.
     *
     * @return wagon type name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the wagon type name.
     *
     * @param name wagon type name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the list of supported cargo types.
     *
     * @return list of cargo types
     */
    public List<CargoType> getCargoTypes() {
        return cargoTypes;
    }

    /**
     * Sets the list of supported cargo types.
     *
     * @param cargoTypes list of cargo types
     */
    public void setCargoTypes(List<CargoType> cargoTypes) {
        this.cargoTypes = cargoTypes;
    }
}
