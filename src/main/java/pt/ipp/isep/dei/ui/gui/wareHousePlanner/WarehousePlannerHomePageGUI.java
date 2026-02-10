package pt.ipp.isep.dei.ui.gui.wareHousePlanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Warehouse Planner Home Page.
 * Provides navigation between different functional screens for the warehouse planner role.
 */
public class WarehousePlannerHomePageGUI implements Initializable {

    private UIUtils uiUtils;

    /**
     * Initializes the UI utility instance.
     *
     * @param url The location of the FXML file.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uiUtils = new UIUtils();
    }

    /**
     * Opens the Choose Order page.
     *
     * @param event The action event triggered by the button click.
     */
    @FXML
    public void orderButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/warehousePlanner/chooseOrder.fxml", "MABEC - Choose Order");
    }

    /**
     * Logs out and navigates to the Home Page.
     *
     * @param event The action event triggered by the button click.
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Opens the Allocation Report page.
     *
     * @param event The action event triggered by the button click.
     */
    @FXML
    public void allocationReportButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/warehousePlanner/orderAllocations.fxml", "MABEC - Warehouse Planner - Allocation Report");
    }

    /**
     * Opens the Warehouse Planner WMS Log page.
     *
     * @param event The action event triggered by the button click.
     */
    @FXML
    public void handleWmsLogButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/warehousePlanner/warehousePlannerWMS.fxml", "MABEC - Warehouse Planner - WMS Log");
    }
}
