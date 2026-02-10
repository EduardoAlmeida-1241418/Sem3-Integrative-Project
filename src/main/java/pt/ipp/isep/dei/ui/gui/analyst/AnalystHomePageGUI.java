package pt.ipp.isep.dei.ui.gui.analyst;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Analyst Home Page.
 *
 * <p>This JavaFX controller is responsible for initialising UI helper
 * components and handling navigation from the Analyst home page to related
 * views such as proximity search and GQS logs. It delegates scene changes to
 * {@link UIUtils} and does not itself perform business logic.</p>
 *
 */
public class AnalystHomePageGUI implements Initializable {

    private UIUtils uiUtils;

    /**
     * Initialise the controller.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
    }


    /**
     * Handle the proximity search button action by loading the proximity
     * search scene.
     *
     * @param actionEvent the action event supplied by the UI
     */
    @FXML
    public void handleProximitySearch(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/analyst/analystProximitySearch.fxml", "MABEC - Analyst - Proximity Search");
    }

    /**
     * Log out the user and return to the public home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Navigate to the GQS logs view for the analyst.
     *
     * @param actionEvent the action event supplied by the UI
     */
    @FXML
    public void handleWmsLogsButton(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/analyst/analystGQS.fxml", "MABEC - Analyst - GQS Logs");
    }
}
