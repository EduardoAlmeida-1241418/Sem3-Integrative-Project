package pt.ipp.isep.dei.domain.pickingPlanRelated.spacePolicyRelated;

/**
 * Enumeration representing different space management policies
 * applied during picking plan generation.
 */
public enum SpacePolicyType {

    /** Split the contents across multiple locations or containers if needed. */
    SPLIT("Split"),

    /** Defer allocation or action until sufficient space becomes available. */
    DEFER("Defer"),

    /** Automatically decide the best space policy based on conditions. */
    AUTOMATIC("Automatic");

    private final String name;

    /**
     * Constructs a {@code SpacePolicyType} with the given display name.
     *
     * @param name the display name of the policy
     */
    SpacePolicyType(String name) {
        this.name = name;
    }

    /**
     * Retrieves a {@code SpacePolicyType} by its display name (case-insensitive).
     *
     * @param spacePolicyName the name of the space policy to find
     * @return the matching {@code SpacePolicyType}, or {@code null} if not found
     */
    public static SpacePolicyType getByName(String spacePolicyName) {
        for (SpacePolicyType type : values()) {
            if (type.getName().equalsIgnoreCase(spacePolicyName)) {
                return type;
            }
        }
        return null;
    }

    /**
     * @return the display name of this space policy type
     */
    public String getName() {
        return name;
    }

    /**
     * @return the string representation of the policy (its display name)
     */
    @Override
    public String toString() {
        return name;
    }
}
