package pt.ipp.isep.dei.ui.gui.wareHousePlanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pt.ipp.isep.dei.controller.wareHousePlanner.OrderAllocationsController;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the "Order Allocations" GUI.
 * Displays a list of order lines and their allocation status,
 * allowing users to view detailed box allocation information.
 */
public class OrderAllocationsGUI implements Initializable {

    private UIUtils uiUtils;
    private OrderAllocationsController controller;

    @FXML private Label boxQuantitylabel;
    @FXML private TableView<OrderLine> orderLinesTable;
    @FXML private TableColumn<OrderLine, String> LineNoTableColumn;
    @FXML private TableColumn<OrderLine, String> SKUTableColumn;
    @FXML private TableColumn<OrderLine, String> QtyTableColumn;
    @FXML private TableColumn<OrderLine, String> statusTableColumn;

    /**
     * Initializes the controller, loads order allocation data, and sets up listeners.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uiUtils = new UIUtils();
        controller = new OrderAllocationsController();
        loadGuiInfo();

        orderLinesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> updateLabels(newValue)
        );
    }

    /**
     * Loads the GUI data, including label and table content.
     */
    public void loadGuiInfo() {
        loadLabelInfo();
        loadTableInfo();
    }

    /**
     * Populates the table with order line data and sorts it by status.
     */
    private void loadTableInfo() {
        LineNoTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getOrderLineId()))
        );

        SKUTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getSkuItem()))
        );

        QtyTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAllocatedQuantity()))
        );

        statusTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getRealStatus().getState().toString()))
        );

        statusTableColumn.setSortType(TableColumn.SortType.ASCENDING);

        ObservableList<OrderLine> orderLines = FXCollections.observableArrayList(controller.getOrderLines());
        orderLinesTable.setItems(orderLines);

        orderLinesTable.getSortOrder().clear();
        orderLinesTable.getSortOrder().add(statusTableColumn);
        orderLinesTable.sort();
    }

    /**
     * Initializes the label with default values.
     */
    private void loadLabelInfo() {
        boxQuantitylabel.setText("?");
    }

    /**
     * Updates label data based on the selected order line.
     *
     * @param selectedLine The currently selected order line.
     */
    private void updateLabels(OrderLine selectedLine) {
        if (selectedLine != null) {
            boxQuantitylabel.setText(String.valueOf(controller.getTotalAllocatedUnits(selectedLine)));
        } else {
            boxQuantitylabel.setText("?");
        }
    }

    /**
     * Navigates back to the Warehouse Planner Home Page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void backButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/warehousePlanner/warehousePlannerHomePage.fxml",
                "MABEC - Warehouse Planner Home Page");
    }

    /**
     * Navigates to the Warehouse Planner Home Page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/warehousePlanner/warehousePlannerHomePage.fxml",
                "MABEC - Warehouse Planner Home Page");
    }

    /**
     * Logs out and returns to the Visitor Home Page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Opens the "Choose Order" page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void orderButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/warehousePlanner/chooseOrder.fxml", "MABEC - Choose Order");
    }

    /**
     * Opens the Warehouse Planner WMS Log page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void handleWmsLogButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/warehousePlanner/warehousePlannerWMS.fxml",
                "MABEC - Warehouse Planner - WMS Log");
    }

    /**
     * Opens the "View Box Allocation" page for the selected order line.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void selectButtonOnAction(ActionEvent event) {
        FXMLLoader loader = uiUtils.loadFXMLScene(event, "/fxml/warehousePlanner/viewBoxAllocation.fxml",
                "MABEC - Choose Order");
        ViewBoxAllocationGUI controller = loader.getController();
        controller.loadControllerInfo(orderLinesTable.getSelectionModel().getSelectedItem());
    }
}
