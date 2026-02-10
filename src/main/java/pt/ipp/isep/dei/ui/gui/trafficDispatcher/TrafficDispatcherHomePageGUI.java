package pt.ipp.isep.dei.ui.gui.trafficDispatcher;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Traffic Dispatcher Home Page GUI.
 * Provides navigation options for traffic dispatchers to access
 * the Estimated Travel and WMS Log interfaces.
 */
public class TrafficDispatcherHomePageGUI implements Initializable {

    private UIUtils uiUtils;

    /**
     * Initializes the controller and utility instance.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uiUtils = new UIUtils();
    }

    /**
     * Handles the "Estimated Travel" button action.
     * Navigates to the Estimated Travel interface for traffic dispatchers.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void estimatedTravelButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficDispatcher/estimatedTravel.fxml",
                "MABEC - Traffic Dispatcher - Estimated Travel");
    }

    /**
     * Handles the logout button action.
     * Returns the user to the main home page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml",
                "MABEC - Home Page");
    }

    /**
     * Handles the WMS log button action.
     * Opens the Traffic Dispatcher WMS Log interface.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void handleWmsLogButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/trafficDispatcher/trafficDispatcherWMS.fxml",
                "MABEC - Traffic Dispatcher - WMS Log");
    }
}
