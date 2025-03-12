package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import model.entity.User;

public class myProfileController extends LibraryController {

    @FXML private AnchorPane showState, editState, passwordChangeState;
    @FXML private TextField changeNameField, changeEmailField, changePhoneField;
    @FXML private PasswordField oldPasswordField, newPasswordField, confirmNewPasswordField;
    @FXML private Label nameLabel, emailLabel, phoneLabel, errorMessage;
    @FXML private Button changePasswordButton, donePasswordButton;

    public void initializeProfilePage() {
        // Initialize the profile information
        nameLabel.setText(getSavedUsername());
        emailLabel.setText(getSavedEmail());
        phoneLabel.setText(getSavedPhoneNumber());

        // Set initial visibility
        showState.setVisible(true);
        editState.setVisible(false);
        passwordChangeState.setVisible(false);
        changePasswordButton.setVisible(true);
        donePasswordButton.setVisible(false);
    }

    @FXML
    private void handleEditUser() {
        // Show edit state and hide others
        showState.setVisible(false);
        editState.setVisible(true);
        passwordChangeState.setVisible(false);
    }

    @FXML
    private void handleDoneUser() throws Exception {
        String newName = changeNameField.getText();
        String newEmail = changeEmailField.getText();
        String newPhone = changePhoneField.getText();
        Long userId = getSavedUserId();

        // Retrieve the user's information and update it
        User user = getUserService().getUserById(userId);
        if (user != null) {
            if (!newName.isEmpty()) {
                user.setUsername(newName);
                setSavedUsername(newName);
            }
            if (!newEmail.isEmpty()) {
                user.setEmail(newEmail);
                setSavedEmail(newEmail);
            }
            if (!newPhone.isEmpty()) {
                user.setPhone(newPhone);
                setSavedPhoneNumber(newPhone);
            }

            // Save the updated user
            getUserService().updateUser(user);

            // Update profile information
            nameLabel.setText(getSavedUsername());
            emailLabel.setText(getSavedEmail());
            phoneLabel.setText(getSavedPhoneNumber());

            // Show showState and hide others
            showState.setVisible(true);
            editState.setVisible(false);
            passwordChangeState.setVisible(false);
            changePasswordButton.setVisible(true);
            donePasswordButton.setVisible(false);

            updateHeader();
        } else {
            System.out.println("User not found");
        }
    }

    @FXML
    private void handleChangePassword() {
        // Show password change state and hide others
        showState.setVisible(false);
        editState.setVisible(false);
        passwordChangeState.setVisible(true);
        changePasswordButton.setVisible(false);
        donePasswordButton.setVisible(true);
    }

    @FXML
    private void handleDonePassword() {
        try {
            String oldPassword = oldPasswordField.getText();
            String newPassword = newPasswordField.getText();
            String confirmNewPassword = confirmNewPasswordField.getText();
            Long userId = getSavedUserId();

            User user = getUserService().getUserById(userId);
            if (user != null) {
                String hashedOldPassword = getUserService().hashPassword(oldPassword);
                if (!hashedOldPassword.equals(user.getPassword())) {
                    errorMessage.setText("Old password is incorrect.");
                    return;
                }

                if (!newPassword.equals(confirmNewPassword)) {
                    errorMessage.setText("New passwords do not match.");
                    return;
                }

                String hashedNewPassword = getUserService().hashPassword(newPassword);
                user.setPassword(hashedNewPassword);
                getUserService().updateUser(user);

                // Clear error message
                errorMessage.setText("");

                // Switch back to showState
                showState.setVisible(true);
                editState.setVisible(false);
                passwordChangeState.setVisible(false);
                changePasswordButton.setVisible(true);
                donePasswordButton.setVisible(false);
            } else {
                errorMessage.setText("User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.setText("An error occurred while changing the password.");
        }
    }
}