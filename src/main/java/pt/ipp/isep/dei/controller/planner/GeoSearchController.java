package pt.ipp.isep.dei.controller.planner;
import pt.ipp.isep.dei.data.repository.sprint2.StationEsinf2Repository;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Tree.KDTree.KD2TreeStation;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for performing spatial searches on the station
 * KD-tree for the planner role.
 *
 * <p>This class obtains the shared {@code StationEsinfRepository} and exposes
 * a region search method that returns stations matching simple boolean
 * filters (city, main station, airport) and an optional country filter.
 */
public class GeoSearchController {

    private final StationEsinf2Repository stationRepository;

    /**
     * Create a new {@code GeoSearchController} and obtain the shared
     * station repository from the application's repository registry.
     */
    public GeoSearchController() {
        this.stationRepository = Repositories.getInstance().getStationEsinf2Repository();
    }

    /**
     * Search for stations inside an axis-aligned rectangular region and
     * apply optional filters.
     *
     * @param latMin lower bound for latitude
     * @param latMax upper bound for latitude
     * @param lonMin lower bound for longitude
     * @param lonMax upper bound for longitude
     * @param isCity optional filter: {@code true} to require city stations,
     *               {@code false} to require non-city stations, or {@code null}
     *               to ignore this filter
     * @param isMainStation optional filter for main stations (same semantics)
     * @param isAirport optional filter for airport stations (same semantics)
     * @param country optional country name to filter by; case-insensitive
     *                comparison is used and the special value "all" is
     *                treated as no filtering
     * @return a list of {@code StationEsinf} that lie within the region and
     *         satisfy the provided filters
     */
    public List<StationEsinf> searchInRegion(double latMin, double latMax,
                                             double lonMin, double lonMax,
                                             Boolean isCity, Boolean isMainStation, Boolean isAirport, String country) {

        List<StationEsinf> allMatches = new ArrayList<>();                                 // complexidade da linha: O(1).

        //Retorna todos os nós (Station ou ListStation) na região definida
        List<KD2TreeStation.ListStation> candidates = stationRepository.getKdTree().rangeSearch(latMin, latMax, lonMin, lonMax);    //Análise de complexidade: Vide nota explicativa na classe Kdtree.

        //Percorre todos os nós retornados (ListStation)
        for(KD2TreeStation.ListStation listNode : candidates){

                for (StationEsinf s : listNode.getStations()) {                             // complexidade da linha: O(S), S = número de estações na ListStation.
                    if (matchesFilters(s, isCity, isMainStation, isAirport, country)) {     // complexidade da linha: O(1).
                        allMatches.add(s);
                    }
                }
        }
        return allMatches;      // Complexidade da linha: 0(1)
    }


    // Filtro Centralizado, verifica todos os filtros ao mesmo tempo.
    /**
     * Evaluate whether a single station satisfies the provided filters.
     *
     * @param s the station to test
     * @param isCity optional city filter
     * @param isMainStation optional main-station filter
     * @param isAirport optional airport filter
     * @param country optional country name; the special value "all" means no filtering
     * @return {@code true} if the station matches all supplied filters, otherwise {@code false}
     */
    private boolean matchesFilters(StationEsinf s, Boolean isCity, Boolean isMainStation, Boolean isAirport, String country) {

        if (isCity != null && s.isIs_city() != isCity){                             //complexidade da linha: O(1).
            return false;
        }

        if (isMainStation != null && s.isIs_main_station() != isMainStation){       //complexidade da linha: O(1).
            return false;
        }

        if (isAirport != null && s.isIs_airport() != isAirport){                    //complexidade da linha: O(1).
            return false;
        }

        if ((country != null && !"all".equalsIgnoreCase(country)) && !s.getCountry().getName().equalsIgnoreCase(country)){
            return false;                                                                                                          //Complexidade da linha: O(1).
        }

        return true;                 //Complexidade da linha: O(1)
    }
}
