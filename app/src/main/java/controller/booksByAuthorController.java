package controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.entity.Book;
import model.entity.Author;
import service.BookService;
import service.AuthorService;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;

public class booksByAuthorController extends LibraryController {
    private final BookService bookService = new BookService();
    private final AuthorService authorService = new AuthorService();
    private List<Book> allBooks, availableBooks;
    private Author selectedAuthor;

    @FXML
    private VBox bookVBox;

    @FXML
    private ScrollPane bookScrollPane;

    @FXML
    private CheckBox availabilityCheckBox;

    @FXML
    private AnchorPane noBooks, scrollBox;

    @FXML
    private Label selectedAuthorLabel;


    public void setAllBooks(List<Book> allBooks) {
        this.allBooks = allBooks;
    }

    public void setAvailableBooks(List<Book> availableBooks) {
        this.availableBooks = availableBooks;
    }

    public void setSelectedAuthor(Author author) {
        this.selectedAuthor = author;

        if (selectedAuthorLabel == null) {
            System.out.println("Error: selectedAuthorLabel is null. Check FXML binding.");
            return;
        }

        if (selectedAuthor != null) {
            selectedAuthorLabel.setText(selectedAuthor.getFirstName() + " " + selectedAuthor.getLastName());
        } else {
            selectedAuthorLabel.setText("Unknown Author");
        }
    }

    public void loadBooksByAuthor() {
        if (selectedAuthor == null) {
            System.out.println("Error: No author selected.");
            return;
        }

        // Fetch books for the selected author
        List<Book> books = bookService.getBooksByAuthorId(selectedAuthor.getAuthorId());
        setAllBooks(books);
        setBooks(allBooks);
    }

    @FXML
    public void backToAuthors() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/authorsPage.fxml"));
        Parent root = loader.load();
        authorsPageController controller = loader.getController();
        controller.setAuthors(authorService.getAllAuthors());
        getPrimaryStage().setTitle("Luku Library - Authors");
        getPrimaryStage().setScene(new Scene(root));
        getPrimaryStage().show();
        updateHeader();
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

    public void chooseReserveAuthor(Long bookId) throws Exception {
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
                Button reserveButton = (Button) bookBox.lookup("#reserveButton");

                Book book = books.get(i);
                bookName.setText(book.getTitle());
                publicationDate.setText(book.getPublicationDate().toString());
                availability.setText(book.getAvailabilityStatus());
                location.setText(book.getLocation());

                // Fetch and set authors
                String authorNames = bookService.getAuthorsByBookId(book.getBookId()).stream()
                        .map(a -> a.getFirstName() + " " + a.getLastName())
                        .collect(Collectors.joining(", "));
                author.setText(authorNames);

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
                        chooseReserveAuthor(bookIdNo);
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
