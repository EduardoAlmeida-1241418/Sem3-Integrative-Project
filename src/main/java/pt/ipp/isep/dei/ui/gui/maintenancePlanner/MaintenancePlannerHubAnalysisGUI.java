package pt.ipp.isep.dei.ui.gui.maintenancePlanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pt.ipp.isep.dei.controller.algorithms.DegreeCalculatorAlgorithm;
import pt.ipp.isep.dei.controller.maintenancePlanner.MaintenancePlannerHubAnalysisController;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Maintenance Planner hub analysis view.
 *
 * <p>This JavaFX controller initialises the GUI used to inspect network
 * centrality measures for stations (for example degree, strength,
 * betweenness, harmonic closeness and hub score). It delegates the
 * computational work to {@link MaintenancePlannerHubAnalysisController}
 * and formats the results for presentation in a {@link TableView}.</p>
 *
 */
public class MaintenancePlannerHubAnalysisGUI implements Initializable {
    private UIUtils uiUtils;
    private MaintenancePlannerHubAnalysisController controller;

    @FXML
    private TableView<StationEsinf> stationTableView;

    @FXML
    private TableColumn<StationEsinf, String> idTableColumn, nameTableColumn, degreeTableColumn, strenghtTableColumn, betweennessTableColumn, harmonicClosenessTableColumn, hubScoreTableColumn;

    /**
     * Initialise the controller and supporting helpers.
     *
     * <p>This method is invoked by the JavaFX runtime when the FXML view is
     * loaded. It creates the {@link UIUtils} helper, initialises the
     * {@link MaintenancePlannerHubAnalysisController} and calls the GUI
     * initialisation routines so that the table is populated.</p>
     *
     * @param url not used by this implementation
     * @param resourceBundle not used by this implementation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
        this.controller = new MaintenancePlannerHubAnalysisController();
        
        initGuiItems();
    }

    /**
     * Initialise GUI items used by the hub analysis view.
     *
     * <p>This method configures the station table and may be extended to
     * initialise additional controls in the future.</p>
     */
    private void initGuiItems() {
        initFacilityTable();
        //printStationTableWithHubScore();
    }

    /**
     * Configure the station table columns and populate the table.
     *
     * <p>Each column's cell value factory extracts and formats values from
     * {@link StationEsinf} instances. Centrality measures are computed via
     * controller methods (or small helper algorithms) and rendered with a
     * suitable textual format.</p>
     */
    private void initFacilityTable() {
        idTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        nameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getStationName()))
        );

        degreeTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(new DegreeCalculatorAlgorithm().calculateDegree(cellData.getValue())))
        );

        strenghtTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.5f", controller.findStrength(cellData.getValue())))
        );

        betweennessTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.5f", controller.findBetweenness(cellData.getValue())))
        );

        harmonicClosenessTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.5f", controller.findHarmonicCloseness(cellData.getValue())))
        );

        hubScoreTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.4f", controller.findHubScore(cellData.getValue())))
        );

        ObservableList<StationEsinf> stationEsinfs = FXCollections.observableArrayList(controller.getFacilities());
        stationTableView.setItems(stationEsinfs);
    }


    /**
     * Navigate to the hub analysis view (current view).
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void hubAnalysisButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/maintenancePlanner/maintenancePlannerHubAnalysis.fxml", "MABEC - Maintenance Planner - Hub Analysis");
    }

    /**
     * Open the Maintenance NOS logs view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void handleNOSLogsButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/maintenancePlanner/maintenanceManagerNOS.fxml", "MABEC - Maintenance Planner - NOS Logs");
    }

    /**
     * Navigate to the Maintenance Planner home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void homePageButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/maintenancePlanner/maintenancePlannerHomePage.fxml", "MABEC - Maintenance Planner - Home Page");
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
     * Print the station table contents including hub score and other
     * centrality measures to standard output.
     *
     * <p>This helper is intended for debugging and inspection in a
     * development environment. It formats the table rows into aligned
     * columns and prints each station's computed metrics.</p>
     */
    private void printStationTableWithHubScore() {

        System.out.printf(
                "%-6s %-25s %-8s %-12s %-14s %-20s %-10s%n",
                "ID",
                "NAME",
                "DEGREE",
                "STRENGTH",
                "BETWEENNESS",
                "HARMONIC_CLOSENESS",
                "HUBSCORE"
        );

        System.out.println(
                "-----------------------------------------------------------------------------------------------"
        );

        for (StationEsinf station : stationTableView.getItems()) {

            int degree =
                    new DegreeCalculatorAlgorithm().calculateDegree(station);

            double strength =
                    controller.findStrength(station);

            double betweenness =
                    controller.findBetweenness(station);

            double harmonicCloseness =
                    controller.findHarmonicCloseness(station);

            double hubScore =
                    controller.findHubScore(station);

            System.out.printf(
                    "%-6s %-25s %-8d %-12.5f %-14.5f %-20.5f %-10.5f%n",
                    station.getId(),                 // %s → seguro
                    station.getStationName(),
                    degree,
                    strength,
                    betweenness,
                    harmonicCloseness,
                    hubScore
            );
        }
    }


}
