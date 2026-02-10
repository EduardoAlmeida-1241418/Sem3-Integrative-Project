package pt.ipp.isep.dei.ui.gui.trafficManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Planner Home Page.
 * Handles navigation to planner-related views such as Trolleys, Picking Plans, and WMS Logs.
 */
public class TrafficManagerHomePageGUI implements Initializable {

    private UIUtils uiUtils;

    /**
     * Initialise the controller.
     *
     * <p>Called by the JavaFX runtime when the corresponding FXML view is
     * loaded. This method initialises the {@link UIUtils} helper used by
     * the navigation handlers.</p>
     *
     * @param url resource location (unused)
     * @param resourceBundle resources for localisation (unused)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
    }


    /**
     * Navigate to the Traffic Manager Paths view.
     *
     * <p>Invoked by the associated UI control; the actual scene change
     * is delegated to {@link UIUtils#loadFXMLScene}.</p>
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void pathOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/pathRelated/trafficManagerPath.fxml", "MABEC - Traffic Manager - Paths");
    }

    /**
     * Navigate to the Traffic Manager Assign view.
     *
     * <p>Triggered by the UI; delegates scene loading to
     * {@link UIUtils#loadFXMLScene}.</p>
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void assignOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/trafficManagerAssign.fxml", "MABEC - Traffic Manager - Assign");
    }

    /**
     * Navigate to the Traffic Manager Scheduler view.
     *
     * <p>Delegate the scene transition to the UI utility.</p>
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void schedulerOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/scheduleRelated/trafficManagerScheduler.fxml", "MABEC - Traffic Manager - Scheduler");
    }

    /**
     * Open the Traffic Manager NOS logs view.
     *
     * <p>This handler loads the NOS logs scene; it does not perform any
     * business logic itself.</p>
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficManager/trafficManagerNOS.fxml", "MABEC - Traffic Manager - NOS Logs");
    }

    /**
     * Log out the current user and return to the public Home Page.
     *
     * <p>Performs a scene transition to the public home view; any session
     * handling is performed elsewhere.</p>
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }
}