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

public class languagePageController extends LibraryController {
    private List<Book> allBooks, availableBooks;

    @FXML private VBox bookVBox;
    @FXML private ScrollPane bookScrollPane;
    @FXML private CheckBox availabilityCheckBox;
    @FXML private AnchorPane noBooks, scrollBox;
    @FXML private Label languageTag;


    public void setLanguageTag(String language) {
        languageTag.setText(language);
    }

    public void setAllBooks(List<Book> allBooks) {
        this.allBooks = allBooks;
    }

    public void setAvailableBooks(List<Book> availableBooks) {
        this.availableBooks = availableBooks;
    }

    public CheckBox getAvailabilityCheckBox() {
        return availabilityCheckBox;
    }

    public void clearBookLists() {
        if (allBooks != null) {
            allBooks.clear();
        }
        if (availableBooks != null) {
            availableBooks.clear();
        }
    }

    @FXML
    public void chooseOnlyAvailable() {
        if (availabilityCheckBox.isSelected()) {
            availableBooks = allBooks.stream()
                    .filter(book -> "Available".equalsIgnoreCase(book.getAvailabilityStatus()))
                    .collect(Collectors.toList());
            setBooks(availableBooks);
        } else {
            setBooks(allBooks);
        }
    }

    public void setBooks(List<Book> books) {
        bookVBox.getChildren().clear();
        bookVBox.setSpacing(40);

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
                hBox = new HBox(40);
                bookVBox.getChildren().add(hBox);
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookBox.fxml"));
                loader.setResources(getResourceBundle());
                AnchorPane bookBox = loader.load();

                Label bookName = (Label) bookBox.lookup("#bookName");
                Label author = (Label) bookBox.lookup("#author");
                Label publicationDate = (Label) bookBox.lookup("#publicationDate");
                Label availability = (Label) bookBox.lookup("#availability");
                Label location = (Label) bookBox.lookup("#locationTag");
                Label bookId = (Label) bookBox.lookup("#bookId");
                Button reserveButton = (Button) bookBox.lookup("#reserveButton");
                ImageView bookCover = (ImageView) bookBox.lookup("#bookCover"); // New line for cover


                Book book = books.get(i);
                bookName.setText(book.getTitle());

                // Fetch authors for the book
                Set<Author> authorSet = getBookService().getAuthorsByBookId(book.getBookId());
                String authorsText = authorSet.stream()
                        .map(a -> a.getFirstName() + " " + a.getLastName())
                        .collect(Collectors.joining(", "));
                author.setText(authorsText.isEmpty() ? getResourceBundle().getString("unknownAuthor") : authorsText);

                publicationDate.setText(book.getPublicationDate() != null ? book.getPublicationDate().toString() : getResourceBundle().getString("unknown"));
                availability.setText("Available".equals(book.getAvailabilityStatus()) ? getResourceBundle().getString("available") : getResourceBundle().getString("checkedout"));
                location.setText(book.getLocation() != null ? book.getLocation() : getResourceBundle().getString("unknown"));
                bookId.setText(String.valueOf(book.getBookId()));

                // ** Load book cover **
                String imagePath = "/" + book.getCoverImage(); // Directly in resources
                Image image;
                try {
                    image = new Image(getClass().getResourceAsStream(imagePath));
                    if (image.isError()) { // Handle if image loading fails
                        throw new Exception("Image not found");
                    }
                } catch (Exception e) {
                    image = new Image(getClass().getResourceAsStream("/bookpicture.jpg")); // Fallback image
                }
                bookCover.setImage(image);

                // Get login status dynamically
                Long userId = getSavedUserId();
                boolean isLoggedIn = userId != null;

                if (!isLoggedIn) {
                    // If not logged in, show "Login to Reserve"
                    reserveButton.setText(getResourceBundle().getString("loginToReserve"));
                    reserveButton.setStyle("-fx-text-fill: grey;");
                    reserveButton.setDisable(true);
                } else {
                    // Get user role and book count
                    String userRole = getUserService().getUserRole(userId);
                    int userBookCount = getUserService().getUserBookCount(userId);

                    if ("teacher".equalsIgnoreCase(userRole)) {
                        reserveButton.setText(getResourceBundle().getString("reserve"));
                        reserveButton.setStyle("-fx-text-fill: green; -fx-font-size: 18px; -fx-font-weight: bold;");
                        reserveButton.setDisable(false);
                        availability.setStyle("-fx-text-fill: green;");
                    } else if ("student".equalsIgnoreCase(userRole) && userBookCount >= 5) {
                        reserveButton.setText(getResourceBundle().getString("limitReached"));
                        reserveButton.setStyle("-fx-text-fill: red; -fx-font-size: 13px; -fx-font-weight: bold;");
                        reserveButton.setDisable(true);
                    } else if ("Available".equalsIgnoreCase(book.getAvailabilityStatus())) {
                        reserveButton.setText(getResourceBundle().getString("reserve"));
                        reserveButton.setStyle("-fx-text-fill: green; -fx-font-size: 18px; -fx-font-weight: bold;");
                        reserveButton.setDisable(false);
                        availability.setStyle("-fx-text-fill: green;");
                    } else {
                        reserveButton.setText(getResourceBundle().getString("unavailable"));
                        reserveButton.setStyle("-fx-text-fill: red; -fx-font-size: 18px; -fx-font-weight: bold;");
                        reserveButton.setDisable(true);
                        availability.setStyle("-fx-text-fill: red;");
                    }
                }

                // Set up reservation action
                Long bookIdNo = book.getBookId();
                reserveButton.setOnAction(event -> {
                    if (isLoggedIn) {
                        try {
                            chooseReserveLanguage(bookIdNo);
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