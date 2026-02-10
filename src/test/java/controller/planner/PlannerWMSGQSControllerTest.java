package controller.planner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.planner.PlannerWMS_GQSController;
import pt.ipp.isep.dei.domain.Log;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.data.repository.LogRepository;
import pt.ipp.isep.dei.data.repository.Repositories;
import javafx.collections.ObservableList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PlannerWmsController.
 */
public class PlannerWMSGQSControllerTest {

    private PlannerWMS_GQSController controller;
    private LogRepository logRepository;
    private Log logGlobal;
    private Log logPlanner;

    /**
     * Sets up the test environment before each test.
     * Clears the log repository and adds sample logs for global and planner roles.
     */
    @BeforeEach
    void setUp() {
        controller = new PlannerWMS_GQSController();
        logRepository = Repositories.getInstance().getLogRepository();
        logRepository.clear();

        logGlobal = new Log("System started", LogType.INFO, RoleType.GLOBAL);
        logPlanner = new Log("Plan created", LogType.PICKING_PLAN, RoleType.PLANNER1);

        logRepository.add(logGlobal);
        logRepository.add(logPlanner);
    }

    /**
     * Tests all setter methods for role selection in the controller.
     * Ensures that each role can be selected without error.
     */
    @Test
    void testSetters() {
        controller.setGlobalSelected(true);
        controller.setPickerSelected(true);
        controller.setPlanner1Selected(true);
        controller.setQualityOperatorSelected(true);
        controller.setTerminalOperatorSelected(true);
        controller.setTrafficDispatcherSelected(true);
        controller.setWarehousePlannerSelected(true);
    }

    /**
     * Tests selecting all roles and retrieving filtered logs.
     * Verifies that logs for both global and planner roles are present when all roles are selected.
     */
    @Test
    void testSelectAllRolesAndGetFilteredLogs() {
        controller.selectAllRoles();
        ObservableList<String> logs = controller.getFilteredLogs();
        assertNotNull(logs);

        assertTrue(logs.contains(logGlobal.toString()), "Global log should be present when all roles selected");
        assertTrue(logs.contains(logPlanner.toString()), "Planner log should be present when all roles selected");
    }

    /**
     * Tests clearing all roles and retrieving filtered logs.
     * Ensures that no logs are returned when all roles are cleared.
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
     * Tests retrieving filtered logs when only the planner role is selected.
     * Verifies that only the planner log is returned.
     */
    @Test
    void testGetFilteredLogsForSpecificRole() {
        controller.clearAllRoles();
        controller.setPlanner1Selected(true);
        ObservableList<String> logs = controller.getFilteredLogs();
        assertNotNull(logs);
        assertEquals(1, logs.size(), "Only the planner log should be returned when only Planner is selected");
        assertEquals(logPlanner.toString(), logs.get(0));
    }

    /**
     * Tests retrieving the role type for a given log string.
     * Verifies correct role type for known logs and null for unknown logs.
     */
    @Test
    void testGetRoleTypeForLog() {
        String logStr1 = logGlobal.toString();
        RoleType role1 = controller.getRoleTypeForLog(logStr1);
        assertEquals(RoleType.GLOBAL, role1);

        String logStr2 = logPlanner.toString();
        RoleType role2 = controller.getRoleTypeForLog(logStr2);
        assertEquals(RoleType.PLANNER1, role2);

        String unknown = "This log does not exist";
        RoleType role3 = controller.getRoleTypeForLog(unknown);
        assertNull(role3);
    }
}