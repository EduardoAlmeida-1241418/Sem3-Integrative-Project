package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Aisle;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Aisle}.
 * Adapted to use full bay identifiers compatible with {@link pt.ipp.isep.dei.comparator.BayOrderComparator}.
 */
class AisleTest {

    private Aisle aisle;

    @BeforeEach
    void setUp() {
        aisle = new Aisle("W1A3", "W1");
    }

    /**
     * Verifies constructor initialization and automatic number extraction.
     */
    @Test
    void testConstructorInitializesCorrectly() {
        assertEquals("W1A3", aisle.getAisleID());
        assertEquals("W1", aisle.getWarehouseID());
        assertEquals(3, aisle.getNumber());
        assertTrue(aisle.getBaysId().isEmpty());
    }

    /**
     * Verifies that the aisle number is updated when the ID changes.
     */
    @Test
    void testSetAisleIDUpdatesNumber() {
        aisle.setAisleID("W1A7");
        assertEquals("W1A7", aisle.getAisleID());
        assertEquals(7, aisle.getNumber());
    }

    /**
     * Verifies warehouse ID getter and setter behavior.
     */
    @Test
    void testSetAndGetWarehouseID() {
        aisle.setWarehouseID("W2");
        assertEquals("W2", aisle.getWarehouseID());
    }

    /**
     * Verifies bay addition maintains sorted order using full bay identifiers.
     */
    @Test
    void testAddBayMaintainsOrder() {
        aisle.addBay("W1A3B3");
        aisle.addBay("W1A3B1");
        aisle.addBay("W1A3B2");

        var bays = aisle.getBaysIdAsList();
        assertEquals(3, bays.size());
        assertEquals("W1A3B1", bays.get(0));
        assertEquals("W1A3B3", bays.get(2));
    }

    /**
     * Verifies bay set can be replaced directly.
     */
    @Test
    void testSetBaysId() {
        Set<String> newSet = new TreeSet<>();
        newSet.add("W1A3B5");
        newSet.add("W1A3B6");

        aisle.setBaysId(newSet);
        assertEquals(2, aisle.getBaysId().size());
        assertTrue(aisle.getBaysId().contains("W1A3B5"));
    }

    /**
     * Verifies toString output contains aisle ID and bay list.
     */
    @Test
    void testToStringContainsExpectedInformation() {
        aisle.addBay("W1A3B1");
        aisle.addBay("W1A3B2");
        String result = aisle.toString();

        assertTrue(result.contains("W1A3"));
        assertTrue(result.contains("W1A3B1"));
        assertTrue(result.contains("W1A3B2"));
    }
}
