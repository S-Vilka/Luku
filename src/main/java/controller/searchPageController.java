package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.entity.Book;
import model.entity.Author;
import service.BookService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;

public class searchPageController extends LibraryController {
    private List<Book> allBooks, availableBooks;
    private String savedSearchTerm;

    @FXML
    private VBox bookVBox;
    @FXML
    private ScrollPane bookScrollPane;
    @FXML
    private CheckBox availabilityCheckBox;
    @FXML
    private AnchorPane noBooks, scrollBox;

    public void setAllBooks(List<Book> allBooks) {
        this.allBooks = allBooks;
    }

    public void setAvailableBooks(List<Book> availableBooks) {
        this.availableBooks = availableBooks;
    }

    public void setSavedSearchTerm(String searchTerm) {
        this.savedSearchTerm = searchTerm;
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
                AnchorPane bookBox = loader.load();

                Label bookName = (Label) bookBox.lookup("#bookName");
                Label author = (Label) bookBox.lookup("#author");
                Label publicationDate = (Label) bookBox.lookup("#publicationDate");
                Label availability = (Label) bookBox.lookup("#availability");
                Label location = (Label) bookBox.lookup("#locationTag");
                Label bookId = (Label) bookBox.lookup("#bookId");
                Button reserveButton = (Button) bookBox.lookup("#reserveButton");
                ImageView bookCover = (ImageView) bookBox.lookup("#bookCover"); // Book cover support

                Book book = books.get(i);
                bookName.setText(book.getTitle());

                // Fetch authors for the book
                Set<Author> authorSet = getBookService().getAuthorsByBookId(book.getBookId());
                String authorsText = authorSet.stream()
                        .map(a -> a.getFirstName() + " " + a.getLastName())
                        .collect(Collectors.joining(", "));
                author.setText(authorsText.isEmpty() ? "Unknown Author" : authorsText);

                publicationDate.setText(book.getPublicationDate() != null ? book.getPublicationDate().toString() : "Unknown");
                availability.setText(book.getAvailabilityStatus() != null ? book.getAvailabilityStatus() : "Unknown");
                location.setText(book.getLocation() != null ? book.getLocation() : "Unknown");
                bookId.setText(String.valueOf(book.getBookId()));

                // ** Load book cover **
                String imagePath = book.getCoverImage(); // Get image filename from DB
                Image image;

                if (imagePath != null && !imagePath.isEmpty()) {
                    try {
                        // Try to load the image
                        image = new Image(getClass().getResourceAsStream("/" + imagePath));

                        // Check if the image failed to load
                        if (image.isError() || image.getWidth() <= 0) {
                            throw new Exception("Image not found: " + imagePath);
                        }
                    } catch (Exception e) {
                        System.out.println("Falling back to default image: bookpicture.jpg");
                        image = new Image(getClass().getResourceAsStream("/bookpicture.jpg"));
                    }
                } else {
                    System.out.println("No cover image found for book. Using default.");
                    image = new Image(getClass().getResourceAsStream("/bookpicture.jpg"));
                }

                bookCover.setImage(image);

                // ** Handle Reserve Button Based on User Role **
                Long userId = getSavedUserId();
                boolean isLoggedIn = userId != null;

                if (!isLoggedIn) {
                    reserveButton.setText("Login to Reserve");
                    reserveButton.setStyle("-fx-text-fill: grey;");
                    reserveButton.setDisable(true);
                } else {
                    String userRole = getUserService().getUserRole(userId);
                    int userBookCount = getUserService().getUserBookCount(userId);

                    if ("teacher".equalsIgnoreCase(userRole)) {
                        reserveButton.setText("Reserve");
                        reserveButton.setStyle("-fx-text-fill: green; -fx-font-size: 18px; -fx-font-weight: bold;");
                        reserveButton.setDisable(false);
                        availability.setStyle("-fx-text-fill: green;");
                    } else if ("student".equalsIgnoreCase(userRole) && userBookCount >= 5) {
                        reserveButton.setText("Limit Reached (5 Books)");
                        reserveButton.setStyle("-fx-text-fill: red; -fx-font-size: 13px; -fx-font-weight: bold;");
                        reserveButton.setDisable(true);
                    } else if ("Available".equalsIgnoreCase(book.getAvailabilityStatus())) {
                        reserveButton.setText("Reserve");
                        reserveButton.setStyle("-fx-text-fill: green; -fx-font-size: 18px; -fx-font-weight: bold;");
                        reserveButton.setDisable(false);
                        availability.setStyle("-fx-text-fill: green;");
                    } else {
                        reserveButton.setText("Unavailable");
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