package pt.ipp.isep.dei.domain.trackRelated;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a railway line segment.
 * A segment contains information about electrification, weight limits,
 * length, number of tracks, speed limit, track gauge and possible siding.
 */
public class RailwayLineSegment implements SegmentRelated{

    /** Unique identifier of the railway line segment */
    private int id;
    /** Type of electrification of the segment */
    private RailwaySegmentType electrifiedLine;
    /** Maximum supported weight on this segment */
    private int maximumWeight;
    /** Length of the segment */
    private int length;
    /** Number of tracks available in this segment */
    private int numberTracks;
    /** Maximum speed allowed in this segment */
    private int speedLimit;
    /** Identifier of the track gauge */
    private int trackGaugeId;

    /** List of railway lines associated with this segment */
    private List<RailwayLine> railwayLines;

    /** Optional siding associated with this segment */
    private Siding siding;

    /**
     * Full constructor for a railway line segment.
     *
     * @param id segment identifier
     * @param electrifiedLine indicates if the segment is electrified
     * @param maximumWeight maximum allowed weight
     * @param length length of the segment
     * @param numberTracks number of tracks
     * @param speedLimit speed limit
     * @param trackGaugeId track gauge identifier
     * @param siding siding associated with the segment
     */
    public RailwayLineSegment(int id, boolean electrifiedLine, int maximumWeight, int length, int numberTracks, int speedLimit, int trackGaugeId, Siding siding) {
        this.id = id;

        if(electrifiedLine){
            this.electrifiedLine = RailwaySegmentType.ELECTRIC;
        } else {
            this.electrifiedLine = RailwaySegmentType.NOT_ELECTRIC;
        }

        this.maximumWeight = maximumWeight;
        this.length = length;
        this.numberTracks = numberTracks;
        this.speedLimit = speedLimit;
        this.trackGaugeId = trackGaugeId;
        this.railwayLines = new ArrayList<>();

        this.siding = siding;
    }

    /**
     * Constructor for a railway line segment with default number of tracks.
     *
     * @param electrifiedLine indicates if the segment is electrified
     * @param maximumWeight maximum allowed weight
     * @param length length of the segment
     * @param speedLimit speed limit
     * @param trackGaugeId track gauge identifier
     */
    public RailwayLineSegment(boolean electrifiedLine, int maximumWeight, int length, int speedLimit, int trackGaugeId) {
        if(electrifiedLine){
            this.electrifiedLine = RailwaySegmentType.ELECTRIC;
        } else {
            this.electrifiedLine = RailwaySegmentType.NOT_ELECTRIC;
        }

        this.maximumWeight = maximumWeight;
        this.length = length;
        this.numberTracks = 2;
        this.speedLimit = speedLimit;
        this.trackGaugeId = trackGaugeId;
        this.railwayLines = new ArrayList<>();
    }

    /**
     * Constructor without siding.
     *
     * @param id segment identifier
     * @param electrifiedLine indicates if the segment is electrified
     * @param maximumWeight maximum allowed weight
     * @param length length of the segment
     * @param numberTracks number of tracks
     * @param speedLimit speed limit
     * @param trackGaugeId track gauge identifier
     */
    public RailwayLineSegment(int id, boolean electrifiedLine, int maximumWeight, int length, int numberTracks, int speedLimit, int trackGaugeId) {
        this(id, electrifiedLine, maximumWeight, length, numberTracks, speedLimit, trackGaugeId, null);
    }

    /**
     * Constructor used when a segment is divided.
     *
     * @param segmentLineDivided original divided segment
     * @param lenght length of the new segment
     */
    public RailwayLineSegment(SegmentLineDivided segmentLineDivided, int lenght) {
        this.electrifiedLine = segmentLineDivided.getOwnerSegment().getElectrifiedLine();
        this.maximumWeight = segmentLineDivided.getOwnerSegment().getMaximumWeight();
        this.length = lenght;
        this.numberTracks = segmentLineDivided.getOwnerSegment().getNumberTracks();
        this.speedLimit = segmentLineDivided.getOwnerSegment().getSpeedLimit();
        this.trackGaugeId = segmentLineDivided.getOwnerSegment().getTrackGaugeId();
    }

    /**
     * Adds a railway line to this segment.
     *
     * @param railwayLine railway line to add
     */
    public void addRailwayLine(RailwayLine railwayLine) {
        this.railwayLines.add(railwayLine);
    }

    /**
     * Returns the segment identifier.
     *
     * @return segment id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the segment identifier.
     *
     * @param id new segment id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Checks if the segment is electrified.
     *
     * @return true if electrified, false otherwise
     */
    public boolean isElectrifiedLine() {
        if (electrifiedLine == RailwaySegmentType.NOT_ELECTRIC){
            return false;
        }
        return true;
    }

    /**
     * Sets the electrification state using a boolean.
     *
     * @param electrifiedLine true if electrified
     */
    public void setElectrifiedLine(boolean electrifiedLine) {
        if (electrifiedLine){
            this.electrifiedLine = RailwaySegmentType.ELECTRIC;
            return;
        }

        this.electrifiedLine = RailwaySegmentType.NOT_ELECTRIC;

    }

    /**
     * Returns the electrification type.
     *
     * @return electrification type
     */
    public RailwaySegmentType getElectrifiedLine() {
        return electrifiedLine;
    }

    /**
     * Sets the electrification type.
     *
     * @param electrifiedLine electrification type
     */
    public void setElectrifiedLine(RailwaySegmentType electrifiedLine) {
        this.electrifiedLine = electrifiedLine;
    }

    /**
     * Returns the maximum allowed weight.
     *
     * @return maximum weight
     */
    public int getMaximumWeight() {
        return maximumWeight;
    }

    /**
     * Sets the maximum allowed weight.
     *
     * @param maximumWeight maximum weight
     */
    public void setMaximumWeight(int maximumWeight) {
        this.maximumWeight = maximumWeight;
    }

    /**
     * Returns the segment length.
     *
     * @return length
     */
    @Override
    public int getLength() {
        return length;
    }

    /**
     * Sets the segment length.
     *
     * @param length segment length
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Returns the number of tracks.
     *
     * @return number of tracks
     */
    @Override
    public int getNumberTracks() {
        return numberTracks;
    }

    /**
     * Sets the number of tracks.
     *
     * @param numberTracks number of tracks
     */
    public void setNumberTracks(int numberTracks) {
        this.numberTracks = numberTracks;
    }

    /**
     * Returns the speed limit.
     *
     * @return speed limit
     */
    @Override
    public int getSpeedLimit() {
        return speedLimit;
    }

    /**
     * Sets the speed limit.
     *
     * @param speedLimit speed limit
     */
    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    /**
     * Returns the track gauge identifier.
     *
     * @return track gauge id
     */
    public int getTrackGaugeId() {
        return trackGaugeId;
    }

    /**
     * Sets the track gauge identifier.
     *
     * @param trackGaugeId track gauge id
     */
    public void setTrackGaugeId(int trackGaugeId) {
        this.trackGaugeId = trackGaugeId;
    }

    /**
     * Returns the list of railway lines.
     *
     * @return list of railway lines
     */
    public List<RailwayLine> getRailwayLines() {
        return railwayLines;
    }

    /**
     * Sets the list of railway lines.
     *
     * @param railwayLines list of railway lines
     */
    public void setRailwayLines(List<RailwayLine> railwayLines) {
        this.railwayLines = railwayLines;
    }

    /**
     * Returns the siding associated with this segment.
     *
     * @return siding
     */
    public Siding getSiding() {
        return siding;
    }

    /**
     * Sets the siding for this segment.
     *
     * @param siding siding
     */
    public void setSiding(Siding siding) {
        this.siding = siding;
    }

    /**
     * Checks if this segment has a siding.
     *
     * @return true if a siding exists
     */
    public boolean hasSiding(){
        return siding != null;
    }

    /**
     * Returns the distance to the siding depending on direction.
     *
     * @param isForward direction flag
     * @return distance to siding or -1 if none exists
     */
    public int getDistanceToSiding(boolean isForward){
        if (siding == null) return -1;

        if (isForward){
            return siding.getPosition();
        }

        return length - siding.getPosition();
    }

    /**
     * Returns the segment name.
     *
     * @return segment name
     */
    @Override
    public String name() {
        return "Segment: " + this.id + "";
    }

    /**
     * Returns the string representation of the segment.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "Line Segment: " + id;
    }
}
