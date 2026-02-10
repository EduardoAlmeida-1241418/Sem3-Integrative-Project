package pt.ipp.isep.dei.domain;

/**
 * Represents a product return record.
 * Contains item identification, quantity, reason, and timestamp information related to the return process.
 */
public class Return {

    /** Unique identifier for the return. */
    private String returnId;

    /** Stock Keeping Unit (SKU) of the returned item. */
    private String skuItem;

    /** Quantity of items being returned. */
    private int quantity;

    /** Reason for the return. */
    private ReturnReason returnReason;

    /** Date on which the return was recorded. */
    private Date dateStamp;

    /** Time at which the return was recorded. */
    private Time timeStamp;

    /** Expiry date of the returned product, if applicable. */
    private Date expiryDate;

    /**
     * Constructs a Return instance.
     *
     * @param returnId unique identifier for the return
     * @param skuItem SKU of the returned item
     * @param quantity number of items being returned
     * @param returnReason reason for the return
     * @param dateStamp date of the return
     * @param timeStamp time of the return
     * @param expiryDate expiry date of the item, if applicable
     */
    public Return(String returnId, String skuItem, int quantity, ReturnReason returnReason, Date dateStamp, Time timeStamp, Date expiryDate) {
        this.returnId = returnId;
        this.skuItem = skuItem;
        this.quantity = quantity;
        this.returnReason = returnReason;
        this.dateStamp = dateStamp;
        this.timeStamp = timeStamp;
        this.expiryDate = expiryDate;
    }

    /** @return the return ID */
    public String getReturnId() {
        return returnId;
    }

    /** @param returnId sets the return ID */
    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }

    /** @return the SKU of the returned item */
    public String getSkuItem() {
        return skuItem;
    }

    /** @param skuItem sets the SKU of the returned item */
    public void setSkuItem(String skuItem) {
        this.skuItem = skuItem;
    }

    /** @return the quantity of items returned */
    public int getQuantity() {
        return quantity;
    }

    /** @param quantity sets the quantity of items returned */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /** @return the return reason */
    public ReturnReason getReturnReason() {
        return returnReason;
    }

    /** @param returnReason sets the return reason */
    public void setReturnReason(ReturnReason returnReason) {
        this.returnReason = returnReason;
    }

    /** @return the date when the return was recorded */
    public Date getDateStamp() {
        return dateStamp;
    }

    /** @param dateStamp sets the return date */
    public void setDateStamp(Date dateStamp) {
        this.dateStamp = dateStamp;
    }

    /** @return the time when the return was recorded */
    public Time getTimeStamp() {
        return timeStamp;
    }

    /** @param timeStamp sets the return time */
    public void setTimeStamp(Time timeStamp) {
        this.timeStamp = timeStamp;
    }

    /** @return the expiry date of the returned item */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /** @param expiryDate sets the expiry date of the returned item */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Returns a string representation of the return record.
     *
     * @return formatted string containing return details
     */
    @Override
    public String toString() {
        return returnId + " - " + skuItem + " - " + quantity + " - " + returnReason + " - " + dateStamp + " - " + timeStamp + " - " + expiryDate;
    }
}
