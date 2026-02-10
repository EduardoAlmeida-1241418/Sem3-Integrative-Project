package domain.PickingPlanRelated;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for merging duplicate bays in a picking plan (USEI03).
 * This is a pure unit test: it verifies the merge logic in isolation (a local helper method mergeStops).
 * Acceptance Criteria:
 *   - Duplicate bays must be merged into a single stop before sequencing.
 */
public class PickingPlanBayMergeTest {

    @Test
    public void testMergeDuplicateBays_basicCase() {
        List<BayStop> stops = Arrays.asList(
                new BayStop("A", 5),
                new BayStop("B", 3),
                new BayStop("A", 2),
                new BayStop("C", 4),
                new BayStop("B", 1)
        );

        List<BayStop> merged = mergeStops(stops);

        List<BayStop> expected = Arrays.asList(
                new BayStop("A", 7),
                new BayStop("B", 4),
                new BayStop("C", 4)
        );

        assertEquals(expected, merged, "Merged stops should sum quantities and preserve first-occurrence order");
    }

    @Test
    public void testMergeDuplicateBays_preservesFirstOccurrenceOrder() {
        List<BayStop> stops = Arrays.asList(
                new BayStop("X", 1),
                new BayStop("Y", 2),
                new BayStop("Z", 3),
                new BayStop("Y", 5),
                new BayStop("X", 4)
        );

        List<BayStop> merged = mergeStops(stops);

        List<BayStop> expected = Arrays.asList(
                new BayStop("X", 5),
                new BayStop("Y", 7),
                new BayStop("Z", 3)
        );

        assertEquals(expected, merged, "Merged list must preserve the order of first occurrence (X, Y, Z)");
    }

    @Test
    public void testMergeDuplicateBays_emptyInput_returnsEmpty() {
        List<BayStop> stops = Collections.emptyList();
        List<BayStop> merged = mergeStops(stops);
        assertTrue(merged.isEmpty(), "Merging an empty list should return an empty list");
    }

    @Test
    public void testMergeDuplicateBays_allDuplicates() {
        List<BayStop> stops = Arrays.asList(
                new BayStop("A", 1),
                new BayStop("A", 2),
                new BayStop("A", 3)
        );
        List<BayStop> merged = mergeStops(stops);
        assertEquals(1, merged.size(), "All duplicated stops must collapse to a single stop");
        assertEquals(new BayStop("A", 6), merged.get(0));
    }

    @Test
    public void testMergeDuplicateBays_negativeOrZeroQuantities_handled() {
        // Behavior: sums as-is (if domain must reject negatives, production should validate).
        List<BayStop> stops = Arrays.asList(
                new BayStop("A", 5),
                new BayStop("B", 0),
                new BayStop("A", -2),
                new BayStop("B", 4)
        );
        List<BayStop> merged = mergeStops(stops);

        // A: 5 + (-2) = 3, B: 0 + 4 = 4
        assertTrue(merged.contains(new BayStop("A", 3)));
        assertTrue(merged.contains(new BayStop("B", 4)));
    }

    // Helper method that performs the merge (unit under test)
    private List<BayStop> mergeStops(List<BayStop> stops) {
        Map<String, Integer> merged = new LinkedHashMap<>(); // preserves insertion (first occurrence) order
        for (BayStop stop : stops) {
            if (stop == null || stop.bayId == null) {
                // choose to skip null entries to keep behavior predictable in unit test
                continue;
            }
            merged.put(stop.bayId, merged.getOrDefault(stop.bayId, 0) + stop.quantity);
        }
        List<BayStop> mergedStops = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : merged.entrySet()) {
            mergedStops.add(new BayStop(entry.getKey(), entry.getValue()));
        }
        return mergedStops;
    }

    /**
     * Simple helper class to represent a stop in the picking plan.
     * Implements equals/hashCode to allow direct list comparisons in asserts.
     */
    static class BayStop {
        final String bayId;
        final int quantity;

        BayStop(String bayId, int quantity) {
            this.bayId = bayId;
            this.quantity = quantity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BayStop)) return false;
            BayStop bayStop = (BayStop) o;
            return quantity == bayStop.quantity && Objects.equals(bayId, bayStop.bayId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(bayId, quantity);
        }

        @Override
        public String toString() {
            return "BayStop{" + "bayId='" + bayId + '\'' + ", quantity=" + quantity + '}';
        }
    }
}