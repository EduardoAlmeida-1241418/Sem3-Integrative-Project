package domain.ImportRelated;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for dataset import and validation logic.
 * Acceptance Criteria:
 *  - Load datasets: bays.csv, wagons.csv, items.csv, orders.csv, order_lines.csv, returns.csv.
 *  - Validate imported data: detect unknown SKUs, invalid wagons, negative/invalid quantities,
 *    invalid/missing dates/timestamps, uniqueness of boxId.
 *  - Reject invalid records but continue loading valid ones.
 *  - Produce deterministic error messages identifying the exact problem.
 *  - Store valid records in in-memory structures.
 */
public class DatasetImportValidationTest {
    /**
     * Helper classes for each entity.
     */
    static class Bay { String bayId; Bay(String bayId) { this.bayId = bayId; } }
    static class Wagon { String wagonId; Wagon(String wagonId) { this.wagonId = wagonId; } }
    static class Item { String sku; Item(String sku) { this.sku = sku; } }
    static class Order { String orderId; Order(String orderId) { this.orderId = orderId; } }
    static class OrderLine { String orderId; String sku; int qty; OrderLine(String orderId, String sku, int qty) { this.orderId = orderId; this.sku = sku; this.qty = qty; } }
    static class Return { String returnId; String sku; int qty; Return(String returnId, String sku, int qty) { this.returnId = returnId; this.sku = sku; this.qty = qty; } }
    static class Box { String boxId; String sku; int qty; String date; Box(String boxId, String sku, int qty, String date) { this.boxId = boxId; this.sku = sku; this.qty = qty; this.date = date; } }

    /**
     * Simulates loading and validating all datasets, collecting errors and storing valid records.
     */
    @Test
    public void testImportAndValidation() {
        // Mock CSV data (some valid, some invalid)
        List<String> baysCsv = Arrays.asList("BAY01", "BAY02", "BAY03");
        List<String> wagonsCsv = Arrays.asList("WGN01", "WGN02", "INVALID_WGN");
        List<String> itemsCsv = Arrays.asList("SKU01", "SKU02", "SKU03");
        List<String> ordersCsv = Arrays.asList("ORD01", "ORD02");
        List<String> orderLinesCsv = Arrays.asList(
                "ORD01,SKU01,10", // valid
                "ORD01,SKU99,5",  // unknown SKU
                "ORD02,SKU02,-3", // negative qty
                "ORD02,SKU02,abc"  // invalid qty
        );
        List<String> returnsCsv = Arrays.asList(
                "RET01,SKU01,2", // valid
                "RET02,SKU03,-1", // negative qty
                "RET03,SKU04,1"   // unknown SKU
        );
        List<String> boxesCsv = Arrays.asList(
                "BX01,SKU01,5,2025-10-01", // valid
                "BX02,SKU02,3,2025-13-01", // invalid date
                "BX01,SKU03,2,2025-10-02", // duplicate boxId
                "BX03,SKU99,1,2025-10-03"  // unknown SKU
        );

        // In-memory validated-id sets (built from validation steps)
        Set<String> validBayIds = new LinkedHashSet<>();
        Set<String> validWagonIds = new LinkedHashSet<>();
        Set<String> validSkus = new LinkedHashSet<>();
        Set<String> validOrderIds = new LinkedHashSet<>();
        Set<String> boxIds = new HashSet<>();

        List<OrderLine> validOrderLines = new ArrayList<>();
        List<Return> validReturns = new ArrayList<>();
        List<Box> validBoxes = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        // Validate bays (simple format check: non-empty and starts with "BAY")
        for (String b : baysCsv) {
            if (b == null || b.trim().isEmpty()) {
                errors.add("Bay: Invalid bayId: " + b);
                continue;
            }
            if (!b.matches("^BAY\\d+$")) {
                errors.add("Bay: Invalid bayId: " + b);
                continue;
            }
            validBayIds.add(b);
        }

        // Validate wagons (simple rule: must match WGN followed by digits)
        for (String w : wagonsCsv) {
            if (w == null || w.trim().isEmpty()) {
                errors.add("Wagon: Invalid wagonId: " + w);
                continue;
            }
            if (!w.matches("^WGN\\d+$")) {
                errors.add("Wagon: Invalid wagonId: " + w);
                continue;
            }
            validWagonIds.add(w);
        }

        // Validate items (SKU pattern)
        for (String s : itemsCsv) {
            if (s == null || s.trim().isEmpty()) {
                errors.add("Item: Invalid SKU: " + s);
                continue;
            }
            if (!s.matches("^SKU\\d+$")) {
                errors.add("Item: Invalid SKU: " + s);
                continue;
            }
            validSkus.add(s);
        }

        // Validate orders (simple format)
        for (String o : ordersCsv) {
            if (o == null || o.trim().isEmpty()) {
                errors.add("Order: Invalid orderId: " + o);
                continue;
            }
            if (!o.matches("^ORD\\d+$")) {
                errors.add("Order: Invalid orderId: " + o);
                continue;
            }
            validOrderIds.add(o);
        }

        // Validate order lines
        for (String line : orderLinesCsv) {
            String[] parts = line.split(",");
            if (parts.length != 3) {
                errors.add("OrderLine: Invalid format: " + line);
                continue;
            }
            String orderId = parts[0], sku = parts[1];
            String qtyStr = parts[2];
            if (!validOrderIds.contains(orderId)) {
                errors.add("OrderLine: Unknown orderId: " + orderId);
                continue;
            }
            if (!validSkus.contains(sku)) {
                errors.add("OrderLine: Unknown SKU: " + sku);
                continue;
            }
            int qty;
            try { qty = Integer.parseInt(qtyStr); } catch (Exception e) {
                errors.add("OrderLine: Invalid quantity: " + qtyStr);
                continue;
            }
            if (qty < 0) {
                errors.add("OrderLine: Negative quantity: " + qty);
                continue;
            }
            validOrderLines.add(new OrderLine(orderId, sku, qty));
        }

        // Validate returns
        for (String line : returnsCsv) {
            String[] parts = line.split(",");
            if (parts.length != 3) {
                errors.add("Return: Invalid format: " + line);
                continue;
            }
            String returnId = parts[0], sku = parts[1];
            String qtyStr = parts[2];
            if (!validSkus.contains(sku)) {
                errors.add("Return: Unknown SKU: " + sku);
                continue;
            }
            int qty;
            try { qty = Integer.parseInt(qtyStr); } catch (Exception e) {
                errors.add("Return: Invalid quantity: " + qtyStr);
                continue;
            }
            if (qty < 0) {
                errors.add("Return: Negative quantity: " + qty);
                continue;
            }
            validReturns.add(new Return(returnId, sku, qty));
        }

        // Validate boxes
        for (String line : boxesCsv) {
            String[] parts = line.split(",");
            if (parts.length != 4) {
                errors.add("Box: Invalid format: " + line);
                continue;
            }
            String boxId = parts[0], sku = parts[1], qtyStr = parts[2], date = parts[3];
            if (!validSkus.contains(sku)) {
                errors.add("Box: Unknown SKU: " + sku);
                continue;
            }
            if (boxIds.contains(boxId)) {
                errors.add("Box: Duplicate boxId: " + boxId);
                continue;
            }
            boxIds.add(boxId);
            int qty;
            try { qty = Integer.parseInt(qtyStr); } catch (Exception e) {
                errors.add("Box: Invalid quantity: " + qtyStr);
                continue;
            }
            if (qty < 0) {
                errors.add("Box: Negative quantity: " + qty);
                continue;
            }
            if (!isValidDate(date)) {
                errors.add("Box: Invalid date: " + date);
                continue;
            }
            validBoxes.add(new Box(boxId, sku, qty, date));
        }

        // Build expected errors in deterministic order according to validation steps:
        // bays -> wagons -> items -> orders -> orderLines -> returns -> boxes
        List<String> expectedErrors = Arrays.asList(
                // wagons: INVALID_WGN does not match ^WGN\d+$ rule -> will be reported during wagons validation
                "Wagon: Invalid wagonId: INVALID_WGN",
                // order lines
                "OrderLine: Unknown SKU: SKU99",
                "OrderLine: Negative quantity: -3",
                "OrderLine: Invalid quantity: abc",
                // returns
                "Return: Negative quantity: -1",
                "Return: Unknown SKU: SKU04",
                // boxes
                "Box: Invalid date: 2025-13-01",
                "Box: Duplicate boxId: BX01",
                "Box: Unknown SKU: SKU99"
        );

        assertEquals(expectedErrors, errors, "Errors must be detected deterministically and match expectations.");

        // Assert valid records are stored
        assertEquals(1, validOrderLines.size(), "Exactly one valid order line should be stored.");
        assertEquals("ORD01", validOrderLines.get(0).orderId);
        assertEquals("SKU01", validOrderLines.get(0).sku);
        assertEquals(10, validOrderLines.get(0).qty);

        assertEquals(1, validReturns.size(), "Exactly one valid return should be stored.");
        assertEquals("RET01", validReturns.get(0).returnId);
        assertEquals("SKU01", validReturns.get(0).sku);
        assertEquals(2, validReturns.get(0).qty);

        assertEquals(1, validBoxes.size(), "Exactly one valid box should be stored.");
        assertEquals("BX01", validBoxes.get(0).boxId);
        assertEquals("SKU01", validBoxes.get(0).sku);
        assertEquals(5, validBoxes.get(0).qty);
        assertEquals("2025-10-01", validBoxes.get(0).date);
    }

    /**
     * Simple date validation: checks format yyyy-MM-dd and valid month/day.
     */
    private static boolean isValidDate(String date) {
        String[] parts = date.split("-");
        if (parts.length != 3) return false;
        int year, month, day;
        try {
            year = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            day = Integer.parseInt(parts[2]);
        } catch (Exception e) { return false; }
        if (month < 1 || month > 12) return false;
        if (day < 1 || day > 31) return false;
        return true;
    }
}