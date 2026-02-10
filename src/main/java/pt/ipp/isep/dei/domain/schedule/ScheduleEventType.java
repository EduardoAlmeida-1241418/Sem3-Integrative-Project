package pt.ipp.isep.dei.domain.schedule;

/**
 * Represents the type of an event in a schedule.
 * Each type describes a specific operational state or activity.
 */
public enum ScheduleEventType {

    /**
     * Movement of a train or vehicle between two locations.
     */
    MOVEMENT("Movement between\nlocations"),

    /**
     * Train stopped in a siding, usually waiting for clearance or priority.
     */
    WAITING_IN_SIDING("Waiting in\nsiding"),

    /**
     * Train stopped at a station without performing load or unload operations.
     */
    WAITING_IN_STATION("Waiting at\nstation"),

    /**
     * Train waiting to be assembled, coupled, or prepared.
     */
    WAITING_FOR_ASSEMBLE("Waiting for\nassemble"),

    /**
     * Loading operation of cargo or passengers.
     */
    LOAD("Loading operation"),

    /**
     * Unloading operation of cargo or passengers.
     */
    UNLOAD("Unloading operation"),

    WAITING("Waiting"),

    MOVEMENT_TO_SEGMENT("Moving to\nSegment"),

    MOVEMENT_IN_SEGMENT("Moving in\nSegment"),

    MOVEMENT_IN_SIDING("Moving in\nSiding"),

    IN_PROGRESS("In Progress");

    private final String description;

    ScheduleEventType(String description) {
        this.description = description;
    }

    /**
     * @return human-readable description of the event type
     */
    public String getDescription() {
        return description;
    }
}
