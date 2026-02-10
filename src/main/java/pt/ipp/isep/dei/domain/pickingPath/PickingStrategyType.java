package pt.ipp.isep.dei.domain.pickingPath;

/**
 * Enumeration representing the available picking strategy types
 * used for generating and organizing picking paths.
 */
public enum PickingStrategyType {

    /** Represents picking strategy A. */
    STRATEGY_A("Strategy A"),

    /** Represents picking strategy B. */
    STRATEGY_B("Stategy B");

    private final String displayName;

    /**
     * Constructs a {@code PickingStrategyType} with a display name.
     *
     * @param displayName the human-readable name of the strategy
     */
    PickingStrategyType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the display name of this picking strategy type
     */
    @Override
    public String toString() {
        return displayName;
    }
}
