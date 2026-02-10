package domain.PickingPlanRelated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.pickingPlanRelated.PickingPlan;
import pt.ipp.isep.dei.domain.trolleyRelated.Trolley;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PickingPlan}.
 * Validates ID generation, trolley management, model association, and string representation.
 */
class PickingPlanTest {

    private TrolleyModel model;
    private PickingPlan pickingPlan;

    @BeforeEach
    void setUp() {
        // Reset static counter via reflection to ensure predictable IDs
        try {
            var field = PickingPlan.class.getDeclaredField("counter");
            field.setAccessible(true);
            field.setInt(null, 0);
        } catch (Exception e) {
            fail("Failed to reset counter: " + e.getMessage());
        }

        model = new TrolleyModel("ModelA", 100);
        pickingPlan = new PickingPlan(model);
    }

    /**
     * Verifies constructor initialization and auto-generated ID.
     */
    @Test
    void testConstructorInitializesFields() {
        assertNotNull(pickingPlan.getId());
        assertEquals("PP000", pickingPlan.getId());
        assertEquals(model, pickingPlan.getTrolleyModel());
        assertTrue(pickingPlan.getTrolleys().isEmpty());
    }

    /**
     * Verifies that IDs increment sequentially across instances.
     */
    @Test
    void testIdIncrementsSequentially() {
        PickingPlan secondPlan = new PickingPlan(model);
        assertEquals("PP001", secondPlan.getId());
    }

    /**
     * Verifies that a new trolley can be added successfully.
     */
    @Test
    void testAddTrolleyAddsNewTrolley() {
        pickingPlan.addTrolley();
        assertEquals(1, pickingPlan.getTrolleys().size());
        assertTrue(pickingPlan.getTrolleys().get(0) instanceof Trolley);
    }

    /**
     * Verifies getter and setter behavior for trolley list.
     */
    @Test
    void testSetAndGetTrolleys() {
        List<Trolley> trolleys = new ArrayList<>();
        trolleys.add(new Trolley(model));
        pickingPlan.setTrolleys(trolleys);
        assertEquals(trolleys, pickingPlan.getTrolleys());
    }

    /**
     * Verifies getter and setter behavior for trolley model.
     */
    @Test
    void testSetAndGetTrolleyModel() {
        TrolleyModel newModel = new TrolleyModel("ModelB", 200);
        pickingPlan.setTrolleyModel(newModel);
        assertEquals(newModel, pickingPlan.getTrolleyModel());
    }

    /**
     * Verifies getter and setter behavior for picking plan ID.
     */
    @Test
    void testSetAndGetId() {
        pickingPlan.setId("PP999");
        assertEquals("PP999", pickingPlan.getId());
    }

    /**
     * Verifies that toString output contains all key elements.
     */
    @Test
    void testToStringContainsAllKeyDetails() {
        pickingPlan.addTrolley();
        String result = pickingPlan.toString();

        assertTrue(result.contains("Picking Plan ID:"));
        assertTrue(result.contains("Trolley Model: ModelA"));
        assertTrue(result.contains("Number of Trolleys: 1"));
        assertTrue(result.contains("Trolley"));
    }

    /**
     * Verifies that toString handles an empty trolley list properly.
     */
    @Test
    void testToStringWhenNoTrolleys() {
        String result = pickingPlan.toString();
        assertTrue(result.contains("No trolleys in this picking plan"));
    }
}
