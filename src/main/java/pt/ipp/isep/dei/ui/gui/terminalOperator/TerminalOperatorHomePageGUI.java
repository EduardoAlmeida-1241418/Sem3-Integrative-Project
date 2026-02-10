package pt.ipp.isep.dei.ui.gui.terminalOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Terminal Operator Home Page GUI.
 * Provides navigation options for terminal operators, including unloading wagons,
 * relocating boxes, and viewing WMS logs.
 */
public class TerminalOperatorHomePageGUI implements Initializable {

    private UIUtils uiUtils;

    /**
     * Initializes the controller and its utility instance.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uiUtils = new UIUtils();
    }

    /**
     * Handles the logout button action.
     * Redirects the user to the main home page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Handles the wagons button action.
     * Navigates to the Unload Wagons interface.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void wagonsButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/unloadWagons.fxml",
                "MABEC - Terminal Operator - Unload Wagons");
    }

    /**
     * Handles the relocation box button action.
     * Navigates to the Relocation Box interface.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void relocationBoxButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/relocateBox_Bay.fxml",
                "MABEC - Terminal Operator - Relocation Box");
    }

    /**
     * Handles the WMS logs button action.
     * Opens the WMS Logs interface for the terminal operator.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void handleWmsLogsButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/terminalOperatorWMS.fxml",
                "MABEC - Terminal Operator - WMS Logs");
    }
}
