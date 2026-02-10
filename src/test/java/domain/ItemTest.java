package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Item} class.
 * <p>
 * Ensures correct construction, modification, and comparison of {@code Item} instances.
 * </p>
 */
class ItemTest {

    private Item item1;
    private Item item2;
    private Date expiryDate;
    private Date receivedDate;
    private Time receivedTime;

    /** Base setup executed before each test. */
    @BeforeEach
    void setUp() {
        expiryDate = new Date(31, 12, 2025);
        receivedDate = new Date(1, 1, 2024);
        receivedTime = new Time(10, 30, 0);

        item1 = new Item(
                "SKU001",
                "Bottled Water",
                CategoryItem.BEVERAGE,
                UnitType.BOTTLE,
                1.5,
                receivedTime,
                receivedDate,
                expiryDate,
                1.2
        );

        item2 = new Item(
                "SKU001",
                "Bottled Water",
                CategoryItem.BEVERAGE,
                UnitType.BOTTLE,
                1.5,
                receivedTime,
                receivedDate,
                expiryDate,
                1.2
        );
    }

    /** Verifies that the main constructor initializes all fields correctly. */
    @Test
    void testFullConstructorInitializesCorrectly() {
        assertEquals("SKU001", item1.getSku());
        assertEquals("Bottled Water", item1.getName());
        assertEquals(CategoryItem.BEVERAGE, item1.getCategoryItem());
        assertEquals(UnitType.BOTTLE, item1.getUnitType());
        assertEquals(1.5, item1.getVolume());
        assertEquals(1.2, item1.getUnitWeight());
        assertEquals(expiryDate, item1.getExpiryDate());
        assertEquals(receivedDate, item1.getReceivedDate());
        assertEquals(receivedTime, item1.getReceivedTime());
    }

    /** Tests the simplified constructor (without dates). */
    @Test
    void testBasicConstructorInitializesCorrectly() {
        Item simpleItem = new Item("SKU002", "Shampoo", CategoryItem.PERSONALCARE, UnitType.BOTTLE, 0.5, 0.45);

        assertEquals("SKU002", simpleItem.getSku());
        assertEquals("Shampoo", simpleItem.getName());
        assertEquals(CategoryItem.PERSONALCARE, simpleItem.getCategoryItem());
        assertEquals(UnitType.BOTTLE, simpleItem.getUnitType());
        assertEquals(0.5, simpleItem.getVolume());
        assertEquals(0.45, simpleItem.getUnitWeight());
        assertNull(simpleItem.getExpiryDate());
        assertNull(simpleItem.getReceivedDate());
        assertNull(simpleItem.getReceivedTime());
    }

    /** Tests setters and getters individually. */
    @Test
    void testSettersAndGettersWorkCorrectly() {
        item1.setSku("SKU009");
        item1.setName("Hammer");
        item1.setCategoryItem(CategoryItem.HARDWARE);
        item1.setUnitType(UnitType.BOX);
        item1.setVolume(0.3);
        item1.setUnitWeight(0.25);

        Date newExp = new Date(1, 6, 2026);
        Date newRec = new Date(15, 5, 2024);
        Time newTime = new Time(9, 0, 0);

        item1.setExpiryDate(newExp);
        item1.setReceivedDate(newRec);
        item1.setReceivedTime(newTime);

        assertEquals("SKU009", item1.getSku());
        assertEquals("Hammer", item1.getName());
        assertEquals(CategoryItem.HARDWARE, item1.getCategoryItem());
        assertEquals(UnitType.BOX, item1.getUnitType());
        assertEquals(0.3, item1.getVolume());
        assertEquals(0.25, item1.getUnitWeight());
        assertEquals(newExp, item1.getExpiryDate());
        assertEquals(newRec, item1.getReceivedDate());
        assertEquals(newTime, item1.getReceivedTime());
    }

    /** Verifies the string output from {@code toString()} contains key information. */
    @Test
    void testToStringContainsKeyInfo() {
        String text = item1.toString();
        assertTrue(text.contains("SKU001"));
        assertTrue(text.contains("Bottled Water"));
        assertTrue(text.contains("BEVERAGE"));
        assertTrue(text.contains("BOTTLE"));
        assertTrue(text.contains("1.5"));
        assertTrue(text.contains("1.2"));
    }

    /** Ensures that two identical items are considered equal. */
    @Test
    void testEqualsReturnsTrueForIdenticalObjects() {
        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    /** Ensures that different items are not considered equal. */
    @Test
    void testEqualsReturnsFalseForDifferentObjects() {
        Item different = new Item("SKU999", "TV", CategoryItem.ELECTRONICS, UnitType.UNIT, 15.0, 8.0);
        assertNotEquals(item1, different);
    }

    /** Verifies reflexive and symmetric equality behavior. */
    @Test
    void testEqualsIsReflexiveAndSymmetric() {
        assertEquals(item1, item1);
        assertEquals(item1, item2);
        assertEquals(item2, item1);
    }

    /** Ensures equals returns false for null or different object types. */
    @Test
    void testEqualsReturnsFalseForNullOrDifferentType() {
        assertNotEquals(item1, null);
        assertNotEquals(item1, "StringObject");
    }
}
