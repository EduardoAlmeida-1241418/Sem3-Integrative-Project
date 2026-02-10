package domain.trolleyRelated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TrolleyModel}.
 * Validates construction, field access, ID generation, and string representation.
 */
class TrolleyModelTest {

    @BeforeEach
    void resetCounter() {
        try {
            var field = TrolleyModel.class.getDeclaredField("modelCounter");
            field.setAccessible(true);
            field.setInt(null, 0);
        } catch (Exception e) {
            fail("Error resetting counter: " + e.getMessage());
        }
    }

    @Test
    void testConstructorAssignsValuesAndGeneratesId() {
        TrolleyModel model = new TrolleyModel("Alpha", 100);
        assertEquals("Alpha", model.getName());
        assertEquals(100, model.getMaxWeight());
        assertEquals("TM0", model.getId());
    }

    @Test
    void testIdIncrementsAutomatically() {
        TrolleyModel m1 = new TrolleyModel("A", 50);
        TrolleyModel m2 = new TrolleyModel("B", 60);
        TrolleyModel m3 = new TrolleyModel("C", 70);
        assertEquals("TM0", m1.getId());
        assertEquals("TM1", m2.getId());
        assertEquals("TM2", m3.getId());
    }

    @Test
    void testSetAndGetName() {
        TrolleyModel model = new TrolleyModel("Old", 80);
        model.setName("NewName");
        assertEquals("NewName", model.getName());
    }

    @Test
    void testSetAndGetMaxWeight() {
        TrolleyModel model = new TrolleyModel("Beta", 150);
        model.setMaxWeight(200);
        assertEquals(200, model.getMaxWeight());
    }

    @Test
    void testSetAndGetId() {
        TrolleyModel model = new TrolleyModel("Gamma", 120);
        model.setId("TM99");
        assertEquals("TM99", model.getId());
    }

    @Test
    void testToStringContainsAllFields() {
        TrolleyModel model = new TrolleyModel("Delta", 90);
        String result = model.toString();
        assertTrue(result.contains("TM0"));
        assertTrue(result.contains("Delta"));
        assertTrue(result.contains("90"));
    }
}
