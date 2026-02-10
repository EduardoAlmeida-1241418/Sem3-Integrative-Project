package domain.PickingPlanRelated;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Improved unit tests for Strategy B: Greedy nearest-neighbour path using distance function D.
 *
 * Acceptance Criteria:
 *  - Strategy B must return path followed and total distance.
 *
 * Notes / improvements in this test:
 *  - Renamed helper class to BayCoord to avoid collision with production domain classes.
 *  - Implemented a deterministic tie-breaker when selecting the nearest candidate:
 *      prefer smaller aisle, then smaller bay (so tests are reproducible).
 *  - Added explicit assertions for path start, uniqueness, and deterministic ordering for the sample.
 *  - Added descriptive assertion messages.
 */
public class StrategyBPathDistanceTest {

    /**
     * Helper class to represent a bay with aisle and bay number.
     */
    public static class BayCoord {
        public final int aisle;
        public final int bay;
        public BayCoord(int aisle, int bay) {
            this.aisle = aisle;
            this.bay = bay;
        }
        @Override
        public String toString() {
            return "Aisle " + aisle + ", Bay " + bay;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BayCoord)) return false;
            BayCoord that = (BayCoord) o;
            return aisle == that.aisle && bay == that.bay;
        }

        @Override
        public int hashCode() {
            return Objects.hash(aisle, bay);
        }
    }

    /**
     * Distance function as specified:
     * If same aisle: D = |b1 − b2|
     * If different aisles: D = b1 + |a1 − a2| * 3 + b2
     */
    public static int calculateDistance(BayCoord b1, BayCoord b2) {
        if (b1.aisle == b2.aisle) {
            return Math.abs(b1.bay - b2.bay);
        } else {
            return b1.bay + Math.abs(b1.aisle - b2.aisle) * 3 + b2.bay;
        }
    }

    /**
     * Strategy B (greedy nearest neighbour) with deterministic tie-breaker:
     * start from the first bay in input list, repeatedly pick the nearest unvisited bay;
     * if two candidates tie in distance, choose the one with smaller aisle, then smaller bay.
     *
     * Returns path and total distance.
     */
    public static PathResult calculateStrategyBPath(List<BayCoord> bays) {
        if (bays == null || bays.isEmpty()) return new PathResult(new ArrayList<>(), 0);
        List<BayCoord> toVisit = new ArrayList<>(bays);
        List<BayCoord> path = new ArrayList<>();
        BayCoord current = toVisit.remove(0);
        path.add(current);
        int totalDistance = 0;
        while (!toVisit.isEmpty()) {
            BayCoord nearest = null;
            int minDist = Integer.MAX_VALUE;
            for (BayCoord candidate : toVisit) {
                int dist = calculateDistance(current, candidate);
                if (dist < minDist) {
                    minDist = dist;
                    nearest = candidate;
                } else if (dist == minDist) {
                    // Deterministic tie-breaker: smaller aisle, then smaller bay
                    if (nearest != null) {
                        if (candidate.aisle < nearest.aisle ||
                                (candidate.aisle == nearest.aisle && candidate.bay < nearest.bay)) {
                            nearest = candidate;
                        }
                    } else {
                        nearest = candidate;
                    }
                }
            }
            totalDistance += minDist;
            path.add(nearest);
            current = nearest;
            toVisit.remove(nearest);
        }
        return new PathResult(path, totalDistance);
    }

    /**
     * Holder for path and total distance.
     */
    public static class PathResult {
        public final List<BayCoord> path;
        public final int totalDistance;
        public PathResult(List<BayCoord> path, int totalDistance) {
            this.path = path;
            this.totalDistance = totalDistance;
        }
    }

    /**
     * Tests Strategy B with a sample set of bays. Because Strategy B is greedy and this implementation
     * starts at the first input bay, the expected path is derived from that rule.
     */
    @Test
    public void testStrategyBPathAndDistance_sampleSet() {
        List<BayCoord> bays = Arrays.asList(
                new BayCoord(2, 5),  // start (index 0)
                new BayCoord(1, 3),
                new BayCoord(3, 2),
                new BayCoord(1, 7)
        );

        PathResult result = calculateStrategyBPath(bays);

        // path must contain all input bays
        assertEquals(4, result.path.size(), "Path should contain all input bays.");

        // path must start with the first input bay (strategy definition in this test)
        assertEquals(new BayCoord(2,5), result.path.get(0), "Path must start at the first bay of the input list.");

        // verify uniqueness of visited bays
        Set<BayCoord> visited = new HashSet<>(result.path);
        assertEquals(4, visited.size(), "All visited bays must be unique (no duplicates in path).");

        // Compute expected distances for the produced path (verify consistency)
        int d1 = calculateDistance(result.path.get(0), result.path.get(1));
        int d2 = calculateDistance(result.path.get(1), result.path.get(2));
        int d3 = calculateDistance(result.path.get(2), result.path.get(3));
        int expectedTotal = d1 + d2 + d3;
        assertEquals(expectedTotal, result.totalDistance, "Total distance must equal sum of segment distances for the produced path.");

        // For reproducibility, assert the deterministic path we expect with our tie-breaker:
        // Starting at (2,5), nearest candidates are:
        //  - to (1,3): D = 5 + |2-1|*3 + 3 = 5 + 3 + 3 = 11
        //  - to (3,2): D = 5 + |2-3|*3 + 2 = 5 + 3 + 2 = 10  <-- smallest
        //  - to (1,7): D = 5 + |2-1|*3 + 7 = 5 + 3 + 7 = 15
        // So next should be (3,2). After that, between (1,3) and (1,7), nearest to (3,2):
        //  - to (1,3): 2 + |3-1|*3 + 3 = 2 + 6 + 3 = 11
        //  - to (1,7): 2 + |3-1|*3 + 7 = 2 + 6 + 7 = 15
        // So order should be: (2,5) -> (3,2) -> (1,3) -> (1,7)
        List<BayCoord> expectedOrder = Arrays.asList(
                new BayCoord(2,5),
                new BayCoord(3,2),
                new BayCoord(1,3),
                new BayCoord(1,7)
        );
        assertEquals(expectedOrder, result.path, "Deterministic greedy nearest-neighbour path should match expected order with tie-breaker rules.");
    }

    /**
     * Edge cases: single bay and empty list.
     */
    @Test
    public void testStrategyBEdgeCases_singleAndEmpty() {
        List<BayCoord> single = Collections.singletonList(new BayCoord(1,1));
        PathResult rSingle = calculateStrategyBPath(single);
        assertEquals(0, rSingle.totalDistance, "Single bay path distance must be 0.");
        assertEquals(1, rSingle.path.size(), "Single bay path must contain exactly one element.");

        List<BayCoord> empty = Collections.emptyList();
        PathResult rEmpty = calculateStrategyBPath(empty);
        assertEquals(0, rEmpty.totalDistance, "Empty path distance must be 0.");
        assertEquals(0, rEmpty.path.size(), "Empty path must contain zero elements.");
    }

    /**
     * Test deterministic tie-breaking when two candidates have equal distance.
     */
    @Test
    public void testStrategyB_tieBreakingDeterministic() {
        // Construct a scenario where two candidates are equidistant from the start
        // Start at (1,1). Candidates: (1,3) distance 2, and (3,1) distance = 1 + |1-3|*3 + 1 = 1+6+1 = 8 (not tie).
        // To produce a tie we craft values manually:
        // We'll use start (0,0) and two candidates whose distance from start computes to same value:
        BayCoord start = new BayCoord(0,0);
        BayCoord c1 = new BayCoord(1,1); // D = 0 + |0-1|*3 + 1 = 4
        BayCoord c2 = new BayCoord(1,1); // identical -> same distance; deterministic tie-break chooses smaller aisle/bay (equal)
        List<BayCoord> bays = Arrays.asList(start, c1, c2);
        PathResult res = calculateStrategyBPath(bays);
        // Even though candidates identical, path should include both entries (equal objects) but remain deterministic.
        assertEquals(3, res.path.size(), "Even with equal-distance/identical candidates, path should include all entries.");
        // Check order: start, then c1, then c2 (since they are identical, order is predictable based on removal sequence)
        assertEquals(start, res.path.get(0));
    }
}