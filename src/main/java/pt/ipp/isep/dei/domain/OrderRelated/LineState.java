package pt.ipp.isep.dei.domain.OrderRelated;

/**
 * Enumeration representing the state of an order line
 * during the order fulfillment and allocation process.
 */
public enum LineState {

    /** The line is eligible for processing or allocation. */
    ELIGIBLE("Eligible"),

    /** The line is partially processed or allocated. */
    PARTIAL("Partial"),

    /** The line cannot be dispatched due to missing or invalid conditions. */
    UNDISPATCHABLE("Undispatchable"),

    /** The line has been fully allocated. */
    ALLOCATED("Allocated");

    private final String displayName;

    /**
     * Constructs a {@code LineState} with a human-readable display name.
     *
     * @param displayName the name to be displayed for this state
     */
    LineState(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the display name of this line state
     */
    @Override
    public String toString() {
        return displayName;
    }
}
