package pt.ipp.isep.dei.controller.dataEnginner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.data.repository.LogRepository;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.domain.Log;
import pt.ipp.isep.dei.domain.RoleType;

import java.util.HashSet;
import java.util.Set;

/**
 * Controller responsible for managing the Warehouse Management System (WMS) log
 * filtering and viewing operations for the Planner role.
 * <p>
 * Allows filtering logs by user roles, selecting/deselecting roles, and retrieving
 * log entries based on selected filters.
 */
public class DataEngineerGQSController {

    private final LogRepository logRepository;

    private boolean globalSelected;
    private boolean analystSelected;
    private boolean dataEngineerSelected;
    private boolean freightManagerSelected;
    private boolean operationsPlannerSelected;
    private boolean plannerSelected;

    /**
     * Initializes the controller and obtains a reference to the LogRepository.
     */
    public DataEngineerGQSController() {
        this.logRepository = Repositories.getInstance().getLogRepository();
    }

    public void setGlobalSelected(boolean globalSelected) {
        this.globalSelected = globalSelected;
    }

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

    public void setPlannerSelected(boolean plannerSelected) {
        this.plannerSelected = plannerSelected;
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
        if (analystSelected) selectedRoles.add(RoleType.ANALYST);
        if (dataEngineerSelected) selectedRoles.add(RoleType.DATA_ENGINEER);
        if (freightManagerSelected) selectedRoles.add(RoleType.FREIGHT_MANAGER2);
        if (operationsPlannerSelected) selectedRoles.add(RoleType.OPERATIONS_PLANNER);
        if (plannerSelected) selectedRoles.add(RoleType.PLANNER2);

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
