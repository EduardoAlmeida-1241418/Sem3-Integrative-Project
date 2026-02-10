package controller.planner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.planner.CreateTrolleyController;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;
import pt.ipp.isep.dei.data.repository.Repositories;
import javafx.collections.ObservableList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CreateTrolleyController class.
 */
public class CreateTrolleyControllerTest {

    /**
     * Ensure repository is cleared before each test so tests are deterministic.
     */
    @BeforeEach
    public void clearRepos() {
        Repositories.getInstance().getTrolleyModelRepository().clear();
    }

    /**
     * Tests retrieving the trolley model list and its initial state.
     */
    @Test
    public void testGetTrolleyModelList() {
        CreateTrolleyController controller = new CreateTrolleyController();
        ObservableList<TrolleyModel> list = controller.getTrolleyModelList();
        assertNotNull(list);
        assertTrue(list.size() >= 0);
    }

    /**
     * Tests creating a new trolley model with valid data.
     */
    @Test
    public void testCreateTrolleyModelValid() {
        CreateTrolleyController controller = new CreateTrolleyController();
        String name = "Standard";
        String maxWeight = "1000";
        String result = controller.createTrolleyModel(name, maxWeight);
        assertNull(result);
        ObservableList<TrolleyModel> list = controller.getTrolleyModelList();
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            TrolleyModel model = list.get(i);
            if (model.getName().equals(name) && model.getMaxWeight() == 1000) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    /**
     * Tests creating a trolley model with a duplicate name.
     */
    @Test
    public void testCreateTrolleyModelDuplicateName() {
        CreateTrolleyController controller = new CreateTrolleyController();
        String name = "HeavyDuty";
        String maxWeight = "1500";
        controller.createTrolleyModel(name, maxWeight);
        String result = controller.createTrolleyModel(name, "2000");
        assertEquals("Name already exists", result);
    }

    /**
     * Tests creating a trolley model with a non-numeric max weight.
     */
    @Test
    public void testCreateTrolleyModelInvalidWeightNonNumeric() {
        CreateTrolleyController controller = new CreateTrolleyController();
        String result = controller.createTrolleyModel("Light", "abc");
        assertEquals("Max weight must be a valid number", result);
    }

    /**
     * Tests creating a trolley model with a negative max weight.
     */
    @Test
    public void testCreateTrolleyModelInvalidWeightNegative() {
        CreateTrolleyController controller = new CreateTrolleyController();
        String result = controller.createTrolleyModel("Mini", "-500");
        assertEquals("Max weight must be a positive number", result);
    }

    /**
     * Tests creating a trolley model with zero max weight.
     */
    @Test
    public void testCreateTrolleyModelInvalidWeightZero() {
        CreateTrolleyController controller = new CreateTrolleyController();
        String result = controller.createTrolleyModel("Zero", "0");
        assertEquals("Max weight must be a positive number", result);
    }
}