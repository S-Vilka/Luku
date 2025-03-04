package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.entity.Author;
import model.entity.Book;
import model.entity.Reservation;
import service.BookService;
import service.UserService;

import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class myBookingController extends LibraryController {
    @FXML
    private Button extendReservation, returnBook;
    @FXML
    private VBox bookVBox;

    @FXML
    private javafx.scene.control.ScrollPane bookScrollPane;
    @FXML
    private AnchorPane scrollBox;


    public void setBooksForUser(Long userId) {
        bookVBox.getChildren().clear();
        bookVBox.setSpacing(40);

        List<Reservation> reservations = getMyBookings(userId);
        if (reservations.isEmpty()) {
            scrollBox.setVisible(false);
//            noBooks.setVisible(true);
            return;
        } else {
            scrollBox.setVisible(true);
//            noBooks.setVisible(false);
        }

        List<Book> books = reservations.stream()
                .map(reservation -> getBookService().getBookById(reservation.getBookId()))
                .collect(Collectors.toList());


        HBox hBox = null;
        for (int i = 0; i < books.size(); i++) {
            if (i % 2 == 0) {
                hBox = new HBox(40); // Create a new HBox with spacing of 10
                bookVBox.getChildren().add(hBox);
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookingsBookBox.fxml"));
                AnchorPane bookBox = loader.load();

                javafx.scene.control.Label bookName = (javafx.scene.control.Label) bookBox.lookup("#bookName");
                javafx.scene.control.Label author = (javafx.scene.control.Label) bookBox.lookup("#author");
                javafx.scene.control.Label publicationDate = (javafx.scene.control.Label) bookBox.lookup("#publicationDate");
                javafx.scene.control.Label borrowDate = (javafx.scene.control.Label) bookBox.lookup("#borrowDate");
                javafx.scene.control.Label dueDate = (javafx.scene.control.Label) bookBox.lookup("#dueDate");
                javafx.scene.control.Label bookId = (javafx.scene.control.Label) bookBox.lookup("#bookId");
                javafx.scene.control.Button extendButton = (javafx.scene.control.Button) bookBox.lookup("#extendButton");
                javafx.scene.control.Button returnButton = (javafx.scene.control.Button) bookBox.lookup("#returnButton");


                Book book = books.get(i);
                Reservation reservation = reservations.get(i);
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
                bookId.setText(String.valueOf(book.getBookId()));
                borrowDate.setText(reservation.getBorrowDate().toString());
                dueDate.setText(reservation.getDueDate().toString());


                Long bookIdNo = book.getBookId();
                extendButton.setOnAction(event -> {
                    try {
                        Reservation res = getReservationByUserAndBook(userId, bookIdNo);

                        if (res != null) {
                            extendReservation(reservation.getReservationId());
                            getNotificationService().updateNotification(res.getReservationId());
                            //Refresh page after extending reservation
                            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/myBookings.fxml"));
                            Parent root = loader2.load();
                            myBookingController controller = loader2.getController();
                            controller.setBooksForUser(userId);
                            getPrimaryStage().setScene(new Scene(root));
                            getPrimaryStage().show();
                            updateHeader();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                returnButton.setOnAction(event -> {
                    try {
                        Reservation res = getReservationByUserAndBook(userId, bookIdNo);
                        if (res != null) {
                            System.out.println("RESERVATION RETURNED");
                            getBookService().setBookAvailability(bookIdNo, "Available");
                            System.out.println("RESERVATION AVAILABLE");

                            // decrease the user's book count
                            getUserService().decreaseUserBookCount(userId);

                            // delete the notification
                            getNotificationService().deleteNotificationByReservationId(reservation.getReservationId());

                            // Remove the reservation
                            getReservationService().deleteReservation(reservation.getReservationId());

                            // Optionally, refresh the book list for the user
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
