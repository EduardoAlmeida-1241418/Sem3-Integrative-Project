package pt.ipp.isep.dei.controller.trafficDispatcher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.domain.Log;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.data.repository.LogRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.HashSet;
import java.util.Set;

/**
 * Controller responsible for managing the Warehouse Management System (WMS)
 * logs accessible to the Traffic Dispatcher.
 * <p>
 * Provides functionality to filter logs based on selected role types.
 */
public class TrafficDispatcherWmsController {

    private final LogRepository logRepository;

    private boolean globalSelected;
    private boolean pickerSelected;
    private boolean plannerSelected;
    private boolean qualityOperatorSelected;
    private boolean terminalOperatorSelected;
    private boolean trafficDispatcherSelected;
    private boolean warehousePlannerSelected;

    /**
     * Initializes the controller and retrieves the LogRepository instance.
     */
    public TrafficDispatcherWmsController() {
        this.logRepository = Repositories.getInstance().getLogRepository();
    }

    /** @param globalSelected enables or disables filtering by the Global role. */
    public void setGlobalSelected(boolean globalSelected) { this.globalSelected = globalSelected; }

    /** @param pickerSelected enables or disables filtering by the Picker role. */
    public void setPickerSelected(boolean pickerSelected) { this.pickerSelected = pickerSelected; }

    /** @param plannerSelected enables or disables filtering by the Planner role. */
    public void setPlannerSelected(boolean plannerSelected) { this.plannerSelected = plannerSelected; }

    /** @param qualityOperatorSelected enables or disables filtering by the Quality Operator role. */
    public void setQualityOperatorSelected(boolean qualityOperatorSelected) { this.qualityOperatorSelected = qualityOperatorSelected; }

    /** @param terminalOperatorSelected enables or disables filtering by the Terminal Operator role. */
    public void setTerminalOperatorSelected(boolean terminalOperatorSelected) { this.terminalOperatorSelected = terminalOperatorSelected; }

    /** @param trafficDispatcherSelected enables or disables filtering by the Traffic Dispatcher role. */
    public void setTrafficDispatcherSelected(boolean trafficDispatcherSelected) { this.trafficDispatcherSelected = trafficDispatcherSelected; }

    /** @param warehousePlannerSelected enables or disables filtering by the Warehouse Planner role. */
    public void setWarehousePlannerSelected(boolean warehousePlannerSelected) { this.warehousePlannerSelected = warehousePlannerSelected; }

    /**
     * Selects all available roles, allowing logs from every user role to be shown.
     */
    public void selectAllRoles() {
        globalSelected = true;
        pickerSelected = true;
        plannerSelected = true;
        qualityOperatorSelected = true;
        terminalOperatorSelected = true;
        trafficDispatcherSelected = true;
        warehousePlannerSelected = true;
    }

    /**
     * Clears all role selections, resulting in no logs being displayed.
     */
    public void clearAllRoles() {
        globalSelected = false;
        pickerSelected = false;
        plannerSelected = false;
        qualityOperatorSelected = false;
        terminalOperatorSelected = false;
        trafficDispatcherSelected = false;
        warehousePlannerSelected = false;
    }

    /**
     * Retrieves logs filtered by the selected role types.
     *
     * @return an observable list containing log entries that match the selected roles.
     */
    public ObservableList<String> getFilteredLogs() {
        ObservableList<String> filteredLogs = FXCollections.observableArrayList();
        Set<RoleType> selectedRoles = new HashSet<>();

        if (globalSelected) selectedRoles.add(RoleType.GLOBAL);
        if (pickerSelected) selectedRoles.add(RoleType.PICKER);
        if (plannerSelected) selectedRoles.add(RoleType.PLANNER1);
        if (qualityOperatorSelected) selectedRoles.add(RoleType.QUALITY_OPERATOR);
        if (terminalOperatorSelected) selectedRoles.add(RoleType.TERMINAL_OPERATOR);
        if (trafficDispatcherSelected) selectedRoles.add(RoleType.TRAFFIC_DISPATCHER);
        if (warehousePlannerSelected) selectedRoles.add(RoleType.WAREHOUSE_PLANNER);

        for (Log log : logRepository.findAll()) {
            if (selectedRoles.contains(log.getRoleType())) {
                filteredLogs.add(log.toString());
            }
        }
        return filteredLogs;
    }

    /**
     * Retrieves the role type associated with a specific log entry.
     *
     * @param logString the string representation of the log.
     * @return the {@link RoleType} of the log, or null if not found.
     */
    public RoleType getRoleTypeForLog(String logString) {
        for (Log log : logRepository.findAll()) {
            if (log.toString().equals(logString)) {
                return log.getRoleType();
            }
        }
        return null;
    }
}
