package pt.ipp.isep.dei.ui.gui.picker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Picker Home Page.
 * Handles navigation between picker-related views and logout.
 */
public class PickerHomePageGUI implements Initializable {

    private UIUtils uiUtils;

    /**
     * Initializes the controller by creating a new instance of UIUtils.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uiUtils = new UIUtils();
    }

    /**
     * Navigates to the Picker Home Page view.
     *
     * @param event the action event triggered by the Home button
     */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/picker/pickerHomePage.fxml", "MABEC - Picker - Home Page");
    }

    /**
     * Navigates to the Picking Path view.
     *
     * @param event the action event triggered by the Picking Path button
     */
    @FXML
    public void pickingPathButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/picker/pickingPath.fxml", "MABEC - Picker - Picking Path");
    }

    /**
     * Logs out the current user and returns to the main Home Page.
     *
     * @param event the action event triggered by the Logout button
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Navigates to the Picker WMS Log view.
     *
     * @param event the action event triggered by the WMS Log button
     */
    @FXML
    public void handleWmsLogButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/picker/pickerWMS.fxml", "MABEC - Picker - WMS Log");
    }
}
