/*
 * This class is part of the library management system.
 * It handles the creation, retrieval,
 *  and deletion of notifications
 * related to book reservations.
 */
package service;

import model.dao.impl.ReservationDao;
import model.entity.Notification;
import model.dao.impl.NotificationDao;
import model.entity.Reservation;
import model.entity.User;
import model.entity.Book;
import java.util.List;

/**
 * The NotificationService class provides methods to manage notifications
 * related to book reservations. It interacts with the NotificationDao
 * and ReservationDao to perform CRUD operations on notifications.
 */
public class NotificationService {

    /**
     * The NotificationDao instance for database operations.
     */
    private final NotificationDao notificationDao;
    /**
     * The ReservationDao instance for reservation-related operations.
     */
    private final ReservationDao reservationDao;

    /**
     * Constructor for NotificationService.
     * Initializes the NotificationDao and ReservationDao instances.
     */
    public NotificationService() {
        this.notificationDao = new NotificationDao();
        this.reservationDao = new ReservationDao();

    }

    /**
     * Saves a notification to the database.
     * @param notification the notification to be saved
     */
    public void saveNotification(final Notification notification) {
        notificationDao.saveNotification(notification);
    }

    /**
     * Creates a notification for a reservation.
     * @param reservation the ID of the notification to created
     */
    public void createNotificationForReservation(final Long reservation) {
        notificationDao.createNotificationForReservation(reservation);
    }

    /**
     * Retrieves a list of notifications for a specific user.
     * @param userId the ID of the user whose notifications are to be retrieved
     * @return a list of notifications for the specified user
     */
    public List<Notification> getNotificationsByUserId(final Long userId) {
        return notificationDao.getNotificationsByUserId(userId);
    }

    /**
     * Deletes a notification by its ID.
     * @param reservation the ID of the reservation
     * whose notifications are to be deleted
     */
    public void deleteNotificationByReservationId(final Long reservation) {
        notificationDao.deleteNotification(reservation);
    }

    /**
     * Updates the notification status for a specific reservation.
     * @param reservationId the ID of the reservation
     * whose notification status is to be updated
     */
    public void updateNotification(final Long reservationId) {
        notificationDao.updateNotification(reservationId);
    }
    /**
     * Creates a reminder notification for a reservation.
     * @param reservation the reservation for
     * which the reminder notification is to be created
     */
    public void createReminderNotification(final Reservation reservation) {
        User user = reservation.getUser();
        Book book = reservation.getBook();

        String messageEn = "Reminder: Dear " + user.getUsername()
                + ", the book '" + book.getTitleEn()
                + "' is due tomorrow. Please return it on time.";
        String messageUr = "یاد دہانی: محترم " + user.getUsername()
                + ", کتاب '" + book.getTitleUr()
                + "' کل واپس کرنی ہے۔ براہ کرم وقت پر واپس کریں۔";
        String messageRu = "Напоминание: Уважаемый " + user.getUsername()
                + ", книга '" + book.getTitleRu()
                + "' должна быть возвращена завтра."
                + " Пожалуйста, верните её вовремя.";

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessageEnglish(messageEn);
        notification.setMessageUrdu(messageUr);
        notification.setMessageRussian(messageRu);
        notification.setCreatedAt(java.time.LocalDateTime.now());
        notification.setReservation(reservation);
        saveNotification(notification);
    }

}
