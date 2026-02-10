package pt.ipp.isep.dei.ui.gui.trafficDispatcher;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import pt.ipp.isep.dei.controller.trafficDispatcher.TrafficDispatcherWmsController;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Traffic Dispatcher WMS Logs GUI.
 * Allows traffic dispatchers to view and filter WMS logs based on user roles.
 * Displays logs in color according to their associated role type.
 */
public class TrafficDispatcherWmsGUI implements Initializable {

    private TrafficDispatcherWmsController controller;
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
     * Initializes the controller, UI utilities, and configures the log list.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new TrafficDispatcherWmsController();
        uiUtils = new UIUtils();

        configureColoredLogs();
        updateLogListView();

        listViewWarehouseLogs.setSelectionModel(null);
    }

    /** Navigates to the Traffic Dispatcher home page. */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficDispatcher/trafficDispatcherHomePage.fxml",
                "MABEC - Traffic Dispatcher - Home Page");
    }

    /** Opens the Estimated Travel interface. */
    @FXML
    public void estimatedTravelButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficDispatcher/estimatedTravel.fxml",
                "MABEC - Traffic Dispatcher - Estimated Travel");
    }

    /** Logs out and returns to the main home page. */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml",
                "MABEC - Home Page");
    }

    /**
     * Selects all role filters and updates the displayed logs.
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
     * Clears all role filters and refreshes the displayed logs.
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
     * Updates the role filter state based on the user's checkbox selections.
     * Refreshes the list of displayed logs accordingly.
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
     * Updates the ListView with logs filtered by the selected roles.
     * Automatically scrolls to the latest entry if available.
     */
    private void updateLogListView() {
        listViewWarehouseLogs.getItems().clear();
        listViewWarehouseLogs.getItems().addAll(controller.getFilteredLogs());
        if (!listViewWarehouseLogs.getItems().isEmpty()) {
            listViewWarehouseLogs.scrollTo(listViewWarehouseLogs.getItems().size() - 1);
        }
    }

    /**
     * Configures colored log entries in the ListView based on their role type.
     * Each log displays in a distinct color tied to its originating role.
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
