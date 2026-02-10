package controller.warehousePlanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.wareHousePlanner.ChooseOrderController;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.OrderRelated.LineState;
import pt.ipp.isep.dei.domain.OrderRelated.Order;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.data.repository.sprint1.OrderLineRepository;
import pt.ipp.isep.dei.data.repository.sprint1.OrderRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ChooseOrderController.
 */
public class ChooseOrderControllerTest {

    private ChooseOrderController controller;
    private OrderRepository orderRepo;
    private OrderLineRepository lineRepo;

    /**
     * Sets up the test environment before each test execution.
     * Initializes the controller and repositories, and clears their contents.
     */
    @BeforeEach
    void setUp() {
        controller = new ChooseOrderController();
        var repos = Repositories.getInstance();
        orderRepo = repos.getOrderRepository();
        lineRepo = repos.getOrderLineRepository();

        orderRepo.clear();
        lineRepo.clear();
    }

    /**
     * Tests loading order info, retrieving the order list, and getting the number of lines in an order.
     * Verifies that the order is loaded, present in the list, and the line count is correct.
     */
    @Test
    void testLoadInfo_and_getOrderList_and_getLineNumber() {
        Order order = new Order("O100", new Date(1,1,2025), 1);
        OrderLine l1 = new OrderLine("O100", 1, "SKU1", 2);
        OrderLine l2 = new OrderLine("O100", 2, "SKU2", 3);

        lineRepo.add(l1);
        lineRepo.add(l2);

        order.addOrderLine(l1.getOrderLineId());
        order.addOrderLine(l2.getOrderLineId());
        orderRepo.add(order);

        controller.loadInfo();
        List<Order> orders = controller.getOrderList();
        assertNotNull(orders);
        boolean found = false;
        for (Order o : orders) {
            if (o.getOrderId().equals("O100")) {
                found = true;
                break;
            }
        }
        assertTrue(found);

        assertEquals(2, controller.getLineNumber(order));
    }

    /**
     * Tests counting the number of lines in a specific state (UNDISPATCHABLE) for an order.
     * Verifies that lines with no allocations are correctly counted as undispatchable.
     */
    @Test
    void testGetQuantityOfStateLines_countsUndispatchable() {
        Order order = new Order("O200", new Date(1,1,2025), 1);
        OrderLine l1 = new OrderLine("O200", 1, "SKUX", 5); // no allocations -> UNDISPATCHABLE after update
        OrderLine l2 = new OrderLine("O200", 2, "SKUY", 4);

        lineRepo.add(l1);
        lineRepo.add(l2);

        order.addOrderLine(l1.getOrderLineId());
        order.addOrderLine(l2.getOrderLineId());
        orderRepo.add(order);

        BoxRepository boxRepo = Repositories.getInstance().getBoxRepository();
        boxRepo.clear();
        boxRepo.add(new Box("B1", "SKUX", 0, null, new Date(1,1,2025), new Time(9,0,0)));
        boxRepo.add(new Box("B2", "SKUY", 0, null, new Date(1,1,2025), new Time(9,0,0)));

        controller.loadInfo();
        String countUndispatchable = controller.getQuantityOfStateLines(order, LineState.UNDISPATCHABLE);
        assertEquals("2", countUndispatchable);
    }
}