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
import view.View;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    private static Locale locale;
    private static ResourceBundle bundle;
    private static String currentBodyBoxFXML = null;
    private static String currentCategory = null;
    private static String currentBookLanguage = null;
    private static Author currentAuthor = null;
    private static String currentSearchTerm = null;


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

    public void updateHeader() {
        disablePanelVisibility();
        AnchorPane loginBox = (AnchorPane) primaryStage.getScene().lookup("#loginBox");
        AnchorPane userProfileBox = (AnchorPane) primaryStage.getScene().lookup("#userProfileBox");
        Button appLanguage = (Button) primaryStage.getScene().lookup("#appLanguage");
        appLanguage.setText(currentLanguage);

        if (loginBox != null && userProfileBox != null) {
            if (AuthManager.getInstance().validateToken()) {
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

//== App Language Change Functions ==//
    @FXML
    private void chooseLanguageEnglish() throws Exception {
        currentLanguage = "English";
        setLanguageText(currentBodyBoxFXML);
    }

    @FXML
    private void chooseLanguageRussian() throws Exception {
        currentLanguage = "Русский";
        setLanguageText(currentBodyBoxFXML);
    }

    @FXML
    private void chooseLanguageUrdu() throws Exception {
        currentLanguage = "اردو";
        setLanguageText(currentBodyBoxFXML);
    }

    public void setLanguageText(String fxmlFile) {
        if (currentLanguage.equals("English")) {
            setLocale(new Locale("en", "US"));
        } else if (currentLanguage.equals("Русский")) {
            setLocale(new Locale("ru", "RUS"));
        } else if (currentLanguage.equals("اردو")) {
            setLocale(new Locale("ur", "Pak"));
        }
        setResourceBundle(ResourceBundle.getBundle("resource_bundle", getCurrentLocale()));

        // Reload the FXML to apply the new locale
        try {
            reloadFXML(fxmlFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateHeader();
    }

    public void reloadFXML(String fxmlFile) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainpage.fxml"));
        loader.setController(this);
        loader.setResources(getResourceBundle());
        Parent root = loader.load();
        loader.setController(this);
        primaryStage.getScene().setRoot(root);
        if (fxmlFile == null) {
            return;
        } else if (fxmlFile.equals("/category.fxml")) {
            chooseCategory(currentCategory);
        } else if (fxmlFile.equals("/language.fxml")) {
            chooseLanguage(currentBookLanguage);
        } else if (fxmlFile.equals("/authorsPage.fxml")) {
            chooseAuthor();
        } else if (fxmlFile.equals("/booksByAuthor.fxml")) {
            showAuthorBooks(currentAuthor);
        } else if (fxmlFile.equals("/myBookings.fxml")) {
            chooseBookings();
        } else if (fxmlFile.equals("/searchPage.fxml")) {
            goToSearchPage(currentSearchTerm);
        } else {
            loadScene(fxmlFile);
        }
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
                notiMessage.setText(notification.getMessage(currentLanguage));

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
            List<Reservation> reservations = reservationService.getReservationsDueSoon(userId, currentLanguage);
            for (Reservation reservation : reservations) {
                notificationService.createReminderNotification(reservation, currentLanguage);
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
        if (!AuthManager.getInstance().validateToken()) {
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
        notificationService.createNotificationForReservation(reservation.getReservationId(), currentLanguage);
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
        Reservation reservation = reservationService.getReservationById(reservationId, currentLanguage);
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
        currentBodyBoxFXML = fxmlFile;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        loader.setResources(getResourceBundle());
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
        System.out.println("Books fetched for category: " + category + " - " + books.size() + " books found.");

        FXMLLoader loader = loadScene("/category.fxml");
        categoryPageController controller = loader.getController();

        // Map category names to resource bundle keys
        String categoryKey;
        switch (category) {
            case "Fiction":
                categoryKey = "fiction.button";
                break;
            case "Non-Fiction":
                categoryKey = "nonFiction.button";
                break;
            case "History":
                categoryKey = "history.button";
                break;
            case "Science":
                categoryKey = "science.button";
                break;
            default:
                categoryKey = category; // Fallback to the original category if no match
        }

        String categoryTranslation = getResourceBundle().getString(categoryKey);
        controller.setCategoryTag(categoryTranslation);
        controller.clearBookLists();
        controller.getAvailabilityCheckBox().setSelected(false);
        controller.setBooks(books); // Ensure books are passed to UI
        System.out.println("Books set in categoryPageController: " + books.size() + " books.");
    }

    private void chooseLanguage(String language) throws Exception {
        // Fetch books by language
        List<Book> books = bookService.getBooksByLanguage(language);

        FXMLLoader loader = loadScene("/language.fxml");
        languagePageController controller = loader.getController();

        // Map language names to resource bundle keys
        String languageKey;
        switch (language) {
            case "English":
                languageKey = "english.button";
                break;
            case "Finnish":
                languageKey = "finnish.button";
                break;
            case "Swedish":
                languageKey = "swedish.button";
                break;
            default:
                languageKey = language; // Fallback to the original language if no match
        }

        String languageTranslation = getResourceBundle().getString(languageKey);
        controller.setLanguageTag(languageTranslation);
        controller.clearBookLists();
        controller.getAvailabilityCheckBox().setSelected(false);
        controller.setBooks(books); // Ensure books are passed to UI
    }

    @FXML
    private void chooseFiction() throws Exception {
        chooseCategory("Fiction");
        currentCategory = "Fiction";
    }

    @FXML
    private void chooseNonFiction() throws Exception {
        chooseCategory("Non-Fiction");
        currentCategory = "Non-Fiction";
    }

    @FXML
    private void chooseScience() throws Exception {
        chooseCategory("Science");
        currentCategory = "Science";
    }

    @FXML
    private void chooseHistory() throws Exception {
        chooseCategory("History");
        currentCategory = "History";
    }

    @FXML
    private void chooseEnglish() throws Exception {
        chooseLanguage("English");
        currentBookLanguage = "English";
    }

    @FXML
    private void chooseFinnish() throws Exception {
        chooseLanguage("Finnish");
        currentBookLanguage = "Finnish";
    }

    @FXML
    private void chooseSwedish() throws Exception {
        chooseLanguage("Swedish");
        currentBookLanguage = "Swedish";
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
        currentSearchTerm = searchTerm;
        goToSearchPage(searchTerm);
    }

    private void goToSearchPage(String searchTerm) throws Exception {
        List<Book> books = bookService.searchBooks(searchTerm, currentLanguage);

        FXMLLoader loader = loadScene("/searchPage.fxml");
        searchPageController controller = loader.getController();
        controller.setSavedSearchTerm(searchTerm);
        controller.clearBookLists();
        controller.getAvailabilityCheckBox().setSelected(false);
        controller.setBooks(books);
    }

    public void showAuthorBooks(Author author) throws Exception {
        FXMLLoader loader = loadScene("/booksByAuthor.fxml");
        currentAuthor = author;
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
        controller.setBooksForUser(userId, currentLanguage);
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

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
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
        return reservationService.getReservationByUserAndBook(userId, bookId, currentLanguage);
    }

    public String getUserNameByEmail(String email) {
        return userService.getUserByEmail(email).getUsername();
    }

    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    public List<Book> getBooksByCategory(String category) {
        if (!AuthManager.getInstance().validateToken()) {
            throw new SecurityException("Invalid token");
        }
        return bookService.getBooksByCategory(category);
    }

    public List<Book> searchBooksByAuthor(String authorFirstName, String authorLastName) {
        return authorService.getBooksByAuthor(authorFirstName, authorLastName, currentLanguage);
    }

    public List<Book> searchBooksByTitle(String title, String currentLanguage) {
        return bookService.getBooksByTitle(title, currentLanguage);
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
        return reservationService.getReservationsByUserId(userId, currentLanguage);
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public Locale getCurrentLocale() {
        if (this.locale == null) {
            this.locale = new Locale("en", "US");
        }
        return this.locale;
    }

    public ResourceBundle getResourceBundle() {
        if (this.bundle == null) {
            this.bundle = ResourceBundle.getBundle("resource_bundle", getCurrentLocale());
        }
        return this.bundle;
    }
//** Getters **//
}