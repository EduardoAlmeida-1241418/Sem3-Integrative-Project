package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.OrderRelated.Order;
import pt.ipp.isep.dei.data.repository.sprint1.OrderRepository;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link OrderRepository} class.
 */
public class OrderRepositoryTest {
    private OrderRepository repository;
    private Order order1;
    private Order order2;
    private Order order3;

    /**
     * Sets up a fresh repository and sample orders before each test.
     */
    @BeforeEach
    void setUp() {
        repository = new OrderRepository();
        order1 = new Order("ORD00001", new Date(29, 9, 2025), new Time(9, 0, 0), 2);
        order2 = new Order("ORD00002", new Date(3, 10, 2025), new Time(14, 0, 0), 3);
        order3 = new Order("ORD00003", new Date(4, 10, 2025), new Time(22, 0, 0), 3);
    }

    /**
     * Tests adding a valid order to the repository.
     */
    @Test
    void testAddValidOrder() {
        repository.add(order1);
        assertTrue(repository.existsById(order1.getOrderId()));
    }

    /**
     * Tests that adding a null order throws IllegalArgumentException.
     */
    @Test
    void testAddNullOrderThrows() {
        try {
            repository.add(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests that adding a duplicate order throws IllegalStateException.
     */
    @Test
    void testAddDuplicateOrderThrows() {
        repository.add(order1);
        Order duplicate = new Order("ORD00001", new Date(29, 9, 2025), new Time(9, 0, 0), 2);
        try {
            repository.add(duplicate);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests finding an order by valid ID returns the correct order.
     */
    @Test
    void testFindByIdValid() {
        repository.add(order1);
        Order found = repository.findById(order1.getOrderId());
        assertEquals(order1.getOrderId(), found.getOrderId());
        assertEquals(order1.getDueDate().getDay(), found.getDueDate().getDay());
        assertEquals(order1.getDueDate().getMonth(), found.getDueDate().getMonth());
        assertEquals(order1.getDueDate().getYear(), found.getDueDate().getYear());
        assertEquals(order1.getDueTime().getHour(), found.getDueTime().getHour());
        assertEquals(order1.getDueTime().getMinute(), found.getDueTime().getMinute());
        assertEquals(order1.getPriority(), found.getPriority());
    }

    /**
     * Tests that finding an order by invalid ID throws NoSuchElementException.
     */
    @Test
    void testFindByIdInvalidThrows() {
        try {
            repository.findById("ORD99999");
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests that finding by null or empty ID throws IllegalArgumentException.
     */
    @Test
    void testFindByIdNullOrEmptyThrows() {
        repository.add(order1);
        try {
            repository.findById(null);
            fail("Expected IllegalArgumentException for null ID");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            repository.findById("");
            fail("Expected IllegalArgumentException for empty ID");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests existsById returns true for existing order and false for non-existing.
     */
    @Test
    void testExistsById() {
        repository.add(order1);
        assertTrue(repository.existsById(order1.getOrderId()));
        assertFalse(repository.existsById("ORD99999"));
    }

    /**
     * Tests that existsById with null or empty ID throws IllegalArgumentException.
     */
    @Test
    void testExistsByIdNullOrEmptyThrows() {
        try {
            repository.existsById(null);
            fail("Expected IllegalArgumentException for null ID");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            repository.existsById("");
            fail("Expected IllegalArgumentException for empty ID");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests removing an order by valid ID.
     */
    @Test
    void testRemoveValid() {
        repository.add(order1);
        repository.remove(order1.getOrderId());
        assertFalse(repository.existsById(order1.getOrderId()));
    }

    /**
     * Tests that removing an order by invalid ID throws NoSuchElementException.
     */
    @Test
    void testRemoveInvalidThrows() {
        try {
            repository.remove("ORD99999");
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests that removing by null or empty ID throws IllegalArgumentException.
     */
    @Test
    void testRemoveNullOrEmptyThrows() {
        try {
            repository.remove(null);
            fail("Expected IllegalArgumentException for null ID");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            repository.remove("");
            fail("Expected IllegalArgumentException for empty ID");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests findAll returns all added orders.
     */
    @Test
    void testFindAll() {
        repository.add(order1);
        repository.add(order2);
        Collection<Order> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(order1));
        assertTrue(all.contains(order2));
    }

    /**
     * Tests that findAll returns an unmodifiable collection.
     */
    @Test
    void testFindAllIsUnmodifiable() {
        repository.add(order1);
        Collection<Order> all = repository.findAll();
        try {
            all.clear();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests findAllSorted returns orders sorted by priority and due date/time.
     */
    @Test
    void testFindAllSorted() {
        repository.add(order2);
        repository.add(order1);
        repository.add(order3);
        List<Order> sorted = (List<Order>) repository.findAllSorted();
        assertEquals(3, sorted.size());
        // Sorted by priority (lower first), then due date/time
        assertEquals(order1, sorted.get(0));
    }

    /**
     * Tests that clear removes all orders from the repository.
     */
    @Test
    void testClear() {
        repository.add(order1);
        repository.add(order2);
        repository.clear();
        assertEquals(0, repository.count());
        assertTrue(repository.findAll().isEmpty());
    }

    /**
     * Tests count returns the correct number of orders.
     */
    @Test
    void testCount() {
        assertEquals(0, repository.count());
        repository.add(order1);
        assertEquals(1, repository.count());
        repository.add(order2);
        assertEquals(2, repository.count());
    }
}
