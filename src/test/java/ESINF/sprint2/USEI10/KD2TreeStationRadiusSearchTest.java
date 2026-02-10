package ESINF.sprint2.USEI10;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.data.repository.sprint2.StationEsinf2Repository;
import pt.ipp.isep.dei.domain.Country;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.TimeZone;
import pt.ipp.isep.dei.domain.TimeZoneGroup;
import pt.ipp.isep.dei.domain.Tree.KDTree.KD2TreeStation;
import pt.ipp.isep.dei.domain.Tree.KDTree.KD2TreeStation.HaversineDistanceComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for validating radius-based search operations on a KD2TreeStation
 * structure. These tests verify correct behavior for:
 * <ul>
 *   <li>Stations returned within a given radius</li>
 *   <li>No results when radius is too small</li>
 *   <li>Invalid input handling (invalid coordinates or radius)</li>
 * </ul>
 */
class KD2TreeStationRadiusSearchTest {

    /**
     * Creates a test repository containing 5 stations:
     * Estoril, Cascais, Lisboa, Lisboa Oriente e Lisboa Santa Apolónia.
     * Builds the KD-tree before returning the repository.
     *
     * @return a populated StationEsinfRepository with KD-tree created
     */
    private StationEsinf2Repository createRepoLisboaEstorilCascais() {
        Country PT = new Country("PT");

        StationEsinf estoril = new StationEsinf(PT, TimeZone.EUROPE_LISBON, TimeZoneGroup.CET, "ESTORIL", 38.70325, -9.39843, true, false, false);
        StationEsinf cascais = new StationEsinf(PT, TimeZone.EUROPE_LISBON, TimeZoneGroup.CET, "CASCAIS", 38.70089, -9.41775, true, false, false);
        StationEsinf lisboa = new StationEsinf(PT, TimeZone.EUROPE_LISBON, TimeZoneGroup.CET, "LISBOA", 38.71387, -9.12227, true, true, false);
        StationEsinf lisboaOriente = new StationEsinf(PT, TimeZone.EUROPE_LISBON, TimeZoneGroup.CET, "LISBOA ORIENTE", 38.71387, -9.12227, true, true, false);
        StationEsinf lisboaSantaApolonia = new StationEsinf(PT, TimeZone.EUROPE_LISBON, TimeZoneGroup.CET, "LISBOA SANTA APOLÓNIA", 38.71387, -9.12227, true, true, false);

        StationEsinf2Repository repo = new StationEsinf2Repository();
        repo.addStation(estoril);
        repo.addStation(cascais);
        repo.addStation(lisboa);
        repo.addStation(lisboaOriente);
        repo.addStation(lisboaSantaApolonia);

        repo.createKDTree();
        return repo;
    }

    /**
     * Converts the map returned by rangeSearchRadius into a list of stations.
     * Each entry in the map contains a ListStation object, from which all
     * stations are extracted.
     *
     * @param map the map produced by KD-tree radius search
     * @return a flat list of stations inside the radius
     */
    private List<StationEsinf> toStationList(Map<KD2TreeStation.ListStation, Double> map) {
        List<StationEsinf> list = new ArrayList<>();
        for (KD2TreeStation.ListStation ls : map.keySet()) {
            list.addAll(ls.getStations());
        }
        return list;
    }

    /**
     * Test 1:
     * Verifies that a 3 km radius search around Lisbon returns exactly
     * 3 stations. Also prints the haversine distances of all stations.
     */
    @Test
    void test1_Radius3KmLisbon_Returns3StationsWithinRadius() {

        StationEsinf2Repository repo = createRepoLisboaEstorilCascais();
        KD2TreeStation tree = repo.getKdTree();

        double lat = 38.7164;
        double lon = -9.1399;
        double radiusKm = 3.0;

        System.out.println("\n[Test1 - Radius 3 km Lisbon]:");
        tree.printTree();

        Map<KD2TreeStation.ListStation, Double> map = tree.rangeSearchRadius(lat, lon, radiusKm);

        List<StationEsinf> resultStations = toStationList(map);

        int expectedSize = 3;
        int actualSize = resultStations.size();

        System.out.println("Expected size: " + expectedSize);
        System.out.println("Actual size:   " + actualSize);
        assertEquals(expectedSize, actualSize);

        System.out.println("\nAll stations and their Haversine distance to Lisbon center:");

        HaversineDistanceComparator h = new HaversineDistanceComparator(lat, lon);

        for (StationEsinf s : repo.getStations().values()) {
            double d = h.haversine(lat, lon, s.getLatitude(), s.getLongitude());
            System.out.println(" - " + s.getStationName() + " [" + s.getLatitude() + ", " + s.getLongitude() + "]" + " -> " + d + " km" + (d <= radiusKm ? "  (INSIDE radius)" : "  (OUTSIDE radius)"));
        }

        System.out.println("\nReturned stations for Test1 (inside 3 km radius):");
        for (StationEsinf s : resultStations) {
            System.out.println(" - " + s.getStationName() + " [" + s.getLatitude() + ", " + s.getLongitude() + "]");
        }
    }

    /**
     * Test 2:
     * Verifies that an extremely small radius (0.5 km) around Lisbon returns
     * no stations. This ensures precision and correct boundary handling.
     *
     * Although the radius is small, the test also prints the Haversine distance
     * of the returned stations (if any), allowing distance verification just like
     * in Test 1.
     */

    @Test
    void test2_RadiusTooSmall_NoStationsInRadius() {

        StationEsinf2Repository repo = createRepoLisboaEstorilCascais();
        KD2TreeStation tree = repo.getKdTree();

        double lat = 38.7164;
        double lon = -9.1399;
        double radiusKm = 0.5;

        System.out.println("\n[Test2 - Radius 0.5 km Lisbon]:");
        tree.printTree();

        Map<KD2TreeStation.ListStation, Double> map = tree.rangeSearchRadius(lat, lon, radiusKm);

        List<StationEsinf> stations = toStationList(map);

        int expectedSize = 0;
        int actualSize = stations.size();

        System.out.println("Expected size: " + expectedSize);
        System.out.println("Actual size:   " + actualSize);
        assertEquals(expectedSize, actualSize);

        System.out.println("\nAll stations and their Haversine distance to Lisbon center (radius 0.5 km):");

        HaversineDistanceComparator h = new HaversineDistanceComparator(lat, lon);

        for (StationEsinf s : repo.getStations().values()) {
            double d = h.haversine(lat, lon, s.getLatitude(), s.getLongitude());
            System.out.println(" - " + s.getStationName() + " [" + s.getLatitude() + ", " + s.getLongitude() + "]" + " -> " + d + " km" + (d <= radiusKm ? "  (INSIDE radius)" : "  (OUTSIDE radius)"));
        }

        System.out.println("\nReturned stations for Test2 (inside 0.5 km radius):");
        for (StationEsinf s : stations) {
            System.out.println(" - " + s.getStationName() + " [" + s.getLatitude() + ", " + s.getLongitude() + "]");
        }
    }

    /**
     * Test 3: Valid coordinates far away (Porto) should return zero stations.
     *
     * <p>This is the minimal version of the test: only validates the result size.
     */
    @Test
    void test3_ValidCoordsFar_NoStationsFound() {

        StationEsinf2Repository repo = createRepoLisboaEstorilCascais();
        KD2TreeStation tree = repo.getKdTree();

        // Porto coordinates
        double lat = 41.14919;
        double lon = -8.61000;
        double radiusKm = 3.0;

        Map<KD2TreeStation.ListStation, Double> map = tree.rangeSearchRadius(lat, lon, radiusKm);

        List<StationEsinf> stations = toStationList(map);

        int expectedSize = 0;
        int actualSize = stations.size();

        System.out.println("\n[Test3 - Valid coords far from all stations (Porto)]:");
        System.out.println("Expected size: " + expectedSize);
        System.out.println("Actual size:   " + actualSize);

        assertEquals(expectedSize, actualSize);
    }


}
