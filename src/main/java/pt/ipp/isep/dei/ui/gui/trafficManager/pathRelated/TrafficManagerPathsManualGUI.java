package pt.ipp.isep.dei.ui.gui.trafficManager.pathRelated;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.traficManager.pathRelated.TrafficManagerPathsManualController;
import pt.ipp.isep.dei.domain.Facility;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.domain.transportationRelated.Route;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for manual path composition used by the Traffic Manager.
 *
 * <p>This JavaFX controller allows a traffic manager to inspect freight
 * orders, view available stations and assemble a path for a selected
 * route using explicit user actions (double‑click to add/remove
 * stations). The controller configures table views, initialises the
 * interaction behaviour and delegates business operations to
 * {@link TrafficManagerPathsManualController}.</p>
 *
 */
public class TrafficManagerPathsManualGUI implements Initializable {

    private UIUtils uiUtils;
    private TrafficManagerPathsManualController controller;

    @FXML
    private TableView<Freight> freightTableView;

    @FXML
    private TableColumn<Freight, String> idFreightTableColumn,
            departureTableColumn, destinationTableColumn, statusTableColumn;

    @FXML
    private TableView<Facility> actualTableView, nextTableView;

    @FXML
    private TableColumn<Facility, String> idActualTableColumn,
            idNextTableColumn, nameActualTableColumn, nameNextTableColumn;

    @FXML
    private Label firstAStationLabel;

    /**
     * Initialise the controller and supporting helpers.
     *
     * <p>Invoked by the JavaFX runtime when the FXML view is loaded. This
     * method creates a UI helper instance and the underlying controller,
     * then registers double‑click behaviours required by the view.</p>
     *
     * @param url resource location (unused)
     * @param resourceBundle resources for localisation (unused)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
        this.controller = new TrafficManagerPathsManualController();

        configureDoubleClicks();
    }

    /**
     * Load controller information, for example the chosen route, and
     * initialise GUI items accordingly.
     *
     * @param route the chosen {@link Route} instance supplied by the caller
     */
    public void loadControllerInfo(Route route) {
        controller.setChosenRoute(route);
        initGuiItems();
    }

    /**
     * Initialise GUI items (freight list, station lists and current path).
     */
    private void initGuiItems() {
        loadFreightItems();
        loadStationItems();
        loadPathStationItems();
    }

    /* =====================================================
                        DOUBLE CLICKS
       ===================================================== */

    /**
     * Register mouse double‑click handlers for the station tables.
     *
     * <p>A double‑click on the "next" table adds the selected station to
     * the current path; a double‑click on the "actual" path table removes
     * the last station from the path.</p>
     */
    private void configureDoubleClicks() {

        // ADD station → double click on NEXT table
        nextTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                addStationByDoubleClick();
            }
        });

        // REMOVE last station → double click on ACTUAL path
        actualTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                removeLastStationByDoubleClick();
            }
        });
    }

    /**
     * Add the station currently selected in the "next" table to the
     * controller's path and refresh the UI.
     */
    private void addStationByDoubleClick() {
        Facility selected = nextTableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Stage stage = (Stage) nextTableView.getScene().getWindow();
            UIUtils.showAlert(stage, "Please double-click a Station to add", "error");
            return;
        }

        controller.addFacility(selected);

        updateActualPath();
        updateNextStationList(selected);
        updateFreightList();
        freightTableView.refresh();
    }

    /**
     * Remove the last station from the current path when the user
     * double‑clicks the actual path table; refresh the UI afterwards.
     */
    private void removeLastStationByDoubleClick() {
        if (controller.getFacilityPath().isEmpty()) {
            Stage stage = (Stage) actualTableView.getScene().getWindow();
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

    /* =====================================================
                        LOADERS
       ===================================================== */

    /**
     * Configure the columns for the path (actual) station table.
     */
    private void loadPathStationItems() {
        idActualTableColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(String.valueOf(cd.getValue().getId()))
        );
        nameActualTableColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getName())
        );
    }

    /**
     * Configure the columns for the available station table and populate
     * it with all facilities obtained from the controller.
     */
    private void loadStationItems() {
        idNextTableColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(String.valueOf(cd.getValue().getId()))
        );
        nameNextTableColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getName())
        );

        nextTableView.setItems(
                FXCollections.observableArrayList(controller.getAllFacilities())
        );
    }

    /**
     * Configure the freight table columns and populate the freight order
     * list obtained from the controller.
     */
    private void loadFreightItems() {
        idFreightTableColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(String.valueOf(cd.getValue().getId()))
        );
        departureTableColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getOriginFacility().getName())
        );
        destinationTableColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getDestinationFacility().getName())
        );
        statusTableColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(controller.findStatus(cd.getValue()))
        );

        freightTableView.setItems(
                FXCollections.observableArrayList(controller.getFreightOrderList())
        );
    }

    /* =====================================================
                        UPDATES
       ===================================================== */

    /**
     * Refresh the freight table with the current order list.
     */
    private void updateFreightList() {
        freightTableView.setItems(
                FXCollections.observableArrayList(controller.getFreightOrderList())
        );
    }

    /**
     * Refresh the actual path table and update the helper label when the
     * path contains a single element.
     */
    private void updateActualPath() {
        actualTableView.setItems(
                FXCollections.observableArrayList(controller.getFacilityPath())
        );

        if (controller.getFacilityPath().size() == 1) {
            firstAStationLabel.setText("Next Station");
        }
    }

    /**
     * Update the list of adjacent stations for the supplied facility.
     *
     * @param facility the facility whose adjacent stations are requested
     */
    private void updateNextStationList(Facility facility) {
        nextTableView.setItems(
                FXCollections.observableArrayList(controller.getAdjacentStations(facility))
        );
    }

    /**
     * Reset the available station list to contain all facilities.
     */
    private void updateNextStationList() {
        nextTableView.setItems(
                FXCollections.observableArrayList(controller.getAllFacilities())
        );
    }

    /* =====================================================
                        NAVIGATION
       ===================================================== */

    /**
     * Navigate to the Traffic Manager home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,
                "/fxml/trafficManager/trafficManagerHomePage.fxml",
                "MABEC - Traffic Manager - Home Page");
    }

    /**
     * Navigate to the path assignment view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void pathOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,
                "/fxml/trafficManager/pathRelated/trafficManagerPath.fxml",
                "MABEC - Traffic Manager - Assign");
    }

    /**
     * Navigate to the assignment screen.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void assignOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,
                "/fxml/trafficManager/trafficManagerAssign.fxml",
                "MABEC - Traffic Manager - Assign");
    }

    /**
     * Navigate to the scheduler view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void schedulerOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,
                "/fxml/trafficManager/scheduleRelated/trafficManagerScheduler.fxml",
                "MABEC - Traffic Manager - Scheduler");
    }

    /**
     * Open the NOS logs view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,
                "/fxml/trafficManager/trafficManagerNOS.fxml",
                "MABEC - Traffic Manager - NOS Logs");
    }

    /**
     * Log out the current user and return to the public home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,
                "/fxml/visitorMenu/homePage.fxml",
                "MABEC - Home Page");
    }

    /**
     * Navigate back to the path assignment view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void backButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,
                "/fxml/trafficManager/pathRelated/trafficManagerPath.fxml",
                "MABEC - Traffic Manager - Assign");
    }

    /**
     * Assign the currently composed path to the selected route after
     * verifying preconditions; shows a success alert when the assignment
     * completes and navigates back to the path view.
     *
     * @param event the action event supplied by the UI
     */
    public void assignPathOnAction(ActionEvent event) {

        if (!verificationsCreation(event)){
            return;
        }

        controller.assignPath();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        UIUtils.showAlert(stage, "Path Assigned to Route with Success", "success");

        pathOnAction(event);
    }

    /**
     * Verify that all preconditions for creating a path are satisfied.
     *
     * <p>The method checks that all freights in the order list are
     * delivered; when an undelivered freight is found an error alert is
     * shown and {@code false} is returned.</p>
     *
     * @param event the action event supplied by the UI
     * @return {@code true} when creation may proceed, otherwise {@code false}
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

    /**
     * Show a brief informational popup describing double‑click behaviours
     * and constraints related to freight management within the view.
     *
     * @param event the action event supplied by the UI
     */
    public void infoButonOnAction(ActionEvent event) {
        uiUtils.showInfoPopup(event, "Double-Click do add Freights from Freight List .\n"
                + "Double-Click to remove freights from Freight Order.\n"
                + "\nCannot add freight with the same wagons", 3);
    }
}
