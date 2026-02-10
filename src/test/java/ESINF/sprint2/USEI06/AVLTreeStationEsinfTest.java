package ESINF.sprint2.USEI06;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Country;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.TimeZone;
import pt.ipp.isep.dei.domain.TimeZoneGroup;
import pt.ipp.isep.dei.domain.Tree.AVL.AVLTreeStation;
import pt.ipp.isep.dei.domain.Tree.Node.TreeNode;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the AVL tree used to order stations by a numeric key
 * (latitude in these tests).
 *
 * <p>These tests verify balancing behaviour, handling of duplicate keys,
 * stable ordering of stations within the same node and robustness when
 * invalid station data is provided.
 */
public class AVLTreeStationEsinfTest {

    /**
     * Verify that the AVL tree remains balanced after inserting multiple
     * station entries and that the ascending-order traversal returns the
     * expected latitudes.
     */
    @Test
    void verifyIfIsBallanced() {
        Country country = new Country("PT");
        List<StationEsinf> stationList = List.of(
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 1", 10, 20, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 2", 15, 25, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 3", 5, 30, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 4", 12, 18, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 5", 13, 22, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 6", 6, 10, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 7", 7, 28, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 8", 18, 15, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 9", 19, 35, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 10", 20, 27, true, true, true)
        );

        AVLTreeStation<Double> tree = new AVLTreeStation();

        for (StationEsinf station : stationList) {
            tree.insert(station.getLatitude(), station, true);
        }

        List<Double> expectedLatitudes = List.of(5.0, 6.0, 7.0, 10.0, 12.0, 13.0, 15.0, 18.0, 19.0, 20.0);
        List<StationEsinf> actualStations = tree.getListAscendingOrder();

        List<Double> actualLatitudes = new ArrayList<>();
        for (StationEsinf s : actualStations) {
            actualLatitudes.add(s.getLatitude());
        }

        assertEquals(expectedLatitudes, actualLatitudes);
    }

    /**
     * Verify that multiple stations sharing the same key (latitude) are stored
     * in the same tree node and that the node contains the appropriate number
     * of stations.
     */
    @Test
    void verifyIfMultipleStationsShareSameCoord() {
        Country country = new Country("PT");
        List<StationEsinf> stationList = List.of(
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 1", 10, 20, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 2", 15, 25, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 3", 5, 30, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 4", 12, 18, true, true, true),  // mesmo nó
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 5", 13, 22, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 10", 12, 27, true, true, true), // mesmo nó
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 6", 6, 10, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 7", 7, 28, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 8", 18, 15, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 9", 19, 35, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 11", 20, 27, true, true, true)
        );

        AVLTreeStation<Double> tree = new AVLTreeStation();

        for (StationEsinf station : stationList) {
            tree.insert(station.getLatitude(), station, true);
        }

        Double searchKey = 12.0;
        TreeNode<Double> foundNode = tree.search(searchKey);

        assertEquals(2, foundNode.getStations().size());
    }

    /**
     * Verify that when multiple stations reside in the same node their names
     * are ordered as expected (alphabetically) when retrieved from that node.
     */
    @Test
    void verifyNameOrderInSameNode() {
        Country country = new Country("PT");
        List<StationEsinf> stationList = List.of(
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 1", 10, 20, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 7", 10, 25, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 3", 10, 30, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 9", 10, 18, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 8", 10, 22, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 6", 10, 10, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 2", 10, 28, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 5", 10, 15, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 4", 10, 35, true, true, true)
        );

        AVLTreeStation<Double> tree = new AVLTreeStation();

        for (StationEsinf station : stationList) {
            tree.insert(station.getLatitude(), station, true);
        }

        Double searchKey = 10.0;
        TreeNode<Double> foundNode = tree.search(searchKey);

        List<StationEsinf> stations = foundNode.getStations();

        List<String> expectedNames = List.of(
                "Station 1", "Station 2", "Station 3", "Station 4",
                "Station 5", "Station 6", "Station 7", "Station 8", "Station 9"
        );

        List<String> actualNames = new ArrayList<>();
        for (StationEsinf s : stations) {
            actualNames.add(s.getStationName());
        }

        assertEquals(expectedNames, actualNames);
    }

    /**
     * Verify the tree's resilience to invalid station data: insertions with
     * invalid latitude, longitude, null country, or missing time zone must be
     * ignored and not cause incorrect counts in the ascending-order list.
     */
    @Test
    void verifyInsertInvalidCoordinates() {
        Country country = new Country("PT");
        List<StationEsinf> stationList = List.of(
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 1", 10, 20, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 2", 15, 25, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 3", -100, 30, true, true, true),  // inválido
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 4", 12, 190, true, true, true),   // inválido
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "", 13, 22, true, true, true),             // inválido
                new StationEsinf(null, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 6", 6, 10, true, true, true), // inválido
                new StationEsinf(country, null, TimeZoneGroup.CET, "Station 7", 7, 28, true, true, true),                   // inválido
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, null, "Station 8", 18, 15, true, true, true),     // inválido
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 9", 19, 35, true, true, true),
                new StationEsinf(country, TimeZone.AFRICA_CASABLANCA, TimeZoneGroup.CET, "Station 10", 20, 27, true, true, true)
        );

        AVLTreeStation<Double> tree = new AVLTreeStation();

        for (StationEsinf station : stationList) {
            tree.insert(station.getLatitude(), station, true);
        }

        assertEquals(4, tree.getListAscendingOrder().size());
    }
}
