package controller.terminalOperator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.terminalOperator.TerminalOperatorWmsController;
import pt.ipp.isep.dei.domain.Log;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.data.repository.LogRepository;
import pt.ipp.isep.dei.data.repository.Repositories;
import javafx.collections.ObservableList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TerminalOperatorWmsController.
 */
public class TerminalOperatorWmsControllerTest {

    private TerminalOperatorWmsController controller;
    private LogRepository logRepository;
    private Log logGlobal;
    private Log logTerminal;

    /**
     * Initializes the controller and log repository before each test.
     * Adds sample logs for testing filtering and role type detection.
     */
    @BeforeEach
    void setUp() {
        controller = new TerminalOperatorWmsController();
        logRepository = Repositories.getInstance().getLogRepository();
        logRepository.clear();

        logGlobal = new Log("System up", LogType.INFO, RoleType.GLOBAL);
        logTerminal = new Log("Unloaded box", LogType.INFO, RoleType.TERMINAL_OPERATOR);

        logRepository.add(logGlobal);
        logRepository.add(logTerminal);
    }

    /**
     * Tests selecting all roles, clearing all roles, and filtering logs by role selection.
     * Verifies that logs are correctly filtered according to selected roles.
     */
    @Test
    void testSelectClearAndFilteredLogs() {
        controller.selectAllRoles();
        ObservableList<String> logs = controller.getFilteredLogs();
        assertNotNull(logs);
        assertTrue(logs.contains(logGlobal.toString()));
        assertTrue(logs.contains(logTerminal.toString()));

        controller.clearAllRoles();
        logs = controller.getFilteredLogs();
        assertEquals(0, logs.size());

        controller.setTerminalOperatorSelected(true);
        logs = controller.getFilteredLogs();
        assertEquals(1, logs.size());
        assertEquals(logTerminal.toString(), logs.get(0));
    }

    /**
     * Tests retrieving the RoleType for a given log string.
     * Verifies correct role type is returned for known logs and null for unknown logs.
     */
    @Test
    void testGetRoleTypeForLog() {
        RoleType r1 = controller.getRoleTypeForLog(logGlobal.toString());
        assertEquals(RoleType.GLOBAL, r1);

        RoleType r2 = controller.getRoleTypeForLog(logTerminal.toString());
        assertEquals(RoleType.TERMINAL_OPERATOR, r2);

        assertNull(controller.getRoleTypeForLog("non existing"));
    }

    /**
     * Tests all role selection setter methods to ensure they set the corresponding flags without error.
     */
    @Test
    void testRoleSelectionSetters() {
        controller.setGlobalSelected(true);
        controller.setPickerSelected(true);
        controller.setPlannerSelected(true);
        controller.setQualityOperatorSelected(true);
        controller.setTerminalOperatorSelected(true);
        controller.setTrafficDispatcherSelected(true);
        controller.setWarehousePlannerSelected(true);
    }
}