package comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.comparator.FacilityDistanceComparator;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for FacilityDistanceComparator.
 * This class contains test cases to verify the correct behavior of facility comparison based on distances.
 * Tests cover various scenarios including equal distances, different distances, and edge cases.
 */
class FacilityDistanceComparatorTest {

    private FacilityDistanceComparator comparator;
    private Map<String, Double> distances;

    /**
     * Sets up the test environment before each test.
     * Initializes a new map of distances and creates a new FacilityDistanceComparator instance.
     */
    @BeforeEach
    void setUp() {
        distances = new HashMap<>();
        // Initialize with some sample distances
        distances.put("F1", 10.0);
        distances.put("F2", 20.0);
        distances.put("F3", 10.0);
        distances.put("F4", 5.0);
        comparator = new FacilityDistanceComparator(distances);
    }

    /**
     * Tests comparison of facilities with equal distances.
     * Verifies that comparing facilities with the same distance returns 0.
     */
    @Test
    void compareEqualDistances() {
        assertEquals(0, comparator.compare("F1", "F3"),
                "Facilities with equal distances should return 0");
    }

    /**
     * Tests comparison of facilities where first facility is closer.
     * Verifies that comparing a closer facility with a farther one returns negative value.
     */
    @Test
    void compareFirstFacilityCloser() {
        assertTrue(comparator.compare("F1", "F2") < 0,
                "First facility (10.0) should be considered closer than second facility (20.0)");
    }

    /**
     * Tests comparison of facilities where second facility is closer.
     * Verifies that comparing a farther facility with a closer one returns positive value.
     */
    @Test
    void compareSecondFacilityCloser() {
        assertTrue(comparator.compare("F2", "F4") > 0,
                "First facility (20.0) should be considered farther than second facility (5.0)");
    }

    /**
     * Tests comparison with a facility having zero distance.
     * Verifies correct comparison when one facility has zero distance.
     */
    @Test
    void compareWithZeroDistance() {
        distances.put("F5", 0.0);
        assertTrue(comparator.compare("F5", "F1") < 0,
                "Facility with zero distance should be considered closer");
        assertTrue(comparator.compare("F1", "F5") > 0,
                "Facility with non-zero distance should be considered farther than zero distance");
    }

    /**
     * Tests comparison with negative distances.
     * Verifies correct ordering when negative distances are involved.
     */
    @Test
    void compareWithNegativeDistances() {
        distances.put("F6", -5.0);
        distances.put("F7", -10.0);
        assertTrue(comparator.compare("F7", "F6") < 0,
                "More negative distance should be considered closer");
        assertTrue(comparator.compare("F6", "F4") < 0,
                "Negative distance should be considered closer than positive distance");
    }

    /**
     * Tests handling of non-existent facility identifiers.
     * Verifies that the comparator throws NullPointerException for missing facilities.
     */
    @Test
    void compareNonExistentFacilities() {
        assertThrows(NullPointerException.class,
                () -> comparator.compare("NonExistent", "F1"),
                "Should throw NullPointerException for non-existent first facility");

        assertThrows(NullPointerException.class,
                () -> comparator.compare("F1", "NonExistent"),
                "Should throw NullPointerException for non-existent second facility");
    }

    /**
     * Tests comparison with very large distance values.
     * Verifies correct handling of extreme distance values.
     */
    @Test
    void compareExtremeDistances() {
        distances.put("F8", Double.MAX_VALUE);
        distances.put("F9", Double.MIN_VALUE);
        assertTrue(comparator.compare("F9", "F8") < 0,
                "Minimum distance should be considered closer than maximum distance");
        assertTrue(comparator.compare("F8", "F9") > 0,
                "Maximum distance should be considered farther than minimum distance");
    }

    /**
     * Tests constructor with null map.
     * Verifies that the comparator can be constructed with null map but will throw
     * NullPointerException when comparing.
     */
    @Test
    void constructWithNullMap() {
        FacilityDistanceComparator nullMapComparator = new FacilityDistanceComparator(null);
        assertThrows(NullPointerException.class,
                () -> nullMapComparator.compare("F1", "F2"),
                "Should throw NullPointerException when comparing with null map");
    }
}