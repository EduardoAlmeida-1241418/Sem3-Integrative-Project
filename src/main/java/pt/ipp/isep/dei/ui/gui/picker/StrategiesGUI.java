package pt.ipp.isep.dei.ui.gui.picker;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pt.ipp.isep.dei.controller.picker.StrategiesController;
import pt.ipp.isep.dei.domain.pickingPath.PickingPath;
import pt.ipp.isep.dei.domain.pickingPath.PickingPathReport;
import pt.ipp.isep.dei.domain.pickingPath.PathPoint;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for displaying and comparing two picking path strategies (A and B).
 * Shows path details such as trolley ID, aisle, bay, and distance between points.
 */
public class StrategiesGUI implements Initializable {

    private UIUtils uiUtils;
    private StrategiesController controller;

    @FXML
    private TableView<PathPoint> orderTableView, orderTableView2;

    @FXML
    private TableColumn<PathPoint, String> trolley1TableColumn, trolley2TableColumn;

    @FXML
    private TableColumn<PathPoint, String> aisle1TableColumn, aisle2TableColumn;

    @FXML
    private TableColumn<PathPoint, String> bay1TableColumn, bay2TableColumn;

    @FXML
    private TableColumn<PathPoint, String> quantity1TableColumn, quantity2TableColumn;

    @FXML
    private Label stratATotalDistanceLabel, stratBTotalDistanceLabel;

    /**
     * Initializes the controller and utility class.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uiUtils = new UIUtils();
        controller = new StrategiesController();
    }

    /**
     * Sets picking path reports for both strategies and loads the data into tables and labels.
     *
     * @param pickingPathReportA the report generated using strategy A
     * @param pickingPathReportB the report generated using strategy B
     */
    public void setData(PickingPathReport pickingPathReportA, PickingPathReport pickingPathReportB) {
        controller.setData(pickingPathReportA, pickingPathReportB);
        loadTableGUI();
        loadLabelGUI();
    }

    /**
     * Displays the total distance for each picking path strategy.
     */
    private void loadLabelGUI() {
        stratATotalDistanceLabel.setText(String.valueOf(controller.getTotalDistance(controller.getPickingPathReportA())));
        stratBTotalDistanceLabel.setText(String.valueOf(controller.getTotalDistance(controller.getPickingPathReportB())));
    }

    /**
     * Populates both strategy tables with path point data.
     * Each table shows path details such as trolley, aisle, bay, and distances.
     */
    private void loadTableGUI() {
        // Trolley ID
        trolley1TableColumn.setCellValueFactory(cellData -> {
            PathPoint pathPoint = cellData.getValue();
            for (PickingPath path : controller.getPickingPathReportA().getPickingPaths()) {
                if (path.getPathPointList().contains(pathPoint)) {
                    return new SimpleStringProperty(path.getTrolley().getTrolleyId());
                }
            }
            return new SimpleStringProperty("");
        });

        trolley2TableColumn.setCellValueFactory(cellData -> {
            PathPoint pathPoint = cellData.getValue();
            for (PickingPath path : controller.getPickingPathReportB().getPickingPaths()) {
                if (path.getPathPointList().contains(pathPoint)) {
                    return new SimpleStringProperty(path.getTrolley().getTrolleyId());
                }
            }
            return new SimpleStringProperty("");
        });

        // Aisle
        aisle1TableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(controller.getAisleNumber(
                        cellData.getValue().getBoxList().getFirst()))
        );
        aisle2TableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(controller.getAisleNumber(
                        cellData.getValue().getBoxList().getFirst()))
        );

        // Bay
        bay1TableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(controller.getBayNumber(
                        cellData.getValue().getBoxList().getFirst())))
        );
        bay2TableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(controller.getBayNumber(
                        cellData.getValue().getBoxList().getFirst())))
        );

        // Distance from previous
        quantity1TableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getDistanceFromPrevious()))
        );

        quantity2TableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getDistanceFromPrevious()))
        );

        // Fill tables with PathPoints from all PickingPaths
        orderTableView.getItems().clear();
        orderTableView2.getItems().clear();

        for (PickingPath path : controller.getPickingPathReportA().getPickingPaths()) {
            orderTableView.getItems().addAll(path.getPathPointList());
        }

        for (PickingPath path : controller.getPickingPathReportB().getPickingPaths()) {
            orderTableView2.getItems().addAll(path.getPathPointList());
        }
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
     * Returns to the previous page (Picking Path page).
     *
     * @param event the action event triggered by the Back button
     */
    @FXML
    public void backButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/picker/pickingPath.fxml", "MABEC - Picker - Picking Path");
    }

    /**
     * Navigates to the Picker WMS Log page.
     *
     * @param event the action event triggered by the WMS Log button
     */
    @FXML
    public void handleWmsLogButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/picker/pickerWMS.fxml", "MABEC - Picker - WMS Log");
    }
}
