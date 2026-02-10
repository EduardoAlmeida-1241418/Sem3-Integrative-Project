package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Aisle;
import pt.ipp.isep.dei.data.repository.sprint1.AisleRepository;

import java.util.Collection;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link AisleRepository} class.
 */
public class AisleRepositoryTest {
    private AisleRepository repo;
    private final String validAisleId = "A01";
    private final String validWarehouseId = "W01";
    private final String anotherAisleId = "A02";

    /**
     * Sets up a fresh repository before each test.
     */
    @BeforeEach
    public void setUp() {
        repo = new AisleRepository();
    }

    /**
     * Tests adding a valid aisle and verifies it is stored and retrievable.
     */
    @Test
    public void testAddAndFindByKey() {
        Aisle aisle = new Aisle(validAisleId, validWarehouseId);
        repo.add(aisle);
        Aisle found = repo.findByKey(validAisleId);
        assertEquals(validAisleId, found.getAisleID());
        assertEquals(validWarehouseId, found.getWarehouseID());
    }

    /**
     * Tests that adding a null aisle throws an exception.
     */
    @Test
    public void testAddNullAisleThrows() {
        try {
            repo.add(null);
            fail("Should throw IllegalArgumentException for null aisle");
        } catch (IllegalArgumentException e) {

        }
    }

    /**
     * Tests that adding an aisle with null or empty ID throws an exception.
     *
     * Note: constructing new Aisle(null, ...) directly will cause the Aisle constructor
     * to call extractNumberFromId(null) and throw NullPointerException before repo.add().
     * To test the repository validation (i.e., repo.add should reject aisle objects whose
     * getAisleID() returns null), create an anonymous subclass that overrides getAisleID()
     * to return null.
     */
    @Test
    public void testAddAisleWithNullOrEmptyIdThrows() {
        Aisle aisleNull = new Aisle("TMP", validWarehouseId) {
            @Override
            public String getAisleID() {
                return null;
            }
        };
        Aisle aisleEmpty = new Aisle("", validWarehouseId);

        try {
            repo.add(aisleNull);
            fail("Should throw IllegalArgumentException for null ID");
        } catch (IllegalArgumentException e) {
        }

        try {
            repo.add(aisleEmpty);
            fail("Should throw IllegalArgumentException for empty ID");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests that adding an aisle with a duplicate ID throws an exception.
     */
    @Test
    public void testAddDuplicateAisleThrows() {
        Aisle aisle1 = new Aisle(validAisleId, validWarehouseId);
        Aisle aisle2 = new Aisle(validAisleId, validWarehouseId);
        repo.add(aisle1);
        try {
            repo.add(aisle2);
            fail("Should throw IllegalStateException for duplicate ID");
        } catch (IllegalStateException e) {
        }
    }

    /**
     * Tests finding an aisle by a valid key and by invalid keys (null, empty, not found).
     */
    @Test
    public void testFindByKeyThrowsForInvalidKeys() {
        Aisle aisle = new Aisle(validAisleId, validWarehouseId);
        repo.add(aisle);
        try {
            repo.findByKey(null);
            fail("Should throw IllegalArgumentException for null key");
        } catch (IllegalArgumentException e) {
        }
        try {
            repo.findByKey("");
            fail("Should throw IllegalArgumentException for empty key");
        } catch (IllegalArgumentException e) {
        }
        try {
            repo.findByKey("A99");
            fail("Should throw NoSuchElementException for not found key");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Tests existsByKey for present, absent, null, and empty keys.
     */
    @Test
    public void testExistsByKey() {
        Aisle aisle = new Aisle(validAisleId, validWarehouseId);
        repo.add(aisle);
        assertTrue(repo.existsByKey(validAisleId));
        assertFalse(repo.existsByKey(anotherAisleId));
        try {
            repo.existsByKey(null);
            fail("Should throw IllegalArgumentException for null key");
        } catch (IllegalArgumentException e) {
        }
        try {
            repo.existsByKey("");
            fail("Should throw IllegalArgumentException for empty key");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests removing an aisle by key, including error cases for null, empty, and not found keys.
     */
    @Test
    public void testRemove() {
        Aisle aisle = new Aisle(validAisleId, validWarehouseId);
        repo.add(aisle);
        repo.remove(validAisleId);
        assertFalse(repo.existsByKey(validAisleId));
        try {
            repo.remove(null);
            fail("Should throw IllegalArgumentException for null key");
        } catch (IllegalArgumentException e) {
        }
        try {
            repo.remove("");
            fail("Should throw IllegalArgumentException for empty key");
        } catch (IllegalArgumentException e) {
        }
        try {
            repo.remove("A99");
            fail("Should throw NoSuchElementException for not found key");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Tests findAll returns all aisles and is unmodifiable.
     */
    @Test
    public void testFindAll() {
        Aisle aisle1 = new Aisle(validAisleId, validWarehouseId);
        Aisle aisle2 = new Aisle(anotherAisleId, validWarehouseId);
        repo.add(aisle1);
        repo.add(aisle2);
        Collection<Aisle> all = repo.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(aisle1));
        assertTrue(all.contains(aisle2));
        try {
            all.clear();
            fail("findAll should return an unmodifiable collection");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Tests clear removes all aisles from the repository.
     */
    @Test
    public void testClear() {
        repo.add(new Aisle(validAisleId, validWarehouseId));
        repo.add(new Aisle(anotherAisleId, validWarehouseId));
        repo.clear();
        assertEquals(0, repo.count());
        assertTrue(repo.findAll().isEmpty());
    }

    /**
     * Tests count returns the correct number of aisles.
     */
    @Test
    public void testCount() {
        assertEquals(0, repo.count());
        repo.add(new Aisle(validAisleId, validWarehouseId));
        assertEquals(1, repo.count());
        repo.add(new Aisle(anotherAisleId, validWarehouseId));
        assertEquals(2, repo.count());
        repo.remove(validAisleId);
        assertEquals(1, repo.count());
    }
}