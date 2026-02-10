package controller.qualityOperator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.qualityOperator.QuarantineReturnController;
import pt.ipp.isep.dei.data.repository.*;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.sprint1.ReturnRepository;
import pt.ipp.isep.dei.data.repository.sprint1.WarehouseRepository;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.ReturnReason;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for QuarantineReturnController.
 */
public class QuarantineReturnControllerTest {

    private QuarantineReturnController controller;
    private ReturnRepository returnRepo;
    private BoxRepository boxRepo;
    private BayRepository bayRepo;
    private WarehouseRepository whRepo;
    private LogRepository logRepo;

    @BeforeEach
    void setUp() {
        Repositories repos = Repositories.getInstance();
        returnRepo = repos.getReturnRepository();
        boxRepo = repos.getBoxRepository();
        bayRepo = repos.getBayRepository();
        whRepo = repos.getWarehouseRepository();
        logRepo = repos.getLogRepository();
        returnRepo.clear();
        boxRepo.clear();
        bayRepo.clear();
        whRepo.clear();
        logRepo.clear();

        controller = new QuarantineReturnController();
    }

    @Test
    void testListQuarantineAndGetReturnQuantity() {
        Return r = new Return("R001", "SKU1", 7, ReturnReason.CUSTOMER_REMORSE, new Date(1,1,2025), new Time(10,0,0), null);
        returnRepo.add(r);

        List<Return> list = controller.listQuarantine();
        assertNotNull(list);
        assertTrue(list.stream().anyMatch(x -> x.getReturnId().equals("R001")));

        int qty = controller.getReturnQuantity("R001");
        assertEquals(7, qty);
    }

    @Test
    void testInspectAuto_customerRemorse_whenRestockFails_returnsDiscardedFailedRestockAndRemovesReturn() {
        Return r = new Return("R002", "SKU2", 5, ReturnReason.CUSTOMER_REMORSE, new Date(2,2,2025), new Time(11,0,0), null);
        returnRepo.add(r);

        String result = controller.inspectAuto("R002");
        assertEquals("DISCARDED_FAILED_RESTOCK", result);

        assertThrows(java.util.NoSuchElementException.class, () -> returnRepo.findById("R002"));

        List<String> audits = controller.listAuditLines();
        assertFalse(audits.isEmpty());
    }

    @Test
    void testInspectAuto_damaged_discardsAndRemovesReturn() {
        Return r = new Return("R003", "SKU3", 3, ReturnReason.DAMAGED, new Date(3,3,2025), new Time(12,0,0), null);
        returnRepo.add(r);

        String result = controller.inspectAuto("R003");
        assertEquals("DISCARDED", result);

        assertThrows(java.util.NoSuchElementException.class, () -> returnRepo.findById("R003"));

        List<String> audits = controller.listAuditLines();
        assertTrue(audits.stream().anyMatch(msg -> msg.contains("R003") || msg.contains("SKU3")));
    }

    @Test
    void testInspectAuto_cycleCount_needsDecision_andDoesNotRemoveReturn() {
        Return r = new Return("R004", "SKU4", 1, ReturnReason.CYCLE_COUNT, new Date(4,4,2025), new Time(13,0,0), null);
        returnRepo.add(r);

        String result = controller.inspectAuto("R004");
        assertEquals("NEEDS_DECISION", result);

        Return fetched = returnRepo.findById("R004");
        assertNotNull(fetched);
        assertEquals(ReturnReason.CYCLE_COUNT, fetched.getReturnReason());
    }

    @Test
    void testRestock_whenNoWarehouseAvailable_returnsFalseAndRemovesReturnAndLogs() {
        Return r = new Return("R005", "SKU5", 6, ReturnReason.CUSTOMER_REMORSE, new Date(5,5,2025), new Time(14,0,0), null);
        returnRepo.add(r);

        boolean success = controller.restock("R005", 6);
        assertFalse(success);

        assertThrows(java.util.NoSuchElementException.class, () -> returnRepo.findById("R005"));

        List<String> audits = controller.listAuditLines();
        assertFalse(audits.isEmpty());
    }

    @Test
    void testDiscard_removesReturn_andLogsInspection() {
        Return r = new Return("R006", "SKU6", 2, ReturnReason.DAMAGED, new Date(6,6,2025), new Time(15,0,0), null);
        returnRepo.add(r);

        controller.discard("R006");

        assertThrows(java.util.NoSuchElementException.class, () -> returnRepo.findById("R006"));

        List<String> audits = controller.listAuditLines();
        assertTrue(audits.stream().anyMatch(m -> m.contains("R006") || m.contains("SKU6")));
    }
}