/**
 * This package contains controllers for handling user interactions
 * and managing the application's UI logic.
 */
package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.entity.User;
import service.UserService;

public final class LoginController extends LibraryController {
    /**
     * The email input field for the user to enter their email address.
     */
    @FXML private TextField email;
    /**
     * The password input field for the user to enter their password.
     */
    @FXML private PasswordField password;
    /**
     * The label that displays error messages related to login.
     */
    @FXML private Label wrongLogIn;
    /**
     * The button that allows the user to create a new account.
     */
    @FXML private Button userProfile;
    /**
     * The button that initiates the login process when clicked.
     */
    @FXML
    public void initialize() {
        // Set Enter key event listener for both fields
        email.setOnKeyPressed(this::handleEnterKey);
        password.setOnKeyPressed(this::handleEnterKey);
    }

    @FXML
    private void handleEnterKey(final KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            try {
                handleLogin();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleLogin() throws Exception {
        String userEmail = email.getText();
        String pass = password.getText();

        if (authenticateUser(userEmail, pass)) {
            String username = getUserNameByEmail(userEmail);
            String phoneNumber = getUserPhone(userEmail);
            setSavedUsername(username);
            setSavedEmail(userEmail);
            setSavedPhoneNumber(phoneNumber);
            User user = getUserByEmail(userEmail);
            Long userId = user.getUserId();
            setSavedUserId(userId);
            loadScene("/mainpage2.fxml");
            startDueDateChecker();
        } else {
            wrongLogIn.setVisible(true);
            wrongLogIn.setText(
                getResourceBundle().getString("wrongLogIn.label"));
            wrongLogIn.setStyle("-fx-text-fill: red;");
        }
    }

    /**
     * Retrieves the user's name based on their email address.
     *
     * @param userEmail The email address of the user.
     * @param userPassword The password of the user.
     * @return True if the user is authenticated, false otherwise.
     */
    public boolean authenticateUser(
            final String userEmail,
            final String userPassword
    ) {
        UserService userService = getUserService();
        return userService.authenticateUser(userEmail, userPassword);
    }
}
