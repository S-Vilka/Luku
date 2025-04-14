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

import java.time.LocalDateTime;

public class SignUpController extends LibraryController {
    /**
     * This class handles the sign-up process for new users.
     * It validates user input,
     * checks for existing users, and registers new users.
     */
    @FXML private TextField usernameField;
    /** TextField for the username input. */
    @FXML private TextField emailField;
    /** TextField for the teacher ID input. */
    @FXML private TextField teacherID;
    /** PasswordField for the password input. */
    @FXML private PasswordField passwordField;
    /** PasswordField for the password confirmation input. */
    @FXML private PasswordField repeatPassword;
    /** Label for displaying error messages. */
    @FXML private Label wrongLogIn;
    /** Label for displaying error messages. */
    @FXML private Button userProfile;

    /** Initialize the class. */
    @FXML
    public void initialize() {
        // Set Enter key event listener for all input fields
        usernameField.setOnKeyPressed(this::handleEnterKey);
        emailField.setOnKeyPressed(this::handleEnterKey);
        passwordField.setOnKeyPressed(this::handleEnterKey);
        repeatPassword.setOnKeyPressed(this::handleEnterKey);
    }

    @FXML
    private void handleEnterKey(final KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            try {
                handleSignup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleSignup() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String repeat = repeatPassword.getText();
        String email = emailField.getText();

        // Check if email already exists
        if (getUserByEmail(email) != null) {
            wrongLogIn.setText(getResourceBundle().getString("email.exists"));
            wrongLogIn.setStyle("-fx-text-fill: red;");
            return;
        }

        // Check if passwords match
        if (!password.equals(repeat)) {
            wrongLogIn.setText(getResourceBundle()
                    .getString("passwords.mismatch"));
            wrongLogIn.setStyle("-fx-text-fill: red;");
            return;
        }

        // Create a new user
        registerUserSimple(username, password, email);
        setSavedUsername(username);
        setSavedEmail(email);
        User user = getUserByEmail(email);
        Long userId = user.getUserId();
        String phoneNumber = getUserPhone(email);
        setSavedPhoneNumber(phoneNumber);
        setSavedUserId(userId);
        loadScene("/mainpage2.fxml");
        startDueDateChecker();
    }

    /**
     * Registers a new user with the provided username, password, and email.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @param email    the email of the new user
     */
    public void registerUserSimple(final String username,
                                   final String password, final String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole("student");
        user.setBookCount(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setDeletedAt(null);

        UserService userService = getUserService();
        userService.registerUser(user);
    }
}
