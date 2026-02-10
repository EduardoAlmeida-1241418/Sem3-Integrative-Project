package pt.ipp.isep.dei.ui.gui.planner;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import pt.ipp.isep.dei.controller.planner.PlannerStationSearchController;
import pt.ipp.isep.dei.domain.Country;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.TimeZoneGroup;
import pt.ipp.isep.dei.domain.Tree.TreeType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Planner station search view.
 *
 * <p>This JavaFX controller manages the user interface used by planners to
 * search and inspect stations by various spatial indices (latitude tree,
 * longitude tree and time‑zone group trees). It initialises UI components,
 * populates choice boxes and table views, validates user input and
 * delegates data retrieval to {@link PlannerStationSearchController}.</p>
 *
 */
public class PlannerStationSearchGUI implements Initializable {

    private final double LOWER_VAL_LAT = -90;
    private final double HIGHER_VAL_LAT = 90;
    private final double LOWER_VAL_LON = -180;
    private final double HIGHER_VAL_LON = 180;

    private final String COUNTRY_COMBOBOX_NONE = "Country";
    private final String TZG_COMBOBOX_NONE = "Time Zone 1";
    private final String TZG_COMBOBOX_NONE_2 = "Time Zone 2";

    private PlannerStationSearchController controller;
    private final UIUtils uiUtils = new UIUtils();



    @FXML
    private TableView<StationEsinf> stationTableView;

    @FXML
    private TableColumn<StationEsinf, String> stationNameTableColumn, tzgTableColumn, countryTableColumn, coodinatesTableColumn;

    @FXML
    private ComboBox<String> treeComboBox, countryComboBox, tzg1ComboBox, tzg2ComboBox;

    @FXML
    private AnchorPane tzgSearchAnchorPane, coordSearchAnchorPane;

    @FXML
    private TextField highValTextField, lowerValTextField;

    @FXML
    private Label errorLabel, quantityFoundLabel;

    /**
     * Initialise the controller.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new PlannerStationSearchController();

        loadGuiInfo();
    }

    /**
     * Load graphical user‑interface information: combo box values and the
     * initial table view content.
     */
    private void loadGuiInfo() {
        loadComboBoxValues();
        loadTableView();

    }

    /**
     * Populate the combo boxes used to filter station searches.
     *
     * <p>The method initialises country and time‑zone group selections and
     * sets up a listener on the tree type combo box to update the UI when
     * the selected tree type changes.</p>
     */
    private void loadComboBoxValues() {

        // Country ComboBox
        countryComboBox.getItems().add(COUNTRY_COMBOBOX_NONE); // opção default
        for (Country country : controller.getCountryList()) {
            countryComboBox.getItems().add(country.getName());
        }
        countryComboBox.setValue(COUNTRY_COMBOBOX_NONE); // valor inicial

        // TimeZoneGroup ComboBoxes
        tzg1ComboBox.getItems().add(TZG_COMBOBOX_NONE);
        tzg2ComboBox.getItems().add(TZG_COMBOBOX_NONE_2);
        for (TimeZoneGroup timeZoneGroup : TimeZoneGroup.values()) {
            tzg1ComboBox.getItems().add(timeZoneGroup.toString());
            tzg2ComboBox.getItems().add(timeZoneGroup.toString());
        }
        tzg1ComboBox.setValue(TZG_COMBOBOX_NONE);
        tzg2ComboBox.setValue(TZG_COMBOBOX_NONE_2);

        // TreeType ComboBox
        for (TreeType treeType : controller.getTreeTypes()) {
            treeComboBox.getItems().add(treeType.toString());
        }
        treeComboBox.setValue(TreeType.LAT_TREE.toString());

        treeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                clearFields();
                updateAnchorPaneView(newValue);
                updateTableData(newValue);
            }
        });
    }


    /**
     * Clear input fields and reset combo box selections to their default
     * state.
     */
    private void clearFields() {
        highValTextField.setText("");
        lowerValTextField.setText("");

        countryComboBox.setValue(COUNTRY_COMBOBOX_NONE);


    }


    /**
     * Update which anchor pane is visible depending on the selected tree
     * type. Latitude/longitude searches show coordinate inputs whilst
     * time‑zone searches show TZG inputs.
     *
     * @param selectedTree textual representation of the chosen tree type
     */
    private void updateAnchorPaneView(String selectedTree) {

        if (selectedTree.equals(TreeType.LAT_TREE.toString()) || selectedTree.equals(TreeType.LON_TREE.toString())) {
            tzgSearchAnchorPane.setVisible(false);
            coordSearchAnchorPane.setVisible(true);
            return;
        }

        if (selectedTree.equals(TreeType.TZG_TREE.toString())) {
            coordSearchAnchorPane.setVisible(false);
            tzgSearchAnchorPane.setVisible(true);
        }

    }

    /**
     * Configure the stations table columns and populate the initial list
     * of stations according to the selected tree type.
     */
    private void loadTableView() {
        stationNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStationName()));

        countryTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry().getName()));

        tzgTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimeZoneGroup().toString()));

        coodinatesTableColumn.setCellValueFactory(cellData -> {
            StationEsinf station = cellData.getValue();
            String text = String.format("Lat: %.5f%nLon: %.5f", station.getLatitude(), station.getLongitude());
            return new SimpleStringProperty(text);
        });
        ObservableList<StationEsinf> stationList = FXCollections.observableArrayList(controller.getStations(treeComboBox.getSelectionModel().getSelectedItem()));
        stationTableView.setItems(stationList);

        updateQuantityLabel(stationList.size());

    }


    /**
     * Refresh the table data using the supplied tree selection (textual).
     *
     * @param selectedTree textual representation of the desired tree type
     */
    private void updateTableData(String selectedTree) {
        ObservableList<StationEsinf> newData = FXCollections.observableArrayList(controller.getStations(selectedTree));
        stationTableView.setItems(newData);

        updateQuantityLabel(newData.size());

    }

    /**
     * Refresh the table data using an explicit list of stations.
     *
     * @param stations list of stations to display in the table
     */
    private void updateTableData(java.util.List<StationEsinf> stations) {
        ObservableList<StationEsinf> newData = FXCollections.observableArrayList(stations);
        stationTableView.setItems(newData);

        updateQuantityLabel(newData.size());

    }

    /**
     * Update the quantity label used to display the number of stations
     * currently shown in the table.
     *
     * @param size number of stations
     */
    private void updateQuantityLabel(int size) {
        quantityFoundLabel.setText(String.format("Found: %d Stations", size));
    }


    /**
     * Navigate to the public home page and log out the user.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Navigate to the Planner home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerHomePage.fxml", "MABEC - Planner - Home Page");
    }

    /**
     * Open the Planner trolleys view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void trolleysOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerTrolleys.fxml", "MABEC - Planner - Trolleys");
    }

    /**
     * Open the Planner picking plans view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void pickingPlansOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerPickingPlans.fxml", "MABEC - Planner - Picking Plans");
    }

    /**
     * Open the Planner WMS/GQS logs view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerWMS_GQS.fxml", "MABEC - Planner - WMS / GQS Logs");
    }

    /**
     * Open the Planner geographical area view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void handleGeographicalArea(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerGeographicalArea.fxml", "MABEC - Planner - Geographical Area");
    }

    /**
     * Perform a coordinate range search and update the table with the
     * resulting stations.
     *
     * <p>Input validation is performed by {@link #validateCoordInputItems()}.
     * If validation fails the method returns without modifying the
     * displayed table.</p>
     *
     * @param event the action event supplied by the UI
     */
    public void searchCoordButtonOnAction(ActionEvent event) {
        if(!validateCoordInputItems()){ // Error in verifications
            return;
        }

        updateTableData(controller.getStationInCoordZone(Double.parseDouble(highValTextField.getText()),  Double.parseDouble(lowerValTextField.getText()), treeComboBox.getSelectionModel().getSelectedItem()));
    }

    /**
     * Perform a time‑zone group search and update the table with the
     * resulting stations.
     *
     * @param actionEvent the action event supplied by the UI
     */
    public void searchTZGButtonOnAction(ActionEvent actionEvent) {
        if(!validateTZGInputItems()){
            return;
        }

        updateTableData(controller.getStationInTZG(countryComboBox.getSelectionModel().getSelectedItem(), tzg1ComboBox.getSelectionModel().getSelectedItem(), tzg2ComboBox.getSelectionModel().getSelectedItem()));


    }

    /**
     * Validate the time‑zone group input controls and display an informative
     * message if the input is incomplete.
     *
     * @return {@code true} when inputs are valid; {@code false} otherwise
     */
    private boolean validateTZGInputItems() {
        boolean countryEmpty = countryComboBox.getSelectionModel().isEmpty() || countryComboBox.getValue().equals(COUNTRY_COMBOBOX_NONE);
        boolean tzg1Empty = tzg1ComboBox.getSelectionModel().isEmpty() || tzg1ComboBox.getValue().equals(TZG_COMBOBOX_NONE);
        boolean tzg2Empty = tzg2ComboBox.getSelectionModel().isEmpty() || tzg2ComboBox.getValue().equals(TZG_COMBOBOX_NONE_2);

        if (tzg1Empty && tzg2Empty && countryEmpty) {
            showTemporaryError("Please select a Time Zone Group or Country", "red");
            return false;
        }

        return true;
    }



    /**
     * Validate the coordinate input fields for numeric range and ordering.
     *
     * <p>The method checks presence, numeric parsing and that the higher
     * value exceeds the lower value. It also verifies latitude/longitude
     * bounds according to the selected tree type.</p>
     *
     * @return {@code true} when inputs are valid; otherwise {@code false}
     */
    private boolean validateCoordInputItems() {
        if (highValTextField.getText().isEmpty() || lowerValTextField.getText().isEmpty()) {
            showTemporaryError("Fill both fields", "red");
            return false;
        }

        double high = 0;
        double low = 0;
        try {
            high = Double.parseDouble(highValTextField.getText());
            low = Double.parseDouble(lowerValTextField.getText());
        } catch (NumberFormatException e) {
            showTemporaryError("One or more values are not numbers", "red");
            return false;
        }

        if (high <= low){
            showTemporaryError("Higher value must be greater than lower value", "red");
            return false;
        }

        if(treeComboBox.getSelectionModel().getSelectedItem().equals(TreeType.LAT_TREE.toString())){
            if (high > HIGHER_VAL_LAT || low < LOWER_VAL_LAT ){
                showTemporaryError("Latitude values must be between -90 and 90", "red");
                return false;
            }
        } else if(treeComboBox.getSelectionModel().getSelectedItem().equals(TreeType.LON_TREE.toString())){
            if (high > HIGHER_VAL_LON || low < LOWER_VAL_LON ){
                showTemporaryError("Longitude values must be between -180 and 180", "red");
                return false;
            }
        }

        return true;
    }

    /**
     * Show a short temporary error message to the user.
     *
     * @param message the message to display
     * @param color the CSS colour to use for the text
     */
    private void showTemporaryError(String message, String color) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: " + color + ";");

        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
        pause.setOnFinished(event -> errorLabel.setText(""));
        pause.play();
    }

    /**
     * Open the world map view initialised with the current search results.
     *
     * @param event the action event supplied by the UI
     */
    public void mapButtonOnAction(ActionEvent event) {
        FXMLLoader loader = uiUtils.loadFXMLScene(event, "/fxml/planner/plannerViewMapaMundi.fxml","MABEC - Planner - View Map");

        PlannerViewMapaMundiGUI controller = loader.getController();
        controller.loadControllerInfo(this.controller.getStationsActualList(), "/fxml/planner/plannerStationSearch.fxml", "MABEC - Planner - Search Stations");
    }
}
