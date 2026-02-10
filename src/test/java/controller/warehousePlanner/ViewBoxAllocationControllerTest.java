package controller.warehousePlanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.wareHousePlanner.ViewBoxAllocationController;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Bay;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ViewBoxAllocationController.
 */
public class ViewBoxAllocationControllerTest {

    private ViewBoxAllocationController controller;
    private BayRepository bayRepo;

    /**
     * Sets up the test environment by initializing the controller and clearing the BayRepository.
     */
    @BeforeEach
    void setUp() {
        controller = new ViewBoxAllocationController();
        bayRepo = Repositories.getInstance().getBayRepository();
        bayRepo.clear();
    }

    /**
     * Tests getBoxes, getItemQtyInSameBox, and getAisleID methods.
     * Ensures the Box is added to the BoxRepository before any comparator or controller usage to prevent NoSuchElementException.
     */
    @Test
    void testGetBoxes_and_getItemQtyInSameBox_and_getAisleID() {
        OrderLine line = new OrderLine("ORD1", 1, "SKU1", 10);
        line.addAllocatedBox("BX1", 4);
        Box box = new Box("BX1", "SKU1", 4, null, null, null);
        Repositories.getInstance().getBoxRepository().add(box);
        line.getPossibleBox().put("BX1", box);
        Bay bay = new Bay("W1A1", 1, 5);
        bayRepo.add(bay);
        box.setBayId(bay.getBayId());
        controller.setData(line);
        List<Box> boxes = controller.getBoxes();
        assertNotNull(boxes);
        assertEquals(1, boxes.size());
        assertEquals("BX1", boxes.get(0).getBoxId());
        String qty = controller.getItemQtyInSameBox(box);
        assertEquals(String.valueOf(4), qty);
        String aisleId = controller.getAisleID(bay.getBayId());
        assertEquals(bay.getAisleId(), aisleId);
    }

    /**
     * Tests that getBoxes returns an empty list when the order line is null.
     */
    @Test
    void testGetBoxes_withNullOrderLine_returnsEmptyList() {
        controller.setData(null);
        List<Box> boxes = controller.getBoxes();
        assertNotNull(boxes);
        assertTrue(boxes.isEmpty());
    }
}