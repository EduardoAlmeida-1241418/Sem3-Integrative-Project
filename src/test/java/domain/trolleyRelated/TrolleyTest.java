package domain.trolleyRelated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.domain.trolleyRelated.Trolley;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyAllocation;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Trolley}.
 * Validates construction, capacity handling, allocation management,
 * and weight calculation behaviors.
 */
class TrolleyTest {

    private TrolleyModel model;
    private Trolley trolley;
    private TrolleyAllocation allocation1;
    private TrolleyAllocation allocation2;
    private Box box1;
    private Box box2;

    @BeforeEach
    void setUp() {
        try {
            var field = Trolley.class.getDeclaredField("trolleyCounter");
            field.setAccessible(true);
            field.setInt(null, 1);
        } catch (Exception e) {
            fail("Error resetting counter: " + e.getMessage());
        }

        model = new TrolleyModel("TM", 100);
        trolley = new Trolley(model);

        box1 = new Box("B1", "SKU1", 10,
                new Date(1, 1, 2026),
                new Date(25, 10, 2025),
                new Time(8, 30, 0));
        box2 = new Box("B2", "SKU2", 5,
                new Date(1, 1, 2026),
                new Date(25, 10, 2025),
                new Time(9, 0, 0));

        allocation1 = new TrolleyAllocation(box1, 2, new OrderLine("O1", 1, "SKU1", 5), 10.0);
        allocation2 = new TrolleyAllocation(box2, 1, new OrderLine("O1", 2, "SKU2", 10), 60.0);
    }

    @Test
    void testSetAndGetModel() {
        TrolleyModel newModel = new TrolleyModel("NEW", 200);
        trolley.setModel(newModel);
        assertEquals(newModel, trolley.getModel());
    }

    @Test
    void testSetAndGetTrolleyId() {
        trolley.setTrolleyId("CUSTOM_ID");
        assertEquals("CUSTOM_ID", trolley.getTrolleyId());
    }

    @Test
    void testSetAndGetCurrentWeight() {
        trolley.setCurrentWeight(40.0);
        assertEquals(40.0, trolley.getCurrentWeight());
    }

    @Test
    void testSetAndGetTrolleyAllocations() {
        List<TrolleyAllocation> list = new ArrayList<>();
        list.add(allocation1);
        trolley.setTrolleyAllocations(list);
        assertEquals(1, trolley.getTrolleyAllocations().size());
    }

    @Test
    void testGetAvailableWeightWhenEmpty() {
        assertEquals(100.0, trolley.getAvailableWeight(), 0.0001);
    }

    @Test
    void testGetAvailableWeightAfterAllocation() {
        trolley.setCurrentWeight(40.0);
        assertEquals(60.0, trolley.getAvailableWeight(), 0.0001);
    }

    @Test
    void testHasSpaceReturnsTrueWhenEnoughCapacity() {
        assertTrue(trolley.hasSpace(allocation1));
    }

    @Test
    void testHasSpaceReturnsFalseWhenOverCapacity() {
        trolley.setCurrentWeight(90.0);
        assertFalse(trolley.hasSpace(allocation2));
    }

    @Test
    void testHasSpaceForItemReturnsTrueAndFalse() {
        assertTrue(trolley.hasSpaceForItem(50.0));
        trolley.setCurrentWeight(90.0);
        assertFalse(trolley.hasSpaceForItem(20.0));
    }

    @Test
    void testAddTrolleyAllocationAddsWhenSpaceAvailable() {
        trolley.addTrolleyAllocation(allocation1);
        assertEquals(1, trolley.getTrolleyAllocations().size());
        assertEquals(20.0, trolley.getCurrentWeight(), 0.0001);
    }

    @Test
    void testAddTrolleyAllocationDoesNotAddWhenNoSpace() {
        trolley.setCurrentWeight(95.0);
        trolley.addTrolleyAllocation(allocation2);
        assertTrue(trolley.getTrolleyAllocations().isEmpty());
        assertEquals(95.0, trolley.getCurrentWeight());
    }
}
