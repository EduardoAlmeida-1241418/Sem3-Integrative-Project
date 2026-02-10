package pt.ipp.isep.dei.ui.gui.freightManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Freight Manager home page.
 *
 * <p>This JavaFX controller provides navigation from the Freight Manager
 * home page to related screens such as the locomotives view, route
 * management, scheduler and GQS/NOS logs. The controller delegates scene
 * changes to {@link UIUtils} and does not perform business logic itself.</p>
 *
 */
public class FreightManagerHomePageGUI implements Initializable {

    private UIUtils uiUtils;

    /**
     * Initialise the controller.
     *
     * <p>Creates an instance of the UI helper used to perform scene
     * transitions. This method is invoked by the JavaFX runtime when the
     * corresponding FXML view is loaded.</p>
     *
     * @param url location used to resolve relative paths (not used)
     * @param resourceBundle resources used to localise the root object (not used)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
    }


    /**
     * Navigate to the locomotives (trains) view.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void trainsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/TrainRelated/freightManagerLocomotive.fxml", "MABEC - Freight Manager - Trains");
    }

    /**
     * Navigate to the route management view.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void routeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/RouteRelated/freightManagerRoute.fxml", "MABEC - Freight Manager - Route");
    }

    /**
     * Navigate to the scheduler view.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void schedulerOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerScheduler.fxml", "MABEC - Freight Manager - Scheduler");
    }

    /**
     * Open the GQS / NOS logs view for the freight manager.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerGQS_NOS.fxml", "MABEC - Freight Manager - GQS / NOS Logs");
    }

    /**
     * Log out the current user and return to the public home page.
     *
     * @param event the action event that triggered the logout
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }
}