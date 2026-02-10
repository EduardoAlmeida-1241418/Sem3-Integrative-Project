package domain.BoxRelated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Box} class.
 * <p>
 * This class verifies the correct behavior of allocation logic under two modes:
 * <ul>
 *     <li><b>Strict mode:</b> Only fully allocated boxes are considered eligible.</li>
 *     <li><b>Partial mode:</b> Boxes can be partially allocated and still remain valid for partial dispatch.</li>
 * </ul>
 * Each test validates the consistency between allocated and free quantities.
 */
public class BoxTest {

    private Box box;
    private static final String BOX_ID = "BOX001";
    private static final String SKU_ITEM = "SKU123";
    private static final int TOTAL_QUANTITY = 10;
    private static final Date EXPIRY_DATE = new Date(31, 12, 2025);
    private static final Date RECEIVED_DATE = new Date(23, 10, 2025);
    private static final Time RECEIVED_TIME = new Time(14, 30, 0);

    /**
     * Initializes a {@link Box} instance with predefined identifiers,
     * SKU, quantities, and date/time before each test.
     */
    @BeforeEach
    void setUp() {
        box = new Box(BOX_ID, SKU_ITEM, TOTAL_QUANTITY, EXPIRY_DATE, RECEIVED_DATE, RECEIVED_TIME);
    }

    /**
     * Tests that when the box is fully allocated in strict mode,
     * it is considered eligible for dispatch.
     * Verifies that free quantity becomes zero.
     */
    @Test
    void testStrictMode_WhenFullyAllocated_ThenEligible() {
        box.setAllocatedQuantity(TOTAL_QUANTITY);

        assertEquals(TOTAL_QUANTITY, box.getAllocatedQuantity(), "Box should have all quantity allocated");
        assertEquals(0, box.getFreeQuantity(), "Box should have no free quantity");
    }

    /**
     * Tests that when the box is partially allocated in strict mode,
     * it is not eligible for dispatch. Both allocated and free
     * quantities must be positive but not total.
     */
    @Test
    void testStrictMode_WhenPartiallyAllocated_ThenUndispatchable() {
        int partialAllocation = 5;
        box.setAllocatedQuantity(partialAllocation);

        assertTrue(box.getAllocatedQuantity() < TOTAL_QUANTITY, "Box should be partially allocated");
        assertTrue(box.getFreeQuantity() > 0, "Box should have remaining free quantity");
        assertTrue(box.getAllocatedQuantity() > 0, "Box should have some allocated quantity");
    }

    /**
     * Tests that when the box has zero allocated quantity in strict mode,
     * it is considered undispatchable and all quantity remains free.
     */
    @Test
    void testStrictMode_WhenZeroAllocation_ThenUndispatchable() {
        assertEquals(0, box.getAllocatedQuantity(), "Box should have no allocated quantity");
        assertEquals(TOTAL_QUANTITY, box.getFreeQuantity(), "Box should have all quantity free");
    }

    /**
     * Tests that when the box is partially allocated in partial mode,
     * it retains its partial allocation and correct free quantity.
     */
    @Test
    void testPartialMode_WhenPartiallyAllocated_ThenPartial() {
        int partialAllocation = 5;
        box.setAllocatedQuantity(partialAllocation);

        assertTrue(box.getAllocatedQuantity() > 0, "Box should have some allocation");
        assertTrue(box.getAllocatedQuantity() < TOTAL_QUANTITY, "Box should not be fully allocated");
        assertEquals(TOTAL_QUANTITY - partialAllocation, box.getFreeQuantity(), "Box should have correct free quantity");
        assertEquals(partialAllocation, box.getAllocatedQuantity(), "Partial allocation should be maintained");
    }

    /**
     * Tests that when the box has zero allocation in partial mode,
     * it is considered undispatchable and all quantity remains free.
     */
    @Test
    void testPartialMode_WhenZeroAllocation_ThenUndispatchable() {
        assertEquals(0, box.getAllocatedQuantity(), "Box should have no allocated quantity");
        assertEquals(TOTAL_QUANTITY, box.getFreeQuantity(), "Box should have all quantity free");
    }

    /**
     * Tests that when the box is fully allocated in partial mode,
     * it is considered eligible for dispatch and has no free quantity left.
     */
    @Test
    void testPartialMode_WhenFullyAllocated_ThenEligible() {
        box.setAllocatedQuantity(TOTAL_QUANTITY);

        assertEquals(TOTAL_QUANTITY, box.getAllocatedQuantity(), "Box should be fully allocated");
        assertEquals(0, box.getFreeQuantity(), "Box should have no free quantity");
    }
}
