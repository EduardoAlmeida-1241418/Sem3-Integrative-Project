package pt.ipp.isep.dei.ui.gui.freightManager.routeRelated;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pt.ipp.isep.dei.controller.freightManager.routeRelated.ViewRouteController;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.domain.transportationRelated.Route;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for viewing routes and their detailed freights.
 *
 * <p>This JavaFX controller initialises and populates the tables used to
 * inspect existing routes and the freights that constitute each route. It
 * delegates data retrieval and business behaviour to
 * {@link ViewRouteController} and scene navigation to {@link UIUtils}.</p>
 *
 */
public class ViewRouteGUI implements Initializable {

    private UIUtils uiUtils;
    private ViewRouteController controller;

    @FXML
    private TableView<Route> routeTableView;

    @FXML
    private TableColumn<Route, String> iDRouteTableColumn, freightQuantityTableColumn;

    @FXML
    private TableView<Freight> detailTableView;

    @FXML
    private TableColumn<Freight, String> freightIdTableColumn, stationTableColumn, wagonQuantityTableColumn;

    @FXML
    private Label assignedPathLabel;

    /**
     * Initialise the GUI controller and supporting utilities.
     *
     * @param url not used by this implementation
     * @param resourceBundle not used by this implementation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
        this.controller = new ViewRouteController();

        loadGUIInfo();
    }

    /**
     * Populate the GUI components with initial data.
     *
     * <p>This method initialises the routes table and the details table.</p>
     */
    private void loadGUIInfo() {
        loadRouteTable();
        initDetailTable();
    }


    /**
     * Configure and populate the routes table.
     *
     * <p>Each column is configured to display a textual representation of the
     * route's identifier and the number of freights. A selection listener
     * updates the details table when a route is chosen.</p>
     */
    private void loadRouteTable() {

        iDRouteTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        freightQuantityTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getFreights().size()))
        );

        ObservableList<Route> routeList = FXCollections.observableArrayList(controller.getRoutes());
        routeTableView.setItems(routeList);

        routeTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                        updateDetails(newValue)
        );
    }

    /**
     * Initialise the detail table that shows freights belonging to the
     * currently selected route.
     *
     * <p>Column factories format freight identifiers, origin/destination
     * station names and a compact listing of wagon identifiers.</p>
     */
    private void initDetailTable() {
        freightIdTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        stationTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf("Departure: " + cellData.getValue().getOriginFacility().getName() +
                        "\nDestination: " + cellData.getValue().getDestinationFacility().getName()))
        );

        wagonQuantityTableColumn.setCellValueFactory(cellData -> {
            var wagons = cellData.getValue().getWagons();

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < wagons.size(); i++) {
                if (i > 0) {
                    if (i % 3 == 0) {
                        sb.append("\n");
                    } else {
                        sb.append(", ");
                    }
                }
                sb.append(wagons.get(i).getWagonID());
            }

            String ids = sb.length() == 0 ? "None" : sb.toString();
            String result = ids + " (" + wagons.size() + ")";

            return new SimpleStringProperty(result);
        });

    }

    /**
     * Update the details table and assigned-path label for the selected
     * route.
     *
     * @param selectedRoute the route currently selected in the routes table
     */
    private void updateDetails(Route selectedRoute){
        ObservableList<Freight> freightList = FXCollections.observableArrayList(selectedRoute.getFreights());

        detailTableView.setItems(freightList);

        if(selectedRoute.getPath() == null){
            assignedPathLabel.setText("No path assigned");
        } else {
            assignedPathLabel.setText("Has path assigned");
        }
    }

    /**
     * Navigate to the Freight Manager home page.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerHomePage.fxml", "MABEC - Freight Manager - Home Page");
    }

    /**
     * Navigate to the trains view.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void trainsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/TrainRelated/freightManagerLocomotive.fxml", "MABEC - Freight Manager - Trains");
    }

    /**
     * Navigate to the route menu view.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void routeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/RouteRelated/freightManagerRoute.fxml", "MABEC - Freight Manager - Route");
    }

    /**
     * Navigate back to the route menu view.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void backButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/RouteRelated/freightManagerRoute.fxml", "MABEC - Freight Manager - Route");
    }

    /**
     * Navigate to the GQS / NOS logs view for freight management.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerGQS_NOS.fxml", "MABEC - Freight Manager - GQS / NOS Logs");
    }

    /**
     * Navigate to the scheduler view for freight management.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void schedulerOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerScheduler.fxml", "MABEC - Freight Manager - Scheduler");
    }

    /**
     * Log out the current user and return to the public home page.
     *
     * @param event the action event that triggered the logout
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }
}
