package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Item;
import pt.ipp.isep.dei.domain.CategoryItem;
import pt.ipp.isep.dei.domain.UnitType;
import pt.ipp.isep.dei.data.repository.sprint1.ItemInfoRepository;

import java.util.Collection;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ItemInfoRepository} class.
 */
public class ItemInfoRepositoryTest {
    private ItemInfoRepository repository;
    private Item item1;
    private Item item2;

    /**
     * Sets up a fresh ItemInfoRepository and sample Item objects before each test.
     */
    @BeforeEach
    void setUp() {
        repository = new ItemInfoRepository();
        item1 = new Item("SKU0001", "Item_01", CategoryItem.CLEANING, UnitType.PACK, 1.9, 9.38);
        item2 = new Item("SKU0002", "Item_02", CategoryItem.ELECTRONICS, UnitType.PACK, 8.99, 8.71);
    }

    /**
     * Tests that an item can be added and is correctly counted and found by SKU.
     */
    @Test
    void testAddItem() {
        repository.add(item1);
        assertEquals(1, repository.count());
        assertTrue(repository.existsBySku(item1.getSku()));
    }

    /**
     * Tests that adding a null item throws an IllegalArgumentException.
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
     * Tests that adding the same Item object twice throws an IllegalStateException.
     */
    @Test
    void testAddDuplicateSameObjectThrowsException() {
        repository.add(item1);
        try {
            repository.add(item1);
            fail("Expected IllegalStateException for duplicate SKU (same instance)");
        } catch (IllegalStateException e) {
        }
    }

    /**
     * Tests that adding a different Item object with the same SKU throws an IllegalStateException.
     */
    @Test
    void testAddDuplicateDifferentInstanceSameSkuThrows() {
        repository.add(item1);
        Item otherSameSku = new Item("SKU0001", "Item_X", CategoryItem.GROCERY, UnitType.PACK, 2.0, 1.0);
        try {
            repository.add(otherSameSku);
            fail("Expected IllegalStateException for duplicate SKU (different instance)");
        } catch (IllegalStateException e) {
        }
    }

    /**
     * Tests that finding an item by SKU returns the correct Item object.
     */
    @Test
    void testFindBySkuReturnsItem() {
        repository.add(item1);
        Item found = repository.findBySku(item1.getSku());
        assertEquals(item1.getSku(), found.getSku());
        assertEquals(item1.getName(), found.getName());
    }

    /**
     * Tests that finding an item by null or empty SKU throws IllegalArgumentException.
     */
    @Test
    void testFindBySkuNullOrEmptyThrows() {
        repository.add(item1);
        try {
            repository.findBySku(null);
            fail("findBySku(null) should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            repository.findBySku("");
            fail("findBySku(\"\") should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests that finding an item by a non-existent SKU throws NoSuchElementException.
     */
    @Test
    void testFindBySkuThrowsExceptionIfNotFound() {
        try {
            repository.findBySku("SKU9999");
            fail("Expected NoSuchElementException for non-existent SKU");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Tests the existence check for an item by SKU.
     */
    @Test
    void testExistsBySku() {
        repository.add(item1);
        assertTrue(repository.existsBySku(item1.getSku()));
        assertFalse(repository.existsBySku(item2.getSku()));
    }

    /**
     * Tests that existsBySku throws IllegalArgumentException for null or empty SKUs.
     */
    @Test
    void testExistsBySkuNullOrEmptyThrows() {
        repository.add(item1);
        try {
            repository.existsBySku(null);
            fail("existsBySku(null) should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            repository.existsBySku("");
            fail("existsBySku(\"\") should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests that an item can be removed and is no longer present in the repository.
     */
    @Test
    void testRemoveItem() {
        repository.add(item1);
        repository.remove(item1.getSku());
        assertFalse(repository.existsBySku(item1.getSku()));
        assertEquals(0, repository.count());
    }

    /**
     * Tests that remove throws IllegalArgumentException for null or empty SKUs.
     */
    @Test
    void testRemoveNullOrEmptyThrows() {
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
     * Tests that removing an item by a non-existent SKU throws NoSuchElementException.
     */
    @Test
    void testRemoveItemThrowsExceptionIfNotFound() {
        try {
            repository.remove("SKU9999");
            fail("Expected NoSuchElementException for non-existent SKU");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Tests that findAll returns all items and the returned collection is unmodifiable.
     */
    @Test
    void testFindAllReturnsAllItemsAndIsUnmodifiable() {
        repository.add(item1);
        repository.add(item2);
        Collection<Item> allItems = repository.findAll();
        assertEquals(2, allItems.size());
        assertTrue(allItems.contains(item1));
        assertTrue(allItems.contains(item2));
        try {
            allItems.clear();
            fail("findAll should return an unmodifiable collection");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Tests that clear removes all items and count reflects the change.
     */
    @Test
    void testClearRemovesAllItemsAndCount() {
        repository.add(item1);
        repository.add(item2);
        assertEquals(2, repository.count());
        repository.clear();
        assertEquals(0, repository.count());
        assertTrue(repository.findAll().isEmpty());
    }
}