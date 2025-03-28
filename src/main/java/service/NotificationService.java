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

    public void createNotificationForReservation(Long reservation, String currentLanguage) {
         notificationDao.createNotificationForReservation(reservation, currentLanguage);


    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationDao.getNotificationsByUserId(userId);
    }

    public void deleteNotificationByReservationId(Long reservation) {
        notificationDao.deleteNotification(reservation);
    }

    public void updateNotification(Long reservationId, String currentLanguage) {

        notificationDao.updateNotification(reservationId, currentLanguage);
    }

    public void createReminderNotification(Reservation reservation, String currentLanguage) {
        User user = reservation.getUser();
        Book book = reservation.getBook();

        String messageEn = "Reminder: Dear " + user.getUsername() + ", the book '" + book.getTitle(currentLanguage) + "' is due tomorrow. Please return it on time.";
        String messageUr = "یاد دہانی: محترم " + user.getUsername() + ", کتاب '" + book.getTitle(currentLanguage) + "' کل واپس کرنی ہے۔ براہ کرم وقت پر واپس کریں۔";
        String messageRu = "Напоминание: Уважаемый " + user.getUsername() + ", книга '" + book.getTitle(currentLanguage) + "' должна быть возвращена завтра. Пожалуйста, верните её вовремя.";

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
