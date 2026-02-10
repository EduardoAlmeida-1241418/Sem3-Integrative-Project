package pt.ipp.isep.dei.domain.trolleyRelated;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a physical trolley used in warehouse operations.
 * Each trolley has a model, a current load, and a list of allocations.
 */
public class Trolley {
    private String trolleyId;
    private double currentWeight;
    private TrolleyModel model;
    private List<TrolleyAllocation> trolleyAllocations = new ArrayList<>();

    private static int trolleyCounter = 1;

    /**
     * Constructs a {@code Trolley} with a given model.
     * Automatically generates a unique trolley ID.
     *
     * @param model the model defining capacity and properties of the trolley
     */
    public Trolley(TrolleyModel model) {
        this.trolleyId = model.getId() + "T" + trolleyCounter;
        this.currentWeight = 0.0;
        this.model = model;
        trolleyCounter++;
    }

    /**
     * @return the trolley model
     */
    public TrolleyModel getModel() {
        return model;
    }

    /**
     * @param model the model to set for this trolley
     */
    public void setModel(TrolleyModel model) {
        this.model = model;
    }

    /**
     * @return the unique identifier of this trolley
     */
    public String getTrolleyId() {
        return trolleyId;
    }

    /**
     * @param trolleyId the identifier to assign to this trolley
     */
    public void setTrolleyId(String trolleyId) {
        this.trolleyId = trolleyId;
    }

    /**
     * @return the current total weight loaded on the trolley
     */
    public double getCurrentWeight() {
        return currentWeight;
    }

    /**
     * @param currentWeight the current loaded weight to set
     */
    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    /**
     * @return the list of allocations currently on this trolley
     */
    public List<TrolleyAllocation> getTrolleyAllocations() {
        return trolleyAllocations;
    }

    /**
     * @param trolleyAllocations the list of allocations to set for this trolley
     */
    public void setTrolleyAllocations(List<TrolleyAllocation> trolleyAllocations) {
        this.trolleyAllocations = trolleyAllocations;
    }

    /**
     * Calculates the available weight capacity remaining on the trolley.
     *
     * @return available weight in kilograms
     */
    public double getAvailableWeight() {
        return this.model.getMaxWeight() - this.currentWeight;
    }

    /**
     * Adds a new allocation to the trolley if sufficient space is available.
     * Updates the current total weight.
     *
     * @param allocation the {@link TrolleyAllocation} to add
     */
    public void addTrolleyAllocation(TrolleyAllocation allocation) {
        if (!this.hasSpace(allocation)) {
            return;
        }
        this.trolleyAllocations.add(allocation);
        currentWeight += allocation.getItemWeight() * allocation.getQuantity();
    }

    /**
     * Checks whether this trolley has enough capacity for the given allocation.
     *
     * @param allocation the allocation to test
     * @return true if there is enough space, false otherwise
     */
    public boolean hasSpace(TrolleyAllocation allocation) {
        double totalWeight = this.currentWeight + (allocation.getItemWeight() * allocation.getQuantity());
        return totalWeight <= this.model.getMaxWeight();
    }

    /**
     * Checks whether the trolley has enough capacity for a given item weight.
     *
     * @param weight the weight of the item
     * @return true if the item fits, false otherwise
     */
    public boolean hasSpaceForItem(double weight) {
        return this.model.getMaxWeight() >= this.currentWeight + weight;
    }

    /**
     * @return a string representation of the trolley, including model,
     *         current weight, and detailed allocations
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        double usagePercent = (model.getMaxWeight() > 0) ? (currentWeight / model.getMaxWeight()) * 100 : 0;

        sb.append("Trolley ID: ").append(trolleyId)
                .append(", Model: ").append(model.getId())
                .append(String.format(", Current Weight: %.2f", currentWeight))
                .append(" / ").append(model.getMaxWeight()).append(" kg")
                .append(String.format(" (%.1f%% used)", usagePercent))
                .append("\n");
        if (trolleyAllocations.isEmpty()) {
            sb.append("  No allocations.\n");
        } else {
            sb.append("  Allocations:\n");
            for (TrolleyAllocation allocation : trolleyAllocations) {
                sb.append("    Box: ").append(allocation.getBox().getBoxId())
                        .append(", Quantity: ").append(allocation.getQuantity());
                if (allocation.getOrderLine() != null) {
                    sb.append(", OrderLine: ").append(allocation.getOrderLine().getOrderLineId());
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
