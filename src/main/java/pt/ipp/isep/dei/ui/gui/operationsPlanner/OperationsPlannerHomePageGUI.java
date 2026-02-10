package pt.ipp.isep.dei.ui.gui.operationsPlanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Operations Planner home page.
 *
 * <p>This JavaFX controller provides navigation from the Operations
 * Planner home page to related views such as radius search and GQS logs.
 * It initialises a small UI helper used to perform scene transitions and
 * exposes simple FXML handlers which the view invokes. The class is
 * concerned purely with presentation and navigation; no business logic is
 * implemented here.</p>
 *
 */
public class OperationsPlannerHomePageGUI implements Initializable {

    private UIUtils uiUtils;

    /**
     * Initialise the controller.
     *
     * <p>Called by the JavaFX runtime when the corresponding FXML view is
     * loaded. This method creates an instance of {@link UIUtils} which is
     * used by the handlers to perform scene changes.</p>
     *
     * @param url resource location (not used)
     * @param resourceBundle resources for localisation (not used)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
    }

    /**
     * Log out the current user and return to the public home page.
     *
     * <p>This handler delegates the actual scene change to
     * {@link UIUtils#loadFXMLScene}.</p>
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Open the radius search view for the Operations Planner.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void handleRadiusSearch(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/operationsPlanner/operationsPlannerRadiusSearch.fxml", "MABEC - Operations Planner - Radius Search");
    }

    /**
     * Open the GQS logs view for the Operations Planner.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void handleWmsLogsButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/operationsPlanner/operationsPlannerGQS.fxml", "MABEC - Operations Planner - GQS Logs");
    }
}