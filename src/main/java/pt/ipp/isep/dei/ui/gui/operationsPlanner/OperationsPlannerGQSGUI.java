package pt.ipp.isep.dei.ui.gui.operationsPlanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import pt.ipp.isep.dei.controller.dataEnginner.DataEngineerGQSController;
import pt.ipp.isep.dei.controller.operationsPlanner.OperationsPlannerGQSController;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * JavaFX controller for the Operations Planner general query/service (GQS)
 * user interface.
 *
 * <p>This controller initialises the underlying domain controller and the
 * UI helper utilities, prepares the log list view and provides handlers for
 * navigation and role‑based filtering. Log lines are coloured according to
 * the {@link RoleType} associated with each entry; the colouring is applied
 * by a cell factory configured in {@link #configureColoredLogs()}.</p>
 *
 */
public class OperationsPlannerGQSGUI implements Initializable {

    private OperationsPlannerGQSController controller;
    private UIUtils uiUtils;

    @FXML
    private ListView<String> listViewLogs;

    @FXML private CheckBox checkBoxGlobal;
    @FXML private CheckBox checkBoxAnalyst;
    @FXML private CheckBox checkBoxDataEngineer;
    @FXML private CheckBox checkBoxFreightManager;
    @FXML private CheckBox checkBoxOperationsPlanner;
    @FXML private CheckBox checkBoxPlanner;

    /**
     * Initialise the controller and user‑interface utilities.
     *
     * <p>This method is invoked by the JavaFX runtime when the FXML view is
     * loaded. It constructs the domain controller and the {@link UIUtils}
     * helper, configures the coloured log rendering and populates the
     * visible log list.</p>
     *
     * @param url resource location (unused)
     * @param resourceBundle resources for localisation (unused)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new OperationsPlannerGQSController();
        this.uiUtils = new UIUtils();

        configureColoredLogs();
        updateLogListView();

        listViewLogs.setSelectionModel(null);
    }

    /**
     * Log out the current user and return to the public home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Navigate to the operations planner radius search view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void handleRadiusSearch(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/operationsPlanner/operationsPlannerRadiusSearch.fxml", "MABEC - Operations Planner - Radius Search");
    }

    /**
     * Navigate to the Operations Planner home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/operationsPlanner/operationsPlannerHomePage.fxml", "MABEC - Operations Planner - Home Page");
    }

    /**
     * Select all role filters and refresh the log view.
     *
     * <p>The method updates the checkbox controls, informs the controller
     * which roles are selected and repopulates the visible logs.</p>
     */
    @FXML
    public void handleSelectAllButton() {
        checkBoxGlobal.setSelected(true);
        checkBoxAnalyst.setSelected(true);
        checkBoxDataEngineer.setSelected(true);
        checkBoxFreightManager.setSelected(true);
        checkBoxOperationsPlanner.setSelected(true);
        checkBoxPlanner.setSelected(true);
        controller.selectAllRoles();
        updateLogListView();
    }

    /**
     * Clear all role filters and refresh the log view.
     */
    @FXML
    public void handleClearAllButton() {
        checkBoxGlobal.setSelected(false);
        checkBoxAnalyst.setSelected(false);
        checkBoxDataEngineer.setSelected(false);
        checkBoxFreightManager.setSelected(false);
        checkBoxOperationsPlanner.setSelected(false);
        checkBoxPlanner.setSelected(false);
        controller.clearAllRoles();
        updateLogListView();
    }

    /**
     * Apply the current checkbox selections to the controller's role
     * filter and refresh the log list.
     */
    @FXML
    public void handleUsedCheckBox() {
        controller.setGlobalSelected(checkBoxGlobal.isSelected());
        controller.setAnalystSelected(checkBoxAnalyst.isSelected());
        controller.setDataEngineerSelected(checkBoxDataEngineer.isSelected());
        controller.setFreightManagerSelected(checkBoxFreightManager.isSelected());
        controller.setOperationsPlannerSelected(checkBoxOperationsPlanner.isSelected());
        controller.setPlannerSelected(checkBoxPlanner.isSelected());
        updateLogListView();
    }

    /**
     * Refresh the log list view with the controller's filtered log entries.
     *
     * <p>If entries exist the view is scrolled to the most recent one.</p>
     */
    private void updateLogListView() {
        listViewLogs.getItems().clear();
        listViewLogs.getItems().addAll(controller.getFilteredLogs());
        if (!listViewLogs.getItems().isEmpty()) {
            listViewLogs.scrollTo(listViewLogs.getItems().size() - 1);
        }
    }

    /**
     * Configure the ListView cell factory to colour each log line according
     * to its associated {@link RoleType}.
     *
     * <p>The colouring follows the {@code getColor()} value provided by the
     * {@link RoleType} enumeration. If no role is recognised the text is
     * rendered in black. The method does not perform null checks and a
     * {@link NullPointerException} may be thrown if a role or the
     * underlying colour is absent.</p>
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