package pt.ipp.isep.dei.domain.BoxRelated;

import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;

import java.util.Objects;

/**
 * Represents a Box that contains a specific SKU item, its quantity, allocation, and related metadata
 * such as expiry, receiving information, and location identifiers (wagon or bay).
 */
public class Box {
    private String boxId;
    private String skuItem;
    private int quantity;
    private int allocatedQuantity;
    private Date expiryDate;
    private Date receivedDate;
    private Time receivedTime;
    private double weight;
    private String wagonId;
    private String bayId;

    /**
     * Constructs a new Box with the specified parameters.
     *
     * @param boxId        the unique identifier of the box
     * @param skuItem      the SKU item contained in the box
     * @param quantity     the quantity of the SKU item in the box
     * @param expiryDate   the expiry date of the items
     * @param receivedDate the date when the box was received
     * @param receivedTime the time when the box was received
     */
    public Box(String boxId, String skuItem, int quantity, Date expiryDate, Date receivedDate, Time receivedTime) {
        this.boxId = boxId;
        this.skuItem = skuItem;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.receivedDate = receivedDate;
        this.receivedTime = receivedTime;
        this.allocatedQuantity = 0;
    }

    /**
     * @return the unique identifier of the box
     */
    public String getBoxId() {
        return boxId;
    }

    /**
     * @param boxId the new unique identifier for the box
     */
    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    /**
     * @return the SKU item stored in this box
     */
    public String getSkuItem() {
        return skuItem;
    }

    /**
     * @param skuItem the SKU item to set
     */
    public void setSkuItem(String skuItem) {
        this.skuItem = skuItem;
    }

    /**
     * @return the total quantity of items in the box
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity of items to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the expiry date of the items, or null if not set
     */
    public Date getExpiryDate() {
        if (expiryDate == null) {
            return null;
        }
        return expiryDate;
    }

    /**
     * @return the expiry date as a string, or "N/A" if not available
     */
    public String getExpiryDateString() {
        if (expiryDate == null) {
            return "N/A";
        }
        return expiryDate.toString();
    }

    /**
     * @param expiryDate the expiry date to set
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * @return the date when the box was received
     */
    public Date getReceivedDate() {
        return receivedDate;
    }

    /**
     * @return a string combining received date and time, or "N/A" if unavailable
     */
    public String getReceivedDateTimeString() {
        if (receivedDate == null) {
            return "N/A";
        }
        if (receivedTime == null) {
            return receivedDate.toString();
        }
        return receivedDate + "\n" + receivedTime;
    }

    /**
     * @param receivedDate the received date to set
     */
    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    /**
     * @return the time when the box was received
     */
    public Time getReceivedTime() {
        return receivedTime;
    }

    /**
     * @param receivedTime the time to set for when the box was received
     */
    public void setReceivedTime(Time receivedTime) {
        this.receivedTime = receivedTime;
    }

    /**
     * @return the wagon ID where the box is stored
     */
    public String getWagonId() {
        return wagonId;
    }

    /**
     * Sets the wagon ID and clears the bay ID.
     *
     * @param wagonId the wagon identifier to assign
     */
    public void setWagonId(String wagonId) {
        this.wagonId = wagonId;
        this.bayId = null;
    }

    /**
     * @return the quantity already allocated from this box
     */
    public int getAllocatedQuantity() {
        return allocatedQuantity;
    }

    /**
     * @param allocatedQuantity the allocated quantity to set
     */
    public void setAllocatedQuantity(int allocatedQuantity) {
        this.allocatedQuantity = allocatedQuantity;
    }

    /**
     * @return the remaining quantity available for allocation
     */
    public int getFreeQuantity() {
        return this.quantity - this.allocatedQuantity;
    }

    /**
     * @return the bay ID where the box is stored
     */
    public String getBayId() {
        return bayId;
    }

    /**
     * Sets the bay ID and clears the wagon ID.
     *
     * @param bayId the bay identifier to assign
     */
    public void setBayId(String bayId) {
        this.bayId = bayId;
        this.wagonId = null;
    }

    /**
     * @return true if the box has zero quantity, false otherwise
     */
    public boolean boxIsEmpty() {
        return this.quantity == 0;
    }

    /**
     * @return the weight of the box
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set for the box
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Compares this box to another for equality based on content and identifiers.
     *
     * @param o the object to compare with
     * @return true if both boxes are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Box box = (Box) o;
        return quantity == box.quantity && Objects.equals(boxId, box.boxId) && Objects.equals(skuItem, box.skuItem) && Objects.equals(expiryDate, box.expiryDate) && Objects.equals(receivedDate, box.receivedDate) && Objects.equals(receivedTime, box.receivedTime) && Objects.equals(wagonId, box.wagonId) && Objects.equals(bayId, box.bayId);
    }

    /**
     * @return a hash code value for this box
     */
    @Override
    public int hashCode() {
        return Objects.hash(boxId, skuItem, quantity, expiryDate, receivedDate, receivedTime, wagonId, bayId);
    }

    /**
     * @return a string representation of the box, including IDs and dates
     */
    @Override
    public String toString() {
        return boxId + " - " + skuItem + " - " + quantity + " - " + expiryDate + " - " + receivedDate + " - " + receivedTime + "\n" +
                "Wagon ID: " + wagonId + " - Bay ID: " + bayId;
    }
}
