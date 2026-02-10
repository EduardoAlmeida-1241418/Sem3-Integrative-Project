package pt.ipp.isep.dei.domain.trolleyRelated;

/**
 * Represents a model of a trolley, defining its unique identifier,
 * name, and maximum weight capacity.
 */
public class TrolleyModel {

    private String name;
    private String id;
    private int maxWeight;
    private static int modelCounter = 0;

    /**
     * Constructs a {@code TrolleyModel} with a specified name and maximum weight.
     * Automatically generates a unique ID for the model.
     *
     * @param name       the name of the trolley model
     * @param maxWeight  the maximum weight capacity of the trolley in kilograms
     */
    public TrolleyModel(String name, int maxWeight) {
        this.name = name;
        this.maxWeight = maxWeight;
        this.id = "TM" + modelCounter;
        modelCounter++;
    }

    /**
     * @return the name of the trolley model
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to assign to this trolley model
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the maximum weight capacity of the trolley model
     */
    public double getMaxWeight() {
        return maxWeight;
    }

    /**
     * @param maxWeight the maximum weight capacity to set
     */
    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    /**
     * @return the unique identifier of this trolley model
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the identifier to assign to this trolley model
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return a string representation of the trolley model, including ID, name, and capacity
     */
    @Override
    public String toString() {
        return "TrolleyModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", maxWeight=" + maxWeight +
                '}';
    }
}
