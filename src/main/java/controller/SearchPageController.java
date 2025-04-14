package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.entity.Book;
import model.entity.Author;
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
 * Controller for the search page view that displays search results.
 */
public class SearchPageController extends LibraryController {

    /** List of all matching books. */
    private List<Book> allBooks;

    /** List of available matching books. */
    private List<Book> availableBooks;

    /** Term used in search bar. */
    private String savedSearchTerm;

    /** Spacing between book entries in the layout. */
    private static final int BOOK_SPACING = 40;

    /** Maximum number of books a student is allowed to reserve. */
    private static final int STUDENT_BOOK_LIMIT = 5;

    /** Container for book boxes. */
    @FXML private VBox bookVBox;

    /** Scroll pane containing book results. */
    @FXML private ScrollPane bookScrollPane;

    /** Checkbox to filter only available books. */
    @FXML private CheckBox availabilityCheckBox;

    /** AnchorPane shown when no results are found. */
    @FXML private AnchorPane noBooks;

    /** AnchorPane shown when results exist. */
    @FXML private AnchorPane scrollBox;

    /**
     * Sets the list of all books.
     *
     * @param allBooksList the full list of books to be shown
     */
    public final void setAllBooks(final List<Book> allBooksList) {
        this.allBooks = allBooksList;
    }

    /**
     * Sets the list of available books.
     *
     * @param availableBooksList list of books with "Available" status
     */
    public final void setAvailableBooks(final List<Book> availableBooksList) {
        this.availableBooks = availableBooksList;
    }

    /**
     * Stores the search term entered by the user.
     *
     * @param searchTerm search string
     */
    public final void setSavedSearchTerm(final String searchTerm) {
        this.savedSearchTerm = searchTerm;
    }

    /**
     * Returns the availability checkbox.
     *
     * @return checkbox instance
     */
    public final CheckBox getAvailabilityCheckBox() {
        return availabilityCheckBox;
    }

    /**
     * Clears book lists.
     */
    public final void clearBookLists() {
        if (allBooks != null) {
            allBooks.clear();
        }
        if (availableBooks != null) {
            availableBooks.clear();
        }
    }

    /**
     * Filters books to only available ones based on checkbox state.
     */
    @FXML
    public final void chooseOnlyAvailable() {
        if (availabilityCheckBox.isSelected()) {
            availableBooks = allBooks.stream()
                    .filter(book -> "Available".equalsIgnoreCase(
                            book.getAvailabilityStatus()))
                    .collect(Collectors.toList());
            setBooks(availableBooks);
        } else {
            setBooks(allBooks);
        }
    }

    /**
     * Renders a list of books visually.
     *
     * @param books list of books to render
     */
    public final void setBooks(final List<Book> books) {
        bookVBox.getChildren().clear();
        bookVBox.setSpacing(BOOK_SPACING);

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
                hBox = new HBox(BOOK_SPACING);
                bookVBox.getChildren().add(hBox);
            }
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/bookBox.fxml"));
                loader.setResources(getResourceBundle());
                AnchorPane bookBox = loader.load();

                Label bookName = (Label) bookBox.lookup("#bookName");
                Label author = (Label) bookBox.lookup("#author");
                Label publicationDate = (Label) bookBox.lookup(
                        "#publicationDate");
                Label availability = (Label) bookBox.lookup("#availability");
                Label location = (Label) bookBox.lookup("#locationTag");
                Label bookId = (Label) bookBox.lookup("#bookId");
                Button reserveButton = (Button) bookBox.lookup(
                        "#reserveButton");
                ImageView bookCover = (ImageView) bookBox.lookup(
                        "#bookCover");

                Book book = books.get(i);
                bookName.setText(book.getTitle(getCurrentLanguage()));

                Set<Author> authorSet = getBookService()
                        .getAuthorsByBookId(book.getBookId());
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
                bookId.setText(String.valueOf(book.getBookId()));

                String imagePath = book.getCoverImage();
                Image image;
                if (imagePath != null && !imagePath.isEmpty()) {
                    try {
                        image = new Image(getClass().getResourceAsStream(
                                "/" + imagePath));
                        if (image.isError() || image.getWidth() <= 0) {
                            throw new Exception("Image not found: "
                                    + imagePath);
                        }
                    } catch (Exception e) {
                        System.out.println("Falling back to default image: "
                                + "bookpicture.jpg");
                        image = new Image(getClass().getResourceAsStream(
                                "/bookpicture.jpg"));
                    }
                } else {
                    System.out.println("No cover image found for book. "
                            + "Using default.");
                    image = new Image(getClass().getResourceAsStream(
                            "/bookpicture.jpg"));
                }

                bookCover.setImage(image);

                Long userId = getSavedUserId();
                boolean isLoggedIn = userId != null;

                if (!isLoggedIn) {
                    reserveButton.setText(getResourceBundle().getString(
                            "loginToReserve"));
                    reserveButton.setStyle("-fx-text-fill: grey;");
                    reserveButton.setDisable(true);
                } else {
                    String userRole = getUserService().getUserRole(userId);
                    int userBookCount = getUserService()
                            .getUserBookCount(userId);

                    if ("teacher".equalsIgnoreCase(userRole)) {
                        reserveButton.setText(getResourceBundle()
                                .getString("reserve"));
                        reserveButton.setStyle("-fx-text-fill: green; "
                                + "-fx-font-size: 18px; "
                                + "-fx-font-weight: bold;");
                        reserveButton.setDisable(false);
                        availability.setStyle("-fx-text-fill: green;");
                    } else if ("student".equalsIgnoreCase(userRole)
                            && userBookCount >= STUDENT_BOOK_LIMIT) {
                        reserveButton.setText(getResourceBundle()
                                .getString("limitReached"));
                        reserveButton.setStyle("-fx-text-fill: red; "
                                + "-fx-font-size: 13px; "
                                + "-fx-font-weight: bold;");
                        reserveButton.setDisable(true);
                    } else if ("Available".equalsIgnoreCase(
                            book.getAvailabilityStatus())) {
                        reserveButton.setText(getResourceBundle()
                                .getString("reserve"));
                        reserveButton.setStyle("-fx-text-fill: green; "
                                + "-fx-font-size: 18px; "
                                + "-fx-font-weight: bold;");
                        reserveButton.setDisable(false);
                        availability.setStyle("-fx-text-fill: green;");
                    } else {
                        reserveButton.setText(getResourceBundle()
                                .getString("unavailable"));
                        reserveButton.setStyle("-fx-text-fill: red; "
                                + "-fx-font-size: 18px; "
                                + "-fx-font-weight: bold;");
                        reserveButton.setDisable(true);
                        availability.setStyle("-fx-text-fill: red;");
                    }
                }

                Long bookIdNo = book.getBookId();
                reserveButton.setOnAction(event -> {
                    if (isLoggedIn) {
                        try {
                            chooseReserveSearch(bookIdNo, savedSearchTerm);
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