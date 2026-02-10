package pt.ipp.isep.dei.domain;

/**
 * Enumeration that represents groups of time zones.
 * Each time zone group has an associated description.
 */
public enum TimeZoneGroup {

    /** Central European Time */
    CET("Central European Time"),
    /** Eastern European Time */
    EET("Eastern European Time"),
    /** Further-eastern European Time */
    FET("Further-eastern European Time"),
    /** Western European Time / Greenwich Mean Time */
    WET_GMT("Western European Time / Greenwich Mean Time");

    /** Description of the time zone group */
    private final String description;

    /**
     * Constructs a time zone group with a description.
     *
     * @param description time zone group description
     */
    TimeZoneGroup(String description) {
        this.description = description;
    }

    /**
     * Returns the description of the time zone group.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }
}
