package pt.ipp.isep.dei.controller.analyst;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.data.repository.sprint2.StationEsinf2Repository;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.TimeZone;
import pt.ipp.isep.dei.domain.Tree.KDTree.KD2TreeStation;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing proximity searches for analyst users.
 *
 * <p>This controller encapsulates the logic for configuring and executing
 * proximity searches using a KD-tree spatial index. It provides methods to
 * set search parameters such as location, number of neighbours, and time
 * zone, and to retrieve the search results.
 */
public class AnalystProximitySearchController {
    private StationEsinf2Repository stationEsinfSprint2Repository;

    private KD2TreeStation kd2TreeStation;

    private List<StationEsinf> kd2TreeNodes;

    private double searchLatitude;
    private double searchLongitude;

    private int kNearest;

    private TimeZone timeZone;

    /**
     * Initialise a new AnalystProximitySearchController.
     *
     * <p>The constructor obtains the shared StationEsinfRepository from the
     * application repositories and retrieves its KD-tree for subsequent
     * proximity queries.
     */
    public AnalystProximitySearchController() {
        this.stationEsinfSprint2Repository = Repositories.getInstance().getStationEsinf2Repository();
        this.kd2TreeStation = stationEsinfSprint2Repository.getKdTree();
    }

    /**
     * Return the current KD-tree station nodes as an observable list suitable
     * for use by JavaFX UI components.
     *
     * @return an ObservableList containing the stations currently stored in
     *         the controller's KD-tree result list
     */
    public ObservableList<StationEsinf> getKD2TreeStationNodes() {
        return FXCollections.observableArrayList(kd2TreeNodes);
    }

    /**
     * Return a list of time zone labels for presentation in the UI.
     *
     * <p>The returned list begins with the option "All Time Zones" followed
     * by the names provided by {@code TimeZone.getAllTimeZones()}.
     *
     * @return an ObservableList of time zone names
     */
    public ObservableList<String> getAllTimeZones() {
        List<String> timeZones = new ArrayList<>();
        timeZones.add("All Time Zones");
        timeZones.addAll(TimeZone.getAllTimeZones());

        return FXCollections.observableArrayList(timeZones);
    }

    /**
     * Get the search latitude currently set in the controller.
     *
     * @return the latitude used for proximity searches
     */
    public double getSearchLatitude() {
        return searchLatitude;
    }

    /**
     * Set the search latitude to be used for subsequent proximity queries.
     *
     * @param searchLatitude the latitude in decimal degrees
     */
    public void setSearchLatitude(double searchLatitude) {
        this.searchLatitude = searchLatitude;
    }

    /**
     * Get the search longitude currently set in the controller.
     *
     * @return the longitude used for proximity searches
     */
    public double getSearchLongitude() {
        return searchLongitude;
    }

    /**
     * Set the search longitude to be used for subsequent proximity queries.
     *
     * @param searchLongitude the longitude in decimal degrees
     */
    public void setSearchLongitude(double searchLongitude) {
        this.searchLongitude = searchLongitude;
    }

    /**
     * Get the configured number of nearest neighbours to retrieve.
     *
     * @return the number of nearest stations to request
     */
    public int getkNearest() {
        return kNearest;
    }

    /**
     * Set the number of nearest neighbours to retrieve in searches.
     *
     * @param kNearest the number of nearest stations to request
     */
    public void setkNearest(int kNearest) {
        this.kNearest = kNearest;
    }

    /**
     * Get the currently selected time zone filter.
     *
     * @return the TimeZone filter (may be null to indicate all time zones)
     */
    public TimeZone getTimeZone() {
        return timeZone;
    }

    /**
     * Set the time zone filter to restrict proximity results.
     *
     * @param timeZone the TimeZone to filter by, or null for no filter
     */
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * Return the raw list of KD-tree result stations held by the controller.
     *
     * @return the list of StationEsinf objects resulting from the last
     *         proximity query
     */
    public List<StationEsinf> getKd2TreeNodes() {
        return kd2TreeNodes;
    }

    /**
     * Execute the KD-tree nearest-neighbour search using the controller's
     * current search parameters and store the result in the controller's
     * internal list.
     *
     * <p>This method delegates to {@code KD2TreeStation.findKNearestStations}
     * and does not perform validation of the parameters.
     */
    public void setKd2TreeNodes() {
        this.kd2TreeNodes = kd2TreeStation.findKNearestStations(searchLatitude, searchLongitude, kNearest, timeZone);
    }
}