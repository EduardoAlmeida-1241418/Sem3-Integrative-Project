package pt.ipp.isep.dei.ui.gui.analyst;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import pt.ipp.isep.dei.controller.analyst.AnalystProximitySearchController;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.TimeZone;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the analyst proximity search user interface.
 *
 * <p>This JavaFX controller initialises the UI helpers and the underlying
 * proximity search controller, configures the stations table and time zone
 * selector, and handles user actions such as searching for nearby stations,
 * opening the map view and navigating between scenes.</p>
 *
 * <p>The class is concerned solely with presentation and user interaction;
 * it delegates business logic to {@link AnalystProximitySearchController}
 * and scene changes to {@link UIUtils}.</p>
 */
public class AnalystProximitySearchGUI implements Initializable {

    private UIUtils uiUtils;
    private AnalystProximitySearchController controller;

    @FXML
    private TableView<StationEsinf> stationTableView;

    @FXML
    private TableColumn<StationEsinf, String> stationNameTableColumn, tzgTableColumn, countryTableColumn, latitudeTableColumn, longitudeTableColumn;

    @FXML
    private TextField latitudeMinTextField, longitudeMinTextField, nStationsTextField;

    @FXML
    private Label messageLabel;

    @FXML
    private ComboBox<String> tzgComboBox;

    /**
     * Initialise the controller and supporting utilities.
     *
     * @param url not used by this implementation
     * @param resourceBundle not used by this implementation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
        this.controller = new AnalystProximitySearchController();

        configureStationsTableView();
        configureTimeZoneComboBox();
    }

    /**
     * Configure the table columns to display station properties.
     *
     * <p>Each column's cell value factory extracts the appropriate value from
     * a {@link StationEsinf} instance and formats it for display.</p>
     */
    private void configureStationsTableView() {
        stationNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStationName()));

        tzgTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimeZone().getZoneId()));

        countryTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry().toString()));

        latitudeTableColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("%.5f", data.getValue().getLatitude())));

        longitudeTableColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("%.5f", data.getValue().getLongitude())));
    }

    /**
     * Populate the time zone combo box with available time zone groups.
     */
    private void configureTimeZoneComboBox() {
        tzgComboBox.setItems(controller.getAllTimeZones());
    }

    /**
     * Perform the proximity search using the values supplied in the UI.
     *
     * <p>Validates the input fields, parses numeric values, configures the
     * controller and updates the stations table with the k-nearest results.
     * If input parsing fails a temporary error message is shown to the user.</p>
     *
     * @param event the action event triggered by the search button
     */
    @FXML
    public void searchButtonOnAction(ActionEvent event) {
        if (latitudeMinTextField.getText().isEmpty() || longitudeMinTextField.getText().isEmpty() || nStationsTextField.getText().isEmpty()) {
            showTemporaryMessage("Please fill in all fields.", Color.RED);
            return;
        }

        if (tzgComboBox.getValue() == null) {
            showTemporaryMessage("Please select a time zone group.", Color.RED);
            return;
        }

        try {
            double latitude = Double.parseDouble(latitudeMinTextField.getText());
            double longitude = Double.parseDouble(longitudeMinTextField.getText());
            int nStations = Integer.parseInt(nStationsTextField.getText());

            controller.setSearchLatitude(latitude);
            controller.setSearchLongitude(longitude);
            controller.setkNearest(nStations);

            if (!tzgComboBox.getValue().equals("All Time Zones")) {
                controller.setTimeZone(TimeZone.valueOf(tzgComboBox.getValue().replace('/', '_').toUpperCase()));
            } else controller.setTimeZone(null);

            controller.setKd2TreeNodes();

            stationTableView.setItems(FXCollections.observableArrayList(controller.getKD2TreeStationNodes()));
        } catch (NumberFormatException e) {
            showTemporaryMessage("Invalid input. Please enter valid numbers for latitude, longitude, and number of stations.", Color.RED);
        }
    }

    /**
     * Open the map view and pass the current search context to it.
     *
     * @param event the action event that triggered opening the map
     */
    public void mapButtonOnAction(ActionEvent event) {
        FXMLLoader loader = uiUtils.loadFXMLScene(event, "/fxml/analyst/analystViewMapaMundi.fxml","MABEC - Planner - View Map");

        AnalystViewMapaMundiGUI controller = loader.getController();
        controller.loadControllerInfo(this.controller.getKd2TreeNodes(), this.controller.getSearchLatitude(), this.controller.getSearchLongitude());
    }

    /**
     * Navigate to the analyst home page.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/analyst/analystHomePage.fxml", "MABEC - Analyst - Home Page");
    }

    /**
     * Navigate to the GQS logs view for the analyst.
     *
     * @param actionEvent the triggering action event
     */
    @FXML
    public void handleWmsLogsButton(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/analyst/analystGQS.fxml", "MABEC - Analyst - GQS Logs");
    }

    /**
     * Log out the current user and return to the visitor home page.
     *
     * @param event the action event that triggered the logout
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Display a temporary message to the user, which fades away after a short time.
     *
     * @param message the text to display
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
