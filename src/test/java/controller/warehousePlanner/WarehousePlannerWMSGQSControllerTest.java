package controller.warehousePlanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.wareHousePlanner.WarehousePlannerWmsController;
import pt.ipp.isep.dei.domain.Log;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.data.repository.LogRepository;
import pt.ipp.isep.dei.data.repository.Repositories;
import javafx.collections.ObservableList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for WarehousePlannerWmsController.
 */
public class WarehousePlannerWMSGQSControllerTest {

    private WarehousePlannerWmsController controller;
    private LogRepository logRepository;
    private Log logGlobal;
    private Log logPicker;
    private Log logWarehousePlanner;

    /**
     * Sets up the test environment by initializing the controller and log repository, and adding sample logs.
     */
    @BeforeEach
    void setUp() {
        controller = new WarehousePlannerWmsController();
        logRepository = Repositories.getInstance().getLogRepository();
        logRepository.clear();

        logGlobal = new Log("System started", LogType.INFO, RoleType.GLOBAL);
        logPicker = new Log("Picked item", LogType.INFO, RoleType.PICKER);
        logWarehousePlanner = new Log("Warehouse plan", LogType.INFO, RoleType.WAREHOUSE_PLANNER);

        logRepository.add(logGlobal);
        logRepository.add(logPicker);
        logRepository.add(logWarehousePlanner);
    }

    /**
     * Tests selecting all roles and verifies that all relevant logs are returned.
     */
    @Test
    void testSelectAllRolesAndGetFilteredLogs() {
        controller.selectAllRoles();
        ObservableList<String> logs = controller.getFilteredLogs();
        assertNotNull(logs);
        assertTrue(logs.contains(logGlobal.toString()));
        assertTrue(logs.contains(logPicker.toString()));
        assertTrue(logs.contains(logWarehousePlanner.toString()));
    }

    /**
     * Tests clearing all roles and verifies that no logs are returned.
     */
    @Test
    void testClearAllRolesAndGetFilteredLogs() {
        controller.selectAllRoles();
        controller.clearAllRoles();
        ObservableList<String> logs = controller.getFilteredLogs();
        assertNotNull(logs);
        assertEquals(0, logs.size());
    }

    /**
     * Tests selecting only the warehouse planner role and verifies that only its log is returned.
     */
    @Test
    void testGetFilteredLogsSpecificRole() {
        controller.clearAllRoles();
        controller.setWarehousePlannerSelected(true);
        ObservableList<String> logs = controller.getFilteredLogs();
        assertNotNull(logs);
        assertEquals(1, logs.size());
        assertEquals(logWarehousePlanner.toString(), logs.get(0));
    }

    /**
     * Tests the getRoleTypeForLog method for known and unknown log strings.
     */
    @Test
    void testGetRoleTypeForLog() {
        RoleType r1 = controller.getRoleTypeForLog(logGlobal.toString());
        assertEquals(RoleType.GLOBAL, r1);

        RoleType r2 = controller.getRoleTypeForLog(logWarehousePlanner.toString());
        assertEquals(RoleType.WAREHOUSE_PLANNER, r2);

        assertNull(controller.getRoleTypeForLog("no such log"));
    }

    /**
     * Tests the constructor to ensure the controller is instantiated.
     */
    @Test
    void testConstructor() {
        WarehousePlannerWmsController ctrl = new WarehousePlannerWmsController();
        assertNotNull(ctrl);
    }

    /**
     * Tests setGlobalSelected and verifies its effect on getFilteredLogs.
     */
    @Test
    void testSetGlobalSelected() {
        controller.clearAllRoles();
        controller.setGlobalSelected(true);
        ObservableList<String> logs = controller.getFilteredLogs();
        assertTrue(logs.contains(logGlobal.toString()));
    }

    /**
     * Tests setPickerSelected and verifies its effect on getFilteredLogs.
     */
    @Test
    void testSetPickerSelected() {
        controller.clearAllRoles();
        controller.setPickerSelected(true);
        ObservableList<String> logs = controller.getFilteredLogs();
        assertTrue(logs.contains(logPicker.toString()));
    }

    /**
     * Tests setPlannerSelected and verifies no logs are returned when only planner is selected.
     */
    @Test
    void testSetPlannerSelected() {
        controller.clearAllRoles();
        controller.setPlannerSelected(true);
        ObservableList<String> logs = controller.getFilteredLogs();
        assertTrue(logs.isEmpty());
    }

    /**
     * Tests setQualityOperatorSelected and verifies no logs are returned when only quality operator is selected.
     */
    @Test
    void testSetQualityOperatorSelected() {
        controller.clearAllRoles();
        controller.setQualityOperatorSelected(true);
        ObservableList<String> logs = controller.getFilteredLogs();
        assertTrue(logs.isEmpty());
    }

    /**
     * Tests setTerminalOperatorSelected and verifies no logs are returned when only terminal operator is selected.
     */
    @Test
    void testSetTerminalOperatorSelected() {
        controller.clearAllRoles();
        controller.setTerminalOperatorSelected(true);
        ObservableList<String> logs = controller.getFilteredLogs();
        assertTrue(logs.isEmpty());
    }

    /**
     * Tests setTrafficDispatcherSelected and verifies no logs are returned when only traffic dispatcher is selected.
     */
    @Test
    void testSetTrafficDispatcherSelected() {
        controller.clearAllRoles();
        controller.setTrafficDispatcherSelected(true);
        ObservableList<String> logs = controller.getFilteredLogs();
        assertTrue(logs.isEmpty());
    }
}