package controller;

import model.entity.Book;
import model.entity.Reservation;
import model.entity.User;
import service.UserService;
import service.BookService;
import service.ReservationService;
import service.NotificationService;
import service.AuthorService;
import util.AuthManager;
import util.JwtUtil;
import view.View;

import java.time.LocalDateTime;
import java.util.List;


public class LibraryController {
    private View View;

    public void setMainApp(View View) {
        this.View = View;
    }

    private UserService userService;
    private BookService bookService;
    private ReservationService reservationService;
    private final NotificationService notificationService;
    private AuthorService authorService;

    public LibraryController() {
        this.userService = new UserService();
        this.bookService = new BookService();
        this.reservationService = new ReservationService();
        this.notificationService = new NotificationService();
        this.authorService = new AuthorService();

    }

    public boolean validateToken() {
        String token = AuthManager.getInstance().getToken();
        return JwtUtil.validateToken(token);
    }

    public boolean authenticateUser(String email, String password) {
        return userService.authenticateUser(email, password);
    }

    public String getUserNameByEmail(String email) {
        return userService.getUserByEmail(email).getUsername();
    }

    public void registerUserSimple(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole("student");
        user.setBookCount(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setDeletedAt(null);

        userService.registerUser(user);
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

    public List<Book> getBooksByCategory(String category) {
        if (!validateToken()) {
            throw new SecurityException("Invalid token");
        }
        return bookService.getBooksByCategory(category);
    }

    public void reserveBook(Long userId, Long bookId) {
        if (!validateToken()) {
            throw new SecurityException("Invalid token");
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        if ("student".equalsIgnoreCase(user.getRole()) && user.getBookCount() >= 5) {
            throw new IllegalArgumentException("Students cannot reserve more than 5 books.");
        }

        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setBookId(bookId);

        reservationService.createReservation(reservation);

        // Update the user's book count
        user.setBookCount(user.getBookCount() + 1);
        userService.updateUser(user);
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

    public List<Book> searchBooksByAuthor(String authorFirstName, String authorLastName) {
        return authorService.getBooksByAuthor(authorFirstName, authorLastName);

    }

    public List<Book> searchBooksByTitle(String title) {
        return bookService.getBooksByTitle(title);
    }

    public List<Book> searchBooksByCategory(String genre) {
        return bookService.getBooksByCategory(genre);
    }

    public List<Book> searchBooksByLanguage(String language) {
        return bookService.getBooksByLanguage(language);
    }

    public int getUserBookCount(Long userId) {
        return userService.getUserBookCount(userId);
    }

    public List<Reservation> getMyBookings(Long userId) {
        return reservationService.getReservationsByUserId(userId);
    }

    public void updateUserInfo(User user) {
        userService.updateUser(user);
    }


    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public void setAuthorService(AuthorService authorService) {
        this.authorService = authorService;
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }
}
