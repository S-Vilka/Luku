package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

/**
 * Controller for displaying and managing user's book reservations.
 */
public class MyBookingController extends LibraryController {

    /** Spacing between book entries in the layout. */
    private static final int BOOK_SPACING = 40;

    /**
     * Button to extend a reservation.
     */
    @FXML private Button extendReservation;

    /**
     * Button to return a reserved book.
     */
    @FXML private Button returnBook;

    /**
     * VBox container for displaying books.
     */
    @FXML private VBox bookVBox;

    /**
     * Scroll pane for book display.
     */
    @FXML private javafx.scene.control.ScrollPane bookScrollPane;

    /**
     * Scroll container pane.
     */
    @FXML private AnchorPane scrollBox;

    /**
     * Pane displayed when no books are available.
     */
    @FXML private AnchorPane noBooks;

    /**
     * Displays books reserved by the user.
     *
     * @param userId the ID of the user
     * @param currentLanguage the current UI language
     */
    public final void setBooksForUser(final Long userId,
                                      final String currentLanguage) {
        bookVBox.getChildren().clear();
        bookVBox.setSpacing(BOOK_SPACING);

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
                .map(res -> getBookService().getBookById(res.getBookId()))
                .collect(Collectors.toList());

        HBox hBox = null;
        for (int i = 0; i < books.size(); i++) {
            if (i % 2 == 0) {
                hBox = new HBox(BOOK_SPACING);
                bookVBox.getChildren().add(hBox);
            }
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/bookingsBookBox.fxml"));
                loader.setResources(getResourceBundle());
                AnchorPane bookBox = loader.load();

                Label bookName = (Label) bookBox.lookup("#bookName");
                Label author = (Label) bookBox.lookup("#author");
                Label publicationDate = (Label) bookBox.lookup(
                        "#publicationDate");
                Label borrowDate = (Label) bookBox.lookup("#borrowDate");
                Label dueDate = (Label) bookBox.lookup("#dueDate");
                Label bookId = (Label) bookBox.lookup("#bookId");
                Button extendButton = (Button) bookBox.lookup(
                        "#extendButton");
                Button returnButton = (Button) bookBox.lookup(
                        "#returnButton");
                ImageView bookCover = (ImageView) bookBox.lookup(
                        "#bookCover");

                Book book = books.get(i);
                Reservation reservation = reservations.get(i);
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
                        : "Unknown");
                borrowDate.setText(reservation.getBorrowDate().toString());
                dueDate.setText(reservation.getDueDate().toString());
                bookId.setText(String.valueOf(book.getBookId()));

                String imagePath = "/" + book.getCoverImage();
                Image image;
                try {
                    image = new Image(getClass().getResourceAsStream(
                            imagePath));
                    if (image.isError()) {
                        throw new Exception("Image not found");
                    }
                } catch (Exception e) {
                    image = new Image(getClass().getResourceAsStream(
                            "/bookpicture.jpg"));
                }
                bookCover.setImage(image);

                Long bookIdNo = book.getBookId();
                extendButton.setOnAction(event -> {
                    try {
                        Reservation res = getReservationByUserAndBook(
                                userId, bookIdNo);
                        if (res != null) {
                            extendReservation(
                                    reservation.getReservationId());
                            getNotificationService().updateNotification(
                                    res.getReservationId());
                            FXMLLoader loader2 = loadScene("/myBookings.fxml");
                            loader2.setResources(getResourceBundle());
                            MyBookingController controller =
                                    loader2.getController();
                            controller.setBooksForUser(userId,
                                    currentLanguage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                returnButton.setOnAction(event -> {
                    try {
                        Reservation res = getReservationByUserAndBook(
                                userId, bookIdNo);
                        if (res != null) {
                            getBookService().setBookAvailability(bookIdNo,
                                    "Available");
                            getUserService().decreaseUserBookCount(userId);
                            getNotificationService()
                                    .deleteNotificationByReservationId(
                                            reservation.getReservationId());
                            getReservationService().deleteReservation(
                                    reservation.getReservationId());
                            setBooksForUser(userId, currentLanguage);
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
