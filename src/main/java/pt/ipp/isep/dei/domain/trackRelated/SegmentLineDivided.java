package pt.ipp.isep.dei.domain.trackRelated;

import pt.ipp.isep.dei.data.memory.GeneralScheduleStoreInMemory;

/**
 * Represents a division of a railway line segment.
 * Each divided segment belongs to an original railway line segment
 * and maintains its own order and length.
 */
public class SegmentLineDivided implements SegmentRelated{

    /** Unique identifier of the divided segment */
    private int id;

    /** Order of this division within the original segment */
    private int id_order;
    /** Original railway line segment that owns this division */
    private RailwayLineSegment ownerSegment;
    /** Length of the divided segment */
    private int length;

    /**
     * Constructs a divided segment with a specific identifier.
     * Validates if the identifier already exists.
     *
     * @param id unique identifier
     * @param id_order order of the division
     * @param ownerSegment original railway line segment
     * @param length length of the divided segment
     * @throws IllegalArgumentException if the id already exists
     */
    public SegmentLineDivided(int id, int id_order, RailwayLineSegment ownerSegment, int length) {
        this.id = id;
        if (new GeneralScheduleStoreInMemory().existsSegmentLineDividedWithId(id)) {
            throw new IllegalArgumentException("Segment Line Divided with this ID already exists.");
        }

        this.id_order = id_order;
        this.ownerSegment = ownerSegment;
        this.length = length;
        new GeneralScheduleStoreInMemory().addSegmentLineDividedId(id);
    }

    /**
     * Constructs a divided segment with an automatically generated identifier.
     *
     * @param id_order order of the division
     * @param ownerSegment original railway line segment
     * @param length length of the divided segment
     */
    public SegmentLineDivided(int id_order, RailwayLineSegment ownerSegment, int length) {
        this.id = new GeneralScheduleStoreInMemory().getNextSegmentLineDividedId();
        this.id_order = id_order;
        this.ownerSegment = ownerSegment;
        this.length = length;
        new GeneralScheduleStoreInMemory().addSegmentLineDividedId(id);
    }

    /**
     * Returns the divided segment identifier.
     *
     * @return divided segment id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the divided segment identifier.
     *
     * @param id new identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the order of this division.
     *
     * @return division order
     */
    public int getId_order() {
        return id_order;
    }

    /**
     * Sets the order of this division.
     *
     * @param id_order division order
     */
    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    /**
     * Returns the original railway line segment.
     *
     * @return owner segment
     */
    public RailwayLineSegment getOwnerSegment() {
        return ownerSegment;
    }

    /**
     * Sets the original railway line segment.
     *
     * @param ownerSegment owner segment
     */
    public void setOwnerSegment(RailwayLineSegment ownerSegment) {
        this.ownerSegment = ownerSegment;
    }

    /**
     * Returns the length of the divided segment.
     *
     * @return segment length
     */
    @Override
    public int getLength() {
        return length;
    }

    /**
     * Returns the number of tracks inherited from the owner segment.
     *
     * @return number of tracks
     */
    @Override
    public int getNumberTracks(){
        return ownerSegment.getNumberTracks();
    }

    /**
     * Returns the speed limit inherited from the owner segment.
     *
     * @return speed limit
     */
    @Override
    public int getSpeedLimit(){
        return ownerSegment.getSpeedLimit();
    }

    /**
     * Sets the length of the divided segment.
     *
     * @param length new length
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Returns the name of the divided segment.
     *
     * @return segment name
     */
    @Override
    public String name() {
        return ownerSegment.name() + " Part. " + id_order;
    }

    /**
     * Returns the string representation of the divided segment.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "Segment Line: " + ownerSegment.getId() + "\nDivision: "+ id_order;
    }
}
