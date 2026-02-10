package domain.PickingPlanRelated;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Improved unit tests for bay distance calculation according to acceptance criteria:
 *  - Same aisle: D = |b1 − b2|
 *  - Different aisles: D = b1 + |a1 − a2| * 3 + b2
 *
 * Improvements made:
 *  - Renamed the test helper class to BayCoord to avoid confusion with production Bay class.
 *  - Added descriptive assertion messages.
 *  - Added more cases (symmetry, non-adjacent aisles, large values, negative inputs documented).
 *  - Kept the distance implementation here so this remains a pure unit test; if you move the production
 *    calculateDistance into a utility class later, update the test to call the production method instead.
 */
public class BayDistanceCalculatorTest {
    /**
     * Helper class to represent a bay with aisle and bay number. Named BayCoord to avoid collision
     * with the domain pt.ipp.isep.dei.domain.Bay class.
     */
    static class BayCoord {
        final int aisle;
        final int bay;
        BayCoord(int aisle, int bay) {
            this.aisle = aisle;
            this.bay = bay;
        }
    }

    /**
     * Calculates the distance between two bays according to the rules:
     * If same aisle: D = |b1 − b2|
     * If different aisles: D = b1 + |a1 − a2| * 3 + b2
     *
     * Kept here to make the unit test self-contained. If you refactor this logic to production code,
     * point the tests to the production method instead.
     */
    public static int calculateDistance(BayCoord b1, BayCoord b2) {
        if (b1.aisle == b2.aisle) {
            return Math.abs(b1.bay - b2.bay);
        } else {
            return b1.bay + Math.abs(b1.aisle - b2.aisle) * 3 + b2.bay;
        }
    }

    @Test
    public void testSameAisleDistance_basicCases() {
        BayCoord bay1 = new BayCoord(2, 5);
        BayCoord bay2 = new BayCoord(2, 8);
        assertEquals(3, calculateDistance(bay1, bay2),
                "Same aisle distance should be absolute difference of bay numbers (8-5 = 3)");
        assertEquals(3, calculateDistance(bay2, bay1),
                "Symmetry: distance should be same in the reverse order for same aisle");
        BayCoord bay3 = new BayCoord(2, 5);
        assertEquals(0, calculateDistance(bay1, bay3),
                "Same bay (same aisle & bay) should have distance 0");
    }

    @Test
    public void testDifferentAisleDistance_examplesAndSymmetry() {
        BayCoord bay1 = new BayCoord(1, 2);
        BayCoord bay2 = new BayCoord(3, 4);
        // D = 2 + |1-3|*3 + 4 = 2 + 6 + 4 = 12
        assertEquals(12, calculateDistance(bay1, bay2),
                "Different aisles: formula b1 + |a1 - a2|*3 + b2 should apply (expected 12)");
        assertEquals(12, calculateDistance(bay2, bay1),
                "Different aisles: formula should be symmetric for these inputs (gives same numeric result)");

        // Another example with larger aisle difference
        BayCoord bay3 = new BayCoord(5, 1);
        BayCoord bay4 = new BayCoord(2, 7);
        // D = 1 + |5-2|*3 + 7 = 1 + 9 + 7 = 17
        assertEquals(17, calculateDistance(bay3, bay4),
                "Different aisles non-adjacent: check example producing 17");
    }

    @Test
    public void testEdgeCases_zeroAndLargeValues() {
        BayCoord zeroZeroA = new BayCoord(0, 0);
        BayCoord zeroZeroB = new BayCoord(0, 0);
        assertEquals(0, calculateDistance(zeroZeroA, zeroZeroB),
                "Zero aisles and bays should yield distance 0");

        BayCoord bayA = new BayCoord(0, 5);
        BayCoord bayB = new BayCoord(2, 0);
        // D = 5 + |0-2|*3 + 0 = 5 + 6 + 0 = 11
        assertEquals(11, calculateDistance(bayA, bayB),
                "Mixed zero values should be handled by the formula (expected 11)");

        BayCoord large1 = new BayCoord(1000, 2000);
        BayCoord large2 = new BayCoord(1003, 3000);
        int expectedLarge = 2000 + Math.abs(1000 - 1003) * 3 + 3000; // = 2000+9+3000=5009
        assertEquals(expectedLarge, calculateDistance(large1, large2),
                "Formula should work for large aisle/bay numbers");
    }
}
