package pt.ipp.isep.dei.domain.OrderRelated;

/**
 * Represents the status of a specific line within an order,
 * including SKU identification, requested and allocated quantities,
 * and the current processing state.
 */
public class LineStatus {
    private String sku;
    private int requestedQty;
    private int allocatedQty;
    private LineState state;

    /**
     * Constructs a {@code LineStatus} with the specified SKU, requested quantity, and initial state.
     * The allocated quantity is initialized to zero.
     *
     * @param sku          the SKU identifier of the product
     * @param requestedQty the quantity requested for this line
     * @param state        the current {@link LineState} of the line
     */
    public LineStatus(String sku, int requestedQty, LineState state) {
        this.sku = sku;
        this.requestedQty = requestedQty;
        this.allocatedQty = 0;
        this.state = state;
    }

    /**
     * @return the SKU identifier of the line
     */
    public String getSku() {
        return sku;
    }

    /**
     * @param sku the SKU identifier to set
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * @return the requested quantity for this line
     */
    public int getRequestedQty() {
        return requestedQty;
    }

    /**
     * @param requestedQty the requested quantity to set
     */
    public void setRequestedQty(int requestedQty) {
        this.requestedQty = requestedQty;
    }

    /**
     * @return the quantity currently allocated to this line
     */
    public int getAllocatedQty() {
        return allocatedQty;
    }

    /**
     * @param allocatedQty the allocated quantity to set
     */
    public void setAllocatedQty(int allocatedQty) {
        this.allocatedQty = allocatedQty;
    }

    /**
     * @return the current state of this order line
     */
    public LineState getState() {
        return state;
    }

    /**
     * @param state the {@link LineState} to set
     */
    public void setState(LineState state) {
        this.state = state;
    }
}
