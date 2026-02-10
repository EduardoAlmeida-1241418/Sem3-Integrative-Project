package domain.OrderRelated;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.OrderRelated.AllocationStatusType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link AllocationStatusType}.
 * Ensures that all enumeration values exist and behave as expected.
 */
class AllocationInfoTest {

    /**
     * Verifies that all expected enum constants are defined.
     */
    @Test
    void testEnumValuesExist() {
        AllocationStatusType[] values = AllocationStatusType.values();
        assertEquals(3, values.length);
        assertArrayEquals(
                new AllocationStatusType[]{
                        AllocationStatusType.ALLOCATION_DONE,
                        AllocationStatusType.PICKING_PLAN_DONE,
                        AllocationStatusType.PICKING_PATH_DONE
                },
                values
        );
    }

    /**
     * Verifies that {@link AllocationStatusType#valueOf(String)} correctly retrieves enum constants.
     */
    @Test
    void testValueOfReturnsCorrectConstant() {
        assertEquals(AllocationStatusType.ALLOCATION_DONE, AllocationStatusType.valueOf("ALLOCATION_DONE"));
        assertEquals(AllocationStatusType.PICKING_PLAN_DONE, AllocationStatusType.valueOf("PICKING_PLAN_DONE"));
        assertEquals(AllocationStatusType.PICKING_PATH_DONE, AllocationStatusType.valueOf("PICKING_PATH_DONE"));
    }

    /**
     * Ensures that an invalid name passed to {@link AllocationStatusType#valueOf(String)}
     * throws an {@link IllegalArgumentException}.
     */
    @Test
    void testValueOfInvalidNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> AllocationStatusType.valueOf("INVALID_NAME"));
    }
}
