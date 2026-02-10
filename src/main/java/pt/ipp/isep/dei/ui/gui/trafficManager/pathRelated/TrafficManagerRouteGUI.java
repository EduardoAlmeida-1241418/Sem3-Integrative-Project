package pt.ipp.isep.dei.ui.gui.trafficManager.pathRelated;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.traficManager.pathRelated.TrafficManagerRouteController;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.domain.transportationRelated.Route;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible for displaying available routes and their freight
 * details to the Traffic Manager.
 *
 * <p>This JavaFX controller initialises and populates a table of routes and
 * a detail table showing freights associated with the currently selected
 * route. It also exposes navigation handlers to other Traffic Manager
 * views and a convenience action to open the manual path composition view
 * for the selected route. All business operations are delegated to
 * {@link TrafficManagerRouteController}; this class concerns itself only
 * with presentation and user interaction.</p>
 *
 */
public class TrafficManagerRouteGUI implements Initializable {

    private TrafficManagerRouteController controller;
    private UIUtils uiUtils;

    @FXML
    private TableView<Route> routeTableView;

    @FXML
    private TableColumn<Route, String> routeIdTableColumn, freightQuantityTableColumn;

    @FXML
    private TableView<Freight> routeDetailsTableView;

    @FXML
    private TableColumn<Freight, String> freightIdTableColumn, departureStationTableColumn, arrivalStationTableColumn;

    /**
     * Initialise the controller and its helper utilities.
     *
     * <p>This method is invoked by the JavaFX runtime when the FXML view is
     * loaded. It constructs the UI helper and domain controller, and then
     * initialises the GUI elements.</p>
     *
     * @param location resource location (unused)
     * @param resources resource bundle (unused)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        uiUtils = new UIUtils();
        controller = new TrafficManagerRouteController();
        
        initGuiItems();
    }

    /**
     * Configure GUI items: route table and detail table.
     */
    private void initGuiItems() {
        initRouteTable();
        initDetailTable();
    }

    /**
     * Initialise and configure the detail table that shows freights for
     * the selected route.
     */
    private void initDetailTable() {
        freightIdTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        departureStationTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getOriginFacility().getName()))
        );

        arrivalStationTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getDestinationFacility().getName()))
        );
    }

    /**
     * Refresh the freight detail table for the supplied route.
     *
     * @param route the selected route whose freights are displayed
     */
    private void updateDetailTable(Route route){
        routeDetailsTableView.getItems().clear();
        ObservableList<Freight> freights = FXCollections.observableArrayList(route.getFreights());
        routeDetailsTableView.setItems(freights);
    }

    /**
     * Initialise and populate the route table with available routes from
     * the controller; register a selection listener to update details.
     */
    private void initRouteTable() {
        routeIdTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        freightQuantityTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getFreights().size()))
        );

        ObservableList<Route> routes = FXCollections.observableArrayList(controller.getAvailableRoutes());
        routeTableView.setItems(routes);

        routeTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                updateDetailTable(newSel);
            } else {
                routeDetailsTableView.getItems().clear();
            }
        });
    }

    /**
     * Navigate to the Paths view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void pathOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/pathRelated/trafficManagerPath.fxml", "MABEC - Traffic Manager - Paths");
    }

    /**
     * Navigate to the Assign view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void assignOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/trafficManagerAssign.fxml", "MABEC - Traffic Manager - Assign");
    }

    /**
     * Navigate to the Scheduler view.
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
     * Navigate to the Traffic Manager home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/trafficManagerHomePage.fxml", "MABEC - Traffic Manager - Home Page");
    }


    /**
     * Navigate back to the Paths view.
     *
     * @param event the action event supplied by the UI
     */
    public void backButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/pathRelated/trafficManagerPath.fxml", "MABEC - Traffic Manager - Paths");
    }

    /**
     * Open the manual path composition view for the currently selected
     * route. The method first validates that a route has been selected.
     *
     * @param event the action event supplied by the UI
     */
    public void chooseRouteButtonOnAction(ActionEvent event) {
        if (verification(event)){
            return;
        }

        FXMLLoader fxmlLoader = uiUtils.loadFXMLScene(event, "/fxml/trafficManager/pathRelated/trafficManagerPathManual.fxml", "MABEC - Traffic Manager - Paths");
        TrafficManagerPathsManualGUI trafficManagerPathsManualGUI = fxmlLoader.getController();
        trafficManagerPathsManualGUI.loadControllerInfo(routeTableView.getSelectionModel().getSelectedItem());

    }

    /**
     * Verify that a route is selected before proceeding with an action.
     *
     * @param event the action event supplied by the UI (used to obtain a Stage for alerts)
     * @return {@code true} when verification failed (no route selected), otherwise {@code false}
     */
    private boolean verification(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (routeTableView.getSelectionModel().getSelectedItem() == null){
            uiUtils.showAlert(stage, "Select a route first",  "error");
            return true;
        }
        return false;
    }
}
