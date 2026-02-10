package pt.ipp.isep.dei.domain.trackRelated;

/**
 * Interface that represents a generic track location.
 * Implementing classes must provide a human-readable name.
 */
public interface TrackLocation {

    /**
     * Returns the name of the track location.
     *
     * @return location name
     */
    String name();
}
