package pt.ipp.isep.dei.ui.gui.freightManager;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;
import pt.ipp.isep.dei.controller.freightManager.FreightManagerCreateRoutesController;
import pt.ipp.isep.dei.domain.Facility;
import pt.ipp.isep.dei.domain.transportationRelated.RouteType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * GUI controller used by Freight Managers to create and manage routes.
 *
 * <p>This JavaFX controller initialises and populates the tables that allow
 * a user to select a start station, choose successive stations and assemble
 * a route. It also exposes controls for choosing a route type and
 * for creating the final route. All business logic is delegated to the
 * {@link FreightManagerCreateRoutesController}; this class concerns itself
 * solely with presentation and user interaction.</p>
 *
 */
public class FreightManagerCreateRoutesGUI implements Initializable {

    private UIUtils uiUtils;
    private FreightManagerCreateRoutesController controller;

    @FXML
    private TableView<Facility> startStationTableView, nextStationTableView, actualRouteTableView;

    @FXML
    private TableColumn<Facility, String> iDStartStationTableColumn, nameStartStationTableColumn,
            idNextStationTableColumn, nameNextStationTableColumn, nameActualRouteTableColumn;

    @FXML
    private Label errorLabel, actualStationLabel, actualStationName;

    @FXML
    private ComboBox<String> routeTypeComboBox;

    /**
     * Initialise the GUI controller and supporting helpers.
     *
     * @param url resource location (not used)
     * @param resourceBundle resources (not used)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
        this.controller = new FreightManagerCreateRoutesController();

        loadGuiItems();
    }

    /**
     * Load and configure all GUI items (tables and combo box).
     */
    private void loadGuiItems() {
        loadTableItems();
        loadComboBoxItems();
    }

    /**
     * Populate the route-type {@link ComboBox} with available types.
     */
    private void loadComboBoxItems() {
        routeTypeComboBox.getItems().addAll(
                Arrays.stream(RouteType.values()).map(Enum::name).toList()
        );
    }

    /**
     * Configure table columns and register listeners for selection events.
     *
     * <p>When a start station is selected the adjacent facilities are shown
     * in the "next station" table.</p>
     */
    private void loadTableItems() {
        iDStartStationTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        nameStartStationTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getName()))
        );

        idNextStationTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        nameNextStationTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getName()))
        );

        nameActualRouteTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getName()))
        );

        ObservableList<Facility> stationList = FXCollections.observableArrayList(controller.getAllFacilities());

        startStationTableView.setItems(stationList);

        startStationTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                nextStationTableView.setItems(controller.getAdjacentFacilities(newValue));
            }
        });
    }

    /**
     * Navigate to Freight Manager home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerHomePage.fxml","MABEC - Freight Manager - Home Page");
    }

    /**
     * Navigate to Freight Manager trains view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void trainsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/TrainRelated/freightManagerLocomotive.fxml","MABEC - Freight Manager - Trains");
    }

    /**
     * Navigate to the scheduler view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void schedulerOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerScheduler.fxml","MABEC - Freight Manager - Scheduler");
    }

    /**
     * Navigate to the Freight Manager GQS / NOS logs view.
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
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml","MABEC - Home Page");
    }

    /**
     * Create a new point of the route joining the currently selected
     * stations.
     *
     * <p>The method validates that a start station and a next station are
     * selected and then appends the chosen facility to the current route.</p>
     *
     * @param event the action event supplied by the UI
     */
    public void createPointOfRouteButtonOnAction(ActionEvent event) {

        if (startStationTableView.getSelectionModel().getSelectedItem() == null && controller.getActualRoute().isEmpty()) {
            showTemporaryMessage("Please select a start station", "red", 3);
            return;
        }

        if (nextStationTableView.getSelectionModel().getSelectedItem() == null) {
            showTemporaryMessage("Please select a next station", "red", 3);
            return;
        }

        if (controller.getActualRoute().isEmpty()) {
            addFirstStationMethods();
        }

        controller.addFacility(nextStationTableView.getSelectionModel().getSelectedItem());
        showTemporaryMessage("Point Of Route created", "green", 3);

        loadTableNewTableItems();
    }

    /**
     * Refresh the displayed tables when a new station has been appended to
     * the route.
     */
    private void loadTableNewTableItems() {
        startStationTableView.setItems(FXCollections.observableArrayList(controller.getActualRoute().get(controller.getActualRoute().size() - 1)));

        nextStationTableView.setItems(controller.getAdjacentFacilities(controller.getActualRoute().get(controller.getActualRoute().size() - 1)));

        actualRouteTableView.setItems(FXCollections.observableArrayList(controller.getActualRoute()));
    }

    /**
     * Internal helper invoked when the first station is chosen: add the
     * station to the current route and update labels.
     */
    private void addFirstStationMethods() {
        controller.addFacility(startStationTableView.getSelectionModel().getSelectedItem());
        actualStationName.setText(startStationTableView.getSelectionModel().getSelectedItem().getName());
        actualStationLabel.setText("Actual Station:");
    }



    /**
     * Create the route after verifying that the route has at least two
     * stations and that a route type has been chosen.
     *
     * @param event the action event supplied by the UI
     */
    public void createRouteButtonOnAction(ActionEvent event) {
        if (controller.getActualRoute().size() < 2) {
            showTemporaryMessage("Please add at least 2 stations", "red", 3);
            return;
        }

        if (routeTypeComboBox.getSelectionModel().getSelectedItem() == null) {
            showTemporaryMessage("Please select a route type", "red", 3);
            return;
        }

        controller.createRoute(routeTypeComboBox.getSelectionModel().getSelectedItem());
        showTemporaryMessage("Route created successfully", "green", 3);

        resetGUI();
        controller.reset();
    }



    /**
     * Restore the GUI to its initial state after a route has been created
     * or when the user clears the current selection.
     */
    private void resetGUI() {
        // Limpa tabela inicial
        startStationTableView.setItems(FXCollections.observableArrayList(controller.getAllFacilities()));

        // Limpa next stations
        nextStationTableView.setItems(FXCollections.observableArrayList());

        // Limpa rota atual
        actualRouteTableView.setItems(FXCollections.observableArrayList());

        // Limpa labels
        actualStationLabel.setText("");
        actualStationName.setText("");

        // Limpa ComboBox
        routeTypeComboBox.getSelectionModel().clearSelection();

        // Limpa seleção da tabela inicial
        startStationTableView.getSelectionModel().clearSelection();
    }



    /**
     * Display a transient message in the GUI.
     *
     * @param message the message text to display
     * @param color CSS colour to apply to the message text
     * @param seconds duration in seconds before the message disappears
     */
    private void showTemporaryMessage(String message, String color, int seconds) {
        errorLabel.setStyle("-fx-text-fill: " + color + ";");
        errorLabel.setText(message);
        errorLabel.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(e -> {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        });
        pause.play();
    }
}
