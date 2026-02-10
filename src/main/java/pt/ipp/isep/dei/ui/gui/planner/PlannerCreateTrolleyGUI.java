package pt.ipp.isep.dei.ui.gui.planner;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import pt.ipp.isep.dei.controller.planner.CreateTrolleyController;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Planner Create Trolley page.
 * Allows planners to create new trolley models and view existing ones.
 */
public class PlannerCreateTrolleyGUI implements Initializable {

    private CreateTrolleyController controller;
    private UIUtils uiUtils;

    @FXML
    private TextField modelTextField, weightTextField;

    @FXML
    private TableView<TrolleyModel> createdTrolleyTableView;

    @FXML
    private TableColumn<TrolleyModel, String> weightTableColumn;

    @FXML
    private TableColumn<TrolleyModel, String> modelTableColumn;

    @FXML
    private Label errorLabel;

    /**
     * Initializes the GUI by setting up the controller, UI utilities, and loading the table data.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new CreateTrolleyController();
        this.uiUtils = new UIUtils();
        loadGuiInfo();
    }

    /**
     * Loads the table with trolley model data and sets up column bindings.
     */
    private void loadGuiInfo() {
        weightTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getMaxWeight()))
        );

        modelTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getName()))
        );

        createdTrolleyTableView.setItems(controller.getTrolleyModelList());
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
     * Navigates to the Planner Picking Plans page.
     *
     * @param event the action event triggered by the Picking Plans button
     */
    @FXML
    public void pickingPlansOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerPickingPlans.fxml", "MABEC - Planner - Picking Plans");
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
     * Validates input and creates a new trolley model if valid.
     * Displays appropriate success or error messages temporarily.
     *
     * @param event the action event triggered by the Confirm button
     */
    @FXML
    public void confirmButtonOnAction(ActionEvent event) {
        if (modelTextField.getText().isEmpty() || weightTextField.getText().isEmpty()) {
            showTemporaryMessage(errorLabel, "Please fill all the fields", "red", 3);
            return;
        }

        String creationError = controller.createTrolleyModel(
                modelTextField.getText(),
                weightTextField.getText()
        );

        if (creationError != null) {
            showTemporaryMessage(errorLabel, creationError, "red", 3);
            return;
        }

        showTemporaryMessage(errorLabel, "Trolley model created successfully!", "green", 3);
    }

    /**
     * Displays a temporary on-screen message in the specified color for a limited duration.
     *
     * @param label the label used to display the message
     * @param message the text to display
     * @param color the CSS color of the text
     * @param seconds the number of seconds before the message disappears
     */
    private void showTemporaryMessage(Label label, String message, String color, int seconds) {
        label.setText(message);
        label.setStyle("-fx-text-fill: " + color + ";");
        label.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(e -> label.setVisible(false));
        pause.play();
    }
}
