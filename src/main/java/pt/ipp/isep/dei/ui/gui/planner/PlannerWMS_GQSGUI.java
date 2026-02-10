package pt.ipp.isep.dei.ui.gui.planner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import pt.ipp.isep.dei.controller.planner.PlannerWMS_GQSController;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Planner WMS / GQS logs view.
 *
 * <p>This JavaFX controller initialises the WMS/GQS log display used by
 * planners. It configures a coloured ListView where each log entry is
 * rendered using a colour associated with the responsible role (the
 * mapping is obtained from {@link PlannerWMS_GQSController}). The class
 * provides handlers to navigate to other planner screens and to filter
 * the visible logs by role via a set of checkboxes.</p>
 *
 * <p>The controller delegates data retrieval and role determination to
 * {@link PlannerWMS_GQSController}. This class concerns itself only with
 * presentation and user interaction; no business logic is performed here.
 */
public class PlannerWMS_GQSGUI implements Initializable {
    private PlannerWMS_GQSController controller;
    private UIUtils uiUtils;

    @FXML
    private ListView<String> listViewLogs;

    @FXML private CheckBox checkBoxGlobal1;
    @FXML private CheckBox checkBoxPicker;
    @FXML private CheckBox checkBoxPlanner1;
    @FXML private CheckBox checkBoxQualityOperator;
    @FXML private CheckBox checkBoxTerminalOperator;
    @FXML private CheckBox checkBoxTrafficDispatcher;
    @FXML private CheckBox checkBoxWarehousePlanner1;

    @FXML private CheckBox checkBoxGlobal2;
    @FXML private CheckBox checkBoxAnalyst;
    @FXML private CheckBox checkBoxDataEngineer;
    @FXML private CheckBox checkBoxFreightManager;
    @FXML private CheckBox checkBoxOperationsPlanner;
    @FXML private CheckBox checkBoxPlanner2;

    @FXML private AnchorPane GQSAnchorPane, WMSAnchorPane;

    /**
     * Initialise the controller and supporting utilities.
     *
     * <p>This method is invoked by the JavaFX runtime when the FXML view is
     * loaded. It creates the domain controller and a small {@link UIUtils}
     * helper, configures the coloured cell factory for the log ListView and
     * populates the initial list of filtered logs.</p>
     *
     * @param url not used by this implementation
     * @param resourceBundle not used by this implementation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new PlannerWMS_GQSController();
        uiUtils = new UIUtils();

        configureColoredLogs();
        updateLogListView();

        listViewLogs.setSelectionModel(null);
    }

    /**
     * Navigate to the Planner Home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerHomePage.fxml", "MABEC - Planner - Home Page");
    }

    /**
     * Log out the current user and return to the public Home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Navigate to the Planner Trolleys view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void trolleysOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerTrolleys.fxml", "MABEC - Planner - Trolleys");
    }

    /**
     * Navigate to the Planner Picking Plans view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void pickingPlansOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerPickingPlans.fxml", "MABEC - Planner - Picking Plans");
    }

    /**
     * Navigate to the Planner station search view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void searchStationButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerStationSearch.fxml", "MABEC - Planner - Search Stations");
    }

    /**
     * Navigate to the Planner geographical area view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void handleGeograpgicalArea(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerGeographicalArea.fxml", "MABEC - Planner - Geographical Area");
    }

    /**
     * Select all available role filters and refresh the log list.
     *
     * <p>The method also informs the domain controller so that the
     * subsequent filtered result set reflects the UI state.</p>
     */
    @FXML
    public void handleSelectAllButton() {
        checkBoxGlobal1.setSelected(true);
        checkBoxGlobal2.setSelected(true);
        checkBoxPicker.setSelected(true);
        checkBoxPlanner1.setSelected(true);
        checkBoxQualityOperator.setSelected(true);
        checkBoxTerminalOperator.setSelected(true);
        checkBoxTrafficDispatcher.setSelected(true);
        checkBoxWarehousePlanner1.setSelected(true);
        checkBoxAnalyst.setSelected(true);
        checkBoxDataEngineer.setSelected(true);
        checkBoxFreightManager.setSelected(true);
        checkBoxOperationsPlanner.setSelected(true);
        checkBoxPlanner2.setSelected(true);
        controller.selectAllRoles();
        updateLogListView();
    }

    /**
     * Clear all role filters and refresh the log list.
     */
    @FXML
    public void handleClearAllButton() {
        checkBoxGlobal1.setSelected(false);
        checkBoxGlobal2.setSelected(false);
        checkBoxPicker.setSelected(false);
        checkBoxPlanner1.setSelected(false);
        checkBoxQualityOperator.setSelected(false);
        checkBoxTerminalOperator.setSelected(false);
        checkBoxTrafficDispatcher.setSelected(false);
        checkBoxWarehousePlanner1.setSelected(false);
        checkBoxAnalyst.setSelected(false);
        checkBoxDataEngineer.setSelected(false);
        checkBoxFreightManager.setSelected(false);
        checkBoxOperationsPlanner.setSelected(false);
        checkBoxPlanner2.setSelected(false);
        controller.clearAllRoles();
        updateLogListView();
    }

    /**
     * Apply the currently selected checkboxes to the controller's role
     * filter and refresh the visible logs.
     *
     * <p>The method distinguishes between the WMS and GQS panes and sets an
     * appropriate subset of roles in the domain controller.</p>
     */
    @FXML
    public void handleUsedCheckBox() {
        if (WMSAnchorPane.isVisible()) {
            controller.setGlobalSelected(checkBoxGlobal1.isSelected());
            controller.setPickerSelected(checkBoxPicker.isSelected());
            controller.setPlanner1Selected(checkBoxPlanner1.isSelected());
            controller.setQualityOperatorSelected(checkBoxQualityOperator.isSelected());
            controller.setTerminalOperatorSelected(checkBoxTerminalOperator.isSelected());
            controller.setTrafficDispatcherSelected(checkBoxTrafficDispatcher.isSelected());
            controller.setWarehousePlannerSelected(checkBoxWarehousePlanner1.isSelected());
        } else {
            controller.setGlobalSelected(checkBoxGlobal2.isSelected());
            controller.setAnalystSelected(checkBoxAnalyst.isSelected());
            controller.setDataEngineerSelected(checkBoxDataEngineer.isSelected());
            controller.setFreightManagerSelected(checkBoxFreightManager.isSelected());
            controller.setOperationsPlannerSelected(checkBoxOperationsPlanner.isSelected());
            controller.setPlanner2Selected(checkBoxPlanner2.isSelected());
        }
        updateLogListView();
    }

    /**
     * Toggle the visible pane between WMS and GQS logs and reset filters.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void swapLogs(ActionEvent event) {
        if (WMSAnchorPane.isVisible()) {
            WMSAnchorPane.setVisible(false);
            WMSAnchorPane.setDisable(true);
            GQSAnchorPane.setVisible(true);
            GQSAnchorPane.setDisable(false);
        } else {
            WMSAnchorPane.setVisible(true);
            WMSAnchorPane.setDisable(false);
            GQSAnchorPane.setVisible(false);
            GQSAnchorPane.setDisable(true);
        }
        handleClearAllButton();
    }

    /**
     * Refresh the visible log list according to the currently active
     * filters and scroll to the most recent entry if present.
     */
    private void updateLogListView() {
        listViewLogs.getItems().clear();
        listViewLogs.getItems().addAll(controller.getFilteredLogs());
        if (!listViewLogs.getItems().isEmpty()) {
            listViewLogs.scrollTo(listViewLogs.getItems().size() - 1);
        }
    }

    /**
     * Configure the ListView cell factory so that log lines are coloured
     * according to the {@link RoleType} associated with each entry.
     *
     * <p>If no role is identified the text is rendered in black. The method
     * modifies the visual style of each cell only; it does not change the
     * underlying log data.</p>
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
