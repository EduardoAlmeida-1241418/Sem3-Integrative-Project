package pt.ipp.isep.dei.controller.analyst;

import pt.ipp.isep.dei.domain.ESINF.StationEsinf;

import java.util.List;

/**
 * Controller that holds the data required to present a world map view of
 * stations for the analyst role.
 *
 * <p>This class is a simple data holder used by the UI layer. It stores the
 * list of stations to display and a single reference point (latitude and
 * longitude) used to centre or highlight a location on the map.
 */
public class AnalystViewMapaMundiController {
    private List<StationEsinf> stations;

    private double latitudePoint;

    private double longitudePoint;

    /**
     * Obtain the list of stations currently configured for display.
     *
     * @return the list of {@code StationEsinf} to be shown on the map
     */
    public List<StationEsinf> getStations() {
        return stations;
    }

    /**
     * Set the list of stations to be displayed on the map.
     *
     * @param stations the list of {@code StationEsinf} to display
     */
    public void setStations(List<StationEsinf> stations) {
        this.stations = stations;
    }

    /**
     * Get the currently configured latitude of the map reference point.
     *
     * @return the reference latitude in decimal degrees
     */
    public double getLatitudePoint() {
        return latitudePoint;
    }

    /**
     * Set the latitude of the reference point used to centre or highlight
     * the map view.
     *
     * @param latitudePoint the latitude in decimal degrees
     */
    public void setLatitudePoint(double latitudePoint) {
        this.latitudePoint = latitudePoint;
    }

    /**
     * Get the currently configured longitude of the map reference point.
     *
     * @return the reference longitude in decimal degrees
     */
    public double getLongitudePoint() {
        return longitudePoint;
    }

    /**
     * Set the longitude of the reference point used to centre or highlight
     * the map view.
     *
     * @param longitudePoint the longitude in decimal degrees
     */
    public void setLongitudePoint(double longitudePoint) {
        this.longitudePoint = longitudePoint;
    }
}