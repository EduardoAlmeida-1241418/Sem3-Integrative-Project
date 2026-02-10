package controller.picker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.picker.PickerWmsController;
import pt.ipp.isep.dei.domain.Log;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.data.repository.LogRepository;
import pt.ipp.isep.dei.data.repository.Repositories;
import javafx.collections.ObservableList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Enhanced unit tests for PickerWmsController.
 */
public class PickerWmsControllerTest {
    private PickerWmsController controller;
    private LogRepository logRepository;
    private Log logGlobal;
    private Log logPicker;

    @BeforeEach
    void setUp() {
        controller = new PickerWmsController();
        logRepository = Repositories.getInstance().getLogRepository();
        logRepository.clear();
        logGlobal = new Log("System started", LogType.INFO, RoleType.GLOBAL);
        logPicker = new Log("Item picked", LogType.PICKING_PLAN, RoleType.PICKER);
        logRepository.add(logGlobal);
        logRepository.add(logPicker);
    }

    /**
     * Tests all setter methods for role selection flags and verifies that selecting all roles displays all logs.
     * Asserts that the filtered logs list contains both sample logs.
     */
    @Test
    void testSetters() {
        controller.setGlobalSelected(true);
        controller.setPickerSelected(true);
        controller.setPlannerSelected(true);
        controller.setQualityOperatorSelected(true);
        controller.setTerminalOperatorSelected(true);
        controller.setTrafficDispatcherSelected(true);
        controller.setWarehousePlannerSelected(true);
        // No exception means setters work; optionally assert that selecting all shows logs:
        controller.selectAllRoles();
        ObservableList<String> logs = controller.getFilteredLogs();
        assertEquals(2, logs.size());
    }

    /**
     * Tests that selecting all roles returns all logs in the filtered list.
     * Asserts that both sample logs are present in the result.
     */
    @Test
    void testSelectAllRolesAndGetFilteredLogs() {
        controller.selectAllRoles();
        ObservableList<String> logs = controller.getFilteredLogs();
        assertEquals(2, logs.size());
        assertTrue(logs.contains(logGlobal.toString()));
        assertTrue(logs.contains(logPicker.toString()));
    }

    /**
     * Tests that clearing all roles results in an empty filtered logs list.
     * Asserts that no logs are returned after clearing all role selections.
     */
    @Test
    void testClearAllRolesAndGetFilteredLogs() {
        controller.selectAllRoles();
        controller.clearAllRoles();
        ObservableList<String> logs = controller.getFilteredLogs();
        assertEquals(0, logs.size());
    }

    /**
     * Tests that filtering logs by a specific role returns only the relevant log.
     * Asserts that only the global log is present when only the global role is selected.
     */
    @Test
    void testGetFilteredLogsForSpecificRole() {
        controller.clearAllRoles();
        controller.setGlobalSelected(true);
        ObservableList<String> logs = controller.getFilteredLogs();
        assertEquals(1, logs.size());
        assertTrue(logs.contains(logGlobal.toString()));
    }

    /**
     * Tests that getRoleTypeForLog returns the correct role type for each log string.
     * Asserts that the correct role is returned for known logs and null for unknown logs.
     */
    @Test
    void testGetRoleTypeForLog() {
        String logStr = logGlobal.toString();
        RoleType role = controller.getRoleTypeForLog(logStr);
        assertEquals(RoleType.GLOBAL, role);

        String logStr2 = logPicker.toString();
        RoleType role2 = controller.getRoleTypeForLog(logStr2);
        assertEquals(RoleType.PICKER, role2);

        String unknownLog = "Unknown log entry";
        RoleType role3 = controller.getRoleTypeForLog(unknownLog);
        assertNull(role3);
    }
}