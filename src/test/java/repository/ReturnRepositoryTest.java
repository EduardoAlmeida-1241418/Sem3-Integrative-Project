package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Return;
import pt.ipp.isep.dei.domain.ReturnReason;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint1.ReturnRepository;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ReturnRepository} class.
 */
public class ReturnRepositoryTest {
    private ReturnRepository repository;
    private Return ret1;
    private Return ret2;

    /**
     * Prepare a fresh repository and two sample Return instances before each test.
     */
    @BeforeEach
    public void setUp() {
        repository = Repositories.getInstance().getReturnRepository();
        repository.clear();
        ret1 = new Return(
                "RET00001",
                "SKU0009",
                19,
                ReturnReason.CUSTOMER_REMORSE,
                new Date(13, 9, 2025),
                new Time(12, 0, 0),
                null
        );
        ret2 = new Return(
                "RET00002",
                "SKU0008",
                18,
                ReturnReason.DAMAGED,
                new Date(5, 9, 2025),
                new Time(18, 0, 0),
                null
        );
    }

    /**
     * Test that adding a valid return stores it and increases the count.
     */
    @Test
    public void testAddReturn() {
        repository.add(ret1);
        assertEquals(1, repository.count());
        assertTrue(repository.existsById("RET00001"));
    }

    /**
     * Test that adding null Return throws IllegalArgumentException.
     */
    @Test
    public void testAddNullThrows() {
        try {
            repository.add(null);
            fail("Adding null Return should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test that adding a Return whose getReturnId() returns null or empty throws IllegalArgumentException.
     * Uses anonymous subclasses to simulate invalid id without changing constructor behaviour.
     */
    @Test
    public void testAddReturnWithNullOrEmptyIdThrows() {
        Return nullId = new Return("TMP", "SKU", 1, ReturnReason.DAMAGED, new Date(1,1,2025), new Time(1,1,1), null) {
            @Override
            public String getReturnId() {
                return null;
            }
        };
        try {
            repository.add(nullId);
            fail("Adding Return with null id should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        Return emptyId = new Return("TMP2", "SKU", 1, ReturnReason.DAMAGED, new Date(1,1,2025), new Time(1,1,1), null) {
            @Override
            public String getReturnId() {
                return "";
            }
        };
        try {
            repository.add(emptyId);
            fail("Adding Return with empty id should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test that adding a duplicate Return (same id) throws IllegalStateException.
     */
    @Test
    public void testAddDuplicateReturnThrows() {
        repository.add(ret1);
        Return duplicate = new Return("RET00001", "SKU0010", 5, ReturnReason.EXPIRED, new Date(1, 1, 2025), new Time(10, 0, 0), null);
        assertThrows(IllegalStateException.class, () -> repository.add(duplicate));
    }

    /**
     * Test that findById returns the stored Return for a valid id.
     */
    @Test
    public void testFindByIdReturnsReturn() {
        repository.add(ret1);
        Return found = repository.findById("RET00001");
        assertEquals(ret1, found);
    }

    /**
     * Test that findById throws NoSuchElementException when the id is unknown.
     */
    @Test
    public void testFindByIdThrowsIfNotFound() {
        assertThrows(NoSuchElementException.class, () -> repository.findById("RET99999"));
    }

    /**
     * Test that findById validates null/empty id and throws IllegalArgumentException.
     */
    @Test
    public void testFindByIdThrowsIfIdInvalid() {
        assertThrows(IllegalArgumentException.class, () -> repository.findById(null));
        assertThrows(IllegalArgumentException.class, () -> repository.findById(""));
    }

    /**
     * Test that existsById returns true for stored id and false for unknown id.
     */
    @Test
    public void testExistsById() {
        repository.add(ret1);
        assertTrue(repository.existsById("RET00001"));
        assertFalse(repository.existsById("RET00002"));
    }

    /**
     * Test that existsById validates null/empty id and throws IllegalArgumentException.
     */
    @Test
    public void testExistsByIdThrowsIfIdInvalid() {
        assertThrows(IllegalArgumentException.class, () -> repository.existsById(null));
        assertThrows(IllegalArgumentException.class, () -> repository.existsById(""));
    }

    /**
     * Test that remove(String) deletes a stored Return and decrements count.
     */
    @Test
    public void testRemoveReturn() {
        repository.add(ret1);
        repository.remove("RET00001");
        assertEquals(0, repository.count());
        assertFalse(repository.existsById("RET00001"));
    }

    /**
     * Test that remove(String) throws NoSuchElementException for unknown id.
     */
    @Test
    public void testRemoveThrowsIfNotFound() {
        assertThrows(NoSuchElementException.class, () -> repository.remove("RET99999"));
    }

    /**
     * Test that remove(String) validates null/empty id and throws IllegalArgumentException.
     */
    @Test
    public void testRemoveThrowsIfIdInvalid() {
        assertThrows(IllegalArgumentException.class, () -> repository.remove(null));
        assertThrows(IllegalArgumentException.class, () -> repository.remove(""));
    }

    /**
     * Test that findAllSorted returns in sorted order according to the comparator
     * and that the returned list is unmodifiable.
     */
    @Test
    public void testFindAllSortedReturnsAndImmutable() {
        Return early = new Return("RET_A", "SKU_A", 1, ReturnReason.DAMAGED, new Date(1, 1, 2025), new Time(8, 0, 0), null);
        Return later = new Return("RET_B", "SKU_B", 2, ReturnReason.DAMAGED, new Date(2, 1, 2025), new Time(9, 0, 0), null);

        repository.add(later);
        repository.add(early);

        List<Return> sorted = repository.findAllSorted();
        assertEquals(2, sorted.size());
        assertEquals(later, sorted.get(0));
        assertEquals(early, sorted.get(1));
        try {
            sorted.clear();
            fail("findAllSorted() must return an unmodifiable list");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test that findAll returns all stored returns and the returned collection is unmodifiable.
     */
    @Test
    public void testFindAllReturns() {
        repository.add(ret1);
        repository.add(ret2);
        Collection<Return> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(ret1));
        assertTrue(all.contains(ret2));
        try {
            all.clear();
            fail("findAll() must return an unmodifiable collection");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test that clear() removes all returns and count() reflects the empty state.
     */
    @Test
    public void testClearRemovesAllReturns() {
        repository.add(ret1);
        repository.add(ret2);
        repository.clear();
        assertEquals(0, repository.count());
        assertTrue(repository.findAll().isEmpty());
    }

    /**
     * Test that count() increments and decrements appropriately as returns are added and removed.
     */
    @Test
    public void testCountReturns() {
        assertEquals(0, repository.count());
        repository.add(ret1);
        assertEquals(1, repository.count());
        repository.add(ret2);
        assertEquals(2, repository.count());
        repository.remove("RET00001");
        assertEquals(1, repository.count());
    }
}

