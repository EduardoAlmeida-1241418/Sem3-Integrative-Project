package pt.ipp.isep.dei.domain.trackRelated;

/**
 * Represents a track gauge.
 * A track gauge defines the distance between the rails in a railway track.
 */
public class TrackGauge {

    /** Unique identifier of the track gauge */
    private int id;
    /** Size of the track gauge */
    private int gaugeSize;

    /**
     * Constructs a track gauge with the given parameters.
     *
     * @param id track gauge identifier
     * @param gaugeSize size of the track gauge
     */
    public TrackGauge(int id, int gaugeSize) {
        this.id = id;
        this.gaugeSize = gaugeSize;
    }

    /**
     * Returns the track gauge identifier.
     *
     * @return track gauge id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the track gauge identifier.
     *
     * @param id new track gauge id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the size of the track gauge.
     *
     * @return gauge size
     */
    public int getGaugeSize() {
        return gaugeSize;
    }

    /**
     * Sets the size of the track gauge.
     *
     * @param gaugeSize gauge size
     */
    public void setGaugeSize(int gaugeSize) {
        this.gaugeSize = gaugeSize;
    }

    /**
     * Returns the string representation of the track gauge.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "TrackGauge{" +
                "id=" + id +
                ", gaugeSize=" + gaugeSize +
                '}';
    }
}
