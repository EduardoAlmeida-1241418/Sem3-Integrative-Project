package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.domain.trackRelated.TrackGauge;

import java.util.List;

/**
 * Represents a locomotive model.
 * A locomotive model defines the technical and operational characteristics
 * of a locomotive, including power, speed, traction, fuel type and compatibility
 * with different track gauges.
 */
public class LocomotiveModel {

    /** Unique identifier of the locomotive model */
    private int id;
    /** Name of the locomotive model */
    private String name;
    /** Power of the locomotive */
    private int power;
    /** Maximum supported weight */
    private int maximumWeight;
    /** Acceleration capability */
    private double acceleration;
    /** Number of wheels */
    private int numberWheels;
    /** Maximum allowed speed */
    private int maxSpeed;
    /** Operational speed */
    private double operationalSpeed;
    /** Traction value */
    private int traction;

    /** Manufacturer of the locomotive */
    private Maker maker;
    /** Physical dimensions of the locomotive */
    private Dimensions dimensions;
    /** Fuel type used by the locomotive */
    private FuelType fuelType;
    /** Supported track gauges */
    private List<TrackGauge> trackGauges;

    /** Fuel capacity */
    private int fuelCapacity;
    /** Voltage used by the locomotive */
    private int voltage;
    /** Electrical frequency */
    private int frequency;

    /**
     * Constructs a locomotive model with all its attributes.
     *
     * @param id locomotive model identifier
     * @param name locomotive model name
     * @param power locomotive power
     * @param maximumWeight maximum supported weight
     * @param acceleration acceleration capability
     * @param numberWheels number of wheels
     * @param maxSpeed maximum speed
     * @param operationalSpeed operational speed
     * @param traction traction value
     * @param maker manufacturer
     * @param dimensions physical dimensions
     * @param fuelType fuel type
     * @param trackGauges supported track gauges
     * @param fuelCapacity fuel capacity
     * @param voltage voltage
     * @param frequency frequency
     */
    public LocomotiveModel(int id, String name, int power, int maximumWeight, double acceleration, int numberWheels, int maxSpeed, double operationalSpeed, int traction, Maker maker, Dimensions dimensions, FuelType fuelType, List<TrackGauge> trackGauges, int fuelCapacity, int voltage, int frequency) {
        this.id = id;
        this.name = name;
        this.power = power;
        this.maximumWeight = maximumWeight;
        this.acceleration = acceleration;
        this.numberWheels = numberWheels;
        this.maxSpeed = maxSpeed;
        this.operationalSpeed = operationalSpeed;
        this.traction = traction;
        this.maker = maker;
        this.dimensions = dimensions;
        this.fuelType = fuelType;
        this.trackGauges = trackGauges;
        this.fuelCapacity = fuelCapacity;
        this.voltage = voltage;
        this.frequency = frequency;
    }

    /**
     * Returns the locomotive model identifier.
     *
     * @return locomotive model id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the locomotive model identifier.
     *
     * @param id locomotive model id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the locomotive model name.
     *
     * @return model name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the locomotive model name.
     *
     * @param name model name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the locomotive power.
     *
     * @return power
     */
    public int getPower() {
        return power;
    }

    /**
     * Sets the locomotive power.
     *
     * @param power power
     */
    public void setPower(int power) {
        this.power = power;
    }

    /**
     * Returns the maximum supported weight.
     *
     * @return maximum weight
     */
    public int getMaximumWeight() {
        return maximumWeight;
    }

    /**
     * Sets the maximum supported weight.
     *
     * @param maximumWeight maximum weight
     */
    public void setMaximumWeight(int maximumWeight) {
        this.maximumWeight = maximumWeight;
    }

    /**
     * Returns the acceleration capability.
     *
     * @return acceleration
     */
    public double getAcceleration() {
        return acceleration;
    }

    /**
     * Sets the acceleration capability.
     *
     * @param acceleration acceleration
     */
    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * Returns the number of wheels.
     *
     * @return number of wheels
     */
    public int getNumberWheels() {
        return numberWheels;
    }

    /**
     * Sets the number of wheels.
     *
     * @param numberWheels number of wheels
     */
    public void setNumberWheels(int numberWheels) {
        this.numberWheels = numberWheels;
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
     * Returns the operational speed.
     *
     * @return operational speed
     */
    public double getOperationalSpeed() {
        return operationalSpeed;
    }

    /**
     * Sets the operational speed.
     *
     * @param operationalSpeed operational speed
     */
    public void setOperationalSpeed(double operationalSpeed) {
        this.operationalSpeed = operationalSpeed;
    }

    /**
     * Returns the traction value.
     *
     * @return traction
     */
    public int getTraction() {
        return traction;
    }

    /**
     * Sets the traction value.
     *
     * @param traction traction
     */
    public void setTraction(int traction) {
        this.traction = traction;
    }

    /**
     * Returns the manufacturer.
     *
     * @return maker
     */
    public Maker getMaker() {
        return maker;
    }

    /**
     * Sets the manufacturer.
     *
     * @param maker manufacturer
     */
    public void setMaker(Maker maker) {
        this.maker = maker;
    }

    /**
     * Returns the dimensions of the locomotive.
     *
     * @return dimensions
     */
    public Dimensions getDimensions() {
        return dimensions;
    }

    /**
     * Sets the dimensions of the locomotive.
     *
     * @param dimensions dimensions
     */
    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * Returns the fuel type.
     *
     * @return fuel type
     */
    public FuelType getFuelType() {
        return fuelType;
    }

    /**
     * Sets the fuel type.
     *
     * @param fuelType fuel type
     */
    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    /**
     * Returns the supported track gauges.
     *
     * @return list of track gauges
     */
    public List<TrackGauge> getTrackGauges() {
        return trackGauges;
    }

    /**
     * Sets the supported track gauges.
     *
     * @param trackGauges list of track gauges
     */
    public void setTrackGauges(List<TrackGauge> trackGauges) {
        this.trackGauges = trackGauges;
    }

    /**
     * Returns the fuel capacity.
     *
     * @return fuel capacity
     */
    public int getFuelCapacity() {
        return fuelCapacity;
    }

    /**
     * Sets the fuel capacity.
     *
     * @param fuelCapacity fuel capacity
     */
    public void setFuelCapacity(int fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    /**
     * Returns the voltage used by the locomotive.
     *
     * @return voltage
     */
    public int getVoltage() {
        return voltage;
    }

    /**
     * Sets the voltage used by the locomotive.
     *
     * @param voltage voltage
     */
    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    /**
     * Returns the electrical frequency.
     *
     * @return frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Sets the electrical frequency.
     *
     * @param frequency frequency
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
