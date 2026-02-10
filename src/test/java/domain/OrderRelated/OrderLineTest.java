package domain.OrderRelated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.OrderRelated.AllocatedInfo;
import pt.ipp.isep.dei.domain.OrderRelated.AllocationStatusType;
import pt.ipp.isep.dei.domain.OrderRelated.LineState;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.domain.Time;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link OrderLine}.
 * Validates allocation logic, quantity calculations, and state transitions.
 */
class OrderLineTest {

    private OrderLine orderLine;

    @BeforeEach
    void setUp() {
        orderLine = new OrderLine("100", 1, "SKU1", 10);
    }

    @Test
    void testConstructorInitializesCorrectly() {
        assertEquals("100L1", orderLine.getOrderLineId());
        assertEquals("SKU1", orderLine.getSkuItem());
        assertEquals(10, orderLine.getQuantity());
        assertEquals(LineState.UNDISPATCHABLE, orderLine.getRealStatus().getState());
        assertEquals(LineState.UNDISPATCHABLE, orderLine.getPossibleState());
    }

    @Test
    void testAddAllocatedBoxAddsCorrectly() {
        orderLine.addAllocatedBox("B1", 5);
        assertEquals(1, orderLine.getAllocatedInfoList().size());
        assertEquals(5, orderLine.getAllocatedInfoList().get("B1").getAllocatedQuantity());
    }

    @Test
    void testGetAllocatedQuantitySumsCorrectly() {
        orderLine.addAllocatedBox("B1", 4);
        orderLine.addAllocatedBox("B2", 6);
        assertEquals(10, orderLine.getAllocatedQuantity());
    }

    @Test
    void testGetMissingQuantityReturnsCorrectValue() {
        orderLine.addAllocatedBox("B1", 4);
        assertEquals(6, orderLine.getMissingQuantity());
    }

    @Test
    void testGetEligibleQuantityUsesFreeQuantityFromBoxes() {
        Box b1 = new Box("B1", "SKU1", 10,
                new Date(1, 1, 2025),
                new Date(1, 1, 2024),
                new Time(10, 0, 0));
        Box b2 = new Box("B2", "SKU1", 5,
                new Date(1, 1, 2025),
                new Date(1, 1, 2024),
                new Time(10, 0, 0));

        b1.setAllocatedQuantity(6);
        b2.setAllocatedQuantity(0);

        Map<String, Box> possibleBoxes = new HashMap<>();
        possibleBoxes.put("B1", b1);
        possibleBoxes.put("B2", b2);
        orderLine.setPossibleBox(possibleBoxes);

        assertEquals(9, orderLine.getEligibleQuantity());
    }

    @Test
    void testGetAllocatedInfoByStateFiltersCorrectly() {
        AllocatedInfo info1 = new AllocatedInfo("B1", 3);
        AllocatedInfo info2 = new AllocatedInfo("B2", 2);
        info2.setAllocationState(AllocationStatusType.PICKING_PLAN_DONE);

        Map<String, AllocatedInfo> map = new HashMap<>();
        map.put("B1", info1);
        map.put("B2", info2);
        orderLine.setAllocatedInfoList(map);

        Map<String, AllocatedInfo> result =
                orderLine.getAllocatedInfoByState(AllocationStatusType.ALLOCATION_DONE);

        assertEquals(1, result.size());
        assertTrue(result.containsKey("B1"));
    }

    @Test
    void testGetAllocatedBoxCountReturnsUniqueDoneBoxes() {
        AllocatedInfo info1 = new AllocatedInfo("B1", 2);
        AllocatedInfo info2 = new AllocatedInfo("B2", 3);
        info1.setAllocationState(AllocationStatusType.ALLOCATION_DONE);
        info2.setAllocationState(AllocationStatusType.ALLOCATION_DONE);

        Map<String, AllocatedInfo> map = new HashMap<>();
        map.put("B1", info1);
        map.put("B2", info2);
        orderLine.setAllocatedInfoList(map);

        assertEquals(2, orderLine.getAllocatedBoxCount());
    }

    @Test
    void testContainsAllocatedOrderByStateTrueWhenExists() {
        AllocatedInfo info = new AllocatedInfo("B1", 2);
        info.setAllocationState(AllocationStatusType.PICKING_PLAN_DONE);
        orderLine.getAllocatedInfoList().put("B1", info);

        assertTrue(orderLine.containsAllocatedOrderByState(AllocationStatusType.PICKING_PLAN_DONE));
    }

    @Test
    void testContainsAllocatedOrderByStateFalseWhenNotExists() {
        assertFalse(orderLine.containsAllocatedOrderByState(AllocationStatusType.PICKING_PATH_DONE));
    }

    @Test
    void testUpdateStatesUndispatchable() {
        orderLine.updateStates();
        assertEquals(LineState.UNDISPATCHABLE, orderLine.getRealStatus().getState());
        assertEquals(LineState.UNDISPATCHABLE, orderLine.getPossibleState());
    }

    @Test
    void testUpdateStatesPartialAndEligible() {
        orderLine.addAllocatedBox("B1", 5);

        Box box = new Box("B2", "SKU1", 10,
                new Date(1, 1, 2025),
                new Date(1, 1, 2024),
                new Time(10, 0, 0));
        box.setAllocatedQuantity(5);
        Map<String, Box> map = new HashMap<>();
        map.put("B2", box);
        orderLine.setPossibleBox(map);

        orderLine.updateStates();

        assertEquals(LineState.PARTIAL, orderLine.getRealStatus().getState());
        assertEquals(LineState.ELIGIBLE, orderLine.getPossibleState());
    }

    @Test
    void testUpdateStatesFullyAllocated() {
        orderLine.addAllocatedBox("B1", 10);
        orderLine.updateStates();
        assertEquals(LineState.ALLOCATED, orderLine.getRealStatus().getState());
        assertEquals(LineState.ALLOCATED, orderLine.getPossibleState());
    }

    @Test
    void testSetOrderIdAndLineNumberUpdatesOrderLineId() {
        orderLine.setOrderId("999");
        orderLine.setLineNumber(3);
        assertEquals("O999L3", orderLine.getOrderLineId());
    }
}
