import model.dao.impl.BaseDao;
import model.dao.impl.ReservationDao;
import model.entity.Book;
import model.entity.Reservation;
import model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import service.ReservationService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceTest {
    @Container
    private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.5.9")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private ReservationService reservationService;
    private ReservationDao reservationDao;

    @BeforeEach
    public void setUp() {
        Connection connection = null;
        mariaDBContainer.start();
        try {
            connection = DriverManager.getConnection(mariaDBContainer.getJdbcUrl(), mariaDBContainer.getUsername(), mariaDBContainer.getPassword());
            BaseDao.setConnection(connection);
            reservationDao = new ReservationDao();
            reservationService = new ReservationService();

            // Create the reservations table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                        "user_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "username VARCHAR(255) NOT NULL, " +
                        "email VARCHAR(255) NOT NULL)");

                stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                        "book_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "title VARCHAR(255) NOT NULL)");


                stmt.execute("CREATE TABLE IF NOT EXISTS reservations (" +
                        "reservation_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "user_id BIGINT NOT NULL, " +
                        "book_id BIGINT NOT NULL, " +
                        "borrow_date TIMESTAMP NOT NULL, " +
                        "due_date TIMESTAMP NOT NULL, " +
                        "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                        "FOREIGN KEY (book_id) REFERENCES books(book_id))");

                stmt.execute("CREATE TABLE IF NOT EXISTS notifications (" +
                        "notification_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "user_id BIGINT NOT NULL, " +
                        "message_en TEXT, " +
                        "message_ur TEXT, " +
                        "message_ru TEXT, " +
                        "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                        "reservation_id BIGINT, " +
                        "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                        "FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id))");
            }

            // Insert sample data
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("INSERT INTO users (username, email) VALUES ('testuser', 'testuser@example.com')");
                stmt.execute("INSERT INTO books (title) VALUES ('Sample Book')");
                stmt.execute("INSERT INTO reservations (user_id, book_id, borrow_date, due_date) VALUES (1, 1, NOW(), NOW() + INTERVAL 14 DAY)");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateReservation() {
        User user = new User();
        user.setUserId(1L);

        Book book = new Book();
        book.setBookId(1L);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);

        reservationService.createReservation(reservation);

        assertNotNull(reservation.getReservationId());
    }


    @Test
    public void testGetReservationById() {
        Reservation reservation = reservationService.getReservationById(1L, "English");
        assertNotNull(reservation);
        assertEquals(1L, reservation.getReservationId());
    }


    @Test
    public void testGetAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations("English");
        assertNotNull(reservations);
        assertFalse(reservations.isEmpty());
    }


    @Test
    public void testExtendReservation() {
        LocalDateTime newDueDate = LocalDateTime.now().plusDays(7).withNano(0);
        reservationService.extendReservation(1L, newDueDate);

        Reservation reservation = reservationService.getReservationById(1L, "English");
        assertEquals(newDueDate, reservation.getDueDate().withNano(0));
    }

    @Test
    public void testGetReservationsByUserId() {
        List<Reservation> reservations = reservationService.getReservationsByUserId(1L, "English");
        assertNotNull(reservations);
        assertFalse(reservations.isEmpty());
    }

    @Test
    public void testGetReservationByUserAndBook() {
        Reservation reservation = reservationService.getReservationByUserAndBook(1L, 1L, "English");
        assertNotNull(reservation);
        assertEquals(1L, reservation.getUser().getUserId());
        assertEquals(1L, reservation.getBook().getBookId());
    }

    @Test
    public void testUpdateReservation() {
        Reservation reservation = reservationService.getReservationById(1L, "English");
        reservation.setDueDate(LocalDateTime.now().plusDays(7).withNano(0));
        reservationService.updateReservation(reservation);

        Reservation updatedReservation = reservationService.getReservationById(1L, "English");
        assertEquals(reservation.getDueDate().withNano(0), updatedReservation.getDueDate().withNano(0));
    }

    @Test
    public void testDeleteReservation() {
        User user = new User();
        user.setUserId(1L);

        Book book = new Book();
        book.setBookId(1L);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);

        reservationService.createReservation(reservation);

        // Ensure the reservation was created
        assertNotNull(reservation.getReservationId());

        // Delete the created reservation
        reservationService.deleteReservation(reservation.getReservationId());

        // Verify the reservation has been deleted
        Reservation deletedReservation = reservationService.getReservationById(reservation.getReservationId(), "English");
        assertNull(deletedReservation);
    }



}


