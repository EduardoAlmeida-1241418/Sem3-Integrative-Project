package pt.ipp.isep.dei.controller.freightManager;

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
public class FreightManagerGQS_NOSController {

    /**
     * Repository responsible for accessing log data.
     */
    private final LogRepository logRepository;

    private boolean globalSelected;
    private boolean analystSelected;
    private boolean dataEngineerSelected;
    private boolean freightManagerSelected;
    private boolean operationsPlannerSelected;
    private boolean plannerSelected;

    private boolean infrastructurePlannerSelected;
    private boolean maintenancePlannerSelected;
    private boolean operationsAnalystSelected;
    private boolean routePlannerSelected;
    private boolean trafficManagerSelected;

    /**
     * Initializes the controller and obtains a reference to the LogRepository.
     */
    public FreightManagerGQS_NOSController() {
        this.logRepository = Repositories.getInstance().getLogRepository();
    }

    /**
     * Sets whether the GLOBAL role is selected.
     *
     * @param globalSelected true if selected, false otherwise
     */
    public void setGlobalSelected(boolean globalSelected) {
        this.globalSelected = globalSelected;
    }

    /**
     * Sets whether the ANALYST role is selected.
     *
     * @param analystSelected true if selected, false otherwise
     */
    public void setAnalystSelected(boolean analystSelected) {
        this.analystSelected = analystSelected;
    }

    /**
     * Sets whether the DATA_ENGINEER role is selected.
     *
     * @param dataEngineerSelected true if selected, false otherwise
     */
    public void setDataEngineerSelected(boolean dataEngineerSelected) {
        this.dataEngineerSelected = dataEngineerSelected;
    }

    /**
     * Sets whether the FREIGHT_MANAGER role is selected.
     *
     * @param freightManagerSelected true if selected, false otherwise
     */
    public void setFreightManagerSelected(boolean freightManagerSelected) {
        this.freightManagerSelected = freightManagerSelected;
    }

    /**
     * Sets whether the OPERATIONS_PLANNER role is selected.
     *
     * @param operationsPlannerSelected true if selected, false otherwise
     */
    public void setOperationsPlannerSelected(boolean operationsPlannerSelected) {
        this.operationsPlannerSelected = operationsPlannerSelected;
    }

    /**
     * Sets whether the PLANNER role is selected.
     *
     * @param plannerSelected true if selected, false otherwise
     */
    public void setPlannerSelected(boolean plannerSelected) {
        this.plannerSelected = plannerSelected;
    }

    /**
     * Sets whether the INFRASTRUCTURE_PLANNER role is selected.
     *
     * @param infrastructurePlannerSelected true if selected, false otherwise
     */
    public void setInfrastructurePlannerSelected(boolean infrastructurePlannerSelected) {
        this.infrastructurePlannerSelected = infrastructurePlannerSelected;
    }

    /**
     * Sets whether the MAINTENANCE_PLANNER role is selected.
     *
     * @param maintenancePlannerSelected true if selected, false otherwise
     */
    public void setMaintenancePlannerSelected(boolean maintenancePlannerSelected) {
        this.maintenancePlannerSelected = maintenancePlannerSelected;
    }

    /**
     * Sets whether the OPERATIONS_ANALYST role is selected.
     *
     * @param operationsAnalystSelected true if selected, false otherwise
     */
    public void setOperationsAnalystSelected(boolean operationsAnalystSelected) {
        this.operationsAnalystSelected = operationsAnalystSelected;
    }

    /**
     * Sets whether the ROUTE_PLANNER role is selected.
     *
     * @param routePlannerSelected true if selected, false otherwise
     */
    public void setRoutePlannerSelected(boolean routePlannerSelected) {
        this.routePlannerSelected = routePlannerSelected;
    }

    /**
     * Sets whether the TRAFFIC_MANAGER role is selected.
     *
     * @param trafficManagerSelected true if selected, false otherwise
     */
    public void setTrafficManagerSelected(boolean trafficManagerSelected) {
        this.trafficManagerSelected = trafficManagerSelected;
    }

    /**
     * Selects all available roles for filtering logs.
     */
    public void selectAllRoles() {
        globalSelected = true;
        analystSelected = true;
        dataEngineerSelected = true;
        freightManagerSelected = true;
        operationsPlannerSelected = true;
        plannerSelected = true;
        infrastructurePlannerSelected = true;
        maintenancePlannerSelected = true;
        operationsAnalystSelected = true;
        routePlannerSelected = true;
        trafficManagerSelected = true;
    }

    /**
     * Deselects all roles for filtering logs.
     */
    public void clearAllRoles() {
        globalSelected = false;
        analystSelected = false;
        dataEngineerSelected = false;
        freightManagerSelected = false;
        operationsPlannerSelected = false;
        plannerSelected = false;
        infrastructurePlannerSelected = false;
        maintenancePlannerSelected = false;
        operationsAnalystSelected = false;
        routePlannerSelected = false;
        trafficManagerSelected = false;
    }

    /**
     * Retrieves a filtered list of logs based on the selected roles.
     *
     * @return observable list containing the string representations of the filtered logs
     */
    public ObservableList<String> getFilteredLogs() {
        ObservableList<String> filteredLogs = FXCollections.observableArrayList();
        Set<RoleType> selectedRoles = new HashSet<>();
        if (globalSelected) selectedRoles.add(RoleType.GLOBAL);
        if (analystSelected) selectedRoles.add(RoleType.ANALYST);
        if (dataEngineerSelected) selectedRoles.add(RoleType.DATA_ENGINEER);
        if (freightManagerSelected) selectedRoles.add(RoleType.FREIGHT_MANAGER2);
        if (operationsPlannerSelected) selectedRoles.add(RoleType.OPERATIONS_PLANNER);
        if (plannerSelected) selectedRoles.add(RoleType.PLANNER2);
        if (infrastructurePlannerSelected) selectedRoles.add(RoleType.INFRASTRUCTURE_PLANNER);
        if (maintenancePlannerSelected) selectedRoles.add(RoleType.MAINTENANCE_PLANNER);
        if (operationsAnalystSelected) selectedRoles.add(RoleType.OPERATIONS_ANALYST);
        if (routePlannerSelected) selectedRoles.add(RoleType.ROUTE_PLANNER);
        if (trafficManagerSelected) selectedRoles.add(RoleType.TRAFFIC_MANAGER);

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
     * @param logString the string representation of a log entry
     * @return the RoleType of the corresponding log, or null if not found
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
