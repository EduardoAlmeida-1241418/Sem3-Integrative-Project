package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;
import pt.ipp.isep.dei.data.repository.sprint1.TrolleyModelRepository;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TrolleyModelRepository} class.
 */
public class TrolleyModelRepositoryTest {
    private TrolleyModelRepository repository;
    private TrolleyModel modelA;
    private TrolleyModel modelB;

    /**
     * Initialise repository and sample trolley models before each test.
     */
    @BeforeEach
    public void setUp() {
        repository = new TrolleyModelRepository();
        modelA = new TrolleyModel("TROLLEY_A", 1200);
        modelB = new TrolleyModel("TROLLEY_B", 1500);
    }

    /**
     * Tests adding a new trolley model and verifies it is stored correctly.
     */
    @Test
    public void testAddTrolleyModel() {
        repository.add(modelA);
        assertTrue(repository.exists(modelA));
        assertEquals(modelA, repository.findByName("TROLLEY_A"));
        assertEquals(1, repository.count());
    }

    /**
     * Tests that adding a null trolley model throws IllegalArgumentException.
     */
    @Test
    public void testAddNullThrows() {
        try {
            repository.add(null);
            fail("Should throw IllegalArgumentException when adding null");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests that adding a duplicate trolley model (same name, different case or weight) throws IllegalStateException.
     */
    @Test
    public void testAddDuplicateThrows() {
        repository.add(modelA);
        try {
            repository.add(new TrolleyModel("TROLLEY_A", 999));
            fail("Should throw IllegalStateException when adding duplicate");
        } catch (IllegalStateException e) {
        }
        try {
            repository.add(new TrolleyModel("trolley_a", 200));
            fail("Should throw IllegalStateException when adding duplicate with different case");
        } catch (IllegalStateException e) {
        }
    }

    /**
     * Tests finding a trolley model by name returns the correct object and is case-insensitive.
     */
    @Test
    public void testFindByNameReturnsModelAndCaseInsensitive() {
        repository.add(modelA);
        TrolleyModel found = repository.findByName("TROLLEY_A");
        assertEquals(modelA, found);

        TrolleyModel foundLower = repository.findByName("trolley_a");
        assertEquals(modelA, foundLower);
    }

    /**
     * Tests that finding by null or empty name throws IllegalArgumentException.
     */
    @Test
    public void testFindByNameThrowsIfInvalid() {
        try {
            repository.findByName(null);
            fail("Should throw IllegalArgumentException for null name");
        } catch (IllegalArgumentException e) {
        }
        try {
            repository.findByName("");
            fail("Should throw IllegalArgumentException for empty name");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests that findByName returns null when model is not present.
     */
    @Test
    public void testFindByNameReturnsNullIfNotFound() {
        assertNull(repository.findByName("NON_EXISTENT"));
    }

    /**
     * Tests retrieving all trolley models returns the correct list and is unmodifiable.
     */
    @Test
    public void testFindAllReturnsModelsAndUnmodifiable() {
        repository.add(modelA);
        repository.add(modelB);
        List<TrolleyModel> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(modelA));
        assertTrue(all.contains(modelB));
        try {
            all.clear();
            fail("findAll() must return an unmodifiable list");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Tests removing a trolley model by object works and is case-insensitive with respect to name.
     */
    @Test
    public void testRemoveTrolleyModel() {
        repository.add(modelA);
        boolean removed = repository.remove(modelA);
        assertTrue(removed);
        assertFalse(repository.exists(modelA));
        assertEquals(0, repository.count());

        repository.add(modelA);
        TrolleyModel differentCase = new TrolleyModel("trolley_a", 1200);
        boolean removedByDifferentCase;
        try {
            removedByDifferentCase = repository.remove(differentCase);
            assertTrue(removedByDifferentCase);
        } catch (NoSuchElementException e) {
            fail("Removal should succeed for same name with different case");
        }
    }

    /**
     * Tests that removing a null trolley model throws IllegalArgumentException.
     */
    @Test
    public void testRemoveNullThrows() {
        try {
            repository.remove(null);
            fail("Should throw IllegalArgumentException when removing null");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests that removing a non-existent trolley model throws NoSuchElementException.
     */
    @Test
    public void testRemoveNonExistentThrows() {
        try {
            repository.remove(modelB);
            fail("Should throw NoSuchElementException when removing non-existent model");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Tests existence check for a trolley model and that null input throws.
     */
    @Test
    public void testExistsTrolleyModelAndNullBehaviour() {
        repository.add(modelA);
        assertTrue(repository.exists(modelA));
        assertFalse(repository.exists(modelB));
        try {
            repository.exists(null);
            fail("Should throw IllegalArgumentException when checking existence of null");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests clearing the repository removes all trolley models and count() reflects this.
     */
    @Test
    public void testClearRemovesAllModelsAndCount() {
        repository.add(modelA);
        repository.add(modelB);
        assertEquals(2, repository.count());
        repository.clear();
        assertTrue(repository.findAll().isEmpty());
        assertEquals(0, repository.count());
    }

    /**
     * Tests count increments and decrements on add/remove.
     */
    @Test
    public void testCountBehaviour() {
        assertEquals(0, repository.count());
        repository.add(modelA);
        assertEquals(1, repository.count());
        repository.add(modelB);
        assertEquals(2, repository.count());
        repository.remove(modelA);
        assertEquals(1, repository.count());
    }
}