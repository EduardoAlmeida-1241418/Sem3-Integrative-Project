package pt.ipp.isep.dei.controller.picker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.domain.Log;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.data.repository.LogRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.HashSet;
import java.util.Set;

/**
 * Controller responsible for managing log visibility and filtering for the Picker WMS interface.
 * <p>
 * Allows the user to select roles whose logs should be displayed, filter the log list,
 * and retrieve role information for a specific log entry.
 */
public class PickerWmsController {

    /** Repository containing system logs. */
    private final LogRepository logRepository;

    private boolean globalSelected;
    private boolean pickerSelected;
    private boolean plannerSelected;
    private boolean qualityOperatorSelected;
    private boolean terminalOperatorSelected;
    private boolean trafficDispatcherSelected;
    private boolean warehousePlannerSelected;

    /**
     * Default constructor that initializes the log repository.
     */
    public PickerWmsController() {
        this.logRepository = Repositories.getInstance().getLogRepository();
    }

    /** Sets the global log filter state. */
    public void setGlobalSelected(boolean globalSelected) { this.globalSelected = globalSelected; }

    /** Sets the picker log filter state. */
    public void setPickerSelected(boolean pickerSelected) { this.pickerSelected = pickerSelected; }

    /** Sets the planner log filter state. */
    public void setPlannerSelected(boolean plannerSelected) { this.plannerSelected = plannerSelected; }

    /** Sets the quality operator log filter state. */
    public void setQualityOperatorSelected(boolean qualityOperatorSelected) { this.qualityOperatorSelected = qualityOperatorSelected; }

    /** Sets the terminal operator log filter state. */
    public void setTerminalOperatorSelected(boolean terminalOperatorSelected) { this.terminalOperatorSelected = terminalOperatorSelected; }

    /** Sets the traffic dispatcher log filter state. */
    public void setTrafficDispatcherSelected(boolean trafficDispatcherSelected) { this.trafficDispatcherSelected = trafficDispatcherSelected; }

    /** Sets the warehouse planner log filter state. */
    public void setWarehousePlannerSelected(boolean warehousePlannerSelected) { this.warehousePlannerSelected = warehousePlannerSelected; }

    /**
     * Selects all role types for log filtering.
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
     * Clears all role selections, removing all filters.
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
     * Retrieves logs filtered by the currently selected roles.
     *
     * @return an observable list of log strings that match selected role filters
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
     * Finds the role type associated with a specific log string.
     *
     * @param logString the log string to look up
     * @return the {@link RoleType} of the matching log, or {@code null} if not found
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
