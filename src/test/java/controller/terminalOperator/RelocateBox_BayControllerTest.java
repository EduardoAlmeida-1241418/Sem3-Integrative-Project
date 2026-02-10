package controller.terminalOperator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.terminalOperator.RelocateBox_Bay_Controller;
import pt.ipp.isep.dei.domain.Bay;
import pt.ipp.isep.dei.domain.Aisle;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RelocateBox_Bay_Controller.
 * Covers all public methods and main scenarios, including success and error cases.
 */
public class RelocateBox_BayControllerTest {

    private RelocateBox_Bay_Controller controller;
    private BayRepository bayRepo;

    /**
     * Initializes repositories and controller before each test.
     */
    @BeforeEach
    void setUp() {
        var repos = Repositories.getInstance();
        bayRepo = repos.getBayRepository();
        bayRepo.clear();
        controller = new RelocateBox_Bay_Controller();
    }

    /**
     * Tests colorByBayOccupancy, isBayFull, isBayEmpty, getSkuByBayId, and getBayById methods.
     */
    @Test
    void testColorByBayOccupancy_and_isBayFull_isBayEmpty_and_getSkuAndGetBay() {
        // create bay with capacity 10 and 0 stored
        Bay bay = new Bay("W1A1", 1, 10);
        bayRepo.add(bay);

        // occupancy 0 => green
        String color0 = controller.colorByBayOccupancy(bay.getBayId());
        assertEquals("#2ECC71", color0);

        // set 2 boxes => occupancy 20% => green
        bay.setNBoxesStorage(2);
        String color20 = controller.colorByBayOccupancy(bay.getBayId());
        assertEquals("#2ECC71", color20);

        // set 3 boxes => occupancy 30% => lime
        bay.setNBoxesStorage(3);
        assertEquals("#A3E635", controller.colorByBayOccupancy(bay.getBayId()));

        // set 5 boxes => 50% => yellow
        bay.setNBoxesStorage(5);
        assertEquals("#FACC15", controller.colorByBayOccupancy(bay.getBayId()));

        // set 7 boxes => 70% => orange
        bay.setNBoxesStorage(7);
        assertEquals("#F97316", controller.colorByBayOccupancy(bay.getBayId()));

        // set 9 boxes => 90% => red
        bay.setNBoxesStorage(9);
        assertEquals("#EF4444", controller.colorByBayOccupancy(bay.getBayId()));

        // set 10 boxes => 100% => dark red
        bay.setNBoxesStorage(10);
        assertEquals("#8B0000", controller.colorByBayOccupancy(bay.getBayId()));

        // getSkuByBayId when sku is null -> returns empty string per controller logic? controller returns bay.getSkuItem() or ""
        assertEquals(bay.getSkuItem(), controller.getSkuByBayId(bay.getBayId()));

        // isBayFull/isBayEmpty
        assertTrue(controller.isBayFull(bay.getBayId()));
        bay.setNBoxesStorage(0);
        assertTrue(controller.isBayEmpty(bay.getBayId()));
        assertFalse(controller.isBayFull(bay.getBayId()));

        // getBayById
        assertEquals(bay, controller.getBayById(bay.getBayId()));
    }

    /**
     * Tests baysWithDifferentSkus method for different scenarios.
     */
    @Test
    void testBaysWithDifferentSkus_behaviour() {
        Bay b1 = new Bay("W1A2", 1, 5);
        Bay b2 = new Bay("W1A2", 2, 5);
        bayRepo.add(b1);
        bayRepo.add(b2);

        // initially both have null sku -> should return false
        assertFalse(controller.baysWithDifferentSkus(b1.getBayId(), b2.getBayId()));

        // set sku for one bay
        b1.setSkuItem("SKU_A");
        assertFalse(controller.baysWithDifferentSkus(b1.getBayId(), b2.getBayId()));

        // set different sku for other bay
        b2.setSkuItem("SKU_B");
        assertTrue(controller.baysWithDifferentSkus(b1.getBayId(), b2.getBayId()));
    }

    /**
     * Tests getBayColor method when bay is not present.
     */
    @Test
    void testGetBayColor_whenNotPresent_returnsZero() {
        assertEquals("0", controller.getBayColor("NO_SUCH_BAY"));
    }

    /**
     * Tests getWarehouseId and setWarehouseId methods.
     */
    @Test
    void testWarehouseIdGetterSetter() {
        controller.setWarehouseId("WH1");
        assertEquals("WH1", controller.getWarehouseId());
    }

    /**
     * Tests getWarehousesIds method.
     */
    @Test
    void testGetWarehousesIds() {
        List<String> ids = controller.getWarehousesIds();
        assertNotNull(ids);
    }

    /**
     * Tests getAisles method.
     */
    @Test
    void testGetAisles() {
        List<Aisle> aisles = controller.getAisles();
        assertNotNull(aisles);
    }

    /**
     * Tests setBayColors method (should not throw exception).
     */
    @Test
    void testSetBayColors() {
        assertDoesNotThrow(() -> controller.setBayColors());
    }
}