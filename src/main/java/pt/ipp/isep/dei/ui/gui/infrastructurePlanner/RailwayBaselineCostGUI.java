package pt.ipp.isep.dei.ui.gui.infrastructurePlanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pt.ipp.isep.dei.controller.infrastructurePlanner.BackboneController;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the railway baseline cost view.
 *
 * <p>This JavaFX controller initialises and populates the stations table
 * used when computing baseline costs for backbone routing. It also handles
 * simple user interactions such as choosing whether to use the original or
 * an adapted backbone, invoking the backbone generation routine and
 * navigating between related Infrastructure Planner scenes.</p>
 *
 * <p>All business logic (backbone generation and station retrieval) is
 * delegated to {@link BackboneController}; this class is concerned only
 * with presentation and user interaction.
 */
public class RailwayBaselineCostGUI implements Initializable {

    private UIUtils uiUtils;
    private BackboneController controller;

    private static final String BASE_FILENAME = "global_backbone";

    /* ===== Table ===== */

    @FXML
    private TableView<StationEsinf> tableStations;

    @FXML
    private TableColumn<StationEsinf, String> colStationName;

    @FXML
    private TableColumn<StationEsinf, String> colLatitude;

    @FXML
    private TableColumn<StationEsinf, String> colLongitude;

    @FXML
    private RadioButton rbOriginal;

    @FXML
    private RadioButton rbAdapted;


    /* ===== Initialize ===== */

    /**
     * Initialise the GUI controller.
     *
     * <p>Creates helper instances, configures the stations table and loads
     * the station data. The original backbone option is selected by
     * default.</p>
     *
     * @param url resource location (unused)
     * @param resourceBundle resources (unused)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        uiUtils = new UIUtils();
        controller = new BackboneController();

        configTable();
        loadStations();

        rbOriginal.setSelected(true);

    }

    /**
     * Configure the table columns for station display.
     *
     * <p>Each column's cell value factory extracts a human readable value
     * from {@link StationEsinf} instances for presentation in the table.</p>
     */
    private void configTable() {

        colStationName.setCellValueFactory(
                cell -> new SimpleStringProperty(
                        cell.getValue().getStationName()
                )
        );

        colLatitude.setCellValueFactory(
                cell -> new SimpleStringProperty(
                        String.valueOf(cell.getValue().getLatitude())
                )
        );

        colLongitude.setCellValueFactory(
                cell -> new SimpleStringProperty(
                        String.valueOf(cell.getValue().getLongitude())
                )
        );

        tableStations.setSelectionModel(null);
    }

    /**
     * Load station data into the table from the controller.
     */
    private void loadStations() {

        tableStations.setItems(
                FXCollections.observableArrayList(
                        controller.getGlobalStations()
                )
        );
    }

    /* ===== Actions required by FXML ===== */

    /**
     * Navigate to the Infrastructure Planner home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(
                event,
                "/fxml/infrastructurePlanner/infrastructurePlannerHomePage.fxml",
                "MABEC - Infrastructure Planner - Home Page"
        );
    }

    /**
     * Navigate to the railway upgrades view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void handleRailwayUpgrades(ActionEvent event) {
        uiUtils.loadFXMLScene(
                event,
                "/fxml/infrastructurePlanner/railwayUpgradePlanning.fxml",
                "MABEC - Infrastructure Planner - Railway Upgrades"
        );
    }

    /**
     * Open the NOS logs view for Infrastructure Planner.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void handleNOSLogsButton(ActionEvent event) {
        uiUtils.loadFXMLScene(
                event,
                "/fxml/infrastructurePlanner/infrastructurePlannerNOS.fxml",
                "MABEC - Infrastructure Planner - NOS Logs"
        );
    }

    /**
     * Log out the current user and return to the public home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(
                event,
                "/fxml/visitorMenu/homePage.fxml",
                "MABEC - Home Page"
        );
    }

    /**
     * Compute and generate the global backbone, then open the backbone map
     * view.
     *
     * <p>The method delegates backbone generation to {@link BackboneController}.
     * If generation fails an error alert is displayed. The user's choice
     * regarding adapted/original backbone is stored in
     * {@link BackboneViewContext} prior to opening the map view.</p>
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void handleComputePlan(ActionEvent event) {

        String baseFilename = BASE_FILENAME;


        BackboneController controller = new BackboneController();

        try {
            controller.generateGlobalBackbone(baseFilename);
        } catch (IOException e) {
            showError("Error generating backbone graph:\n" + e.getMessage());
            return;
        }

        // guardar escolha do utilizador
        boolean adaptedSelected = rbAdapted.isSelected();

        BackboneViewContext.setAdapted(adaptedSelected);
        BackboneViewContext.setBaseFilename(baseFilename);

        // abrir a view do mapa (próximo passo)
        uiUtils.loadFXMLScene(
                event,
                "/fxml/infrastructurePlanner/viewBackboneMap.fxml",
                "MABEC - View Backbone Map"
        );
    }

    /**
     * Display an error alert with the supplied message.
     *
     * @param message the text to display in the alert content
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation failed");
        alert.setContentText(message);
        alert.showAndWait();
    }


}
