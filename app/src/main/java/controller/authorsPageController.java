package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.entity.Author;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import model.entity.Writes;

public class authorsPageController extends LibraryController {
    private List<Author> allAuthors;
    @FXML
    private VBox authorVBox;
    @FXML
    private ScrollPane authorScrollPane;
    @FXML
    private AnchorPane noAuthors;

    public void setAllAuthors(List<Author> allAuthors) {
        this.allAuthors = allAuthors;
    }

    public void setAuthors(List<Author> authors) {
        System.out.println("setAuthors() called with " + authors.size() + " authors."); // Debugging

        if (noAuthors == null) {
            System.out.println("Error: noAuthors is null! Check FXML file.");
            return;
        }

        if (authors.isEmpty()) {
            System.out.println("No authors found.");
            noAuthors.setVisible(true);
            return;
        } else {
            noAuthors.setVisible(false);
        }

        if (allAuthors == null) {
            setAllAuthors(authors);
        }

        authorVBox.getChildren().clear();
        authorVBox.setSpacing(40);
        HBox hBox = null;

        for (int i = 0; i < authors.size(); i++) {
            System.out.println("Loading author: " + authors.get(i).getFirstName() + " " + authors.get(i).getLastName()); // Debugging

            if (i % 2 == 0) {
                hBox = new HBox(40);
                authorVBox.getChildren().add(hBox);
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/authorBox.fxml"));
                AnchorPane authorBox = loader.load();

                if (authorBox == null) {
                    System.out.println("Error: authorBox is null!");
                    continue;
                }

                Label authorName = (Label) authorBox.lookup("#authorName");
                Label authorBirth = (Label) authorBox.lookup("#authorBirth");
                Label authorBirthplace = (Label) authorBox.lookup("#authorBirthplace");
                Label authorBooks = (Label) authorBox.lookup("#authorBooks");

                Author author = authors.get(i);
                authorName.setText(author.getFirstName() + " " + author.getLastName());
                authorBirth.setText(author.getDateOfBirth().toString());
                authorBirthplace.setText(author.getPlaceOfBirth());
                authorBooks.setText(author.getWrites().stream()
                        .map(w -> w.getBook().getTitle())
                        .collect(Collectors.joining(", ")));

                hBox.getChildren().add(authorBox);
                System.out.println("Added authorBox for: " + author.getFirstName()); // Debugging
            } catch (Exception e) {
                System.out.println("Error loading authorBox.fxml:");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void showAuthorBooks() {
        System.out.println("Show books for selected author");
    }

    @FXML
    private void searchAuthors() {
        System.out.println("Searching for authors...");
    }
}