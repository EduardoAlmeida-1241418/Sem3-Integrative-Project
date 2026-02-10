package pt.ipp.isep.dei.controller.qualityOperator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.domain.Log;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.data.repository.LogRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.HashSet;
import java.util.Set;

/**
 * Controller responsible for managing log filtering and visualization
 * in the WMS interface for the Quality Operator role.
 * <p>
 * Provides functionality to select or clear role-based filters,
 * retrieve logs corresponding to specific roles, and determine the
 * role associated with each log entry.
 */
public class QualityOperatorWmsController {

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
    public QualityOperatorWmsController() {
        this.logRepository = Repositories.getInstance().getLogRepository();
    }

    /** Sets whether the GLOBAL role is selected. */
    public void setGlobalSelected(boolean globalSelected) { this.globalSelected = globalSelected; }

    /** Sets whether the PICKER role is selected. */
    public void setPickerSelected(boolean pickerSelected) { this.pickerSelected = pickerSelected; }

    /** Sets whether the PLANNER role is selected. */
    public void setPlannerSelected(boolean plannerSelected) { this.plannerSelected = plannerSelected; }

    /** Sets whether the QUALITY_OPERATOR role is selected. */
    public void setQualityOperatorSelected(boolean qualityOperatorSelected) { this.qualityOperatorSelected = qualityOperatorSelected; }

    /** Sets whether the TERMINAL_OPERATOR role is selected. */
    public void setTerminalOperatorSelected(boolean terminalOperatorSelected) { this.terminalOperatorSelected = terminalOperatorSelected; }

    /** Sets whether the TRAFFIC_DISPATCHER role is selected. */
    public void setTrafficDispatcherSelected(boolean trafficDispatcherSelected) { this.trafficDispatcherSelected = trafficDispatcherSelected; }

    /** Sets whether the WAREHOUSE_PLANNER role is selected. */
    public void setWarehousePlannerSelected(boolean warehousePlannerSelected) { this.warehousePlannerSelected = warehousePlannerSelected; }

    /**
     * Marks all available roles as selected.
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
     * Clears all selected roles.
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
     * Retrieves a filtered list of logs based on the selected roles.
     *
     * @return an observable list containing string representations of logs
     *         filtered by the selected roles.
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
     * @param logString the string representation of the log entry.
     * @return the {@link RoleType} of the corresponding log, or null if no match is found.
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
