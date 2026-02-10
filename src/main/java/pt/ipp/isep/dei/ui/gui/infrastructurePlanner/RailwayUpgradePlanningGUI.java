package pt.ipp.isep.dei.ui.gui.infrastructurePlanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pt.ipp.isep.dei.controller.infrastructurePlanner.RailwayUpgradePlanningController;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Railway Upgrade Planning user interface.
 *
 * <p>This JavaFX controller initialises widgets, configures the stations
 * table and provides handlers for computing upgrade plans and for simple
 * navigation between Infrastructure Planner views. The controller delegates
 * the actual planning logic to {@link RailwayUpgradePlanningController} and
 * confines itself to presentation and user interaction.</p>
 *
 */
public class RailwayUpgradePlanningGUI implements Initializable {

    private RailwayUpgradePlanningController controller;
    private UIUtils uiUtils;

    @FXML
    private TableView<StationEsinf> stationsTable;

    @FXML
    private TableColumn<StationEsinf, String> stationNameColumn, stationIDColumn;


    @FXML
    private ChoiceBox<String> filterChoiceBox;

    @FXML
    private TextField fromIdField;

    @FXML
    private TextField toIdField;

    @FXML
    private TextField singleStationIdField;

    @FXML
    private Label fromLabel;

    @FXML
    private Label toLabel;

    @FXML
    private Label stationIDLabel;

    @FXML
    private Label resultTypeLabel;

    /**
     * Initialise the controller and prepare the UI.
     *
     * <p>This method is invoked by the JavaFX runtime when the FXML view is
     * loaded. It constructs the underlying controller, prepares the stations
     * table, initialises the filter choice box and triggers an initial
     * computation of the plan so that the UI displays data immediately.</p>
     *
     * @param url not used by this implementation
     * @param resourceBundle not used by this implementation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new RailwayUpgradePlanningController();
        uiUtils = new UIUtils();
        configStationsTableView();
        setupComboBox();
        handleComputePlan();
    }

    /**
     * Configure the stations table columns.
     *
     * <p>Each column is configured with a cell value factory that extracts a
     * human-readable value from {@link StationEsinf} instances.</p>
     */
    private void configStationsTableView() {
        stationNameColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getStationName()));
        stationIDColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getId()));
        stationsTable.setSelectionModel(null);
    }

    /**
     * Initialise and configure the filter choice box.
     *
     * <p>The choice box offers three modes: the original graph, an ID
     * interval and a single-station analysis. A listener updates the
     * visibility of the input fields whenever the selection changes.</p>
     */
    private void setupComboBox() {
        filterChoiceBox.setItems(FXCollections.observableArrayList(
                "Original Graph",
                "ID Interval",
                "Single Station Analysis"
        ));
        filterChoiceBox.setValue("Original Graph");
        filterChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> updateFieldVisibility());
    }

    /**
     * Update which input fields are visible according to the selected
     * filter type.
     *
     * <p>Fields are cleared when the visibility is changed to avoid stale
     * input being used in subsequent computations.</p>
     */
    private void updateFieldVisibility() {
        fromIdField.setVisible(false);
        fromLabel.setVisible(false);
        toIdField.setVisible(false);
        toLabel.setVisible(false);
        stationIDLabel.setVisible(false);
        singleStationIdField.setVisible(false);

        fromIdField.clear();
        toIdField.clear();
        singleStationIdField.clear();

        if ("ID Interval".equals(filterChoiceBox.getValue())) {
            fromIdField.setVisible(true);
            fromLabel.setVisible(true);
            toIdField.setVisible(true);
            toLabel.setVisible(true);
        }

        if ("Single Station Analysis".equals(filterChoiceBox.getValue())) {
            stationIDLabel.setVisible(true);
            singleStationIdField.setVisible(true);
        }
    }

    /**
     * Compute the upgrade plan according to the current filter and display
     * the resulting stations in the table.
     *
     * <p>The method delegates the computation to
     * {@link RailwayUpgradePlanningController#computeUpgradePlan(String, String, String, String)}.
     * If a cycle is detected the table is populated with the cycle stations
     * and the result label indicates that cycles were found; otherwise the
     * upgrade order is shown.</p>
     */
    @FXML
    public void handleComputePlan() {
        controller.computeUpgradePlan(
                filterChoiceBox.getValue(),
                fromIdField.getText(),
                toIdField.getText(),
                singleStationIdField.getText()
        );

        if (controller.hasCycle()) {
            resultTypeLabel.setText("Were found cycles!");
            stationsTable.setItems(FXCollections.observableArrayList(controller.getCycleStations()));
        } else {
            resultTypeLabel.setText("Were not found cycles!");
            stationsTable.setItems(FXCollections.observableArrayList(controller.getUpgradeOrder()));
        }
    }

    /**
     * Log out the current user and navigate to the public home page.
     *
     * @param event the JavaFX action event supplied by the UI
     */
    @FXML
    public void logoutButtonOnAction(javafx.event.ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Navigate back to the Infrastructure Planner home page.
     *
     * @param event the JavaFX action event supplied by the UI
     */
    @FXML
    public void homeOnAction(javafx.event.ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/infrastructurePlanner/infrastructurePlannerHomePage.fxml", "MABEC - Infrastructure Planner - Home Page");
    }

    /**
     * Open the Infrastructure Planner NOS logs view.
     *
     * @param event the JavaFX action event supplied by the UI
     */
    @FXML
    public void handleNOSLogsButton(javafx.event.ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/infrastructurePlanner/infrastructurePlannerNOS.fxml", "MABEC - Infrastructure Planner - NOS Logs");
    }

    /**
     * Open the railway baseline cost view.
     *
     * @param event the JavaFX action event supplied by the UI
     */
    @FXML
    public void handleBaselineCost(javafx.event.ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/infrastructurePlanner/railwayBaselineCost.fxml", "MABEC - Infrastructure Planner - Baseline Cost");
    }
}
