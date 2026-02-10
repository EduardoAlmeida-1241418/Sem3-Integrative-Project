package pt.ipp.isep.dei.controller.operationsPlanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.data.repository.sprint2.StationEsinf2Repository;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Tree.AVL.AVLTreeStation;
import pt.ipp.isep.dei.domain.Tree.KDTree.KD2TreeStation;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller that provides radius-based station search functionality for
 * operations planners.
 *
 * <p>This controller obtains the application's {@code StationEsinfRepository}
 * and exposes methods to compute stations within a Haversine radius, to build
 * an AVL tree ordered by distance and to present results (optionally filtered
 * by country and by whether a station is in a city).
 */
public class OperationsPlannerRadiusSearchController {

    private StationEsinf2Repository stationRepository;

    private AVLTreeStation avlTreeStations;
    private Map<KD2TreeStation.ListStation, Double> stationsWithinHaversineRadius;

    private List<StationEsinf> stationsList;

    private double latitudeCenter;
    private double longitudeCenter;
    private double haversineRadius;

    private String countryFilter;
    private Boolean isCityFilter;

    /**
     * Create a new controller and obtain the shared station repository.
     */
    public OperationsPlannerRadiusSearchController() {
        this.stationRepository = Repositories.getInstance().getStationEsinf2Repository();
    }

    /**
     * Return the AVL tree instance that orders stations by distance.
     *
     * @return an AVL tree containing stations keyed by distance
     */
    public AVLTreeStation getAvlTreeStations() {
        return avlTreeStations;
    }

    /**
     * Build the AVL tree from the stations found within the configured
     * Haversine radius. This method initialises the AVL tree and inserts
     * each station with its computed distance as the key.
     */
    public void setAvlTreeStations() {
        setStationsWithinHaversineRadius();
        this.avlTreeStations = new AVLTreeStation<>();

        for (KD2TreeStation.ListStation ls : stationsWithinHaversineRadius.keySet()) {
            double distance = stationsWithinHaversineRadius.get(ls);
            for (var station : ls.getStations()) {
                avlTreeStations.insert(distance, station, false);
            }
        }
    }

    /**
     * Return the stations as an observable list in ascending order as provided
     * by the AVL tree (plain, unfiltered list).
     *
     * @return an ObservableList of {@code StationEsinf} in ascending order
     */
    public ObservableList<StationEsinf> getAscendingStationsByAvlTree() {
        return FXCollections.observableArrayList(stationsList);
    }

    /**
     * Return the stations ordered by distance and filtered according to the
     * currently configured country and city filters.
     *
     * @return an ObservableList of filtered {@code StationEsinf} objects
     */
    public ObservableList<StationEsinf> getAscendingStationsByAvlTreeWithFilters() {
        List<StationEsinf> filteredStations = new ArrayList<>();

        for (StationEsinf station : stationsList) {
            boolean matchesCountry = (countryFilter == null) || countryFilter.equals("All") || station.getCountry().getName().equals(countryFilter);
            boolean matchesCity = (isCityFilter == null) || station.isIs_city() == isCityFilter;

            if (matchesCountry && matchesCity) {
                filteredStations.add(station);
            }
        }

        return FXCollections.observableArrayList(filteredStations);
    }

    /**
     * Return the raw map of KD-tree station groups to their Haversine
     * distances from the configured centre point.
     *
     * @return a Map where keys are KD-tree station groups and values are
     *         distances in kilometres
     */
    public Map<KD2TreeStation.ListStation, Double> getStationsWithinHaversineRadius() {
        return stationsWithinHaversineRadius;
    }

    /**
     * Compute and set the stations that lie within the configured Haversine
     * radius from the centre coordinates. The result is stored in
     * {@link #stationsWithinHaversineRadius}.
     */
    public void setStationsWithinHaversineRadius() {
        this.stationsWithinHaversineRadius = stationRepository.getKdTree().rangeSearchRadius(latitudeCenter, longitudeCenter, haversineRadius);
    }

    /**
     * Get the latitude of the search centre.
     *
     * @return the centre latitude in decimal degrees
     */
    public double getLatitudeCenter() {
        return latitudeCenter;
    }

    /**
     * Set the latitude of the search centre.
     *
     * @param latitudeCenter the latitude in decimal degrees
     */
    public void setLatitudeCenter(double latitudeCenter) {
        this.latitudeCenter = latitudeCenter;
    }

    /**
     * Get the longitude of the search centre.
     *
     * @return the centre longitude in decimal degrees
     */
    public double getLongitudeCenter() {
        return longitudeCenter;
    }

    /**
     * Set the longitude of the search centre.
     *
     * @param longitudeCenter the longitude in decimal degrees
     */
    public void setLongitudeCenter(double longitudeCenter) {
        this.longitudeCenter = longitudeCenter;
    }

    /**
     * Get the Haversine radius currently used for range searches.
     *
     * @return the radius in kilometres
     */
    public double getHaversineRadius() {
        return haversineRadius;
    }

    /**
     * Set the Haversine radius for subsequent range queries.
     *
     * @param haversineRadius the radius in kilometres
     */
    public void setHaversineRadius(double haversineRadius) {
        this.haversineRadius = haversineRadius;
    }

    /**
     * Return the list of stations obtained from the AVL tree.
     *
     * @return a List of {@code StationEsinf} in ascending order by distance
     */
    public List<StationEsinf> getStationsList() {
        return stationsList;
    }

    /**
     * Update the internal station list from the AVL tree's ascending-order
     * traversal.
     */
    public void setStationsList() {
        this.stationsList = avlTreeStations.getListAscendingOrder();
    }

    /**
     * Get the configured country filter (or null if none is set).
     *
     * @return the country filter string
     */
    public String getCountryFilter() {
        return countryFilter;
    }

    /**
     * Set the country filter to restrict displayed stations.
     *
     * @param countryFilter the country name to filter by, or "All" for no filtering
     */
    public void setCountryFilter(String countryFilter) {
        this.countryFilter = countryFilter;
    }

    /**
     * Return the city filter flag.
     *
     * @return {@code Boolean.TRUE} to show city stations only, {@code Boolean.FALSE}
     *         to show non-city stations only, or {@code null} to show both
     */
    public Boolean isCityFilter() {
        return isCityFilter;
    }

    /**
     * Set the city filter flag.
     *
     * @param cityFilter {@code Boolean.TRUE} to filter to city stations,
     *                   {@code Boolean.FALSE} to filter to non-city stations,
     *                   or {@code null} to disable the filter
     */
    public void setCityFilter(Boolean cityFilter) {
        isCityFilter = cityFilter;
    }

    /**
     * Return the list of unique country names present in the current
     * station list. The returned list begins with the option "All".
     *
     * @return a List of country names (first element is "All")
     */
    public List<String> getAllCountriesNames() {
        if (stationsList == null || stationsList.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> countryNames = new ArrayList<>();
        countryNames.add("All");
        for (StationEsinf station : stationsList) {
            String countryName = station.getCountry().getName();
            if (!countryNames.contains(countryName)) {
                countryNames.add(countryName);
            }
        }

        return countryNames;
    }

    /**
     * Check whether the current station list contains both city and non-city
     * stations.
     *
     * @return {@code true} if both kinds of stations are present, otherwise {@code false}
     */
    public boolean hasCityAndNoCityStations() {
        if (stationsList == null || stationsList.isEmpty()) {
            return false;
        }

        boolean hasCity = false;
        boolean hasNoCity = false;

        for (StationEsinf station : stationsList) {
            if (station.isIs_city()) {
                hasCity = true;
            } else {
                hasNoCity = true;
            }

            if (hasCity && hasNoCity) {
                return true;
            }
        }

        return false;
    }
}