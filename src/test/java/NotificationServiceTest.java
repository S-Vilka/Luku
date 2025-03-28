import model.dao.impl.BaseDao;
import model.dao.impl.NotificationDao;
import model.dao.impl.ReservationDao;
import model.entity.Book;
import model.entity.Notification;
import model.entity.Reservation;
import model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import service.NotificationService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



public class NotificationServiceTest {
    @Container
    private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.5.9")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private NotificationService notificationService;
    private NotificationDao notificationDao;
    private ReservationDao reservationDao;



    @BeforeEach
    public void setUp() {
        Connection connection = null;
        mariaDBContainer.start();
        try {
            connection = DriverManager.getConnection(mariaDBContainer.getJdbcUrl(), mariaDBContainer.getUsername(), mariaDBContainer.getPassword());
            BaseDao.setConnection(connection);
            notificationDao = new NotificationDao();
            notificationService = new NotificationService();
            reservationDao = new ReservationDao();

            // Create the users table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                        "user_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "username VARCHAR(255) NOT NULL, " +
                        "password VARCHAR(255) NOT NULL, " +
                        "email VARCHAR(255) NOT NULL, " +
                        "phone VARCHAR(20), " +
                        "role VARCHAR(50), " +
                        "book_count INT, " +
                        "created_at TIMESTAMP, " +
                        "deleted_at TIMESTAMP" +
                        ")");
            }

            // Create the authors table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS authors (" +
                        "author_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "first_name VARCHAR(255) NOT NULL, " +
                        "last_name VARCHAR(255) NOT NULL, " +
                        "description TEXT, " +
                        "date_of_birth DATE, " +
                        "place_of_birth VARCHAR(255)" +
                        ")");
            }

            // Create the books table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                        "book_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "title VARCHAR(255) NOT NULL, " +
                        "publication_date DATE, " +
                        "description TEXT, " +
                        "availability_status VARCHAR(50), " +
                        "category VARCHAR(50), " +
                        "language VARCHAR(50), " +
                        "isbn VARCHAR(20), " +
                        "location VARCHAR(255)" +
                                ")");
            }

            // Create the writes table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS writes (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "book_id BIGINT, " +
                        "author_id BIGINT, " +
                        "FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE, " +
                        "FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE CASCADE" +
                        ")");
            }


            // Create the reservations table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS reservations (" +
                        "reservation_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "user_id BIGINT, " +
                        "book_id BIGINT, " +
                        "borrow_date TIMESTAMP, " +
                        "due_date TIMESTAMP, " +
                        "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                        "FOREIGN KEY (book_id) REFERENCES books(book_id))");
            }

            // Create the notifications table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS notifications (" +
                        "notification_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "user_id BIGINT, " +
                        "message_en TEXT, " +
                        "message_ur TEXT, " +
                        "message_ru TEXT, " +
                        "created_at TIMESTAMP, " +
                        "reservation_id BIGINT, " +
                        "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                        "FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id))");
            }

            // Insert a test user
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("INSERT INTO users (username, password, email, phone, role, book_count, created_at, deleted_at) VALUES " +
                        "('testuser', 'testPass', 'test@test.com', '1234567890', 'user', 5, NOW(), NULL)");
            }
            // Insert a test author
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("INSERT INTO authors (first_name, last_name, description, date_of_birth, place_of_birth) VALUES " +
                        "('John', 'Doe', 'Famous author', '1970-01-01', 'Somewhere')");
            }

            // Insert a test book
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("INSERT INTO books (title_en, title_ur, title_ru, publication_date, description, availability_status, category, language, isbn, location) VALUES " +
                        "('Test Book', '2021-01-01', 'Description of Test Book', 'Available', 'Fiction', 'English', '1234567890', 'Aisle3')");
            }

            // Insert a test writes entry
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("INSERT INTO writes (book_id, author_id) VALUES (1, 1)");
            }

            // Insert a test reservation
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("INSERT INTO reservations (user_id, book_id, borrow_date, due_date) VALUES " +
                        "(1, 1, NOW(), NOW() + INTERVAL 7 DAY)");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSaveNotification() {
        User user = new User();
        user.setUserId(1L);

        Reservation reservation = new Reservation();
        reservation.setReservationId(1L);
        reservation.setUser(user);

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessageEnglish("Test message in English");
        notification.setMessageUrdu("ٹیسٹ پیغام اردو میں");
        notification.setMessageRussian("Тестовое сообщение на русском");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReservation(reservation);

        notificationService.saveNotification(notification);

        List<Notification> notifications = notificationService.getNotificationsByUserId(1L);
        assertFalse(notifications.isEmpty());
        assertNotNull( notifications.getFirst().getMessageEnglish()
        );
    }


    @Test
    public void testCreateNotificationForReservation() {
        User user = new User();
        user.setUserId(1L);

        Book book = new Book();
        book.setBookId(1L);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setDueDate(LocalDateTime.now().plusDays(7));

        reservationDao.saveReservation(reservation);

        notificationService.createNotificationForReservation(reservation.getReservationId(), "English");

        List<Notification> notifications = notificationService.getNotificationsByUserId(1L);
        assertFalse(notifications.isEmpty());
        assertNotNull(notifications.getFirst().getMessageEnglish());
    }

    @Test
    public void testGetNotificationsByUserId() {
        User user = new User();
        user.setUserId(1L);

        Reservation reservation = new Reservation();
        reservation.setReservationId(1L);
        reservation.setUser(user);

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessageEnglish("Test message in English");
        notification.setMessageUrdu("ٹیسٹ پیغام اردو میں");
        notification.setMessageRussian("Тестовое сообщение на русском");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReservation(reservation);

        notificationService.saveNotification(notification);

        List<Notification> notifications = notificationService.getNotificationsByUserId(1L);
        assertFalse(notifications.isEmpty());
        Notification notification1 = notifications.getFirst();
        assertNotNull(notification1.getMessageEnglish());
    }

    @Test
    public void testDeleteNotificationByReservationId() {
        User user = new User();
        user.setUserId(1L);

        Reservation reservation = new Reservation();
        reservation.setReservationId(1L);
        reservation.setUser(user);

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessageEnglish("Test message in English");
        notification.setMessageUrdu("ٹیسٹ پیغام اردو میں");
        notification.setMessageRussian("Тестовое сообщение на русском");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReservation(reservation);

        notificationService.saveNotification(notification);

        notificationService.deleteNotificationByReservationId(1L);

        List<Notification> notifications = notificationService.getNotificationsByUserId(1L);
        assertTrue(notifications.isEmpty());
    }

    @Test
    public void testUpdateNotification(){
        User user = new User();
        user.setUserId(1L);

        Reservation reservation = new Reservation();
        reservation.setReservationId(1L);
        reservation.setUser(user);

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessageEnglish("Test message in English");
        notification.setMessageUrdu("ٹیسٹ پیغام اردو میں");
        notification.setMessageRussian("Тестовое сообщение на русском");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReservation(reservation);

        notificationService.saveNotification(notification);

        notificationService.updateNotification(1L, "English");

        List<Notification> notifications = notificationService.getNotificationsByUserId(1L);
        assertFalse(notifications.isEmpty());
        assertNotNull(notifications.getFirst().getMessageEnglish());
    }

}
