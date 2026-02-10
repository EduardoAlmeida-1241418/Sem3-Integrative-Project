package pt.ipp.isep.dei.controller.wareHousePlanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.domain.Log;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.data.repository.LogRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.HashSet;
import java.util.Set;

/**
 * Controller responsible for managing and filtering system logs
 * visible to the Warehouse Planner role in the WMS (Warehouse Management System).
 * <p>
 * Allows selection of multiple role filters to display logs associated
 * with specific system users.
 */
public class WarehousePlannerWmsController {

    private final LogRepository logRepository;

    private boolean globalSelected;
    private boolean pickerSelected;
    private boolean plannerSelected;
    private boolean qualityOperatorSelected;
    private boolean terminalOperatorSelected;
    private boolean trafficDispatcherSelected;
    private boolean warehousePlannerSelected;

    /**
     * Constructs the controller and initializes access to the {@link LogRepository}.
     */
    public WarehousePlannerWmsController() {
        this.logRepository = Repositories.getInstance().getLogRepository();
    }

    /**
     * Sets whether logs from global users are included.
     *
     * @param globalSelected true to include, false to exclude.
     */
    public void setGlobalSelected(boolean globalSelected) { this.globalSelected = globalSelected; }

    /**
     * Sets whether logs from pickers are included.
     *
     * @param pickerSelected true to include, false to exclude.
     */
    public void setPickerSelected(boolean pickerSelected) { this.pickerSelected = pickerSelected; }

    /**
     * Sets whether logs from planners are included.
     *
     * @param plannerSelected true to include, false to exclude.
     */
    public void setPlannerSelected(boolean plannerSelected) { this.plannerSelected = plannerSelected; }

    /**
     * Sets whether logs from quality operators are included.
     *
     * @param qualityOperatorSelected true to include, false to exclude.
     */
    public void setQualityOperatorSelected(boolean qualityOperatorSelected) { this.qualityOperatorSelected = qualityOperatorSelected; }

    /**
     * Sets whether logs from terminal operators are included.
     *
     * @param terminalOperatorSelected true to include, false to exclude.
     */
    public void setTerminalOperatorSelected(boolean terminalOperatorSelected) { this.terminalOperatorSelected = terminalOperatorSelected; }

    /**
     * Sets whether logs from traffic dispatchers are included.
     *
     * @param trafficDispatcherSelected true to include, false to exclude.
     */
    public void setTrafficDispatcherSelected(boolean trafficDispatcherSelected) { this.trafficDispatcherSelected = trafficDispatcherSelected; }

    /**
     * Sets whether logs from warehouse planners are included.
     *
     * @param warehousePlannerSelected true to include, false to exclude.
     */
    public void setWarehousePlannerSelected(boolean warehousePlannerSelected) { this.warehousePlannerSelected = warehousePlannerSelected; }

    /**
     * Selects all available role filters, showing logs from all user roles.
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
     * Clears all role filters, effectively hiding all logs until a role is reselected.
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
     * Retrieves all logs filtered by the selected user roles.
     *
     * @return an observable list of formatted log entries as strings.
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
     * Determines the role type associated with a given log entry string.
     *
     * @param logString the string representation of the log.
     * @return the corresponding {@link RoleType}, or null if not found.
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
