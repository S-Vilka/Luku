package model.dao.impl;

import model.entity.Book;
import model.entity.Reservation;
import model.entity.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//
public class ReservationDao {

    /** gets a reservation by its ID.
     * @param reservationId the ID of the reservation to retrieve
     * @return the Reservation object if found, null otherwise
     */
    public Reservation getReservationById(final Long reservationId) {
        String query = "SELECT r.*, u.username, b.* FROM reservations r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "JOIN books b ON r.book_id = b.book_id "
                + "WHERE r.reservation_id = ?";
        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query)) {
            stmt.setLong(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToReservation(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Maps a ResultSet row to a Reservation object.
     *
     * @param rs the ResultSet containing the reservation data
     * @return the mapped Reservation object
     * @throws SQLException if an SQL error occurs
     */
    private Reservation mapRowToReservation(final ResultSet rs)
            throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getLong("reservation_id"));

        User user = new User();
        user.setUserId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        reservation.setUser(user);

        Book book = new Book();
        book.setBookId(rs.getLong("book_id"));
        book.setTitleEn(rs.getString("title_en"));
        book.setTitleUr(rs.getString("title_ur"));
        book.setTitleRu(rs.getString("title_ru"));
        reservation.setBook(book);
        reservation.setBorrowDate(rs.getTimestamp("borrow_date")
                .toLocalDateTime());
        reservation.setDueDate(rs.getTimestamp("due_date")
                .toLocalDateTime());
        return reservation;
    }
    /**
     * Retrieves all reservations from the database.
     *
     * @return a list of all reservations
     */
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.*, u.username, b.* FROM reservations r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "JOIN books b ON r.book_id = b.book_id";
        try (Statement stmt = BaseDao.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                reservations.add(mapRowToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    /**
     * Extends the reservation for a book by updating the due date.
     *
     * @param reservationId the ID of the reservation to extend
     * @param newDueDate the new due date
     */
    public void extendReservation(final Long reservationId,
                                  final LocalDateTime newDueDate) {
        String query = "UPDATE reservations SET due_date = ? "
                + " WHERE reservation_id = ?";
        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(newDueDate));
            stmt.setLong(2, reservationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all reservations made by a specific user.
     *
     * @param userId the ID of the user
     * @return a list of reservations made by the user
     */
    public List<Reservation> getReservationsByUserId(final Long userId) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.*, u.username, b.* FROM reservations r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "JOIN books b ON r.book_id = b.book_id "
                + "WHERE r.user_id = ?";
        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reservations.add(mapRowToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
    /**
     * Default borrow days for a reservation.
     */
    private static final int DEFAULT_BORROW_DAYS = 14;
    /**
     * Borrow Date index in the database.
     */
    private static final int BORROW_DATE_INDEX = 3;
    /**
     * Due Date index in the database.
     */
    private static final int DUE_DATE_INDEX = 4;
    /**
     * Reservation ID index in the database.
     */
    private static final int RESERVATION_ID_INDEX = 5;
    /**
     * Index for the third parameter in the PreparedStatement.
     */
    private static final int THIRD_PARAMETER_INDEX = 3;


    /**
     * Saves a new reservation to the database.
     *
     * @param reservation the Reservation object to save
     * @return the ID of the saved reservation
     */
    public Long saveReservation(final Reservation reservation) {
        String query = "INSERT INTO reservations (user_id, book_id, "
                + "borrow_date, due_date)"
                + " VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            LocalDateTime borrowDate = LocalDateTime.now();
            LocalDateTime dueDate = borrowDate.plusDays(DEFAULT_BORROW_DAYS);
            reservation.setBorrowDate(borrowDate);
            reservation.setDueDate(dueDate);
            stmt.setLong(1, reservation.getUser().getUserId());
            stmt.setLong(2, reservation.getBook().getBookId());
            stmt.setTimestamp(BORROW_DATE_INDEX,
                    Timestamp.valueOf(reservation.getBorrowDate()));
            stmt.setTimestamp(DUE_DATE_INDEX,
                    Timestamp.valueOf(reservation.getDueDate()));
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long reservationId = generatedKeys.getLong(1);
                    reservation.setReservationId(reservationId);
                    System.out.println("Reservation saved "
                            + "successfully with ID: "
                            + reservationId);
                    return reservationId;
                } else {
                    throw new SQLException("Creating reservation failed, "
                            + "no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates an existing reservation in the database.
     *
     * @param reservation the Reservation object with updated data
     */
    public void updateReservation(final Reservation reservation) {
        String query = "UPDATE reservations SET user_id = ?, "
                + "book_id = ?, borrow_date = ?, due_date = ?"
                + " WHERE reservation_id = ?";
        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query)) {
            stmt.setLong(1, reservation.getUser().getUserId());
            stmt.setLong(2, reservation.getBook().getBookId());
            stmt.setTimestamp(BORROW_DATE_INDEX,
                    Timestamp.valueOf(reservation.getBorrowDate()));
            stmt.setTimestamp(DUE_DATE_INDEX,
                    Timestamp.valueOf(reservation.getDueDate()));
            stmt.setLong(RESERVATION_ID_INDEX,
                    reservation.getReservationId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Retrieves a reservation made by a specific user for a specific book.
     *
     * @param userId the ID of the user
     * @param bookId the ID of the book
     * @return the Reservation object if found, null otherwise
     */
    public Reservation getReservationByUserAndBook(final Long userId,
                                                   final Long bookId) {
        String query = "SELECT r.*, u.username, b.* FROM reservations r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "JOIN books b ON r.book_id = b.book_id "
                + "WHERE r.user_id = ? AND r.book_id = ?";

        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToReservation(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Deletes a reservation from the database.
     *
     * @param reservationId the ID of the reservation to delete
     */
    public void deleteReservation(final Long reservationId) {
        String query = "DELETE FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query)) {
            stmt.setLong(1, reservationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Retrieves reservations that are due soon
     * (within the next 14 days) for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of reservations due soon
     */
    public List<Reservation> getReservationsDueSoon(final Long userId) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.*, u.username, b.* FROM reservations r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "JOIN books b ON r.book_id = b.book_id "
                + "WHERE r.user_id = ? AND r.due_date BETWEEN ? AND ?";
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fourteenDaysLater = now.plusDays(DEFAULT_BORROW_DAYS);
        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query)) {
            stmt.setLong(1, userId);
            stmt.setTimestamp(2, Timestamp.valueOf(now));
            stmt.setTimestamp(THIRD_PARAMETER_INDEX,
                    Timestamp.valueOf(fourteenDaysLater));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reservations.add(mapRowToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
}
