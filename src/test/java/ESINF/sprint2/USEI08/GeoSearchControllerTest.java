package ESINF.sprint2.USEI08;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.planner.GeoSearchController;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint2.StationEsinf2Repository;
import pt.ipp.isep.dei.domain.Country;
import pt.ipp.isep.dei.domain.TimeZone;
import pt.ipp.isep.dei.domain.TimeZoneGroup;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Tree.KDTree.KD2TreeStation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GeoSearchControllerTest {

    private static GeoSearchController controller;

    private final double LAT_MIN = 37.00689;
    private final double LAT_MAX = 37.02860;
    private final double LON_MIN = -7.96972;
    private final double LON_MAX = -2.74118;

    private List<String> extractNames(List<StationEsinf> list) {
        List<String> names = new ArrayList<>();
        for (StationEsinf s : list) {
            names.add(s.getStationName());
        }
        return names;
    }

    @BeforeAll
    public static void setUpRepository() {
        Country PT = new Country("PT");
        Country ES = new Country("ES");

        // Create StationEsinf instances using coordinates and flags from dataset
        StationEsinf faro = new StationEsinf(PT, TimeZone.EUROPE_LISBON, TimeZoneGroup.WET_GMT, "Faro", 37.019187, -7.940261, true, false, false);
        StationEsinf faroAirport = new StationEsinf(PT, TimeZone.EUROPE_LISBON, TimeZoneGroup.WET_GMT, "Faro Airport", 37.0175956, -7.96972, false, false, true);
        StationEsinf olhao = new StationEsinf(PT, TimeZone.EUROPE_LISBON, TimeZoneGroup.WET_GMT, "Olhao", 37.0286, -7.8411, true, false, false);
        StationEsinf matalascanas = new StationEsinf(ES, TimeZone.EUROPE_MADRID, TimeZoneGroup.CET, "Matalascanas", 37.01235, -6.55927, true, false, false);
        StationEsinf lasCabezas = new StationEsinf(ES, TimeZone.EUROPE_MADRID, TimeZoneGroup.CET, "Las Cabezas de San Juan", 37.020077, -5.946553, false, false, false);
        StationEsinf antequeraCiudad = new StationEsinf(ES, TimeZone.EUROPE_MADRID, TimeZoneGroup.CET, "Antequera Ciudad", 37.028276, -4.561063, false, true, false);
        StationEsinf villanueva = new StationEsinf(ES, TimeZone.EUROPE_MADRID, TimeZoneGroup.CET, "Villanueva del Trabuco", 37.0279447, -4.3385575, true, false, false);
        StationEsinf antequera = new StationEsinf(ES, TimeZone.EUROPE_MADRID, TimeZoneGroup.CET, "Antequera", 37.020055, -4.55936, true, false, false);
        StationEsinf alhama = new StationEsinf(ES, TimeZone.EUROPE_MADRID, TimeZoneGroup.CET, "Alhama de Granada", 37.00689, -3.98963, true, false, false);
        StationEsinf padul = new StationEsinf(ES, TimeZone.EUROPE_MADRID, TimeZoneGroup.CET, "Padul", 37.0215109, -3.626021, true, false, false);
        StationEsinf jubar = new StationEsinf(ES, TimeZone.EUROPE_MADRID, TimeZoneGroup.CET, "Jubar", 37.0084279, -3.0327472, true, false, false);
        StationEsinf laroles = new StationEsinf(ES, TimeZone.EUROPE_MADRID, TimeZoneGroup.CET, "Laroles", 37.00852, -3.01386, true, false, false);
        StationEsinf canjayar = new StationEsinf(ES, TimeZone.EUROPE_MADRID, TimeZoneGroup.CET, "Canjayar", 37.010326, -2.74118, true, false, false);
        StationEsinf beires = new StationEsinf(ES, TimeZone.EUROPE_MADRID, TimeZoneGroup.CET, "Beires", 37.01237, -2.79134, true, false, false);

        StationEsinf2Repository repo = Repositories.getInstance().getStationEsinf2Repository();
        repo.getStations().clear();
        repo.addStation(faro);
        repo.addStation(faroAirport);
        repo.addStation(olhao);
        repo.addStation(matalascanas);
        repo.addStation(lasCabezas);
        repo.addStation(antequeraCiudad);
        repo.addStation(villanueva);
        repo.addStation(antequera);
        repo.addStation(alhama);
        repo.addStation(padul);
        repo.addStation(jubar);
        repo.addStation(laroles);
        repo.addStation(canjayar);
        repo.addStation(beires);

        repo.createKDTree();
        KD2TreeStation kdTree = repo.getKdTree();
        kdTree.printTree();

        controller = new GeoSearchController();
    }


    @Test
    public void test1_AllTypes() {
        List<StationEsinf> result = controller.searchInRegion(LAT_MIN, LAT_MAX, LON_MIN, LON_MAX,
                null, null, null, "all");

        List<String> expected = Arrays.asList(
                "Faro Airport", "Jubar", "Alhama de Granada", "Matalascanas", "Canjayar",
                "Laroles", "Beires", "Antequera Ciudad", "Las Cabezas de San Juan",
                "Faro", "Olhao", "Padul", "Antequera", "Villanueva del Trabuco");

        List<String> obtained = extractNames(result);

        System.out.println("\nTest 1 - ALL + ALL Types:");
        System.out.println("Expected: " + expected);
        System.out.println("Obtained: " + obtained);

        assertEquals(expected, obtained, "should return all stations inside the bounding box in the expected order");
    }


    @Test
    public void test2_PTAllTypes() {

        List<StationEsinf> result = controller.searchInRegion(LAT_MIN, LAT_MAX, LON_MIN, LON_MAX,
                null, null, null, "PT");

        List<String> expected = Arrays.asList("Faro Airport", "Faro", "Olhao");

        List<String> obtained = extractNames(result);

        System.out.println("\nTest 2 - PT + All Types:");
        System.out.println("Expected: " + expected);
        System.out.println("Obtained: " + obtained);

        assertEquals(expected, obtained, "should return only Portuguese stations within the bounding box");
    }


    @Test
    public void test3_ESAllTypes() {

        List<StationEsinf> result = controller.searchInRegion(LAT_MIN, LAT_MAX, LON_MIN, LON_MAX,
                null, null, null, "ES");

        List<String> expected = Arrays.asList(
                "Jubar", "Alhama de Granada", "Matalascanas", "Canjayar",
                "Laroles", "Beires", "Antequera Ciudad", "Las Cabezas de San Juan", "Padul", "Antequera", "Villanueva del Trabuco");


        List<String> obtained = extractNames(result);

        System.out.println("\nTest 3 - ES + All Types:");
        System.out.println("Expected: " + expected);
        System.out.println("Obtained: " + obtained);

        assertEquals(expected, obtained, "should return only Spanish stations within the bounding box");
    }


    @Test
    public void test4_AllCities() {

        List<StationEsinf> result = controller.searchInRegion(LAT_MIN, LAT_MAX, LON_MIN, LON_MAX,
                true, null, null, "all");

        List<String> expected = Arrays.asList(
                "Jubar", "Alhama de Granada", "Matalascanas", "Canjayar", "Laroles",
                "Beires", "Faro", "Olhao", "Padul", "Antequera", "Villanueva del Trabuco");


        List<String> obtained = extractNames(result);

        System.out.println("\n0Test 4 - ALL + Cities:");
        System.out.println("Expected: " + expected);
        System.out.println("Obtained: " + obtained);

        assertEquals(expected, obtained, "should return only station names classified as cities");
    }


    @Test
    public void test5_AllMainStations() {

        List<StationEsinf> result = controller.searchInRegion(LAT_MIN, LAT_MAX, LON_MIN, LON_MAX,
                null, true, null, "all");

        List<String> expected = Collections.singletonList("Antequera Ciudad");

        List<String> obtained = extractNames(result);

        System.out.println("\nTest 5 - ALL + Main Stations:");
        System.out.println("Expected: " + expected);
        System.out.println("Obtained: " + obtained);

        assertEquals(expected, obtained, "should return only the main station(s) in the bounding box");
    }


    @Test
    public void test6_AllAirports() {

        List<StationEsinf> result = controller.searchInRegion(LAT_MIN, LAT_MAX, LON_MIN, LON_MAX,
                null, null, true, "all");

        List<String> expected = Collections.singletonList("Faro Airport");

        List<String> obtained = extractNames(result);

        System.out.println("\nTest 6 - ALL + Airports:");
        System.out.println("Expected: " + expected);
        System.out.println("Obtained: " + obtained);

        assertEquals(expected, obtained, "should return only airports in the bounding box");
    }


    @Test
    public void test7_LatitudeAboveLimit() {

        List<StationEsinf> result = controller.searchInRegion(120.0, 130.0, LON_MIN, LON_MAX,
                null, null, null, "all");

        List<String> expected = new ArrayList<>();
        List<String> obtained = extractNames(result);

        System.out.println("\nTest 7 - Latitude above limit:");
        System.out.println("Expected: []");
        System.out.println("Obtained: " + obtained);

        assertEquals(expected, obtained, "should return an empty list");
    }


    @Test
    public void test8_LatitudeBelowLimit() {

        List<StationEsinf> result = controller.searchInRegion(-150.0, -120.0, LON_MIN, LON_MAX,
                null, null, null, "all");

        List<String> expected = new ArrayList<>();
        List<String> obtained = extractNames(result);

        System.out.println("\nTest 8 - Latitude below limit:");
        System.out.println("Expected: []");
        System.out.println("Obtained: " + obtained);

        assertEquals(expected, obtained, "should return an empty list");
    }


    @Test
    public void test9_LongitudeAboveLimit() {

        List<StationEsinf> result = controller.searchInRegion(LAT_MIN, LAT_MAX, 200.0, 300.0,
                null, null, null, "all");

        List<String> expected = new ArrayList<>();
        List<String> obtained = extractNames(result);

        System.out.println("\nTest 9 - Longitude above limit:");
        System.out.println("Expected: []");
        System.out.println("Obtained: " + obtained);

        assertEquals(expected, obtained, "should return an empty list");
    }


    @Test
    public void test10_LongitudeBelowLimit() {

        List<StationEsinf> result = controller.searchInRegion(LAT_MIN, LAT_MAX, -300.0, -200.0,
                null, null, null, "all");

        List<String> expected = new ArrayList<>();
        List<String> obtained = extractNames(result);

        System.out.println("\nTest 10 - Longitude below limit:");
        System.out.println("Expected: []");
        System.out.println("Obtained: " + obtained);

        assertEquals(expected, obtained, "should return an empty list");
    }


    @Test
    public void test11_InvertedLongitude() {

        List<StationEsinf> result = controller.searchInRegion(LAT_MIN, LAT_MAX, -2.0, -10.0,
                null, null, null, "all");

        List<String> expected = new ArrayList<>();
        List<String> obtained = extractNames(result);

        System.out.println("\nTest 11 - Inverted longitude:");
        System.out.println("Expected: []");
        System.out.println("Obtained: " + obtained);

        assertEquals(expected, obtained, "should return an empty list");
}
}
