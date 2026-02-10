package pt.ipp.isep.dei.domain.wagonRelated;

import pt.ipp.isep.dei.data.memory.DimensionsStoreInMemory;
import pt.ipp.isep.dei.domain.Dimensions;

/**
 * Represents a wagon model.
 * A wagon model defines the physical and operational characteristics
 * of a wagon, including payload, volume capacity, speed limits and dimensions.
 */
public class WagonModel {

    /** In-memory store used to retrieve dimensions */
    private DimensionsStoreInMemory dimensionsStoreInMemory = new DimensionsStoreInMemory();

    /** Unique identifier of the wagon model */
    private int id;
    /** Name of the wagon model */
    private String name;
    /** Maximum payload supported by the wagon */
    private double payload;
    /** Maximum volume capacity of the wagon */
    private double volumeCapacity;
    /** Number of wheels of the wagon */
    private int nWheels;
    /** Maximum allowed speed of the wagon */
    private int maxSpeed;
    /** Identifier of the wagon manufacturer */
    private int makerId;
    /** Identifier of the wagon dimensions */
    private int dimensionsId;
    /** Identifier of the wagon type */
    private int wagonTypeId;

    /**
     * Constructs a wagon model with all required attributes.
     *
     * @param id wagon model identifier
     * @param name wagon model name
     * @param payload maximum payload
     * @param volumeCapacity volume capacity
     * @param nWheels number of wheels
     * @param maxSpeed maximum speed
     * @param makerId manufacturer identifier
     * @param dimensionsId dimensions identifier
     * @param wagonTypeId wagon type identifier
     */
    public WagonModel(int id, String name, double payload, double volumeCapacity, int nWheels, int maxSpeed, int makerId, int dimensionsId, int wagonTypeId) {
        this.id = id;
        this.name = name;
        this.payload = payload;
        this.volumeCapacity = volumeCapacity;
        this.nWheels = nWheels;
        this.maxSpeed = maxSpeed;
        this.makerId = makerId;
        this.dimensionsId = dimensionsId;
        this.wagonTypeId = wagonTypeId;
    }

    /**
     * Returns the wagon model identifier.
     *
     * @return wagon model id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the wagon model identifier.
     *
     * @param id wagon model id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the wagon model name.
     *
     * @return wagon model name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the wagon model name.
     *
     * @param name wagon model name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the maximum payload of the wagon.
     *
     * @return payload
     */
    public double getPayload() {
        return payload;
    }

    /**
     * Sets the maximum payload of the wagon.
     *
     * @param payload payload
     */
    public void setPayload(double payload) {
        this.payload = payload;
    }

    /**
     * Returns the volume capacity of the wagon.
     *
     * @return volume capacity
     */
    public double getVolumeCapacity() {
        return volumeCapacity;
    }

    /**
     * Sets the volume capacity of the wagon.
     *
     * @param volumeCapacity volume capacity
     */
    public void setVolumeCapacity(double volumeCapacity) {
        this.volumeCapacity = volumeCapacity;
    }

    /**
     * Returns the number of wheels.
     *
     * @return number of wheels
     */
    public int getnWheels() {
        return nWheels;
    }

    /**
     * Sets the number of wheels.
     *
     * @param nWheels number of wheels
     */
    public void setnWheels(int nWheels) {
        this.nWheels = nWheels;
    }

    /**
     * Returns the maximum allowed speed.
     *
     * @return maximum speed
     */
    public int getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Sets the maximum allowed speed.
     *
     * @param maxSpeed maximum speed
     */
    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Returns the manufacturer identifier.
     *
     * @return maker id
     */
    public int getMakerId() {
        return makerId;
    }

    /**
     * Sets the manufacturer identifier.
     *
     * @param makerId maker id
     */
    public void setMakerId(int makerId) {
        this.makerId = makerId;
    }

    /**
     * Returns the dimensions identifier.
     *
     * @return dimensions id
     */
    public int getDimensionsId() {
        return dimensionsId;
    }

    /**
     * Sets the dimensions identifier.
     *
     * @param dimensionsId dimensions id
     */
    public void setDimensionsId(int dimensionsId) {
        this.dimensionsId = dimensionsId;
    }

    /**
     * Returns the wagon type identifier.
     *
     * @return wagon type id
     */
    public int getWagonTypeId() {
        return wagonTypeId;
    }

    /**
     * Sets the wagon type identifier.
     *
     * @param wagonTypeId wagon type id
     */
    public void setWagonTypeId(int wagonTypeId) {
        this.wagonTypeId = wagonTypeId;
    }

    /**
     * Retrieves the dimensions associated with this wagon model.
     *
     * @return wagon dimensions
     */
    public Dimensions getDimensions(){
        return dimensionsStoreInMemory.findById(null, String.valueOf(this.wagonTypeId));
    }
}
