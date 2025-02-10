package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.LibraryController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class View extends Application {
    private LibraryController controller;
    private Stage primaryStage;

    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Button enterBurron;
    @FXML
    private Label wrongLogIn;

    @FXML
    private void handleLogin() {
        String user = email.getText();
        String pass = password.getText();

        if (controller.authenticateUser(user, pass)) {
            wrongLogIn.setText("Login successful!");
            wrongLogIn.setStyle("-fx-text-fill: green;");
        } else {
            wrongLogIn.setText("Invalid email or password!");
            wrongLogIn.setStyle("-fx-text-fill: red;");
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.controller = new LibraryController();
        controller.setMainApp(this);
        loadScene("/login.fxml");
    }

    public void loadScene(String fxmlFile) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        fxmlLoader.setController(this);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Luku Library");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}