package pt.ipp.isep.dei.ui.gui.trafficDispatcher;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import pt.ipp.isep.dei.controller.trafficDispatcher.EstimatedTravelController;
import pt.ipp.isep.dei.domain.Facility;
import pt.ipp.isep.dei.domain.Locomotive;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Estimated Travel GUI.
 * Allows the traffic dispatcher to calculate estimated travel time between facilities
 * using selected locomotives and railway lines. Displays results by line and segment.
 */
public class EstimatedTravelGUI implements Initializable {

    private EstimatedTravelController controller;
    private UIUtils uiUtils;

    @FXML private AnchorPane anchorPaneFacilitiesTable;
    @FXML private AnchorPane anchorPaneLocomotivesTable;

    @FXML private TableView<Facility> tableViewStartFacility;
    @FXML private TableColumn<Facility, String> columnStartFacilityId;
    @FXML private TableColumn<Facility, String> columnStartFacilityName;

    @FXML private TableView<Facility> tableViewEndFacility;
    @FXML private TableColumn<Facility, String> columnEndFacilityId;
    @FXML private TableColumn<Facility, String> columnEndFacilityName;

    @FXML private TableView<Locomotive> tableViewLocomotives;
    @FXML private TableColumn<Locomotive, String> columnNumLocomotive;
    @FXML private TableColumn<Locomotive, String> columnLocomotiveName;
    @FXML private TableColumn<Locomotive, String> columnLocomotiveOperationalSpeed;
    @FXML private TableColumn<Locomotive, String> columnLocomotiveMaxSpeed;

    @FXML private TableView<EstimatedTravelController.LineResult> tableViewResultLines;
    @FXML private TableColumn<EstimatedTravelController.LineResult, String> columnLineIdRailwayLine;
    @FXML private TableColumn<EstimatedTravelController.LineResult, String> columnResultStartEndFacilities;
    @FXML private TableColumn<EstimatedTravelController.LineResult, String> columnResultDistance;
    @FXML private TableColumn<EstimatedTravelController.LineResult, String> columnResultTravelTime;

    @FXML private TableView<EstimatedTravelController.TraversedSegment> tableViewResultSegments;
    @FXML private TableColumn<EstimatedTravelController.TraversedSegment, String> columnLineIdSegment;
    @FXML private TableColumn<EstimatedTravelController.TraversedSegment, String> columnOrderSegment;
    @FXML private TableColumn<EstimatedTravelController.TraversedSegment, String> columnIsElectrifiedSegment;
    @FXML private TableColumn<EstimatedTravelController.TraversedSegment, String> columnSpeedLimitSegment;
    @FXML private TableColumn<EstimatedTravelController.TraversedSegment, String> columnLengthSegment;
    @FXML private TableColumn<EstimatedTravelController.TraversedSegment, String> columnEstimateTimeSegment;

    @FXML private Label labelStartFacility;
    @FXML private Label labelEndFacility;
    @FXML private Label labelDistance;
    @FXML private Label labelTotalTime;
    @FXML private Label labelMessage;

    @FXML private Button backButton;

    /**
     * Initializes the GUI, controller, and facility tables.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new EstimatedTravelController();
        uiUtils = new UIUtils();
        loadStartFacilityTable();
        tableViewResultSegments.setSelectionModel(null);
    }

    /**
     * Loads the start facility table and sets up selection logic.
     */
    private void loadStartFacilityTable() {
        columnStartFacilityId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getId() + ""));
        columnStartFacilityName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        tableViewStartFacility.setItems(controller.getAllFacilities());
        tableViewStartFacility.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                controller.setStartFacilityId(newSel.getId());
                loadEndFacilityTable();
            }
        });
    }

    /**
     * Loads the end facility table based on the selected start facility.
     * Displays a message depending on path availability.
     */
    private void loadEndFacilityTable() {
        columnEndFacilityId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getId() + ""));
        columnEndFacilityName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        tableViewEndFacility.setItems(controller.getAllFacilitiesExcluding());
        tableViewEndFacility.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                controller.setEndFacilityId(newSel.getId());
                if (controller.createPath()) {
                    showTemporaryMessage("Path created successfully.", Color.GREEN);
                } else {
                    showTemporaryMessage("No path exists between the selected facilities.", Color.RED);
                    return;
                }
                loadLocomotivesTable();
                backButton.setDisable(false);
                backButton.setVisible(true);
            }
        });
    }

    /**
     * Loads the locomotives table and enables locomotive selection.
     */
    private void loadLocomotivesTable() {
        anchorPaneFacilitiesTable.setDisable(true);
        anchorPaneFacilitiesTable.setVisible(false);

        anchorPaneLocomotivesTable.setDisable(false);
        anchorPaneLocomotivesTable.setVisible(true);

        columnNumLocomotive.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getId() + ""));
        columnLocomotiveName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        columnLocomotiveOperationalSpeed.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getLocomotiveModel().getOperationalSpeed())));
        columnLocomotiveMaxSpeed.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getLocomotiveModel().getMaxSpeed())));
        tableViewLocomotives.setItems(controller.getAllLocomotives());
        tableViewLocomotives.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                controller.setLocomotiveId(newSel.getId());
                loadRailwayLineTable();
                tableViewResultSegments.setDisable(true);
                tableViewResultSegments.setVisible(false);
            }
        });
    }

    /**
     * Loads the railway line results table with estimated travel times and distances.
     */
    private void loadRailwayLineTable() {
        setResults();
        columnLineIdRailwayLine.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLineId() + ""));
        columnResultStartEndFacilities.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStartEndFacilities()));
        columnResultDistance.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDistance()));
        columnResultTravelTime.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTravelTimeHours()));
        tableViewResultLines.setItems(controller.getLineResults());
        tableViewResultLines.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                loadSegmentsTable(newSel.getLineId() + "");
                UIUtils.addLog("Estimated travel time to travel the railway line (" + newSel.getLineId() + ") using locomotive (" +
                        controller.getLocomotive().getName() + "): " + newSel.getTravelTimeHours(), LogType.INFO, RoleType.TRAFFIC_DISPATCHER);
            }
        });
    }

    /**
     * Updates result summary labels with total distance and time.
     */
    private void setResults() {
        controller.calculateEstimatedTravelTime();
        labelStartFacility.setText(controller.getStartFacilityName());
        labelEndFacility.setText(controller.getEndFacilityName());
        labelDistance.setText(String.format("%.2f km", controller.getTotalDistanceKm()));
        labelTotalTime.setText(controller.getTotalEstimatedTime().toString());
        UIUtils.addLog("Estimated travel time between " + controller.getStartFacilityName() +
                " and " + controller.getEndFacilityName() + " using locomotive (" + controller.getLocomotive().getName() + "): " +
                controller.getTotalEstimatedTime().toString(), LogType.INFO, RoleType.TRAFFIC_DISPATCHER);
    }

    /**
     * Loads the segments table for the selected railway line.
     *
     * @param lineId The ID of the railway line.
     */
    private void loadSegmentsTable(String lineId) {
        columnLineIdSegment.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLineId()));
        columnOrderSegment.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getActualOrderPosition())));
        columnIsElectrifiedSegment.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().isElectrified())));
        columnSpeedLimitSegment.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getSpeedLimit())));
        columnLengthSegment.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getLengthString())));
        columnEstimateTimeSegment.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstimatedTimeHours()));
        tableViewResultSegments.setItems(FXCollections.observableArrayList(controller.getTraversedSegmentsByLine(lineId)));
        tableViewResultSegments.setVisible(true);
        tableViewResultSegments.setDisable(false);
    }

    /**
     * Displays a temporary status message with fade-out animation.
     *
     * @param message The message text.
     * @param color The message color.
     */
    private void showTemporaryMessage(String message, Color color) {
        labelMessage.setText(message);
        labelMessage.setTextFill(color);
        labelMessage.setOpacity(1.0);
        labelMessage.setVisible(true);
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            FadeTransition fade = new FadeTransition(Duration.seconds(1), labelMessage);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(ev -> labelMessage.setVisible(false));
            fade.play();
        }));
        delay.play();
    }

    /**
     * Handles the back button action. Resets selections and restores the facility view.
     */
    @FXML
    public void handleBackButton(ActionEvent event) {
        tableViewEndFacility.getItems().clear();
        tableViewLocomotives.getItems().clear();
        tableViewResultLines.getItems().clear();
        tableViewStartFacility.getSelectionModel().clearSelection();
        tableViewEndFacility.getSelectionModel().clearSelection();
        tableViewLocomotives.getSelectionModel().clearSelection();

        controller.setStartFacilityId(-1);
        controller.setEndFacilityId(-1);
        controller.setLocomotiveId(-1);

        anchorPaneFacilitiesTable.setDisable(false);
        anchorPaneFacilitiesTable.setVisible(true);

        anchorPaneLocomotivesTable.setDisable(true);
        anchorPaneLocomotivesTable.setVisible(false);

        tableViewStartFacility.setItems(controller.getAllFacilities());

        tableViewResultSegments.setDisable(true);
        tableViewResultSegments.setVisible(false);

        backButton.setDisable(true);
        backButton.setVisible(false);
    }

    /** Navigates to the Traffic Dispatcher home page. */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficDispatcher/trafficDispatcherHomePage.fxml",
                "MABEC - Traffic Dispatcher - Home Page");
    }

    /** Logs out and returns to the main home page. */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /** Opens the Traffic Dispatcher WMS Logs interface. */
    @FXML
    public void handleWmsLogButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficDispatcher/trafficDispatcherWMS.fxml",
                "MABEC - Traffic Dispatcher - WMS Log");
    }
}
