package pt.ipp.isep.dei.ui.gui.dataEngineer;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pt.ipp.isep.dei.controller.dataEnginner.OrganizedStationsController;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Tree.KDTree.KD2TreeStation;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller responsible for displaying organised station structures.
 *
 * <p>This JavaFX controller presents KD-Tree nodes alongside the stations
 * contained within each node. It initialises the backing controller and UI
 * helpers, populates table views, and updates summary labels such as tree
 * height and node counts. Navigation handlers are provided to return to the
 * Data Engineer home page or to view GQS logs.</p>
 *
 */
public class OrganizedStationsGUI implements Initializable {

    private OrganizedStationsController controller;
    private UIUtils uiUtils;

    @FXML
    private TableView<KD2TreeStation.KD2NodeStation> nodesTableView;

    @FXML
    private TableView<StationEsinf> stationsTableView;

    @FXML
    private TableColumn<KD2TreeStation.KD2NodeStation, String> nodeTableColumn, heightTableColumn, countryTableColumn, timeZoneTableColumn, timeZoneGroupTableColumn, latitudeTableColumn, longitudeTableColumn, nStationsTableColumn;

    @FXML
    private TableColumn<StationEsinf, String> stationsNameTableColumn;

    @FXML
    private Label heightLabel, nNodesLabel, nLeavesLabel;

    /**
     * Initialise the controller and populate the UI components.
     *
     * <p>Creates the {@link OrganizedStationsController} and {@link UIUtils}
     * helper, fills the nodes table, configures the stations table, and sets
     * the summary labels.</p>
     *
     * @param url not used by this implementation
     * @param resourceBundle not used by this implementation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new OrganizedStationsController();
        this.uiUtils = new UIUtils();

        putDataStationsTableView();
        configureStationsTableView();
        initializeLabels();
    }

    /**
     * Populate the nodes table with KD-Tree node information and register a
     * listener to update the stations table when the selection changes.
     *
     * <p>Each column extracts a displayable value from the node or its
     * first station. The nodes table is initialised with the controller's
     * node list.</p>
     */
    private void putDataStationsTableView() {
        nodeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocationNodeString()));

        heightTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getHeight())));

        countryTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getElement().getStations().getFirst().getCountry().toString()));

        timeZoneTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getElement().getStations().getFirst().getTimeZone().getZoneId().replace("urope","")));

        timeZoneGroupTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getElement().getStations().getFirst().getTimeZoneGroup().toString()));

        latitudeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getElement().getStations().getFirst().getX())));

        longitudeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getElement().getStations().getFirst().getY())));

        nStationsTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getElement().getStations().size())));

        nodesTableView.setItems(controller.getKD2TreeStationNodes());

        nodesTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                updateStationsTableView(newSel);
            } else {
                stationsTableView.setItems(FXCollections.observableArrayList());
            }
        });
    }

    /**
     * Configure the stations table to display station names and disable
     * selection behaviour.
     */
    private void configureStationsTableView() {
        stationsNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStationName()));
        stationsTableView.setSelectionModel(null);
    }

    /**
     * Initialise summary labels showing tree metrics such as height, number
     * of nodes and number of leaves.
     */
    private void initializeLabels() {
        heightLabel.setText(String.valueOf(controller.getTreeHeight()));
        nNodesLabel.setText(String.valueOf(controller.getnNodes()));
        nLeavesLabel.setText(String.valueOf(controller.getnLeaves()));
    }

    /**
     * Update the stations table to show the stations contained in the
     * supplied KD-Tree node.
     *
     * @param selectedNode the node whose stations will be displayed
     */
    private void updateStationsTableView(KD2TreeStation.KD2NodeStation selectedNode) {
        ObservableList<StationEsinf> stations = FXCollections.observableArrayList(selectedNode.getElement().getStations());
        stationsTableView.setItems(stations);
    }

    /**
     * Navigate to the Data Engineer home page.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/dataEngineer/dataEngineerHomePage.fxml", "MABEC - Data Engineer - Home Page");
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


    /**
     * Navigate to the Data Engineer GQS logs view.
     *
     * @param actionEvent the action event that triggered the navigation
     */
    @FXML
    public void handleWmsLogsButton(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/dataEngineer/dataEngineerGQS.fxml", "MABEC - Data Engineer - GQS Logs");
    }
}
