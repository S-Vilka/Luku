package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.entity.Book;
import model.entity.Reservation;
import model.entity.User;
import service.UserService;
import service.BookService;
import service.ReservationService;
import service.NotificationService;
import service.AuthorService;
import util.AuthManager;
import util.JwtUtil;
import view.View;
import java.util.List;

import java.time.LocalDateTime;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class LibraryController {
    private static View View;
    private static Stage primaryStage;
    private static UserService userService;
    private static BookService bookService;
    private static ReservationService reservationService;
    private static NotificationService notificationService;
    private static AuthorService authorService;

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

    public LibraryController() {
        this.userService = new UserService();
        this.bookService = new BookService();
        this.reservationService = new ReservationService();
        this.notificationService = new NotificationService();
        this.authorService = new AuthorService();
    }

    public void loadScene2(String fxmlFile) throws Exception {
        setPrimaryStage(View.getPrimaryStage());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        fxmlLoader.setController(this);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Luku Library");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void loadScene(String fxmlFile) throws Exception {
        setPrimaryStage(View.getPrimaryStage());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Luku Library");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @FXML
    private void handleLogin() throws Exception {
        String userEmail = email.getText();
        String pass = password.getText();

        if (authenticateUser(userEmail, pass)) {
//            wrongLogIn.setText("Login successful!");
//            wrongLogIn.setStyle("-fx-text-fill: green;");
            loadScene("/mainpage.fxml");
            String username = getUserNameByEmail(userEmail);
            userProfile.setText(username);
        } else {
            wrongLogIn.setText("Invalid email or password!");
            wrongLogIn.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void switchToSignUp() throws Exception {
        loadScene("/signup.fxml");
    }

    @FXML
    private void switchToLogin() throws Exception {
        loadScene2("/login.fxml");
    }

    @FXML
    private void chooseAuthor() throws Exception {
    }

    @FXML
    private void chooseUserProfile() throws Exception {
    }

    @FXML
    private void chooseFiction() throws Exception {
        List<Book> books = getBooksByCategory("Fiction");
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


    public boolean validateToken() {
        String token = AuthManager.getInstance().getToken();
        return JwtUtil.validateToken(token);
    }

    public boolean authenticateUser(String email, String password) {
        return userService.authenticateUser(email, password);
    }

    public String getUserNameByEmail(String email) {
        return userService.getUserByEmail(email).getUsername();
    }

    public void registerUserSimple(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole("student");
        user.setBookCount(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setDeletedAt(null);

        userService.registerUser(user);
    }

    public void registerUser(String username, String password, String email, String phone, String role, int book_count, LocalDateTime created_at, LocalDateTime deleted_at) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);
        user.setBookCount(book_count);
        user.setCreatedAt(created_at);
        user.setDeletedAt(deleted_at);

        userService.registerUser(user);
    }

    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    public List<Book> getBooksByCategory(String category) {
        if (!validateToken()) {
            throw new SecurityException("Invalid token");
        }
        return bookService.getBooksByCategory(category);
    }

    public void reserveBook(Long userId, Long bookId) {
        if (!validateToken()) {
            throw new SecurityException("Invalid token");
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        if ("student".equalsIgnoreCase(user.getRole()) && user.getBookCount() >= 5) {
            throw new IllegalArgumentException("Students cannot reserve more than 5 books.");
        }

        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setBookId(bookId);

        reservationService.createReservation(reservation);

        // Update the user's book count
        user.setBookCount(user.getBookCount() + 1);
        userService.updateUser(user);
    }


    public void extendDueDate(Long reservationId, LocalDateTime newDueDate) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation != null) {
            reservation.setDueDate(newDueDate);
            reservationService.updateReservation(reservation);
        } else {
            throw new IllegalArgumentException("Reservation with ID " + reservationId + " not found.");
        }
    }

    public List<Book> searchBooksByAuthor(String authorFirstName, String authorLastName) {
        return authorService.getBooksByAuthor(authorFirstName, authorLastName);

    }

    public List<Book> searchBooksByTitle(String title) {
        return bookService.getBooksByTitle(title);
    }

    public List<Book> searchBooksByCategory(String genre) {
        return bookService.getBooksByCategory(genre);
    }

    public List<Book> searchBooksByLanguage(String language) {
        return bookService.getBooksByLanguage(language);
    }

    public int getUserBookCount(Long userId) {
        return userService.getUserBookCount(userId);
    }

    public List<Reservation> getMyBookings(Long userId) {
        return reservationService.getReservationsByUserId(userId);
    }

    public void updateUserInfo(User user) {
        userService.updateUser(user);
    }


    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public void setAuthorService(AuthorService authorService) {
        this.authorService = authorService;
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    public UserService getUserService() {
        return userService;
    }
    public ReservationService getReservationService() {
        return reservationService;
    }
    public AuthorService getAuthorService() {
        return authorService;
    }
    public BookService getBookService() {
        return bookService;
    }
    public NotificationService getNotificationService() {
        return notificationService;
    }
    public void setMainApp(View View) {
        this.View = View;;
    }
    public View getView() {
        return View;
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
