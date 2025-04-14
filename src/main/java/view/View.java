package view;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import controller.LibraryController;


public final class View extends Application {

    /**
     * The controller responsible for handling library-related logic and events.
     */
    private LibraryController controller;
    /**
     * The primary stage (window) of the JavaFX application.
     */
    private Stage primaryStage;

    /**
     * The main content area of the application's UI, injected via FXML.
     */
    @FXML private AnchorPane bodyBox;

    @Override
    public void start(final Stage stage) throws Exception {
        this.primaryStage = stage;
        this.controller = new LibraryController();
        controller.setMainApp(this);
        controller.setPrimaryStage(stage);

        FXMLLoader fxmlLoader =
                new FXMLLoader(getClass().getResource("/mainpage.fxml"));
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

    /**
     * Returns the primary stage (window) of the application.
     *
     * @return the primary Stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Returns the controller responsible for handling library-related logic.
     *
     * @return the LibraryController instance
     */
    public LibraryController getController() {
        return controller;
    }
}