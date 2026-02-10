package domain.OrderRelated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.OrderRelated.Order;
import pt.ipp.isep.dei.domain.Time;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Order}.
 * Ensures correct behavior of constructors, getters, setters,
 * and management of associated order line identifiers.
 */
class OrderTest {

    private Order order;
    private Date dueDate;
    private Time dueTime;

    /**
     * Initializes a sample order instance before each test case.
     */
    @BeforeEach
    void setUp() {
        dueDate = new Date(1, 1, 2025);
        dueTime = new Time(10, 30, 0);
        order = new Order("ORD1", dueDate, dueTime, 3);
    }

    /**
     * Verifies that the constructor with date and priority initializes correctly.
     */
    @Test
    void testConstructorWithDateAndPriority() {
        Order simpleOrder = new Order("ORD2", dueDate, 1);
        assertEquals("ORD2", simpleOrder.getOrderId());
        assertEquals(dueDate, simpleOrder.getDueDate());
        assertEquals(1, simpleOrder.getPriority());
        assertNotNull(simpleOrder.getDueTime());
    }

    /**
     * Verifies that the constructor with date and time initializes correctly.
     */
    @Test
    void testConstructorWithDateTimeAndPriority() {
        assertEquals("ORD1", order.getOrderId());
        assertEquals(dueDate, order.getDueDate());
        assertEquals(dueTime, order.getDueTime());
        assertEquals(3, order.getPriority());
    }

    /**
     * Verifies that setters correctly update field values.
     */
    @Test
    void testSettersUpdateValues() {
        Date newDate = new Date(5, 5, 2026);
        Time newTime = new Time(14, 15, 0);
        order.setOrderId("ORD9");
        order.setDueDate(newDate);
        order.setDueTime(newTime);
        order.setPriority(5);

        assertEquals("ORD9", order.getOrderId());
        assertEquals(newDate, order.getDueDate());
        assertEquals(newTime, order.getDueTime());
        assertEquals(5, order.getPriority());
    }

    /**
     * Verifies that {@link Order#addOrderLine(String)} correctly adds identifiers.
     */
    @Test
    void testAddOrderLine() {
        order.addOrderLine("LINE1");
        order.addOrderLine("LINE2");
        assertEquals(Set.of("LINE1", "LINE2"), order.getOrderLineIds());
    }

    /**
     * Verifies that {@link Order#setOrderLineIds(Set)} correctly replaces the entire set.
     */
    @Test
    void testSetOrderLineIds() {
        Set<String> newLines = new TreeSet<>();
        newLines.add("L1");
        newLines.add("L2");
        order.setOrderLineIds(newLines);
        assertEquals(newLines, order.getOrderLineIds());
    }

    /**
     * Verifies that {@link Order#toString()} contains expected information.
     */
    @Test
    void testToStringContainsExpectedData() {
        order.addOrderLine("L1");
        String output = order.toString();
        assertTrue(output.contains("ORD1"));
        assertTrue(output.contains("L1"));
        assertTrue(output.contains(String.valueOf(order.getPriority())));
    }
}
