package pt.ipp.isep.dei.domain;

/**
 * Represents the association between a facility and its position in a path.
 * This class is used to define the order of facilities within a path.
 */
public class PathFacility {

    /** Identifier of the facility */
    private int facilityId;
    /** Position of the facility in the path */
    private int position;

    /**
     * Constructs a PathFacility with a facility identifier and position.
     *
     * @param facilityId facility identifier
     * @param position position in the path
     */
    public PathFacility(int facilityId, int position) {
        this.facilityId = facilityId;
        this.position = position;
    }

    /**
     * Returns the facility identifier.
     *
     * @return facility id
     */
    public int getFacilityId() {
        return facilityId;
    }

    /**
     * Returns the position of the facility in the path.
     *
     * @return position
     */
    public int getPosition() {
        return position;
    }
}
