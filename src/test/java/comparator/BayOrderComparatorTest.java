package comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.comparator.BayOrderComparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for BayOrderComparator.
 * This class contains test cases to verify the correct behavior of bay identifier comparison.
 * It tests various scenarios including warehouse IDs, aisle numbers, and bay numbers.
 * Bay identifiers are expected to be in the format "WxAyBz" where:
 * - Wx represents the warehouse ID
 * - Ay represents the aisle number
 * - Bz represents the bay number
 */
class BayOrderComparatorTest {

    private BayOrderComparator comparator;

    /**
     * Sets up the test environment before each test.
     * Initializes a new instance of BayOrderComparator.
     */
    @BeforeEach
    void setUp() {
        comparator = new BayOrderComparator();
    }

    /**
     * Tests comparison of identical bay identifiers.
     * Verifies that comparing the same bay identifier returns 0.
     */
    @Test
    void compareIdenticalBays() {
        assertEquals(0, comparator.compare("W1A1B1", "W1A1B1"),
                "Identical bay identifiers should be equal");
    }

    /**
     * Tests comparison of bay identifiers with different warehouse IDs.
     * Verifies that warehouse IDs are compared lexicographically.
     */
    @Test
    void compareDifferentWarehouses() {
        assertTrue(comparator.compare("W1A1B1", "W2A1B1") < 0,
                "W1 should come before W2");
        assertTrue(comparator.compare("W2A1B1", "W1A1B1") > 0,
                "W2 should come after W1");
    }

    /**
     * Tests comparison of bay identifiers with same warehouse but different aisles.
     * Verifies that aisle numbers are compared numerically.
     */
    @Test
    void compareDifferentAisles() {
        assertTrue(comparator.compare("W1A1B1", "W1A2B1") < 0,
                "Aisle 1 should come before Aisle 2");
        assertTrue(comparator.compare("W1A2B1", "W1A1B1") > 0,
                "Aisle 2 should come after Aisle 1");
    }

    /**
     * Tests comparison of bay identifiers with same warehouse and aisle but different bays.
     * Verifies that bay numbers are compared numerically.
     */
    @Test
    void compareDifferentBays() {
        assertTrue(comparator.compare("W1A1B1", "W1A1B2") < 0,
                "Bay 1 should come before Bay 2");
        assertTrue(comparator.compare("W1A1B2", "W1A1B1") > 0,
                "Bay 2 should come after Bay 1");
    }

    /**
     * Tests comparison of bay identifiers with double-digit numbers.
     * Verifies correct numerical ordering for larger numbers in aisles and bays.
     */
    @Test
    void compareDoubleDigitNumbers() {
        assertTrue(comparator.compare("W1A2B1", "W1A10B1") < 0,
                "Aisle 2 should come before Aisle 10");
        assertTrue(comparator.compare("W1A1B2", "W1A1B10") < 0,
                "Bay 2 should come before Bay 10");
    }

    /**
     * Tests handling of invalid format bay identifiers.
     * Verifies that the comparator throws NumberFormatException for malformed input.
     */
    @Test
    void compareInvalidFormat() {
        assertThrows(NumberFormatException.class,
                () -> comparator.compare("W1AXB1", "W1A1B1"),
                "Should throw NumberFormatException for non-numeric aisle");

        assertThrows(NumberFormatException.class,
                () -> comparator.compare("W1A1BX", "W1A1B1"),
                "Should throw NumberFormatException for non-numeric bay");
    }

    /**
     * Tests comparison with null values.
     * Verifies that the comparator throws NullPointerException when null values are provided.
     */
    @Test
    void compareNullValues() {
        assertThrows(NullPointerException.class,
                () -> comparator.compare(null, "W1A1B1"),
                "Should throw NullPointerException for null first argument");

        assertThrows(NullPointerException.class,
                () -> comparator.compare("W1A1B1", null),
                "Should throw NullPointerException for null second argument");
    }

    /**
     * Tests comparison with malformed bay identifiers.
     * Verifies that the comparator throws ArrayIndexOutOfBoundsException for incomplete identifiers.
     */
    @Test
    void compareMalformedIdentifiers() {
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> comparator.compare("W1", "W1A1B1"),
                "Should throw ArrayIndexOutOfBoundsException for incomplete identifier");

        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> comparator.compare("W1A1", "W1A1B1"),
                "Should throw ArrayIndexOutOfBoundsException for missing bay number");
    }
}