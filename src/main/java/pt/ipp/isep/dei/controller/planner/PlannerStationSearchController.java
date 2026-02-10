package pt.ipp.isep.dei.controller.planner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.data.repository.sprint2.StationEsinf2Repository;
import pt.ipp.isep.dei.domain.Country;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.TimeZoneGroup;
import pt.ipp.isep.dei.domain.Tree.TreeType;
import pt.ipp.isep.dei.data.repository.sprint2.CountryRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller used by planners to search and retrieve station information.
 *
 * <p>This controller provides methods to obtain available tree types,
 * retrieve countries and to query stations using different spatial and
 * time‑zone group indices. All methods are self‑contained and operate on
 * the shared repositories managed by {@code Repositories}.
 */
public class PlannerStationSearchController {

    private final String COUNTRY_COMBOBOX_NONE = "Country";
    private final String TZG_COMBOBOX_NONE = "Time Zone 1";
    private final String TZG_COMBOBOX_NONE_2 = "Time Zone 2";

    private StationEsinf2Repository stationRepository;
    private CountryRepository countryRepository;

    private List<StationEsinf> stationList = new ArrayList<>();

    /**
     * Initialise a new {@code PlannerStationSearchController} and obtain the
     * required repositories from the application registry.
     */
    public PlannerStationSearchController() {
        this.stationRepository = Repositories.getInstance().getStationEsinf2Repository();
        this.countryRepository = Repositories.getInstance().getCountryRepository();
    }

    /**
     * Return the available KD/tree types that may be used to order stations.
     *
     * @return an {@code ObservableList} containing the supported {@code TreeType} values
     */
    public ObservableList<TreeType> getTreeTypes() {
        return FXCollections.observableArrayList(TreeType.values());
    }

    /**
     * Return the list of countries available in the repository, ordered
     * alphabetically.
     *
     * @return a {@code List} of {@code Country} objects
     */
    public List<Country> getCountryList() {
        return new ArrayList<>(countryRepository.findAllAlphabetically());
    }

    /**
     * Retrieve stations ordered according to the selected tree type.
     *
     * @param selectedTree the textual representation of the chosen {@code TreeType}
     * @return an observable list of {@code StationEsinf} ordered by the selected tree
     */
    public ObservableList<StationEsinf> getStations(String selectedTree) {

        stationList.clear();

        if (selectedTree.equals(TreeType.LAT_TREE.toString())) {
            stationList = stationRepository.getLatTree().getListAscendingOrder();
        } else if (selectedTree.equals(TreeType.LON_TREE.toString())) {
            stationList = stationRepository.getLonTree().getListAscendingOrder();
        } else if (selectedTree.equals(TreeType.TZG_TREE.toString())) {
            stationList = stationRepository.getTzgTree().getListAscendingOrder();
        }


        return FXCollections.observableArrayList(stationList);
    }

    /**
     * Retrieve stations whose coordinate (latitude or longitude) lies within
     * the specified range on the selected tree.
     *
     * @param higher upper bound of the coordinate range
     * @param lower lower bound of the coordinate range
     * @param selectedTree the textual representation of the chosen {@code TreeType}
     * @return an observable list of {@code StationEsinf} matching the coordinate range
     */
    public ObservableList<StationEsinf> getStationInCoordZone(double higher, double lower, String selectedTree) {

        stationList.clear();

        if (selectedTree.equals(TreeType.LAT_TREE.toString())) {
            stationList = stationRepository.getLatTree().searchCoordRange(lower, higher);
        } else if (selectedTree.equals(TreeType.LON_TREE.toString())) {
            stationList = stationRepository.getLonTree().searchCoordRange(lower, higher);
        }

        return FXCollections.observableArrayList(stationList);
    }

    /**
     * Search for stations using time‑zone group filters and an optional
     * country filter. The special combobox values indicate no selection.
     *
     * @param country the country name or the special value indicating none
     * @param tzg1 first time‑zone group selection
     * @param tzg2 second time‑zone group selection
     * @return a list of {@code StationEsinf} matching the supplied filters
     */
    public List<StationEsinf> getStationInTZG(String country, String tzg1, String tzg2) {

        Country countryObj = null;
        TimeZoneGroup tzg1Obj = null;
        TimeZoneGroup tzg2Obj = null;

        if (country != null && !country.equals(COUNTRY_COMBOBOX_NONE)) {
            countryObj = countryRepository.findByName(country);
        }

        if (tzg1 != null && !tzg1.equals(TZG_COMBOBOX_NONE)) {
            tzg1Obj = TimeZoneGroup.valueOf(tzg1);
        }

        if (tzg2 != null && !tzg2.equals(TZG_COMBOBOX_NONE_2)) {
            tzg2Obj = TimeZoneGroup.valueOf(tzg2);
        }

        stationList.clear();
        stationList = stationRepository.getTzgTree().searchTZG(countryObj, tzg1Obj, tzg2Obj);

        return stationList;
    }

    /**
     * Return the controller's currently cached station list.
     *
     * @return the list of {@code StationEsinf} last retrieved by this controller
     */
    public List<StationEsinf> getStationsActualList() {
        return stationList;
    }
}
