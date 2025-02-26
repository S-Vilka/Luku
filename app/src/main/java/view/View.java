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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Luku Library");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public LibraryController getController() {
        return controller;
    }
}