package pt.ipp.isep.dei.ui.gui.dataEngineer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Data Engineer Home Page.
 *
 * <p>This JavaFX controller manages navigation from the Data Engineer home
 * page to related views such as organised stations and GQS logs. It delegates
 * scene changes to {@link UIUtils} and does not perform business logic itself.</p>
 *
 */
public class DataEngineerHomePageGUI implements Initializable {

    private UIUtils uiUtils;

    /**
     * Initialise the controller by creating instances of required helpers.
     *
     * @param url not used by this implementation
     * @param resourceBundle not used by this implementation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
    }


    /**
     * Navigate to the organised stations view.
     *
     * @param actionEvent the action event supplied by the UI
     */
    @FXML
    public void handleOrganizedStations(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/dataEngineer/organizedStations.fxml", "MABEC - Data Engineer - Organized Stations");
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
     * Navigate to the Data Engineer GQS logs view.
     *
     * @param actionEvent the action event supplied by the UI
     */
    @FXML
    public void handleWmsLogsButton(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/dataEngineer/dataEngineerGQS.fxml", "MABEC - Data Engineer - GQS Logs");
    }
}
