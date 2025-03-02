package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.entity.Author;
import model.entity.Book;
import model.entity.User;
import service.BookService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class myProfileController extends LibraryController{

    @FXML
    private TextField changeNameField;
    @FXML
    private TextField changeEmailField;
    @FXML
    private TextField changePhoneField;

    @FXML
    private void handleUpdateUser() throws Exception {
        String newName = changeNameField.getText();
        String newEmail = changeEmailField.getText();
        String newPhone = changePhoneField.getText();
        Long userId = getSavedUserId();

        // Retrieve the user's information and update it
        User user = getUserService().getUserById(userId);
        if (user != null) {
            if (!newName.isEmpty()) {
                user.setUsername(newName);
            }
            if (!newEmail.isEmpty()) {
                user.setEmail(newEmail);
            }
            if (!newPhone.isEmpty()) {
                user.setPhone(newPhone);
            }

            // Save the updated user
            getUserService().updateUser(user);

            // Reload profile page
            loadScene("/myProfile.fxml");

        } else {
            // Handle the case where the user is not found
            System.out.println("User not found");
        }
    }

}
