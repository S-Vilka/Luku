package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.entity.Book;
import model.entity.Author;
import service.BookService;
import service.AuthorService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;

/**
 * Controller for displaying books by a selected author.
 */
public class BooksByAuthorController extends LibraryController {

    /** Service for book-related operations. */
    private final BookService bookService = new BookService();

    /** Service for author-related operations. */
    private final AuthorService authorService = new AuthorService();

    /** All books written by the selected author. */
    private List<Book> allBooks;

    /** List of available books by the selected author. */
    private List<Book> availableBooks;

    /** The currently selected author. */
    private Author selectedAuthor;

    /** Spacing between author boxes in the layout. */
    private static final int AUTHOR_SPACING = 40;

    /** Maximum number of books a student is allowed to reserve. */
    private static final int STUDENT_BOOK_LIMIT = 5;

    /** Container for displaying books in vertical layout. */
    @FXML private VBox bookVBox;

    /** ScrollPane containing the book list. */
    @FXML private ScrollPane bookScrollPane;

    /** Checkbox to filter only available books. */
    @FXML private CheckBox availabilityCheckBox;

    /** Pane shown when no books are available. */
    @FXML private AnchorPane noBooks;

    /** Pane shown when no books are available. */
    @FXML private AnchorPane scrollBox;

    /** Label displaying the selected author's name. */
    @FXML private Label selectedAuthorLabel;

    /**
     * Sets all books written by the selected author.
     *
     * @param booksList list of all books
     */
    public void setAllBooks(final List<Book> booksList) {
        this.allBooks = booksList;
    }

    /**
     * Sets the list of available books written by the selected author.
     *
     * @param availableBooksList list of available books
     */
    public void setAvailableBooks(final List<Book> availableBooksList) {
        this.availableBooks = availableBooksList;
    }

    /**
     * Sets the selected author and updates the author label.
     *
     * @param author the selected Author object
     */
    public void setSelectedAuthor(final Author author) {
        this.selectedAuthor = author;

        if (selectedAuthorLabel == null) {
            System.out.println(
                    "Error: selectedAuthorLabel is null. Check FXML binding."
            );
            return;
        }

        if (selectedAuthor != null) {
            selectedAuthorLabel.setText(
                    selectedAuthor.getFirstName() + " "
                            + selectedAuthor.getLastName()
            );
        } else {
            selectedAuthorLabel.setText(
                    getResourceBundle().getString("unknownAuthor")
            );
        }
    }

    /**
     * Loads and displays books written by the selected author.
     */
    public void loadBooksByAuthor() {
        if (selectedAuthor == null) {
            System.out.println("Error: No author selected.");
            return;
        }

        List<Book> books = bookService.getBooksByAuthorId(
                selectedAuthor.getAuthorId()
        );
        setAllBooks(books);
        setBooks(allBooks);
    }

    /**
     * Navigates back to the authors page.
     *
     * @throws Exception if FXML loading fails
     */
    @FXML
    public void backToAuthors() throws Exception {
        FXMLLoader loader = loadScene("/authorsPage.fxml");
        AuthorsPageController controller = loader.getController();
        controller.setAuthors(authorService.getAllAuthors());
    }

    /**
     * Filters books based on availability checkbox selection.
     */
    @FXML
    public void chooseOnlyAvailable() {
        if (availabilityCheckBox.isSelected()) {
            availableBooks = allBooks.stream()
                    .filter(book -> "Available".equalsIgnoreCase(
                            book.getAvailabilityStatus()
                    ))
                    .collect(Collectors.toList());
            setBooks(availableBooks);
        } else {
            setBooks(allBooks);
        }
    }

    /**
     * Attempts to reserve a book for the currently logged-in user.
     *
     * @param bookId the ID of the book to reserve
     * @throws Exception if reservation fails
     */
    public void chooseReserveAuthor(final Long bookId) throws Exception {
        Long userId = getSavedUserId();
        if (userId == null) {
            throw new IllegalStateException("User is not logged in.");
        }

        try {
            reserveBook(userId, bookId);
            showAuthorBooks(selectedAuthor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the view to display the given list of books.
     *
     * @param books list of books to display
     */
    public void setBooks(final List<Book> books) {
        bookVBox.getChildren().clear();
        bookVBox.setSpacing(AUTHOR_SPACING);
        if (books == null || books.isEmpty()) {
            scrollBox.setVisible(false);
            noBooks.setVisible(true);
            return;
        } else {
            scrollBox.setVisible(true);
            noBooks.setVisible(false);
        }
        if (!availabilityCheckBox.isSelected()) {
            setAllBooks(books);
        }
        HBox hBox = null;
        for (int i = 0; i < books.size(); i++) {
            if (i % 2 == 0) {
                hBox = new HBox(AUTHOR_SPACING);
                bookVBox.getChildren().add(hBox);
            }
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/bookBox.fxml")
                );
                loader.setResources(getResourceBundle());
                AnchorPane bookBox = loader.load();
                Label bookName = (Label) bookBox.lookup("#bookName");
                Label author = (Label) bookBox.lookup("#author");
                Label publicationDate =
                        (Label) bookBox.lookup("#publicationDate");
                Label availability =
                        (Label) bookBox.lookup("#availability");
                Label location = (Label) bookBox.lookup("#locationTag");
                Label bookIdLabel = (Label) bookBox.lookup("#bookId");
                Button reserveButton =
                        (Button) bookBox.lookup("#reserveButton");
                ImageView bookCover =
                        (ImageView) bookBox.lookup("#bookCover");
                Book book = books.get(i);
                bookName.setText(book.getTitle(getCurrentLanguage()));
                Set<Author> authorSet =
                        bookService.getAuthorsByBookId(book.getBookId());
                String authorsText = authorSet.stream()
                        .map(a -> a.getFirstName() + " " + a.getLastName())
                        .collect(Collectors.joining(", "));
                author.setText(authorsText.isEmpty()
                        ? getResourceBundle().getString("unknownAuthor")
                        : authorsText);
                publicationDate.setText(book.getPublicationDate() != null
                        ? book.getPublicationDate().toString()
                        : getResourceBundle().getString("unknown"));
                availability.setText("Available".equals(
                        book.getAvailabilityStatus())
                        ? getResourceBundle().getString("available")
                        : getResourceBundle().getString("checkedout"));
                location.setText(book.getLocation() != null
                        ? book.getLocation()
                        : getResourceBundle().getString("unknown"));
                bookIdLabel.setText(String.valueOf(book.getBookId()));
                Image image;
                String imagePath = book.getCoverImage();
                if (imagePath != null && !imagePath.isEmpty()) {
                    try {
                        image = new Image(getClass()
                            .getResourceAsStream("/" + imagePath));
                        if (image.isError() || image.getWidth() <= 0) {
                            throw new Exception("Image not found");
                        }
                    } catch (Exception e) {
                        image = new Image(
                                getClass().getResourceAsStream(
                                        "/bookpicture.jpg")
                        );
                    }
                } else {
                    image = new Image(getClass()
                            .getResourceAsStream("/bookpicture.jpg"));
                }
                bookCover.setImage(image);
                Long userId = getSavedUserId();
                boolean isLoggedIn = userId != null;
                if (!isLoggedIn) {
                    reserveButton.setText(
                            getResourceBundle().getString("loginToReserve")
                    );
                    reserveButton.setStyle("-fx-text-fill: grey;");
                    reserveButton.setDisable(true);
                } else {
                    String userRole =
                            getUserService().getUserRole(userId);
                    int userBookCount =
                            getUserService().getUserBookCount(userId);
                    if ("teacher".equalsIgnoreCase(userRole)) {
                        reserveButton.setText(getResourceBundle()
                                .getString("reserve"));
                        reserveButton.setStyle(
                                "-fx-text-fill: green; -fx-font-size: 18px; "
                                        + "-fx-font-weight: bold;"
                        );
                        reserveButton.setDisable(false);
                        availability.setStyle("-fx-text-fill: green;");
                    } else if ("student".equalsIgnoreCase(userRole)
                            && userBookCount >= STUDENT_BOOK_LIMIT) {
                        reserveButton.setText(
                                getResourceBundle().getString("limitReached")
                        );
                        reserveButton.setStyle(
                                "-fx-text-fill: red; -fx-font-size: 13px; "
                                        + "-fx-font-weight: bold;"
                        );
                        reserveButton.setDisable(true);
                    } else if ("Available".equalsIgnoreCase(
                            book.getAvailabilityStatus())) {
                        reserveButton.setText(
                                getResourceBundle().getString("reserve")
                        );
                        reserveButton.setStyle(
                                "-fx-text-fill: green; -fx-font-size: 18px; "
                                        + "-fx-font-weight: bold;"
                        );
                        reserveButton.setDisable(false);
                        availability.setStyle("-fx-text-fill: green;");
                    } else {
                        reserveButton.setText(
                                getResourceBundle().getString("unavailable")
                        );
                        reserveButton.setStyle(
                                "-fx-text-fill: red; -fx-font-size: 18px; "
                                        + "-fx-font-weight: bold;"
                        );
                        reserveButton.setDisable(true);
                        availability.setStyle("-fx-text-fill: red;");
                    }
                }
                final Long bookIdNo = book.getBookId();
                reserveButton.setOnAction(event -> {
                    if (isLoggedIn) {
                        try {
                            chooseReserveAuthor(bookIdNo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                hBox.getChildren().add(bookBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
