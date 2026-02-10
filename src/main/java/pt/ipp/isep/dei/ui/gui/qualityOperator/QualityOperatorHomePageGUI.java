package pt.ipp.isep.dei.ui.gui.qualityOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Quality Operator Home Page.
 * Handles navigation to quarantine management and WMS logs pages.
 */
public class QualityOperatorHomePageGUI implements Initializable {

    private UIUtils uiUtils;

    /**
     * Initializes the controller by creating a new instance of UIUtils.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
    }

    /**
     * Navigates to the Quarantine Return page.
     *
     * @param actionEvent the action event triggered by the Quarantine button
     */
    @FXML
    public void handleQuarantine(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/qualityOperator/quarantineReturn.fxml", "MABEC - Quality Operator - Quarantine");
    }

    /**
     * Logs out the current user and navigates to the main Home Page.
     *
     * @param event the action event triggered by the Logout button
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Navigates to the Quality Operator WMS Logs page.
     *
     * @param actionEvent the action event triggered by the WMS Logs button
     */
    @FXML
    public void handleWmsLogsButton(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/qualityOperator/qualityOperatorWMS.fxml", "MABEC - Quality Operator - WMS Logs");
    }
}
