package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.LibraryController;


public class View extends Application {
    private LibraryController controller;
    private Stage primaryStage;


    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.controller = new LibraryController();
        controller.setMainApp(this);
        controller.loadScene("/mainpage.fxml");
        primaryStage.setOnCloseRequest(event -> {
            controller.stopDueDateChecker(); // Stop when app closes
        });
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public LibraryController getController() {
        return controller;
    }
}