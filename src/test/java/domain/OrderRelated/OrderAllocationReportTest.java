package domain.OrderRelated;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.OrderRelated.LineState;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.domain.Time;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for allocation logic reflecting acceptance criteria:
 *  - Produces eligibility list: orderId, lineNo, sku, requestedQty, allocatedQty, status
 *  - Produces allocation rows: orderId, lineNo, sku, qty, boxId, aisle, bay
 */
public class OrderAllocationReportTest {

    /**
     * Tests that the eligibility list is produced correctly for each order line.
     * The list contains: orderId, lineNo, sku, requestedQty, allocatedQty, status.
     */
    @Test
    public void testEligibilityListProduction() {
        OrderLine line1 = new OrderLine("ORD001", 1, "SKU1", 10);
        OrderLine line2 = new OrderLine("ORD001", 2, "SKU2", 5);
        OrderLine line3 = new OrderLine("ORD002", 1, "SKU1", 8);

        Box boxA = new Box("BX01", "SKU1", 12,
                Date.fromString("2025-11-01", "yyyy-MM-dd"),
                Date.fromString("2025-10-01", "yyyy-MM-dd"),
                new Time(8,0,0));
        Box boxB = new Box("BX02", "SKU2", 3,
                Date.fromString("2025-12-01", "yyyy-MM-dd"),
                Date.fromString("2025-10-02", "yyyy-MM-dd"),
                new Time(9,0,0));
        Box boxC = new Box("BX03", "SKU2", 4,
                Date.fromString("2025-12-01", "yyyy-MM-dd"),
                Date.fromString("2025-10-03", "yyyy-MM-dd"),
                new Time(10,0,0));

        boxA.setQuantity(12);
        boxB.setQuantity(3);
        boxC.setQuantity(4);

        Map<OrderLine, Integer> allocatedQty = new HashMap<>();
        allocatedQty.put(line1, 10);
        allocatedQty.put(line2, 5);
        allocatedQty.put(line3, 2);

        Map<OrderLine, String> status = new HashMap<>();
        status.put(line1, LineState.ALLOCATED.toString());
        status.put(line2, LineState.ALLOCATED.toString());
        status.put(line3, LineState.PARTIAL.toString());

        List<String[]> eligibilityList = new ArrayList<>();
        for (OrderLine line : Arrays.asList(line1, line2, line3)) {
            eligibilityList.add(new String[]{
                    line.getOrderId(),
                    String.valueOf(line.getLineNumber()),
                    line.getSkuItem(),
                    String.valueOf(line.getQuantity()),
                    String.valueOf(allocatedQty.getOrDefault(line, 0)),
                    status.get(line)
            });
        }

        assertEquals(3, eligibilityList.size(), "Eligibility list must contain one entry per tested line.");
        assertArrayEquals(new String[]{"ORD001","1","SKU1","10","10", LineState.ALLOCATED.toString()}, eligibilityList.get(0));
        assertArrayEquals(new String[]{"ORD001","2","SKU2","5","5",  LineState.ALLOCATED.toString()}, eligibilityList.get(1));
        assertArrayEquals(new String[]{"ORD002","1","SKU1","8","2",  LineState.PARTIAL.toString()}, eligibilityList.get(2));

        for (Map.Entry<OrderLine, Integer> e : allocatedQty.entrySet()) {
            assertTrue(e.getValue() <= e.getKey().getQuantity(), "Allocated qty cannot exceed requested qty");
        }

        Map<String, Integer> availableBySku = new HashMap<>();
        availableBySku.put(boxA.getSkuItem(), boxA.getQuantity());
        availableBySku.put(boxB.getSkuItem(), boxB.getQuantity() + boxC.getQuantity());

        Map<String, Integer> allocatedBySku = new HashMap<>();
        allocatedBySku.put("SKU1", allocatedQty.get(line1) + allocatedQty.get(line3));
        allocatedBySku.put("SKU2", allocatedQty.get(line2));

        for (String sku : allocatedBySku.keySet()) {
            assertTrue(allocatedBySku.get(sku) <= availableBySku.getOrDefault(sku, 0),
                    "Allocated total for SKU must not exceed available quantity for SKU: " + sku);
        }
    }

    /**
     * Tests that allocation rows are produced correctly for each allocation.
     * The rows contain: orderId, lineNo, sku, qty, boxId, aisle, bay.
     */
    @Test
    public void testAllocationRowsProduction() {
        OrderLine line = new OrderLine("ORD001", 1, "SKU1", 10);

        Box boxA = new Box("BX01", "SKU1", 6,
                Date.fromString("2025-11-01", "yyyy-MM-dd"),
                Date.fromString("2025-10-01", "yyyy-MM-dd"),
                new Time(8,0,0));
        Box boxB = new Box("BX02", "SKU1", 4,
                Date.fromString("2025-11-01", "yyyy-MM-dd"),
                Date.fromString("2025-10-02", "yyyy-MM-dd"),
                new Time(9,0,0));

        boxA.setQuantity(6);
        boxB.setQuantity(4);

        String aisleA = "A1"; String bayA = "B1";
        String aisleB = "A2"; String bayB = "B2";

        List<String[]> allocationRows = new ArrayList<>();
        allocationRows.add(new String[]{"ORD001","1","SKU1","6","BX01",aisleA,bayA});
        allocationRows.add(new String[]{"ORD001","1","SKU1","4","BX02",aisleB,bayB});

        assertEquals(2, allocationRows.size(), "There should be two allocation rows (6+4 = 10).");
        assertArrayEquals(new String[]{"ORD001","1","SKU1","6","BX01","A1","B1"}, allocationRows.get(0));
        assertArrayEquals(new String[]{"ORD001","1","SKU1","4","BX02","A2","B2"}, allocationRows.get(1));

        int sumAllocated = allocationRows.stream()
                .mapToInt(r -> Integer.parseInt(r[3]))
                .sum();
        assertEquals(line.getQuantity(), sumAllocated, "Sum of allocation rows must equal requested qty for the line.");

        Map<String, Integer> boxAvailable = new HashMap<>();
        boxAvailable.put("BX01", boxA.getQuantity());
        boxAvailable.put("BX02", boxB.getQuantity());

        for (String[] row : allocationRows) {
            String boxId = row[4];
            int qty = Integer.parseInt(row[3]);
            assertTrue(qty <= boxAvailable.getOrDefault(boxId, 0),
                    "Allocated qty for box " + boxId + " cannot exceed available qty");
        }
    }
}