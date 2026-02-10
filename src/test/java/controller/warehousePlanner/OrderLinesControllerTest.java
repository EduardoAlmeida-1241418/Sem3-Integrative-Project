package controller.warehousePlanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.wareHousePlanner.OrderLinesController;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.OrderRelated.Order;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.domain.OrderRelated.ViewMode;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.sprint1.OrderLineRepository;
import pt.ipp.isep.dei.data.repository.sprint1.OrderRepository;
import pt.ipp.isep.dei.data.repository.Repositories;
import javafx.collections.ObservableList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pt.ipp.isep.dei.domain.OrderRelated.AllocationStatusType.ALLOCATION_DONE;

/**
 * Unit tests for OrderLinesController.
 */
public class OrderLinesControllerTest {

    private OrderLinesController controller;
    private OrderRepository orderRepo;
    private OrderLineRepository lineRepo;
    private BoxRepository boxRepo;

    /**
     * Sets up the test environment before each test execution.
     * Initializes the controller and repositories, and clears their contents.
     */
    @BeforeEach
    void setUp() {
        controller = new OrderLinesController();
        var repos = Repositories.getInstance();
        orderRepo = repos.getOrderRepository();
        lineRepo = repos.getOrderLineRepository();
        boxRepo = repos.getBoxRepository();

        orderRepo.clear();
        lineRepo.clear();
        boxRepo.clear();
    }

    /**
     * Tests loading line info, retrieving order lines, and allocation process.
     * Verifies that the order line is correctly loaded, requested and allocated quantities are accurate,
     * and allocation completes as expected.
     */
    @Test
    void testLoadLineInfo_and_getOrderLines_and_allocation() {
        Order order = new Order("ORD100", new Date(1,1,2025), 1);
        OrderLine line = new OrderLine("ORD100", 1, "SKU100", 5);
        lineRepo.add(line);
        order.addOrderLine(line.getOrderLineId());
        orderRepo.add(order);

        Box box = new Box("BOX100", "SKU100", 10, null, new Date(1,1,2025), new Time(9,0,0));
        box.setBayId("W1A1B1");
        boxRepo.add(box);

        controller.setOrder(order);
        controller.loadLineInfo();

        List<OrderLine> lines = controller.getOrderLines();
        assertNotNull(lines);
        assertEquals(1, lines.size());
        assertEquals(line.getOrderLineId(), lines.get(0).getOrderLineId());

        assertEquals(String.valueOf(5), controller.getRequestedQuantity(line));
        assertEquals("0", controller.getAllocatedQuantity(line));

        String msg = controller.allocateOrderLine(line);
        assertEquals("Allocation Completed", msg);
        assertTrue(line.containsAllocatedOrderByState(ALLOCATION_DONE) ||
                line.getAllocatedQuantity() > 0);
    }

    /**
     * Tests that the view mode values list is not empty.
     * Ensures that at least one view mode is available.
     */
    @Test
    void testViewModeValues_nonEmpty() {
        ObservableList<ViewMode> modes = controller.getViewModeValues();
        assertNotNull(modes);
        assertTrue(modes.size() >= 1);
    }

    /**
     * Tests the behavior of verifyQuantityLeft method.
     * Checks that the method returns true when no eligible boxes are available for allocation.
     */
    @Test
    void testVerifyQuantityLeft_behaviour() {
        OrderLine line = new OrderLine("O2", 1, "SKUX", 3);
        assertTrue(controller.verifyQuantityLeft(line));
    }
}