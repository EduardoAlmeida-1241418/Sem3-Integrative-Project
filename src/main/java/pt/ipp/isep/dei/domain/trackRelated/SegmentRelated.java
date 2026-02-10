package pt.ipp.isep.dei.domain.trackRelated;

/**
 * Interface that represents common behavior for elements related to railway segments.
 * Classes implementing this interface must provide information about
 * number of tracks, length and speed limit.
 */
public interface SegmentRelated extends TrackLocation {

    /**
     * Returns the number of tracks associated with the segment.
     *
     * @return number of tracks
     */
    public int getNumberTracks();

    /**
     * Returns the length of the segment.
     *
     * @return segment length
     */
    public int getLength();

    /**
     * Returns the speed limit of the segment.
     *
     * @return speed limit
     */
    public int getSpeedLimit();

}
