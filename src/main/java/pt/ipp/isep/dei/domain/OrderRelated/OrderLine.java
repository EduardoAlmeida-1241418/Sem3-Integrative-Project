package pt.ipp.isep.dei.domain.OrderRelated;

import pt.ipp.isep.dei.comparator.FefoBoxComparator;
import pt.ipp.isep.dei.domain.BoxRelated.Box;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a single order line within an order.
 * Contains information about SKU, quantity, allocation, eligible boxes, and allocation states.
 */
public class OrderLine {
    private String orderLineId;
    private String orderId;
    private int lineNumber;
    private String skuItem;
    private int quantity;

    private LineStatus realStatus;
    private LineState possibleState;

    private Map<String, AllocatedInfo> allocatedInfoList = new TreeMap<>();
    private Map<String, Box> possibleBox = new TreeMap<>(new FefoBoxComparator());

    /**
     * Constructs an {@code OrderLine} instance.
     * Initializes the {@link LineStatus} and {@link LineState} as {@link LineState#UNDISPATCHABLE}.
     *
     * @param orderId    the ID of the order to which this line belongs
     * @param lineNumber the number identifying this line within the order
     * @param skuItem    the SKU of the item being ordered
     * @param quantity   the total quantity requested for this SKU
     */
    public OrderLine(String orderId, int lineNumber, String skuItem, int quantity) {
        this.orderId = orderId;
        this.lineNumber = lineNumber;
        this.skuItem = skuItem;
        this.quantity = quantity;
        this.orderLineId = orderId + "L" + lineNumber;
        this.realStatus = new LineStatus(skuItem, quantity, LineState.UNDISPATCHABLE);
        this.possibleState = LineState.UNDISPATCHABLE;
    }

    /** @return the unique identifier of this order line */
    public String getOrderLineId() { return orderLineId; }

    /** @return the order identifier associated with this line */
    public String getOrderId() { return orderId; }

    /**
     * Sets the order ID and updates the order line ID accordingly.
     * @param orderId the new order ID
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
        this.orderLineId = "O" + orderId + "L" + lineNumber;
    }

    /** @return the order line number */
    public int getLineNumber() { return lineNumber; }

    /**
     * Sets the line number and updates the order line ID accordingly.
     * @param lineNumber the line number to set
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
        this.orderLineId = "O" + orderId + "L" + lineNumber;
    }

    /** @return the SKU item for this order line */
    public String getSkuItem() { return skuItem; }

    /** @param skuItem the SKU item to set */
    public void setSkuItem(String skuItem) { this.skuItem = skuItem; }

    /** @return the quantity requested for this SKU */
    public int getQuantity() { return quantity; }

    /** @param quantity the quantity to set */
    public void setQuantity(int quantity) { this.quantity = quantity; }

    /** @return the real (actual) status of this order line */
    public LineStatus getRealStatus() { return realStatus; }

    /** @return the possible (forecasted) state of this order line */
    public LineState getPossibleState() { return possibleState; }

    /** @return the list of allocations associated with this order line */
    public Map<String, AllocatedInfo> getAllocatedInfoList() { return allocatedInfoList; }

    /** @param allocatedInfoList the allocation information map to set */
    public void setAllocatedInfoList(Map<String, AllocatedInfo> allocatedInfoList) { this.allocatedInfoList = allocatedInfoList; }

    /** @return a map of possible boxes eligible for this line */
    public Map<String, Box> getPossibleBox() { return possibleBox; }

    /** @param possibleBox the map of possible boxes to set */
    public void setPossibleBox(Map<String, Box> possibleBox) { this.possibleBox = possibleBox; }

    /**
     * Adds an allocated box to the allocation list.
     *
     * @param boxId    the box identifier
     * @param quantity the quantity allocated from that box
     */
    public void addAllocatedBox(String boxId, int quantity) {
        this.allocatedInfoList.put(boxId, new AllocatedInfo(boxId, quantity));
    }

    /**
     * Filters allocation information by allocation state.
     *
     * @param allocationState the state to filter by
     * @return a map of box IDs to {@link AllocatedInfo} entries matching the given state
     */
    public Map<String, AllocatedInfo> getAllocatedInfoByState(AllocationStatusType allocationState) {
        Map<String, AllocatedInfo> filteredMap = new HashMap<>();
        for (Map.Entry<String, AllocatedInfo> entry : this.allocatedInfoList.entrySet()) {
            if (entry.getValue().getAllocationState() == allocationState) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }

    /**
     * @return the quantity still missing (requested minus allocated)
     */
    public int getMissingQuantity() {
        return quantity - getAllocatedQuantity();
    }

    /**
     * @return the total quantity currently allocated
     */
    public int getAllocatedQuantity() {
        int allocatedQuantity = 0;
        for (AllocatedInfo allocatedInfo : allocatedInfoList.values()) {
            allocatedQuantity += allocatedInfo.getAllocatedQuantity();
        }
        return allocatedQuantity;
    }

    /**
     * @return the total quantity available from eligible boxes
     */
    public int getEligibleQuantity() {
        int eligible = 0;
        for (Box box : possibleBox.values()) {
            eligible += box.getFreeQuantity();
        }
        return eligible;
    }

    /**
     * Updates both the real and possible state of the order line
     * based on allocated, missing, and eligible quantities.
     */
    public void updateStates() {
        int allocatedQuantity = getAllocatedQuantity();
        int missingQuantity = quantity - allocatedQuantity;
        int eligibleQuantity = getEligibleQuantity();

        if (allocatedQuantity == 0) {
            realStatus.setState(LineState.UNDISPATCHABLE);
        } else if (allocatedQuantity < quantity) {
            realStatus.setState(LineState.PARTIAL);
        } else {
            realStatus.setState(LineState.ALLOCATED);
        }

        if (missingQuantity == 0) {
            possibleState = LineState.ALLOCATED;
        } else if (eligibleQuantity >= missingQuantity) {
            possibleState = LineState.ELIGIBLE;
        } else if (eligibleQuantity == 0 && allocatedQuantity == 0) {
            possibleState = LineState.UNDISPATCHABLE;
        } else {
            possibleState = LineState.PARTIAL;
        }
    }

    /**
     * @return the count of unique boxes that have completed allocation
     */
    public int getAllocatedBoxCount() {
        return (int) getAllocatedInfoByState(AllocationStatusType.ALLOCATION_DONE).values().stream()
                .map(AllocatedInfo::getBoxID)
                .distinct()
                .count();
    }

    /**
     * @return a string representation of this order line, including ID, SKU, quantity, and states
     */
    @Override
    public String toString() {
        return orderLineId + " - SKU: " + skuItem + " - Qty: " + quantity +
                " | Real: " + realStatus.getState() +
                " | Possible: " + possibleState;
    }

    /**
     * Checks if any allocated entry matches a given allocation state.
     *
     * @param allocationState the allocation state to check
     * @return true if at least one allocation matches the state, false otherwise
     */
    public boolean containsAllocatedOrderByState(AllocationStatusType allocationState) {
        for (AllocatedInfo allocatedInfo : allocatedInfoList.values()) {
            if (allocatedInfo.getAllocationState() == allocationState) {
                return true;
            }
        }
        return false;
    }
}
