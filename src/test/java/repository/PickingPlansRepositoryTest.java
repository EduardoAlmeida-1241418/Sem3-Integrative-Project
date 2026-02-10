package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.pickingPlanRelated.PickingPlan;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;
import pt.ipp.isep.dei.data.repository.sprint1.PickingPlansRepository;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PickingPlansRepository} class.
 */
public class PickingPlansRepositoryTest {

    private PickingPlansRepository repository;
    private TrolleyModel trolleyModel;
    private PickingPlan plan1;
    private PickingPlan plan2;

    /**
     * Prepare a fresh repository and sample trolley model and plans before each test.
     */
    @BeforeEach
    public void setUp() {
        repository = new PickingPlansRepository();
        trolleyModel = new TrolleyModel("TM_PLAINTEXT", 150);
        plan1 = new PickingPlan(trolleyModel);
        plan2 = new PickingPlan(trolleyModel);
    }

    /**
     * Verify that adding a valid picking plan stores it, updates count and makes it discoverable by id.
     */
    @Test
    public void testAddAndCountAndExistsById() {
        repository.add(plan1);
        assertEquals(1, repository.count(), "Count should be 1 after adding one plan");
        assertTrue(repository.existsById(plan1.getId()), "Plan must exist by its id after add");
    }

    /**
     * Verify that adding a null picking plan throws IllegalArgumentException.
     */
    @Test
    public void testAddNullThrows() {
        try {
            repository.add(null);
            fail("Adding null picking plan should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Verify that adding a plan whose getId() returns null or empty causes IllegalArgumentException.
     * Uses anonymous subclasses to avoid constructor/time-generated id interfering with test.
     */
    @Test
    public void testAddPlanWithNullOrEmptyIdThrows() {
        PickingPlan nullIdPlan = new PickingPlan(trolleyModel) {
            @Override
            public String getId() {
                return null;
            }
        };
        try {
            repository.add(nullIdPlan);
            fail("Adding a plan with null id should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        PickingPlan emptyIdPlan = new PickingPlan(trolleyModel) {
            @Override
            public String getId() {
                return "";
            }
        };
        try {
            repository.add(emptyIdPlan);
            fail("Adding a plan with empty id should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Verify that adding a plan with a duplicate id throws IllegalStateException.
     */
    @Test
    public void testAddDuplicateThrows() {
        repository.add(plan1);
        try {
            repository.add(plan1);
            fail("Adding duplicate plan (same instance) should throw IllegalStateException");
        } catch (IllegalStateException e) {
        }
        PickingPlan other = new PickingPlan(trolleyModel);
        other.setId(plan1.getId());
        try {
            repository.add(other);
            fail("Adding different plan with same id should throw IllegalStateException");
        } catch (IllegalStateException e) {
        }
    }

    /**
     * Verify that findById returns the correct picking plan for a valid id.
     */
    @Test
    public void testFindByIdReturnsPlan() {
        repository.add(plan1);
        PickingPlan found = repository.findById(plan1.getId());
        assertSame(plan1, found, "findById must return the identical plan instance stored");
    }

    /**
     * Verify that findById throws NoSuchElementException when the id is not present.
     */
    @Test
    public void testFindByIdNotFoundThrows() {
        try {
            repository.findById("PP999");
            fail("findById for unknown id should throw NoSuchElementException");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Verify that findById with null or empty id throws IllegalArgumentException.
     */
    @Test
    public void testFindByIdNullOrEmptyThrows() {
        repository.add(plan1);
        try {
            repository.findById(null);
            fail("findById(null) should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            repository.findById("");
            fail("findById(\"\") should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Verify existsById validates input and returns expected boolean results.
     */
    @Test
    public void testExistsByIdValidation() {
        repository.add(plan1);
        assertTrue(repository.existsById(plan1.getId()), "existsById should return true for existing plan");
        assertFalse(repository.existsById("PP_NOT_EXISTS"), "existsById should return false for non-existing id");
        try {
            repository.existsById(null);
            fail("existsById(null) should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            repository.existsById("");
            fail("existsById(\"\") should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Verify removal by id succeeds for an existing id and updates the repository state.
     */
    @Test
    public void testRemoveByIdAndEffects() {
        repository.add(plan1);
        repository.add(plan2);
        repository.remove(plan1.getId());
        assertFalse(repository.existsById(plan1.getId()), "Plan must no longer exist after removal by id");
        assertEquals(1, repository.count(), "Count must reflect removal");
    }

    /**
     * Verify that removing by id not present throws NoSuchElementException.
     */
    @Test
    public void testRemoveByIdNotFoundThrows() {
        try {
            repository.remove("PP_NOT_EXISTS");
            fail("Removing non-existent id should throw NoSuchElementException");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Verify that remove(String) validates null/empty input and throws IllegalArgumentException.
     */
    @Test
    public void testRemoveByIdNullOrEmptyThrows() {
        try {
            repository.remove((String)null);
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
     * Verify that remove(PickingPlan) removes the given instance and that remove(null) throws.
     */
    @Test
    public void testRemoveByInstance() {
        repository.add(plan1);
        repository.add(plan2);
        boolean removed = repository.remove(plan1);
        assertTrue(removed, "remove(plan) should return true for an existing plan");
        assertEquals(1, repository.count(), "Count should be decremented after removal by instance");
        try {
            repository.remove((PickingPlan) null);
            fail("remove(null) should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Verify removeAt removes the plan at the given index and throws when index is out of range.
     */
    @Test
    public void testRemoveAtIndex() {
        repository.add(plan1);
        repository.add(plan2);
        PickingPlan removed = repository.removeAt(0);
        assertSame(plan1, removed, "removeAt(0) should remove and return the first inserted plan");
        assertEquals(1, repository.count(), "Count should be updated after removeAt");
        try {
            repository.removeAt(99);
            fail("removeAt with invalid index should throw NoSuchElementException");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Verify findAll returns an unmodifiable list preserving insertion order.
     */
    @Test
    public void testFindAllAndUnmodifiable() {
        repository.add(plan1);
        repository.add(plan2);
        List<PickingPlan> all = repository.findAll();
        assertEquals(2, all.size(), "findAll should return both inserted plans");
        assertSame(plan1, all.get(0), "Insertion order must be preserved in findAll()");
        assertSame(plan2, all.get(1), "Insertion order must be preserved in findAll()");
        try {
            all.clear();
            fail("findAll() must return an unmodifiable list (clear should throw)");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Verify clear removes all plans and count returns expected values.
     */
    @Test
    public void testClearAndCount() {
        repository.add(plan1);
        repository.add(plan2);
        assertEquals(2, repository.count(), "count should report the number of stored plans");
        repository.clear();
        assertEquals(0, repository.count(), "count must be zero after clear");
        assertTrue(repository.findAll().isEmpty(), "findAll must be empty after clear");
    }
}
