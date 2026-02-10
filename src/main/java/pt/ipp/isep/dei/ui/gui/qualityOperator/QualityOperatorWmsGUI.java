package pt.ipp.isep.dei.ui.gui.qualityOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import pt.ipp.isep.dei.controller.qualityOperator.QualityOperatorWmsController;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Quality Operator WMS Log page.
 * Manages log filtering by role type and provides navigation between quality operator views.
 */
public class QualityOperatorWmsGUI implements Initializable {

    private QualityOperatorWmsController controller;
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
     * Initializes the controller, configures colored log display,
     * and populates the log list with initial data.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new QualityOperatorWmsController();
        uiUtils = new UIUtils();

        configureColoredLogs();
        updateLogListView();

        listViewWarehouseLogs.setSelectionModel(null);
    }

    /**
     * Navigates to the Quality Operator Home Page.
     *
     * @param event the action event triggered by the Home button
     */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/qualityOperator/qualityOperatorHomePage.fxml", "MABEC - Quality Operator - Home Page");
    }

    /**
     * Navigates to the Quarantine Return page.
     *
     * @param actionEvent the action event triggered by the Quarantine button
     */
    @FXML
    public void handleQuarantine(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/qualityOperator/quarantineReturn.fxml", "MABEC - Quality Operator - Quarantine");
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
     * Selects all role filters and refreshes the log display.
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
     * Clears all selected role filters and refreshes the log display.
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
     * Updates controller filters based on checkbox selections and refreshes the log display.
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
     * Updates the ListView with filtered logs and scrolls to the latest log entry.
     */
    private void updateLogListView() {
        listViewWarehouseLogs.getItems().clear();
        listViewWarehouseLogs.getItems().addAll(controller.getFilteredLogs());
        if (!listViewWarehouseLogs.getItems().isEmpty()) {
            listViewWarehouseLogs.scrollTo(listViewWarehouseLogs.getItems().size() - 1);
        }
    }

    /**
     * Configures the log list to display colored text based on role type.
     * Each role's logs are styled with their associated color.
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
