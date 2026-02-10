package pt.ipp.isep.dei.ui.gui.trafficManager;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.traficManager.trainRelated.TrafficManagerAssignController;
import pt.ipp.isep.dei.domain.Locomotive;
import pt.ipp.isep.dei.domain.Operator;
import pt.ipp.isep.dei.domain.schedule.TrainSchedule;
import pt.ipp.isep.dei.domain.transportationRelated.Route;
import pt.ipp.isep.dei.domain.wagonRelated.Wagon;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the Traffic Manager assignment screen.
 *
 * <p>This JavaFX controller initialises and manages the UI used by a
 * Traffic Manager to create, preview and assign train schedules for
 * available routes. It configures tables showing routes, wagons,
 * locomotives and the generated schedule, and provides handlers for the
 * various navigation and action buttons. Business logic is delegated to
 * {@link TrafficManagerAssignController}; this class is responsible
 * solely for presentation and user interaction.</p>
 *
 */
public class TrafficManagerAssignGUI implements Initializable {

    private UIUtils uiUtils;
    private TrafficManagerAssignController controller;

    @FXML
    private ComboBox<String> operatorComboBox;

    @FXML
    private DatePicker dateDatePicker;

    @FXML
    private Spinner<Integer> hourSpinner, minSpinner;

    @FXML
    private TableView<Route> routeTableView;

    @FXML
    private TableColumn<Route, String> idTableColumn, freightQtyTableColumn;

    @FXML
    private TableView<Wagon> routeDetailTableView;

    @FXML
    private TableColumn<Wagon, String> wagonIDTableColumn, wagonPositionTableColumn;

    @FXML
    private TableView<Locomotive> locomotiveTableView;

    @FXML
    private TableColumn<Locomotive, String> locomotiveIDTableColumn, locomotiveModelTableColumn, locomotivePositionTableColumn;


    @FXML
    private TableView<TrainSchedule> scheduleDetailsTableView;

    @FXML
    private TableColumn<TrainSchedule, String> trainIdTableColumn, startTimeTableColumn, endTimeTableColumn, startPositionTableColumn, endPositionTableColumn, waitingTimeTableColumn;

    @FXML
    private AnchorPane primaryAnchorPane, secundaryAnchorPane;

    /**
     * Initialise the controller and supporting helpers.
     *
     * <p>Called by the JavaFX runtime when the FXML view is loaded. This
     * method constructs the UI helper and domain controller, then initialises
     * the visible UI components via {@link #initGuiItems()}.</p>
     *
     * @param url resource location (unused)
     * @param resourceBundle resources for localisation (unused)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
        this.controller = new TrafficManagerAssignController();

        initGuiItems();
    }

    /**
     * Initialise GUI components: panes, controls, tables, spinners and
     * date picker wiring.
     */
    private void initGuiItems() {
        initAnchorPane();
        initComboBox();
        initSpinners();
        initRouteTable();
        initDatePicker();
        initRouteDetailTable();
        initLocomotiveTable();
        initScheduleDetailsTable();
    }

    /**
     * Configure the schedule details table columns.
     */
    private void initScheduleDetailsTable() {
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
     * Populate the schedule details table with the controller's generated
     * schedule data.
     */
    private void loadScheduleDetailsTable() {
        ObservableList<TrainSchedule> trainSchedules = FXCollections.observableArrayList(controller.getTrainSchedules());
        scheduleDetailsTableView.setItems(trainSchedules);
    }

    /**
     * Configure which anchor pane is visible by default (primary view).
     */
    private void initAnchorPane() {
        primaryAnchorPane.setVisible(true);
        secundaryAnchorPane.setVisible(false);
    }

    /**
     * Configure the route detail (wagon) table columns.
     */
    private void initRouteDetailTable() {
        wagonIDTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getWagonID()))
        );

        wagonPositionTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(controller.findWagonPosition(cellData.getValue(), dateDatePicker.getValue(), hourSpinner.getValue(), minSpinner.getValue()).getName()))
        );
    }

    /**
     * Configure the date picker to update dependent tables when the date
     * changes.
     */
    private void initDatePicker() {
        dateDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate == null) {
                return;
            }

            updateLocomotiveTable();
            updateRouteTable();
        });
    }

    /**
     * Configure the locomotive table columns and allow multiple selection.
     */
    private void initLocomotiveTable() {
        locomotiveIDTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        locomotiveModelTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getLocomotiveModel().getName()))
        );


        locomotivePositionTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(controller.getLocomotiveActualPosition(cellData.getValue(), dateDatePicker.getValue(), hourSpinner.getValue(), minSpinner.getValue()).getName()))
        );

        locomotiveTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Refresh the locomotive table from the controller's available
     * locomotives.
     */
    private void updateLocomotiveTable() {
        locomotiveTableView.getItems().clear();

        ObservableList<Locomotive> locomotives = FXCollections.observableArrayList(controller.locomotiveList());
        locomotiveTableView.setItems(locomotives);
    }

    /**
     * Configure the routes table and attach a listener to update the route
     * detail table when a selection changes.
     */
    private void initRouteTable() {
        idTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        freightQtyTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getFreights().size()))
        );

        routeTableView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldRoute, newRoute) -> {
                    if (newRoute == null)
                        return;

                    updateRouteDetailTable(newRoute);
                });
    }

    /**
     * Refresh the available routes list for the currently selected date.
     */
    private void updateRouteTable() {
        routeTableView.getItems().clear();

        ObservableList<Route> routes = FXCollections.observableArrayList(controller.getReadyRoutes(dateDatePicker.getValue()));
        routeTableView.setItems(routes);

        routeDetailTableView.getItems().clear();
    }

    /**
     * Update the route detail (wagon) table for the supplied route.
     *
     * @param newRoute the selected route whose wagons are displayed
     */
    private void updateRouteDetailTable(Route newRoute) {
        ObservableList<Wagon> wagons = FXCollections.observableArrayList(controller.getRouteWagons(newRoute));
        routeDetailTableView.setItems(wagons);
    }

    /**
     * Configure and wire the time spinners to update dependent UI when
     * values change.
     */
    private void initSpinners() {
        hourSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0)
        );

        minSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0)
        );

        hourSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;

            if (dateDatePicker.getValue() == null){
                return;
            }

            updateLocomotiveTable();
            updateRouteTable();
        });

        minSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;

            if (dateDatePicker.getValue() == null){
                return;
            }

            updateLocomotiveTable();
            updateRouteTable();
        });
    }

    /**
     * Populate the operator combo box with available operator names.
     */
    private void initComboBox() {
        ArrayList<String> listNames = new ArrayList<>();

        for (Operator operator: controller.getOperators()){
            listNames.add(operator.getName());
        }

        operatorComboBox.setItems(FXCollections.observableArrayList(listNames));
    }




    @FXML
    /**
     * Navigate to the Traffic Manager home page.
     *
     * @param event the action event supplied by the UI
     */
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/trafficManagerHomePage.fxml", "MABEC - Traffic Manager - Home Page");
    }

    @FXML
    /**
     * Navigate to the path assignment view.
     *
     * @param event the action event supplied by the UI
     */
    public void pathOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/pathRelated/trafficManagerPath.fxml", "MABEC - Traffic Manager - Assign");
    }

    @FXML
    /**
     * Navigate to the scheduler view.
     *
     * @param event the action event supplied by the UI
     */
    public void schedulerOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/scheduleRelated/trafficManagerScheduler.fxml", "MABEC - Traffic Manager - Scheduler");
    }

    @FXML
    /**
     * Open the NOS logs view for inspection.
     *
     * @param event the action event supplied by the UI
     */
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/trafficManagerNOS.fxml", "MABEC - Traffic Manager - NOS Logs");
    }

    @FXML
    /**
     * Log out the current user and return to the public home page.
     *
     * @param event the action event supplied by the UI
     */
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    @FXML
    /**
     * Close the schedule detail view and return to the primary view.
     *
     * @param event the action event supplied by the UI
     */
    public void closeDetailsButtonOnAction(ActionEvent event) {
        modifyView(true);
    }

    /**
     * Generate a train schedule based on the selected options and show
     * the details in the secondary view.
     *
     * <p>Creates a train with the selected operator, date, time, route and
     * locomotives, then generates the schedule and updates the details
     * table for preview. Switches to the secondary view to show the
     * schedule details.</p>
     *
     * @param event the action event triggered by the user
     */
    public void generateScheduleOnAction(ActionEvent event) {
        if (!verifications(event)){
            return;
        }

        controller.createTrain(operatorComboBox.getSelectionModel().getSelectedItem(), dateDatePicker.getValue(), hourSpinner, minSpinner, routeTableView.getSelectionModel().getSelectedItem(), new ArrayList<>(locomotiveTableView.getSelectionModel().getSelectedItems()));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        uiUtils.showAlert(stage, "Schedule Generated for Preview",  "success");


        controller.generateSchedule();


        loadScheduleDetailsTable();
        modifyView(false);
    }

    /**
     * Modify which anchor pane is visible.
     *
     * @param viewType {@code true} to show primary view, {@code false} to show secondary view
     */
    private void modifyView(boolean viewType) {

        if (viewType){
            // View Primaria
            primaryAnchorPane.setVisible(true);
            secundaryAnchorPane.setVisible(false);
        }else{
            // View Secundaria
            primaryAnchorPane.setVisible(false);
            secundaryAnchorPane.setVisible(true);
        }
    }

    /**
     * Verify that the minimal inputs required to generate a schedule are
     * present; presents an alert when a validation check fails.
     *
     * @param event the action event used to obtain a Stage for alerts
     * @return {@code true} when inputs are valid; {@code false} otherwise
     */
    private boolean verifications(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (operatorComboBox.getSelectionModel().getSelectedItem() == null){
            uiUtils.showAlert(stage, "Select an Operator first",  "error");
            return false;
        }

        if (dateDatePicker.getValue() == null){
            uiUtils.showAlert(stage, "Select a Date first",  "error");
            return false;
        }

        if (routeTableView.getSelectionModel().getSelectedItem() == null){
            uiUtils.showAlert(stage, "Select a route first",  "error");
            return false;
        }

        if (locomotiveTableView.getSelectionModel().getSelectedItems().isEmpty()){
            uiUtils.showAlert(stage, "Select a Locomotive first",  "error");
            return false;
        }

        return true;
    }

    /**
     * Assign the generated train to the system after verifying preconditions.
     *
     * <p>This method delegates assignment to the controller and displays a
     * success alert when the operation completes.</p>
     *
     * @param event the action event supplied by the UI
     */
    public void assignTrainOnAction(ActionEvent event) {
        if (!verificationsAssign(event)){
            return;
        }

        controller.assignTrain();

        generalUpdate();

        controller.setGeneratedTrain(null);
        controller.setGeneralSchedule(null);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        uiUtils.showAlert(stage, "Train Assigned",  "success");
    }

    /**
     * Refresh UI tables after an assignment operation.
     */
    private void generalUpdate() {
        updateRouteTable();
        updateLocomotiveTable();
    }

    /**
     * Verify that a generated schedule and train exist before assignment.
     *
     * @param event the action event used to obtain a Stage for alerts
     * @return {@code true} when preconditions are satisfied; {@code false} otherwise
     */
    private boolean verificationsAssign(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (controller.getGeneratedTrain() == null){
            uiUtils.showAlert(stage, "Generate a Schedule First",  "error");
            return false;
        }

        if (controller.getGeneralSchedule() == null){
            uiUtils.showAlert(stage, "Generate a Schedule First",  "error");
            return false;
        }

        return true;
    }

    @FXML
    /**
     * Show a short help popup explaining route information requirements.
     *
     * @param event the action event supplied by the UI
     */
    public void infoRouteButtonOnAction(ActionEvent event) {
        uiUtils.showInfoPopup(event,
                "Choose a Date to View Available Routes",3);
    }

    @FXML
    /**
     * Show a short help popup explaining locomotive availability.
     *
     * @param event the action event supplied by the UI
     */
    public void infoLocomotivesButtonOnAction(ActionEvent event) {
        uiUtils.showInfoPopup(event,
                "Choose a Date to View Available Locomotives",3);
    }
}

