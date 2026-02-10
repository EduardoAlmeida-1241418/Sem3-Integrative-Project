package pt.ipp.isep.dei.domain.trackRelated;

/**
 * Enumeration that represents the electrification type of a railway segment.
 * Each type contains a textual description associated with it.
 */
public enum RailwaySegmentType {

    /** Represents an electrified railway segment */
    ELECTRIC("Electric"),
    /** Represents a non-electrified railway segment */
    NOT_ELECTRIC("Not electric");

    /** Textual description of the segment type */
    private final String description;

    /**
     * Constructs a RailwaySegmentType with the given description.
     *
     * @param description textual description of the segment type
     */
    RailwaySegmentType(String description) {
        this.description = description;
    }

    /**
     * Returns the RailwaySegmentType that matches the given description.
     * The comparison is case-insensitive.
     *
     * @param description textual description of the segment type
     * @return matching RailwaySegmentType
     * @throws IllegalArgumentException if no matching type is found
     */
    public static RailwaySegmentType getByDescription(String description) {
        for (RailwaySegmentType type : values()) {
            if (type.description.equalsIgnoreCase(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
                "Invalid RailwaySegmentType description: " + description
        );
    }

    /**
     * Returns the textual description of the segment type.
     *
     * @return description of the segment type
     */
    @Override
    public String toString() {
        return description;
    }
}
