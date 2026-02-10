package pt.ipp.isep.dei.ui.gui.trafficManager.pathRelated;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Traffic Manager paths screen.
 *
 * <p>This JavaFX controller provides navigation from the Traffic Manager
 * paths view to related screens such as path assignment, scheduler and
 * NOS logs. It initialises a small UI helper used to perform scene
 * transitions and exposes simple FXML handlers which the view invokes.</p>
 *
 */
public class TrafficManagerPathsGUI implements Initializable {

    private UIUtils uiUtils;

    /**
     * Initialise the controller helper instances.
     *
     * <p>Called by the JavaFX runtime when the corresponding FXML view is
     * loaded. This method initialises the {@link UIUtils} helper used by
     * the FXML handlers to perform scene transitions.</p>
     *
     * @param url resource location (not used)
     * @param resourceBundle resources for localisation (not used)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
    }


    /**
     * Navigate to the Traffic Manager home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/trafficManagerHomePage.fxml", "MABEC - Traffic Manager - Home Page");
    }

    /**
     * Navigate to the assignment view where paths may be assigned.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void assignOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/trafficManagerAssign.fxml", "MABEC - Traffic Manager - Assign");
    }

    /**
     * Open the scheduler view for traffic management duties.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void schedulerOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/scheduleRelated/trafficManagerScheduler.fxml", "MABEC - Traffic Manager - Scheduler");
    }

    /**
     * Open the NOS logs view for inspection.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/trafficManagerNOS.fxml", "MABEC - Traffic Manager - NOS Logs");
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
     * Navigate to the manual path creation view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void createPathManualOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/pathRelated/trafficManagerRoute.fxml", "MABEC - Traffic Manager - Manual Path");
    }

    /**
     * Navigate to the automatic path generation view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void createPathAutomaticOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/pathRelated/trafficManagerPathAutomatic.fxml", "MABEC - Traffic Manager - Automatic Path");
    }
}