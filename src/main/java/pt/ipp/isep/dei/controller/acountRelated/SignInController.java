package pt.ipp.isep.dei.controller.acountRelated;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.UserRepository;

import java.util.List;

/**
 * Controller responsible for user sign-in operations.
 * <p>
 * Provides methods to verify credentials, retrieve roles,
 * determine navigation paths, and check account activation status.
 */
public class SignInController {

    /** Repository for managing user data. */
    private final UserRepository userRepository;

    /**
     * Default constructor that initializes the user repository.
     */
    public SignInController() {
        userRepository = Repositories.getInstance().getUserRepository();
    }

    /**
     * Checks if an account with the given email exists.
     *
     * @param email the email to check
     * @return true if the account exists, false otherwise
     */
    public boolean accountExists(String email) {
        return userRepository.exists(email);
    }

    /**
     * Verifies if the given password matches the one stored for the specified email.
     *
     * @param email the user's email
     * @param password the password to verify
     * @return true if the password matches, false otherwise
     */
    public boolean verifyPassword(String email, String password) {
        var user = userRepository.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }

    /**
     * Retrieves the list of roles associated with a user.
     *
     * @param email the user's email
     * @return an observable list of role names, or an empty list if none are found
     */
    public ObservableList<String> getRoles(String email) {
        var user = userRepository.findByEmail(email);
        if (user == null || user.getRoleList() == null) {
            return FXCollections.observableArrayList();
        }

        List<String> roles = user.getRoleList();
        return FXCollections.observableArrayList(roles);
    }

    /**
     * Determines the FXML path corresponding to a specific user role.
     *
     * @param role the role name
     * @return the path to the corresponding FXML file
     */
    public String getSignPath(String role) {
        return switch (role) {
            case "Admin" -> "/fxml/admin/adminHomePage.fxml";
            case "Analyst" -> "/fxml/analyst/analystHomePage.fxml";
            case "Data Engineer" -> "/fxml/dataEngineer/dataEngineerHomePage.fxml";
            case "Freight Manager" -> "/fxml/freightManager/freightManagerHomePage.fxml";
            case "Infrastructure Planner" -> "/fxml/infrastructurePlanner/infrastructurePlannerHomePage.fxml";
            case "Maintenance Planner" -> "/fxml/maintenancePlanner/maintenancePlannerHomePage.fxml";
            case "Operations Planner" -> "/fxml/operationsPlanner/operationsPlannerHomePage.fxml";
            case "Planner" -> "/fxml/planner/plannerHomePage.fxml";
            case "Picker" -> "/fxml/picker/pickerHomePage.fxml";
            case "Station Storage Manager" -> "/fxml/stationStorageManager/stationStorageManagerHomePage.fxml";
            case "Terminal Operator" -> "/fxml/terminalOperator/terminalOperatorHomePage.fxml";
            case "Traffic Dispatcher" -> "/fxml/trafficDispatcher/trafficDispatcherHomePage.fxml";
            case "Traffic Manager" -> "/fxml/trafficManager/trafficManagerHomePage.fxml";
            case "Warehouse Planner" -> "/fxml/warehousePlanner/warehousePlannerHomePage.fxml";
            case "Quality Operator" -> "/fxml/qualityOperator/qualityOperatorHomePage.fxml";
            case "Operations Analyst" -> "/fxml/operationsAnalyst/operationsAnalystHomePage.fxml";
            case "Route Planner" -> "/fxml/routePlanner/routePlannerHomePage.fxml";
            default -> "/fxml/visitorMenu/homePage.fxml";
        };
    }

    /**
     * Checks if an account is active.
     *
     * @param email the user's email
     * @return true if the account is active, false otherwise
     */
    public boolean verifyIfAccountIsActive(String email) {
        var user = userRepository.findByEmail(email);
        return user != null && user.isActive();
    }
}
