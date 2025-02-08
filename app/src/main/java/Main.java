
import config.DatabaseConfig;
import controller.LibraryController;
import model.dao.impl.BaseDao;
import model.dao.impl.UserDao;
import model.entity.Book;
import model.entity.Reservation;
import model.entity.User;
import service.BookService;
import service.ReservationService;
import service.UserService;
import util.JwtUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection connection = DatabaseConfig.getConnection();
        BaseDao.setConnection(connection);
        UserDao userDao = new model.dao.impl.UserDao();
        UserService userService = new UserService();
        User user = new User();
        user.setUsername("Mahnoor");
        user.setPassword("password123");
        user.setEmail("testuser@example.com");
        user.setPhone("1234567890");
        user.setRole("student");
        user.setBookCount(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setDeletedAt(null);

        userService.registerUser(user);

        System.out.println("User registered successfully!");


        // Fetch user by email
        User fetchedUser = userService.getUserByEmail("testuser@example.com");
        if (fetchedUser != null) {
            System.out.println("User fetched by email: " + fetchedUser.getUsername());
            var token = JwtUtil.generateToken(fetchedUser.getUsername());
            System.out.println("Token: " + token);
            JwtUtil.parseToken(token);
        } else {
            System.out.println("User not found.");
        }



        // Fetch book by id
        Long bookId = 2L; // ID of "Book One"
        BookService bookService = new BookService();
        Book book = bookService.getBookById(bookId);
        if (book != null) {
            System.out.println("Book fetched by ID: " + book.getTitle());
        } else {
            System.out.println("Book not found.");
        }

        // Check if a reservation already exists
        ReservationService reservationService = new ReservationService();
        Reservation existingReservation = reservationService.getReservationByUserAndBook(fetchedUser.getUserId(), bookId);
        if (existingReservation == null) {
            // Reserving a book
            Reservation reservation = new Reservation();
            reservation.setBook(book);
            reservation.setUser(fetchedUser);
            reservationService.createReservation(reservation);
            System.out.println("Book reserved successfully!");
        } else {
            System.out.println("Reservation already exists for this user and book.");
        }

        // Check controller
        LibraryController libraryController = new LibraryController();
        libraryController.registerUser("testUser3", "testPass4", "rr@eee", "1234567890", "user", 5, LocalDateTime.now(), null);
        User user1 = libraryController.getUserByEmail("rr@eee");
        System.out.println("User fetched by email: " + user1.getUsername());

        libraryController.reserveBook(1L, 2L);
        System.out.println("Controller Book reserved successfully!");

        libraryController.extendDueDate(2L, LocalDateTime.now().plusDays(7));
        System.out.println("Controller Due date extended successfully!");

    }

}