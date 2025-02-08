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

    public void saveNotification(Notification notification) {
        String query = "INSERT INTO notifications (user_id, message, created_at, reservation_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, notification.getUser().getUserId());
            stmt.setString(2, notification.getMessage());
            stmt.setTimestamp(3, Timestamp.valueOf(notification.getCreatedAt()));
            stmt.setLong(4, notification.getReservationId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notification WHERE user_id = ?";
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
        notification.setMessage(rs.getString("message"));
        notification.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        // Fetch user
        Long userId = rs.getLong("user_id");
        UserDao userDao = new UserDao();
        User user = userDao.getUserById(userId);
        notification.setUser(user);

        return notification;
    }

    public void createNotificationForReservation(Long reservationId) {
        ReservationDao reservationDao = new ReservationDao();
        Reservation reservation = reservationDao.getReservationById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation with ID " + reservationId + " not found.");
        }
        User user = reservation.getUser();
        Book book = reservation.getBook();
        String message = "Dear " + user.getUsername() + ", you have borrowed the book '" + book.getTitle() +
                "'. Please return it by " + reservation.getDueDate() + ".";
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReservation(reservation);
        saveNotification(notification);

        // Update reservation with notification ID
//        reservation.setNotificationId(notification.getNotificationId());
//        reservationDao.updateReservation(reservation);

    }

    public Long getLastInsertedNotificationId() {
        String query = "SELECT LAST_INSERT_ID()";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}