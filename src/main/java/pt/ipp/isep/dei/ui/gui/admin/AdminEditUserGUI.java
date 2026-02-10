package pt.ipp.isep.dei.ui.gui.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import pt.ipp.isep.dei.controller.admin.AdminEditUserController;
import pt.ipp.isep.dei.domain.User;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for editing user roles and information in the admin interface.
 * Handles user interaction with the Admin Edit User screen.
 */
public class AdminEditUserGUI implements Initializable {

    private AdminEditUserController controller;
    private UIUtils uiUtils;

    @FXML
    private CheckBox pickerCheckBox, plannerCheckBox, qualityOperatorCheckBox,
            terminalOperatorCheckBox, trafficDispatcherCheckBox, warehousePlannerCheckBox;

    @FXML
    private TextField idTextField, emailTextField, statusTextField;

    @FXML
    private Label errorLabel;

    /**
     * Initializes the controller and utility objects when the scene loads.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new AdminEditUserController();
        uiUtils = new UIUtils();
    }

    /**
     * Loads user information into the controller and updates the GUI fields.
     *
     * @param user the user to load and display
     */
    public void loadInfo(User user) {
        controller.loadInfo(user);
        loadGuiInfo();
        controller.initializeBox(pickerCheckBox, plannerCheckBox, qualityOperatorCheckBox,
                terminalOperatorCheckBox, trafficDispatcherCheckBox, warehousePlannerCheckBox);
    }

    /**
     * Populates the GUI fields with data retrieved from the controller.
     */
    private void loadGuiInfo() {
        idTextField.setText(controller.getId());
        idTextField.setEditable(false);

        emailTextField.setText(controller.getEmail());
        emailTextField.setEditable(false);

        statusTextField.setText(controller.getActive());
        statusTextField.setEditable(false);
    }

    /**
     * Navigates to the home page when the logout button is pressed.
     *
     * @param event the logout action event
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Navigates to the admin home page.
     *
     * @param event the action event
     */
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/admin/adminHomePage.fxml", "MABEC - Admin Home Page");
    }

    /**
     * Navigates back to the user selection page.
     *
     * @param event the action event
     */
    public void backButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/admin/chooseUser.fxml", "MABEC - Admin Users");
    }

    /**
     * Opens the admin users management page.
     *
     * @param event the action event
     */
    public void usersButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/admin/chooseUser.fxml", "MABEC - Admin Users");
    }

    /**
     * Confirms role modifications for the selected user.
     * Updates user roles and displays a success message.
     *
     * @param event the action event
     */
    @FXML
    public void confirmButtonOnAction(ActionEvent event) {
        controller.setRoles(pickerCheckBox, plannerCheckBox, qualityOperatorCheckBox,
                terminalOperatorCheckBox, trafficDispatcherCheckBox, warehousePlannerCheckBox);

        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setText("Roles Modified Successfully");
    }
}
