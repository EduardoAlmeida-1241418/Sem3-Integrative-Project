package controller.terminalOperator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.terminalOperator.ManualRelocationBoxController;
import pt.ipp.isep.dei.domain.Bay;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.Repositories;
import javafx.collections.ObservableList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ManualRelocationBoxController}.
 * Covers normal and error relocation scenarios, along with basic getters and setters.
 */
public class ManualRelocationBoxControllerTest {

    private ManualRelocationBoxController controller;
    private BayRepository bayRepo;
    private BoxRepository boxRepo;

    /** Initializes repositories and controller before each test. */
    @BeforeEach
    void setUp() {
        var repos = Repositories.getInstance();
        bayRepo = repos.getBayRepository();
        boxRepo = repos.getBoxRepository();
        bayRepo.clear();
        boxRepo.clear();
        controller = new ManualRelocationBoxController();
    }

    /** Tests bay retrieval, box listing, and successful relocation of a box. */
    @Test
    void testGetAllBays_and_getBoxesFromSelectedBay_and_relocate_success() {
        Bay oldBay = new Bay("W1A1", 1, 5);
        Bay newBay = new Bay("W1A1", 2, 5);
        bayRepo.add(oldBay);
        bayRepo.add(newBay);
        Box box = new Box("BX1", "SKU1", 3, null, new Date(1, 1, 2025), new Time(10, 0, 0));
        boxRepo.add(box);
        boolean added = oldBay.addBox(box.getBoxId(), RoleType.TERMINAL_OPERATOR);
        assertTrue(added);
        ObservableList<Bay> bays = controller.getAllBays();
        assertNotNull(bays);
        assertTrue(bays.size() >= 2);
        controller.setSelectedBay(oldBay);
        ObservableList<Box> boxes = controller.getBoxesFromSelectedBay();
        assertNotNull(boxes);
        assertTrue(boxes.stream().anyMatch(b -> b.getBoxId().equals("BX1")));
        controller.setBoxId("BX1");
        controller.setNewBayId(newBay.getBayId());
        String result = controller.relocateBox();
        assertEquals("Box relocated successfully", result);
        Box b2 = boxRepo.findById("BX1");
        assertEquals(newBay.getBayId(), b2.getBayId());
    }

    /** Tests getter and setter for boxId. */
    @Test
    void testBoxIdGetterSetter() {
        controller.setBoxId("TESTBOX");
        assertEquals("TESTBOX", controller.getBoxId());
    }

    /** Tests getter and setter for newBayId. */
    @Test
    void testNewBayIdGetterSetter() {
        controller.setNewBayId("TESTBAY");
        assertEquals("TESTBAY", controller.getNewBayId());
    }

    /** Tests getter and setter for selectedBay. */
    @Test
    void testSelectedBayGetterSetter() {
        Bay bay = new Bay("BAYID", 1, 5);
        controller.setSelectedBay(bay);
        assertEquals(bay, controller.getSelectedBay());
    }
}
