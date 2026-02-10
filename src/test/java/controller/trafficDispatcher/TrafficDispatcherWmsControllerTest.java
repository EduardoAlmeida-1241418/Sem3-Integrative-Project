package controller.trafficDispatcher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.trafficDispatcher.TrafficDispatcherWmsController;
import pt.ipp.isep.dei.domain.Log;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.data.repository.LogRepository;
import pt.ipp.isep.dei.data.repository.Repositories;
import javafx.collections.ObservableList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TrafficDispatcherWmsController.
 * Tests selection/clearing of roles, filtered log retrieval and role lookup by log string.
 */
public class TrafficDispatcherWmsControllerTest {

    private TrafficDispatcherWmsController controller;
    private LogRepository logRepository;
    private Log logGlobal;
    private Log logPicker;
    private Log logTraffic;

    @BeforeEach
    void setUp() {
        controller = new TrafficDispatcherWmsController();
        logRepository = Repositories.getInstance().getLogRepository();
        logRepository.clear();

        logGlobal = new Log("System started", LogType.INFO, RoleType.GLOBAL);
        logPicker = new Log("Picked item", LogType.INFO, RoleType.PICKER);
        logTraffic = new Log("Train dispatched", LogType.INFO, RoleType.TRAFFIC_DISPATCHER);

        logRepository.add(logGlobal);
        logRepository.add(logPicker);
        logRepository.add(logTraffic);
    }

    @Test
    void testSetters_doNotThrow() {
        controller.setGlobalSelected(true);
        controller.setPickerSelected(true);
        controller.setPlannerSelected(true);
        controller.setQualityOperatorSelected(true);
        controller.setTerminalOperatorSelected(true);
        controller.setTrafficDispatcherSelected(true);
        controller.setWarehousePlannerSelected(true);
        // absence of exception is success
    }

    @Test
    void testSelectAllRolesAndGetFilteredLogs_containsAll() {
        controller.selectAllRoles();
        ObservableList<String> logs = controller.getFilteredLogs();
        assertNotNull(logs);
        assertTrue(logs.contains(logGlobal.toString()));
        assertTrue(logs.contains(logPicker.toString()));
        assertTrue(logs.contains(logTraffic.toString()));
    }

    @Test
    void testClearAllRoles_returnsEmptyList() {
        controller.selectAllRoles();
        controller.clearAllRoles();
        ObservableList<String> logs = controller.getFilteredLogs();
        assertNotNull(logs);
        assertEquals(0, logs.size());
    }

    @Test
    void testGetFilteredLogs_specificRole_returnsOnlyThose() {
        controller.clearAllRoles();
        controller.setPickerSelected(true);
        ObservableList<String> logs = controller.getFilteredLogs();
        assertNotNull(logs);
        assertEquals(1, logs.size());
        assertEquals(logPicker.toString(), logs.get(0));

        controller.clearAllRoles();
        controller.setTrafficDispatcherSelected(true);
        logs = controller.getFilteredLogs();
        assertEquals(1, logs.size());
        assertEquals(logTraffic.toString(), logs.get(0));
    }

    @Test
    void testGetRoleTypeForLog_returnsCorrectRoleOrNull() {
        RoleType r1 = controller.getRoleTypeForLog(logGlobal.toString());
        assertEquals(RoleType.GLOBAL, r1);

        RoleType r2 = controller.getRoleTypeForLog(logPicker.toString());
        assertEquals(RoleType.PICKER, r2);

        RoleType r3 = controller.getRoleTypeForLog("Unknown log text");
        assertNull(r3);
    }
}