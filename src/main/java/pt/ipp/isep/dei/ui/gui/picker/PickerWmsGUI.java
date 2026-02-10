package pt.ipp.isep.dei.ui.gui.picker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import pt.ipp.isep.dei.controller.picker.PickerWmsController;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Picker WMS Log page.
 * Manages log display, role-based filtering, and navigation between picker views.
 */
public class PickerWmsGUI implements Initializable {

    private PickerWmsController controller;
    private UIUtils uiUtils;

    @FXML
    private ListView<String> listViewWarehouseLogs;

    @FXML private CheckBox checkBoxGlobal;
    @FXML private CheckBox checkBoxPicker;
    @FXML private CheckBox checkBoxPlanner;
    @FXML private CheckBox checkBoxQualityOperator;
    @FXML private CheckBox checkBoxTerminalOperator;
    @FXML private CheckBox checkBoxTrafficDispatcher;
    @FXML private CheckBox checkBoxWarehousePlanner;

    /**
     * Initializes the GUI, sets up controllers, configures colored log display, and populates the initial log list.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new PickerWmsController();
        uiUtils = new UIUtils();

        configureColoredLogs();
        updateLogListView();

        listViewWarehouseLogs.setSelectionModel(null);
    }

    /**
     * Navigates to the Picker Home Page.
     *
     * @param event the action event triggered by the Home button
     */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/picker/pickerHomePage.fxml", "MABEC - Picker - Home Page");
    }

    /**
     * Navigates to the Picking Path page.
     *
     * @param event the action event triggered by the Picking Path button
     */
    @FXML
    public void pickingPathButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/picker/pickingPath.fxml", "MABEC - Picker - Picking Path");
    }

    /**
     * Logs out the current user and navigates to the main Home Page.
     *
     * @param event the action event triggered by the Logout button
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Selects all role checkboxes and displays all logs.
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
     * Clears all role checkboxes and hides all logs.
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
     * Updates role filters based on selected checkboxes and refreshes the log list.
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
     * Refreshes the log list based on the selected roles and scrolls to the latest entry.
     */
    private void updateLogListView() {
        listViewWarehouseLogs.getItems().clear();
        listViewWarehouseLogs.getItems().addAll(controller.getFilteredLogs());
        if (!listViewWarehouseLogs.getItems().isEmpty()) {
            listViewWarehouseLogs.scrollTo(listViewWarehouseLogs.getItems().size() - 1);
        }
    }

    /**
     * Configures the ListView to color each log line according to its corresponding role type.
     * Uses RGB conversion from JavaFX Color objects for text styling.
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
                            (int)(color.getRed() * 255),
                            (int)(color.getGreen() * 255),
                            (int)(color.getBlue() * 255));
                    setStyle("-fx-text-fill: " + rgb + "; -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
                }
            }
        });
    }
}
