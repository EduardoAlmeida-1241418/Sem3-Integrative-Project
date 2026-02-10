package pt.ipp.isep.dei.ui.gui.operationsAnalyst;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Operations Analyst home page.
 *
 * <p>This JavaFX controller provides simple navigation from the Operations
 * Analyst home page to related views such as the maximum flow calculation
 * screen and the NOS logs for operations analysts. It delegates scene
 * transitions to {@link UIUtils} and does not perform business logic itself.</p>
 *
 */
public class OperationsAnalystHomePageGUI implements Initializable {
    private UIUtils uiUtils;

    /**
     * Initialise the controller.
     *
     * <p>Instantiates utility helpers required to perform scene transitions
     * when UI controls are activated. This method is called by the JavaFX
     * runtime when the corresponding FXML view is initialised.</p>
     *
     * @param url location used to resolve relative paths (not used)
     * @param resourceBundle resources used to localise the root object (not used)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
    }

    /**
     * Open the maximum flow calculation view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void calculateMaxFlowOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/operationsAnalyst/CalculateMaximumFlow.fxml", "MABEC - Operations Analyst - Calculate Max Flow");
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
     * Open the Operations Analyst NOS logs view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void NOSButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/operationsAnalyst/operationsAnalystNOS.fxml", "MABEC - Operations Analyst - NOS Logs");
    }
}
