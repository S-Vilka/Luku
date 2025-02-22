package view;
import model.entity.Book;
import java.util.List;
import model.entity.User;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import controller.LibraryController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class View extends Application {
    private LibraryController controller;
    private Stage primaryStage;

    @FXML
    private TextField email, usernameField, emailField, teacherID, searchBar;
    @FXML
    private PasswordField password, passwordField, repeatPassword;
    @FXML
    private Button enterBurron, loginButtonTop, categoryButton, languageButton, authorButtom, searchButton, loginButton, signupButton, userProfile, fictionButton, nonFictionButton, scienceButton, historyButton, englishButton, finnishButton, swedishButton, searchButton2;
    @FXML
    private Label wrongLogIn, bookName, author, publicationDate, availability;
    @FXML
    private ImageView profilePicture;
    @FXML
    private AnchorPane searchBox, categoryList, languageList;

    @FXML
    private void handleLogin() throws Exception {
        String userEmail = email.getText();
        String pass = password.getText();

        if (controller.authenticateUser(userEmail, pass)) {
//            wrongLogIn.setText("Login successful!");
//            wrongLogIn.setStyle("-fx-text-fill: green;");
            loadScene("/mainpage.fxml");
            String username = controller.getUserNameByEmail(userEmail);
            userProfile.setText(username);
        } else {
            wrongLogIn.setText("Invalid email or password!");
            wrongLogIn.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleSignup() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String repeat = repeatPassword.getText();
        String email = emailField.getText();

        // Check if email already exists
        if (controller.getUserByEmail(email) != null) {
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
        controller.registerUserSimple(username, password, email);
        loadScene("/mainpage.fxml");
        userProfile.setText(username);
    }

    @FXML
    private void switchToSignUp() throws Exception {
        loadScene("/signup.fxml");
    }

    @FXML
    private void switchToLogin() throws Exception {
        loadScene("/login.fxml");
    }

    @FXML
    private void chooseAuthor() throws Exception {
    }

    @FXML
    private void chooseUserProfile() throws Exception {
    }

    @FXML
    private void chooseFiction() throws Exception {
        List<Book> books = controller.getBooksByCategory("Fiction");
        if (books == null || books.isEmpty()) {
            System.out.println("Access denied. Invalid token.");
            return;
        }
        loadScene("/categoryFiction.fxml");
        Book book = books.get(0);
        bookName.setText(book.getTitle().toString());
        publicationDate.setText(book.getPublicationDate().toString());
        availability.setText(book.getAvailabilityStatus().toString());
    }

    @FXML
    private void chooseNonFiction() throws Exception {
    }

    @FXML
    private void chooseScience() throws Exception {
    }

    @FXML
    private void chooseHistory() throws Exception {
    }

    @FXML
    private void chooseEnglish() throws Exception {
    }

    @FXML
    private void chooseFinnish() throws Exception {
    }

    @FXML
    private void chooseSwedish() throws Exception {
    }

    @FXML
    private void searchAction() throws Exception {
    }

    @FXML
    private void chooseCategory() {
        categoryList.setVisible(!categoryList.isVisible());
    }

    @FXML
    private void chooseLanguage() {
        languageList.setVisible(!languageList.isVisible());
    }

    @FXML
    private void chooseSearch() {
        searchBox.setVisible(!searchBox.isVisible());
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