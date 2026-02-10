package pt.ipp.isep.dei.domain;

/**
 * Represents physical dimensions.
 * This class stores dimensional and weight information,
 * such as length, width, height and tare weight.
 */
public class Dimensions {

    /** Unique identifier of the dimensions */
    private int id;
    /** Length value */
    private double length;
    /** Width value */
    private double width;
    /** Height value */
    private double height;
    /** Tare weight */
    private double weightTare;

    /**
     * Constructs a Dimensions object with all attributes.
     *
     * @param id dimensions identifier
     * @param length length value
     * @param width width value
     * @param height height value
     * @param weightTare tare weight
     */
    public Dimensions(int id, double length, double width, double height, double weightTare) {
        this.id = id;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weightTare = weightTare;
    }

    /**
     * Returns the dimensions identifier.
     *
     * @return dimensions id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the dimensions identifier.
     *
     * @param id dimensions id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the length value.
     *
     * @return length
     */
    public double getLength() {
        return length;
    }

    /**
     * Sets the length value.
     *
     * @param length length value
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * Returns the width value.
     *
     * @return width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets the width value.
     *
     * @param width width value
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Returns the height value.
     *
     * @return height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the height value.
     *
     * @param height height value
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Returns the tare weight.
     *
     * @return tare weight
     */
    public double getWeightTare() {
        return weightTare;
    }

    /**
     * Sets the tare weight.
     *
     * @param weightTare tare weight
     */
    public void setWeightTare(double weightTare) {
        this.weightTare = weightTare;
    }
}
