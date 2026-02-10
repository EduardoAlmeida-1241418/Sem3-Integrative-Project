package pt.ipp.isep.dei.ui.gui.trafficManager;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.traficManager.TrafficManagerSchedulerController;
import pt.ipp.isep.dei.data.memory.GeneralScheduleStoreInMemory;
import pt.ipp.isep.dei.domain.schedule.GeneralSchedule;
import pt.ipp.isep.dei.domain.schedule.ScheduleEvent;
import pt.ipp.isep.dei.domain.schedule.TrainSchedule;
import pt.ipp.isep.dei.domain.trainRelated.Train;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * GUI controller for the Planner Home Page.
 * Handles navigation to planner-related views such as Trolleys, Picking Plans, and WMS Logs.
 */
public class TrafficManagerSchedulerGUI implements Initializable {

    private UIUtils uiUtils;
    private TrafficManagerSchedulerController controller;

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
    private TableColumn<ScheduleEvent, String> trainIdDetailsTableColumn, movementTypeDetailsTableColumn, startPositionDetailsTableColumn, startTimeDetailsTableColumn, endPositionDetailsTableColumn, finalTimeDetailsTableColumn;

    @FXML
    private ComboBox<Train> trainOptionsComboBox;

    @FXML
    private Button youngerSchedulesButton, olderSchedulesButton, dispatchTrainButton;


    /**
     * Initialise the controller when the FXML view is loaded.
     *
     * <p>Creates helper instances, initialises GUI items and sets the
     * enabled state of the navigation buttons according to available
     * schedules.</p>
     *
     * @param url not used by this implementation
     * @param resourceBundle not used by this implementation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
        this.controller = new TrafficManagerSchedulerController();

        initGuiItems();

        youngerSchedulesButton.setDisable(!controller.existYoungerSchedule());
        olderSchedulesButton.setDisable(!controller.existOlderSchedule());
    }

    /**
     * Initialise GUI components and populate initial data.
     *
     * <p>Initialises panes, tables, combo boxes and chart, then sets the
     * initial information displayed to the user.</p>
     */
    private void initGuiItems() {
        initAnchorPanes();
        initScheduleTableView();
        initScheduleDetailTableView();
        initTrainComboBox();

        setInformation();
    }

    /**
     * Set the table, combo box and chart information from the controller.
     *
     * <p>Populates the schedule table, configures the train options and
     * updates the movement pie chart with all schedule events.</p>
     */
    private void setInformation() {
        scheduleTableView.setItems(FXCollections.observableArrayList(controller.getTrainSchedules()));

        trainOptionsComboBox.setItems(FXCollections.observableArrayList(controller.getAllTrainsInSchedule()));

        updateMovementPieChart(controller.getAllScheduleEvents());
    }

    /**
     * Update the movement pie chart showing stopped vs moving time.
     *
     * <p>Computes the total seconds spent in WAITING events and in other
     * movement events and sets the pie chart data accordingly. If the
     * supplied list is null or empty the chart is cleared.</p>
     *
     * @param events list of schedule events to aggregate
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
     * Initialise the train selection combo box and attach a listener to
     * update the detail table when a train is selected.
     */
    private void initTrainComboBox() {
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
     * Configure the schedule details table columns used to display
     * individual schedule events for a particular train.
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
     * Update the schedule details table with the supplied list of events.
     *
     * @param scheduleEvents list of schedule events to display
     */
    private void updateScheduleDetailTableView(List<ScheduleEvent> scheduleEvents) {
        scheduleDetailsTableView.getItems().clear();

        ObservableList<ScheduleEvent> scheduleEventsList = FXCollections.observableArrayList(scheduleEvents);
        scheduleDetailsTableView.setItems(scheduleEventsList);
    }

    /**
     * Configure which anchor pane is visible by default.
     * Primary pane is shown and secondary pane hidden.
     */
    private void initAnchorPanes() {
        primaryAnchorPane.setVisible(true);
        secundaryAnchorPane.setVisible(false);
    }

    /**
     * Configure the main schedule table columns to show brief schedule
     * summary information for each train schedule.
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
    }

    /**
     * Show older schedules by moving the internal pointer to the previous
     * stored schedule and refresh displayed information.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void olderSchedulesOnAction(ActionEvent event) {
        controller.setOlderTrainScheduleId();
        setInformation();
        if (controller.existYoungerSchedule()) {
            youngerSchedulesButton.setDisable(false);
            dispatchTrainButton.setDisable(true);
        } else {
            youngerSchedulesButton.setDisable(true);
            dispatchTrainButton.setDisable(false);
        }
        olderSchedulesButton.setDisable(!controller.existOlderSchedule());
    }

    /**
     * Show younger schedules by moving the internal pointer to the next
     * stored schedule and refresh displayed information.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void youngerSchedulesOnAction(ActionEvent event) {
        controller.setYoungerTrainScheduleId();
        setInformation();
        if (controller.existYoungerSchedule()) {
            youngerSchedulesButton.setDisable(false);
            dispatchTrainButton.setDisable(true);
        } else {
            youngerSchedulesButton.setDisable(true);
            dispatchTrainButton.setDisable(false);
        }
        olderSchedulesButton.setDisable(!controller.existOlderSchedule());
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
     * Navigate to the Traffic Manager Paths view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void pathOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/pathRelated/trafficManagerPath.fxml", "MABEC - Traffic Manager - Paths");
    }

    /**
     * Navigate to the Traffic Manager Assign view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void assignOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/trafficManagerAssign.fxml", "MABEC - Traffic Manager - Assign");
    }

    /**
     * Open the Traffic Manager NOS logs view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/trafficManagerNOS.fxml", "MABEC - Traffic Manager - NOS Logs");
    }

    /**
     * Log out the current user and return to the public Home Page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Switch to the secondary view and show the details of the currently
     * available schedule.
     *
     * @param event the action event supplied by the UI
     */
    public void viewScheduleDetailsButtonOnAction(ActionEvent event) {

        changeView();

        updateScheduleDetailTableView(controller.getAllScheduleEvents());
    }

    /**
     * Toggle between primary and secondary pane visibility.
     */
    private void changeView() {
        if (primaryAnchorPane.isVisible()) {
            secundaryAnchorPane.setVisible(true);
            primaryAnchorPane.setVisible(false);
        } else {
            secundaryAnchorPane.setVisible(false);
            primaryAnchorPane.setVisible(true);
        }
    }

    /**
     * Close the details view and clear the train selection.
     *
     * @param event the action event supplied by the UI
     */
    public void closeDetailsButtonOnAction(ActionEvent event) {
        changeView();
        trainOptionsComboBox.getSelectionModel().clearSelection();
    }

    /**
     * Show all train events in the details table and clear train selection.
     *
     * @param event the action event supplied by the UI
     */
    public void viewAllTrainsButtonOnAction(ActionEvent event) {
        updateScheduleDetailTableView(controller.getAllScheduleEvents());

        trainOptionsComboBox.getSelectionModel().clearSelection();
    }

    /**
     * Dispatch the selected train if verification passes.
     *
     * <p>The method verifies selection and dispatch status, calls the
     * controller to dispatch the train, updates the schedule table and
     * shows a success alert.</p>
     *
     * @param event the action event supplied by the UI
     */
    public void dispatchTrainButtonOnAction(ActionEvent event) {
        if (!verification(event)) {
            return;
        }

        controller.dispatchTrain(scheduleTableView.getSelectionModel().getSelectedItem());

        updateDetailTableView(controller.getTrainSchedules());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        uiUtils.showAlert(stage, "Train dispatched with Success", "success");

    }

    /**
     * Update the schedule table view with the supplied train schedules.
     *
     * @param trainSchedules list of train schedules to display
     */
    private void updateDetailTableView(List<TrainSchedule> trainSchedules) {
        scheduleTableView.getItems().clear();

        ObservableList<TrainSchedule> trainScheduleList = FXCollections.observableArrayList(trainSchedules);
        scheduleTableView.setItems(trainScheduleList);
    }

    /**
     * Verify that a train is selected and is not already dispatched.
     *
     * @param event the action event used to obtain a Stage for alerts
     * @return {@code true} when verification passes; {@code false} otherwise
     */
    private boolean verification(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (scheduleTableView.getSelectionModel().getSelectedItem() == null) {
            uiUtils.showAlert(stage, "Select a train first", "error");
            return false;
        }

        if (controller.verifyIfAlreadyDispatched(scheduleTableView.getSelectionModel().getSelectedItem())) {
            uiUtils.showAlert(stage, "Train already dispatched", "error");
            return false;
        }

        return true;
    }
}