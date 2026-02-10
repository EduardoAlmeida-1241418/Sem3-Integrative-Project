package pt.ipp.isep.dei.ui.gui.login;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import pt.ipp.isep.dei.controller.acountRelated.SignInController;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Sign-In process.
 * Handles user authentication, input validation, and navigation to role-specific dashboards.
 */
public class SignInGUI implements Initializable {

    private SignInController controller;
    private UIUtils uiUtils;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private ChoiceBox<String> loginChoiceBox;

    @FXML
    private VBox loginAsVbox, passwordVbox, emailVbox;

    @FXML
    private Button signAsButton, loginButton;

    /**
     * Initializes the GUI components and sets up keyboard navigation.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new SignInController();
        uiUtils = new UIUtils();
        loginAsVbox.setVisible(false);
        signAsButton.setVisible(false);
        setupKeyboardNavigation();
    }

    /**
     * Enables keyboard navigation between email and password fields.
     * Supports arrow keys and Enter for movement.
     */
    private void setupKeyboardNavigation() {
        Platform.runLater(() -> emailTextField.requestFocus());

        emailTextField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DOWN, ENTER -> passwordTextField.requestFocus();
            }
        });

        passwordTextField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP -> emailTextField.requestFocus();
            }
        });
    }

    /**
     * Navigates back to the main home page.
     *
     * @param event the action event triggered by the Back button
     */
    @FXML
    private void backButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Navigates to the registration page.
     *
     * @param event the action event triggered by the Register button
     */
    @FXML
    private void registerButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/register.fxml", "MABEC - Register");
    }

    /**
     * Clears error messages displayed on the GUI.
     */
    private void clearErrorLabel() {
        errorLabel.setVisible(false);
        errorLabel.setText("");
    }

    /**
     * Logs in the user with email and password validation.
     * If valid, displays available roles for selection.
     *
     * @param event the action event triggered by the Login button
     */
    @FXML
    private void loginButtonOnAction(ActionEvent event) {
        clearErrorLabel();

        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        if (email.isEmpty()) {
            errorLabel.setVisible(true);
            errorLabel.setText("Empty Email Text Field");
            return;
        }

        if (!controller.accountExists(email)) {
            errorLabel.setVisible(true);
            errorLabel.setText("Account Doesn't Exist");
            return;
        }

        if (!controller.verifyPassword(email, password)) {
            errorLabel.setVisible(true);
            errorLabel.setText("Password Incorrect");
            return;
        }

        if (!controller.verifyIfAccountIsActive(email)) {
            errorLabel.setVisible(true);
            errorLabel.setText("Account isn't Active. Please Contact an Admin");
            return;
        }

        showRoleSelection(email);
    }

    /**
     * Displays available user roles for login after successful authentication.
     *
     * @param email the email of the authenticated user
     */
    private void showRoleSelection(String email) {
        loginAsVbox.setVisible(true);
        emailVbox.setVisible(false);
        passwordVbox.setVisible(false);
        loginButton.setVisible(false);
        signAsButton.setVisible(true);

        loginChoiceBox.setItems(controller.getRoles(email));
    }

    /**
     * Loads the role-specific home page after the user selects a role.
     *
     * @param event the action event triggered by the "Sign As" button
     */
    @FXML
    private void signAsButtonOnAction(ActionEvent event) {
        if (loginChoiceBox.getValue() == null) {
            errorLabel.setVisible(true);
            errorLabel.setText("Choose a Role first");
            return;
        }

        uiUtils.loadFXMLScene(
                event,
                controller.getSignPath(loginChoiceBox.getValue()),
                "MABEC - " + loginChoiceBox.getValue() + " - Home Page"
        );
    }
}
