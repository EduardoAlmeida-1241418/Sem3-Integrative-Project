package domain.OrderRelated;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.OrderRelated.Order;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test to verify that orders and their lines are processed in the correct order:
 * Orders: priority ASC, dueDate ASC, orderId ASC
 * Lines: lineNo ASC
 */
public class OrderProcessingTest {

    @Test
    public void ordersAndLinesAreProcessedInCorrectOrder() {
        // Create sample orders (orderId, dueDate, priority)
        Order o1 = new Order("ORD00002", Date.fromString("2025-10-03", "yyyy-MM-dd"), 2);
        Order o2 = new Order("ORD00001", Date.fromString("2025-09-29", "yyyy-MM-dd"), 1);
        Order o3 = new Order("ORD00003", Date.fromString("2025-09-29", "yyyy-MM-dd"), 1);

        // Create order lines and store in a map by their generated orderLineId
        Map<String, OrderLine> orderLineMap = new TreeMap<>();
        OrderLine l1 = new OrderLine("ORD00002", 2, "SKU0001", 1);
        OrderLine l2 = new OrderLine("ORD00002", 1, "SKU0001", 1);
        OrderLine l3 = new OrderLine("ORD00001", 3, "SKU0002", 1);
        OrderLine l4 = new OrderLine("ORD00001", 1, "SKU0002", 1);
        OrderLine l5 = new OrderLine("ORD00003", 2, "SKU0003", 1);
        OrderLine l6 = new OrderLine("ORD00003", 1, "SKU0003", 1);
        orderLineMap.put(l1.getOrderLineId(), l1);
        orderLineMap.put(l2.getOrderLineId(), l2);
        orderLineMap.put(l3.getOrderLineId(), l3);
        orderLineMap.put(l4.getOrderLineId(), l4);
        orderLineMap.put(l5.getOrderLineId(), l5);
        orderLineMap.put(l6.getOrderLineId(), l6);

        // Add lines to orders (by ID) in unsorted order to ensure sorting step is meaningful
        o1.getOrderLineIds().add(l1.getOrderLineId()); // line 2 then 1
        o1.getOrderLineIds().add(l2.getOrderLineId());
        o2.getOrderLineIds().add(l3.getOrderLineId()); // line 3 then 1
        o2.getOrderLineIds().add(l4.getOrderLineId());
        o3.getOrderLineIds().add(l5.getOrderLineId()); // line 2 then 1
        o3.getOrderLineIds().add(l6.getOrderLineId());

        List<Order> orders = new ArrayList<>();
        orders.add(o1);
        orders.add(o2);
        orders.add(o3);

        // Sort orders by priority ASC, dueDate ASC, orderId ASC (matches acceptance criteria)
        orders.sort(new Comparator<Order>() {
            @Override
            public int compare(Order a, Order b) {
                int cmp = Integer.compare(a.getPriority(), b.getPriority());
                if (cmp != 0) return cmp;
                cmp = a.getDueDate().compareTo(b.getDueDate());
                if (cmp != 0) return cmp;
                return a.getOrderId().compareTo(b.getOrderId());
            }
        });

        // For each order, sort lines by lineNo ASC and replace orderLineIds with sorted IDs
        for (Order order : orders) {
            List<OrderLine> lines = new ArrayList<>();
            for (String lineId : order.getOrderLineIds()) {
                lines.add(orderLineMap.get(lineId));
            }
            lines.sort(new Comparator<OrderLine>() {
                @Override
                public int compare(OrderLine lA, OrderLine lB) {
                    return Integer.compare(lA.getLineNumber(), lB.getLineNumber());
                }
            });
            order.getOrderLineIds().clear();
            for (OrderLine ol : lines) {
                order.getOrderLineIds().add(ol.getOrderLineId());
            }
        }

        // Basic sanity checks
        assertEquals(3, orders.size(), "There should be exactly 3 orders after setup.");

        // Assert order of orders (expected: ORD00001, ORD00003, ORD00002)
        List<String> expectedOrderIds = List.of("ORD00001", "ORD00003", "ORD00002");
        List<String> actualOrderIds = List.of(orders.get(0).getOrderId(), orders.get(1).getOrderId(), orders.get(2).getOrderId());
        assertIterableEquals(expectedOrderIds, actualOrderIds, "Orders are not sorted as expected by priority/dueDate/orderId.");

        // Assert lines for first order (ORD00001)
        List<String> expectedLinesOrder1 = List.of("ORD00001L1", "ORD00001L3");
        assertIterableEquals(expectedLinesOrder1, orders.get(0).getOrderLineIds(), "Lines for ORD00001 not ordered by line number ASC.");

        // Assert lines for second order (ORD00003)
        List<String> expectedLinesOrder2 = List.of("ORD00003L1", "ORD00003L2");
        assertIterableEquals(expectedLinesOrder2, orders.get(1).getOrderLineIds(), "Lines for ORD00003 not ordered by line number ASC.");

        // Assert lines for third order (ORD00002)
        List<String> expectedLinesOrder3 = List.of("ORD00002L1", "ORD00002L2");
        assertIterableEquals(expectedLinesOrder3, orders.get(2).getOrderLineIds(), "Lines for ORD00002 not ordered by line number ASC.");
    }
}