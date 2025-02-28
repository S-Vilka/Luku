package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import service.UserService;

public class LoginController extends LibraryController {
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Label wrongLogIn;
    @FXML
    private Button userProfile;

    @FXML
    private void handleLogin() throws Exception {
        String userEmail = email.getText();
        String pass = password.getText();

        if (authenticateUser(userEmail, pass)) {
            String username = getUserNameByEmail(userEmail);
            setSavedUsername(username);
            setSavedEmail(userEmail);
            loadScene("/mainpage.fxml");
        } else {
            wrongLogIn.setText("Invalid email or password!");
            wrongLogIn.setStyle("-fx-text-fill: red;");
        }
    }

    public boolean authenticateUser(String email, String password) {
        UserService userService = getUserService();
        return userService.authenticateUser(email, password);
    }
}