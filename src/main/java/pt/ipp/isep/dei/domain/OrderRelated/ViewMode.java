package pt.ipp.isep.dei.domain.OrderRelated;

/**
 * Enumeration defining the available view modes
 * for displaying or processing order-related information.
 */
public enum ViewMode {

    /** Displays or processes data in strict mode, enforcing all constraints. */
    STRICT("Strict"),

    /** Displays or processes data in partial mode, allowing incomplete or partial views. */
    PARTIAL("Partial");

    private final String name;

    /**
     * Constructs a {@code ViewMode} with a display name.
     *
     * @param name the name representing this view mode
     */
    ViewMode(String name) {
        this.name = name;
    }

    /**
     * @return the display name of this view mode
     */
    public String getName() {
        return name;
    }
}
