package pt.ipp.isep.dei.ui.gui.freightManager.trainRelated;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.freightManager.FreightManagerTrainsController;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the Freight Manager trains screen.
 *
 * <p>This JavaFX controller manages the user interface used by Freight
 * Managers to select locomotives and schedule train departures. It initialises
 * table views and input controls (operator selector, date picker and time
 * spinners), validates user input and forwards the user to the freight
 * selection screen when a new train is to be created.</p>
 *
 * <p>The class is solely responsible for presentation and UI flow; all
 * business logic is delegated to {@link FreightManagerTrainsController}.
 */
public class FreightManagerTrainsGUI implements Initializable {

    private static final int MAX_HOURS = 23;
    private static final int MAX_MINUTES = 59;


    @FXML
    private TableView<Locomotive> locomotiveTableView;

    @FXML
    private ComboBox<Operator> operatorComboBox;

    @FXML
    private Spinner<Integer> hourSpinner, minuteSpinner;

    @FXML
    private DatePicker typeDatePicker;

    @FXML
    private TableColumn<Locomotive, String> nameTableColumn, powerTableColumn, maxSpeedTableColumn, typeTableColumn, gaugeTableColumn;

    private FreightManagerTrainsController controller;

    private UIUtils uiUtils;

    /**
     * Initialise the controller and supporting utilities.
     *
     * <p>Invoked by the JavaFX runtime after the FXML has been loaded. The
     * method creates helper objects and initialises the UI components.</p>
     *
     * @param url resource location (unused)
     * @param resourceBundle resources (unused)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
        controller = new FreightManagerTrainsController();

        initGUI();
    }

    /**
     * Initialise the GUI components (tables, combo box and spinners).
     */
    private void initGUI() {
        initLocomotiveTable();
        initComboBox();
        initSpinner();
    }

    /**
     * Initialise the hour and minute spinners with their valid ranges.
     */
    private void initSpinner() {
        hourSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, MAX_HOURS, 0)
        );

        minuteSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, MAX_MINUTES, 0)
        );
    }

    /**
     * Configure the operator combo box and the way operator items are
     * displayed in the drop-down and button cell.
     */
    private void initComboBox() {

        operatorComboBox.setItems(controller.getAllOperators());

        operatorComboBox.setCellFactory(cb -> new ListCell<Operator>() {
            @Override
            protected void updateItem(Operator operator, boolean empty) {
                super.updateItem(operator, empty);
                setText(empty || operator == null ? null : operator.getShortName());
            }
        });

        operatorComboBox.setButtonCell(new ListCell<Operator>() {
            @Override
            protected void updateItem(Operator operator, boolean empty) {
                super.updateItem(operator, empty);
                setText(empty || operator == null ? null : operator.getShortName());
            }
        });
    }




    /**
     * Configure and populate the locomotive table columns.
     *
     * <p>Each column extracts a human-readable value from the
     * {@link Locomotive} instances supplied by the controller.</p>
     */
    private void initLocomotiveTable() {
        nameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getName()))
        );

        powerTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getLocomotiveModel().getPower()))
        );

        maxSpeedTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getLocomotiveModel().getMaxSpeed()))
        );

        typeTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getLocomotiveModel().getFuelType()))
        );

        gaugeTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getLocomotiveModel().getTrackGauges().getFirst().getGaugeSize()))
        );


        ObservableList<Locomotive> locomotiveList = FXCollections.observableArrayList(controller.getAllLocomotives());

        locomotiveTableView.setItems(locomotiveList);
        locomotiveTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Navigate to the Freight Manager home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerHomePage.fxml", "MABEC - Freight Manager - Home Page");
    }

    /**
     * Navigate to the route management view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void routeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/RouteRelated/freightManagerRoute.fxml", "MABEC - Freight Manager - Route");
    }

    /**
     * Navigate to the scheduler view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void schedulerOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerScheduler.fxml", "MABEC - Freight Manager - Scheduler");
    }

    /**
     * Navigate to the GQS / NOS logs view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerGQS_NOS.fxml", "MABEC - Freight Manager - GQS / NOS Logs");
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
     * Create a train using the selected operator, date, time and locomotives.
     *
     * <p>The method validates the inputs and if validation passes it opens the
     * freight selection view and forwards the selected information to that
     * controller.</p>
     *
     * @param event the action event supplied by the UI
     */
    public void createTrainButtonOnAction(ActionEvent event) {
        if (!verifyAll(event)) {
            return;
        }

        FXMLLoader loader =  uiUtils.loadFXMLScene(event, "/fxml/freightManager/TrainRelated/freightManagerFreight.fxml", "MABEC - Freight Manager - Choose Freight");
        FreightManagerFreightGUI controller = loader.getController();

        LocalDate localDate = typeDatePicker.getValue();
        Date date = new Date(localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
        Operator op= operatorComboBox.getValue();
        controller.setControllerInfo(op, date, new Time(hourSpinner.getValue(), minuteSpinner.getValue(), 0), new ArrayList<>( locomotiveTableView.getSelectionModel().getSelectedItems()));
    }

    /**
     * Verify user input prior to creating a train.
     *
     * <p>If any required input is missing an informative alert is shown to the
     * user and the method returns {@code false}.</p>
     *
     * @param event the action event supplied by the UI
     * @return {@code true} when all required inputs are present
     */
    private boolean verifyAll(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (operatorComboBox.getValue() == null || operatorComboBox.getItems().isEmpty()) {
            UIUtils.showAlert(stage, "Choose an Owner First.", "error");
            return false;
        }

        if (typeDatePicker.getValue() == null) {
            UIUtils.showAlert(stage, "Choose a Departure Date.", "error");
            return false;
        }

        if (hourSpinner.getValue() == null) {
            UIUtils.showAlert(stage, "Choose a departure Time.", "error");
            return false;
        }

        if (hourSpinner.getValue() == null) {
            UIUtils.showAlert(stage, "Choose a departure time.", "error");
            return false;
        }

        if (minuteSpinner.getValue() == null) {
            UIUtils.showAlert(stage, "Choose a departure time.", "error");
            return false;
        }

        if (locomotiveTableView.getSelectionModel().getSelectedItems().isEmpty()) {
            UIUtils.showAlert(stage, "Choose at least one Locomotive.", "error");
            return false;
        }

        return true;
    }
}