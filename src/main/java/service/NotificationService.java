package service;

import model.dao.impl.ReservationDao;
import model.entity.Notification;
import model.dao.impl.NotificationDao;
import model.entity.Reservation;
import model.entity.User;
import model.entity.Book;

import java.util.List;

public class NotificationService {

    private final NotificationDao notificationDao;
    private final ReservationDao reservationDao;


    public NotificationService() {
        this.notificationDao = new NotificationDao();
        this.reservationDao = new ReservationDao();

    }

    public void saveNotification(Notification notification) {
        notificationDao.saveNotification(notification);
    }

    public void createNotificationForReservation(Long reservation) {
         notificationDao.createNotificationForReservation(reservation);


    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationDao.getNotificationsByUserId(userId);
    }

    public void deleteNotificationByReservationId(Long reservation) {
        notificationDao.deleteNotification(reservation);
    }

    public void updateNotification(Long reservationId) {

        notificationDao.updateNotification(reservationId);
    }

    public void createReminderNotification(Reservation reservation) {
        User user = reservation.getUser();
        Book book = reservation.getBook();
        String message = "Reminder: Dear " + user.getUsername() + ", the book '" + book.getTitle() + "' is due tomorrow. Please return it on time.";
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setCreatedAt(java.time.LocalDateTime.now());
        notification.setReservation(reservation);
        saveNotification(notification);
    }

}
