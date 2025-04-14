package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.entity.Author;
import model.entity.Book;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for displaying and managing the authors page.
 */
public class AuthorsPageController extends LibraryController {

    /**
     * List of all authors loaded in the view.
     */
    private List<Author> allAuthors;

    /**
     * Spacing between author boxes in the layout.
     * */
    private static final int AUTHOR_SPACING = 40;

    /**
     * VBox container holding author UI blocks.
     */
    @FXML
    private VBox authorVBox;

    /**
     * Scroll pane containing the list of authors.
     */
    @FXML
    private ScrollPane authorScrollPane;

    /**
     * Anchor pane displayed when no authors are available.
     */
    @FXML
    private AnchorPane noAuthors;

    /**
     * Anchor pane that wraps the scrollable list of authors.
     */
    @FXML
    private AnchorPane scrollBox;

    /**
     * Logo displayed in the authors view.
     */
    @FXML
    private ImageView lukulogo;

    /**
     * Sets the list of all authors.
     * Subclasses overriding this method must ensure
     * they update any dependent views accordingly.
     *
     * @param authorsList the list of all authors
     */
    public void setAllAuthors(final List<Author> authorsList) {
        this.allAuthors = authorsList;
    }
    /**
     * Sets the authors to be displayed in the view and populates the UI.
     * Subclasses overriding this method must ensure
     * UI elements are safely updated.
     *
     * @param authors the list of authors to display
     */
    public void setAuthors(final List<Author> authors) {
        authorVBox.getChildren().clear();
        authorVBox.setSpacing(AUTHOR_SPACING);

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
                hBox = new HBox(AUTHOR_SPACING);
                authorVBox.getChildren().add(hBox);
            }
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/authorBox.fxml"));
                loader.setResources(getResourceBundle());
                AnchorPane authorBox = loader.load();

                Label authorName = (Label) authorBox.lookup("#authorName");
                Label authorBirth = (Label) authorBox.lookup("#authorBirth");
                Label authorBirthplace = (Label) authorBox.lookup(
                        "#authorBirthplace");
                Label authorBooks = (Label) authorBox.lookup("#authorBooks");

                // ImageView for profile picture
                ImageView authorImage =
                        (ImageView) authorBox.lookup("#authorImage");
                Button checkAuthorBooks =
                        (Button) authorBox.lookup("#checkAuthorBooks");

                Author author = authors.get(i);
                authorName.setText(author.getFirstName() + " "
                        + author.getLastName());
                authorBirth.setText(author.getDateOfBirth().toString());
                authorBirthplace.setText(author.getPlaceOfBirth());

                String imagePath = "/" + author.getProfileImage();
                Image image;
                try {
                    image = new Image(
                            getClass().getResourceAsStream(imagePath));
                    if (image.isError()) {
                        throw new Exception("Image not found");
                    }
                } catch (Exception e) {
                    image = new Image(
                            getClass().getResourceAsStream("/bookpicture.jpg"));
                }
                authorImage.setImage(image);

                List<Book> books = getAuthorService().getBooksByAuthor(
                        author.getFirstName(),
                        author.getLastName(),
                        getCurrentLanguage()
                );

                String bookTitles = books.stream()
                        .map(book -> book.getTitle(getCurrentLanguage()))
                        .collect(Collectors.joining(", "));
                authorBooks.setText(
                        bookTitles.isEmpty() ? "No books available" : bookTitles
                );

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

    /**
     * Placeholder method for searching authors.
     */
    @FXML
    private void searchAuthors() {
        System.out.println("Searching for authors...");
    }
}