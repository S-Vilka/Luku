package model.dao.impl;
import model.entity.Book;
import model.entity.Reservation;
import model.entity.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//
public class ReservationDao extends BaseDao{

//    public Reservation getReservationById(Long reservationId) {
//        String query = "SELECT * FROM reservations WHERE reservation_id = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setLong(1, reservationId);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                return mapRowToReservation(rs);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


        public Reservation getReservationById(Long reservationId) {
            String query = "SELECT r.*, u.*, b.* FROM reservations r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "JOIN books b ON r.book_id = b.book_id " +
                    "WHERE r.reservation_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
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

        private Reservation mapRowToReservation(ResultSet rs) throws SQLException {
            Reservation reservation = new Reservation();
            reservation.setReservationId(rs.getLong("reservation_id"));

            User user = new User();
            user.setUserId(rs.getLong("user_id"));
//            user.setUsername(rs.getString("username"));
//            user.setEmail(rs.getString("email"));
            // Set other user fields as needed
            reservation.setUser(user);

            Book book = new Book();
            book.setBookId(rs.getLong("book_id"));
//            book.setTitle(rs.getString("title"));
            // Set other book fields as needed
            reservation.setBook(book);

            reservation.setBorrowDate(rs.getTimestamp("borrow_date").toLocalDateTime());
            reservation.setDueDate(rs.getTimestamp("due_date").toLocalDateTime());

            return reservation;
        }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                reservations.add(mapRowToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public void extendReservation(Long reservationId, LocalDateTime newDueDate) {
        String query = "UPDATE reservations SET due_date = ? WHERE reservation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(newDueDate));
            stmt.setLong(2, reservationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Reservation> getReservationsByUserId(Long userId) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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

    public Long saveReservation(Reservation reservation) {
        String query = "INSERT INTO reservations (user_id, book_id, borrow_date, due_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            LocalDateTime borrowDate = LocalDateTime.now();
            LocalDateTime dueDate = borrowDate.plusDays(14);
            reservation.setBorrowDate(borrowDate);
            reservation.setDueDate(dueDate);

            stmt.setLong(1, reservation.getUser().getUserId());
            stmt.setLong(2, reservation.getBook().getBookId());
            stmt.setTimestamp(3, Timestamp.valueOf(reservation.getBorrowDate()));
            stmt.setTimestamp(4, Timestamp.valueOf(reservation.getDueDate()));
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long reservationId = generatedKeys.getLong(1);
                    reservation.setReservationId(reservationId);
                    System.out.println("Reservation saved successfully with ID: " + reservationId);
                    return reservationId;
                } else {
                    throw new SQLException("Creating reservation failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateReservation(Reservation reservation) {
        String query = "UPDATE reservations SET user_id = ?, book_id = ?, borrow_date = ?, due_date = ? WHERE reservation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, reservation.getUser().getUserId());
            stmt.setLong(2, reservation.getBook().getBookId());
            stmt.setTimestamp(3, Timestamp.valueOf(reservation.getBorrowDate()));
            stmt.setTimestamp(4, Timestamp.valueOf(reservation.getDueDate()));
            stmt.setLong(5, reservation.getReservationId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReservation(Long reservationId) {
        String query = "DELETE FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, reservationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



//    private Reservation mapRowToReservation(ResultSet rs) throws SQLException {
//        Reservation reservation = new Reservation();
//        reservation.setReservationId(rs.getLong("reservation_id"));
//        reservation.setUser(new User(rs.getLong("user_id"))); // Assuming User has a constructor that accepts userId
//        reservation.setBook(new Book(rs.getLong("book_id"))); // Assuming Book has a constructor that accepts bookId
//        reservation.setBorrowDate(rs.getTimestamp("borrow_date").toLocalDateTime());
//        reservation.setDueDate(rs.getTimestamp("due_date").toLocalDateTime());
//        return reservation;
//    }

    public Reservation getReservationByUserAndBook(Long userId, Long bookId) {
        String query = "SELECT * FROM reservations WHERE user_id = ? AND book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
}