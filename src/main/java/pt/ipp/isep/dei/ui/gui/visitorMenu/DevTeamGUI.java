package pt.ipp.isep.dei.ui.gui.visitorMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Development Team GUI.
 * Provides navigation options for visitors to view the development team,
 * learn about the project, or access other parts of the visitor menu.
 */
public class DevTeamGUI implements Initializable {

    private UIUtils uiUtils;

    /**
     * Initializes the controller and UI utility instance.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
    }

    /**
     * Opens the Sign In page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void loginButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/signIn.fxml", "MABEC - Sign In");
    }

    @FXML
    public void handleDatabase(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/database.fxml", "MABEC - Database");
    }

    /**
     * Navigates to the Home Page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Opens the About Project page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void aboutProjectButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/aboutProject.fxml", "MABEC - About Project");
    }

    /**
     * Closes the application window.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void closeApplicationButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
