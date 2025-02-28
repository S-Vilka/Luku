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

public class categoryPageController extends LibraryController {
    private List<Book> allBooks;
    @FXML
    private VBox bookVBox;
    @FXML
    private ScrollPane bookScrollPane;
    @FXML
    private CheckBox availabilityCheckBox;
    @FXML
    private AnchorPane noBooks;

    public void setAllBooks(List<Book> allBooks) {
        this.allBooks = allBooks;
    }

    @FXML
    public void chooseOnlyAvailable() {
        if (availabilityCheckBox.isSelected()) {
            List<Book> availableBooks = allBooks.stream()
                    .filter(book -> "Available".equalsIgnoreCase(book.getAvailabilityStatus()))
                    .collect(Collectors.toList());
            setBooks(availableBooks);
        } else {
            setBooks(allBooks);
        }
    }
    public void setBooks(List<Book> books) {
        if (books.size() == 0) {
            noBooks.setVisible(true);
            return;
        } else {
            noBooks.setVisible(false);
        }

        if (!availabilityCheckBox.isSelected()) {
            setAllBooks(books);
        }
        bookVBox.getChildren().clear();
        bookVBox.setSpacing(40);
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

                hBox.getChildren().add(bookBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
