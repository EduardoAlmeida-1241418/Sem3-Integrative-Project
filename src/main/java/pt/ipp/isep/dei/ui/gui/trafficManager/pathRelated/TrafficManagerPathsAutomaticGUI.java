package pt.ipp.isep.dei.ui.gui.trafficManager.pathRelated;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.traficManager.pathRelated.TrafficManagerPathsAutomaticController;
import pt.ipp.isep.dei.domain.Facility;
import pt.ipp.isep.dei.domain.trackRelated.RailwaySegmentType;
import pt.ipp.isep.dei.domain.trackRelated.TrackGauge;
import pt.ipp.isep.dei.domain.transportationRelated.Route;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for automatic path generation used by the Traffic Manager.
 *
 * <p>This JavaFX controller initialises the view that allows a traffic
 * manager to inspect existing routes, request an automatically computed
 * path for a chosen route (given a segment type and track gauge) and
 * commit that path to the system. It configures table views and combo
 * boxes, validates basic user actions and delegates the business
 * operations to {@link TrafficManagerPathsAutomaticController}.</p>
 *
 */
public class TrafficManagerPathsAutomaticGUI implements Initializable {

    private UIUtils uiUtils;
    private TrafficManagerPathsAutomaticController controller;

    @FXML
    private TableView<Route> routeTableView;

    @FXML
    private TableColumn<Route, String> idTableColumn, freightQtyTableColumn;

    @FXML
    private ComboBox<String> segmentTypeComboBox, gaugeComboBox;

    @FXML
    private TableView<Facility>  pathTableView;

    @FXML
    private TableColumn<Facility, String> stationIdTableColumn, stationNameTableColumn;

    @FXML
    private Label pathCostLabel;

    /**
     * Initialise the controller and supporting utilities.
     *
     * <p>Called by the JavaFX runtime when the FXML is loaded. The method
     * creates a UI helper instance and the underlying controller, then
     * configures the tables and combo boxes used by the view.</p>
     *
     * @param url location used to resolve relative paths (unused)
     * @param resourceBundle resources used to localise the root object (unused)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
        this.controller = new TrafficManagerPathsAutomaticController();

        initTable1();
        initTable2();
        initComboBox1();
        initComboBox2();
    }

    /**
     * Initialise the segment type combo box with available
     * {@link RailwaySegmentType} values.
     */
    private void initComboBox1() {
        ObservableList<String> segmentTypes = FXCollections.observableArrayList();

        for (RailwaySegmentType type : RailwaySegmentType.values()) {
            segmentTypes.add(type.toString());
        }

        segmentTypeComboBox.setItems(segmentTypes);

        if (!segmentTypes.isEmpty()) {
            segmentTypeComboBox.getSelectionModel().select(0);
        }
    }

    /**
     * Initialise the gauge combo box using the track gauges obtained
     * from the controller.
     */
    private void initComboBox2() {
        ObservableList<String> gauges = FXCollections.observableArrayList();

        for (TrackGauge trackGauge : controller.getTrackGauges()) {
            gauges.add(trackGauge.getGaugeSize() + "");
        }

        gaugeComboBox.setItems(gauges);

        if (!gauges.isEmpty()) {
            gaugeComboBox.getSelectionModel().select(0);
        }
    }

    /**
     * Configure the main routes table and populate it with available
     * {@link Route} instances retrieved from the controller.
     */
    private void initTable1() {
        idTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        freightQtyTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getFreights().size()))
        );

        ObservableList<Route> routes = FXCollections.observableArrayList(controller.getRoutes());
        routeTableView.setItems(routes);
    }

    /**
     * Configure the path table columns used to show individual facilities
     * that compose a computed path.
     */
    private void initTable2() {
        stationIdTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        stationNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getName()))
        );
    }

    /**
     * Refresh the routes table content from the controller.
     */
    private void updateTable2(){
        routeTableView.getItems().clear();

        ObservableList<Route> routes = FXCollections.observableArrayList(controller.getRoutes());
        routeTableView.setItems(routes);
    }

    /**
     * Update the displayed path table corresponding to the selected route
     * and the controller's current path. If no selection exists the table
     * is cleared.
     */
    private void updatePathTable() {
        if (routeTableView.getSelectionModel().getSelectedItem() == null || controller.getPath() == null) {
            pathTableView.getItems().clear();
            return;
        }

        ObservableList<Facility> facilities = FXCollections.observableArrayList(controller.getPath().getFacilities());
        pathTableView.setItems(facilities);
    }


    /**
     * Navigate to the Traffic Manager home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/trafficManagerHomePage.fxml", "MABEC - Traffic Manager - Home Page");
    }

    /**
     * Navigate to the path assignment view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void pathOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/pathRelated/trafficManagerPath.fxml", "MABEC - Traffic Manager - Assign");
    }

    /**
     * Navigate to the assignment screen.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void assignOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/trafficManagerAssign.fxml", "MABEC - Traffic Manager - Assign");
    }

    /**
     * Navigate to the scheduler view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void schedulerOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/scheduleRelated/trafficManagerScheduler.fxml", "MABEC - Traffic Manager - Scheduler");
    }

    /**
     * Open the NOS logs view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/trafficManagerNOS.fxml", "MABEC - Traffic Manager - NOS Logs");
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
     * Navigate back to the path assignment view.
     *
     * @param event the action event supplied by the UI
     */
    public void backButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/pathRelated/trafficManagerPath.fxml", "MABEC - Traffic Manager - Assign");
    }

    /**
     * Generate a path for the selected route using the chosen segment
     * type and track gauge.
     *
     * <p>The method validates that a route has been selected, delegates
     * generation to the controller and displays an informative alert if no
     * feasible path exists. On success the path cost is shown and the path
     * table updated.</p>
     *
     * @param event the action event supplied by the UI
     */
    public void generateOnAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (routeTableView.getSelectionModel().getSelectedItem() == null) {
            uiUtils.showAlert(stage, "Select a route first",  "error");
            controller.setSelectedRoute(null);
            updatePathTable();
            pathCostLabel.setText("Path Cost: ");
            return;
        }

        TrackGauge trackGauge = controller.findTrackGauge(gaugeComboBox.getSelectionModel().getSelectedItem());
        controller.generatePath(routeTableView.getSelectionModel().getSelectedItem(), RailwaySegmentType.getByDescription(segmentTypeComboBox.getSelectionModel().getSelectedItem()), trackGauge );

        if (controller.getPath().getFacilities().isEmpty()) {
            uiUtils.showAlert(stage, "There is no Possible Path to fill this Route",  "error");

            return;
        }


        pathCostLabel.setText("Path Cost: " + controller.getPathCost());

        updatePathTable();
    }

    /**
     * Create the path in the system after verifying that a generated path
     * and a selected route are present.
     *
     * @param event the action event supplied by the UI
     */
    public void createPathOnAction(ActionEvent event) {

        if (controller.getPath() == null || controller.getPath().getFacilities().isEmpty() || controller.getSelectedRoute() == null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            uiUtils.showAlert(stage, "Generate a Path first",  "error");
            return;
        }

        controller.createPath();

        controller.setPath(null);
        controller.setSelectedRoute(null);
        updatePathTable();
        updateTable2();
        pathCostLabel.setText("Path Cost: ");

    }


    /**
     * Show a short informational popup describing the cost components used
     * by the path cost calculation.
     *
     * @param event the action event supplied by the UI
     */
    public void infoButonOnAction(ActionEvent event) {
        uiUtils.showInfoPopup(event,
                "- Base Cost: Sum of all railway segment lengths.\n"
                        + "- Double Track Buff: 20% cost reduction.\n"
                        + "- Siding Buff: 10% cost reduction.\n"
                        + "- No Improvements: Full cost applied.",6);
    }
}