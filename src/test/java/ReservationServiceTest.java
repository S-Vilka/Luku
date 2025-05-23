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

            // Create the tables
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                        "user_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "username VARCHAR(255) NOT NULL, " +
                        "email VARCHAR(255) NOT NULL)");

                stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                        "book_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "title_en VARCHAR(255) NOT NULL, " +
                        "title_ur VARCHAR(255), " +
                        "title_ru VARCHAR(255), " +
                        "publication_date DATE, " +
                        "description TEXT, " +
                        "availability_status VARCHAR(50), " +
                        "category VARCHAR(50), " +
                        "language VARCHAR(50), " +
                        "isbn VARCHAR(20), " +
                        "location VARCHAR(255)" +
                        ")");

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
                stmt.execute("INSERT INTO books (title_en, title_ur, title_ru, publication_date, description, availability_status, category, language, isbn, location) VALUES " +
                        "('Sample Book in English', 'Sample Book in Urdu', 'Sample Book in Russian', '2021-01-01', 'Description of Sample Book', 'Available', 'Fiction', 'English', '1234567890', 'Aisle3')");
                stmt.execute("INSERT INTO reservations (user_id, book_id, borrow_date, due_date) VALUES (1, 1, NOW(), NOW() + INTERVAL 14 DAY)");
            }

            // Verify sample data insertion
            try (Statement stmt = connection.createStatement()) {
                var rs = stmt.executeQuery("SELECT COUNT(*) FROM reservations");
                if (rs.next()) {
                    System.out.println("Number of reservations: " + rs.getInt(1));
                }
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
        Reservation reservation = reservationService.getReservationById(1L);
        assertNotNull(reservation);
        assertEquals(1L, reservation.getReservationId());
    }


    @Test
    public void testGetAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        assertNotNull(reservations);
        assertFalse(reservations.isEmpty());
    }


    @Test
    public void testExtendReservation() {
        LocalDateTime newDueDate = LocalDateTime.now().plusDays(7).withNano(0);
        reservationService.extendReservation(1L, newDueDate);

        Reservation reservation = reservationService.getReservationById(1L);
        assertEquals(newDueDate, reservation.getDueDate().withNano(0));
    }

    @Test
    public void testGetReservationsByUserId() {
        List<Reservation> reservations = reservationService.getReservationsByUserId(1L);
        assertNotNull(reservations);
        assertFalse(reservations.isEmpty());
    }

    @Test
    public void testGetReservationByUserAndBook() {
        Reservation reservation = reservationService.getReservationByUserAndBook(1L, 1L);
        assertNotNull(reservation);
        assertEquals(1L, reservation.getUser().getUserId());
        assertEquals(1L, reservation.getBook().getBookId());
    }

    @Test
    public void testUpdateReservation() {
        Reservation reservation = reservationService.getReservationById(1L);
        reservation.setDueDate(LocalDateTime.now().plusDays(7).withNano(0));
        reservationService.updateReservation(reservation);

        Reservation updatedReservation = reservationService.getReservationById(1L);
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
        Reservation deletedReservation = reservationService.getReservationById(reservation.getReservationId());
        assertNull(deletedReservation);
    }



}


