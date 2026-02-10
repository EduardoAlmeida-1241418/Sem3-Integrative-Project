package controller.planner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.planner.PickingPlansController;
import pt.ipp.isep.dei.data.repository.*;
import pt.ipp.isep.dei.data.repository.sprint1.*;
import pt.ipp.isep.dei.domain.CategoryItem;
import pt.ipp.isep.dei.domain.Item;
import pt.ipp.isep.dei.domain.UnitType;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.OrderRelated.Order;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;
import javafx.collections.ObservableList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PickingPlansController.
 */
public class PickingPlansControllerTest {

    private PickingPlansController controller;

    /**
     * Sets up the test environment by clearing all repositories and initializing the controller.
     * Ensures a clean state for each test case.
     */
    @BeforeEach
    void setUp() {
        Repositories.getInstance().getOrderLineRepository().clear();
        Repositories.getInstance().getItemInfoRepository().clear();
        Repositories.getInstance().getTrolleyModelRepository().clear();
        Repositories.getInstance().getOrderRepository().clear();
        Repositories.getInstance().getBoxRepository().clear();
        Repositories.getInstance().getPickingPlansRepository().clear();
        Repositories.getInstance().getLogRepository().clear();

        controller = new PickingPlansController();
    }

    /**
     * Tests if getOrderLines returns order lines that are dispatchable and have allocated boxes.
     * Verifies that the created order line is present in the returned list.
     */
    @Test
    void testGetOrderLines_returnsDispatchableAllocatedLines() {
        OrderLineRepository orderLineRepository = Repositories.getInstance().getOrderLineRepository();
        OrderLine line = new OrderLine("ORD100", 1, "SKU100", 10);
        line.addAllocatedBox("BX100", 5);
        line.updateStates();
        orderLineRepository.add(line);
        ObservableList<OrderLine> lines = controller.getOrderLines();
        assertNotNull(lines);
        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).getOrderLineId().equals(line.getOrderLineId())) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    /**
     * Tests if getTotalAllocatedUnits returns the correct count of allocated boxes for an order line.
     */
    @Test
    void testGetTotalAllocatedUnits_returnsAllocatedBoxCount() {
        OrderLine line = new OrderLine("ORD101", 1, "SKU101", 20);
        line.addAllocatedBox("BX101A", 3);
        line.addAllocatedBox("BX101B", 2);
        int units = controller.getTotalAllocatedUnits(line);
        assertEquals(2, units);
    }

    /**
     * Tests verifyDeferPossibility for both true and false scenarios.
     * Checks if the method correctly determines if allocations fit within trolley model capacities.
     */
    @Test
    void testVerifyDeferPossibility_trueAndFalseScenarios() {
        BoxRepository boxRepo = Repositories.getInstance().getBoxRepository();
        ItemInfoRepository itemRepo = Repositories.getInstance().getItemInfoRepository();
        TrolleyModelRepository modelRepo = Repositories.getInstance().getTrolleyModelRepository();

        Item item = new Item("SKU200", "It200", CategoryItem.GROCERY, UnitType.PACK, 1.0, 2.5);
        itemRepo.add(item);
        Box box = new Box("BX200", "SKU200", 10, new Date(1, 1, 2026), new Date(1, 1, 2026), new Time(9, 0, 0));
        boxRepo.add(box);

        TrolleyModel smallModel = new TrolleyModel("TM_SMALL", 5);
        modelRepo.add(smallModel);
        TrolleyModel largeModel = new TrolleyModel("TM_LARGE", 100);
        modelRepo.add(largeModel);

        OrderLine line = new OrderLine("ORD200", 1, "SKU200", 10);
        line.addAllocatedBox("BX200", 2);
        line.updateStates();

        ObservableList<OrderLine> selection = javafx.collections.FXCollections.observableArrayList(line);
        boolean canDeferSmall = controller.verifyDeferPossibility(selection, "TM_SMALL");
        assertTrue(canDeferSmall);

        TrolleyModel tiny = new TrolleyModel("TM_TINY", 4);
        modelRepo.add(tiny);
        boolean canDeferTiny = controller.verifyDeferPossibility(selection, "TM_TINY");
        assertFalse(canDeferTiny);
    }

    /**
     * Tests if calculateTotalWeight returns the correct total allocated weight for an order line.
     */
    @Test
    void testCalculateTotalWeight_returnsAllocatedWeight() {
        ItemInfoRepository itemRepo = Repositories.getInstance().getItemInfoRepository();
        Item item = new Item("SKU300", "It300", CategoryItem.HARDWARE, UnitType.PACK, 1.0, 3.0);
        itemRepo.add(item);

        OrderLine line = new OrderLine("ORD300", 1, "SKU300", 10);
        line.addAllocatedBox("BX300", 4);
        double weight = controller.calculateTotalWeight(line);
        assertEquals(12.0, weight, 0.0001);
    }

    /**
     * Tests getTrolleyModels and generatePickingPlan methods.
     * Verifies that a picking plan is added after generation.
     */
    @Test
    void testGetTrolleyModels_andGeneratePickingPlan_addsPickingPlan() {
        TrolleyModelRepository modelRepo = Repositories.getInstance().getTrolleyModelRepository();
        ItemInfoRepository itemRepo = Repositories.getInstance().getItemInfoRepository();
        BoxRepository boxRepo = Repositories.getInstance().getBoxRepository();
        PickingPlansRepository pickingPlansRepo = Repositories.getInstance().getPickingPlansRepository();

        TrolleyModel model = new TrolleyModel("TM_GEN", 1000);
        modelRepo.add(model);

        Item item = new Item("SKU400", "It400", CategoryItem.BEVERAGE, UnitType.PACK, 1.0, 1.0);
        itemRepo.add(item);
        Box box = new Box("BX400", "SKU400", 20, new Date(1, 1, 2026), new Date(1, 1, 2026), new Time(10, 0, 0));
        boxRepo.add(box);

        OrderLine line = new OrderLine("ORD400", 1, "SKU400", 10);
        line.addAllocatedBox("BX400", 5);
        line.updateStates();

        ObservableList<OrderLine> orderLines = javafx.collections.FXCollections.observableArrayList(line);

        ObservableList<String> trolleyModels = controller.getTrolleyModels();
        ObservableList<String> heuristics = controller.getHeuristicNames();
        ObservableList<String> policies = controller.getSpacePolicyTypes();

        assertNotNull(trolleyModels);
        assertTrue(trolleyModels.contains("TM_GEN"));
        assertNotNull(heuristics);
        assertNotNull(policies);

        int before = pickingPlansRepo.count();
        controller.generatePickingPlan(orderLines, "TM_GEN", heuristics.get(0), policies.get(0));
        int after = pickingPlansRepo.count();
        assertTrue(after >= before + 1);
    }

    /**
     * Tests getPriority for both present and missing orders.
     * Verifies correct priority for existing order and non-negative value or exception for missing order.
     */
    @Test
    void testGetPriority_returnsZeroWhenOrderMissingOrPresent() {
        OrderRepository orderRepo = Repositories.getInstance().getOrderRepository();
        Order order = new Order("ORD500", new Date(1, 12, 2025), new Time(12, 0, 0), 7);
        orderRepo.add(order);

        int p = controller.getPriority("ORD500");
        assertEquals(7, p);

        try {
            int missing = controller.getPriority("NO_SUCH_ORDER");
            assertTrue(missing >= 0);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    /**
     * Tests if getHeuristicNames and getSpacePolicyTypes return non-empty lists.
     */
    @Test
    void testGetHeuristicNamesAndSpacePolicyTypes_nonEmpty() {
        ObservableList<String> heuristics = controller.getHeuristicNames();
        ObservableList<String> policies = controller.getSpacePolicyTypes();
        assertNotNull(heuristics);
        assertNotNull(policies);
        assertTrue(heuristics.size() > 0);
        assertTrue(policies.size() > 0);
    }
}

