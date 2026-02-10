package pt.ipp.isep.dei.controller.operationsPlanner;

import pt.ipp.isep.dei.domain.ESINF.StationEsinf;

import java.util.List;

/**
 * Controller that holds the data required to display stations on a world
 * map for the operations planner role.
 *
 * <p>This class is a simple data holder used by the UI layer. It stores the
 * list of stations to display and a reference point (latitude and longitude)
 * together with textual information used by the view.
 */
public class OperationsPlannerViewMapaMundiController {
    private List<StationEsinf> stations;

    private double latitudePoint;
    private double longitudePoint;

    private String latitudeInfo;
    private String longitudeInfo;
    private String radiusInfo;

    /**
     * Obtain the list of stations configured for display on the map.
     *
     * @return the list of {@code StationEsinf} instances
     */
    public List<StationEsinf> getStations() {
        return stations;
    }

    /**
     * Set the list of stations to be displayed on the map.
     *
     * @param stations the list of {@code StationEsinf}
     */
    public void setStations(List<StationEsinf> stations) {
        this.stations = stations;
    }

    /**
     * Get the latitude of the map reference point.
     *
     * @return the latitude in decimal degrees
     */
    public double getLatitudePoint() {
        return latitudePoint;
    }

    /**
     * Set the latitude of the map reference point.
     *
     * @param latitudePoint the latitude in decimal degrees
     */
    public void setLatitudePoint(double latitudePoint) {
        this.latitudePoint = latitudePoint;
    }

    /**
     * Get the longitude of the map reference point.
     *
     * @return the longitude in decimal degrees
     */
    public double getLongitudePoint() {
        return longitudePoint;
    }

    /**
     * Set the longitude of the map reference point.
     *
     * @param longitudePoint the longitude in decimal degrees
     */
    public void setLongitudePoint(double longitudePoint) {
        this.longitudePoint = longitudePoint;
    }

    /**
     * Get the textual representation of the latitude to show in the UI.
     *
     * @return a string describing the latitude
     */
    public String getLatitudeInfo() {
        return latitudeInfo;
    }

    /**
     * Set the textual representation of the latitude to be shown in the UI.
     *
     * @param latitudeInfo the latitude description
     */
    public void setLatitudeInfo(String latitudeInfo) {
        this.latitudeInfo = latitudeInfo;
    }

    /**
     * Get the textual representation of the longitude to show in the UI.
     *
     * @return a string describing the longitude
     */
    public String getLongitudeInfo() {
        return longitudeInfo;
    }

    /**
     * Set the textual representation of the longitude to be shown in the UI.
     *
     * @param longitudeInfo the longitude description
     */
    public void setLongitudeInfo(String longitudeInfo) {
        this.longitudeInfo = longitudeInfo;
    }

    /**
     * Get the textual representation of the radius to show in the UI.
     *
     * @return a string describing the radius
     */
    public String getRadiusInfo() {
        return radiusInfo;
    }

    /**
     * Set the textual representation of the radius to be shown in the UI.
     *
     * @param radiusInfo the radius description
     */
    public void setRadiusInfo(String radiusInfo) {
        this.radiusInfo = radiusInfo;
    }
}