package service;

import model.dao.impl.ReservationDao;
import model.entity.Notification;
import model.dao.impl.NotificationDao;

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

}
