package domain.BoxRelated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.BoxRelated.BoxItem;
import pt.ipp.isep.dei.domain.Item;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BoxItem}.
 * Ensures correct handling of SKU identifiers, quantities, and internal item mappings.
 */
class BoxItemTest {

    private BoxItem boxItem;
    private Item item1;
    private Item item2;

    /**
     * Initializes test data before each test.
     */
    @BeforeEach
    void setUp() {
        boxItem = new BoxItem("SKU-TEST", 10);
        item1 = new Item("SKU1", "Item 1", null, null, 0.0, 2.0);
        item2 = new Item("SKU2", "Item 2", null, null, 0.0, 3.5);
    }

    /**
     * Verifies that the constructor correctly sets SKU and quantity.
     */
    @Test
    void testConstructorAndGetters() {
        assertEquals("SKU-TEST", boxItem.getSkuItem());
        assertEquals(10, boxItem.getQuantity());
    }

    /**
     * Verifies that the setter methods correctly update SKU and quantity.
     */
    @Test
    void testSetters() {
        boxItem.setSkuItem("SKU-NEW");
        boxItem.setQuantity(25);
        assertEquals("SKU-NEW", boxItem.getSkuItem());
        assertEquals(25, boxItem.getQuantity());
    }


    /**
     * Verifies that {@link BoxItem#getItems()} never returns null after initialization.
     */
    @Test
    void testGetItemsNotNull() {
        assertNotNull(boxItem.getItems());
    }
}
