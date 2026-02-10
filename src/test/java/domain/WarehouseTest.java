package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Warehouse;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Warehouse} class.
 * <p>
 * Ensures correct functionality of constructors, getters, setters,
 * and aisle manipulation methods.
 * </p>
 */
class WarehouseTest {

    private Warehouse warehouse;

    /** Base setup executed before each test. */
    @BeforeEach
    void setUp() {
        warehouse = new Warehouse("WH001");
    }

    /** Verifies that the constructor correctly initializes the warehouse ID. */
    @Test
    void testConstructorInitializesCorrectly() {
        assertEquals("WH001", warehouse.getWarehouseID());
        assertTrue(warehouse.getAislesIds().isEmpty());
    }

    /** Tests that setters and getters work correctly. */
    @Test
    void testSettersAndGettersWorkCorrectly() {
        warehouse.setWarehouseID("WH999");
        Set<String> aisles = new TreeSet<>();
        aisles.add("A01");
        aisles.add("A02");
        warehouse.setAislesIds(aisles);

        assertEquals("WH999", warehouse.getWarehouseID());
        assertEquals(aisles, warehouse.getAislesIds());
    }

    /** Verifies that {@code getBaysIds()} returns an empty list when no aisles exist. */
    @Test
    void testGetBaysIdsReturnsEmptyListWhenNoAisles() {
        assertTrue(warehouse.getBaysIds().isEmpty());
    }

    /** Verifies that {@code getFreeBayWithItem()} returns null by default. */
    @Test
    void testGetFreeBayWithItemReturnsNullByDefault() {
        assertNull(warehouse.getFreeBayWithItem("BOX001"));
    }
}
