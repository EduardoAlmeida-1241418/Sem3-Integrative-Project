package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Warehouse;
import pt.ipp.isep.dei.data.repository.sprint1.WarehouseRepository;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link WarehouseRepository} class.
 */
public class WarehouseRepositoryTest {
    private WarehouseRepository repository;
    private Warehouse warehouse1;
    private Warehouse warehouse2;

    /**
     * Initialise a fresh repository and two warehouse instances before each test.
     */
    @BeforeEach
    public void setUp() {
        repository = new WarehouseRepository();
        warehouse1 = new Warehouse("W1");
        warehouse2 = new Warehouse("W2");
    }

    /**
     * Test that adding a valid warehouse stores it and increments the count.
     */
    @Test
    public void testAddWarehouse() {
        repository.add(warehouse1);
        assertEquals(1, repository.count());
        assertTrue(repository.existsByKey("W1"));
        assertSame(warehouse1, repository.findByKey("W1"));
    }

    /**
     * Test that adding a null warehouse throws IllegalArgumentException.
     */
    @Test
    public void testAddNullThrows() {
        try {
            repository.add(null);
            fail("Adding null warehouse should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test that adding a warehouse with null or empty ID throws IllegalArgumentException.
     */
    @Test
    public void testAddWarehouseWithNullOrEmptyIdThrows() {
        Warehouse nullId = new Warehouse(null);
        try {
            repository.add(nullId);
            fail("Adding warehouse with null ID should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        Warehouse emptyId = new Warehouse("");
        try {
            repository.add(emptyId);
            fail("Adding warehouse with empty ID should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test that adding a duplicate warehouse (same ID) throws IllegalStateException.
     */
    @Test
    public void testAddDuplicateWarehouseThrows() {
        repository.add(warehouse1);
        try {
            repository.add(new Warehouse("W1"));
            fail("Adding duplicate warehouse should throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("already exists"));
        }
    }

    /**
     * Test that findByKey returns the correct warehouse for a valid key.
     */
    @Test
    public void testFindByKeyReturnsWarehouse() {
        repository.add(warehouse1);
        Warehouse found = repository.findByKey("W1");
        assertEquals("W1", found.getWarehouseID());
        assertSame(warehouse1, found);
    }

    /**
     * Test that findByKey with a non-existent key throws NoSuchElementException.
     */
    @Test
    public void testFindByKeyThrowsIfNotFound() {
        try {
            repository.findByKey("W9");
            fail("findByKey for non-existent key should throw NoSuchElementException");
        } catch (java.util.NoSuchElementException e) {
            assertTrue(e.getMessage().contains("not found"));
        }
    }

    /**
     * Test that findByKey validates null and empty inputs and throws IllegalArgumentException.
     */
    @Test
    public void testFindByKeyNullOrEmptyThrows() {
        try {
            repository.findByKey(null);
            fail("findByKey(null) should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            repository.findByKey("");
            fail("findByKey(\"\") should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test existsByKey returns true for stored warehouse and false for unknown key,
     * and that it validates null/empty inputs.
     */
    @Test
    public void testExistsByKeyAndValidation() {
        repository.add(warehouse1);
        assertTrue(repository.existsByKey("W1"));
        assertFalse(repository.existsByKey("W2"));

        try {
            repository.existsByKey(null);
            fail("existsByKey(null) should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            repository.existsByKey("");
            fail("existsByKey(\"\") should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test removing a warehouse by key deletes it and updates count.
     */
    @Test
    public void testRemoveWarehouse() {
        repository.add(warehouse1);
        repository.remove("W1");
        assertEquals(0, repository.count());
        assertFalse(repository.existsByKey("W1"));
    }

    /**
     * Test that remove throws when called with a non-existent key.
     */
    @Test
    public void testRemoveThrowsIfNotFound() {
        try {
            repository.remove("W9");
            fail("remove for non-existent key should throw NoSuchElementException");
        } catch (java.util.NoSuchElementException e) {
            // expected
            assertTrue(e.getMessage().contains("not found"));
        }
    }

    /**
     * Test that remove validates null and empty inputs and throws IllegalArgumentException.
     */
    @Test
    public void testRemoveNullOrEmptyThrows() {
        try {
            repository.remove(null);
            fail("remove(null) should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            repository.remove("");
            fail("remove(\"\") should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test findAll returns all warehouses and the returned collection is unmodifiable.
     */
    @Test
    public void testFindAllWarehousesAndUnmodifiable() {
        repository.add(warehouse1);
        repository.add(warehouse2);
        Collection<Warehouse> all = repository.findAll();
        assertEquals(2, all.size());

        boolean foundW1 = false;
        boolean foundW2 = false;
        for (Warehouse w : all) {
            if ("W1".equals(w.getWarehouseID())) foundW1 = true;
            if ("W2".equals(w.getWarehouseID())) foundW2 = true;
        }
        assertTrue(foundW1);
        assertTrue(foundW2);

        try {
            all.clear();
            fail("findAll() must return an unmodifiable collection");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test clear removes all warehouses and count returns zero afterwards.
     */
    @Test
    public void testClearWarehouses() {
        repository.add(warehouse1);
        repository.add(warehouse2);
        repository.clear();
        assertEquals(0, repository.count());
        assertTrue(repository.findAll().isEmpty());
    }

    /**
     * Test count returns the correct number as warehouses are added and removed.
     */
    @Test
    public void testCountWarehouses() {
        assertEquals(0, repository.count());
        repository.add(warehouse1);
        assertEquals(1, repository.count());
        repository.add(warehouse2);
        assertEquals(2, repository.count());
        repository.remove("W1");
        assertEquals(1, repository.count());
    }
}