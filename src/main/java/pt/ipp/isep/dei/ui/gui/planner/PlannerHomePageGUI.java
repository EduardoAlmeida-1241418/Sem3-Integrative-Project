package pt.ipp.isep.dei.ui.gui.planner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Planner Home Page.
 *
 * <p>This JavaFX controller manages navigation from the Planner home page
 * to related planner screens such as trolleys, picking plans, station
 * search, geographical area and WMS/GQS logs. It delegates scene
 * transitions to {@link UIUtils} and contains no business logic itself.</p>
 *
 */
public class PlannerHomePageGUI implements Initializable {

    private UIUtils uiUtils;

    /**
     * Initialise the controller by creating required helper instances.
     *
     * <p>This method is invoked by the JavaFX runtime when the FXML view is
     * loaded. It initialises the {@link UIUtils} helper used to perform
     * scene transitions from the various UI handlers.</p>
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
    }

    /**
     * Log out the current user and navigate to the public Home Page.
     *
     * @param event the action event triggered by the Logout button
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Navigate to the Planner Trolleys view.
     *
     * @param event the action event triggered by the Trolleys button
     */
    @FXML
    public void trolleysOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerTrolleys.fxml", "MABEC - Planner - Trolleys");
    }

    /**
     * Navigate to the Planner Picking Plans view.
     *
     * @param event the action event triggered by the Picking Plans button
     */
    @FXML
    public void pickingPlansOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerPickingPlans.fxml", "MABEC - Planner - Picking Plans");
    }

    /**
     * Navigate to the Planner WMS / GQS logs view.
     *
     * @param event the action event triggered by the Logs button
     */
    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerWMS_GQS.fxml", "MABEC - Planner - WMS / GQS Logs");
    }

    /**
     * Navigate to the Planner station search view.
     *
     * @param event the action event triggered by the Search Stations button
     */
    @FXML
    public void searchStationButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerStationSearch.fxml", "MABEC - Planner - Search Stations");
    }

    /**
     * Navigate to the Planner geographical area view.
     *
     * @param event the action event triggered by the Geographical Area button
     */
    @FXML
    public void handleGeographicalArea(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/planner/plannerGeographicalArea.fxml", "MABEC - Planner - Geographical Area");
    }
}
