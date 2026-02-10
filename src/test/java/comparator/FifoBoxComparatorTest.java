package comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.comparator.FifoBoxComparator;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link FifoBoxComparator}.
 * Tests the FIFO (First In, First Out) comparison logic for boxes based on their received dates and times.
 */
class FifoBoxComparatorTest {

    private FifoBoxComparator comparator;
    private BoxRepository boxRepository;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        comparator = new FifoBoxComparator();
        boxRepository = Repositories.getInstance().getBoxRepository();
        boxRepository.clear();
    }

    /**
     * Tests comparison of boxes with different received dates.
     * Box received earlier should be ordered before box received later.
     */
    @Test
    void ensureBoxWithEarlierDateComesFirst() {
        Box box1 = new Box("BOX001", "SKU123", 10,
                new Date(31, 12, 2025),
                new Date(23, 10, 2025),
                new Time(14, 30, 0));

        Box box2 = new Box("BOX002", "SKU123", 10,
                new Date(31, 12, 2025),
                new Date(24, 10, 2025),
                new Time(14, 30, 0));

        boxRepository.add(box1);
        boxRepository.add(box2);

        int result = comparator.compare("BOX001", "BOX002");

        assertTrue(result < 0, "Box received earlier should come first");
    }

    /**
     * Tests comparison of boxes with same received date but different times.
     * Box received earlier in the day should be ordered before box received later.
     */
    @Test
    void ensureBoxWithEarlierTimeComesFirst() {
        Box box1 = new Box("BOX003", "SKU123", 10,
                new Date(31, 12, 2025),
                new Date(24, 10, 2025),
                new Time(9, 0, 0));

        Box box2 = new Box("BOX004", "SKU123", 10,
                new Date(31, 12, 2025),
                new Date(24, 10, 2025),
                new Time(14, 0, 0));

        boxRepository.add(box1);
        boxRepository.add(box2);

        int result = comparator.compare("BOX003", "BOX004");

        assertTrue(result < 0, "Box received earlier in the day should come first");
    }

    /**
     * Tests comparison of boxes with same received date and time.
     * Boxes should be ordered by their IDs lexicographically.
     */
    @Test
    void ensureBoxesWithSameDateAndTimeAreOrderedById() {
        Box box1 = new Box("BOX005", "SKU123", 10,
                new Date(31, 12, 2025),
                new Date(24, 10, 2025),
                new Time(14, 30, 0));

        Box box2 = new Box("BOX006", "SKU123", 10,
                new Date(31, 12, 2025),
                new Date(24, 10, 2025),
                new Time(14, 30, 0));

        boxRepository.add(box1);
        boxRepository.add(box2);

        int result = comparator.compare("BOX005", "BOX006");

        assertTrue(result < 0, "Boxes with same date and time should be ordered by ID");
    }
}