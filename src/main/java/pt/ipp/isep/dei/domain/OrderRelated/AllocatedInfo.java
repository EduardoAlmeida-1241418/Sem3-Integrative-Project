package pt.ipp.isep.dei.domain.OrderRelated;

/**
 * Represents allocation information related to a specific box,
 * including the allocated quantity and current allocation status.
 */
public class AllocatedInfo {
    private String boxID;
    private int allocatedQuantity;
    private AllocationStatusType allocationState;

    /**
     * Constructs an {@code AllocatedInfo} instance with a box ID and allocated quantity.
     * The allocation state is initialized as {@link AllocationStatusType#ALLOCATION_DONE}.
     *
     * @param boxID             the identifier of the allocated box
     * @param allocatedQuantity the quantity allocated from the box
     */
    public AllocatedInfo(String boxID, int allocatedQuantity) {
        this.boxID = boxID;
        this.allocatedQuantity = allocatedQuantity;
        allocationState = AllocationStatusType.ALLOCATION_DONE;
    }

    /**
     * @return the identifier of the allocated box
     */
    public String getBoxID() {
        return boxID;
    }

    /**
     * @param boxID the box identifier to set
     */
    public void setBoxID(String boxID) {
        this.boxID = boxID;
    }

    /**
     * @return the allocated quantity for this box
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
     * @return the current allocation status
     */
    public AllocationStatusType getAllocationState() {
        return allocationState;
    }

    /**
     * @param allocationState the allocation status to set
     */
    public void setAllocationState(AllocationStatusType allocationState) {
        this.allocationState = allocationState;
    }
}
