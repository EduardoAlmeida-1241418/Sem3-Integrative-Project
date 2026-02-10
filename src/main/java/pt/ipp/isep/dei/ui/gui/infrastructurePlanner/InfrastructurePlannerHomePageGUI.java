package pt.ipp.isep.dei.ui.gui.infrastructurePlanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Infrastructure Planner home page.
 *
 * <p>This JavaFX controller provides navigation from the Infrastructure
 * Planner home page to related screens such as railway upgrade planning,
 * baseline cost estimation and NOS logs. Scene transitions are delegated
 * to {@link UIUtils}; this class does not perform business logic itself.
 * </p>
 *
 */
public class InfrastructurePlannerHomePageGUI implements Initializable {

    private UIUtils uiUtils;

    /**
     * Initialise the controller.
     *
     * <p>Creates an instance of {@link UIUtils} which is used to perform
     * scene transitions invoked by the UI handlers.</p>
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
    }


    /**
     * Handle the action that opens the railway upgrades planning view.
     *
     * @param actionEvent the event supplied by the UI
     */
    @FXML
    public void handleRailwayUpgrades(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/infrastructurePlanner/railwayUpgradePlanning.fxml", "MABEC - Infrastructure Planner - Railway Upgrades");
    }

    /**
     * Log out the current user and return to the public home page.
     *
     * @param event the event supplied by the UI
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Open the baseline cost estimation view.
     *
     * @param actionEvent the event supplied by the UI
     */
    @FXML
    public void handleBaselineCost(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/infrastructurePlanner/railwayBaselineCost.fxml", "MABEC - Infrastructure Planner - Baseline Cost");
    }

    /**
     * Open the NOS logs view for the Infrastructure Planner.
     *
     * @param actionEvent the event supplied by the UI
     */
    @FXML
    public void handleNOSLogsButton(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/infrastructurePlanner/infrastructurePlannerNOS.fxml", "MABEC - Infrastructure Planner - NOS Logs");
    }
}