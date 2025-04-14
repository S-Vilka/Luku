package model.dao.impl;

import model.entity.Notification;
import model.entity.Reservation;
import model.entity.User;
import model.entity.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing
 * notifications in the system.
 * This class provides methods to save,
 * retrieve, update, and delete notifications.
 * It also handles the creation of
 * notifications based on reservations.
 */
public class NotificationDao {
    /**
     * The UserDao instance for user-related operations.
     */
    private UserDao userDao;
    /**
     * The ReservationDao instance for reservation-related operations.
     */
    private ReservationDao reservationDao;

    /**
     * Constructor for NotificationDao.
     * Initializes the UserDao and ReservationDao instances.
     */
    public NotificationDao() {
        this.userDao = new UserDao();
        this.reservationDao = new ReservationDao();
    }

    /**
     * saves a notification to the database.
     * @param notification the notification to be saved
     */
    // SQL query parameters
    private static final int USER_ID_INDEX = 1;
    /**
     * Index for the English message in the SQL query.
     */
    private static final int MESSAGE_EN_INDEX = 2;
    /**
     * Index for the Urdu message in the SQL query.
     */
    private static final int MESSAGE_UR_INDEX = 3;
    /**
     * Index for the Russian message in the SQL query.
     */
    private static final int MESSAGE_RU_INDEX = 4;
    /**
     * Index for the created_at timestamp in the SQL query.
     */
    private static final int CREATED_AT_INDEX = 5;
    /**
     * Index for the reservation ID in the SQL query.
     */
    private static final int RESERVATION_ID_INDEX = 6;

    /**
     * Saves a notification to the database.
     * <p>
     * Subclasses can override this method to provide custom behavior,
     * but they must ensure that the database connection and query
     * execution are handled properly to avoid SQL injection or resource leaks.
     *
     * @param notification the notification to be saved
     */
    public void saveNotification(final Notification notification) {
        String query = "INSERT INTO notifications (user_id, message_en, "
                + "message_ur, message_ru, created_at, reservation_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = BaseDao.getConnection().prepareStatement(query)) {
            stmt.setLong(USER_ID_INDEX,
                    notification.getUser().getUserId());
            stmt.setString(MESSAGE_EN_INDEX,
                    notification.getMessageEnglish());
            stmt.setString(MESSAGE_UR_INDEX,
                    notification.getMessageUrdu());
            stmt.setString(MESSAGE_RU_INDEX,
                    notification.getMessageRussian());
            stmt.setTimestamp(CREATED_AT_INDEX,
                    Timestamp.valueOf(notification.getCreatedAt()));
            stmt.setLong(RESERVATION_ID_INDEX,
                    notification.getReservationId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Retrieves a list of notifications for a specific user.
     * @param userId the ID of the user whose notifications are to be retrieved
     * @return a list of notifications for the specified user
     */
    public List<Notification> getNotificationsByUserId(final Long userId) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notifications WHERE user_id = ?";
        try (PreparedStatement stmt = BaseDao.getConnection().prepareStatement(query)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(mapRowToNotification(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    /**
        maps a ResultSet row to a Notification object.
     @param rs the ResultSet containing the notification data
     @return provides the notification object.
     */
    private Notification mapRowToNotification(final ResultSet rs)
            throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationId(rs.getLong("notification_id"));
        notification.setMessageEnglish(rs.getString("message_en"));
        notification.setMessageUrdu(rs.getString("message_ur"));
        notification.setMessageRussian(rs.getString("message_ru"));
        notification.setCreatedAt(rs.getTimestamp("created_at")
                .toLocalDateTime());

        // Fetch user
        Long userId = rs.getLong("user_id");
        User user = userDao.getUserById(userId);
        notification.setUser(user);

        return notification;
    }

    /**
        creates a notification for a reservation.
        * @param reservationId the ID of the reservation
        * for which the notification is to be created
     */
    public void createNotificationForReservation(final Long reservationId) {
        Reservation reservation = reservationDao
                .getReservationById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException(
                    "Reservation with ID " + reservationId + " not found.");
        }
        User user = reservation.getUser();
        Book book = reservation.getBook();
        String bookTitleEn = book.getTitleEn();
        String bookTitleUr = book.getTitleUr();
        String bookTitleRu = book.getTitleRu();
        String messageEn = "Dear " + user.getUsername()
                + ", you have borrowed the book '" + bookTitleEn
                + "'. Please return it by " + reservation.getDueDate() + ".";
        String messageUr = "محترم " + user.getUsername()
                + ", آپ نے کتاب '" + bookTitleUr
                + "' ادھار لی ہے۔ براہ کرم اسے "
                + reservation.getDueDate() + " تک واپس کریں۔";
        String messageRu = "Уважаемый " + user.getUsername()
                + ", вы взяли книгу '" + bookTitleRu
                + "'. Пожалуйста, верните её до "
                + reservation.getDueDate() + ".";

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessageEnglish(messageEn);
        notification.setMessageUrdu(messageUr);
        notification.setMessageRussian(messageRu);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReservation(reservation);
        saveNotification(notification);
    }
    /**
        deletes all notifications for a specific reservation.
        * @param reservationId the ID of the reservation
     */

    public void deleteNotification(final Long reservationId) {
        String query = "DELETE FROM notifications WHERE reservation_id = ?";
        try (PreparedStatement stmt = BaseDao.getConnection().prepareStatement(query)) {
            stmt.setLong(1, reservationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
        updates the notification message for a specific reservation.
        * @param reservationId the ID of the reservation for
        * which the notification message is to be updated
     */
    public void updateNotification(final Long reservationId) {
        Reservation reservation = this.reservationDao
                .getReservationById(reservationId);
        User user = reservation.getUser();
        Book book = reservation.getBook();

        String messageEn = "Reservation extended. Dear " + user.getUsername()
                + ", you have borrowed the book '"
                + book.getTitle("English")
                + "'. Please return it by " + reservation.getDueDate() + ".";
        String messageUr = "ریزرویشن میں توسیع کی گئی۔ محترم "
                + user.getUsername()
                + ", آپ نے کتاب '" + book.getTitle("اردو")
                + "' ادھار لی ہے۔ براہ کرم اسے " + reservation.getDueDate()
                + " تک واپس کریں۔";
        String messageRu = "Продление бронирования. Уважаемый "
                + user.getUsername()
                + ", вы взяли книгу '"
                + book.getTitle("Русский")
                + "'. Пожалуйста, верните её до "
                + reservation.getDueDate() + ".";

        String query = "UPDATE notifications SET message_en = ?, "
                + "message_ur = ?, "
                + "message_ru = ? "
                + "WHERE reservation_id = ?";
        try (PreparedStatement stmt = BaseDao.getConnection().prepareStatement(query)) {
            stmt.setString(MESSAGE_EN_INDEX, messageEn);
            stmt.setString(MESSAGE_UR_INDEX, messageUr);
            stmt.setString(MESSAGE_RU_INDEX, messageRu);
            stmt.setLong(RESERVATION_ID_INDEX, reservationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
