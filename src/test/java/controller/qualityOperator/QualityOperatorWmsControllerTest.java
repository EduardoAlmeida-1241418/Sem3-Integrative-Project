package controller.qualityOperator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.qualityOperator.QualityOperatorWmsController;
import pt.ipp.isep.dei.domain.Log;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.data.repository.LogRepository;
import pt.ipp.isep.dei.data.repository.Repositories;
import javafx.collections.ObservableList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for QualityOperatorWmsController.
 */
public class QualityOperatorWmsControllerTest {

    private QualityOperatorWmsController controller;
    private LogRepository logRepository;
    private Log logGlobal;
    private Log logQuality;

    @BeforeEach
    void setUp() {
        controller = new QualityOperatorWmsController();
        logRepository = Repositories.getInstance().getLogRepository();
        logRepository.clear();

        logGlobal = new Log("System started", LogType.INFO, RoleType.GLOBAL);
        logQuality = new Log("Quality check performed", LogType.PICKING_PLAN, RoleType.QUALITY_OPERATOR);

        logRepository.add(logGlobal);
        logRepository.add(logQuality);
    }

    /**
     * Sets all role selection flags to true.
     * This enables filtering logs for all roles.
     */
    @Test
    void testSetters_noExceptions() {
        controller.setGlobalSelected(true);
        controller.setPickerSelected(true);
        controller.setPlannerSelected(true);
        controller.setQualityOperatorSelected(true);
        controller.setTerminalOperatorSelected(true);
        controller.setTrafficDispatcherSelected(true);
        controller.setWarehousePlannerSelected(true);
        // nothing to assert, presence of no exception indicates setters work
    }

    /**
     * Tests selecting all roles and retrieving filtered logs.
     * Verifies that logs for all roles are present in the result.
     */
    @Test
    void testSelectAllRolesAndGetFilteredLogs() {
        controller.selectAllRoles();
        ObservableList<String> logs = controller.getFilteredLogs();
        assertNotNull(logs);
        assertTrue(logs.contains(logGlobal.toString()), "Global log should be present when all roles selected");
        assertTrue(logs.contains(logQuality.toString()), "QualityOperator log should be present when all roles selected");
    }

    /**
     * Tests clearing all role selections and retrieving filtered logs.
     * Verifies that no logs are returned when all roles are cleared.
     */
    @Test
    void testClearAllRolesAndGetFilteredLogs() {
        controller.selectAllRoles();
        controller.clearAllRoles();
        ObservableList<String> logs = controller.getFilteredLogs();
        assertNotNull(logs);
        assertEquals(0, logs.size(), "No logs should be returned when all roles cleared");
    }

    /**
     * Tests filtering logs for a specific role (QUALITY_OPERATOR).
     * Verifies that only the log for the selected role is returned.
     */
    @Test
    void testGetFilteredLogsForSpecificRole() {
        controller.clearAllRoles();
        controller.setQualityOperatorSelected(true);
        ObservableList<String> logs = controller.getFilteredLogs();
        assertNotNull(logs);
        assertEquals(1, logs.size(), "Only the quality operator log should be returned when only QUALITY_OPERATOR is selected");
        assertEquals(logQuality.toString(), logs.get(0));
    }

    /**
     * Tests retrieving the RoleType for a given log string.
     * Verifies correct role type is returned for known logs and null for unknown logs.
     */
    @Test
    void testGetRoleTypeForLog() {
        RoleType r1 = controller.getRoleTypeForLog(logGlobal.toString());
        assertEquals(RoleType.GLOBAL, r1);

        RoleType r2 = controller.getRoleTypeForLog(logQuality.toString());
        assertEquals(RoleType.QUALITY_OPERATOR, r2);

        RoleType r3 = controller.getRoleTypeForLog("Non existing log");
        assertNull(r3);
    }
}