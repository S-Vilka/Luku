package controller;

import model.entity.Reservation;
import model.entity.User;
import service.UserService;
import service.BookService;
import service.ReservationService;
import service.NotificationService;
import service.AuthorService;
import service.WritesService;



import java.time.LocalDateTime;


public class LibraryController {
    private final UserService userService;
    private final BookService bookService;
    private ReservationService reservationService;
    private final NotificationService notificationService;
    private final AuthorService authorService;
    private final WritesService writesService;


    public LibraryController() {
        this.userService = new UserService();
        this.bookService = new BookService();
        this.reservationService = new ReservationService();
        this.notificationService = new NotificationService();
        this.authorService = new AuthorService();
        this.writesService = new WritesService();

    }

    public void registerUser(String username, String password, String email, String phone, String role, int book_count, LocalDateTime created_at, LocalDateTime deleted_at) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);
        user.setBookCount(book_count);
        user.setCreatedAt(created_at);
        user.setDeletedAt(deleted_at);

        userService.registerUser(user);
    }

    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    public void reserveBook(Long userId, Long bookId) {
        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setBookId(bookId);

        reservationService.createReservation(reservation);
    }

    public void extendDueDate(Long reservationId, LocalDateTime newDueDate) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation != null) {
            reservation.setDueDate(newDueDate);
            reservationService.updateReservation(reservation);
        } else {
            throw new IllegalArgumentException("Reservation with ID " + reservationId + " not found.");
        }
    }



}
