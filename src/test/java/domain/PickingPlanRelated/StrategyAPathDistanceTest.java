package domain.PickingPlanRelated;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Strategy A: Sort bays ascending by aisle and calculate path distance using D.
 * <p>
 * Acceptance Criteria:
 * <ul>
 *   <li>Strategy A must return path followed and total distance.</li>
 * </ul>
 * <p>
 * Improvements:
 * <ul>
 *   <li>Renamed helper class to BayCoord to avoid collision with production Bay class.</li>
 *   <li>Made sorting deterministic: sort by aisle ascending, then by bay ascending (still meets "sort by aisle" acceptance).</li>
 *   <li>Added descriptive assertion messages and an extra test for ordering within same aisle.</li>
 *   <li>Kept distance formula as specified.</li>
 * </ul>
 * <p>
 * Test methods:
 * <ul>
 *   <li>{@link #testStrategyAPathAndDistance_sampleSet()} - Verifies path and total distance for a sample set of bays.</li>
 *   <li>{@link #testStrategyAEdgeCases_singleAndEmpty()} - Verifies edge cases for single and empty bay lists.</li>
 *   <li>{@link #testStrategyASameAisleOrdering_and_symmetry()} - Verifies deterministic ordering and symmetry for same aisle.</li>
 *   <li>{@link #testStrategyALargeValues_and_nonAdjacentAisles()} - Verifies correct calculation for large values and non-adjacent aisles.</li>
 * </ul>
 */
public class StrategyAPathDistanceTest {
    /**
     * Helper class to represent a bay with aisle and bay number.
     * Named BayCoord to avoid conflict with domain classes.
     */
    static class BayCoord {
        /** The aisle number of the bay. */
        final int aisle;
        /** The bay number within the aisle. */
        final int bay;
        /**
         * Constructs a BayCoord with the given aisle and bay numbers.
         * @param aisle the aisle number
         * @param bay the bay number
         */
        BayCoord(int aisle, int bay) {
            this.aisle = aisle;
            this.bay = bay;
        }
        /**
         * Returns a string representation of the bay coordinates.
         * @return formatted string "Aisle X, Bay Y"
         */
        @Override
        public String toString() {
            return "Aisle " + aisle + ", Bay " + bay;
        }
    }

    /**
     * Distance function as specified:
     * <ul>
     *   <li>If same aisle: D = |b1 − b2|</li>
     *   <li>If different aisles: D = b1 + |a1 − a2| * 3 + b2</li>
     * </ul>
     * @param b1 the first bay
     * @param b2 the second bay
     * @return the calculated distance D
     */
    public static int calculateDistance(BayCoord b1, BayCoord b2) {
        if (b1.aisle == b2.aisle) {
            return Math.abs(b1.bay - b2.bay);
        } else {
            return b1.bay + Math.abs(b1.aisle - b2.aisle) * 3 + b2.bay;
        }
    }

    /**
     * Strategy A: Sort bays ascending by aisle (and by bay as tie-breaker for determinism),
     * then calculate path sequentially and total distance.
     * @param bays list of bays to visit
     * @return PathResult containing the sorted path and total distance
     */
    public static PathResult calculateStrategyAPath(List<BayCoord> bays) {
        List<BayCoord> sorted = new ArrayList<>(bays);
        // Primary: aisle asc. Secondary: bay asc (deterministic).
        sorted.sort(Comparator.comparingInt((BayCoord b) -> b.aisle).thenComparingInt(b -> b.bay));
        int totalDistance = 0;
        for (int i = 0; i < sorted.size() - 1; i++) {
            totalDistance += calculateDistance(sorted.get(i), sorted.get(i + 1));
        }
        return new PathResult(sorted, totalDistance);
    }

    /**
     * Helper class to hold the path and total distance.
     */
    static class PathResult {
        /** The path followed (sorted list of bays). */
        final List<BayCoord> path;
        /** The total distance of the path. */
        final int totalDistance;
        /**
         * Constructs a PathResult with the given path and total distance.
         * @param path the path followed
         * @param totalDistance the total distance
         */
        PathResult(List<BayCoord> path, int totalDistance) {
            this.path = path;
            this.totalDistance = totalDistance;
        }
    }

    /**
     * Verifies path and total distance for a sample set of bays.
     * Checks sorting, path order, and total distance calculation.
     */
    @Test
    public void testStrategyAPathAndDistance_sampleSet() {
        List<BayCoord> bays = Arrays.asList(
                new BayCoord(2, 5),
                new BayCoord(1, 3),
                new BayCoord(3, 2),
                new BayCoord(1, 7)
        );

        PathResult result = calculateStrategyAPath(bays);

        // Expected sorted path by aisle then bay: (1,3), (1,7), (2,5), (3,2)
        assertEquals(4, result.path.size(), "Path should contain all input bays in sorted order.");
        assertEquals(1, result.path.get(0).aisle, "First path aisle mismatch.");
        assertEquals(3, result.path.get(0).bay, "First path bay mismatch.");
        assertEquals(1, result.path.get(1).aisle, "Second path aisle mismatch.");
        assertEquals(7, result.path.get(1).bay, "Second path bay mismatch.");
        assertEquals(2, result.path.get(2).aisle, "Third path aisle mismatch.");
        assertEquals(5, result.path.get(2).bay, "Third path bay mismatch.");
        assertEquals(3, result.path.get(3).aisle, "Fourth path aisle mismatch.");
        assertEquals(2, result.path.get(3).bay, "Fourth path bay mismatch.");

        // Expected total distance:
        // (1,3) -> (1,7): |3-7| = 4
        // (1,7) -> (2,5): 7 + |1-2|*3 + 5 = 7 + 3 + 5 = 15
        // (2,5) -> (3,2): 5 + |2-3|*3 + 2 = 5 + 3 + 2 = 10
        int expectedTotal = 4 + 15 + 10;
        assertEquals(expectedTotal, result.totalDistance, "Total distance must match the sum of segment distances.");
    }

    /**
     * Verifies edge cases for single and empty bay lists.
     * Checks that total distance is zero and path size is correct.
     */
    @Test
    public void testStrategyAEdgeCases_singleAndEmpty() {
        List<BayCoord> single = Collections.singletonList(new BayCoord(1, 1));
        PathResult resultSingle = calculateStrategyAPath(single);
        assertEquals(0, resultSingle.totalDistance, "Single-bay path must have zero distance.");
        assertEquals(1, resultSingle.path.size(), "Single-bay path must contain one element.");

        List<BayCoord> empty = Collections.emptyList();
        PathResult resultEmpty = calculateStrategyAPath(empty);
        assertEquals(0, resultEmpty.totalDistance, "Empty path must have zero distance.");
        assertEquals(0, resultEmpty.path.size(), "Empty path must contain zero elements.");
    }

    /**
     * Verifies deterministic ordering and symmetry for same aisle.
     * Checks that sorting is by bay within the same aisle and that distance is symmetric.
     */
    @Test
    public void testStrategyASameAisleOrdering_and_symmetry() {
        // Inputs with same aisle but unsorted bays; deterministic tie-breaker by bay ensures expected ordering.
        List<BayCoord> bays = Arrays.asList(
                new BayCoord(2, 10),
                new BayCoord(2, 2),
                new BayCoord(2, 7)
        );
        PathResult res = calculateStrategyAPath(bays);

        // After deterministic sort by aisle then bay: (2,2), (2,7), (2,10)
        assertEquals(3, res.path.size(), "Expect 3 path points.");
        assertEquals(2, res.path.get(0).bay, "First bay should be the smallest bay number in the same aisle.");
        assertEquals(7, res.path.get(1).bay, "Second bay should be the middle bay number in the same aisle.");
        assertEquals(10, res.path.get(2).bay, "Third bay should be the largest bay number in the same aisle.");

        // Distances should be symmetric for individual segments when swapping endpoints:
        int d01 = calculateDistance(res.path.get(0), res.path.get(1));
        int d10 = calculateDistance(res.path.get(1), res.path.get(0));
        assertEquals(d01, d10, "Distance formula should be symmetric for same-aisle segments.");
    }

    /**
     * Verifies correct calculation for large values and non-adjacent aisles.
     * Checks sorting and total distance for bays with large aisle numbers.
     */
    @Test
    public void testStrategyALargeValues_and_nonAdjacentAisles() {
        List<BayCoord> bays = Arrays.asList(
                new BayCoord(1, 1),
                new BayCoord(10, 5),
                new BayCoord(3, 2)
        );
        PathResult res = calculateStrategyAPath(bays);
        // Sorted by aisle then bay: (1,1), (3,2), (10,5)
        assertEquals(3, res.path.size());
        assertEquals(1, res.path.get(0).aisle);
        assertEquals(3, res.path.get(1).aisle);
        assertEquals(10, res.path.get(2).aisle);

        // compute expected distances manually to verify total
        int seg1 = calculateDistance(res.path.get(0), res.path.get(1)); // (1,1)->(3,2): 1 + |1-3|*3 + 2 = 1 + 6 + 2 = 9
        int seg2 = calculateDistance(res.path.get(1), res.path.get(2)); // (3,2)->(10,5): 2 + |3-10|*3 + 5 = 2 + 21 + 5 = 28
        assertEquals(seg1 + seg2, res.totalDistance, "Total distance must equal sum of computed segments for non-adjacent aisles.");
    }
}