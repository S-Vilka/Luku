package model.dao.impl;

import model.entity.Notification;
import model.entity.Reservation;
import model.entity.User;
import model.entity.Book;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDao extends BaseDao {
    private UserDao userDao;
    private ReservationDao reservationDao;

    public NotificationDao() {
        this.userDao = new UserDao();
        this.reservationDao = new ReservationDao();
    }


    public void saveNotification(Notification notification) {
        String query = "INSERT INTO notifications (user_id, message_en, message_ur, message_ru, created_at, reservation_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, notification.getUser().getUserId());
            stmt.setString(2, notification.getMessageEnglish());
            stmt.setString(3, notification.getMessageUrdu());
            stmt.setString(4, notification.getMessageRussian());
            stmt.setTimestamp(5, Timestamp.valueOf(notification.getCreatedAt()));
            stmt.setLong(6, notification.getReservationId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notifications WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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

    private Notification mapRowToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationId(rs.getLong("notification_id"));
        notification.setMessageEnglish(rs.getString("message_en"));
        notification.setMessageUrdu(rs.getString("message_ur"));
        notification.setMessageRussian(rs.getString("message_ru"));
        notification.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        // Fetch user
        Long userId = rs.getLong("user_id");
//        UserDao userDao = new UserDao();
        User user = userDao.getUserById(userId);
        notification.setUser(user);

        return notification;
    }

    public void createNotificationForReservation(Long reservationId, String currentLanguage) {
//        ReservationDao reservationDao = new ReservationDao();
        Reservation reservation = reservationDao.getReservationById(reservationId, currentLanguage);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation with ID " + reservationId + " not found.");
        }
        User user = reservation.getUser();
        Book book = reservation.getBook();
//        String bookTitle = book.getTitle(currentLanguage);
//        String message_en = "Dear " + user.getUsername() + ", you have borrowed the book '" + bookTitle +
//                "'. Please return it by " + reservation.getDueDate() + ".";
//        String message_ur = "محترم " + user.getUsername() + ", آپ نے کتاب '" + bookTitle +
//                "' ادھار لی ہے۔ براہ کرم اسے " + reservation.getDueDate() + " تک واپس کریں۔";
//        String message_ru = "Уважаемый " + user.getUsername() + ", вы взяли книгу '" + bookTitle +
//                "'. Пожалуйста, верните её до " + reservation.getDueDate() + ".";
        String bookTitleEn = book.getTitleEn();
        String bookTitleUr = book.getTitleUr();
        String bookTitleRu = book.getTitleRu();
        String message_en = "Dear " + user.getUsername() + ", you have borrowed the book '" + bookTitleEn +
                "'. Please return it by " + reservation.getDueDate() + ".";
        String message_ur = "محترم " + user.getUsername() + ", آپ نے کتاب '" + bookTitleUr +
                "' ادھار لی ہے۔ براہ کرم اسے " + reservation.getDueDate() + " تک واپس کریں۔";
        String message_ru = "Уважаемый " + user.getUsername() + ", вы взяли книгу '" + bookTitleRu +
                "'. Пожалуйста, верните её до " + reservation.getDueDate() + ".";

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessageEnglish(message_en);
        notification.setMessageUrdu(message_ur);
        notification.setMessageRussian(message_ru);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReservation(reservation);
        saveNotification(notification);
    }

    public void deleteNotification(Long reservationId) {
        String query = "DELETE FROM notifications WHERE reservation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, reservationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNotification(Long reservationId, String currentLanguage) {
        ReservationDao reservationDao = new ReservationDao();
        Reservation reservation = reservationDao.getReservationById(reservationId, currentLanguage);
        User user = reservation.getUser();
        Book book = reservation.getBook();

        String messageEn = "Reservation extended. Dear " + user.getUsername() + ", you have borrowed the book '" + book.getTitle("English") +
                "'. Please return it by " + reservation.getDueDate() + ".";
        String messageUr = "ریزرویشن میں توسیع کی گئی۔ محترم " + user.getUsername() + ", آپ نے کتاب '" + book.getTitle("اردو") +
                "' ادھار لی ہے۔ براہ کرم اسے " + reservation.getDueDate() + " تک واپس کریں۔";
        String messageRu = "Продление бронирования. Уважаемый " + user.getUsername() + ", вы взяли книгу '" + book.getTitle("Русский") +
                "'. Пожалуйста, верните её до " + reservation.getDueDate() + ".";

        String query = "UPDATE notifications SET message_en = ?, message_ur = ?, message_ru = ? WHERE reservation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, messageEn);
            stmt.setString(2,messageUr);
            stmt.setString(3, messageRu);
            stmt.setLong(4, reservationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}