package pt.ipp.isep.dei.ui.gui.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Admin Home Page.
 * Handles navigation actions such as logging out and accessing user management.
 */
public class AdminHomePageGUI implements Initializable {

    private UIUtils uiUtils;

    /**
     * Initializes the Admin Home Page controller.
     * Sets up the utility helper for GUI navigation.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
    }

    /**
     * Logs out the current user and navigates back to the Home Page.
     *
     * @param event the logout button action event
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Opens the Admin Users management screen.
     *
     * @param event the users button action event
     */
    @FXML
    public void usersButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/admin/chooseUser.fxml", "MABEC - Admin Users");
    }
}
