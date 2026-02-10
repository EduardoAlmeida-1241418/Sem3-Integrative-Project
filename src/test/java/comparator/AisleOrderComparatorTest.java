package comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.comparator.AisleOrderComparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link pt.ipp.isep.dei.comparator.AisleOrderComparator}.
 * <p>
 * Verifies the behavior of the comparator that orders aisle identifiers
 * based on warehouse and aisle numbers.
 */
class AisleOrderComparatorTest {

    private AisleOrderComparator comparator;

    /**
     * Initializes the comparator before each test.
     */
    @BeforeEach
    void setUp() {
        comparator = new AisleOrderComparator();
    }

    /**
     * Tests comparing two identical aisles within the same warehouse.
     * Expected result: 0.
     */
    @Test
    void compareSameWarehouseAndAisle() {
        String aisle1 = "W1A5";
        String aisle2 = "W1A5";
        assertEquals(0, comparator.compare(aisle1, aisle2));
    }

    /**
     * Tests comparison between aisles from different warehouses.
     * Expected behavior: ordering is based on warehouse number.
     */
    @Test
    void compareDifferentWarehouses() {
        String aisle1 = "W1A5";
        String aisle2 = "W2A5";
        assertTrue(comparator.compare(aisle1, aisle2) < 0);
        assertTrue(comparator.compare(aisle2, aisle1) > 0);
    }

    /**
     * Tests comparison between different aisles in the same warehouse.
     * Expected behavior: ordering is based on aisle number.
     */
    @Test
    void compareDifferentAisles() {
        String aisle1 = "W1A3";
        String aisle2 = "W1A5";
        assertTrue(comparator.compare(aisle1, aisle2) < 0);
        assertTrue(comparator.compare(aisle2, aisle1) > 0);
    }

    /**
     * Tests comparison between aisles with both different warehouses and aisle numbers.
     * Expected behavior: primary ordering by warehouse, then by aisle.
     */
    @Test
    void compareWithDifferentWarehouseAndAisle() {
        String aisle1 = "W1A7";
        String aisle2 = "W2A3";
        assertTrue(comparator.compare(aisle1, aisle2) < 0);
        assertTrue(comparator.compare(aisle2, aisle1) > 0);
    }

    /**
     * Tests behavior when an aisle identifier has an invalid format.
     * Expected behavior: throws NumberFormatException.
     */
    @Test
    void shouldThrowNumberFormatExceptionForInvalidFormat() {
        String aisle1 = "W1AX";
        String aisle2 = "W1A5";
        assertThrows(NumberFormatException.class, () -> comparator.compare(aisle1, aisle2));
    }
}