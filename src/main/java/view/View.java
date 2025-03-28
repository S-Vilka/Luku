package view;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import controller.LibraryController;


public class View extends Application {
    private LibraryController controller;
    private Stage primaryStage;

    @FXML private AnchorPane bodyBox;


    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.controller = new LibraryController();
        controller.setMainApp(this);
        controller.setPrimaryStage(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mainpage.fxml"));
        fxmlLoader.setController(controller);
        fxmlLoader.setResources(controller.getResourceBundle());
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Luku Library");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        controller.updateHeader();

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