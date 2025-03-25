package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import model.entity.Author;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import model.entity.Book;

import javafx.scene.image.ImageView;

public class authorsPageController extends LibraryController {
    private List<Author> allAuthors;

    @FXML private VBox authorVBox;
    @FXML private ScrollPane authorScrollPane;
    @FXML private AnchorPane noAuthors, scrollBox, bodyBox;
    @FXML private ImageView lukulogo;

    public void setAllAuthors(List<Author> allAuthors) {
        this.allAuthors = allAuthors;
    }

    public void setAuthors(List<Author> authors) {
        authorVBox.getChildren().clear();
        authorVBox.setSpacing(40);

        if (authors.isEmpty()) {
            scrollBox.setVisible(false);
            noAuthors.setVisible(true);
            return;
        } else {
            scrollBox.setVisible(true);
            noAuthors.setVisible(false);
        }

        if (allAuthors == null) {
            setAllAuthors(authors);
        }

        HBox hBox = null;
        for (int i = 0; i < authors.size(); i++) {
            if (i % 2 == 0) {
                hBox = new HBox(40);
                authorVBox.getChildren().add(hBox);
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/authorBox.fxml"));
                AnchorPane authorBox = loader.load();

                Label authorName = (Label) authorBox.lookup("#authorName");
                Label authorBirth = (Label) authorBox.lookup("#authorBirth");
                Label authorBirthplace = (Label) authorBox.lookup("#authorBirthplace");
                Label authorBooks = (Label) authorBox.lookup("#authorBooks");
                ImageView authorImage = (ImageView) authorBox.lookup("#authorImage"); // ImageView for profile picture
                Button checkAuthorBooks = (Button) authorBox.lookup("#checkAuthorBooks");

                Author author = authors.get(i);
                authorName.setText(author.getFirstName() + " " + author.getLastName());
                authorBirth.setText(author.getDateOfBirth().toString());
                authorBirthplace.setText(author.getPlaceOfBirth());

                // ** Load author image **
                String imagePath = "/" + author.getProfileImage(); // Assuming images are in resources
                Image image;
                try {
                    image = new Image(getClass().getResourceAsStream(imagePath));
                    if (image.isError()) {
                        throw new Exception("Image not found");
                    }
                } catch (Exception e) {
                    image = new Image(getClass().getResourceAsStream("/bookpicture.jpg")); // Fallback image
                }
                authorImage.setImage(image);

                // Fetch books by author
                List<Book> books = getAuthorService().getBooksByAuthor(author.getFirstName(), author.getLastName());
                String bookTitles = books.stream().map(Book::getTitle).collect(Collectors.joining(", "));
                authorBooks.setText(bookTitles.isEmpty() ? "No books available" : bookTitles);

                // Set event handler for "Check their books" button
                checkAuthorBooks.setOnAction(event -> {
                    try {
                        showAuthorBooks(author);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                hBox.getChildren().add(authorBox);
            } catch (Exception e) {
                System.out.println("Error loading authorBox.fxml:");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void searchAuthors() {
        System.out.println("Searching for authors...");
    }
}
