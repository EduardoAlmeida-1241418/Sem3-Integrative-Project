package pt.ipp.isep.dei.ui.gui.maintenancePlanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Maintenance Planner home page.
 *
 * <p>This JavaFX controller provides navigation from the Maintenance Planner
 * home page to related views such as hub analysis and NOS logs. Scene
 * transitions are delegated to {@link UIUtils}; the controller does not
 * contain any business logic itself.</p>
 *
 */
public class MaintenancePlannerHomePageGUI implements Initializable {
    private UIUtils uiUtils;

    /**
     * Initialise the controller by creating a new instance of UIUtils.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
    }


    /**
     * Open the hub analysis view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void hubAnalysisButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/maintenancePlanner/maintenancePlannerHubAnalysis.fxml", "MABEC - Maintenance Planner - Hub Analysis");
    }

    /**
     * Open the Maintenance NOS logs view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void handleNOSLogsButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/maintenancePlanner/maintenanceManagerNOS.fxml", "MABEC - Maintenance Planner - NOS Logs");
    }

    /**
     * Navigate to the Maintenance Planner home page (refresh or return).
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void homePageButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/maintenancePlanner/maintenancePlannerHomePage.fxml", "MABEC - Maintenance Planner - Home Page");
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
}
