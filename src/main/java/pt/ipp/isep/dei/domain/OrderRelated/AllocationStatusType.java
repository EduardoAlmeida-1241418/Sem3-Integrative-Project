package pt.ipp.isep.dei.domain.OrderRelated;

/**
 * Enumeration representing the various stages of the allocation process
 * for an order or box within the system.
 */
public enum AllocationStatusType {
    /** Allocation of items or boxes has been completed. */
    ALLOCATION_DONE,

    /** The picking plan has been generated after allocation. */
    PICKING_PLAN_DONE,

    /** The picking path has been finalized following the plan. */
    PICKING_PATH_DONE
}
