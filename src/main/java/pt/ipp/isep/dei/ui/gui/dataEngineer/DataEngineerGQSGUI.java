package pt.ipp.isep.dei.ui.gui.dataEngineer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import pt.ipp.isep.dei.controller.analyst.AnalystGQSController;
import pt.ipp.isep.dei.controller.dataEnginner.DataEngineerGQSController;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Data Engineer GQS graphical interface.
 *
 * <p>This JavaFX controller manages the view used by Data Engineers to
 * inspect general query/service (GQS) logs. It initialises helper
 * components, configures the log list view and provides handlers for scene
 * navigation and filtering by role. The class is responsible for UI
 * behaviour only; business logic is delegated to the underlying
 * {@link DataEngineerGQSController}.</p>
 *
 */
public class DataEngineerGQSGUI implements Initializable {

    private DataEngineerGQSController controller;
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
     * Initialise the controller and supporting utilities.
     *
     * @param url not used by this implementation
     * @param resourceBundle not used by this implementation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new DataEngineerGQSController();
        this.uiUtils = new UIUtils();

        configureColoredLogs();
        updateLogListView();

        listViewLogs.setSelectionModel(null);
    }

    /**
     * Navigate to the organised stations view for data engineers.
     *
     * @param actionEvent the action event supplied by the UI
     */
    @FXML
    public void handleOrganizedStations(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/dataEngineer/organizedStations.fxml", "MABEC - Data Engineer - Organized Stations");
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
     * Navigate to the Data Engineer home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/dataEngineer/dataEngineerHomePage.fxml", "MABEC - Data Engineer - Home Page");
    }

    /**
     * Select all role checkboxes and update the controller filter, then
     * refresh the displayed logs.
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
     * Clear all role checkboxes and update the controller filter, then
     * refresh the displayed logs.
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
     * Apply the current checkbox selections to the controller filter and
     * refresh the log display.
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
     * Refresh the {@link ListView} with the controller's filtered log
     * entries and scroll to the most recent entry if present.
     */
    private void updateLogListView() {
        listViewLogs.getItems().clear();
        listViewLogs.getItems().addAll(controller.getFilteredLogs());
        if (!listViewLogs.getItems().isEmpty()) {
            listViewLogs.scrollTo(listViewLogs.getItems().size() - 1);
        }
    }

    /**
     * Configure the list view cell factory so that each log line is coloured
     * according to its associated {@link RoleType}.
     *
     * <p>If a role cannot be determined the text is rendered in black.</p>
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