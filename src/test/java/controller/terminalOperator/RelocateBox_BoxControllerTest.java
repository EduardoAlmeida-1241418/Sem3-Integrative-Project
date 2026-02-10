package controller.terminalOperator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.terminalOperator.RelocateBox_Box_Controller;
import pt.ipp.isep.dei.domain.Bay;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RelocateBox_Box_Controller.
 * Covers all public methods and main scenarios, including success and error cases.
 */
public class RelocateBox_BoxControllerTest {

    private RelocateBox_Box_Controller controller;
    private BayRepository bayRepo;
    private BoxRepository boxRepo;

    /**
     * Initializes repositories and controller before each test.
     */
    @BeforeEach
    void setUp() {
        var repos = Repositories.getInstance();
        bayRepo = repos.getBayRepository();
        boxRepo = repos.getBoxRepository();
        bayRepo.clear();
        boxRepo.clear();
        controller = new RelocateBox_Box_Controller();
    }

    /**
     * Tests getActualBay and setActualBay methods.
     */
    @Test
    void testActualBayGetterSetter() {
        Bay bay = new Bay("W1A3B1", 1, 5); // valid bayId format
        controller.setActualBay(bay);
        assertEquals(bay, controller.getActualBay());
    }

    /**
     * Tests getNewBay and setNewBay methods.
     */
    @Test
    void testNewBayGetterSetter() {
        Bay bay = new Bay("W1A4B2", 2, 5); // valid bayId format
        controller.setNewBay(bay);
        assertEquals(bay, controller.getNewBay());
    }

    /**
     * Tests setSelectedBaysById method.
     */
    @Test
    void testSetSelectedBaysById() {
        Bay actual = new Bay("W1A1B3", 3, 5); // valid bayId format
        Bay newBay = new Bay("W1A2B4", 4, 5); // valid bayId format
        bayRepo.add(actual);
        bayRepo.add(newBay);
        controller.setSelectedBaysById("W1A1B3", "W1A2B4");
        assertEquals(actual, controller.getActualBay());
        assertEquals(newBay, controller.getNewBay());
    }

    /**
     * Tests getSelectedBox and setSelectedBox methods.
     */
    @Test
    void testSelectedBoxGetterSetter() {
        Box box = new Box("BX1", "SKU1", 1, null, new Date(1,1,2025), new Time(10,0,0));
        controller.setSelectedBox(box);
        assertEquals(box, controller.getSelectedBox());
    }

    /**
     * Tests setSelectedBoxById method.
     */
    @Test
    void testSetSelectedBoxById() {
        Box box = new Box("BX2", "SKU2", 2, null, new Date(2,2,2025), new Time(11,0,0));
        boxRepo.add(box);
        controller.setSelectedBoxById("BX2");
        assertEquals(box, controller.getSelectedBox());
    }

    /**
     * Tests getBoxes, getBoxesObservableList, and bayIsEmpty methods.
     */
    @Test
    void testGetBoxes_and_getBoxesObservableList_and_bayIsEmpty() {
        Bay source = new Bay("W1A1B1", 1, 5);
        bayRepo.add(source);
        controller.setActualBay(source);
        assertTrue(controller.bayIsEmpty());
        assertTrue(controller.getBoxes().isEmpty());
        assertTrue(controller.getBoxesObservableList().isEmpty());
        Box box = new Box("BX10", "SKU10", 4, null, new Date(1,1,2025), new Time(8,0,0));
        boxRepo.add(box);
        boolean added = source.addBox(box.getBoxId(), RoleType.TERMINAL_OPERATOR);
        assertTrue(added);
        List<Box> boxes = controller.getBoxes();
        assertEquals(1, boxes.size());
        assertEquals("BX10", boxes.get(0).getBoxId());
        assertFalse(controller.bayIsEmpty());
        assertEquals(1, controller.getBoxesObservableList().size());
    }

    /**
     * Tests relocateBox method and log message builders for success and failure.
     */
    @Test
    void testRelocateBox_success_and_failure_messages() {
        Bay actual = new Bay("W1A1B1", 1, 5);
        Bay dest = new Bay("W1A1B2", 2, 1);
        bayRepo.add(actual);
        bayRepo.add(dest);
        Box box = new Box("BX20", "SKU20", 2, null, new Date(2,2,2025), new Time(9,0,0));
        boxRepo.add(box);
        actual.addBox(box.getBoxId(), RoleType.TERMINAL_OPERATOR);
        controller.setActualBay(actual);
        controller.setNewBay(dest);
        controller.setSelectedBox(box);
        String res = controller.relocateBox();
        assertEquals("Box relocated successfully", res);
        assertEquals(dest.getBayId(), boxRepo.findById("BX20").getBayId());
        controller.setSelectedBox(null);
        assertEquals("Box not found", controller.relocateBox());
        controller.setSelectedBox(box);
        controller.setActualBay(null);
        assertEquals("Actual bay not found", controller.relocateBox());
        controller.setActualBay(actual);
        controller.setNewBay(null);
        assertEquals("New bay not found", controller.relocateBox());
        controller.setActualBay(actual);
        controller.setNewBay(actual);
        controller.setSelectedBox(box);
        assertEquals("New bay is the same as the actual bay", controller.relocateBox());
        controller.setSelectedBox(box);
        controller.setActualBay(actual);
        controller.setNewBay(dest);
        String success = controller.getLogMessageRelocateBoxSuccess();
        assertTrue(success.contains("Box " + box.getBoxId()));
        String failure = controller.getLogMessageRelocateBoxFailure("reason");
        assertTrue(failure.contains("Failed to relocate Box " + box.getBoxId()));
    }
}