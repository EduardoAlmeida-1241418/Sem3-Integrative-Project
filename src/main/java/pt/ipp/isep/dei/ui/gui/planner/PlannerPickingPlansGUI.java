package pt.ipp.isep.dei.ui.gui.planner;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pt.ipp.isep.dei.controller.planner.PickingPlansController;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.domain.pickingPlanRelated.spacePolicyRelated.SpacePolicyType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Planner Picking Plans page.
 * Allows planners to view order lines, configure plan generation parameters,
 * and create new picking plans using selected heuristics and space policies.
 */
public class PlannerPickingPlansGUI implements Initializable {

    private PickingPlansController controller;
    private UIUtils uiUtils;

    @FXML
    private TableView<OrderLine> orderLineTableView;

    @FXML
    private TableColumn<OrderLine, String> orderIdTableColumn;

    @FXML
    private TableColumn<OrderLine, String> orderLineIdTableColumn;

    @FXML
    private TableColumn<OrderLine, String> statusTableColumn;

    @FXML
    private TableColumn<OrderLine, String> priorityTableColumn;

    @FXML
    private Label weightLabel, boxQtyLabel, errorLabel;

    @FXML
    private ComboBox<String> trolleyComboBox;

    @FXML
    private ComboBox<String> heuristicComboBox;

    @FXML
    private ComboBox<String> spacePolicyComboBox;

    /**
     * Initializes the GUI by loading table and combo box data,
     * and setting up listeners for order line selection changes.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new PickingPlansController();
        this.uiUtils = new UIUtils();
        loadGuiInfo();

        orderLineTableView.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener<OrderLine>) change -> updateLabels());
    }

    /**
     * Loads all GUI components and initializes table and combo box data.
     */
    private void loadGuiInfo() {
        loadTableInfo();
        loadComboBoxInfo();
    }

    /**
     * Loads available options for trolleys, heuristics, and space policies into combo boxes.
     */
    private void loadComboBoxInfo() {
        trolleyComboBox.setItems(controller.getTrolleyModels());
        heuristicComboBox.setItems(controller.getHeuristicNames());
        spacePolicyComboBox.setItems(controller.getSpacePolicyTypes());
    }

    /**
     * Configures the table view with order line data and sorting behavior.
     */
    private void loadTableInfo() {
        orderIdTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getOrderId()))
        );

        orderLineIdTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getLineNumber()))
        );

        statusTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getRealStatus().getState().toString()))
        );

        priorityTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(controller.getPriority(cellData.getValue().getOrderId())))
        );

        ObservableList<OrderLine> orderLines = FXCollections.observableArrayList(controller.getOrderLines());
        orderLineTableView.setItems(orderLines);
        orderLineTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        priorityTableColumn.setSortType(TableColumn.SortType.ASCENDING);
        orderLineTableView.getSortOrder().clear();
        orderLineTableView.getSortOrder().add(priorityTableColumn);
        orderLineTableView.sort();
    }

    /**
     * Updates total weight and box quantity labels based on selected order lines.
     */
    private void updateLabels() {
        ObservableList<OrderLine> selectedLines = orderLineTableView.getSelectionModel().getSelectedItems();
        double totalWeight = 0;
        int totalBoxes = 0;

        for (OrderLine line : selectedLines) {
            totalWeight += controller.calculateTotalWeight(line);
            totalBoxes += controller.getTotalAllocatedUnits(line);
        }

        weightLabel.setText(String.format("%.2f", totalWeight));
        boxQtyLabel.setText(String.valueOf(totalBoxes));
    }

    /**
     * Logs out the current user and navigates to the main Home Page.
     *
     * @param event the action event triggered by the Logout button
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Navigates to the Planner Home Page.
     *
     * @param event the action event triggered by the Home button
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerHomePage.fxml", "MABEC - Planner - Home Page");
    }

    /**
     * Navigates to the Planner Trolleys page.
     *
     * @param event the action event triggered by the Trolleys button
     */
    @FXML
    public void trolleysOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerTrolleys.fxml", "MABEC - Planner - Trolleys");
    }

    /**
     * Navigates to the Planner WMS Logs page.
     *
     * @param event the action event triggered by the Logs button
     */
    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerWMS_GQS.fxml", "MABEC - Planner - WMS / GQS Logs");
    }

    @FXML
    public void searchStationButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerStationSearch.fxml", "MABEC - Planner - Search Stations");
    }

    @FXML
    public void handleGeograpgicalArea(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerGeographicalArea.fxml", "MABEC - Planner - Geographical Area");
    }

    /**
     * Generates a picking plan using the selected order lines and parameters
     * after verifying input validity.
     *
     * @param event the action event triggered by the Confirm button
     */
    @FXML
    public void confirmButtonOnAction(ActionEvent event) {
        if (!inputDataVerification()) {
            return;
        }

        controller.generatePickingPlan(orderLineTableView.getSelectionModel().getSelectedItems(),
                trolleyComboBox.getSelectionModel().getSelectedItem(),
                heuristicComboBox.getSelectionModel().getSelectedItem(),
                spacePolicyComboBox.getSelectionModel().getSelectedItem());

        loadTableInfo();
    }

    /**
     * Verifies that all necessary input selections are made and valid before creating a plan.
     *
     * @return true if inputs are valid, false otherwise
     */
    private boolean inputDataVerification() {
        errorLabel.setText("");

        if (orderLineTableView.getSelectionModel().getSelectedItems().isEmpty()) {
            showTemporaryError("Please select at least one order line.", "red");
            return false;
        }
        if (trolleyComboBox.getSelectionModel().isEmpty()) {
            showTemporaryError("Please select a trolley model.", "red");
            return false;
        }
        if (heuristicComboBox.getSelectionModel().isEmpty()) {
            showTemporaryError("Please select a heuristic.", "red");
            return false;
        }
        if (spacePolicyComboBox.getSelectionModel().isEmpty()) {
            showTemporaryError("Please select a split option.", "red");
            return false;
        }
        if (spacePolicyComboBox.getSelectionModel().getSelectedItem().equals(SpacePolicyType.DEFER.getName())) {
            ObservableList<OrderLine> selectedLines = orderLineTableView.getSelectionModel().getSelectedItems();
            String trolleyModelName = trolleyComboBox.getSelectionModel().getSelectedItem();

            boolean possible = controller.verifyDeferPossibility(selectedLines, trolleyModelName);
            if (!possible) {
                showTemporaryError("Defer not possible: one or more order lines exceed trolley weight!", "red");
                return false;
            }
        }
        return true;
    }

    /**
     * Displays a temporary error message with a given color.
     *
     * @param message the message text to display
     * @param color the color for the text (CSS format)
     */
    private void showTemporaryError(String message, String color) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: " + color + ";");

        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
        pause.setOnFinished(event -> errorLabel.setText(""));
        pause.play();
    }
}
