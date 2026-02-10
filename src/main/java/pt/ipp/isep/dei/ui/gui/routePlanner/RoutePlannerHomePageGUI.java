package pt.ipp.isep.dei.ui.gui.routePlanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Route Planner home page.
 *
 * <p>This JavaFX controller supplies simple navigation from the Route
 * Planner home page to related screens such as the shortest path tool
 * and the NOS logs. Scene transitions are delegated to
 * {@link UIUtils} and no domain logic is performed in this class.</p>
 *
 */
public class RoutePlannerHomePageGUI implements Initializable {

    private UIUtils uiUtils;

    /**
     * Initialise the controller.
     *
     * <p>Called by the JavaFX runtime when the FXML view is loaded. This
     * method initialises the {@link UIUtils} helper used to perform scene
     * transitions from the UI handlers.</p>
     *
     * @param url not used by this implementation
     * @param resourceBundle not used by this implementation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
    }


    /**
     * Open the shortest path view for the route planner.
     *
     * @param actionEvent the action event supplied by the UI
     */
    @FXML
    public void handleShortestPath(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/routePlanner/shortestPath.fxml", "MABEC - Route Planner - Shortest Path");
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
     * Open the NOS logs view for the route planner.
     *
     * @param actionEvent the action event supplied by the UI
     */
    @FXML
    public void handleNOSLogsButton(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/routePlanner/routePlannerNOS.fxml", "MABEC - Route Planner - NOS Logs");
    }
}
