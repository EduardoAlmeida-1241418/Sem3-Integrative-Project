package comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.comparator.FefoBoxComparator;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link FefoBoxComparator}.
 * Tests the FEFO (First Expired, First Out) comparison logic for boxes based on expiry date.
 */
class FefoBoxComparatorTest {

    private FefoBoxComparator comparator;
    private BoxRepository boxRepository;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        comparator = new FefoBoxComparator();
        boxRepository = Repositories.getInstance().getBoxRepository();
        boxRepository.clear();
    }

    /**
     * Tests comparison of boxes with different expiry dates.
     * Box with earlier expiry date should be ordered first.
     */
    @Test
    void compareBoxesWithDifferentExpiryDates() {
        Box box1 = new Box("BOX1", "SKU123", 10,
                new Date(1, 11, 2025),
                new Date(24, 10, 2025),
                new Time(14, 30, 0));

        Box box2 = new Box("BOX2", "SKU123", 10,
                new Date(1, 12, 2025),
                new Date(24, 10, 2025),
                new Time(14, 30, 0));

        boxRepository.add(box1);
        boxRepository.add(box2);

        assertTrue(comparator.compare("BOX1", "BOX2") < 0,
                "Box with earlier expiry date should come first");
    }

    /**
     * Tests comparison of boxes with the same expiry date.
     * Boxes with identical expiry dates should be equal.
     */
    @Test
    void compareBoxesWithSameExpiryDate() {
        Box box1 = new Box("BOX3", "SKU123", 10,
                new Date(1, 12, 2025),
                new Date(24, 10, 2025),
                new Time(14, 30, 0));

        Box box2 = new Box("BOX4", "SKU123", 10,
                new Date(1, 12, 2025),
                new Date(24, 10, 2025),
                new Time(14, 30, 0));

        boxRepository.add(box1);
        boxRepository.add(box2);

        assertEquals(0, comparator.compare("BOX3", "BOX4"),
                "Boxes with same expiry date should be considered equal");
    }

    /**
     * Tests comparison when both boxes have null expiry dates.
     * Boxes without expiry dates should be considered equal.
     */
    @Test
    void compareBoxesWithBothNullExpiryDates() {
        Box box1 = new Box("BOX5", "SKU123", 10,
                null,
                new Date(24, 10, 2025),
                new Time(14, 30, 0));

        Box box2 = new Box("BOX6", "SKU123", 10,
                null,
                new Date(24, 10, 2025),
                new Time(14, 30, 0));

        boxRepository.add(box1);
        boxRepository.add(box2);

        assertEquals(0, comparator.compare("BOX5", "BOX6"),
                "Boxes with null expiry dates should be considered equal");
    }

    /**
     * Tests comparison when one box has a null expiry date.
     * Box with expiry date should come before the one without.
     */
    @Test
    void compareWithOneNullExpiryDate() {
        Box box1 = new Box("BOX7", "SKU123", 10,
                new Date(1, 12, 2025),
                new Date(24, 10, 2025),
                new Time(14, 30, 0));

        Box box2 = new Box("BOX8", "SKU123", 10,
                null,
                new Date(24, 10, 2025),
                new Time(14, 30, 0));

        boxRepository.add(box1);
        boxRepository.add(box2);

        assertTrue(comparator.compare("BOX7", "BOX8") < 0,
                "Box with expiry date should come before box with null expiry date");
    }

    /**
     * Tests handling of non-existent box IDs.
     * Comparator should throw IllegalArgumentException.
     */
    @Test
    void compareWithInvalidBoxIds() {
        assertThrows(IllegalArgumentException.class,
                () -> comparator.compare("INVALID1", "INVALID2"),
                "Should throw IllegalArgumentException for non-existent boxes");
    }
}