package pt.ipp.isep.dei.ui.gui.freightManager;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.freightManager.FreightManagerSchedulerController;
import pt.ipp.isep.dei.domain.schedule.ScheduleEvent;
import pt.ipp.isep.dei.domain.schedule.TrainSchedule;
import pt.ipp.isep.dei.domain.trainRelated.Train;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * GUI controller for the Freight Manager scheduler view.
 *
 * <p>This JavaFX controller initialises and manages the schedule-related
 * user interface used by Freight Managers. It configures table views that
 * show scheduled trains and their events, populates a pie chart with
 * movement statistics, and provides handlers for navigation and for
 * dispatching trains. All business logic is delegated to
 * {@link FreightManagerSchedulerController}; this class is solely
 * responsible for presentation and user interaction.</p>
 */
public class FreightManagerSchedulerGUI implements Initializable {

    private UIUtils uiUtils;
    private FreightManagerSchedulerController controller;

    @FXML
    private TableView<TrainSchedule> scheduleTableView;

    @FXML
    private TableColumn<TrainSchedule, String> dispatchTableColumn, trainIdTableColumn, startTimeTableColumn, endTimeTableColumn, startPositionTableColumn, endPositionTableColumn, waitingTimeTableColumn;

    @FXML
    private AnchorPane primaryAnchorPane, secundaryAnchorPane;

    @FXML
    private PieChart graphPieChart;

    @FXML
    private TableView<ScheduleEvent> scheduleDetailsTableView;

    @FXML
    private TableColumn<ScheduleEvent, String> trainIdDetailsTableColumn, movementTypeDetailsTableColumn, startPositionDetailsTableColumn, startTimeDetailsTableColumn
            , endPositionDetailsTableColumn, finalTimeDetailsTableColumn;

    @FXML
    private ComboBox<Train> trainOptionsComboBox;

    /**
     * Initialise the controller and supporting UI helpers.
     *
     * <p>This method is invoked by the JavaFX runtime when the FXML view is
     * loaded. It initialises the controller and UI utilities, configures the
     * visible panes and populates all UI controls with initial data.</p>
     *
     * @param url resource location (unused)
     * @param resourceBundle resources for localisation (unused)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
        this.controller = new FreightManagerSchedulerController();

        initGuiItems();
    }

    /**
     * Initialise the GUI elements used by the scheduler view.
     *
     * <p>Configures anchor panes, table views, the train selector and the
     * movement pie chart.</p>
     */
    private void initGuiItems() {
        initAnchorPanes();
        initScheduleTableView();
        initScheduleDetailTableView();
        initTrainComboBox();
        updateMovementPieChart(controller.getAllScheduleEvents());
    }

    /**
     * Update the movement pie chart using the supplied schedule events.
     *
     * <p>The chart shows total stopped and moving time aggregated from the
     * events list. If the events list is empty no chart data is produced.</p>
     *
     * @param events list of schedule events used to populate the chart
     */
    private void updateMovementPieChart(List<ScheduleEvent> events) {

        graphPieChart.getData().clear();

        if (events == null || events.isEmpty()) {
            return;
        }

        long stoppedSeconds = 0;
        long movingSeconds = 0;

        for (ScheduleEvent event : events) {

            long duration = event.getTimeInterval().getDurationInSeconds();

            if (event.getScheduleEventType().name().equals("WAITING")) {
                stoppedSeconds += duration;
            } else {
                movingSeconds += duration;
            }
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Stopped Time", stoppedSeconds),
                new PieChart.Data("Moving Time", movingSeconds)
        );

        graphPieChart.setData(pieData);
    }


    /**
     * Initialise the train selection combo box and register a listener so
     * that selecting a train updates the schedule details view.
     */
    private void initTrainComboBox() {
        trainOptionsComboBox.setItems(FXCollections.observableArrayList(controller.getAllTrainsInSchedule()));

        trainOptionsComboBox
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldTrain, newTrain) -> {

                    if (newTrain == null) {
                        return;
                    }

                    updateScheduleDetailTableView(controller.getSingleTrainSchedules(newTrain));
                });
    }

    /**
     * Configure the schedule details table columns to display event
     * attributes such as train id, movement type, positions and timestamps.
     */
    private void initScheduleDetailTableView() {

        dispatchTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getTrain().isDispatched()))
        );

        trainIdDetailsTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getTrain().getId()))
        );

        movementTypeDetailsTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getScheduleEventType().getDescription()))
        );

        startPositionDetailsTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getStartPosition()))
        );

        startTimeDetailsTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getTimeInterval().getInitialDateTime()))
        );

        endPositionDetailsTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getEndPosition()))
        );

        finalTimeDetailsTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getTimeInterval().getFinalDateTime()))
        );

    }

    /**
     * Replace the schedule detail table contents with the supplied events.
     *
     * @param scheduleEvents list of schedule events to display
     */
    private void updateScheduleDetailTableView(List<ScheduleEvent> scheduleEvents){
        scheduleDetailsTableView.getItems().clear();

        ObservableList<ScheduleEvent> scheduleEventsList = FXCollections.observableArrayList(scheduleEvents);
        scheduleDetailsTableView.setItems(scheduleEventsList);
    }

    /**
     * Initialise which anchor pane is visible by default.
     */
    private void initAnchorPanes() {
        primaryAnchorPane.setVisible(true);
        secundaryAnchorPane.setVisible(false);
    }

    /**
     * Configure and populate the main schedule table which summarises each
     * train's schedule (start/end positions and times, waiting time etc.).
     */
    private void initScheduleTableView() {
        trainIdTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getTrain().getId()))
        );

        startTimeTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getScheduleEvents().getFirst().getTimeInterval().getInitialDateTime()))
        );

        endTimeTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getScheduleEvents().getLast().getTimeInterval().getFinalDateTime()))
        );

        startPositionTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getScheduleEvents().getFirst().getStartPosition()))
        );

        endPositionTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getScheduleEvents().getLast().getEndPosition()))
        );

        waitingTimeTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(controller.calculateLostTime(cellData.getValue().getScheduleEvents()))
        );

        ObservableList<TrainSchedule> trainSchedules = FXCollections.observableArrayList(controller.getTrainSchedules());
        scheduleTableView.setItems(trainSchedules);
    }



    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerHomePage.fxml", "MABEC - Freight Manager - Home Page");
    }

    @FXML
    public void trainsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/TrainRelated/freightManagerLocomotive.fxml", "MABEC - Freight Manager - Trains");
    }

    @FXML
    public void routeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/RouteRelated/freightManagerRoute.fxml", "MABEC - Freight Manager - Route");
    }

    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerGQS_NOS.fxml", "MABEC - Freight Manager - GQS / NOS Logs");
    }

    public void viewScheduleDetailsButtonOnAction(ActionEvent event) {

        changeView();

        updateScheduleDetailTableView(controller.getAllScheduleEvents());
    }

    private void changeView() {
        if (primaryAnchorPane.isVisible()) {
            secundaryAnchorPane.setVisible(true);
            primaryAnchorPane.setVisible(false);
        } else {
            secundaryAnchorPane.setVisible(false);
            primaryAnchorPane.setVisible(true);
        }
    }

    public void closeDetailsButtonOnAction(ActionEvent event) {
        changeView();
        trainOptionsComboBox.getSelectionModel().clearSelection();
    }

    public void viewAllTrainsButtonOnAction(ActionEvent event) {
        updateScheduleDetailTableView(controller.getAllScheduleEvents());

        trainOptionsComboBox.getSelectionModel().clearSelection();
    }

    public void dispatchTrainButtonOnAction(ActionEvent event) {
        if (!verification(event)){
            return;
        }

        controller.dispatchTrain(scheduleTableView.getSelectionModel().getSelectedItem());

        updateDetailTableView(controller.getTrainSchedules());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        uiUtils.showAlert(stage, "Train dispatched with Success", "success");

    }

    private void updateDetailTableView(List<TrainSchedule> trainSchedules) {
        scheduleTableView.getItems().clear();

        ObservableList<TrainSchedule> trainScheduleList = FXCollections.observableArrayList(trainSchedules);
        scheduleTableView.setItems(trainScheduleList);
    }

    private boolean verification(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (scheduleTableView.getSelectionModel().getSelectedItem() == null) {
            uiUtils.showAlert(stage, "Select a train first", "error");
            return false;
        }

        if (controller.verifyIfAlreadyDispatched(scheduleTableView.getSelectionModel().getSelectedItem())){
            uiUtils.showAlert(stage, "Train already dispatched", "error");
            return false;
        }

        return true;
    }
}