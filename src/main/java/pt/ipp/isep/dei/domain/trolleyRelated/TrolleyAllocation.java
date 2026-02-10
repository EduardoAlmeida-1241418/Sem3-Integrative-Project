package pt.ipp.isep.dei.domain.trolleyRelated;

import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;

/**
 * Represents the allocation of a specific quantity of items from a {@link Box}
 * to a {@link Trolley}, optionally linked to an {@link OrderLine}.
 */
public class TrolleyAllocation {
    private Box box;
    private int quantity;
    private OrderLine orderLine;
    private double itemWeight;

    /**
     * Constructs a {@code TrolleyAllocation} with the specified box, quantity, order line, and item weight.
     *
     * @param box        the box from which items are allocated
     * @param quantity   the number of items allocated
     * @param orderLine  the order line associated with this allocation
     * @param itemWeight the weight of a single item in kilograms
     */
    public TrolleyAllocation(Box box, int quantity, OrderLine orderLine, double itemWeight) {
        this.box = box;
        this.quantity = quantity;
        this.orderLine = orderLine;
        this.itemWeight = itemWeight;
    }

    /**
     * @return the box associated with this allocation
     */
    public Box getBox() {
        return box;
    }

    /**
     * @param box the box to set for this allocation
     */
    public void setBox(Box box) {
        this.box = box;
    }

    /**
     * @return the number of items allocated
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the number of items to set for this allocation
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the order line linked to this allocation, if any
     */
    public OrderLine getOrderLine() {
        return orderLine;
    }

    /**
     * @param orderLine the order line to associate with this allocation
     */
    public void setOrderLine(OrderLine orderLine) {
        this.orderLine = orderLine;
    }

    /**
     * @return the weight of each allocated item
     */
    public double getItemWeight() {
        return itemWeight;
    }

    /**
     * @param itemWeight the weight of each allocated item to set
     */
    public void setItemWeight(double itemWeight) {
        this.itemWeight = itemWeight;
    }
}
