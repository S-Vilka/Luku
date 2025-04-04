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
    @FXML private TextField usernameField, emailField, teacherID;
    @FXML private PasswordField passwordField, repeatPassword;
    @FXML private Label wrongLogIn;
    @FXML private Button userProfile;

    @FXML
    public void initialize() {
        // Set Enter key event listener for all input fields
        usernameField.setOnKeyPressed(this::handleEnterKey);
        emailField.setOnKeyPressed(this::handleEnterKey);
        passwordField.setOnKeyPressed(this::handleEnterKey);
        repeatPassword.setOnKeyPressed(this::handleEnterKey);
    }

    @FXML
    private void handleEnterKey(KeyEvent event) {
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
            wrongLogIn.setText(getResourceBundle().getString("passwords.mismatch"));
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

    public void registerUserSimple(String username, String password, String email) {
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