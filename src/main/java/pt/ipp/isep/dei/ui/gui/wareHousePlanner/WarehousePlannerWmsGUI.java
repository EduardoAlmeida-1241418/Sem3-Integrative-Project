package pt.ipp.isep.dei.ui.gui.wareHousePlanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import pt.ipp.isep.dei.controller.wareHousePlanner.WarehousePlannerWmsController;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Warehouse Planner WMS Log screen.
 * Allows filtering and displaying of system logs based on user roles.
 */
public class WarehousePlannerWmsGUI implements Initializable {

    private WarehousePlannerWmsController controller;
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
     * Initializes the controller and sets up log filtering.
     *
     * @param url The location of the FXML file.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new WarehousePlannerWmsController();
        uiUtils = new UIUtils();

        configureColoredLogs();
        updateLogListView();

        listViewWarehouseLogs.setSelectionModel(null);
    }

    /**
     * Navigates to the Warehouse Planner Home Page.
     */
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/warehousePlanner/warehousePlannerHomePage.fxml", "MABEC - Warehouse Planner Home Page");
    }

    /**
     * Opens the Choose Order page.
     */
    public void orderButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/warehousePlanner/chooseOrder.fxml", "MABEC - Choose Order");
    }

    /**
     * Logs out and navigates to the main Home Page.
     */
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Opens the Allocation Report page.
     */
    public void allocationReportButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/warehousePlanner/orderAllocations.fxml", "MABEC - Warehouse Planner - Allocation Report");
    }

    /**
     * Selects all roles for log filtering and updates the log list.
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
     * Clears all selected roles and updates the log list.
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
     * Updates selected roles and refreshes the filtered log list.
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
     * Updates the ListView with the current filtered logs.
     */
    private void updateLogListView() {
        listViewWarehouseLogs.getItems().clear();
        listViewWarehouseLogs.getItems().addAll(controller.getFilteredLogs());
        if (!listViewWarehouseLogs.getItems().isEmpty()) {
            listViewWarehouseLogs.scrollTo(listViewWarehouseLogs.getItems().size() - 1);
        }
    }

    /**
     * Configures the log ListView to display colored text based on role type.
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
