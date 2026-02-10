package pt.ipp.isep.dei.ui.gui.planner;

import pt.ipp.isep.dei.controller.planner.GeoSearchController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.*;

public class PlannerGeographicalAreaGUI implements Initializable {

    private final UIUtils uiUtils = new UIUtils();

    private List<StationEsinf> allStationsRoot = new ArrayList<>();

    private final GeoSearchController controller = new GeoSearchController();

    @FXML private TextField latitudeMinTextField;
    @FXML private TextField latitudeMaxTextField;
    @FXML private TextField longitudeMinTextField;
    @FXML private TextField longitudeMaxTextField;
    @FXML private ComboBox<String> treeComboBox1;   // Country
    @FXML private ComboBox<String> treeComboBox11;  // Type
    @FXML private TableView<StationEsinf> stationTableView;
    @FXML private TableColumn<StationEsinf, String> stationNameTableColumn;
    @FXML private TableColumn<StationEsinf, String> tzgTableColumn;
    @FXML private TableColumn<StationEsinf, String> countryTableColumn;
    @FXML private TableColumn<StationEsinf, String> coodinatesTableColumn;
    @FXML private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        treeComboBox11.setItems(FXCollections.observableArrayList("All", "Cities", "Main Stations", "Airports"));
        treeComboBox11.getSelectionModel().selectFirst();

        treeComboBox1.setItems(FXCollections.observableArrayList());
        treeComboBox1.setPromptText("Country");

        errorLabel.setText("");

        stationNameTableColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getStationName()));

        tzgTableColumn.setCellValueFactory(param -> {
            if (param.getValue().getTimeZoneGroup() != null) {
                return new SimpleStringProperty(param.getValue().getTimeZoneGroup().toString());
            } else {
                return new SimpleStringProperty("-");
            }
        });

        countryTableColumn.setCellValueFactory(param -> {
            if (param.getValue().getCountry() != null) {
                return new SimpleStringProperty(param.getValue().getCountry().toString());
            } else {
                return new SimpleStringProperty("-");
            }
        });

        coodinatesTableColumn.setCellValueFactory(param ->
                new SimpleStringProperty("Lat: " + String.format("%.5f", param.getValue().getLatitude())
                        + " | Lon: " + String.format("%.5f", param.getValue().getLongitude())));
    }

    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerHomePage.fxml", "MABEC - Planner - Home Page");
    }

    @FXML
    public void trolleysOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerTrolleys.fxml", "MABEC - Planner - Trolleys");
    }

    @FXML
    public void pickingPlansOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerPickingPlans.fxml", "MABEC - Planner - Picking Plans");
    }

    @FXML
    public void searchStationButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerStationSearch.fxml", "MABEC - Planner - Search Stations");
    }

    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerWMS_GQS.fxml", "MABEC - Planner - WMS / GQS Logs");
    }

    private void populateCountryComboBox(List<StationEsinf> stations) {
        Set<String> countries = new TreeSet<>();

        for (StationEsinf s : stations) {
            if (s.getCountry() != null) {
                countries.add(s.getCountry().toString());
            }
        }

        List<String> comboList = new ArrayList<>();
        comboList.add("All");
        comboList.addAll(countries);

        treeComboBox1.setItems(FXCollections.observableArrayList(comboList));
        treeComboBox1.getSelectionModel().selectFirst();
    }

    @FXML
    public void searchButtonOnAction(ActionEvent event) {
        if (allStationsRoot == null || allStationsRoot.isEmpty()) {
            errorLabel.setText("No stations loaded. Please use the 'Find Station' button first.");
            errorLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        String selectedCountry = treeComboBox1.getValue();
        String selectedType = treeComboBox11.getValue();

        List<StationEsinf> filtered = new ArrayList<>(allStationsRoot);

        if (selectedCountry != null && !"Country".equals(selectedCountry) && !"All".equalsIgnoreCase(selectedCountry)) {
            List<StationEsinf> temp = new ArrayList<>();
            for (StationEsinf s : filtered) {
                if (s.getCountry() != null && s.getCountry().toString().equalsIgnoreCase(selectedCountry)) {
                    temp.add(s);
                }
            }
            filtered = temp;
        }

        if (selectedType != null && !"Type".equals(selectedType) && !"All".equalsIgnoreCase(selectedType)) {
            List<StationEsinf> temp = new ArrayList<>();
            for (StationEsinf s : filtered) {
                if ("Cities".equals(selectedType) && s.isIs_city()) temp.add(s);
                else if ("Main Stations".equals(selectedType) && s.isIs_main_station()) temp.add(s);
                else if ("Airports".equals(selectedType) && s.isIs_airport()) temp.add(s);
            }
            filtered = temp;
        }

        stationTableView.setItems(FXCollections.observableArrayList(filtered));

        if (filtered.isEmpty()) {
            errorLabel.setText("No stations match the selected filter.");
            errorLabel.setStyle("-fx-text-fill: red;");
        } else {
            errorLabel.setText("Found: " + filtered.size() + " stations.");
            errorLabel.setStyle("-fx-text-fill: green;");
        }
    }


    @FXML
    public void findStationOnAction(ActionEvent event) {
        double latMin, latMax, lonMin, lonMax;

        try {
            latMin = Double.parseDouble(latitudeMinTextField.getText());
            latMax = Double.parseDouble(latitudeMaxTextField.getText());
            lonMin = Double.parseDouble(longitudeMinTextField.getText());
            lonMax = Double.parseDouble(longitudeMaxTextField.getText());
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid latitude/longitude values.");
            errorLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if (latMin < -90 || latMax > 90 || lonMin < -180 || lonMax > 180) {
            errorLabel.setText("Coordinates out of bounds [-90,90] and [-180,180].");
            errorLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        String type = treeComboBox11.getValue();
        Boolean isCity = null;
        Boolean isMainStation = null;
        Boolean isAirport = null;


        if ("Cities".equalsIgnoreCase(type)) isCity = true;
        else if ("Main Stations".equalsIgnoreCase(type)) isMainStation = true;
        else if ("Airports".equalsIgnoreCase(type)) isAirport = true;

        List<StationEsinf> stations = controller.searchInRegion(latMin, latMax, lonMin, lonMax, isCity, isMainStation, isAirport, null);

        allStationsRoot = new ArrayList<>(stations);
        populateCountryComboBox(allStationsRoot);

        stationTableView.setItems(FXCollections.observableArrayList(stations));

        if (stations.isEmpty()) {
            errorLabel.setText("No stations found in this region.");
            errorLabel.setStyle("-fx-text-fill: red;");
        } else {
            errorLabel.setText("Found: " + stations.size() + " stations.");
            errorLabel.setStyle("-fx-text-fill: green;");
        }
    }

}
