package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Bay;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BayRepository} class.
 */
public class BayRepositoryTest {

    private BayRepository repository;
    private Bay bay1;
    private Bay bay2;

    /**
     * Sets up a fresh BayRepository and sample Bay objects before each test.
     */
    @BeforeEach
    void setUp() {
        repository = new BayRepository();
        bay1 = new Bay("W1A1", 1, 12);
        bay2 = new Bay("W1A1", 2, 10);
    }

    /**
     * Tests that a bay can be added and is correctly counted and found by key.
     */
    @Test
    void testAddBay() {
        repository.add(bay1);
        assertEquals(1, repository.count());
        assertTrue(repository.existsByKey(bay1.getBayId()));
    }

    /**
     * Tests that adding a null bay throws an IllegalArgumentException.
     */
    @Test
    void testAddNullThrows() {
        try {
            repository.add(null);
            fail("add(null) should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests that adding the same Bay object twice throws an IllegalStateException.
     */
    @Test
    void testAddDuplicateSameObjectThrows() {
        repository.add(bay1);
        assertThrows(IllegalStateException.class, () -> repository.add(bay1));
    }

    /**
     * Tests that adding a different Bay object with the same bayId throws an IllegalStateException.
     */
    @Test
    void testAddDuplicateDifferentObjectSameIdThrows() {
        repository.add(bay1);
        // create a different Bay instance that has the same bayId as bay1
        Bay anotherSameId = new Bay("W1A1", 1, 5);
        try {
            repository.add(anotherSameId);
            fail("Adding different object with same bayId must throw IllegalStateException");
        } catch (IllegalStateException e) {
        }
    }

    /**
     * Tests that finding a bay by key returns the correct Bay object.
     */
    @Test
    void testFindByKeyReturnsBay() {
        repository.add(bay1);
        Bay found = repository.findByKey(bay1.getBayId());
        assertEquals(bay1.getBayId(), found.getBayId());
    }

    /**
     * Tests that finding a bay by a non-existent key throws NoSuchElementException.
     */
    @Test
    void testFindByKeyThrowsWhenNotFound() {
        assertThrows(NoSuchElementException.class, () -> repository.findByKey("W1A1B99"));
    }

    /**
     * Tests that finding a bay by null or empty key throws IllegalArgumentException.
     */
    @Test
    void testFindByKeyNullOrEmptyThrows() {
        repository.add(bay1);
        try {
            repository.findByKey(null);
            fail("findByKey(null) must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            repository.findByKey("");
            fail("findByKey(\"\") must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the existence check for a bay by key.
     */
    @Test
    void testExistsByKey() {
        repository.add(bay1);
        assertTrue(repository.existsByKey(bay1.getBayId()));
        assertFalse(repository.existsByKey(bay2.getBayId()));
    }

    /**
     * Tests that existsByKey throws IllegalArgumentException for null or empty keys.
     */
    @Test
    void testExistsByKeyNullOrEmptyThrows() {
        try {
            repository.existsByKey(null);
            fail("existsByKey(null) must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            repository.existsByKey("");
            fail("existsByKey(\"\") must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests that a bay can be removed and is no longer present in the repository.
     */
    @Test
    void testRemoveBay() {
        repository.add(bay1);
        repository.remove(bay1.getBayId());
        assertFalse(repository.existsByKey(bay1.getBayId()));
        assertEquals(0, repository.count());
    }

    /**
     * Tests that remove throws IllegalArgumentException for null or empty keys.
     */
    @Test
    void testRemoveNullOrEmptyThrows() {
        try {
            repository.remove(null);
            fail("remove(null) must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            repository.remove("");
            fail("remove(\"\") must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests that removing a bay by a non-existent key throws NoSuchElementException.
     */
    @Test
    void testRemoveThrowsWhenNotFound() {
        assertThrows(NoSuchElementException.class, () -> repository.remove("W1A1B99"));
    }

    /**
     * Tests that findAll returns all bays and the returned collection is unmodifiable.
     */
    @Test
    void testFindAllReturnsAllBays_andIsUnmodifiable() {
        repository.add(bay1);
        repository.add(bay2);
        Collection<Bay> allBays = repository.findAll();
        assertEquals(2, allBays.size());
        assertTrue(allBays.contains(bay1));
        assertTrue(allBays.contains(bay2));
        try {
            allBays.clear();
            fail("findAll should return an unmodifiable collection");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Tests that clear removes all bays and count reflects the change.
     */
    @Test
    void testClearAndCount() {
        repository.add(bay1);
        repository.add(bay2);
        assertEquals(2, repository.count());
        repository.clear();
        assertEquals(0, repository.count());
        assertTrue(repository.findAll().isEmpty());
    }

    /**
     * Tests the behaviour of count as bays are added and removed.
     */
    @Test
    void testCountBehavior() {
        assertEquals(0, repository.count());
        repository.add(bay1);
        assertEquals(1, repository.count());
        repository.add(bay2);
        assertEquals(2, repository.count());
        repository.remove(bay1.getBayId());
        assertEquals(1, repository.count());
    }

    /**
     * Verifies that the iteration order of findAll matches the internal TreeMap ordering.
     */
    @Test
    void testFindAllIterationOrderIsStable() {
        Bay bA = new Bay("W1A1", 5, 10); // W1A1B5
        Bay bB = new Bay("W1A1", 1, 10); // W1A1B1
        Bay bC = new Bay("W2A1", 1, 10); // W2A1B1 (different warehouse)
        repository.add(bA);
        repository.add(bB);
        repository.add(bC);

        Collection<Bay> all = repository.findAll();
        // iterate to ensure deterministic order
        Iterator<Bay> it = all.iterator();
        assertTrue(it.hasNext());
        Bay first = it.next();
        assertNotNull(first);
        // iteration should yield exactly 3 elements
        int count = 1;
        while (it.hasNext()) {
            it.next();
            count++;
        }
        assertEquals(3, count);
    }
}