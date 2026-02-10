package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.data.repository.sprint1.OrderLineRepository;

import java.util.Collection;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link OrderLineRepository} class.
 */
public class OrderLineRepositoryTest {
    private OrderLineRepository repository;
    private OrderLine orderLine1;
    private OrderLine orderLine2;

    /**
     * Sets up a fresh repository and sample order lines before each test.
     */
    @BeforeEach
    void setUp() {
        repository = new OrderLineRepository();
        orderLine1 = new OrderLine("ORD00001", 1, "SKU0007", 36);
        orderLine2 = new OrderLine("ORD00002", 2, "SKU0025", 49);
    }

    /**
     * Tests adding a valid order line to the repository.
     */
    @Test
    void testAddValidOrderLine() {
        repository.add(orderLine1);
        assertTrue(repository.existsById(orderLine1.getOrderLineId()));
    }

    /**
     * Tests that adding a null order line throws IllegalArgumentException.
     */
    @Test
    void testAddNullOrderLineThrows() {
        try {
            repository.add(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests that adding a duplicate order line throws IllegalStateException.
     */
    @Test
    void testAddDuplicateOrderLineThrows() {
        repository.add(orderLine1);
        OrderLine duplicate = new OrderLine("ORD00001", 1, "SKU0007", 36);
        try {
            repository.add(duplicate);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests that the orderLineId is computed correctly by the constructor.
     */
    @Test
    void testOrderLineIdFormatIsComputedCorrectly() {
        assertEquals( "ORD00001" + "L" + 1, orderLine1.getOrderLineId());
    }

    /**
     * Tests finding an order line by valid ID returns the correct order line.
     */
    @Test
    void testFindByIdValid() {
        repository.add(orderLine1);
        OrderLine found = repository.findById(orderLine1.getOrderLineId());
        assertEquals(orderLine1.getOrderId(), found.getOrderId());
        assertEquals(orderLine1.getOrderLineId(), found.getOrderLineId());
        assertEquals(orderLine1.getSkuItem(), found.getSkuItem());
        assertEquals(orderLine1.getQuantity(), found.getQuantity());
    }

    /**
     * Tests that findByOrderId returns the same object as added.
     */
    @Test
    void testFindByOrderIdMethodReturnsSameObject() {
        repository.add(orderLine1);
        OrderLine found = repository.findByOrderId(orderLine1.getOrderLineId());
        assertSame(orderLine1, found);
    }

    /**
     * Tests that finding an order line by invalid ID throws NoSuchElementException.
     */
    @Test
    void testFindByIdInvalidThrows() {
        try {
            repository.findById("ORD99999L99");
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
        repository.add(orderLine1);
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
     * Tests existsById returns true for existing order line and false for non-existing.
     */
    @Test
    void testExistsById() {
        repository.add(orderLine1);
        assertTrue(repository.existsById(orderLine1.getOrderLineId()));
        assertFalse(repository.existsById("ORD99999L99"));
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
     * Tests removing an order line by valid ID.
     */
    @Test
    void testRemoveValid() {
        repository.add(orderLine1);
        repository.remove(orderLine1.getOrderLineId());
        assertFalse(repository.existsById(orderLine1.getOrderLineId()));
    }

    /**
     * Tests that removing an order line by invalid ID throws NoSuchElementException.
     */
    @Test
    void testRemoveInvalidThrows() {
        try {
            repository.remove("ORD99999L99");
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
     * Tests findAll returns all added order lines.
     */
    @Test
    void testFindAll() {
        repository.add(orderLine1);
        repository.add(orderLine2);
        Collection<OrderLine> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(orderLine1));
        assertTrue(all.contains(orderLine2));
    }

    /**
     * Tests that findAll returns an unmodifiable collection.
     */
    @Test
    void testFindAllIsUnmodifiable() {
        repository.add(orderLine1);
        Collection<OrderLine> all = repository.findAll();
        try {
            all.clear();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests that clear removes all order lines and count returns the correct number.
     */
    @Test
    void testClearAndCount() {
        repository.add(orderLine1);
        repository.add(orderLine2);
        assertEquals(2, repository.count());
        repository.clear();
        assertEquals(0, repository.count());
        assertTrue(repository.findAll().isEmpty());
    }
}