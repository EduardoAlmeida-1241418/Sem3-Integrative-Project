package domain.OrderRelated;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.domain.Time;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FEFO and FIFO allocation logic for boxes in bays.
 * <p>
 * Acceptance Criteria:
 * <ul>
 *   <li>Allocation iterates through SKU boxes in FEFO/FIFO order.</li>
 *   <li>Box quantity never goes below zero during allocation.</li>
 * </ul>
 */
public class BoxAllocationTest {
    /**
     * Comparator for FEFO (First Expired, First Out) allocation.
     * <ul>
     *   <li>Sorts by expiryDate (earliest first, nulls last)</li>
     *   <li>Then by receivedDate (oldest first)</li>
     *   <li>Then by receivedTime (oldest first)</li>
     *   <li>Then by boxId (ascending)</li>
     * </ul>
     */
    private static final Comparator<Box> FEFO_COMPARATOR = new Comparator<>() {
        /**
         * Compares two boxes according to FEFO rules.
         * @param b1 the first box
         * @param b2 the second box
         * @return negative if b1 comes before b2, positive if after, zero if equal
         */
        @Override
        public int compare(Box b1, Box b2) {
            if (b1.getExpiryDate() == null && b2.getExpiryDate() != null) return 1;
            if (b1.getExpiryDate() != null && b2.getExpiryDate() == null) return -1;
            if (b1.getExpiryDate() != null && b2.getExpiryDate() != null) {
                int cmp = b1.getExpiryDate().compareTo(b2.getExpiryDate());
                if (cmp != 0) return cmp;
            }
            // expiry equal (or both null) -> compare received date/time (oldest first)
            if (b1.getReceivedDate() != null && b2.getReceivedDate() != null) {
                int cmpDate = b1.getReceivedDate().compareTo(b2.getReceivedDate());
                if (cmpDate != 0) return cmpDate;
            }
            if (b1.getReceivedTime() != null && b2.getReceivedTime() != null) {
                int cmpTime = b1.getReceivedTime().compareTo(b2.getReceivedTime());
                if (cmpTime != 0) return cmpTime;
            }
            return b1.getBoxId().compareTo(b2.getBoxId());
        }
    };

    /**
     * Comparator for FIFO (First In, First Out) allocation.
     * <ul>
     *   <li>Sorts by receivedDate (oldest first)</li>
     *   <li>Then by receivedTime (oldest first)</li>
     *   <li>Then by boxId (ascending)</li>
     * </ul>
     */
    private static final Comparator<Box> FIFO_COMPARATOR = new Comparator<>() {
        /**
         * Compares two boxes according to FIFO rules.
         * @param b1 the first box
         * @param b2 the second box
         * @return negative if b1 comes before b2, positive if after, zero if equal
         */
        @Override
        public int compare(Box b1, Box b2) {
            if (b1.getReceivedDate() != null && b2.getReceivedDate() != null) {
                int cmpDate = b1.getReceivedDate().compareTo(b2.getReceivedDate());
                if (cmpDate != 0) return cmpDate;
            }
            if (b1.getReceivedTime() != null && b2.getReceivedTime() != null) {
                int cmpTime = b1.getReceivedTime().compareTo(b2.getReceivedTime());
                if (cmpTime != 0) return cmpTime;
            }
            return b1.getBoxId().compareTo(b2.getBoxId());
        }
    };

    /**
     * Tests FEFO allocation logic:
     * <ul>
     *   <li>Ensures boxes are sorted by FEFO (expiry, received, id)</li>
     *   <li>Simulates allocation of quantities for an OrderLine</li>
     *   <li>Ensures box quantity never goes below zero</li>
     *   <li>Verifies correct allocation and final box quantities</li>
     * </ul>
     */
    @Test
    public void allocateQuantitiesFromBoxes_FEFOOrder_includingReceivedAtTie_breaksAndNeverBelowZero() {
        // Setup: 3 boxes same SKU, two have same expiry but different received dates/times
        String sku = "SKU0001";
        // boxA expiry 2025-10-01 received 2025-08-01 08:00
        Box boxA = new Box("BXA01", sku, 5, Date.fromString("2025-10-01", "yyyy-MM-dd"),
                Date.fromString("2025-08-01", "yyyy-MM-dd"), new Time(8, 0, 0));
        // boxB expiry 2025-10-01 received 2025-07-31 09:30 (older than A -> should come first among same expiry)
        Box boxB = new Box("BXB01", sku, 10, Date.fromString("2025-10-01", "yyyy-MM-dd"),
                Date.fromString("2025-07-31", "yyyy-MM-dd"), new Time(9, 30, 0));
        // boxC expiry 2025-11-01 received later
        Box boxC = new Box("BXC01", sku, 7, Date.fromString("2025-11-01", "yyyy-MM-dd"),
                Date.fromString("2025-09-01", "yyyy-MM-dd"), new Time(10, 0, 0));

        // Ensure explicit available quantities
        boxA.setQuantity(5);
        boxB.setQuantity(10);
        boxC.setQuantity(7);

        List<Box> boxes = new ArrayList<>();
        boxes.add(boxA);
        boxes.add(boxB);
        boxes.add(boxC);

        // Sort by FEFO comparator (expiry then receivedAt then boxId)
        boxes.sort(FEFO_COMPARATOR);

        // Sanity: expected FEFO order should place boxB (same expiry but older receivedAt) before boxA
        assertEquals("BXB01", boxes.get(0).getBoxId());
        assertEquals("BXA01", boxes.get(1).getBoxId());
        assertEquals("BXC01", boxes.get(2).getBoxId());

        // Simulate allocation for an order line needing 18 units
        OrderLine line = new OrderLine("ORD00001", 1, sku, 18);
        int remainingQty = line.getQuantity();
        List<Integer> allocated = new ArrayList<>();

        for (Box box : boxes) {
            int alloc = Math.min(remainingQty, box.getQuantity());
            allocated.add(alloc);
            box.setQuantity(box.getQuantity() - alloc);
            remainingQty -= alloc;
            // Assert box quantity never goes below zero
            assertTrue(box.getQuantity() >= 0, "Box quantity went below zero!");
            if (remainingQty == 0) break;
        }

        // Expected allocation: boxB (10), boxA (5), boxC (3)
        assertEquals(10, allocated.get(0));
        assertEquals(5, allocated.get(1));
        assertEquals(3, allocated.get(2));
        assertEquals(0, remainingQty);

        // Final quantities
        assertEquals(0, boxB.getQuantity());
        assertEquals(0, boxA.getQuantity());
        assertEquals(4, boxC.getQuantity());
    }

    /**
     * Tests FIFO allocation logic:
     * <ul>
     *   <li>Ensures boxes are sorted by FIFO (received, id)</li>
     *   <li>Simulates allocation of quantities for an OrderLine</li>
     *   <li>Ensures box quantity never goes below zero</li>
     *   <li>Verifies correct allocation and final box quantities</li>
     * </ul>
     */
    @Test
    public void allocateQuantitiesFromBoxes_FIFOOrder_neverBelowZero_andOrderByReceivedAt() {
        // Setup FIFO-specific case: same SKU, different received dates/times
        String sku = "SKU0002";
        Box f1 = new Box("BF001", sku, 6, null,
                Date.fromString("2025-06-01", "yyyy-MM-dd"), new Time(7, 0, 0));
        Box f2 = new Box("BF002", sku, 4, null,
                Date.fromString("2025-05-15", "yyyy-MM-dd"), new Time(9, 0, 0));
        Box f3 = new Box("BF003", sku, 5, null,
                Date.fromString("2025-06-10", "yyyy-MM-dd"), new Time(8, 0, 0));

        f1.setQuantity(6);
        f2.setQuantity(4);
        f3.setQuantity(5);

        List<Box> boxes = new ArrayList<>();
        boxes.add(f1);
        boxes.add(f2);
        boxes.add(f3);

        // Sort by FIFO comparator (receivedDate/time then boxId)
        boxes.sort(FIFO_COMPARATOR);

        // Expected FIFO order: f2 (2025-05-15), f1 (2025-06-01 07:00), f3 (2025-06-10)
        assertEquals("BF002", boxes.get(0).getBoxId());
        assertEquals("BF001", boxes.get(1).getBoxId());
        assertEquals("BF003", boxes.get(2).getBoxId());

        // Simulate allocation for an order line needing 10 units
        OrderLine line = new OrderLine("ORD00002", 1, sku, 10);
        int remainingQty = line.getQuantity();
        List<Integer> allocated = new ArrayList<>();

        for (Box box : boxes) {
            int alloc = Math.min(remainingQty, box.getQuantity());
            allocated.add(alloc);
            box.setQuantity(box.getQuantity() - alloc);
            remainingQty -= alloc;
            assertTrue(box.getQuantity() >= 0, "Box quantity went below zero!");
            if (remainingQty == 0) break;
        }

        // Expected: f2 (4), f1 (6) -> allocation satisfied, f3 untouched
        assertEquals(4, allocated.get(0));
        assertEquals(6, allocated.get(1));
        assertEquals(0, remainingQty);

        // Final quantities
        assertEquals(0, f2.getQuantity());
        assertEquals(0, f1.getQuantity());
        assertEquals(5, f3.getQuantity()); // untouched
    }
}