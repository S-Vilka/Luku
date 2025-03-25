package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.entity.Author;
import model.entity.Book;
import model.entity.Reservation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class myBookingController extends LibraryController {
    @FXML private Button extendReservation, returnBook;
    @FXML private VBox bookVBox;
    @FXML private javafx.scene.control.ScrollPane bookScrollPane;
    @FXML private AnchorPane scrollBox, noBooks;

    public void setBooksForUser(Long userId) {
        bookVBox.getChildren().clear();
        bookVBox.setSpacing(40);

        List<Reservation> reservations = getMyBookings(userId);
        if (reservations.isEmpty()) {
            scrollBox.setVisible(false);
            noBooks.setVisible(true);
            return;
        } else {
            scrollBox.setVisible(true);
            noBooks.setVisible(false);
        }

        List<Book> books = reservations.stream()
                .map(reservation -> getBookService().getBookById(reservation.getBookId()))
                .collect(Collectors.toList());

        HBox hBox = null;
        for (int i = 0; i < books.size(); i++) {
            if (i % 2 == 0) {
                hBox = new HBox(40);
                bookVBox.getChildren().add(hBox);
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookingsBookBox.fxml"));
                AnchorPane bookBox = loader.load();

                Label bookName = (Label) bookBox.lookup("#bookName");
                Label author = (Label) bookBox.lookup("#author");
                Label publicationDate = (Label) bookBox.lookup("#publicationDate");
                Label borrowDate = (Label) bookBox.lookup("#borrowDate");
                Label dueDate = (Label) bookBox.lookup("#dueDate");
                Label bookId = (Label) bookBox.lookup("#bookId");
                Button extendButton = (Button) bookBox.lookup("#extendButton");
                Button returnButton = (Button) bookBox.lookup("#returnButton");
                ImageView bookCover = (ImageView) bookBox.lookup("#bookCover");

                Book book = books.get(i);
                Reservation reservation = reservations.get(i);
                bookName.setText(book.getTitle());

                // Fetch and concatenate author names
                Set<Author> authorSet = getBookService().getAuthorsByBookId(book.getBookId());
                String authorsText = authorSet.stream()
                        .map(a -> a.getFirstName() + " " + a.getLastName())
                        .collect(Collectors.joining(", "));
                author.setText(authorsText.isEmpty() ? "Unknown Author" : authorsText);

                publicationDate.setText(book.getPublicationDate() != null ? book.getPublicationDate().toString() : "Unknown");
                borrowDate.setText(reservation.getBorrowDate().toString());
                dueDate.setText(reservation.getDueDate().toString());
                bookId.setText(String.valueOf(book.getBookId()));

                // ** Load book cover **
                String imagePath = "/" + book.getCoverImage();
                Image image;
                try {
                    image = new Image(getClass().getResourceAsStream(imagePath));
                    if (image.isError()) {
                        throw new Exception("Image not found");
                    }
                } catch (Exception e) {
                    image = new Image(getClass().getResourceAsStream("/bookpicture.jpg")); // Fallback image
                }
                bookCover.setImage(image);

                Long bookIdNo = book.getBookId();
                extendButton.setOnAction(event -> {
                    try {
                        Reservation res = getReservationByUserAndBook(userId, bookIdNo);
                        if (res != null) {
                            extendReservation(reservation.getReservationId());
                            getNotificationService().updateNotification(res.getReservationId());
//                            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/myBookings.fxml"));
//                            Parent root = loader2.load();
                            FXMLLoader loader2 = loadScene("/myBookings.fxml");
                            myBookingController controller = loader2.getController();
                            controller.setBooksForUser(userId);
//                            getPrimaryStage().setScene(new Scene(root));
//                            getPrimaryStage().show();
//                            AnchorPane bodyBox = (AnchorPane) getPrimaryStage().getScene().lookup("#bodyBox");
//                            bodyBox.getChildren().setAll(root);
//                            updateHeader();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                returnButton.setOnAction(event -> {
                    try {
                        Reservation res = getReservationByUserAndBook(userId, bookIdNo);
                        if (res != null) {
                            getBookService().setBookAvailability(bookIdNo, "Available");
                            getUserService().decreaseUserBookCount(userId);
                            getNotificationService().deleteNotificationByReservationId(reservation.getReservationId());
                            getReservationService().deleteReservation(reservation.getReservationId());
                            setBooksForUser(userId);
                        }
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