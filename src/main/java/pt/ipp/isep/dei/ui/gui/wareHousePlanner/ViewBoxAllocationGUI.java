package pt.ipp.isep.dei.ui.gui.wareHousePlanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pt.ipp.isep.dei.controller.wareHousePlanner.ViewBoxAllocationController;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for displaying detailed box allocation information
 * related to a specific order line in the Warehouse Planner module.
 */
public class ViewBoxAllocationGUI implements Initializable {

    private ViewBoxAllocationController controller;
    private UIUtils uiUtils;

    @FXML
    private TableView<Box> boxTable;

    @FXML
    private TableColumn<Box, String> boxIDTableColumn;

    @FXML
    private TableColumn<Box, String> aisleIdTableColumn;

    @FXML
    private TableColumn<Box, String> bayTableColumn;

    @FXML
    private TableColumn<Box, String> allocatedQuantityTableColumn;

    /**
     * Initializes controller and utilities.
     *
     * @param url The location of the FXML file.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new ViewBoxAllocationController();
        uiUtils = new UIUtils();
    }

    /**
     * Loads order line data into the controller and initializes the GUI.
     *
     * @param orderLine The selected order line to display its box allocations.
     */
    public void loadControllerInfo(OrderLine orderLine){
        controller.setData(orderLine);
        initGuiInfo();
    }

    /**
     * Initializes all GUI components.
     */
    private void initGuiInfo() {
        initTableGuiInfo();
    }

    /**
     * Initializes and populates the box allocation table with relevant data.
     */
    private void initTableGuiInfo() {
        boxIDTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getBoxId())));
        aisleIdTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(controller.getAisleID(cellData.getValue().getBayId()))));
        bayTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getBayId())));
        allocatedQuantityTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(controller.getItemQtyInSameBox(cellData.getValue()))));

        boxTable.getItems().clear();
        boxTable.getItems().addAll(controller.getBoxes());
    }

    /**
     * Navigates back to the Allocation Report screen.
     */
    public void backButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/warehousePlanner/orderAllocations.fxml", "MABEC - Warehouse Planner - Allocation Report");
    }

    /**
     * Navigates to the Warehouse Planner Home Page.
     */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/warehousePlanner/warehousePlannerHomePage.fxml", "MABEC - Warehouse Planner - Home Page");
    }

    /**
     * Logs out and returns to the main Visitor Home Page.
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Navigates to the Choose Order page.
     */
    @FXML
    public void orderButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/warehousePlanner/chooseOrder.fxml", "MABEC - Choose Order");
    }

    /**
     * Navigates to the Order Allocation Report screen.
     */
    @FXML
    public void allocationReportButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/warehousePlanner/orderAllocations.fxml", "MABEC - Warehouse Planner - Order Allocation");
    }

    /**
     * Navigates to the Warehouse Planner WMS Log screen.
     */
    @FXML
    public void handleWmsLogButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/warehousePlanner/warehousePlannerWMS.fxml", "MABEC - Warehouse Planner - WMS Log");
    }
}
