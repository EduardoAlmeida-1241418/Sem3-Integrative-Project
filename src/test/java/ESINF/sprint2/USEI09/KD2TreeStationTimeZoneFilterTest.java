package ESINF.sprint2.USEI09;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.data.repository.sprint2.StationEsinf2Repository;
import pt.ipp.isep.dei.domain.Country;
import pt.ipp.isep.dei.domain.TimeZone;
import pt.ipp.isep.dei.domain.TimeZoneGroup;
import pt.ipp.isep.dei.domain.Tree.KDTree.KD2TreeStation;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for KD2TreeStation search with an optional TimeZone filter.
 *
 * <p>These tests build small in-memory repositories of {@code StationEsinf} instances,
 * create the KD-tree from the repository and invoke the {@code findKNearestStations}
 * method on the KD-tree. The tests assert that the returned list contains the
 * expected number of stations and print the returned stations for manual inspection.
 *
 */
class KD2TreeStationTimeZoneFilterTest {
    /**
     * Test case: filter by the "Europe/Lisbon" time zone.
     *
     * <p>Sets up a repository with several stations in different time zones. Creates the
     * KD-tree, then requests the 3 nearest stations to coordinates in Lisbon while
     * applying a filter for {@code TimeZone.EUROPE_LISBON}. The test asserts that the
     * returned list contains exactly three stations (those in the requested time zone
     * and nearest by distance).
     */
    @Test
    void test1_FilterByTimeZoneReturnsOnlyMatchingStations_Lisbon() {
        Country PT = new Country("PT");
        Country UK = new Country("UK");
        Country MA = new Country("MA");

        StationEsinf lisbon1 = new StationEsinf(PT, TimeZone.EUROPE_LISBON, TimeZoneGroup.CET, "Oriente", 38.767841, -9.099345, true, true, false);
        StationEsinf lisbon2 = new StationEsinf(PT, TimeZone.EUROPE_LISBON, TimeZoneGroup.CET, "Santa Apolónia", 38.7141, -9.12263, true, false, false);
        StationEsinf braga = new StationEsinf(PT, TimeZone.EUROPE_LISBON, TimeZoneGroup.CET, "Braga", 41.5488, -8.43409, true, false, false);
        StationEsinf viana = new StationEsinf(PT, TimeZone.EUROPE_LISBON, TimeZoneGroup.CET, "Viana do Castelo", 41.695153, -8.831361, true, false, false);
        StationEsinf london1 = new StationEsinf(UK, TimeZone.EUROPE_LONDON, TimeZoneGroup.CET, "King's Cross",  51.5330741, -0.1224766, true, true, false);
        StationEsinf london2 = new StationEsinf(UK, TimeZone.EUROPE_LONDON, TimeZoneGroup.CET, "St Pancras International", 51.5318912, -0.1268506, true, false, false);
        StationEsinf casablanca = new StationEsinf(MA, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Casa Port", 33.59932, 7.61255, true, false, false);

        StationEsinf2Repository stationRepository = new StationEsinf2Repository();
        stationRepository.addStation(lisbon1);
        stationRepository.addStation(lisbon2);
        stationRepository.addStation(braga);
        stationRepository.addStation(viana);
        stationRepository.addStation(london1);
        stationRepository.addStation(london2);
        stationRepository.addStation(casablanca);

        System.out.println("\n[Teste1 - Europe/Lisbon]:");
        stationRepository.createKDTree();
        KD2TreeStation kdTree = stationRepository.getKdTree();
        kdTree.printTree();

        // coordenadas de Lisboa
        double latitude = 38.7167;
        double longitude = -9.1399;
        int n = 3;

        List<StationEsinf> result = kdTree.findKNearestStations(latitude, longitude, n, TimeZone.EUROPE_LISBON);

        int actualSize = result.size();
        int expectedSize = 3; // retorna as três estações mais próximas de Lisboa (Santa Polónia, Oriente e Braga), outras time zones são automaticamente excluidas
        System.out.println("Expected size: " + expectedSize);
        System.out.println("Actual size:   " + actualSize);
        assertEquals(expectedSize, actualSize, "Expected exactly=" + expectedSize + " stations, but actual=" + actualSize);

        // retorna as estações para confirmação
        System.out.println("Returned stations for Test1 (Lisbon):");
        for (StationEsinf s : result) {
            System.out.println(" - " + s.getStationName() + " [" + s.getLatitude() + ", " + s.getLongitude() + "]");
        }
    }

    /**
     * Test case: filter by the "Europe/London" time zone.
     *
     * <p>Creates a repository containing Porto and two London stations. After building
     * the KD-tree the test requests up to five nearest stations to London coordinates
     * while filtering by {@code TimeZone.EUROPE_LONDON}. The expected outcome is that
     * only the two London stations are returned, regardless of the requested maximum
     * count being larger than the available matching stations.
     */
    @Test
    void test2_FilterByTimeZoneReturnsOnlyMatchingStations_London() {
        Country PT = new Country("PT");
        Country UK = new Country("UK");

        StationEsinf porto = new StationEsinf(PT, TimeZone.EUROPE_LISBON, TimeZoneGroup.CET, "Campanhã", 41.14919, -8.58471, true, true, false);
        StationEsinf london1 = new StationEsinf(UK, TimeZone.EUROPE_LONDON, TimeZoneGroup.CET, "King's Cross",  51.5330741, -0.1224766, true, true, false);
        StationEsinf london2 = new StationEsinf(UK, TimeZone.EUROPE_LONDON, TimeZoneGroup.CET, "St Pancras International", 51.5318912, -0.1268506, true, false, false);

        StationEsinf2Repository stationRepository = new StationEsinf2Repository();
        stationRepository.addStation(porto);
        stationRepository.addStation(london1);
        stationRepository.addStation(london2);

        System.out.println("\n[Teste2 - Europe/London]:");
        stationRepository.createKDTree();
        KD2TreeStation kdTree = stationRepository.getKdTree();
        kdTree.printTree();

        // coordenadas de Londres
        double latitude = 51.5074;
        double longitude = -0.1278;
        int n = 5;

        List<StationEsinf> result = kdTree.findKNearestStations(latitude, longitude, n, TimeZone.EUROPE_LONDON);

        int actualSize = result.size();
        int expectedSize = 2; // retorna apenas as duas estações mais próximas de Londres (King's Cross e St Pancras International) por causa da time zone
        System.out.println("Expected size: " + expectedSize);
        System.out.println("Actual size:   " + actualSize);
        assertEquals(expectedSize, actualSize, "Expected exactly=" + expectedSize + " stations, but actual=" + actualSize);

        // retorna as estações para confirmação
        System.out.println("Returned stations for Test2 (London):");
        for (StationEsinf s : result) {
            System.out.println(" - " + s.getStationName() + " [" + s.getLatitude() + ", " + s.getLongitude() + "]");
        }
    }

    /**
     * Test case: no time zone filter (all time zones allowed).
     *
     * <p>Populates the repository with stations in Lisbon, London, Casablanca and Porto,
     * then requests the three nearest stations to Lisbon coordinates without applying a
     * time zone filter (passing {@code null}). The test asserts that the number of
     * returned stations matches the minimum of the requested count and the total
     * repository size and that the results are ordered by distance.
     */
    @Test
    void test3_AllTimeZonesReturnsNNearestStationsOrderedByDistance() {
        Country PT = new Country("PT");
        Country UK = new Country("UK");
        Country MA = new Country("MA");

        StationEsinf lisboa = new StationEsinf(PT, TimeZone.EUROPE_LISBON, TimeZoneGroup.CET, "Oriente", 38.767841, -9.099345, true, true, false);
        StationEsinf londres = new StationEsinf(UK, TimeZone.EUROPE_LONDON, TimeZoneGroup.CET, "King's Cross",  51.5330741, -0.1224766, true, true, false);
        StationEsinf casablanca = new StationEsinf(MA, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Casa Port", 33.59932, 7.61255, true, false, false);
        StationEsinf porto = new StationEsinf(PT, TimeZone.EUROPE_LISBON, TimeZoneGroup.CET, "Campanhã", 41.14919, -8.58471, true, true, false);

        StationEsinf2Repository stationRepository = new StationEsinf2Repository();
        stationRepository.addStation(lisboa);
        stationRepository.addStation(londres);
        stationRepository.addStation(casablanca);
        stationRepository.addStation(porto);

        System.out.println("\n[Teste3 - All Time Zones]:");
        stationRepository.createKDTree();
        KD2TreeStation kdTree = stationRepository.getKdTree();
        kdTree.printTree();

        // coordenadas de Lisboa
        double latitude = 38.7167;
        double longitude = -9.1399;
        int n = 3;

        List<StationEsinf> result = kdTree.findKNearestStations(latitude, longitude, n, null);

        int actualSize = result.size();
        int expectedSize = Math.min(n, stationRepository.getStations().size());
        System.out.println("Expected size: " + expectedSize);
        System.out.println("Actual size:   " + actualSize);
        assertEquals(expectedSize, actualSize, "Expected exactly=" + expectedSize + " stations, but actual=" + actualSize);

        // retorna as estações para confirmação
        System.out.println("Returned stations for Test3 (All Time Zones):");
        for (StationEsinf s : result) {
            System.out.println(" - " + s.getStationName() + " [" + s.getLatitude() + ", " + s.getLongitude() + "]");
        }
    }

    /**
     * Test case: no stations match the requested time zone.
     *
     * <p>Creates a repository with stations in Europe/Lisbon and Europe/London then
     * searches for stations matching {@code TimeZone.ASIA_NICOSIA}. The expected result
     * is an empty list since no station uses the requested time zone.
     */
    @Test
    void test4_NoStationsMatchTimeZoneReturnsEmptyList() {
        Country PT = new Country("PT");
        Country UK = new Country("UK");

        StationEsinf lisbon = new StationEsinf(PT, TimeZone.EUROPE_LISBON, TimeZoneGroup.CET, "Oriente", 38.767841, -9.099345, true, true, false);
        StationEsinf london = new StationEsinf(UK, TimeZone.EUROPE_LONDON, TimeZoneGroup.CET, "King's Cross",  51.5330741, -0.1224766, true, true, false);

        StationEsinf2Repository stationRepository = new StationEsinf2Repository();
        stationRepository.addStation(lisbon);
        stationRepository.addStation(london);

        System.out.println("\n[Teste4 - No Stations to match TimeZone]:");
        stationRepository.createKDTree();
        KD2TreeStation kdTree = stationRepository.getKdTree();
        kdTree.printTree();

        // coordenadas de nicosia
        double latitude = 38.7167;
        double longitude = -9.1399;
        int n = 5;

        List<StationEsinf> result = kdTree.findKNearestStations(latitude, longitude, n, TimeZone.ASIA_NICOSIA);

        int actualSize = result.size();
        int expectedSize = 0;
        System.out.println("Expected size: " + expectedSize);
        System.out.println("Actual size:   " + actualSize);
        assertEquals(expectedSize, actualSize, "Expected exactly=" + expectedSize + " stations when no timezone matches, but actual=" + actualSize);

        // retorna as estações para confirmação
        System.out.println("Returned stations for Test4 (Asia/Nicosia):");
        for (StationEsinf s : result) {
            System.out.println(" - " + s.getStationName() + " [" + s.getLatitude() + ", " + s.getLongitude() + "]");
        }
    }
}
