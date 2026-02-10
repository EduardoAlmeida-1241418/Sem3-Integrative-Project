package pt.ipp.isep.dei.controller.planner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.domain.Log;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.data.repository.LogRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.HashSet;
import java.util.Set;

/**
 * Controller responsible for managing the Warehouse Management System (WMS) log
 * filtering and viewing operations for the Planner role.
 * <p>
 * Allows filtering logs by user roles, selecting/deselecting roles, and retrieving
 * log entries based on selected filters.
 */
public class PlannerWMS_GQSController {

    private final LogRepository logRepository;

    private boolean globalSelected;
    private boolean pickerSelected;
    private boolean planner1Selected;
    private boolean qualityOperatorSelected;
    private boolean terminalOperatorSelected;
    private boolean trafficDispatcherSelected;
    private boolean warehousePlannerSelected;

    private boolean analystSelected;
    private boolean dataEngineerSelected;
    private boolean freightManagerSelected;
    private boolean operationsPlannerSelected;
    private boolean planner2Selected;

    /**
     * Initializes the controller and obtains a reference to the LogRepository.
     */
    public PlannerWMS_GQSController() {
        this.logRepository = Repositories.getInstance().getLogRepository();
    }

    /** Sets whether the GLOBAL role is selected. */
    public void setGlobalSelected(boolean globalSelected) { this.globalSelected = globalSelected; }

    /** Sets whether the PICKER role is selected. */
    public void setPickerSelected(boolean pickerSelected) { this.pickerSelected = pickerSelected; }

    /** Sets whether the PLANNER role is selected. */
    public void setPlanner1Selected(boolean planner1Selected) { this.planner1Selected = planner1Selected; }

    /** Sets whether the QUALITY_OPERATOR role is selected. */
    public void setQualityOperatorSelected(boolean qualityOperatorSelected) { this.qualityOperatorSelected = qualityOperatorSelected; }

    /** Sets whether the TERMINAL_OPERATOR role is selected. */
    public void setTerminalOperatorSelected(boolean terminalOperatorSelected) { this.terminalOperatorSelected = terminalOperatorSelected; }

    /** Sets whether the TRAFFIC_DISPATCHER role is selected. */
    public void setTrafficDispatcherSelected(boolean trafficDispatcherSelected) { this.trafficDispatcherSelected = trafficDispatcherSelected; }

    /** Sets whether the WAREHOUSE_PLANNER role is selected. */
    public void setWarehousePlannerSelected(boolean warehousePlannerSelected) { this.warehousePlannerSelected = warehousePlannerSelected; }


    public void setAnalystSelected(boolean analystSelected) {
        this.analystSelected = analystSelected;
    }

    public void setDataEngineerSelected(boolean dataEngineerSelected) {
        this.dataEngineerSelected = dataEngineerSelected;
    }

    public void setFreightManagerSelected(boolean freightManagerSelected) {
        this.freightManagerSelected = freightManagerSelected;
    }

    public void setOperationsPlannerSelected(boolean operationsPlannerSelected) {
        this.operationsPlannerSelected = operationsPlannerSelected;
    }

    public void setPlanner2Selected(boolean planner2Selected) {
        this.planner2Selected = planner2Selected;
    }

    /**
     * Selects all available roles for filtering logs.
     */
    public void selectAllRoles() {
        globalSelected = true;
        pickerSelected = true;
        planner1Selected = true;
        qualityOperatorSelected = true;
        terminalOperatorSelected = true;
        trafficDispatcherSelected = true;
        warehousePlannerSelected = true;
        analystSelected = true;
        dataEngineerSelected = true;
        freightManagerSelected = true;
        operationsPlannerSelected = true;
        planner2Selected = true;
    }

    /**
     * Deselects all roles for filtering logs.
     */
    public void clearAllRoles() {
        globalSelected = false;
        pickerSelected = false;
        planner1Selected = false;
        qualityOperatorSelected = false;
        terminalOperatorSelected = false;
        trafficDispatcherSelected = false;
        warehousePlannerSelected = false;
        analystSelected = false;
        dataEngineerSelected = false;
        freightManagerSelected = false;
        operationsPlannerSelected = false;
        planner2Selected = false;
    }

    /**
     * Retrieves a filtered list of logs based on the selected roles.
     *
     * @return observable list containing the string representations of the filtered logs.
     */
    public ObservableList<String> getFilteredLogs() {
        ObservableList<String> filteredLogs = FXCollections.observableArrayList();
        Set<RoleType> selectedRoles = new HashSet<>();
        if (globalSelected) selectedRoles.add(RoleType.GLOBAL);
        if (pickerSelected) selectedRoles.add(RoleType.PICKER);
        if (planner1Selected) selectedRoles.add(RoleType.PLANNER1);
        if (qualityOperatorSelected) selectedRoles.add(RoleType.QUALITY_OPERATOR);
        if (terminalOperatorSelected) selectedRoles.add(RoleType.TERMINAL_OPERATOR);
        if (trafficDispatcherSelected) selectedRoles.add(RoleType.TRAFFIC_DISPATCHER);
        if (warehousePlannerSelected) selectedRoles.add(RoleType.WAREHOUSE_PLANNER);
        if (analystSelected) selectedRoles.add(RoleType.ANALYST);
        if (dataEngineerSelected) selectedRoles.add(RoleType.DATA_ENGINEER);
        if (freightManagerSelected) selectedRoles.add(RoleType.FREIGHT_MANAGER2);
        if (operationsPlannerSelected) selectedRoles.add(RoleType.OPERATIONS_PLANNER);
        if (planner2Selected) selectedRoles.add(RoleType.PLANNER2);

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
     * @param logString the string representation of a log entry.
     * @return the {@link RoleType} of the corresponding log, or null if not found.
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
