package service;

import model.entity.Reservation;
import model.dao.impl.ReservationDao;
import java.time.LocalDateTime;
import java.util.List;


public class ReservationService {
    private ReservationDao reservationDao;

    public ReservationService() {
        reservationDao = new ReservationDao();
    }

    public Reservation getReservationById(Long reservationId, String currentLanguage) {
        return reservationDao.getReservationById(reservationId, currentLanguage);
    }

    public List<Reservation> getAllReservations(String currentLanguage) {

        return reservationDao.getAllReservations(currentLanguage);
    }

    public void extendReservation(Long reservationId, LocalDateTime newDueDate) {
        reservationDao.extendReservation(reservationId, newDueDate);

    }

    public void updateReservation(Reservation reservation) {
        reservationDao.updateReservation(reservation);
    }

    public List<Reservation> getReservationsByUserId(Long userId, String currentLanguage) {
        return reservationDao.getReservationsByUserId(userId, currentLanguage);
    }


    public void createReservation(Reservation reservation) {
         reservationDao.saveReservation(reservation);
    }

    public Reservation getReservationByUserAndBook(Long userId, Long bookId, String currentLanguage) {
        return reservationDao.getReservationByUserAndBook(userId, bookId, currentLanguage);
    }

    public void deleteReservation(Long reservationId) {
        reservationDao.deleteReservation(reservationId);
    }

    public List<Reservation> getReservationsDueSoon(Long userId, String currentLanguage) {
        return reservationDao.getReservationsDueSoon(userId, currentLanguage);
    }


}
