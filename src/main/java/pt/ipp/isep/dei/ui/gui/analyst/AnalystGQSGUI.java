package pt.ipp.isep.dei.ui.gui.analyst;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import pt.ipp.isep.dei.controller.analyst.AnalystGQSController;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Analyst GQS GUI.
 *
 * <p>This JavaFX controller manages the Analyst general query/service (GQS)
 * user interface. It initialises controller objects, wires UI components and
 * provides handlers for navigation and filter controls. The class is chiefly
 * concerned with presenting system logs and colouring them according to the
 * associated role.</p>
 *
 */
public class AnalystGQSGUI implements Initializable {

    private AnalystGQSController controller;
    private UIUtils uiUtils;

    @FXML
    private ListView<String> listViewLogs;

    @FXML private CheckBox checkBoxGlobal;
    @FXML private CheckBox checkBoxAnalyst;
    @FXML private CheckBox checkBoxDataEngineer;
    @FXML private CheckBox checkBoxFreightManager;
    @FXML private CheckBox checkBoxOperationsPlanner;
    @FXML private CheckBox checkBoxPlanner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new AnalystGQSController();
        this.uiUtils = new UIUtils();

        configureColoredLogs();
        updateLogListView();

        listViewLogs.setSelectionModel(null);
    }

    /**
     * Navigate to the analyst home page.
     *
     * @param event the {@link ActionEvent} that triggered the navigation
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/analyst/analystHomePage.fxml", "MABEC - Analyst - Home Page");
    }

    /**
     * Handle navigation to the proximity search view.
     *
     * @param actionEvent the {@link ActionEvent} that triggered the navigation
     */
    @FXML
    public void handleProximitySearch(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/analyst/analystProximitySearch.fxml", "MABEC - Analyst - Proximity Search");
    }

    /**
     * Log out the current user and return to the visitor home page.
     *
     * @param event the {@link ActionEvent} that triggered the logout
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Select all role checkboxes and update the filter in the controller.
     * This also refreshes the displayed logs.
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
     * Clear all role checkboxes and update the controller filter.
     * The displayed logs are refreshed afterwards.
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
     * Update controller state according to the current checkboxes and refresh
     * the visible log list.
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
     * Refresh the {@link ListView} with the controller's filtered log entries.
     * If entries exist the view is scrolled to the most recent one.
     */
    private void updateLogListView() {
        listViewLogs.getItems().clear();
        listViewLogs.getItems().addAll(controller.getFilteredLogs());
        if (!listViewLogs.getItems().isEmpty()) {
            listViewLogs.scrollTo(listViewLogs.getItems().size() - 1);
        }
    }

    /**
     * Configure the cell factory to colour log lines according to the role.
     *
     * <p>The colour is derived from the {@link RoleType} returned by the
     * controller for a given log string. If no role can be determined the
     * text is displayed in black by default.</p>
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