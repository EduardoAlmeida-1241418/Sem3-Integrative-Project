package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Bay;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link Bay}.
 * <p>
 * Uses internal fake repositories instead of external mocks,
 * allowing isolation of {@code Bay} behavior without infrastructure dependencies.
 * </p>
 */
class BayTest {

    /** Instance of the class under test. */
    private Bay bay;

    /** Fake repositories used for dependency injection. */
    private FakeRepositories fakeRepositories;

    /**
     * Minimal implementation of {@link BoxRepository} used for testing only.
     * <p>
     * Stores {@link Box} instances in memory using a {@link HashMap},
     * avoiding the use of the global singleton repository.
     * </p>
     */
    private static class FakeBoxRepository extends BoxRepository {
        /** Stores boxes in memory, indexed by their identifier. */
        private final Map<String, Box> boxes = new HashMap<>();

        /**
         * Adds a box to the fake repository.
         *
         * @param box the box instance to add
         * @return {@code true} if the box was successfully added
         */
        @Override
        public boolean add(Box box) {
            if (box == null) return false;
            boxes.put(box.getBoxId(), box);
            return true;
        }

        /**
         * Finds a box by its identifier.
         *
         * @param id the unique identifier of the box
         * @return the corresponding box instance, or {@code null} if not found
         */
        @Override
        public Box findById(String id) {
            return boxes.get(id);
        }

        /**
         * Removes a box from the repository based on its identifier.
         *
         * @param id the identifier of the box to remove
         */
        @Override
        public void remove(String id) {
            boxes.remove(id);
        }

        /**
         * Checks whether a box with the given identifier exists.
         *
         * @param id the box identifier
         * @return {@code true} if it exists, {@code false} otherwise
         */
        @Override
        public boolean existsById(String id) {
            return boxes.containsKey(id);
        }

        /**
         * Returns an unmodifiable collection containing all stored boxes.
         *
         * @return collection of stored boxes
         */
        @Override
        public Collection<Box> findAll() {
            return Collections.unmodifiableCollection(boxes.values());
        }

        /**
         * Completely clears the fake repository content.
         */
        @Override
        public void clear() {
            boxes.clear();
        }
    }

    /**
     * Minimal implementation of {@link Repositories} used only
     * to inject the fake box repository.
     */
    private static class FakeRepositories extends Repositories {
        /** Fake box repository used across all tests. */
        private final FakeBoxRepository fakeRepo = new FakeBoxRepository();

        /**
         * Overrides the superclass method to return the fake repository.
         *
         * @return instance of {@link FakeBoxRepository}
         */
        @Override
        public BoxRepository getBoxRepository() {
            return fakeRepo;
        }
    }

    /**
     * Setup executed before each test.
     * <p>
     * Creates a new fake repository, sets it as the global instance in {@link Repositories},
     * and instantiates a new {@link Bay}.
     * </p>
     */
    @BeforeEach
    void setUp() {
        fakeRepositories = new FakeRepositories();
        Repositories.setInstance(fakeRepositories);
        bay = new Bay("W1A1", 2, 3);
    }

    /**
     * Verifies that the constructor correctly initializes all fields of {@link Bay}.
     */
    @Test
    void testConstructorInitializesCorrectly() {
        assertEquals("W1", bay.getWarehouseID());
        assertEquals("W1A1B2", bay.getBayId());
        assertEquals(3, bay.getMaxCapacityBoxes());
        assertEquals(0, bay.getNBoxesStorage());
    }

    /**
     * Ensures that {@link Bay#getSkuItemSafe()} returns "Empty"
     * when SKU is {@code null}, and the correct value when defined.
     */
    @Test
    void testGetSkuItemSafeWhenNull() {
        assertEquals("Empty", bay.getSkuItemSafe());
        bay.setSkuItem("SKU1");
        assertEquals("SKU1", bay.getSkuItemSafe());
    }

    /**
     * Tests successful addition of a box when space is available in the bay.
     */
    @Test
    void testAddBoxSucceedsWhenSpaceAvailable() {
        Box box = new Box("B1", "SKU1", 10,
                new Date(1,1,200),
                new Date(1,1,200),
                new Time(10,0,0));
        fakeRepositories.getBoxRepository().add(box);

        boolean result = bay.addBox("B1", RoleType.GLOBAL);
        assertTrue(result);
        assertEquals(1, bay.getNBoxesStorage());
        assertEquals("SKU1", bay.getSkuItem());
        assertTrue(bay.getBoxIds().contains("B1"));
    }

    /**
     * Ensures that {@link Bay#addBox(String, RoleType)} fails
     * when the box SKU differs from the bay SKU.
     */
    @Test
    void testAddBoxFailsWhenSkuMismatch() {
        Box b1 = new Box("B1", "SKU1", 10,
                new Date(1,1,200),
                new Date(1,1,200),
                new Time(10,0,0));
        Box b2 = new Box("B2", "SKU2", 10,
                new Date(1,1,200),
                new Date(1,1,200),
                new Time(10,0,0));
        FakeBoxRepository repo = fakeRepositories.fakeRepo;
        repo.add(b1);
        repo.add(b2);

        bay.addBox("B1", RoleType.GLOBAL);
        boolean result = bay.addBox("B2", RoleType.GLOBAL);

        assertFalse(result);
        assertEquals(1, bay.getNBoxesStorage());
        assertEquals("SKU1", bay.getSkuItem());
    }

    /**
     * Tests that adding more boxes than the defined maximum capacity is not allowed.
     */
    @Test
    void testAddBoxFailsWhenFull() {
        FakeBoxRepository repo = fakeRepositories.fakeRepo;
        for (int i = 1; i <= 3; i++) {
            Box b = new Box("B" + i, "SKU1", 5,
                    new Date(1,1,200),
                    new Date(1,1,200),
                    new Time(10,0,0));
            repo.add(b);
            bay.addBox(b.getBoxId(), RoleType.GLOBAL);
        }

        Box overflow = new Box("B4", "SKU1", 5,
                new Date(1,1,200),
                new Date(1,1,200),
                new Time(10,0,0));
        repo.add(overflow);

        boolean result = bay.addBox("B4", RoleType.GLOBAL);
        assertFalse(result);
        assertTrue(bay.isFull());
        assertEquals(3, bay.getNBoxesStorage());
    }

    /**
     * Verifies that a box is correctly removed from the bay.
     */
    @Test
    void testRemoveBoxRemovesSuccessfully() {
        Box b1 = new Box("BOX00001", "SKU1", 10,
                new Date(30,1,200),
                new Date(1,1,200),
                new Time(10,0,0));
        fakeRepositories.getBoxRepository().add(b1);
        bay.addBox("BOX00001", RoleType.GLOBAL);

        boolean result = bay.removeBox("BOX00001", RoleType.GLOBAL);
        assertTrue(result);
        assertEquals(0, bay.getNBoxesStorage());
        assertFalse(bay.getBoxIds().contains("BOX00001"));
        assertNull(b1.getBayId());
    }

    /**
     * Ensures that attempting to remove a non-existent box returns {@code false}.
     */
    @Test
    void testRemoveBoxFailsIfNotFound() {
        boolean result = bay.removeBox("UNKNOWN", RoleType.GLOBAL);
        assertFalse(result);
    }

    /**
     * Validates the correct calculation of the total number of items
     * stored in all boxes within the bay.
     */
    @Test
    void testGetQuantityItemsSumsCorrectly() {
        FakeBoxRepository repo = fakeRepositories.fakeRepo;
        Box b1 = new Box("B1", "SKU1", 5,
                new Date(1,1,200),
                new Date(1,1,200),
                new Time(10,0,0));
        Box b2 = new Box("B2", "SKU1", 3,
                new Date(1,1,200),
                new Date(1,1,200),
                new Time(10,0,0));
        repo.add(b1);
        repo.add(b2);

        bay.addBox("B1", RoleType.GLOBAL);
        bay.addBox("B2", RoleType.GLOBAL);

        assertEquals(8, bay.getQuantityItems());
    }

    /**
     * Ensures that {@link Bay#toString()} includes key information
     * about the bay and its associated boxes.
     */
    @Test
    void testToStringContainsKeyInfo() {
        Box b1 = new Box("B1", "SKU1", 2,
                new Date(1,1,200),
                new Date(1,1,200),
                new Time(10,0,0));
        fakeRepositories.getBoxRepository().add(b1);
        bay.addBox("B1", RoleType.GLOBAL);

        String text = bay.toString();
        assertTrue(text.contains("W1A1B2"));
        assertTrue(text.contains("SKU1"));
        assertTrue(text.contains("B1"));
    }

    /**
     * Tests {@link Bay#equals(Object)} and {@link Bay#hashCode()},
     * ensuring consistency and structural equality.
     */
    @Test
    void testEqualsAndHashCode() {
        Bay bay2 = new Bay("W1A1", 2, 3);
        assertEquals(bay, bay2);
        assertEquals(bay.hashCode(), bay2.hashCode());
    }
}
