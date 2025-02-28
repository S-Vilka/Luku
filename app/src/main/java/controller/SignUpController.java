package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.entity.User;
import service.UserService;

import java.time.LocalDateTime;

public class SignUpController extends LibraryController {
    @FXML
    private TextField usernameField, emailField, teacherID;
    @FXML
    private PasswordField passwordField, repeatPassword;
    @FXML
    private Label wrongLogIn;
    @FXML
    private Button userProfile;

    @FXML
    private void handleSignup() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String repeat = repeatPassword.getText();
        String email = emailField.getText();

        // Check if email already exists
        if (getUserByEmail(email) != null) {
            wrongLogIn.setText("Email already exists!");
            wrongLogIn.setStyle("-fx-text-fill: red;");
            return;
        }

        // Check if passwords match
        if (!password.equals(repeat)) {
            wrongLogIn.setText("Passwords do not match!");
            wrongLogIn.setStyle("-fx-text-fill: red;");
            return;
        }

        // Create a new user
        registerUserSimple(username, password, email);
        setSavedUsername(username);
        setSavedEmail(email);
        loadScene("/mainpage.fxml");
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
