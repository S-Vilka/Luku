package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.entity.Author;
import model.entity.Book;
import model.entity.Notification;
import model.entity.Reservation;
import model.entity.User;
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
    /**
     * This class serves as the controller for the library application, handling
     * user interactions, page navigation, and service management.
     */
    private static View view;
    /** Stage for the primary application window. */
    private static Stage primaryStage;
    /** Page Navigation Variables. */
    private static UserService userService;
    /** Service Variables. */
    private static BookService bookService;
    /** Service Variables. */
    private static ReservationService reservationService;
    /** Service Variables. */
    private static NotificationService notificationService;
    /** Service Variables. */
    private static AuthorService authorService;
    /** Service Variables. */
    private static String savedUsername;
    /** Service Variables. */
    private static String savedEmail;
    /** Service Variables. */
    private static Long savedUserId;
    /** Service Variables. */
    private static String savedPhoneNumber;
    /** Service Variables. */
    private static ScheduledExecutorService scheduler;
    /** Service Variables. */
    private static boolean notiCircleStatus;
    /** Service Variables. */
    private static String currentLanguage = "English";
    /** Service Variables. */
    private static Locale locale;
    /** Service Variables. */
    private static ResourceBundle bundle;
    /** Service Variables. */
    private static String currentBodyBoxFXML = null;
    /** Service Variables. */
    private static String currentCategory = null;
    /** Service Variables. */
    private static String currentBookLanguage = null;
    /** Service Variables. */
    private static Author currentAuthor = null;
    /** Service Variables. */
    private static String currentSearchTerm = null;
    /** Service Variables. */
    private static final int MAX_BOOKS_FOR_STUDENTS = 5;
    /** Service Variables. */
    private static final int EXTEND_RESERVATION_DAYS = 7;
    /** Service Variables. */
    @FXML private TextField email;
    /** Service Variables. */
    @FXML private TextField usernameField;
    /** Service Variables. */
    @FXML private TextField emailField;
    /** Service Variables. */
    @FXML private TextField teacherID;
    /** Service Variables. */
    @FXML private TextField searchBar1;
    /** Service Variables. */
    @FXML private PasswordField password;
    /** Service Variables. */
    @FXML private PasswordField passwordField;
    /** Service Variables. */
    @FXML private PasswordField repeatPassword;
    /** Service Variables. */
    @FXML private Button searchButton21;
    /** Service Variables. */
    @FXML private Button logoutButton;
    /** Service Variables. */
    @FXML private Button myBookingsButton;
    /** Service Variables. */
    @FXML private Button enterBurron;
    /** Service Variables. */
    @FXML private Button loginButtonTop;
    /** Service Variables. */
    @FXML private Button categoryButton;
    /** Service Variables. */
    @FXML private Button languageButton;
    /** Service Variables. */
    @FXML private Button authorButton;
    /** Service Variables. */
    @FXML private Button searchButton;
    /** Service Variables. */
    @FXML private Button loginButton;
    /** Service Variables. */
    @FXML private Button signupButton;
    /** Service Variables. */
    @FXML private Button userProfile;
    /** Service Variables. */
    @FXML private Button fictionButton;
    /** Service Variables. */
    @FXML private Button nonFictionButton;
    /** Service Variables. */
    @FXML private Button scienceButton;
    /** Service Variables. */
    @FXML private Button historyButton;
    /** Service Variables. */
    @FXML private Button englishButton;
    /** Service Variables. */
    @FXML private Button finnishButton;
    /** Service Variables. */
    @FXML private Button swedishButton;
    /** Service Variables. */
    @FXML private Button searchButton2;
    /** Service Variables. */
    @FXML private Button reserveButton;
    /** Service Variables. */
    @FXML private Button extendButton;
    /** Service Variables. */
    @FXML private Button returnButton;
    /** Service Variables. */
    @FXML private Button appLanguage;
    /** Service Variables. */
    @FXML private Button languageEnglish;
    /** Service Variables. */
    @FXML private Button languageRussian;
    /** Service Variables. */
    @FXML private Button languageUrdu;
    /** Service Variables. */
    @FXML private Button profileButton;
    /** Service Variables. */
    @FXML private Label locationTag;
    /** Service Variables. */
    @FXML private Label wrongLogIn;
    /** Service Variables. */
    @FXML private Label bookName;
    /** Service Variables. */
    @FXML private Label author;
    /** Service Variables. */
    @FXML private Label publicationDate;
    /** Service Variables. */
    @FXML private Label availability;
    /** Service Variables. */
    @FXML private Label borrowDate;
    /** Service Variables. */
    @FXML private Label dueDate;
    /** Service Variables. */
    @FXML private Label bookId;
    /** Service Variables. */
    @FXML private Label slogan;
    /** Service Variables. */
    @FXML private ImageView noti;
    /** Service Variables. */
    @FXML private ImageView languageBall;
    /** Service Variables. */
    @FXML private Circle notiCircle;
    /** Service Variables. */
    @FXML private VBox notiVBox;
    /** Service Variables. */
    @FXML private AnchorPane searchBox;
    /** Service Variables. */
    @FXML private AnchorPane categoryList;
    /** Service Variables. */
    @FXML private AnchorPane languageList;
    /** Service Variables. */
    @FXML private AnchorPane userList;
    /** Service Variables. */
    @FXML private AnchorPane bookBox;
    /** Service Variables. */
    @FXML private AnchorPane userProfileBox;
    /** Service Variables. */
    @FXML private AnchorPane loginBox;
    /** Service Variables. */
    @FXML private AnchorPane notiBox;
    /** Service Variables. */
    @FXML private AnchorPane bodyBox;
    /** Service Variables. */
    @FXML private AnchorPane appLanguageBox;
    /** Service Variables. */
    @FXML private ImageView lukulogo;

    /**
     * Constructor for LibraryController.
     * Initializes the services used in the application.
     */
    public LibraryController() {
        this.userService = new UserService();
        this.bookService = new BookService();
        this.reservationService = new ReservationService();
        this.notificationService = new NotificationService();
        this.authorService = new AuthorService();
    }

    /**
     * Initializes the controller and sets the default language.
     */
    public void updateHeader() {
        disablePanelVisibility();
        AnchorPane loginBox1 = (AnchorPane) primaryStage
                .getScene().lookup("#loginBox");
        AnchorPane userProfileBox1 = (AnchorPane) primaryStage
                .getScene().lookup("#userProfileBox");
        Button appLanguage1 = (Button) primaryStage
                .getScene().lookup("#appLanguage");
        appLanguage1.setText(currentLanguage);

        if (loginBox1 != null && userProfileBox1 != null) {
            if (AuthManager.getInstance().validateToken()) {
                loginBox1.setVisible(false);
                userProfileBox1.setVisible(true);
                userProfile = (Button) primaryStage
                        .getScene().lookup("#userProfile");
                userProfile.setText(savedUsername);
                Circle notiCircle1 = (Circle) primaryStage
                        .getScene().lookup("#notiCircle");
                notiCircle1.setVisible(notiCircleStatus);
            } else {
                loginBox1.setVisible(true);
                userProfileBox1.setVisible(false);
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

    /**
     * Sets the language text based on the selected language.
     *
     * @param fxmlFile The FXML file to reload.
     */
    public void setLanguageText(final String fxmlFile) {
        if (currentLanguage.equals("English")) {
            setLocale(new Locale("en", "US"));
        } else if (currentLanguage.equals("Русский")) {
            setLocale(new Locale("ru", "RUS"));
        } else if (currentLanguage.equals("اردو")) {
            setLocale(new Locale("ur", "Pak"));
        }
        setResourceBundle(ResourceBundle
                .getBundle("resource_bundle", getCurrentLocale()));

        // Reload the FXML to apply the new locale
        try {
            reloadFXML(fxmlFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateHeader();
    }

    /**
     * Reloads the FXML file and sets the controller.
     *
     * @param fxmlFile The FXML file to reload.
     * @throws Exception If an error occurs during loading.
     */
    public void reloadFXML(final String fxmlFile) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/mainpage.fxml"));
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
        List<Notification> notifications = notificationService
                .getNotificationsByUserId(userId);
        Collections.reverse(notifications);
        for (Notification notification : notifications) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/notiBox.fxml"));
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
    /**
     * Starts a scheduled task to check for due dates every minute.
     */
    public void startDueDateChecker() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::checkDueDates,
                0, 1, TimeUnit.MINUTES);
    }

    private void checkDueDates() {
        try {
            User user = userService.getUserByEmail(savedEmail);
            Long userId = user.getUserId();
            List<Reservation> reservations = reservationService
                    .getReservationsDueSoon(userId);
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

    /**
     * Stops the scheduled task for checking due dates.
     */
    public void stopDueDateChecker() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
//== Check Due Dates ==//

//** Book Reservation Functions **//
    /**
     * Chooses a book to reserve based on the selected category.
     *
     * @param bookId1 The ID of the book to reserve.
     * @throws Exception If an error occurs during reservation.
     */
    public void chooseReserveCategory(final Long bookId1) throws Exception {
        Long userId = getSavedUserId();
        if (userId == null) {
            throw new IllegalStateException("User is not logged in.");
        }

        Book book = bookService.getBookById(bookId1);
        String category = book.getCategory();

        try {
            reserveBook(userId, bookId1);
            chooseCategory(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Chooses a book to reserve based on the selected language.
     *
     * @param bookId1 The ID of the book to reserve.
     * @throws Exception If an error occurs during reservation.
     */
    public void chooseReserveLanguage(final Long bookId1) throws Exception {
        Long userId = getSavedUserId();
        if (userId == null) {
            throw new IllegalStateException("User is not logged in.");
        }

        Book book = bookService.getBookById(bookId1);
        String language = book.getLanguage();

        try {
            reserveBook(userId, bookId1);
            chooseLanguage(language);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Chooses a book to reserve based on the search term.
     *
     * @param bookId1 The ID of the book to reserve.
     * @param searchTerm The search term used for searching books.
     * @throws Exception If an error occurs during reservation.
     */
    public void chooseReserveSearch(final Long bookId1,
                                    final String searchTerm) throws Exception {
        Long userId = getSavedUserId();
        if (userId == null) {
            throw new IllegalStateException("User is not logged in.");
        }

        try {
            reserveBook(userId, bookId1);
            goToSearchPage(searchTerm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reserves a book for the user.
     *
     * @param userId The ID of the user reserving the book.
     * @param bookId1 The ID of the book to reserve.
     */
    public void reserveBook(final Long userId, final Long bookId1) {
        if (!AuthManager.getInstance().validateToken()) {
            throw new SecurityException("Invalid token");
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        if ("student".equalsIgnoreCase(user.getRole()) && user
                .getBookCount() >= MAX_BOOKS_FOR_STUDENTS) {
            throw new IllegalArgumentException(
                    "Students cannot reserve more than 5 books.");
        }

        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setBookId(bookId1);

        reservationService.createReservation(reservation);
        notificationService.createNotificationForReservation(
                reservation.getReservationId());
        Circle notiCircle1 = (Circle) primaryStage
                .getScene().lookup("#notiCircle");
        if (notiCircle1 != null) {
            notiCircle1.setVisible(true);
        }
        setNotiCircleStatus(true);
        bookService.setBookAvailability(bookId1, "Checked Out");

        // Update the user's book count
        user.setBookCount(user.getBookCount() + 1);
        userService.updateUser(user);
    }

    /**
     * Returns a book that the user has reserved.
     *
     * @param reservationId The ID of the reservation to return.
     */
    public void extendReservation(final Long reservationId) {
        Reservation reservation = reservationService
                .getReservationById(reservationId);
        if (reservation != null) {
            LocalDateTime newDueDate = reservation
                    .getDueDate().plusDays(EXTEND_RESERVATION_DAYS);
            reservation.setDueDate(newDueDate);
            reservationService.updateReservation(reservation);
        } else {
            throw new IllegalArgumentException(
                    "Reservation with ID " + reservationId + " not found.");
        }
    }
//** Book Reservation Functions **//

//== Page Navigation Functions ==//
    /**
     * Loads the specified FXML file and sets it as the current scene.
     *
     * @param fxmlFile The FXML file to load.
     * @return The FXMLLoader used to load the FXML file.
     * @throws Exception If an error occurs during loading.
     */
    public FXMLLoader loadScene(final String fxmlFile) throws Exception {
        currentBodyBoxFXML = fxmlFile;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        loader.setResources(getResourceBundle());
        Parent component = loader.load();
        AnchorPane bodyBox1 = (AnchorPane) primaryStage
                .getScene().lookup("#bodyBox");
        bodyBox1.getChildren().setAll(component);
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
        AuthorsPageController controller = loader.getController();
        controller.setAuthors(authors);
    }

    /**
     * Chooses a category and loads the corresponding books.
     *
     * @param category The category to choose.
     * @throws Exception If an error occurs during loading.
     */
    public void chooseCategory(final String category) throws Exception {
        // Fetch books without requiring authentication
        List<Book> books = bookService.getBooksByCategory(category);
        System.out.println("Books fetched for category: "
                + category + " - " + books.size() + " books found.");

        FXMLLoader loader = loadScene("/category.fxml");
        CategoryPageController controller = loader.getController();

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
                categoryKey = category;
        }

        String categoryTranslation = getResourceBundle().getString(categoryKey);
        controller.setCategoryTag(categoryTranslation);
        controller.clearBookLists();
        controller.getAvailabilityCheckBox().setSelected(false);
        controller.setBooks(books); // Ensure books are passed to UI
        System.out.println("Books set in CategoryPageController: "
                + books.size() + " books.");
    }

    private void chooseLanguage(final String language) throws Exception {
        // Fetch books by language
        List<Book> books = bookService.getBooksByLanguage(language);

        FXMLLoader loader = loadScene("/language.fxml");
        LanguagePageController controller = loader.getController();

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
                languageKey = language;
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
        AnchorPane categoryList1 = (AnchorPane) primaryStage
                .getScene().lookup("#categoryList");
        AnchorPane languageList1 = (AnchorPane) primaryStage
                .getScene().lookup("#languageList");
        AnchorPane searchBox1 = (AnchorPane) primaryStage
                .getScene().lookup("#searchBox");
        AnchorPane userList1 = (AnchorPane) primaryStage
                .getScene().lookup("#userList");
        AnchorPane notiBox1 = (AnchorPane) primaryStage
                .getScene().lookup("#notiBox");
        AnchorPane appLanguageBox1 = (AnchorPane) primaryStage
                .getScene().lookup("#appLanguageBox");

        if (categoryList1 != null) {
            categoryList1.setVisible(false);
        }
        if (languageList1 != null) {
            languageList1.setVisible(false);
        }
        if (searchBox1 != null) {
            searchBox1.setVisible(false);
        }
        if (userList1 != null) {
            userList1.setVisible(false);
        }
        if (notiBox1 != null) {
            notiBox1.setVisible(false);
        }
        if (appLanguageBox1 != null) {
            appLanguageBox1.setVisible(false);
        }
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

    private void goToSearchPage(final String searchTerm) throws Exception {
        List<Book> books = bookService.searchBooks(searchTerm, currentLanguage);

        FXMLLoader loader = loadScene("/searchPage.fxml");
        SearchPageController controller = loader.getController();
        controller.setSavedSearchTerm(searchTerm);
        controller.clearBookLists();
        controller.getAvailabilityCheckBox().setSelected(false);
        controller.setBooks(books);
    }

    /**
     * Loads the books by the selected author.
     *
     * @param author1 The author whose books to load.
     * @throws Exception If an error occurs during loading.
     */
    public void showAuthorBooks(final Author author1) throws Exception {
        FXMLLoader loader = loadScene("/booksByAuthor.fxml");
        currentAuthor = author1;
        BooksByAuthorController controller = loader.getController();
        controller.setSelectedAuthor(author1); // Pass the selected author
        controller.loadBooksByAuthor(); // Load books for that author
    }

    @FXML
    private void chooseProfile() throws Exception {
        FXMLLoader loader = loadScene("/myProfile.fxml");
        MyProfileController controller = loader.getController();
        controller.initializeProfilePage();
    }

    @FXML
    private void chooseBookings() throws Exception {
        Long userId = getSavedUserId();
        FXMLLoader loader = loadScene("/myBookings.fxml");
        MyBookingController controller = loader.getController();
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
    /**
     * Sets the main application view.
     *
     * @param view1 The main application view.
     */
    public void setMainApp(final View view1) {
        this.view = view1;
    }

    /**
     * Sets the primary stage for the application.
     *
     * @param primaryStage1 The primary stage.
     */
    public void setPrimaryStage(final Stage primaryStage1) {
        this.primaryStage = primaryStage1;
    }

    /**
     * Sets the saved username.
     *
     * @param savedUsername1 The saved username.
     */
    public void setSavedUsername(final String savedUsername1) {
        this.savedUsername = savedUsername1;
    }

    /**
     * Sets the saved email.
     *
     * @param savedEmail1 The saved email.
     */
    public void setSavedEmail(final String savedEmail1) {
        this.savedEmail = savedEmail1;
    }

    /**
     * Sets the saved user ID.
     *
     * @param savedUserId1 The saved user ID.
     */
    public void setSavedUserId(final Long savedUserId1) {
        this.savedUserId = savedUserId1;
    }

    /**
     * Sets the saved phone number.
     *
     * @param savedPhoneNumber1 The saved phone number.
     */
    public void setSavedPhoneNumber(final String savedPhoneNumber1) {
        this.savedPhoneNumber = savedPhoneNumber1;
    }

    /**
     * Sets the notification circle status.
     *
     * @param notiCircleStatus1 The notification circle status.
     */
    public void setNotiCircleStatus(final boolean notiCircleStatus1) {
        this.notiCircleStatus = notiCircleStatus1;
    }

    /**
     * Sets the saved language.
     * @param locale1 The saved language.
     */
    public void setLocale(final Locale locale1) {
        this.locale = locale1;
    }

    /**
     * Sets the resource bundle for localization.
     *
     * @param resourceBundle1 The resource bundle.
     */
    public void setResourceBundle(final ResourceBundle resourceBundle1) {
        this.bundle = resourceBundle1;
    }
//** Setters **//

//** Getters **//
    /**
     * Gets the user service.
     *
     * @return The user service.
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * Gets the reservation service.
     *
     * @return The reservation service.
     */
    public ReservationService getReservationService() {
        return reservationService;
    }

    /**
     * Gets the author service.
     *
     * @return The author service.
     */
    public AuthorService getAuthorService() {
        return authorService;
    }

    /**
     * Gets the book service.
     *
     * @return The book service.
     */
    public BookService getBookService() {
        return bookService;
    }

    /**
     * Gets the notification service.
     *
     * @return The notification service.
     */
    public NotificationService getNotificationService() {
        return notificationService;
    }

    /**
     * Gets the main application view.
     *
     * @return The main application view.
     */
    public View getView() {
        return view;
    }

    /**
     * Gets the primary stage of the application.
     *
     * @return The primary stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Gets the saved username.
     *
     * @return The saved username.
     */
    public String getSavedUsername() {
        return savedUsername;
    }

    /**
     * Gets the saved email.
     *
     * @return The saved email.
     */
    public String getSavedEmail() {
        return savedEmail;
    }

    /**
     * Gets the saved user ID.
     *
     * @return The saved user ID.
     */
    public Long getSavedUserId() {
        return savedUserId;
    }

    /**
     * Gets the saved phone number.
     *
     * @return The saved phone number.
     */
    public String getSavedPhoneNumber() {
        return savedPhoneNumber;
    }

    /**
     * Gets the notification circle status.
     * @param email1 The email of the user.
     * @return The notification circle status.
     */
    public String getUserPhone(final String email1) {
        return userService.getUserPhone(email1);
    }

    /**
     * Gets the notification circle status.
     * @param userId1 The ID of the user.
     * @param bookId1 The ID of the book.
     * @return The notification circle status.
     */
    public Reservation getReservationByUserAndBook(final Long userId1,
                                                   final Long bookId1) {
        return reservationService.getReservationByUserAndBook(userId1, bookId1);
    }

    /**
     * Gets the notification circle status.
     * @param email1 The email of the user.
     * @return The notification circle status.
     */
    public String getUserNameByEmail(final String email1) {
        return userService.getUserByEmail(email1).getUsername();
    }

    /**
     * Gets the notification circle status.
     * @param email1 The email of the user.
     * @return The notification circle status.
     */
    public User getUserByEmail(final String email1) {
        return userService.getUserByEmail(email1);
    }

    /**
     * Gets the notification circle status.
     * @param userId1 The ID of the user.
     * @return The notification circle status.
     */
    public List<Reservation> getMyBookings(final Long userId1) {
        return reservationService.getReservationsByUserId(userId1);
    }

    /**
     * Gets the current language.
     * @return The current language.
     */
    public String getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * Gets the current locale.
     * @return The current locale.
     */
    public Locale getCurrentLocale() {
        if (this.locale == null) {
            this.locale = new Locale("en", "US");
        }
        return this.locale;
    }

    /**
     * Gets the resource bundle for localization.
     * @return The resource bundle.
     */
    public ResourceBundle getResourceBundle() {
        if (this.bundle == null) {
            this.bundle = ResourceBundle
                    .getBundle("resource_bundle", getCurrentLocale());
        }
        return this.bundle;
    }
//** Getters **//
}
