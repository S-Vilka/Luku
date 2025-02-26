package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.entity.Book;
import model.entity.Reservation;
import model.entity.User;
import service.UserService;
import service.BookService;
import service.ReservationService;
import service.NotificationService;
import service.AuthorService;
import util.AuthManager;
import util.JwtUtil;
import view.View;
import java.util.List;

import java.time.LocalDateTime;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

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
        loadScene("/mainpage.fxml");
        userProfile.setText(username);
    }
    @FXML
    private void switchToSignUp() throws Exception {
//        View = getView();
//        primaryStage = getPrimaryStage();
        loadScene2("/signup.fxml");
    }

    @FXML
    private void switchToLogin() throws Exception {
//        View = getView();
//        primaryStage = getPrimaryStage();
        loadScene("/login.fxml");
    }
}
