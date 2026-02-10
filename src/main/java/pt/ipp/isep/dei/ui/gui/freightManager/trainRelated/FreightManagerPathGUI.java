package pt.ipp.isep.dei.ui.gui.freightManager.trainRelated;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.freightManager.FreightManagerPathController;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * GUI controller for creating and assigning paths to trains.
 *
 * <p>This JavaFX controller is responsible for presenting the freights that
 * form a train and the sequence of facilities that constitute the train's
 * path. It initialises the associated controller, populates table views and
 * provides handlers for navigation and path manipulation (adding/removing
 * stations, creating the train path).</p>
 *
 * <p>The class solely manages presentation and user interaction; business
 * logic remains in {@link FreightManagerPathController}.
 */
public class FreightManagerPathGUI implements Initializable {

    private UIUtils uiUtils;
    private FreightManagerPathController controller;

    @FXML
    private TableView<Freight> freightTableView;

    @FXML
    private TableColumn<Freight, String> idFreightTableColumn, departureTableColumn, destinationTableColumn, statusTableColumn;

    @FXML
    private TableView<Facility> actualTableView, nextTableView;

    @FXML
    private TableColumn<Facility, String> idActualTableColumn, idNextTableColumn, nameActualTableColumn, nameNextTableColumn;

    @FXML
    private Label firstAStationLabel;

    /**
     * Initialise this GUI controller.
     *
     * <p>Called by the JavaFX runtime when the FXML is loaded. The method
     * initialises helper objects and the underlying controller.</p>
     *
     * @param url location used to resolve relative paths (unused)
     * @param resourceBundle resources used to localise the root object (unused)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
        controller = new FreightManagerPathController();
    }

    /**
     * Provide initial data to the controller and initialise the GUI.
     *
     * @param operator the current operator performing the action
     * @param date the date associated with the operation
     * @param time the time associated with the operation
     * @param locomotiveList list of available locomotives
     * @param freightOrderList list of freights that compose the train order
     */
    public void initControllerItems(Operator operator, Date date, Time time, List<Locomotive> locomotiveList, List<Freight> freightOrderList) {
        controller.loadControllerInfo(operator, date, time, locomotiveList, freightOrderList);
        initGuiItems();
    }

    /**
     * Initialise GUI elements and populate table content.
     */
    private void initGuiItems() {
        loadFreightItems();
        loadStationItems();
        loadPathStationItems();
    }

    /**
     * Configure the table columns that display the selected path (actual
     * stations) and populate the related controls.
     */
    private void loadPathStationItems() {
        idActualTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        nameActualTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getName()))
        );
    }

    /**
     * Configure the table columns that display candidate next stations and
     * populate the list with all available facilities.
     */
    private void loadStationItems() {
        idNextTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        nameNextTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getName()))
        );

        ObservableList<Facility> stations = FXCollections.observableArrayList(controller.getAllFacilities());
        nextTableView.setItems(stations);
    }

    /**
     * Configure the freight table columns and populate the freight list for
     * the current order.
     */
    private void loadFreightItems() {
        idFreightTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        departureTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOriginFacility().getName())
        );

        destinationTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDestinationFacility().getName())
        );

        statusTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(controller.findStatus(cellData.getValue()))
        );

        ObservableList<Freight> freightList = FXCollections.observableArrayList(controller.getFreightOrderList());
        freightTableView.setItems(freightList);
    }

    /**
     * Navigate to the Freight Manager home page.
     *
     * @param event action event that triggered the navigation
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerHomePage.fxml", "MABEC - Freight Manager - Home Page");
    }

    /**
     * Navigate to the freight trains view.
     *
     * @param event action event that triggered the navigation
     */
    @FXML
    public void trainsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerLocomotive.fxml", "MABEC - Freight Manager - Trains");
    }

    /**
     * Navigate to the route menu view.
     *
     * @param event action event that triggered the navigation
     */
    @FXML
    public void routeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/RouteRelated/freightManagerRoute.fxml", "MABEC - Freight Manager - Route");
    }

    /**
     * Open the GQS / NOS logs view.
     *
     * @param event action event that triggered the navigation
     */
    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerGQS_NOS.fxml", "MABEC - Freight Manager - GQS / NOS Logs");
    }

    /**
     * Navigate to the freight manager scheduler view.
     *
     * @param event action event that triggered the navigation
     */
    @FXML
    public void schedulerOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerScheduler.fxml", "MABEC - Freight Manager - Scheduler");
    }

    /**
     * Log out the current user and return to the public home page.
     *
     * @param event action event that triggered the logout
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Add the selected station to the current path after verification.
     *
     * @param event action event that triggered the addition
     */
    public void chooseStationOnAction(ActionEvent event) {
        if (!verificationsAdd(event)) {
            return;
        }

        Facility selected = nextTableView.getSelectionModel().getSelectedItem();
        controller.addFacility(selected);

        updateActualPath();
        updateNextStationList(selected);
        updateFreightList();
        freightTableView.refresh();
    }

    /**
     * Refresh the freight list displayed in the GUI.
     */
    private void updateFreightList() {
        ObservableList<Freight> freights =
                FXCollections.observableArrayList(controller.getFreightOrderList());
        freightTableView.setItems(freights);
    }

    /**
     * Update the list of next stations based on the supplied facility's
     * adjacent stations.
     *
     * @param facility the facility whose adjacent stations will be shown
     */
    private void updateNextStationList(Facility facility) {
        ObservableList<Facility> facilities =
                FXCollections.observableArrayList(controller.getAdjacentStations(facility));
        nextTableView.setItems(facilities);
    }

    /**
     * Restore the next station list to all available facilities.
     */
    private void updateNextStationList() {
        ObservableList<Facility> facilities =
                FXCollections.observableArrayList(controller.getAllFacilities());
        nextTableView.setItems(facilities);
    }

    /**
     * Refresh the displayed actual path (the sequence of chosen facilities).
     */
    private void updateActualPath() {
        ObservableList<Facility> facilities =
                FXCollections.observableArrayList(controller.getFacilityPath());
        actualTableView.setItems(facilities);

        if (controller.getFacilityPath().size() == 1) {
            firstAStationLabel.setText("Next Station");
        }
    }

    /**
     * Verify that a station has been selected before attempting to add it to
     * the path.
     *
     * @param event the triggering action event
     * @return {@code true} when a valid station is selected; otherwise {@code false}
     */
    private boolean verificationsAdd(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (nextTableView.getSelectionModel().getSelectedItem() == null) {
            UIUtils.showAlert(stage, "Please add a Station to add to the Path", "error");
            return false;
        }

        return true;
    }

    /**
     * Remove the last station from the current path.
     *
     * @param event the action event that triggered the removal
     */
    public void removeLastOnAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (controller.getFacilityPath().isEmpty()) {
            UIUtils.showAlert(stage, "No Station in the Path to Remove", "error");
            return;
        }

        controller.removeLastFacility();

        updateActualPath();

        if (controller.getFacilityPath().isEmpty()) {
            updateNextStationList();
        } else {
            updateNextStationList(
                controller.getFacilityPath().get(controller.getFacilityPath().size() - 1)
            );
        }

        updateFreightList();
        freightTableView.refresh();
    }

    /**
     * Create the train with the currently selected path and freights after
     * performing creation verifications.
     *
     * @param event the action event that triggered the creation
     */
    public void createPathOnAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if(!verificationsCreation(event)){
            return;
        }

        controller.createTrain();

        UIUtils.showAlert(stage, "Train Created with Route", "success");

        trainsOnAction(event);

    }

    /**
     * Verify that all freights in the current order are in the 'Delivered'
     * status before creating the train path.
     *
     * @param event the action event that triggered the verification
     * @return {@code true} if all freights are delivered; otherwise {@code false}
     */
    private boolean verificationsCreation(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        for (Freight freight: controller.getFreightOrderList()) {
            if(!controller.findStatus(freight).equals("Delivered")){
                UIUtils.showAlert(stage, "There is a Freight that is not delivered yet", "error");
                return false;
            }
        }

        return true;
    }
}
