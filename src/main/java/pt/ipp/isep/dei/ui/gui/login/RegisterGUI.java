package pt.ipp.isep.dei.ui.gui.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pt.ipp.isep.dei.controller.acountRelated.RegisterController;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller responsible for handling user registration.
 * Provides input validation, password checks, and navigation to related pages.
 */
public class RegisterGUI implements Initializable {

    private RegisterController controller;
    private UIUtils uiUtils;

    @FXML
    private TextField emailTextField, passwordTextField, confirmPasswordTextField;

    @FXML
    private Label least8CharactersLabel, capitalLeterLabel, oneNumberLabel, specialCharacterLabel, errorLabel;

    /**
     * Initializes the registration GUI by creating the controller and utility instance.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new RegisterController();
        uiUtils = new UIUtils();
    }

    /**
     * Navigates back to the application home page.
     *
     * @param event the action event triggered by the Back button
     */
    public void backButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Navigates to the login page.
     *
     * @param event the action event triggered by the Login button
     */
    public void loginButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/signIn.fxml", "MABEC - Sign In");
    }

    /**
     * Handles the registration process.
     * Validates user input, checks password rules, and creates a new account if all checks pass.
     *
     * @param event the action event triggered by the Register button
     */
    public void registerButtonOnAction(ActionEvent event) {

        restartLabelColors();

        if (!controller.verifyMail(emailTextField.getText())) {
            errorLabel.setVisible(true);
            errorLabel.setText("Invalid email address");
            return;
        }

        if (controller.verifyIsAccountAlreadyExists(emailTextField.getText())) {
            errorLabel.setVisible(true);
            errorLabel.setText("Account Already Exists");
            return;
        }

        if (!passwordRelated(passwordTextField.getText())) {
            return;
        }

        if (!passwordTextField.getText().equals(confirmPasswordTextField.getText())) {
            errorLabel.setVisible(true);
            errorLabel.setText("Passwords don't Match");
            return;
        }

        controller.createUser(emailTextField.getText(), passwordTextField.getText());

        loginButtonOnAction(event);
    }

    /**
     * Validates the password against multiple criteria:
     * - Minimum length
     * - Contains capital letters
     * - Contains at least one number
     * - Contains at least one special character
     *
     * Updates the GUI labels to indicate failed conditions.
     *
     * @param password the password string to validate
     * @return true if the password meets all criteria, false otherwise
     */
    private boolean passwordRelated(String password) {
        boolean isValid = true;

        if (!controller.verifyPasswordDigitNumber(password)) {
            least8CharactersLabel.setStyle("-fx-text-fill: #ff0000;");
            isValid = false;
        }

        if (!controller.verifyPasswordIncludesCapitalLetter(password)) {
            capitalLeterLabel.setStyle("-fx-text-fill: #ff0000;");
            isValid = false;
        }

        if (!controller.verifyOneNumber(password)) {
            oneNumberLabel.setStyle("-fx-text-fill: #ff0000;");
            isValid = false;
        }

        if (!controller.verifySpecialCharacter(password)) {
            specialCharacterLabel.setStyle("-fx-text-fill: #ff0000;");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Resets all label colors and clears error messages before each validation attempt.
     */
    private void restartLabelColors() {
        least8CharactersLabel.setStyle("-fx-text-fill: #625e5e;");
        capitalLeterLabel.setStyle("-fx-text-fill: #625e5e;");
        oneNumberLabel.setStyle("-fx-text-fill: #625e5e;");
        specialCharacterLabel.setStyle("-fx-text-fill: #625e5e;");

        errorLabel.setVisible(false);
        errorLabel.setText("");
    }
}
