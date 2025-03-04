package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
import service.UserService;

public class categoryPageController extends LibraryController {
    private List<Book> allBooks, availableBooks;
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

    public CheckBox getAvailabilityCheckBox() {
        return availabilityCheckBox;
    }

    public void clearBookLists() {
        if (allBooks == null) {
            return;
        }
        allBooks.clear();
        availableBooks.clear();
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

        if (books.isEmpty()) {
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
                hBox = new HBox(40); // Create a new HBox with spacing of 10
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

                Book book = books.get(i);
                bookName.setText(book.getTitle());

                // Concatenate author names
                StringBuilder authors = new StringBuilder();
                BookService bookService = getBookService();
                Set<Author> authorSet = bookService.getAuthorsByBookId(book.getBookId());
                for (Author a : authorSet) {
                    if (authors.length() > 0) {
                        authors.append(", ");
                    }
                    authors.append(a.getFirstName()).append(" ").append(a.getLastName());
                }
                author.setText(authors.toString());

                publicationDate.setText(book.getPublicationDate().toString());
                availability.setText(book.getAvailabilityStatus());
                location.setText(book.getLocation());
                bookId.setText(String.valueOf(book.getBookId()));

                // Update reserveButton based on availability
//                if ("Available".equalsIgnoreCase(book.getAvailabilityStatus())) {
//                    reserveButton.setText("Reserve");
//                    reserveButton.setStyle("-fx-text-fill: green;");
//                    reserveButton.setDisable(false);
//                    availability.setStyle("-fx-text-fill: green;");
//                } else {
//                    reserveButton.setText("Unavailable");
//                    reserveButton.setStyle("-fx-text-fill: red;");
//                    reserveButton.setDisable(true);
//                    availability.setStyle("-fx-text-fill: red;");
//                }
                // Check user role and book count
                Long userId = getSavedUserId();
                int userBookCount = getUserService().getUserBookCount(userId);
                String userRole = getUserService().getUserRole(userId);


                if ("teacher".equalsIgnoreCase(userRole)) {
                    reserveButton.setText("Reserve");
                    reserveButton.setStyle("-fx-text-fill: green; -fx-font-size: 18px; -fx-font-weight: bold;");
                    reserveButton.setDisable(false);
                    availability.setStyle("-fx-text-fill: green;");
                } else if ("student".equalsIgnoreCase(userRole) && userBookCount >= 5) {
                    reserveButton.setText("You cannot reserve anymore books");
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

                Long bookIdNo = book.getBookId();
                reserveButton.setOnAction(event -> {
                    try {
                        chooseReserveCategory(bookIdNo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                hBox.getChildren().add(bookBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}