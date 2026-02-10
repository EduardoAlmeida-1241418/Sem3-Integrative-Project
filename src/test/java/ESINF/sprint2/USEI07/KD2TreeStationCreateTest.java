package ESINF.sprint2.USEI07;

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
 * Unit tests for KD2TreeStation construction and node placement.
 *
 * <p>These tests build various small repositories of {@code StationEsinf} and
 * verify that the KD-tree is constructed with the expected root and child
 * node coordinates, including handling of duplicate coordinates and multiple
 * stations at the same location.
 */
class KD2TreeStationCreateTest {

    /**
     * Test that a balanced KD-tree is produced for twenty varied stations and
     * that specific nodes appear at the expected coordinates in the tree.
     */
    @Test
    void test1_AllNodesInBalancedKDTree() {
        Country country = new Country("PT");
        List<StationEsinf> stations = List.of(
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 1", 10, 20, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 2", 15, 25, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 3", 5, 30, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 4", 12, 18, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 5", 8, 22, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 6", 3, 10, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 7", 18, 28, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 8", 7, 15, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 9", 20, 35, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 10", 9, 27, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 11", 2, 8, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 12", 11, 16, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 13", 6, 19, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 14", 14, 24, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 15", 4, 12, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 16", 16, 26, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 17", 13, 23, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 18", 19, 33, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 19", 1, 5, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 20", 17, 30, true, true, true)
        );

        StationEsinf2Repository stationRepository = new StationEsinf2Repository();

        for (StationEsinf station : stations) {
            stationRepository.addStation(station);
        }

        stationRepository.createKDTree();

        KD2TreeStation kdTree = stationRepository.getKdTree();

        kdTree.printTree();

        KD2TreeStation.KD2NodeStation root = kdTree.getRoot();
        assertNotNull(root);

        assertEquals(10, root.getX());
        assertEquals(20, root.getY());

        KD2TreeStation.KD2NodeStation n7_15 = root.getLeft();
        assertNotNull(n7_15);
        assertEquals(7, n7_15.getX());
        assertEquals(15, n7_15.getY());

        KD2TreeStation.KD2NodeStation n15_25 = root.getRight();
        assertNotNull(n15_25);
        assertEquals(15, n15_25.getX());
        assertEquals(25, n15_25.getY());

        KD2TreeStation.KD2NodeStation n2_8 = n7_15.getLeft();
        assertNotNull(n2_8);
        assertEquals(2, n2_8.getX());
        assertEquals(8, n2_8.getY());

        KD2TreeStation.KD2NodeStation n6_19 = n7_15.getRight();
        assertNotNull(n6_19);
        assertEquals(6, n6_19.getX());
        assertEquals(19, n6_19.getY());

        KD2TreeStation.KD2NodeStation n12_18 = n15_25.getLeft();
        assertNotNull(n12_18);
        assertEquals(12, n12_18.getX());
        assertEquals(18, n12_18.getY());

        KD2TreeStation.KD2NodeStation n18_28 = n15_25.getRight();
        assertNotNull(n18_28);
        assertEquals(18, n18_28.getX());
        assertEquals(28, n18_28.getY());

        KD2TreeStation.KD2NodeStation n1_5 = n2_8.getLeft();
        assertNotNull(n1_5);
        assertEquals(1, n1_5.getX());
        assertEquals(5, n1_5.getY());

        KD2TreeStation.KD2NodeStation n3_10 = n2_8.getRight();
        assertNotNull(n3_10);
        assertEquals(3, n3_10.getX());
        assertEquals(10, n3_10.getY());

        KD2TreeStation.KD2NodeStation n5_30 = n6_19.getLeft();
        assertNotNull(n5_30);
        assertEquals(5, n5_30.getX());
        assertEquals(30, n5_30.getY());

        KD2TreeStation.KD2NodeStation n8_22 = n6_19.getRight();
        assertNotNull(n8_22);
        assertEquals(8, n8_22.getX());
        assertEquals(22, n8_22.getY());

        KD2TreeStation.KD2NodeStation n11_16 = n12_18.getLeft();
        assertNotNull(n11_16);
        assertEquals(11, n11_16.getX());
        assertEquals(16, n11_16.getY());

        KD2TreeStation.KD2NodeStation n13_23 = n12_18.getRight();
        assertNotNull(n13_23);
        assertEquals(13, n13_23.getX());
        assertEquals(23, n13_23.getY());

        KD2TreeStation.KD2NodeStation n16_26 = n18_28.getLeft();
        assertNotNull(n16_26);
        assertEquals(16, n16_26.getX());
        assertEquals(26, n16_26.getY());

        KD2TreeStation.KD2NodeStation n19_33 = n18_28.getRight();
        assertNotNull(n19_33);
        assertEquals(19, n19_33.getX());
        assertEquals(33, n19_33.getY());

        KD2TreeStation.KD2NodeStation n17_30 = n16_26.getRight();
        assertNotNull(n17_30);
        assertEquals(17, n17_30.getX());
        assertEquals(30, n17_30.getY());

        KD2TreeStation.KD2NodeStation n20_35 = n19_33.getRight();
        assertNotNull(n20_35);
        assertEquals(20, n20_35.getX());
        assertEquals(35, n20_35.getY());
    }

    /**
     * Test behaviour with a single station: ensure the tree root is correctly
     * set to the station's coordinates and that no child nodes exist.
     */
    @Test
    void test2_SingleStation() {
        Country country = new Country("PT");

        List<StationEsinf> stations = List.of(
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "S1", 5, 10, true, true, true)
        );

        StationEsinf2Repository stationRepository = new StationEsinf2Repository();

        for (StationEsinf station : stations) {
            stationRepository.addStation(station);
        }

        stationRepository.createKDTree();

        KD2TreeStation kdTree = stationRepository.getKdTree();

        kdTree.printTree();

        KD2TreeStation.KD2NodeStation root = kdTree.getRoot();
        assertNotNull(root);
        assertEquals(5, root.getX());
        assertEquals(10, root.getY());
        assertNull(root.getLeft());
        assertNull(root.getRight());
    }

    /**
     * Test that with two stations, the KD-tree places the median node as the
     * root, and the other node as the right child.
     */
    @Test
    void test3_TwoStationsMedianLeft() {
        Country country = new Country("PT");

        List<StationEsinf> stations = List.of(
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "A", 1, 2, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "B", 3, 4, true, true, true)
        );

        StationEsinf2Repository stationRepository = new StationEsinf2Repository();

        for (StationEsinf station : stations) {
            stationRepository.addStation(station);
        }

        stationRepository.createKDTree();

        KD2TreeStation kdTree = stationRepository.getKdTree();

        kdTree.printTree();

        KD2TreeStation.KD2NodeStation root = kdTree.getRoot();
        assertNotNull(root);

        assertEquals(1, root.getX());
        assertEquals(2, root.getY());

        assertNull(root.getLeft());
        assertNotNull(root.getRight());

        assertEquals(3, root.getRight().getX());
        assertEquals(4, root.getRight().getY());
    }

    /**
     * Test a case with five stations where the KD-tree alternates splitting axes
     * at each level of the tree, verifying the correct median selection and
     * child node placement.
     */
    @Test
    void test4_FiveStationsAlternatingAxes() {
        Country country = new Country("PT");

        List<StationEsinf> stations = List.of(
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "S1", 1, 5, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "S2", 2, 3, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "S3", 3, 6, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "S4", 4, 2, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "S5", 5, 7, true, true, true)
        );

        StationEsinf2Repository stationRepository = new StationEsinf2Repository();

        for (StationEsinf station : stations) {
            stationRepository.addStation(station);
        }

        stationRepository.createKDTree();

        KD2TreeStation kdTree = stationRepository.getKdTree();

        kdTree.printTree();

        KD2TreeStation.KD2NodeStation root = kdTree.getRoot();
        assertNotNull(root);

        assertEquals(3, root.getX());
        assertEquals(6, root.getY());

        KD2TreeStation.KD2NodeStation left = root.getLeft();
        KD2TreeStation.KD2NodeStation right = root.getRight();

        assertNotNull(left);
        assertNotNull(right);

        assertEquals(2, left.getX());
        assertEquals(3, left.getY());
        assertEquals(4, right.getX());
        assertEquals(2, right.getY());

        assertNull(left.getLeft());
        assertNotNull(left.getRight());
        assertEquals(1, left.getRight().getX());
        assertEquals(5, left.getRight().getY());

        assertNull(right.getLeft());
        assertNotNull(right.getRight());
        assertEquals(5, right.getRight().getX());
        assertEquals(7, right.getRight().getY());
    }

    /**
     * Test handling of duplicate coordinates: ensure that stations with the same
     * coordinates are added to the same node in the KD-tree, and that the tree
     * structure remains valid.
     */
    @Test
    void test5_DuplicateCoordinates() {
        Country country = new Country("PT");

        List<StationEsinf> stations = List.of(
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "S1", 10, 20, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "S2", 10, 25, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "S3", 15, 25, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "S4", 20, 20, true, true, true)
        );

        StationEsinf2Repository stationRepository = new StationEsinf2Repository();

        for (StationEsinf station : stations) {
            stationRepository.addStation(station);
        }

        stationRepository.createKDTree();

        KD2TreeStation kdTree = stationRepository.getKdTree();

        kdTree.printTree();

        KD2TreeStation.KD2NodeStation root = kdTree.getRoot();

        assertNotNull(root);
        assertEquals(10, root.getX());
        assertEquals(25, root.getY());

        assertNotNull(root.getLeft());
        assertEquals(10, root.getLeft().getX());
        assertEquals(20, root.getLeft().getY());

        assertNotNull(root.getRight());
        assertEquals(20, root.getRight().getX());
        assertEquals(20, root.getRight().getY());

        assertNull(root.getRight().getLeft());
        assertNotNull(root.getRight().getRight());
        assertEquals(15, root.getRight().getRight().getX());
        assertEquals(25, root.getRight().getRight().getY());
    }

    /**
     * Test scenario with duplicate stations having the same coordinates: ensure
     * that all stations are added to the tree and that the tree structure is
     * valid, with duplicates handled according to the KD-tree rules.
     */
    @Test
    void test6_DuplicateStationsSameCoordinates() {
        Country country = new Country("PT");
        List<StationEsinf> stations = List.of(
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Campanha", 1, 2, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "S2", 3, 4, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "S3", 4, 3, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "S4", 4, 1, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "S5", 2, 1, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Camelias", 1, 2, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "S7", 3, 3, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "S8", 1, 5, true, true, true)
        );

        StationEsinf2Repository stationRepository = new StationEsinf2Repository();

        for (StationEsinf station : stations) {
            stationRepository.addStation(station);
        }

        stationRepository.createKDTree();

        KD2TreeStation kdTree = stationRepository.getKdTree();

        kdTree.printTree();

        KD2TreeStation.KD2NodeStation root = kdTree.getRoot();

        assertNotNull(root);
        assertEquals(3, root.getX());
        assertEquals(4, root.getY());

        KD2TreeStation.KD2NodeStation left = root.getLeft();
        assertNotNull(left);
        assertEquals(1, left.getX());
        assertEquals(2, left.getY());

        assertEquals(2, left.getElement().getStations().size());
        assertEquals("Camelias", left.getElement().getStations().get(0).getStationName());
        assertEquals("Campanha", left.getElement().getStations().get(1).getStationName());

        KD2TreeStation.KD2NodeStation right = root.getRight();
        assertNotNull(right);
        assertEquals(3, right.getX());
        assertEquals(3, right.getY());

        assertNotNull(left.getLeft());
        assertEquals(2, left.getLeft().getX());
        assertEquals(1, left.getLeft().getY());

        assertNotNull(left.getRight());
        assertEquals(1, left.getRight().getX());
        assertEquals(5, left.getRight().getY());

        assertNotNull(right.getLeft());
        assertEquals(4, right.getLeft().getX());
        assertEquals(1, right.getLeft().getY());

        assertNotNull(right.getRight());
        assertEquals(4, right.getRight().getX());
        assertEquals(3, right.getRight().getY());
    }
}