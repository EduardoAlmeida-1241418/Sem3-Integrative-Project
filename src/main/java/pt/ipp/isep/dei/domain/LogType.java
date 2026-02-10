package pt.ipp.isep.dei.domain;

/**
 * Enumeration representing different types of system logs.
 * Each constant identifies a specific category of log event.
 */
public enum LogType {

    /** Informational messages for general system events. */
    INFO,

    /** Warning messages indicating potential issues or unusual conditions. */
    WARNING,

    /** Error messages indicating system or process failures. */
    ERROR,

    /** Information related to locomotive or resource allocation processes. */
    INFO_ALLOCATION,

    /** Logs associated with picking plan generation or execution. */
    PICKING_PLAN,

    /** Logs related to inspection operations or quality checks. */
    INSPECTION,

    /** Logs concerning the picking path or routing activities. */
    PIKING_PATH
}
