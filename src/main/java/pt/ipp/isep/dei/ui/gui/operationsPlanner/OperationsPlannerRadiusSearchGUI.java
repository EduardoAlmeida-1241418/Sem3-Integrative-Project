package pt.ipp.isep.dei.ui.gui.operationsPlanner;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import pt.ipp.isep.dei.controller.operationsPlanner.OperationsPlannerRadiusSearchController;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Operations Planner radius search view.
 *
 * <p>This JavaFX controller manages the UI that allows an analyst to
 * search for stations within a given haversine radius around a central
 * point. It initialises the table and combo boxes, handles input
 * validation and delegates data retrieval and spatial computations to
 * {@link OperationsPlannerRadiusSearchController}.</p>
 *
 */
public class OperationsPlannerRadiusSearchGUI implements Initializable {

    private UIUtils uiUtils;
    private OperationsPlannerRadiusSearchController controller;

    @FXML
    private TableView<StationEsinf> stationsTableView;

    @FXML
    private TableColumn<StationEsinf, String> stationNameTableColumn, tzgTableColumn, countryTableColumn, isCityTableColumn, latitudeTableColumn, longitudeTableColumn;

    @FXML
    private TextField latitudeTextField, longitudeTextField, radiusTextField;

    @FXML
    private ComboBox<String> countryComboBox, isCityComboBox;

    @FXML
    private Label nStationsLabel, messageLabel;

    /**
     * Initialise the GUI controller.
     *
     * <p>Called by the JavaFX runtime when the FXML view is loaded. This
     * method creates the UI helper and domain controller, then configures
     * the table columns and combo boxes so the view is ready for use.</p>
     *
     * @param url resource location (not used)
     * @param resourceBundle resources for localisation (not used)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
        this.controller = new OperationsPlannerRadiusSearchController();

        configureTableColumns();
        configureComboBoxs();
    }

    /**
     * Populate input fields from previously supplied controller values.
     *
     * @param latitudeCenter textual latitude of the centre
     * @param longitudeCenter textual longitude of the centre
     * @param haversineRadius textual radius in kilometres
     */
    public void loadOldControllerInfo(String latitudeCenter, String longitudeCenter, String haversineRadius) {
        latitudeTextField.setText(String.valueOf(latitudeCenter));
        longitudeTextField.setText(String.valueOf(longitudeCenter));
        radiusTextField.setText(String.valueOf(haversineRadius));
    }

    /**
     * Configure the table's column cell value factories.
     *
     * <p>Each column extracts a displayable string from a
     * {@link StationEsinf} instance. The table selection model is set to
     * single selection to prevent accidental multi‑selection.</p>
     */
    private void configureTableColumns() {
        stationNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStationName()));
        tzgTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimeZoneGroup() + ""));
        countryTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry().getName()));
        isCityTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isIs_city() ? "Yes" : "No"));

        latitudeTableColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("%.5f", data.getValue().getLatitude())));

        longitudeTableColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("%.5f", data.getValue().getLongitude())));

        stationsTableView.getSelectionModel().setSelectionMode(null);
    }

    /**
     * Populate and configure combo boxes used by the view.
     *
     * <p>The city filter offers the options "All", "Yes" or "No" and is
     * initialised here. Country values are populated after a successful
     * search.</p>
     */
    private void configureComboBoxs() {
        isCityComboBox.getItems().addAll("All", "Yes", "No");
    }

    /**
     * Execute the radius search using the values supplied in the input
     * fields, validate inputs and update the table and filters.
     *
     * <p>Numeric parsing and domain validation are performed here. The
     * method delegates spatial indexing and retrieval to the underlying
     * controller. Informative temporary messages are shown to the user
     * when validation fails or when no stations are found.</p>
     *
     * @param event the action event triggered by the search button
     */
    @FXML
    public void searchButtonOnAction(ActionEvent event) {
        if (latitudeTextField.getText().isEmpty() || longitudeTextField.getText().isEmpty() || radiusTextField.getText().isEmpty()) {
            showTemporaryMessage("Please fill in all fields.", Color.RED);
            return;
        }

        try {
            double latitude = Double.parseDouble(latitudeTextField.getText());
            double longitude = Double.parseDouble(longitudeTextField.getText());
            double radius = Double.parseDouble(radiusTextField.getText());

            if (latitude < -90 || latitude > 90) {
                showTemporaryMessage("Latitude must be between -90 and 90.", Color.RED);
                return;
            }

            if (longitude < -180 || longitude > 180) {
                showTemporaryMessage("Longitude must be between -180 and 180.", Color.RED);
                return;
            }

            if (radius < 0 || radius > 20037) {
                showTemporaryMessage("Radius must be between 0 and 20037 km.", Color.RED);
                return;
            }

            if (latitude == controller.getLatitudeCenter() && longitude == controller.getLongitudeCenter() && radius == controller.getHaversineRadius()) {
                controller.setCountryFilter(countryComboBox.getValue() != null ? countryComboBox.getValue() : null);

                if (isCityComboBox.getValue() == null || isCityComboBox.getValue().equals("All")) {
                    controller.setCityFilter(null);
                } else controller.setCityFilter(isCityComboBox.getValue().equals("Yes"));

                stationsTableView.setItems(controller.getAscendingStationsByAvlTreeWithFilters());
                nStationsLabel.setText(stationsTableView.getItems().size() + "");
                return;
            }

            controller.setLatitudeCenter(latitude);
            controller.setLongitudeCenter(longitude);
            controller.setHaversineRadius(radius);
            controller.setAvlTreeStations();

            controller.setStationsList();
            stationsTableView.setItems(controller.getAscendingStationsByAvlTree());

            nStationsLabel.setText(stationsTableView.getItems().size() + "");

            if (stationsTableView.getItems().isEmpty()) {
                showTemporaryMessage("No stations found within the specified radius.", Color.RED);
                return;
            }

            countryComboBox.getSelectionModel().clearSelection();
            List<String> countryNames = controller.getAllCountriesNames();
            if (countryNames.size() > 2) {
                countryComboBox.setDisable(false);
                countryComboBox.getItems().clear();
                countryComboBox.getItems().addAll(countryNames);
                countryComboBox.getSelectionModel().selectFirst();
            } else {
                countryComboBox.getSelectionModel().select(countryNames.get(1));
                countryComboBox.setDisable(true);
            }

            isCityComboBox.getSelectionModel().clearSelection();
            if (controller.hasCityAndNoCityStations()) {
                isCityComboBox.setDisable(false);
                isCityComboBox.getSelectionModel().selectFirst();
            } else {
                String isCity = controller.getStationsList().getFirst().isIs_city() ? "Yes" : "No";
                isCityComboBox.getSelectionModel().select(isCity);
                isCityComboBox.setDisable(true);
            }

            controller.setCountryFilter(null);
            controller.setCityFilter(null);

        } catch (NumberFormatException e) {
            showTemporaryMessage("Please enter valid numeric values for latitude, longitude, and radius.", Color.RED);
        }
    }

    /**
     * Open the world map view and pass the current search context to it.
     *
     * @param event the action event triggered by the map button
     */
    @FXML
    public void mapButtonOnAction(ActionEvent event) {
        FXMLLoader loader = uiUtils.loadFXMLScene(event, "/fxml/operationsPlanner/operationsPlannerViewMapaMundi.fxml", "MABEC - Operations Planner - View Map");
        OperationsPlannerViewMapaMundiGUI controller = loader.getController();
        controller.loadControllerInfo(stationsTableView.getItems(), this.controller.getLatitudeCenter(), this.controller.getLongitudeCenter(), latitudeTextField.getText(), longitudeTextField.getText(), radiusTextField.getText());
    }

    /**
     * Log out the current user and return to the public home page.
     *
     * @param event the action event triggered by the UI control
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Navigate back to the Operations Planner home page.
     *
     * @param event the action event triggered by the UI control
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/operationsPlanner/operationsPlannerHomePage.fxml", "MABEC - Operations Planner - Home Page");
    }

    /**
     * Navigate to the Operations Planner GQS logs view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void handleWmsLogsButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/operationsPlanner/operationsPlannerGQS.fxml", "MABEC - Operations Planner - GQS Logs");
    }

    /**
     * Display a temporary message to the user that fades away after a
     * short duration.
     *
     * @param message the message text to display
     * @param color the text colour to use
     */
    private void showTemporaryMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setTextFill(color);
        messageLabel.setOpacity(1.0);
        messageLabel.setVisible(true);
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            FadeTransition fade = new FadeTransition(Duration.seconds(1), messageLabel);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(ev -> messageLabel.setVisible(false));
            fade.play();
        }));
        delay.play();
    }
}