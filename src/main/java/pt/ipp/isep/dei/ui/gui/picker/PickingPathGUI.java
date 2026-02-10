package pt.ipp.isep.dei.ui.gui.picker;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pt.ipp.isep.dei.controller.picker.PickingPathController;
import pt.ipp.isep.dei.domain.pickingPath.PickingPathReport;
import pt.ipp.isep.dei.domain.pickingPlanRelated.PickingPlan;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Picking Path page.
 * Displays available picking plans and allows the user to generate picking path reports using different strategies.
 */
public class PickingPathGUI implements Initializable {

    private UIUtils uiUtils;
    private PickingPathController controller;

    @FXML
    private Label totalWeightLabel, trolleyQuantityLabel, errorLabel;

    @FXML
    private TableView<PickingPlan> pickingPathTable;

    @FXML
    private TableColumn<PickingPlan, String> pickingPlanIDTableColumn;

    @FXML
    private TableColumn<PickingPlan, String> trolleyModelTableColumn;

    /**
     * Initializes the controller, loads data into the GUI, and sets up listeners for table selection changes.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
        this.controller = new PickingPathController();

        loadGuiInfo();

        pickingPathTable.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener<PickingPlan>) change -> updateLabels(pickingPathTable.getSelectionModel().getSelectedItem()));
    }

    /**
     * Updates label fields to show total weight and trolley quantity for the selected picking plan.
     *
     * @param selectedItem the selected picking plan from the table
     */
    private void updateLabels(PickingPlan selectedItem) {
        totalWeightLabel.setText(String.valueOf(selectedItem.getTrolleyModel().getMaxWeight()));
        trolleyQuantityLabel.setText(String.valueOf(selectedItem.getTrolleys().size()));
    }

    /**
     * Loads all GUI components and data.
     */
    public void loadGuiInfo() {
        loadTableGUI();
    }

    /**
     * Loads picking plan data into the table view and binds columns to plan attributes.
     */
    private void loadTableGUI() {
        pickingPlanIDTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        trolleyModelTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getTrolleyModel().getName()))
        );

        ObservableList<PickingPlan> pickingPlans = FXCollections.observableArrayList(controller.getPickingPlanList());
        pickingPathTable.setItems(pickingPlans);
    }

    /**
     * Navigates to the Picker Home Page.
     *
     * @param event the action event triggered by the Home button
     */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/picker/pickerHomePage.fxml", "MABEC - Picker - Home Page");
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
     * Navigates to the Picker WMS Log page.
     *
     * @param event the action event triggered by the WMS Log button
     */
    @FXML
    public void handleWmsLogButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/picker/pickerWMS.fxml", "MABEC - Picker - WMS Log");
    }

    /**
     * Generates and displays two picking path reports (Strategy A and B) for the selected picking plan.
     * If no plan is selected, an error message is shown temporarily.
     *
     * @param event the action event triggered by the Select button
     */
    @FXML
    public void selectButtonOnAction(ActionEvent event) {
        if (pickingPathTable.getSelectionModel().getSelectedItem() == null) {
            showTemporaryError("Select at least one Picking Plan", "red", 3);
            return;
        }

        PickingPathReport pickingPathReportA = controller.pickingPathReport_StratAGeneral(pickingPathTable.getSelectionModel().getSelectedItem());

        PickingPathReport pickingPathReportB = controller.pickingPathReport_StratBGeneral(pickingPathTable.getSelectionModel().getSelectedItem());

        FXMLLoader loader = uiUtils.loadFXMLScene(event, "/fxml/picker/strategies.fxml", "MABEC - Picker - Strategies Output");
        StrategiesGUI strategiesGUI = loader.getController();
        strategiesGUI.setData(pickingPathReportA, pickingPathReportB);
    }

    /**
     * Displays a temporary error message in the GUI for a specified duration.
     *
     * @param message the error message text
     * @param color the text color (CSS format)
     * @param time the duration in seconds before the message disappears
     */
    private void showTemporaryError(String message, String color, int time) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: " + color + ";");

        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(time));
        pause.setOnFinished(event -> errorLabel.setText(""));
        pause.play();
    }
}
