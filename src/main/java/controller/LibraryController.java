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
import java.util.Locale;
import java.util.ResourceBundle;

import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import javafx.scene.layout.VBox;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private static String savedPhoneNumber;
    private static ScheduledExecutorService scheduler;
    private static boolean notiCircleStatus;
    private static String currentLanguage = "English";

    @FXML private TextField email, usernameField, emailField, teacherID, searchBar1;
    @FXML private PasswordField password, passwordField, repeatPassword;
    @FXML private Button searchButton21, logoutButton, myBookingsButton, enterBurron, loginButtonTop, categoryButton, languageButton, authorButton, searchButton, loginButton, signupButton, userProfile, fictionButton, nonFictionButton, scienceButton, historyButton, englishButton, finnishButton, swedishButton, searchButton2, reserveButton, extendButton, returnButton, appLanguage, languageEnglish, languageRussian, languageUrdu, profileButton;
    @FXML private Label locationTag, wrongLogIn, bookName, author, publicationDate, availability, borrowDate, dueDate, bookId, slogan;
    @FXML private ImageView noti, languageBall;
    @FXML private Circle notiCircle;
    @FXML private VBox notiVBox;
    @FXML private AnchorPane searchBox, categoryList, languageList, userList, bookBox, userProfileBox, loginBox, notiBox, bodyBox, appLanguageBox;
    @FXML private ImageView lukulogo;


    public LibraryController() {
        this.userService = new UserService();
        this.bookService = new BookService();
        this.reservationService = new ReservationService();
        this.notificationService = new NotificationService();
        this.authorService = new AuthorService();
    }

    public void setLanguageText() {
        Locale locale = null;
        if (currentLanguage.equals("English")) {
            locale = new Locale("en", "US");
        } else if (currentLanguage.equals("Русский")) {
            locale = new Locale("ru", "RUS");
        } else if (currentLanguage.equals("اردو")) {
            locale = new Locale("ur", "Pak");
        }
        ResourceBundle bundle = ResourceBundle.getBundle("resource_bundle", locale);
        Label slogan = (Label) primaryStage.getScene().lookup("#slogan");
        slogan.setText(bundle.getString("welcome"));
        Button loginButtonTop = (Button) primaryStage.getScene().lookup("#loginButtonTop");
        loginButtonTop.setText(bundle.getString("login"));
        Button profileButton = (Button) primaryStage.getScene().lookup("#profileButton");
        profileButton.setText(bundle.getString("profile.button"));
        Button myBookingsButton = (Button) primaryStage.getScene().lookup("#myBookingsButton");
        myBookingsButton.setText(bundle.getString("myBookings.button"));
        Button logoutButton = (Button) primaryStage.getScene().lookup("#logoutButton");
        logoutButton.setText(bundle.getString("logout"));
        Button categoryButton = (Button) primaryStage.getScene().lookup("#categoryButton");
        categoryButton.setText(bundle.getString("category"));
        Button fictionButton = (Button) primaryStage.getScene().lookup("#fictionButton");
        fictionButton.setText(bundle.getString("fiction.button"));
        Button nonFictionButton = (Button) primaryStage.getScene().lookup("#nonFictionButton");
        nonFictionButton.setText(bundle.getString("nonFiction.button"));
        Button scienceButton = (Button) primaryStage.getScene().lookup("#scienceButton");
        scienceButton.setText(bundle.getString("science.button"));
        Button historyButton = (Button) primaryStage.getScene().lookup("#historyButton");
        historyButton.setText(bundle.getString("history.button"));
        Button languageButton = (Button) primaryStage.getScene().lookup("#languageButton");
        languageButton.setText(bundle.getString("language"));
        Button authorButton = (Button) primaryStage.getScene().lookup("#authorButton");
        authorButton.setText(bundle.getString("author"));
        Button englishButton = (Button) primaryStage.getScene().lookup("#englishButton");
        englishButton.setText(bundle.getString("english.button"));
        Button finnishButton = (Button) primaryStage.getScene().lookup("#finnishButton");
        finnishButton.setText(bundle.getString("finnish.button"));
        Button swedishButton = (Button) primaryStage.getScene().lookup("#swedishButton");
        swedishButton.setText(bundle.getString("swedish.button"));
        Button searchButton = (Button) primaryStage.getScene().lookup("#searchButton");
        searchButton.setText(bundle.getString("searchBar"));
        Button searchButton21 = (Button) primaryStage.getScene().lookup("#searchButton21");
        searchButton21.setText(bundle.getString("searchBar"));
        TextField searchBar1 = (TextField) primaryStage.getScene().lookup("#searchBar1");
        searchBar1.setPromptText(bundle.getString("searchBar"));
        Button loginButton = (Button) primaryStage.getScene().lookup("#loginButton");
        if (loginButton != null) {
            loginButton.setText(bundle.getString("login"));
        }
        Button signupButton = (Button) primaryStage.getScene().lookup("#signupButton");
        if (signupButton != null) {
            signupButton.setText(bundle.getString("signup"));
        }
        Label emailLabel = (Label) primaryStage.getScene().lookup("#emailLabel");
        if (emailLabel != null) {
            emailLabel.setText(bundle.getString("email"));
        }
        Label passwordLabel = (Label) primaryStage.getScene().lookup("#passwordLabel");
        if (passwordLabel != null) {
            passwordLabel.setText(bundle.getString("password"));
        }
        TextField email = (TextField) primaryStage.getScene().lookup("#email");
        if (email != null) {
            email.setPromptText(bundle.getString("email"));
        }
        PasswordField password = (PasswordField) primaryStage.getScene().lookup("#password");
        if (password != null) {
            password.setPromptText(bundle.getString("password"));
        }
        Button enterBurron = (Button) primaryStage.getScene().lookup("#enterBurron");
        if (enterBurron != null) {
            enterBurron.setText(bundle.getString("enter.button"));
        }
        Label wrongLogIn = (Label) primaryStage.getScene().lookup("#wrongLogIn");
        if (wrongLogIn != null) {
            wrongLogIn.setText(bundle.getString("wrongLogIn.Label"));
        }
    }

    public void updateHeader() {
        disablePanelVisibility();
        AnchorPane loginBox = (AnchorPane) primaryStage.getScene().lookup("#loginBox");
        AnchorPane userProfileBox = (AnchorPane) primaryStage.getScene().lookup("#userProfileBox");
        Button appLanguage = (Button) primaryStage.getScene().lookup("#appLanguage");
        appLanguage.setText(currentLanguage);
        setLanguageText();

        if (loginBox != null && userProfileBox != null) {
            if (validateToken()) {
                loginBox.setVisible(false);
                userProfileBox.setVisible(true);
                userProfile = (Button) primaryStage.getScene().lookup("#userProfile");
                userProfile.setText(savedUsername);
                Circle notiCircle = (Circle) primaryStage.getScene().lookup("#notiCircle");
                notiCircle.setVisible(notiCircleStatus);
            } else {
                loginBox.setVisible(true);
                userProfileBox.setVisible(false);
            }
        } else {
            System.out.println("Error: loginBox or userProfileBox not found.");
        }
    }

    public boolean validateToken() {
        String token = AuthManager.getInstance().getToken();
        if (token == null) {
            return false;
        }
        return JwtUtil.validateToken(token);
    }

//== App Language Change Functions ==//
    @FXML
    private void chooseLanguageEnglish() throws Exception {
        currentLanguage = "English";
        updateHeader();
    }

    @FXML
    private void chooseLanguageRussian() throws Exception {
        currentLanguage = "Русский";
        updateHeader();
    }

    @FXML
    private void chooseLanguageUrdu() throws Exception {
        currentLanguage = "اردو";
        updateHeader();
    }
//== App Language Change Functions ==//

//== Notification Functions ==//
    private void loadNotifications() {
        notiVBox.getChildren().clear();
        User user = userService.getUserByEmail(savedEmail);
        Long userId = user.getUserId();
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        Collections.reverse(notifications);
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
    private void toggleNotiBox() {
        notiBox.setVisible(!notiBox.isVisible());
        categoryList.setVisible(false);
        languageList.setVisible(false);
        searchBox.setVisible(false);
        userList.setVisible(false);
        if (notiBox.isVisible()) {
            loadNotifications();
            notiCircle.setVisible(false);
            setNotiCircleStatus(false);
        }
    }
//== Notification Functions ==//

//== Check Due Dates ==//
    public void startDueDateChecker() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::checkDueDates, 0, 1, TimeUnit.MINUTES);
    }

    private void checkDueDates() {
        try {
            User user = userService.getUserByEmail(savedEmail);
            Long userId = user.getUserId();
            List<Reservation> reservations = reservationService.getReservationsDueSoon(userId);
            for (Reservation reservation : reservations) {
                notificationService.createReminderNotification(reservation);
            }
            if (!reservations.isEmpty()) {
                setNotiCircleStatus(true);
                updateHeader();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopDueDateChecker() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
//== Check Due Dates ==//

//** Book Reservation Functions **//
    public void chooseReserveCategory(Long bookId) throws Exception {
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

    public void chooseReserveLanguage(Long bookId) throws Exception {
        Long userId = getSavedUserId();
        if (userId == null) {
            throw new IllegalStateException("User is not logged in.");
        }

        Book book = bookService.getBookById(bookId);
        String language = book.getLanguage();

        try {
            reserveBook(userId, bookId);
            chooseLanguage(language);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseReserveSearch(Long bookId, String searchTerm) throws Exception {
        Long userId = getSavedUserId();
        if (userId == null) {
            throw new IllegalStateException("User is not logged in.");
        }

        try {
            reserveBook(userId, bookId);
            goToSearchPage(searchTerm);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
//        notiCircle.setVisible(true);
        Circle notiCircle = (Circle) primaryStage.getScene().lookup("#notiCircle");
        if (notiCircle != null) {
            notiCircle.setVisible(true);
        }
        setNotiCircleStatus(true);
        bookService.setBookAvailability(bookId, "Checked Out");

        // Update the user's book count
        user.setBookCount(user.getBookCount() + 1);
        userService.updateUser(user);
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
//** Book Reservation Functions **//

//== Page Navigation Functions ==//
    public FXMLLoader loadScene(String fxmlFile) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent component = loader.load();
        AnchorPane bodyBox = (AnchorPane) primaryStage.getScene().lookup("#bodyBox");
        bodyBox.getChildren().setAll(component);
        updateHeader();
        return loader;
    }

    @FXML
    private void goToMainPage() throws Exception {
        loadScene("/mainpage2.fxml");
    }

    @FXML
    private void chooseAuthor() throws Exception {
        List<Author> authors = authorService.getAllAuthors();
        FXMLLoader loader = loadScene("/authorsPage.fxml");
        authorsPageController controller = loader.getController();
        controller.setAuthors(authors);
    }

    public void chooseCategory(String category) throws Exception {
        // Fetch books without requiring authentication
        List<Book> books = bookService.getBooksByCategory(category);

        FXMLLoader loader = loadScene("/category.fxml");
        categoryPageController controller = loader.getController();
        controller.setCategoryTag(category);
        controller.clearBookLists();
        controller.getAvailabilityCheckBox().setSelected(false);
        controller.setBooks(books); // Ensure books are passed to UI
    }

    private void chooseLanguage(String language) throws Exception {
        List<Book> books = searchBooksByLanguage(language);

        FXMLLoader loader = loadScene("/language.fxml");
        languagePageController controller = loader.getController();
        controller.setLanguageTag(language);
        controller.clearBookLists();
        controller.getAvailabilityCheckBox().setSelected(false);
        controller.setBooks(books);
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
        chooseLanguage("English");
    }

    @FXML
    private void chooseFinnish() throws Exception {
        chooseLanguage("Finnish");
    }

    @FXML
    private void chooseSwedish() throws Exception {
        chooseLanguage("Swedish");
    }

    private void disablePanelVisibility() {
        AnchorPane categoryList = (AnchorPane) primaryStage.getScene().lookup("#categoryList");
        AnchorPane languageList = (AnchorPane) primaryStage.getScene().lookup("#languageList");
        AnchorPane searchBox = (AnchorPane) primaryStage.getScene().lookup("#searchBox");
        AnchorPane userList = (AnchorPane) primaryStage.getScene().lookup("#userList");
        AnchorPane notiBox = (AnchorPane) primaryStage.getScene().lookup("#notiBox");
        AnchorPane appLanguageBox = (AnchorPane) primaryStage.getScene().lookup("#appLanguageBox");

        if (categoryList != null) categoryList.setVisible(false);
        if (languageList != null) languageList.setVisible(false);
        if (searchBox != null) searchBox.setVisible(false);
        if (userList != null) userList.setVisible(false);
        if (notiBox != null) notiBox.setVisible(false);
        if (appLanguageBox != null) appLanguageBox.setVisible(false);
    }

    @FXML
    private void chooseCategory() {
        categoryList.setVisible(!categoryList.isVisible());
        languageList.setVisible(false);
        searchBox.setVisible(false);
        userList.setVisible(false);
        notiBox.setVisible(false);
        appLanguageBox.setVisible(false);
    }

    @FXML
    private void chooseLanguage() {
        languageList.setVisible(!languageList.isVisible());
        categoryList.setVisible(false);
        searchBox.setVisible(false);
        userList.setVisible(false);
        notiBox.setVisible(false);
        appLanguageBox.setVisible(false);
    }

    @FXML
    private void chooseSearch() {
        searchBox.setVisible(!searchBox.isVisible());
        categoryList.setVisible(false);
        languageList.setVisible(false);
        userList.setVisible(false);
        notiBox.setVisible(false);
        appLanguageBox.setVisible(false);
    }

    @FXML
    private void chooseUserProfile() {
        userList.setVisible(!userList.isVisible());
        categoryList.setVisible(false);
        languageList.setVisible(false);
        searchBox.setVisible(false);
        notiBox.setVisible(false);
        appLanguageBox.setVisible(false);
    }

    @FXML
    private void chooseAppLanguage() {
        appLanguageBox.setVisible(!appLanguageBox.isVisible());
        categoryList.setVisible(false);
        languageList.setVisible(false);
        searchBox.setVisible(false);
        userList.setVisible(false);
        notiBox.setVisible(false);
    }

    @FXML
    private void searchAction() throws Exception {
        String searchTerm = searchBar1.getText();
        goToSearchPage(searchTerm);
    }

    private void goToSearchPage(String searchTerm) throws Exception {
        List<Book> books = bookService.searchBooks(searchTerm);

        FXMLLoader loader = loadScene("/searchPage.fxml");
        searchPageController controller = loader.getController();
        controller.setSavedSearchTerm(searchTerm);
        controller.clearBookLists();
        controller.getAvailabilityCheckBox().setSelected(false);
        controller.setBooks(books);
    }

    public void showAuthorBooks(Author author) throws Exception {
        FXMLLoader loader = loadScene("/booksByAuthor.fxml");
        booksByAuthorController controller = loader.getController();
        controller.setSelectedAuthor(author); // Pass the selected author
        controller.loadBooksByAuthor(); // Load books for that author
    }

    @FXML
    private void chooseProfile() throws Exception {
        FXMLLoader loader = loadScene("/myProfile.fxml");
        myProfileController controller = loader.getController();
        controller.initializeProfilePage();
    }

    @FXML
    private void chooseBookings() throws Exception {
        Long userId = getSavedUserId();
        FXMLLoader loader = loadScene("/myBookings.fxml");
        myBookingController controller = loader.getController();
        controller.setBooksForUser(userId);
    }

    @FXML
    private void logout() throws Exception {
        // Clear user session data
        savedUsername = null;
        savedEmail = null;
        savedUserId = null; // Ensure user ID is null
        AuthManager.getInstance().setToken(null);
        stopDueDateChecker();

        // Always reload the main page after logout
        loadScene("/mainpage2.fxml");
    }

    @FXML
    private void switchToSignUp() throws Exception {
        loadScene("/signup.fxml");
    }

    @FXML
    private void switchToLogin() throws Exception {
        loadScene("/login.fxml");
    }
//== Page Navigation Functions ==//

//** Setters **//
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

    public void setSavedPhoneNumber(String savedPhoneNumber) {
        this.savedPhoneNumber = savedPhoneNumber;
    }

    public void setNotiCircleStatus(boolean notiCircleStatus) {
        this.notiCircleStatus = notiCircleStatus;
    }

    public void updateUserInfo(User user) {
        userService.updateUser(user);
    }

    public void setCurrentLanguage(String language) {
        currentLanguage = language;
    }
//** Setters **//

//** Getters **//
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

    public String getSavedPhoneNumber() {
        return savedPhoneNumber;
    }

    public String getUserPhone(String email){
        return userService.getUserPhone(email);
    }

    public Reservation getReservationByUserAndBook (Long userId, Long bookId) {
        return reservationService.getReservationByUserAndBook(userId, bookId);
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

    public String getCurrentLanguage() {
        return currentLanguage;
    }
//** Getters **//
}