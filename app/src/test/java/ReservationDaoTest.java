import model.dao.impl.ReservationDao;
import model.entity.Book;
import model.entity.Reservation;
import model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

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
    public void testGetReservationByIdSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test SQL Exception"));

        reservationDao.getReservationById(1L);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
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

        reservationDao.getReservationsByUserId(1L);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void testGetReservationByUserAndBookSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test SQL Exception"));

        reservationDao.getReservationByUserAndBook(1L, 1L);

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