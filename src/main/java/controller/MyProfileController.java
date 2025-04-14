package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import model.entity.User;

/**
 * Controller for handling user profile information and password changes.
 */
public class MyProfileController extends LibraryController {

    /** State pane for viewing user profile. */
    @FXML private AnchorPane showState;
    /** State pane for editing user profile. */
    @FXML private AnchorPane editState;
    /** State pane for changing user password. */
    @FXML private AnchorPane passwordChangeState;

    /** TextField for changing name. */
    @FXML private TextField changeNameField;
    /** TextField for changing email. */
    @FXML private TextField changeEmailField;
    /** TextField for changing phone number. */
    @FXML private TextField changePhoneField;

    /** Field for entering old password. */
    @FXML private PasswordField oldPasswordField;
    /** Field for entering new password. */
    @FXML private PasswordField newPasswordField;
    /** Field for confirming new password. */
    @FXML private PasswordField confirmNewPasswordField;

    /** Label for displaying user's name. */
    @FXML private Label nameLabel;
    /** Label for displaying user's email. */
    @FXML private Label emailLabel;
    /** Label for displaying user's phone. */
    @FXML private Label phoneLabel;
    /** Label for showing error messages. */
    @FXML private Label errorMessage;

    /** Button to switch to password change mode. */
    @FXML private Button changePasswordButton;
    /** Button to confirm password change. */
    @FXML private Button donePasswordButton;

    /**
     * Initializes the profile page with saved user data and default view state.
     */
    public final void initializeProfilePage() {
        nameLabel.setText(getSavedUsername());
        emailLabel.setText(getSavedEmail());
        phoneLabel.setText(getSavedPhoneNumber());

        showState.setVisible(true);
        editState.setVisible(false);
        passwordChangeState.setVisible(false);
        changePasswordButton.setVisible(true);
        donePasswordButton.setVisible(false);
    }

    /**
     * Handles switch to user edit mode.
     */
    @FXML
    private void handleEditUser() {
        showState.setVisible(false);
        editState.setVisible(true);
        passwordChangeState.setVisible(false);
    }

    /**
     * Handles saving changes to user profile.
     *
     * @throws Exception if the update fails
     */
    @FXML
    private void handleDoneUser() throws Exception {
        String newName = changeNameField.getText();
        String newEmail = changeEmailField.getText();
        String newPhone = changePhoneField.getText();
        Long userId = getSavedUserId();

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

            getUserService().updateUser(user);

            nameLabel.setText(getSavedUsername());
            emailLabel.setText(getSavedEmail());
            phoneLabel.setText(getSavedPhoneNumber());

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

    /**
     * Handles switch to password change mode.
     */
    @FXML
    private void handleChangePassword() {
        showState.setVisible(false);
        editState.setVisible(false);
        passwordChangeState.setVisible(true);
        changePasswordButton.setVisible(false);
        donePasswordButton.setVisible(true);
    }

    /**
     * Handles confirming password change and updating user password.
     */
    @FXML
    private void handleDonePassword() {
        try {
            String oldPassword = oldPasswordField.getText();
            String newPassword = newPasswordField.getText();
            String confirmNewPassword = confirmNewPasswordField.getText();
            Long userId = getSavedUserId();

            User user = getUserService().getUserById(userId);
            if (user != null) {
                String hashedOldPassword = getUserService()
                        .hashPassword(oldPassword);
                if (!hashedOldPassword.equals(user.getPassword())) {
                    errorMessage.setText(getResourceBundle()
                            .getString("error.oldPasswordIncorrect"));
                    return;
                }

                if (!newPassword.equals(confirmNewPassword)) {
                    errorMessage.setText(getResourceBundle()
                            .getString("error.newPasswordsDoNotMatch"));
                    return;
                }

                String hashedNewPassword = getUserService()
                        .hashPassword(newPassword);
                user.setPassword(hashedNewPassword);
                getUserService().updateUser(user);

                errorMessage.setText("");

                showState.setVisible(true);
                editState.setVisible(false);
                passwordChangeState.setVisible(false);
                changePasswordButton.setVisible(true);
                donePasswordButton.setVisible(false);
            } else {
                errorMessage.setText(getResourceBundle()
                        .getString("error.userNotFound"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.setText(getResourceBundle()
                    .getString("error.passwordChangeError"));
        }
    }
}
