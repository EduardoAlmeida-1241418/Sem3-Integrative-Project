package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.trolleyRelated.Trolley;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;
import pt.ipp.isep.dei.data.repository.sprint1.TrolleyRepository;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link TrolleyRepository} class.
 */
public class TrolleyRepositoryTest {
    private TrolleyRepository repository;
    private Trolley trolleyA;
    private Trolley trolleyB;
    private TrolleyModel modelA;
    private TrolleyModel modelB;

    /**
     * Initialise repository and sample trolleys before each test.
     */
    @BeforeEach
    public void setUp() {
        repository = new TrolleyRepository();
        modelA = new TrolleyModel("TROLLEY_A", 1200);
        modelB = new TrolleyModel("TROLLEY_B", 1500);
        trolleyA = new Trolley(modelA);
        trolleyB = new Trolley(modelB);
    }

    /**
     * Tests adding a new trolley and verifies it is stored correctly.
     */
    @Test
    public void testAddTrolley() {
        repository.add(trolleyA);
        assertTrue(repository.exists(trolleyA));
        assertEquals(trolleyA, repository.findById(trolleyA.getTrolleyId()));
        assertEquals(1, repository.findAll().size());
    }

    /**
     * Tests that adding a null trolley throws IllegalArgumentException.
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
     * Tests that adding a duplicate trolley (same trolleyId) throws IllegalStateException.
     * Note: Trolley constructor produces unique IDs, so we set the duplicate's ID to match trolleyA.
     */
    @Test
    public void testAddDuplicateThrows() {
        repository.add(trolleyA);
        Trolley duplicate = new Trolley(modelA);
        duplicate.setTrolleyId(trolleyA.getTrolleyId());
        try {
            repository.add(duplicate);
            fail("Should throw IllegalStateException when adding duplicate");
        } catch (IllegalStateException e) {
        }
    }

    /**
     * Tests finding a trolley by ID returns the correct object and is case-insensitive.
     */
    @Test
    public void testFindByIdReturnsTrolleyAndCaseInsensitive() {
        repository.add(trolleyA);
        Trolley found = repository.findById(trolleyA.getTrolleyId());
        assertEquals(trolleyA, found);
        Trolley foundLower = repository.findById(trolleyA.getTrolleyId().toLowerCase());
        assertEquals(trolleyA, foundLower);
    }

    /**
     * Tests that finding by null or empty ID throws IllegalArgumentException.
     */
    @Test
    public void testFindByIdThrowsIfInvalid() {
        try {
            repository.findById(null);
            fail("Should throw IllegalArgumentException for null ID");
        } catch (IllegalArgumentException e) {
        }
        try {
            repository.findById("");
            fail("Should throw IllegalArgumentException for empty ID");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests that findById returns null when trolley is not present.
     */
    @Test
    public void testFindByIdReturnsNullIfNotFound() {
        assertNull(repository.findById("NON_EXISTENT"));
    }

    /**
     * Tests retrieving all trolleys returns the correct list and is unmodifiable.
     */
    @Test
    public void testFindAllReturnsTrolleysAndUnmodifiable() {
        repository.add(trolleyA);
        repository.add(trolleyB);
        List<Trolley> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(trolleyA));
        assertTrue(all.contains(trolleyB));
        try {
            all.clear();
            fail("findAll() must return an unmodifiable list");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Tests removing a trolley by object works and is case-insensitive with respect to ID.
     */
    @Test
    public void testRemoveTrolley() {
        repository.add(trolleyA);
        boolean removed = repository.remove(trolleyA);
        assertTrue(removed);
        assertFalse(repository.exists(trolleyA));

        repository.add(trolleyA);
        Trolley differentCase = new Trolley(modelA);
        differentCase.setTrolleyId(trolleyA.getTrolleyId().toLowerCase());
        boolean removedByDifferentCase;
        try {
            removedByDifferentCase = repository.remove(differentCase);
            assertTrue(removedByDifferentCase);
        } catch (NoSuchElementException e) {
            fail("Removal should succeed for same ID with different case");
        }
    }

    /**
     * Tests that removing a null trolley throws IllegalArgumentException.
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
     * Tests that removing a non-existent trolley throws NoSuchElementException.
     */
    @Test
    public void testRemoveNonExistentThrows() {
        try {
            repository.remove(trolleyB);
            fail("Should throw NoSuchElementException when removing non-existent trolley");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Tests existence check for a trolley and that null input throws.
     */
    @Test
    public void testExistsTrolleyAndNullBehaviour() {
        repository.add(trolleyA);
        assertTrue(repository.exists(trolleyA));
        assertFalse(repository.exists(trolleyB));
        try {
            repository.exists(null);
            fail("Should throw IllegalArgumentException when checking existence of null");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests clearing the repository removes all trolleys.
     */
    @Test
    public void testClearRemovesAllTrolleys() {
        repository.add(trolleyA);
        repository.add(trolleyB);
        repository.clear();
        assertTrue(repository.findAll().isEmpty());
        assertEquals(0, repository.count());
    }
}