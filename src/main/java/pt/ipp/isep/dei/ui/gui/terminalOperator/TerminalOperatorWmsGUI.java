package pt.ipp.isep.dei.ui.gui.terminalOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import pt.ipp.isep.dei.controller.terminalOperator.TerminalOperatorWmsController;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Terminal Operator WMS Logs GUI.
 * Allows terminal operators to view, filter, and color WMS logs based on user roles.
 * Provides navigation and log filtering functionality.
 */
public class TerminalOperatorWmsGUI implements Initializable {

    private TerminalOperatorWmsController controller;
    private UIUtils uiUtils;

    @FXML private ListView<String> listViewWarehouseLogs;

    @FXML private CheckBox checkBoxGlobal;
    @FXML private CheckBox checkBoxPicker;
    @FXML private CheckBox checkBoxPlanner;
    @FXML private CheckBox checkBoxQualityOperator;
    @FXML private CheckBox checkBoxTerminalOperator;
    @FXML private CheckBox checkBoxTrafficDispatcher;
    @FXML private CheckBox checkBoxWarehousePlanner;

    /**
     * Initializes the controller, UI utilities, and log list configuration.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new TerminalOperatorWmsController();
        uiUtils = new UIUtils();

        configureColoredLogs();
        updateLogListView();

        listViewWarehouseLogs.setSelectionModel(null);
    }

    /** Navigates back to the terminal operator home page. */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/terminalOperatorHomePage.fxml",
                "MABEC - Terminal Operator - Home Page");
    }

    /** Logs out and returns to the main home page. */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /** Opens the Unload Wagons interface. */
    @FXML
    public void wagonsButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/unloadWagons.fxml",
                "MABEC - Terminal Operator - Unload Wagons");
    }

    /** Opens the Relocation Box interface. */
    @FXML
    public void relocationBoxButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/relocateBox_Bay.fxml",
                "MABEC - Terminal Operator - Relocation Box");
    }

    /**
     * Selects all role filters and updates the log list view.
     */
    @FXML
    public void handleSelectAllButton() {
        checkBoxGlobal.setSelected(true);
        checkBoxPicker.setSelected(true);
        checkBoxPlanner.setSelected(true);
        checkBoxQualityOperator.setSelected(true);
        checkBoxTerminalOperator.setSelected(true);
        checkBoxTrafficDispatcher.setSelected(true);
        checkBoxWarehousePlanner.setSelected(true);
        controller.selectAllRoles();
        updateLogListView();
    }

    /**
     * Clears all role filters and updates the log list view.
     */
    @FXML
    public void handleClearAllButton() {
        checkBoxGlobal.setSelected(false);
        checkBoxPicker.setSelected(false);
        checkBoxPlanner.setSelected(false);
        checkBoxQualityOperator.setSelected(false);
        checkBoxTerminalOperator.setSelected(false);
        checkBoxTrafficDispatcher.setSelected(false);
        checkBoxWarehousePlanner.setSelected(false);
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
        controller.setPickerSelected(checkBoxPicker.isSelected());
        controller.setPlannerSelected(checkBoxPlanner.isSelected());
        controller.setQualityOperatorSelected(checkBoxQualityOperator.isSelected());
        controller.setTerminalOperatorSelected(checkBoxTerminalOperator.isSelected());
        controller.setTrafficDispatcherSelected(checkBoxTrafficDispatcher.isSelected());
        controller.setWarehousePlannerSelected(checkBoxWarehousePlanner.isSelected());
        updateLogListView();
    }

    /**
     * Updates the warehouse log ListView based on active role filters.
     * Automatically scrolls to the latest log entry.
     */
    private void updateLogListView() {
        listViewWarehouseLogs.getItems().clear();
        listViewWarehouseLogs.getItems().addAll(controller.getFilteredLogs());
        if (!listViewWarehouseLogs.getItems().isEmpty()) {
            listViewWarehouseLogs.scrollTo(listViewWarehouseLogs.getItems().size() - 1);
        }
    }

    /**
     * Configures the ListView to display logs with colors based on the role type.
     * Each role has a predefined color for easier visual differentiation.
     */
    private void configureColoredLogs() {
        listViewWarehouseLogs.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
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
