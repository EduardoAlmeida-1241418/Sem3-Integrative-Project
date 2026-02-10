package pt.ipp.isep.dei.ui.gui.freightManager;

import javafx.scene.layout.AnchorPane;
import pt.ipp.isep.dei.controller.freightManager.FreightManagerGQS_NOSController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Freight Manager GQS / NOS view.
 *
 * <p>This JavaFX controller initialises and manages the user interface used
 * by Freight Managers to inspect GQS and NOS logs. It provides navigation
 * handlers to other Freight Manager screens, supplies role-based filters
 * and renders log entries coloured according to the associated role.</p>
 *
 * <p>All business logic is delegated to
 * {@link FreightManagerGQS_NOSController}; this class concerns itself with
 * presentation and user interaction only.
 */
public class FreightManagerGQS_NOSGUI implements Initializable {

    private FreightManagerGQS_NOSController controller;
    private UIUtils uiUtils;

    @FXML
    private ListView<String> GQSListViewLogs, NOSListViewLogs;

    @FXML private CheckBox checkBoxGlobal2;
    @FXML private CheckBox checkBoxAnalyst;
    @FXML private CheckBox checkBoxDataEngineer;
    @FXML private CheckBox checkBoxFreightManager2;
    @FXML private CheckBox checkBoxOperationsPlanner;
    @FXML private CheckBox checkBoxPlanner;

    @FXML private CheckBox checkBoxGlobal3;
    @FXML private CheckBox checkBoxFreightManager3;
    @FXML private CheckBox checkBoxInfrastructurePlanner;
    @FXML private CheckBox checkBoxMaintenancePlanner;
    @FXML private CheckBox checkBoxOperationsAnalyst;
    @FXML private CheckBox checkBoxRoutePlanner;
    @FXML private CheckBox checkBoxTrafficManager;

    @FXML private AnchorPane GQSAnchorPane, NOSAnchorPane;

    /**
     * Initialise the controller and supporting utilities.
     *
     * <p>This method is invoked by the JavaFX runtime when the FXML is
     * loaded. It initialises the underlying controller, helper utilities
     * and prepares the log views for display.</p>
     *
     * @param url location used to resolve relative paths (unused)
     * @param resourceBundle resources used to localise the root object (unused)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new FreightManagerGQS_NOSController();
        this.uiUtils = new UIUtils();

        configureColoredLogs();
        updateLogListView();

        GQSListViewLogs.setSelectionModel(null);
    }


    /**
     * Navigate to the Freight Manager home page.
     *
     * @param event the action event supplied by the UI which triggered navigation
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerHomePage.fxml", "MABEC - Freight Manager - Home Page");
    }

    /**
     * Navigate to the route management view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void routeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/RouteRelated/freightManagerRoute.fxml", "MABEC - Freight Manager - Route");
    }

    /**
     * Navigate to the scheduler view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void schedulerOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerScheduler.fxml", "MABEC - Freight Manager - Scheduler");
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
     * Navigate to the Freight Manager trains view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void trainsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/TrainRelated/freightManagerLocomotive.fxml", "MABEC - Freight Manager - Trains");
    }

    /**
     * Select all role checkboxes across the GQS and NOS panels and refresh
     * the filtered logs.
     */
    @FXML
    public void handleSelectAllButton() {
        checkBoxGlobal3.setSelected(true);
        checkBoxFreightManager3.setSelected(true);
        checkBoxInfrastructurePlanner.setSelected(true);
        checkBoxMaintenancePlanner.setSelected(true);
        checkBoxOperationsAnalyst.setSelected(true);
        checkBoxRoutePlanner.setSelected(true);
        checkBoxTrafficManager.setSelected(true);
        checkBoxGlobal2.setSelected(true);
        checkBoxAnalyst.setSelected(true);
        checkBoxDataEngineer.setSelected(true);
        checkBoxFreightManager2.setSelected(true);
        checkBoxOperationsPlanner.setSelected(true);
        checkBoxPlanner.setSelected(true);
        controller.selectAllRoles();
        updateLogListView();
    }

    /**
     * Clear all role checkboxes across the GQS and NOS panels and refresh
     * the filtered logs.
     */
    @FXML
    public void handleClearAllButton() {
        checkBoxGlobal3.setSelected(false);
        checkBoxFreightManager3.setSelected(false);
        checkBoxInfrastructurePlanner.setSelected(false);
        checkBoxMaintenancePlanner.setSelected(false);
        checkBoxOperationsAnalyst.setSelected(false);
        checkBoxRoutePlanner.setSelected(false);
        checkBoxTrafficManager.setSelected(false);
        checkBoxGlobal2.setSelected(false);
        checkBoxAnalyst.setSelected(false);
        checkBoxDataEngineer.setSelected(false);
        checkBoxFreightManager2.setSelected(false);
        checkBoxOperationsPlanner.setSelected(false);
        checkBoxPlanner.setSelected(false);
        controller.clearAllRoles();
        updateLogListView();
    }

    /**
     * Apply the currently selected checkboxes to the controller's role
     * filter and update the visible log list.
     */
    @FXML
    public void handleUsedCheckBox() {
        if (GQSAnchorPane.isVisible()) {
            controller.setGlobalSelected(checkBoxGlobal2.isSelected());
            controller.setAnalystSelected(checkBoxAnalyst.isSelected());
            controller.setDataEngineerSelected(checkBoxDataEngineer.isSelected());
            controller.setFreightManagerSelected(checkBoxFreightManager2.isSelected());
            controller.setOperationsPlannerSelected(checkBoxOperationsPlanner.isSelected());
            controller.setPlannerSelected(checkBoxPlanner.isSelected());
        } else {
            controller.setGlobalSelected(checkBoxGlobal3.isSelected());
            controller.setFreightManagerSelected(checkBoxFreightManager3.isSelected());
            controller.setInfrastructurePlannerSelected(checkBoxInfrastructurePlanner.isSelected());
            controller.setMaintenancePlannerSelected(checkBoxMaintenancePlanner.isSelected());
            controller.setOperationsAnalystSelected(checkBoxOperationsAnalyst.isSelected());
            controller.setRoutePlannerSelected(checkBoxRoutePlanner.isSelected());
            controller.setTrafficManagerSelected(checkBoxTrafficManager.isSelected());
        }
        updateLogListView();
    }

    /**
     * Toggle the visible panel between GQS and NOS logs and reset the
     * checkbox selections.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void swapLogs(ActionEvent event) {
        if (NOSAnchorPane.isVisible()) {
            NOSAnchorPane.setVisible(false);
            NOSAnchorPane.setDisable(true);
            GQSAnchorPane.setVisible(true);
            GQSAnchorPane.setDisable(false);
        } else {
            NOSAnchorPane.setVisible(true);
            NOSAnchorPane.setDisable(false);
            GQSAnchorPane.setVisible(false);
            GQSAnchorPane.setDisable(true);
        }
        handleClearAllButton();
    }

    /**
     * Refresh the visible log list depending on whether the GQS or NOS
     * pane is active. The most recent entry is scrolled into view.
     */
    private void updateLogListView() {
        GQSListViewLogs.getItems().clear();
        NOSListViewLogs.getItems().clear();
        if (GQSAnchorPane.isVisible()) {
            GQSListViewLogs.getItems().addAll(controller.getFilteredLogs());
            if (!GQSListViewLogs.getItems().isEmpty()) {
                GQSListViewLogs.scrollTo(GQSListViewLogs.getItems().size() - 1);
            }
        } else {
            NOSListViewLogs.getItems().addAll(controller.getFilteredLogs());
            if (!NOSListViewLogs.getItems().isEmpty()) {
                NOSListViewLogs.scrollTo(NOSListViewLogs.getItems().size() - 1);
            }
        }

    }

    /**
     * Configure the cell factories for both log list views so that log lines
     * are coloured according to the {@link RoleType} associated with each
     * entry. If no role is identified the text is rendered in black.
     */
    private void configureColoredLogs() {
        GQSListViewLogs.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
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

        NOSListViewLogs.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
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