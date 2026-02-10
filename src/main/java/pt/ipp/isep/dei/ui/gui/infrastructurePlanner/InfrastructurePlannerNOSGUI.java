package pt.ipp.isep.dei.ui.gui.infrastructurePlanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import pt.ipp.isep.dei.controller.infrastructurePlanner.InfrastructurePlannerNOSController;
import pt.ipp.isep.dei.controller.routePlanner.RoutePlannerNOSController;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Infrastructure Planner NOS logs view.
 *
 * <p>This JavaFX controller initialises the NOS log display, provides role‑based
 * filtering controls and offers simple navigation to other Infrastructure Planner
 * screens. Log entries are rendered with role‑specific colours to aid visual
 * inspection.</p>
 *
 * <p>The class delegates log retrieval and role determination to
 * {@link InfrastructurePlannerNOSController}. It performs only presentation
 * and UI wiring; no business logic is implemented here.</p>
 */
public class InfrastructurePlannerNOSGUI implements Initializable {

    private InfrastructurePlannerNOSController controller;
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
     * Initialise the controller and supporting UI helpers.
     *
     * <p>This method is invoked by the JavaFX runtime following FXML loading.
     * It constructs the underlying controller, initialises UI utilities,
     * configures the coloured log cell factory and populates the visible
     * log list.</p>
     *
     * @param url the location used to resolve relative paths for the root object
     * @param resourceBundle the resources used to localise the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new InfrastructurePlannerNOSController();
        uiUtils = new UIUtils();

        configureColoredLogs();
        updateLogListView();

        listViewLogs.setSelectionModel(null);
    }

    /**
     * Log out the current user and return to the public home page.
     *
     * @param event the action event triggered by the UI control
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Navigate to the Infrastructure Planner home page.
     *
     * @param event the action event triggered by the UI control
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/infrastructurePlanner/infrastructurePlannerHomePage.fxml", "MABEC - Infrastructure Planner - Home Page");
    }

    /**
     * Open the railway upgrade planning view.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void handleRailwayUpgradesButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/infrastructurePlanner/railwayUpgradePlanning.fxml", "MABEC - Infrastructure Planner - Railway Upgrade Planning");
    }

    /**
     * Open the baseline cost estimation view.
     *
     * @param actionEvent the action event that triggered the navigation
     */
    @FXML
    public void handleBaselineCost(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/infrastructurePlanner/railwayBaselineCost.fxml", "MABEC - Infrastructure Planner - Baseline Cost");
    }

    /**
     * Select all role filters and refresh the displayed logs.
     *
     * <p>The method updates the UI checkboxes, instructs the controller to
     * select all roles and then repopulates the log list to reflect the
     * new filter state.</p>
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
     * Clear all role filters and refresh the displayed logs.
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
     * Apply the current checkbox selections to the controller's role filter
     * and refresh the log list.
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
     * Refresh the NOS log ListView according to the currently active filters.
     * The view is scrolled to the most recent entry when entries are present.
     */
    private void updateLogListView() {
        listViewLogs.getItems().clear();
        listViewLogs.getItems().addAll(controller.getFilteredLogs());
        if (!listViewLogs.getItems().isEmpty()) {
            listViewLogs.scrollTo(listViewLogs.getItems().size() - 1);
        }
    }

    /**
     * Configure the ListView cell factory so that each log line is coloured
     * according to the associated {@link RoleType}.
     *
     * <p>If the controller cannot determine a role for a specific log entry
     * the text is rendered in black by default.</p>
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
