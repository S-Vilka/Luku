package service;

import model.entity.Reservation;
import model.dao.impl.ReservationDao;

import java.time.LocalDateTime;
import java.util.List;


public class ReservationService {
    /**
     * ReservationDao is used to interact with the
     * database for reservation-related operations.
     */
    private ReservationDao reservationDao;

    /**
     * Constructor initializes the ReservationDao instance.
     */
    public ReservationService() {
        reservationDao = new ReservationDao();
    }

    /**
     * Retrieves a reservation by its ID.
     *
     * @param reservationId the ID of the reservation to retrieve
     * @return the Reservation object if found, null otherwise
     */
    public Reservation getReservationById(final Long reservationId) {
        return reservationDao.getReservationById(reservationId);
    }

    /**
     * Retrieves all reservations.
     *
     * @return a list of all Reservation objects
     */
    public List<Reservation> getAllReservations() {

        return reservationDao.getAllReservations();
    }

    /**
     * Extend reservation of a book.
     *
     * @param reservationId the ID of the reservation to extend
     * @param newDueDate the new due date for the reservation
     */
    public void extendReservation(final Long reservationId,
                                  final LocalDateTime newDueDate) {
        reservationDao.extendReservation(reservationId, newDueDate);

    }

    /**
     * Updates an existing reservation.
     *
     * @param reservation the Reservation object with updated information
     */
    public void updateReservation(final Reservation reservation) {
        reservationDao.updateReservation(reservation);
    }

    /**
     * Retrieves all reservations for a specific user.
     *
     * @param userId the ID of the user whose reservations to retrieve
     * @return a list of Reservation objects for the specified user
     */
    public List<Reservation> getReservationsByUserId(final Long userId) {
        return reservationDao.getReservationsByUserId(userId);
    }


    /**
     * Creates a new reservation.
     *
     * @param reservation the Reservation object to create
     */
    public void createReservation(final Reservation reservation) {
        reservationDao.saveReservation(reservation);
    }

    /**
     * Retrieves a reservation by user ID and book ID.
     *
     * @param userId the ID of the user
     * @param bookId the ID of the book
     * @return the Reservation object if found, null otherwise
     */
    public Reservation getReservationByUserAndBook(final Long userId,
                                                   final Long bookId) {
        return reservationDao.getReservationByUserAndBook(userId, bookId);
    }

    /**
     * Deletes a reservation by its ID.
     *
     * @param reservationId the ID of the reservation to delete
     */
    public void deleteReservation(final Long reservationId) {
        reservationDao.deleteReservation(reservationId);
    }

    /**
     * Retrieves all reservations that are due soon for a specific user.
     *
     * @param userId the ID of the user whose reservations to check
     * @return a list of Reservation objects that are due soon
     */
    public List<Reservation> getReservationsDueSoon(final Long userId) {
        return reservationDao.getReservationsDueSoon(userId);
    }
}
