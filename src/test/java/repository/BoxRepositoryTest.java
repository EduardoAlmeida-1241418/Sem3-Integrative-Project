package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BoxRepository} class.
 */
public class BoxRepositoryTest {

    private BoxRepository repository;
    private Box box1;
    private Box box2;

    /**
     * Sets up a fresh BoxRepository and sample Box objects before each test.
     */
    @BeforeEach
    void setUp() {
        repository = new BoxRepository();
        box1 = new Box("BX001", "SKU0001", 10, new Date(1, 1, 2025), new Date(1, 1, 2025), new Time(10, 0, 0));
        box2 = new Box("BX002", "SKU0002", 5, new Date(2, 1, 2025), new Date(2, 1, 2025), new Time(11, 0, 0));
    }

    /** Tests that a box can be added and is correctly counted and found by ID. */
    @Test
    void testAddBox() {
        assertTrue(repository.add(box1));
        assertEquals(1, repository.count());
        assertTrue(repository.existsById(box1.getBoxId()));
    }

    /** Tests that adding a null box returns false and does not increase count. */
    @Test
    void testAddNullReturnsFalse() {
        assertFalse(repository.add(null));
        assertEquals(0, repository.count());
    }

    /** Tests that adding a box with a duplicate ID throws an IllegalStateException. */
    @Test
    void testAddDuplicateThrows() {
        repository.add(box1);
        Box duplicate = new Box("BX001", "SKU9999", 1, new Date(3, 1, 2025), new Date(3, 1, 2025), new Time(12, 0, 0));
        try {
            repository.add(duplicate);
            fail("Adding duplicate ID should throw IllegalStateException");
        } catch (IllegalStateException e) {
        }
    }

    /** Tests that adding a box with an empty or null ID throws IllegalArgumentException. */
    @Test
    void testAddEmptyOrNullIdThrows() {
        Box emptyId = new Box("", "SKU003", 2, new Date(4, 1, 2025), new Date(4, 1, 2025), new Time(13, 0, 0));
        try {
            repository.add(emptyId);
            fail("Adding box with empty ID should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        Box nullId = new Box(null, "SKU004", 3, new Date(4, 1, 2025), new Date(4, 1, 2025), new Time(14, 0, 0));
        try {
            repository.add(nullId);
            fail("Adding box with null ID should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /** Tests that findById returns the correct box or null when not found. */
    @Test
    void testFindById() {
        repository.add(box1);
        Box found = repository.findById("BX001");
        assertNotNull(found);
        assertEquals(box1.getBoxId(), found.getBoxId());

        assertNull(repository.findById("NONEXISTENT"));
    }

    /** Tests that findById throws for null or empty input. */
    @Test
    void testFindByIdInvalidThrows() {
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

    /** Tests that existsById returns true only for existing boxes. */
    @Test
    void testExistsById() {
        repository.add(box1);
        assertTrue(repository.existsById("BX001"));
        assertFalse(repository.existsById("BX999"));
    }

    /** Tests that existsById throws for invalid input. */
    @Test
    void testExistsByIdInvalidThrows() {
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

    /** Tests that remove deletes a box successfully. */
    @Test
    void testRemoveBox() {
        repository.add(box1);
        repository.remove("BX001");
        assertFalse(repository.existsById("BX001"));
        assertEquals(0, repository.count());
    }

    /** Tests that remove throws for null or empty ID. */
    @Test
    void testRemoveInvalidThrows() {
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

    /** Tests that findAll returns all boxes and is unmodifiable. */
    @Test
    void testFindAll() {
        repository.add(box1);
        repository.add(box2);
        Collection<Box> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(box1));
        assertTrue(all.contains(box2));
        try {
            all.clear();
            fail("findAll must return unmodifiable collection");
        } catch (UnsupportedOperationException e) {
        }
    }

    /** Tests that findBySkuItem works case-insensitively. */
    @Test
    void testFindBySkuItemCaseInsensitive() {
        Box another = new Box("BX003", "sku0001", 3, new Date(4, 1, 2025), new Date(4, 1, 2025), new Time(9, 0, 0));
        repository.add(box1);
        repository.add(box2);
        repository.add(another);

        List<Box> found = repository.findBySkuItem("SKU0001");
        assertEquals(2, found.size());
    }

    /** Tests that findBySkuItem throws for invalid SKU input. */
    @Test
    void testFindBySkuItemInvalidThrows() {
        try {
            repository.findBySkuItem(null);
            fail("findBySkuItem(null) should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            repository.findBySkuItem("");
            fail("findBySkuItem(\"\") should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /** Tests that findBySkuItem returns empty list if no matches exist. */
    @Test
    void testFindBySkuItemNoMatchesReturnsEmptyList() {
        repository.add(box1);
        List<Box> result = repository.findBySkuItem("SKU9999");
        assertTrue(result.isEmpty());
    }

    /** Tests that clear removes all boxes and count is updated. */
    @Test
    void testClearAndCount() {
        repository.add(box1);
        repository.add(box2);
        assertEquals(2, repository.count());
        repository.clear();
        assertEquals(0, repository.count());
        assertTrue(repository.findAll().isEmpty());
    }
}
