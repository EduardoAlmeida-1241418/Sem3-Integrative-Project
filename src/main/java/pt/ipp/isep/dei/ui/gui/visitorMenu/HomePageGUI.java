package pt.ipp.isep.dei.ui.gui.visitorMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.homePage.HomePageController;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePageGUI implements Initializable {

    private HomePageController controller;
    private UIUtils uiUtils;

    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new HomePageController();
        uiUtils = new UIUtils();
    }

    @FXML
    public void loginButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/signIn.fxml", "MABEC - Sign In");
    }

    @FXML
    public void devTeamButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/devTeam.fxml", "MABEC - Development Team");
    }

    @FXML
    public void handleDatabase(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/database.fxml", "MABEC - Database");
    }

    @FXML
    public void aboutProjectButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/aboutProject.fxml", "MABEC - About Project");
    }

    @FXML
    public void closeApplicationButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void downloadButtonOnAction(ActionEvent event) {
        try {
            controller.downloadUserManual();
        } catch (IOException e) {
            System.err.println("Error saving PDF: " + e.getMessage());
        }
    }
}
