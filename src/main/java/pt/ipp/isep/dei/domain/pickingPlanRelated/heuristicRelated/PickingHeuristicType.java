package pt.ipp.isep.dei.domain.pickingPlanRelated.heuristicRelated;

/**
 * Enumeration defining the available heuristic types for picking plan optimization.
 * Each heuristic determines how items are assigned or grouped during the picking process.
 */
public enum PickingHeuristicType {

    /** Assigns items to the first suitable location that fits. */
    FIRST_FIT("First Fit"),

    /** Sorts items in decreasing order before applying the First Fit strategy. */
    FIRST_FIT_DECREASING("First Fit Decreasing"),

    /** Sorts items in decreasing order and assigns to the best possible fit. */
    BEST_FIT_DECREASING("Best Fit Decreasing");

    private final String name;

    /**
     * Constructs a {@code PickingHeuristicType} with the given display name.
     *
     * @param name the display name of the heuristic
     */
    PickingHeuristicType(String name) {
        this.name = name;
    }

    /**
     * Retrieves a {@code PickingHeuristicType} by its display name (case-insensitive).
     *
     * @param heuristicName the name of the heuristic to search for
     * @return the matching {@code PickingHeuristicType}, or {@code null} if none matches
     */
    public static PickingHeuristicType getByName(String heuristicName) {
        for (PickingHeuristicType type : values()) {
            if (type.getName().equalsIgnoreCase(heuristicName)) {
                return type;
            }
        }
        return null;
    }

    /**
     * @return the display name of the heuristic
     */
    public String getName() {
        return name;
    }

    /**
     * @return the string representation of the heuristic (its display name)
     */
    @Override
    public String toString() {
        return name;
    }
}
