package pt.ipp.isep.dei.controller.planner;

import pt.ipp.isep.dei.domain.ESINF.StationEsinf;

import java.util.List;

/**
 * Controller that holds the stations to be displayed on the planner's world
 * map view.
 *
 * <p>This class is a simple data holder used by the UI layer. It exposes
 * getters and setters for the list of {@code StationEsinf} instances that
 * the map view should render.
 */
public class PlannerViewMapaMundiController {
    private List<StationEsinf> stations;

    /**
     * Obtain the list of stations configured for display on the map.
     *
     * @return the list of {@code StationEsinf} to be shown
     */
    public List<StationEsinf> getStations() {
        return stations;
    }

    /**
     * Set the stations that should be displayed on the planner's map view.
     *
     * @param stations the list of {@code StationEsinf} to display
     */
    public void setStations(List<StationEsinf> stations) {
        this.stations = stations;
    }
}