package controller.warehousePlanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.wareHousePlanner.OrderAllocationsController;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.data.repository.sprint1.OrderLineRepository;
import pt.ipp.isep.dei.data.repository.Repositories;
import javafx.collections.ObservableList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OrderAllocationsController.
 */
public class OrderAllocationControllerTest {

    private OrderAllocationsController controller;
    private OrderLineRepository lineRepo;

    /**
     * Sets up the test environment before each test execution.
     * Initializes the controller and repository, and clears the repository contents.
     */
    @BeforeEach
    void setUp() {
        controller = new OrderAllocationsController();
        lineRepo = Repositories.getInstance().getOrderLineRepository();
        lineRepo.clear();
    }

    /**
     * Tests retrieving order lines and calculating the total allocated units for an order line.
     * Verifies that the order lines are loaded correctly and the total allocated units are accurate.
     */
    @Test
    void testGetOrderLines_and_getTotalAllocatedUnits() {
        OrderLine l1 = new OrderLine("ORDA", 1, "SKU1", 10);
        OrderLine l2 = new OrderLine("ORDB", 1, "SKU2", 5);
        l1.addAllocatedBox("BX1", 3);
        l1.addAllocatedBox("BX2", 2);

        lineRepo.add(l1);
        lineRepo.add(l2);

        ObservableList<OrderLine> lines = controller.getOrderLines();
        assertNotNull(lines);
        assertTrue(lines.size() >= 2);

        int totalAllocatedBoxes = controller.getTotalAllocatedUnits(l1);
        assertEquals(2, totalAllocatedBoxes);
    }
}