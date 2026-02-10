package pt.ipp.isep.dei.domain;

/**
 * Represents a directed connection (edge) between facilities in a logistics or warehouse network.
 * Each edge connects to a destination facility and has an associated distance.
 */
public class Edge {

    private Facility destination;
    private double distanceMeters;

    /**
     * Constructs an {@code Edge} with a specified destination and distance.
     *
     * @param destination    the destination {@link Facility} this edge leads to
     * @param distanceMeters the distance in meters between the source and destination
     */
    public Edge(Facility destination, double distanceMeters) {
        this.destination = destination;
        this.distanceMeters = distanceMeters;
    }

    /**
     * @return the destination facility of this edge
     */
    public Facility getDestination() {
        return destination;
    }

    /**
     * @param destination the destination facility to set for this edge
     */
    public void setDestination(Facility destination) {
        this.destination = destination;
    }

    /**
     * @return the distance between facilities in meters
     */
    public double getDistanceMeters() {
        return distanceMeters;
    }

    /**
     * @param distanceMeters the distance in meters to set for this edge
     */
    public void setDistanceMeters(double distanceMeters) {
        this.distanceMeters = distanceMeters;
    }
}
