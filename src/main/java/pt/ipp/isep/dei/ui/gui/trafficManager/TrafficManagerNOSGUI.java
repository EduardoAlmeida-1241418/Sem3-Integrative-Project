package pt.ipp.isep.dei.ui.gui.trafficManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import pt.ipp.isep.dei.controller.traficManager.pathRelated.TrafficManagerNOSController;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Terminal Operator WMS Logs GUI.
 * Allows terminal operators to view, filter, and color WMS logs based on user roles.
 * Provides navigation and log filtering functionality.
 */
public class TrafficManagerNOSGUI implements Initializable {

    private TrafficManagerNOSController controller;
    private UIUtils uiUtils;

    @FXML private ListView<String> listViewLogs;

    @FXML private CheckBox checkBoxGlobal;
    @FXML private CheckBox checkBoxFreightManager;
    @FXML private CheckBox checkBoxInfrastructurePlanner;
    @FXML private CheckBox checkBoxMaintenancePlanner;
    @FXML private CheckBox checkBoxOperationsAnalyst;
    @FXML private CheckBox checkBoxRoutePlanner;
    @FXML private CheckBox checkBoxTrafficManager;

    /**
     * Initializes the controller, UI utilities, and log list configuration.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new TrafficManagerNOSController();
        uiUtils = new UIUtils();

        configureColoredLogs();
        updateLogListView();

        listViewLogs.setSelectionModel(null);
    }

    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/trafficManagerHomePage.fxml", "MABEC - Traffic Manager - Home Page");
    }

    @FXML
    public void pathOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/pathRelated/trafficManagerPath.fxml", "MABEC - Traffic Manager - Paths");
    }

    @FXML
    public void assignOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/trafficManagerAssign.fxml", "MABEC - Traffic Manager - Assign");
    }

    @FXML
    public void schedulerOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/scheduleRelated/trafficManagerScheduler.fxml", "MABEC - Traffic Manager - Scheduler");
    }

    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Selects all role filters and updates the log list view.
     */
    @FXML
    public void handleSelectAllButton() {
        checkBoxGlobal.setSelected(true);
        checkBoxFreightManager.setSelected(true);
        checkBoxInfrastructurePlanner.setSelected(true);
        checkBoxMaintenancePlanner.setSelected(true);
        checkBoxOperationsAnalyst.setSelected(true);
        checkBoxRoutePlanner.setSelected(true);
        checkBoxTrafficManager.setSelected(true);
        controller.selectAllRoles();
        updateLogListView();
    }

    /**
     * Clears all role filters and updates the log list view.
     */
    @FXML
    public void handleClearAllButton() {
        checkBoxGlobal.setSelected(false);
        checkBoxFreightManager.setSelected(false);
        checkBoxInfrastructurePlanner.setSelected(false);
        checkBoxMaintenancePlanner.setSelected(false);
        checkBoxOperationsAnalyst.setSelected(false);
        checkBoxRoutePlanner.setSelected(false);
        checkBoxTrafficManager.setSelected(false);
        controller.clearAllRoles();
        updateLogListView();
    }

    /**
     * Handles individual checkbox selection for role-based log filtering.
     * Updates the log list view after each change.
     */
    @FXML
    public void handleUsedCheckBox() {
        controller.setGlobalSelected(checkBoxGlobal.isSelected());
        controller.setFreightManagerSelected(checkBoxFreightManager.isSelected());
        controller.setInfrastructurePlannerSelected(checkBoxInfrastructurePlanner.isSelected());
        controller.setMaintenancePlannerSelected(checkBoxMaintenancePlanner.isSelected());
        controller.setOperationsAnalystSelected(checkBoxOperationsAnalyst.isSelected());
        controller.setRoutePlannerSelected(checkBoxRoutePlanner.isSelected());
        controller.setTrafficManagerSelected(checkBoxTrafficManager.isSelected());
        updateLogListView();
    }

    /**
     * Updates the warehouse log ListView based on active role filters.
     * Automatically scrolls to the latest log entry.
     */
    private void updateLogListView() {
        listViewLogs.getItems().clear();
        listViewLogs.getItems().addAll(controller.getFilteredLogs());
        if (!listViewLogs.getItems().isEmpty()) {
            listViewLogs.scrollTo(listViewLogs.getItems().size() - 1);
        }
    }

    /**
     * Configures the ListView to display logs with colors based on the role type.
     * Each role has a predefined color for easier visual differentiation.
     */
    private void configureColoredLogs() {
        listViewLogs.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(item);
                RoleType role = controller.getRoleTypeForLog(item);

                if (role != null) {
                    Color color = role.getColor();
                    String rgb = String.format("rgb(%d, %d, %d)",
                            (int) (color.getRed() * 255),
                            (int) (color.getGreen() * 255),
                            (int) (color.getBlue() * 255));
                    setStyle("-fx-text-fill: " + rgb + "; -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
                }
            }
        });
    }
}
