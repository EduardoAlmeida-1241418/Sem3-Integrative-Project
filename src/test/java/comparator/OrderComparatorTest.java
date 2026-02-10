package comparator;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.comparator.OrderComparator;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.OrderRelated.Order;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link OrderComparator}.
 * Tests the comparison logic for orders based on priority, due date, due time, and order ID.
 */
public class OrderComparatorTest {

    /**
     * Tests comparison of orders with different priorities.
     * An order with higher priority (lower number) should come first.
     */
    @Test
    void ensureOrderWithHigherPriorityComesFirst() {
        Order order1 = new Order("ORD001", new Date(25, 10, 2025), new Time(14, 30, 0), 1);
        Order order2 = new Order("ORD002", new Date(25, 10, 2025), new Time(14, 30, 0), 2);
        OrderComparator comparator = new OrderComparator();

        int result = comparator.compare(order1, order2);

        assertTrue(result < 0, "Order with priority 1 should come before order with priority 2");
    }

    /**
     * Tests comparison of orders with same priority but different due dates.
     * An order with earlier due date should come first.
     */
    @Test
    void ensureOrderWithEarlierDueDateComesFirst() {
        Order order1 = new Order("ORD001", new Date(24, 10, 2025), new Time(14, 30, 0), 1);
        Order order2 = new Order("ORD002", new Date(25, 10, 2025), new Time(14, 30, 0), 1);
        OrderComparator comparator = new OrderComparator();

        int result = comparator.compare(order1, order2);

        assertTrue(result < 0, "Order with earlier due date should come first");
    }

    /**
     * Tests comparison of orders with same priority and due date but different due times.
     * An order with earlier due time should come first.
     */
    @Test
    void ensureOrderWithEarlierDueTimeComesFirst() {
        Order order1 = new Order("ORD001", new Date(24, 10, 2025), new Time(9, 0, 0), 1);
        Order order2 = new Order("ORD002", new Date(24, 10, 2025), new Time(14, 0, 0), 1);
        OrderComparator comparator = new OrderComparator();

        int result = comparator.compare(order1, order2);

        assertTrue(result < 0, "Order with earlier due time should come first");
    }

    /**
     * Tests comparison of orders with same priority, due date, and due time.
     * Orders should be ordered by their IDs lexicographically.
     */
    @Test
    void ensureOrdersWithSamePriorityDateAndTimeAreOrderedById() {
        Order order1 = new Order("ORD001", new Date(24, 10, 2025), new Time(14, 30, 0), 1);
        Order order2 = new Order("ORD002", new Date(24, 10, 2025), new Time(14, 30, 0), 1);
        OrderComparator comparator = new OrderComparator();

        int result = comparator.compare(order1, order2);

        assertTrue(result < 0, "Orders with same priority, date and time should be ordered by ID");
    }

    /**
     * Tests that orders with the same exact values return zero from comparison.
     */
    @Test
    void ensureIdenticalOrdersReturnZero() {
        Order order1 = new Order("ORD001", new Date(24, 10, 2025), new Time(14, 30, 0), 1);
        Order order2 = new Order("ORD001", new Date(24, 10, 2025), new Time(14, 30, 0), 1);
        OrderComparator comparator = new OrderComparator();

        int result = comparator.compare(order1, order2);

        assertEquals(0, result, "Identical orders should return 0 from comparison");
    }

    /**
     * Tests comparison with extreme priority values.
     */
    @Test
    void ensureComparisonWorksWithExtremePriorityValues() {
        Order order1 = new Order("ORD001", new Date(24, 10, 2025), new Time(14, 30, 0), Integer.MIN_VALUE);
        Order order2 = new Order("ORD002", new Date(24, 10, 2025), new Time(14, 30, 0), Integer.MAX_VALUE);
        OrderComparator comparator = new OrderComparator();

        int result = comparator.compare(order1, order2);

        assertTrue(result < 0, "Order with lower priority value should come first");
    }
}