package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.entity.*;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import javafx.scene.layout.VBox;

public class LibraryController {
    private static View View;
    private static Stage primaryStage;
    private static UserService userService;
    private static BookService bookService;
    private static ReservationService reservationService;
    private static NotificationService notificationService;
    private static AuthorService authorService;
    private static String savedUsername;
    private static String savedEmail;
    private static Long savedUserId;

    @FXML
    private TextField email, usernameField, emailField, teacherID, searchBar;
    @FXML
    private PasswordField password, passwordField, repeatPassword;
    @FXML
    private Button enterBurron, loginButtonTop, categoryButton, languageButton, authorButtom, searchButton, loginButton, signupButton, userProfile, fictionButton, nonFictionButton, scienceButton, historyButton, englishButton, finnishButton, swedishButton, searchButton2, reserveButton, extendButton, returnButton;
    @FXML
    private Label locationTag, wrongLogIn, bookName, author, publicationDate, availability, borrowDate, dueDate, bookId;
    @FXML
    private ImageView noti;
    @FXML
    private Circle notiCircle;
    @FXML
    private VBox notiVBox;
    @FXML
    private AnchorPane searchBox, categoryList, languageList, userList, bookBox, userProfileBox, loginBox, notiBox;

    public LibraryController() {
        this.userService = new UserService();
        this.bookService = new BookService();
        this.reservationService = new ReservationService();
        this.notificationService = new NotificationService();
        this.authorService = new AuthorService();
    }

    public void loadScene(String fxmlFile) throws Exception {
        setPrimaryStage(View.getPrimaryStage());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        if ("/mainpage.fxml".equals(fxmlFile)) {
            fxmlLoader.setController(this);
        }
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Luku Library");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        updateHeader();
    }

    public void updateHeader() {
        AnchorPane loginBox = (AnchorPane) primaryStage.getScene().lookup("#loginBox");
        AnchorPane userProfileBox = (AnchorPane) primaryStage.getScene().lookup("#userProfileBox");

        if (loginBox != null && userProfileBox != null) {
            if (validateToken()) {
                loginBox.setVisible(false);
                userProfileBox.setVisible(true);
                userProfile = (Button) primaryStage.getScene().lookup("#userProfile");
                userProfile.setText(savedUsername);
            } else {
                loginBox.setVisible(true);
                userProfileBox.setVisible(false);
            }
        } else {
            System.out.println("Error: loginBox or userProfileBox not found.");
        }
    }

    @FXML
    private void toggleNotiBox() {
        notiBox.setVisible(!notiBox.isVisible());
        if (notiBox.isVisible()) {
            loadNotifications();
        }
    }

    private void loadNotifications() {
        notiVBox.getChildren().clear();
        User user = userService.getUserByEmail(savedEmail);
        Long userId = user.getUserId();
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        for (Notification notification : notifications) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/notiBox.fxml"));
                AnchorPane notiPane = loader.load();
                Label notiTime = (Label) notiPane.lookup("#notiTime");
                Label notiMessage = (Label) notiPane.lookup("#notiMessage");

                notiTime.setText(notification.getCreatedAt().toString());
                notiMessage.setText(notification.getMessage());

                notiVBox.getChildren().add(notiPane);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        List<Author> authors = authorService.getAllAuthors();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/authorsPage.fxml"));
        Parent root = loader.load();
        authorsPageController controller = loader.getController();
        controller.setAuthors(authors);
        primaryStage.setTitle("Luku Library - Authors");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        updateHeader();
    }

    @FXML
    private void chooseUserProfile() throws Exception {
        userList.setVisible(!userList.isVisible());
    }

    private void chooseCategory(String category) throws Exception {
        String fxmlFile = "/category" + category + ".fxml";
        List<Book> books = getBooksByCategory(category);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        categoryPageController controller = loader.getController();
        controller.clearBookLists();
        controller.getAvailabilityCheckBox().setSelected(false);
        controller.setBooks(books);
        primaryStage.setTitle("Luku Library - " + category);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        updateHeader();
    }

    @FXML
    private void chooseFiction() throws Exception {
        chooseCategory("Fiction");
    }

    @FXML
    private void chooseNonFiction() throws Exception {
        chooseCategory("Non-Fiction");
    }

    @FXML
    private void chooseScience() throws Exception {
        chooseCategory("Science");
    }

    @FXML
    private void chooseHistory() throws Exception {
        chooseCategory("History");
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

    @FXML
    private void chooseProfile() throws Exception {
        loadScene("/myProfile.fxml");
    }

    @FXML
    private void logout() throws Exception {
        savedUsername = null;
        savedEmail = null;
        AuthManager.getInstance().setToken(null);
        loadScene("/login.fxml");
    }

    @FXML
    private void chooseBookings() throws Exception {
        Long userId = getSavedUserId();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/myBookings.fxml"));
        Parent root = loader.load();
        myBookingController controller = loader.getController();
        controller.setBooksForUser(userId);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
//        loadScene();
    }

    public void chooseReserve(Long bookId) throws Exception {
        Long userId = getSavedUserId();
        if (userId == null) {
            throw new IllegalStateException("User is not logged in.");
        }

        Book book = bookService.getBookById(bookId);
        String category = book.getCategory();

        try {
            reserveBook(userId, bookId);
            chooseCategory(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validateToken() {
        String token = AuthManager.getInstance().getToken();
        if (token == null) {
            return false;
        }
        return JwtUtil.validateToken(token);
    }

    public String getUserNameByEmail(String email) {
        return userService.getUserByEmail(email).getUsername();
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
        notificationService.createNotificationForReservation(reservation.getReservationId());
        bookService.setBookAvailability(bookId, "Checked Out");

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

    public void extendReservation(Long reservationId) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation != null) {
            LocalDateTime newDueDate = reservation.getDueDate().plusDays(7);
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

//**Setters**//

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

    public void setMainApp(View View) {
        this.View = View;;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setSavedUsername(String savedUsername) {
        this.savedUsername = savedUsername;
    }

    public void setSavedEmail(String savedEmail) {
        this.savedEmail = savedEmail;
    }

    public void setSavedUserId(Long savedUserId) {
        this.savedUserId = savedUserId;
    }

    public Reservation getReservationByUserAndBook (Long userId, Long bookId) {
        return reservationService.getReservationByUserAndBook(userId, bookId);
    }

//**Getters**//

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

    public View getView() {
        return View;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public String getSavedUsername() {
        return savedUsername;
    }

    public String getSavedEmail() {
        return savedEmail;
    }

    public Long getSavedUserId() {
        return savedUserId;
    }
}
