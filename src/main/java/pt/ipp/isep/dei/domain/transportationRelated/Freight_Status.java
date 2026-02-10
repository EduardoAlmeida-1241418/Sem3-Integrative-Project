package pt.ipp.isep.dei.domain.transportationRelated;

/**
 * Enumeration that represents the possible states of a freight.
 * A freight can be pending, loaded or delivered.
 */
public enum Freight_Status {

    /** Freight has been created but not yet loaded */
    PENDING,
    /** Freight has been loaded onto a train */
    LOADED,
    /** Freight has been delivered to the destination */
    DELIVERED
}
