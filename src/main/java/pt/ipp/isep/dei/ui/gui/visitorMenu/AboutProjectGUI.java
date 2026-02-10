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
 * Controller class for the "About Project" GUI.
 * Provides navigation options for visitors to learn about the project,
 * access the development team information, or return to other menus.
 */
public class AboutProjectGUI implements Initializable {

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
     * Opens the Sign In page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void loginButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/signIn.fxml", "MABEC - Sign In");
    }

    /**
     * Returns to the Home Page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Opens the Development Team page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void devTeamButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/devTeam.fxml", "MABEC - Development Team");
    }

    @FXML
    public void handleDatabase(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/database.fxml", "MABEC - Database");
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
