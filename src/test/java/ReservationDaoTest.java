import model.dao.impl.ReservationDao;
import model.entity.Book;
import model.entity.Reservation;
import model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

public class ReservationDaoTest {

    private ReservationDao reservationDao;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        reservationDao = new ReservationDao();
        reservationDao.setConnection(mockConnection);
    }

    @Test
    public void testGetReservationById() throws SQLException {
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getLong("reservation_id")).thenReturn(1L);
            when(mockResultSet.getLong("user_id")).thenReturn(1L);
            when(mockResultSet.getLong("book_id")).thenReturn(1L);
            when(mockResultSet.getTimestamp("borrow_date")).thenReturn(java.sql.Timestamp.valueOf(LocalDateTime.now()));
            when(mockResultSet.getTimestamp("due_date")).thenReturn(java.sql.Timestamp.valueOf(LocalDateTime.now()));

            Reservation reservation = reservationDao.getReservationById(1L, "English");

            assertNotNull(reservation);
            assertEquals(1L, reservation.getReservationId());
            assertEquals(1L, reservation.getUser().getUserId());
            assertEquals(1L, reservation.getBook().getBookId());
            assertNotNull(reservation.getBorrowDate());
            assertNotNull(reservation.getDueDate());

            verify(mockConnection).prepareStatement(anyString());
            verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void testGetAllReservations() throws SQLException {
        Statement mockStatement = mock(Statement.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("reservation_id")).thenReturn(1L);
        when(mockResultSet.getLong("user_id")).thenReturn(1L);
        when(mockResultSet.getLong("book_id")).thenReturn(1L);
        when(mockResultSet.getTimestamp("borrow_date")).thenReturn(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        when(mockResultSet.getTimestamp("due_date")).thenReturn(java.sql.Timestamp.valueOf(LocalDateTime.now()));

        List<Reservation> reservations = reservationDao.getAllReservations("English");

        assertNotNull(reservations);
        assertEquals(1, reservations.size());
        assertEquals(1L, reservations.getFirst().getReservationId());
        assertEquals(1L, reservations.getFirst().getUser().getUserId());
        assertEquals(1L, reservations.getFirst().getBook().getBookId());
        assertNotNull(reservations.getFirst().getBorrowDate());
        assertNotNull(reservations.getFirst().getDueDate());

        verify(mockConnection).createStatement();
        verify(mockStatement).executeQuery(anyString());
    }

    @Test
    public void testExtendReservation() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        reservationDao.extendReservation(1L, LocalDateTime.now());

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testGetReservationsByUserId() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("reservation_id")).thenReturn(1L);
        when(mockResultSet.getLong("user_id")).thenReturn(1L);
        when(mockResultSet.getLong("book_id")).thenReturn(1L);
        when(mockResultSet.getTimestamp("borrow_date")).thenReturn(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        when(mockResultSet.getTimestamp("due_date")).thenReturn(java.sql.Timestamp.valueOf(LocalDateTime.now()));

        List<Reservation> reservations = reservationDao.getReservationsByUserId(1L, "English");

        assertNotNull(reservations);
        assertEquals(1, reservations.size());
    }

    @Test
    public void testGetReservationByIdSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test SQL Exception"));

        reservationDao.getReservationById(1L, "English");

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public  void testSaveReservation() throws SQLException {
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(1L);

        User user = new User();
        user.setUserId(1L); // Ensure userId is set

        Book book = new Book();
        book.setBookId(1L); // Ensure bookId is set

        Reservation reservation = new Reservation();
        reservation.setUser(user); // Set the user with a valid userId
        reservation.setBook(book); // Set the book with a valid bookId

        Long reservationId = reservationDao.saveReservation(reservation);

        assertNotNull(reservationId);
        assertEquals(1L, reservationId);

        verify(mockConnection).prepareStatement(anyString(), anyInt());
        verify(mockPreparedStatement).executeUpdate();
        verify(mockPreparedStatement).getGeneratedKeys();
        verify(mockResultSet).next();
        verify(mockResultSet).getLong(1);
    }

    @Test
    public void testUpdateReservation() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        User user = new User();
        user.setUserId(1L); // Ensure userId is set


        Book book = new Book();
        book.setBookId(1L); // Ensure bookId is set

        Reservation reservation = new Reservation();
        reservation.setReservationId(1L); // Ensure reservationId is set
        reservation.setUser(user); // Set the user with a valid userId
        reservation.setBook(book); // Set the book with a valid bookId
        reservation.setBorrowDate(LocalDateTime.now()); // Set borrowDate
        reservation.setDueDate(LocalDateTime.now().plusDays(14)); // Set dueDate

        reservationDao.updateReservation(reservation);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeUpdate();

    }

    @Test
    public void testDeleteReservation() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        reservationDao.deleteReservation(1L);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeUpdate();
    }


    @Test
    public void testExtendReservationSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        doThrow(new SQLException("Test SQL Exception")).when(mockPreparedStatement).executeUpdate();

        reservationDao.extendReservation(1L, LocalDateTime.now());

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeUpdate();
    }


    @Test
    public void testGetReservationsByUserIdSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test SQL Exception"));

        reservationDao.getReservationsByUserId(1L, "English");

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void testGetReservationByUserAndBookSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test SQL Exception"));

        reservationDao.getReservationByUserAndBook(1L, 1L, "English");

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void testSaveReservationSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
        doThrow(new SQLException("Test SQL Exception")).when(mockPreparedStatement).executeUpdate();

        User user = new User();
        user.setUserId(1L); // Ensure userId is set

        Book book = new Book();
        book.setBookId(1L); // Ensure bookId is set

        Reservation reservation = new Reservation();
        reservation.setUser(user); // Set the user with a valid userId
        reservation.setBook(book); // Set the book with a valid bookId

        reservationDao.saveReservation(reservation);

        verify(mockConnection).prepareStatement(anyString(), anyInt());
        verify(mockPreparedStatement).executeUpdate();
    }
}