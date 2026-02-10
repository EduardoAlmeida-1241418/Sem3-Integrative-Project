package domain.OrderRelated;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for relocating a box from one bay to another.
 * <p>
 * Verifies that only bayId is updated, all other attributes remain unchanged,
 * the box is removed from the origin bay, inserted in FEFO position in the destination bay,
 * and the relative order of pre-existing destination boxes is preserved.
 */
public class BoxRelocationTest {
    /**
     * Comparator for FEFO (First Expired, First Out) allocation.
     * Compares boxes by expiryDate (earliest first, nulls last),
     * then receivedDate (oldest first), then receivedTime (oldest first),
     * then boxId (ascending).
     *
     * @param b1 First box to compare
     * @param b2 Second box to compare
     * @return Negative if b1 comes before b2, positive if after, zero if equal
     */
    private static final Comparator<Box> FEFO_COMPARATOR = new Comparator<>() {
        @Override
        public int compare(Box b1, Box b2) {
            if (b1.getExpiryDate() == null && b2.getExpiryDate() != null) return 1;
            if (b1.getExpiryDate() != null && b2.getExpiryDate() == null) return -1;
            if (b1.getExpiryDate() != null && b2.getExpiryDate() != null) {
                int cmp = b1.getExpiryDate().compareTo(b2.getExpiryDate());
                if (cmp != 0) return cmp;
            }
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
     * Unit test for relocating a box from one bay to another.
     * <p>
     * Verifies that only bayId is updated, all other attributes remain unchanged,
     * the box is removed from the origin bay, inserted in FEFO position in the destination bay,
     * and the relative order of pre-existing destination boxes is preserved.
     */
    @Test
    public void testRelocateBox_updatesBayIdAndInsertsFEFO_preservesOtherAttributes_and_keepsRelativeOrder() {
        List<Box> bayOrigin = new ArrayList<>();
        List<Box> bayDest = new ArrayList<>();

        Box boxA = new Box("BX01", "SKU1", 5,
                Date.fromString("2025-11-01", "yyyy-MM-dd"),
                Date.fromString("2025-10-01", "yyyy-MM-dd"), new Time(8, 0, 0));
        boxA.setBayId("B1");

        String beforeSku = boxA.getSkuItem();
        int beforeQty = boxA.getQuantity();
        Object beforeExpiry = boxA.getExpiryDate();
        Object beforeReceivedDate = boxA.getReceivedDate();
        Object beforeReceivedTime = boxA.getReceivedTime();

        bayOrigin.add(boxA);

        Box boxB = new Box("BX02", "SKU1", 5,
                Date.fromString("2025-10-01", "yyyy-MM-dd"),
                Date.fromString("2025-09-01", "yyyy-MM-dd"), new Time(8, 0, 0));
        boxB.setBayId("B2");
        bayDest.add(boxB);

        Box boxC = new Box("BX03", "SKU1", 5,
                Date.fromString("2025-12-01", "yyyy-MM-dd"),
                Date.fromString("2025-09-15", "yyyy-MM-dd"), new Time(8, 0, 0));
        boxC.setBayId("B2");
        bayDest.add(boxC);

        // Record original relative order of destination boxes before insertion
        List<String> destOrderBefore = new ArrayList<>();
        for (Box b : bayDest) destOrderBefore.add(b.getBoxId());

        // Relocation: move box to destination bay (only by Id change)
        boxA.setBayId("B2");

        // Remove from origin list (simulating removal from old bay)
        boolean removed = bayOrigin.remove(boxA);
        assertTrue(removed, "Box should be removed from origin bay list after relocation");

        // Insertion at the correct FEFO position in the destination bay
        int insertPos = 0;
        for (; insertPos < bayDest.size(); insertPos++) {
            if (FEFO_COMPARATOR.compare(boxA, bayDest.get(insertPos)) < 0) {
                break;
            }
        }
        bayDest.add(insertPos, boxA);

        // 1) Only the bayId has changed (other properties remain the same)
        assertEquals("B2", boxA.getBayId(), "Box bayId must be updated to destination");
        assertEquals(beforeSku, boxA.getSkuItem(), "SKU must remain unchanged after relocation");
        assertEquals(beforeQty, boxA.getQuantity(), "Quantity must remain unchanged after relocation");
        assertEquals(beforeExpiry, boxA.getExpiryDate(), "Expiry date must remain unchanged after relocation");
        assertEquals(beforeReceivedDate, boxA.getReceivedDate(), "Received date must remain unchanged after relocation");
        assertEquals(beforeReceivedTime, boxA.getReceivedTime(), "Received time must remain unchanged after relocation");

        // 2) Box removed from origin
        assertFalse(bayOrigin.contains(boxA), "Origin bay should no longer contain the relocated box");

        // 3) Insertion position matches comparator-based insertion
        assertTrue(insertPos >= 0 && insertPos <= bayDest.size() - 1, "Insertion index must be within destination list bounds");
        assertEquals(boxA, bayDest.get(insertPos), "BoxA must be present at the computed FEFO insertion index");

        // 4) The relative order of pre-existing destination boxes must be preserved
        List<String> destOrderAfter = new ArrayList<>();
        for (Box b : bayDest) destOrderAfter.add(b.getBoxId());
        // Remove the relocated id from after-order to compare relative order of original elements
        List<String> afterWithoutRelocated = new ArrayList<>(destOrderAfter);
        afterWithoutRelocated.remove("BX01");
        assertEquals(destOrderBefore, afterWithoutRelocated, "Relative order of pre-existing destination boxes must be preserved (no global re-sort)");

        // 5) Final list is consistent with FEFO comparator
        List<Box> expectedOrder = new ArrayList<>(bayDest);
        expectedOrder.sort(FEFO_COMPARATOR);
        assertEquals(expectedOrder, bayDest, "Destination bay must be in FEFO order after insertion (sanity)");

        // 6) Explicit expected positions for clarity
        assertEquals("BX02", bayDest.get(0).getBoxId(), "BX02 expected first in FEFO");
        assertEquals("BX01", bayDest.get(1).getBoxId(), "BX01 expected second in FEFO (inserted)");
        assertEquals("BX03", bayDest.get(2).getBoxId(), "BX03 expected third in FEFO");
    }
}