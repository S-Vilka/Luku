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

    public Reservation getReservationById(Long reservationId, String currentLanguage) {
        String titleColumn;
        switch (currentLanguage) {
            case "اردو":
                titleColumn = "b.title_ur";
                break;
            case "Русский":
                titleColumn = "b.title_ru";
                break;
            case "english":
            default:
                titleColumn = "b.title_en";
                break;
        }

        String query = "SELECT r.*, u.username, " + titleColumn + " AS title FROM reservations r " +
                "JOIN users u ON r.user_id = u.user_id " +
                "JOIN books b ON r.book_id = b.book_id " +
                "WHERE r.reservation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToReservation(rs, currentLanguage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Reservation mapRowToReservation(ResultSet rs, String currentLanguage) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getLong("reservation_id"));

        User user = new User();
        user.setUserId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        reservation.setUser(user);

        Book book = new Book();
        book.setBookId(rs.getLong("book_id"));
        switch (currentLanguage) {
            case "Русский":
                book.setTitleRu(rs.getString("title"));
                break;
            case "اردو":
                book.setTitleUr(rs.getString("title"));
                break;
            case "english":
            default:
                book.setTitleEn(rs.getString("title"));
                break;
        }
        reservation.setBook(book);
        reservation.setBorrowDate(rs.getTimestamp("borrow_date").toLocalDateTime());
        reservation.setDueDate(rs.getTimestamp("due_date").toLocalDateTime());

        return reservation;
    }

    public List<Reservation> getAllReservations(String currentLanguage) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.*, u.username, b.title FROM reservations r " +
                "JOIN users u ON r.user_id = u.user_id " +
                "JOIN books b ON r.book_id = b.book_id";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                reservations.add(mapRowToReservation(rs, currentLanguage));
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


    public List<Reservation> getReservationsByUserId(Long userId, String currentLanguage) {
        List<Reservation> reservations = new ArrayList<>();
        String titleColumn;
        switch (currentLanguage) {
            case "اردو":
                titleColumn = "b.title_ur";
                break;
            case "Русский":
                titleColumn = "b.title_ru";
                break;
            case "english":
            default:
                titleColumn = "b.title_en";
                break;
        }
        String query = "SELECT r.*, u.username, " + titleColumn + " AS title FROM reservations r " +
                "JOIN users u ON r.user_id = u.user_id " +
                "JOIN books b ON r.book_id = b.book_id " +
                "WHERE r.user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reservations.add(mapRowToReservation(rs, currentLanguage));
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


    public Reservation getReservationByUserAndBook(Long userId, Long bookId, String currentLanguage) {

        String titleColumn;
        switch (currentLanguage) {
            case "اردو":
                titleColumn = "b.title_ur";
                break;
            case "Русский":
                titleColumn = "b.title_ru";
                break;
            case "english":
            default:
                titleColumn = "b.title_en";
                break;
        }

        String query = "SELECT r.*, u.username, " + titleColumn + " AS title FROM reservations r " +
                "JOIN users u ON r.user_id = u.user_id " +
                "JOIN books b ON r.book_id = b.book_id " +
                "WHERE r.user_id = ? AND r.book_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToReservation(rs, currentLanguage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    public List<Reservation> getReservationsDueSoon(Long userId, String currentLanguage) {
        String titleColumn;
        switch (currentLanguage) {
            case "اردو":
                titleColumn = "b.title_ur";
                break;
            case "Русский":
                titleColumn = "b.title_ru";
                break;
            case "english":
            default:
                titleColumn = "b.title_en";
                break;
        }

        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.*, u.username, " + titleColumn + " AS title FROM reservations r " +
                "JOIN users u ON r.user_id = u.user_id " +
                "JOIN books b ON r.book_id = b.book_id " +
                "WHERE r.user_id = ? AND r.due_date BETWEEN ? AND ?";
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fourteenDaysLater = now.plusDays(14);
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, userId);
            stmt.setTimestamp(2, Timestamp.valueOf(now));
            stmt.setTimestamp(3, Timestamp.valueOf(fourteenDaysLater));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reservations.add(mapRowToReservation(rs, currentLanguage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
}