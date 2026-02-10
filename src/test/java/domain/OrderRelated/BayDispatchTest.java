package domain.OrderRelated;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for bay dispatch logic (improved):
 * - FEFO comparator: expiry (null last) -> receivedDate -> boxId
 * - FIFO comparator: receivedDate -> boxId
 * - Tests full dispatch, partial across bays, empty bays remain, tie-breakers and partial box retention.
 */
public class BayDispatchTest {

    static class Box {
        String boxId;
        int quantity;
        String expiryDate;   // yyyy-MM-dd or null
        String receivedDate; // yyyy-MM-dd
        Box(String boxId, int quantity, String expiryDate, String receivedDate) {
            this.boxId = boxId;
            this.quantity = quantity;
            this.expiryDate = expiryDate;
            this.receivedDate = receivedDate;
        }
    }

    static class Bay {
        String bayId;
        List<Box> boxes = new ArrayList<>();
        Bay(String bayId) { this.bayId = bayId; }
    }

    // FEFO comparator: expiry null last, then expiry asc, then receivedDate asc, then boxId asc
    static Comparator<Box> fefoComparator = Comparator
            .comparing((Box b) -> b.expiryDate, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(b -> b.receivedDate, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(b -> b.boxId);

    // FIFO comparator: receivedDate asc, then boxId asc
    static Comparator<Box> fifoComparator = Comparator
            .comparing((Box b) -> b.receivedDate, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(b -> b.boxId);

    static Map<String, Integer> dispatch(List<Bay> bays, int requestedQty, Comparator<Box> comparator) {
        Map<String, Integer> dispatched = new LinkedHashMap<>();
        int remaining = requestedQty;
        for (Bay bay : bays) {
            bay.boxes.sort(comparator);
            Iterator<Box> it = bay.boxes.iterator();
            while (it.hasNext() && remaining > 0) {
                Box box = it.next();
                int take = Math.min(box.quantity, remaining);
                dispatched.put(box.boxId, take);
                box.quantity -= take;
                remaining -= take;
                if (box.quantity == 0) it.remove();
            }
            if (remaining <= 0) break;
        }
        return dispatched;
    }

    @Test
    public void testFullDispatchSingleBay() {
        Bay bay = new Bay("BAY01");
        bay.boxes.add(new Box("BX001", 10, "2025-10-01", "2025-08-01"));
        List<Bay> bays = Collections.singletonList(bay);
        Map<String, Integer> result = dispatch(bays, 8, fefoComparator);
        assertEquals(Map.of("BX001", 8), result);
        assertEquals(2, bay.boxes.get(0).quantity);
        assertEquals(1, bays.size());
        assertEquals(1, bay.boxes.size());
    }

    @Test
    public void testPartialDispatchAcrossBays() {
        Bay bay1 = new Bay("BAY01");
        bay1.boxes.add(new Box("BX001", 5, "2025-10-01", "2025-08-01"));
        Bay bay2 = new Bay("BAY02");
        bay2.boxes.add(new Box("BX002", 3, "2025-11-01", "2025-09-01"));
        List<Bay> bays = Arrays.asList(bay1, bay2);
        Map<String, Integer> result = dispatch(bays, 7, fefoComparator);
        assertEquals(Map.of("BX001", 5, "BX002", 2), result);
        assertEquals(0, bay1.boxes.size());
        assertEquals(1, bay2.boxes.size());
        assertEquals(1, bay2.boxes.get(0).quantity);
    }

    @Test
    public void testEmptyBaysRemain() {
        Bay bay1 = new Bay("BAY01");
        bay1.boxes.add(new Box("BX001", 2, "2025-10-01", "2025-08-01"));
        Bay bay2 = new Bay("BAY02");
        bay2.boxes.add(new Box("BX002", 1, "2025-11-01", "2025-09-01"));
        List<Bay> bays = Arrays.asList(bay1, bay2);
        Map<String, Integer> result = dispatch(bays, 3, fifoComparator);
        assertEquals(Map.of("BX001", 2, "BX002", 1), result);
        assertEquals(0, bay1.boxes.size());
        assertEquals(0, bay2.boxes.size());
        assertEquals(2, bays.size(), "Bays remain present in the data structure even if empty");
    }

    @Test
    public void testTieBreakersAndPartialRetention() {
        // Two boxes same expiry; FEFO should use receivedDate then boxId
        Bay bay = new Bay("BAY01");
        bay.boxes.add(new Box("BX_A", 3, "2025-10-01", "2025-08-02"));
        bay.boxes.add(new Box("BX_B", 5, "2025-10-01", "2025-08-01")); // older receivedDate -> consumed first
        bay.boxes.add(new Box("BX_C", 4, null, "2025-07-01")); // null expiry -> last
        List<Bay> bays = Collections.singletonList(bay);

        Map<String, Integer> res = dispatch(bays, 6, fefoComparator);
        // Consume from BX_B (5) then BX_A (1)
        assertEquals(Map.of("BX_B", 5, "BX_A", 1), res);
        // BX_B removed; BX_A quantity reduced to 2; BX_C untouched
        assertEquals(2, bay.boxes.stream().filter(bx -> bx.boxId.equals("BX_A")).findFirst().get().quantity);
        assertFalse(bay.boxes.stream().anyMatch(bx -> bx.boxId.equals("BX_B")));
        assertTrue(bay.boxes.stream().anyMatch(bx -> bx.boxId.equals("BX_C")));
    }
}