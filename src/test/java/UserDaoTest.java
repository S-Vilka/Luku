import model.dao.impl.UserDao;
import model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class UserDaoTest {

    private UserDao userDao;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        userDao = new UserDao();
        userDao.setConnection(mockConnection);
    }


    @Test
    public void getUserById() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mock(ResultSet.class));

        userDao.getUserById(1L);
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void getUserByEmail() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mock(ResultSet.class));

        userDao.getUserByEmail("testUser@gmail.com");
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void testSaveUser() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setPhone("1234567890");
        user.setRole("USER");
        user.setBookCount(0);

        userDao.saveUser(user);
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void UpdateUser() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        User user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setPhone("1234567890");
        user.setRole("USER");
        user.setBookCount(0);

        userDao.updateUser(user);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testGetUserBookCount() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mock(ResultSet.class));

        userDao.getUserBookCount(1L);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void testDecreaseUserBookCount() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        userDao.decreaseUserBookCount(1L);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testGetUserRole() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mock(ResultSet.class));

        userDao.getUserRole(1L);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void getUserPhone() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mock(ResultSet.class));

        userDao.getUserPhone("test@example.com");

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }



    @Test
    public void testSaveUserSQLException() throws SQLException {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setPhone("1234567890");
        user.setRole("USER");
        user.setBookCount(0);

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Test SQL Exception"));

        userDao.saveUser(user);

        verify(mockConnection).prepareStatement(anyString());
    }

    @Test
    public void testUpdateUser() throws SQLException {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setPhone("1234567890");
        user.setRole("USER");
        user.setBookCount(0);

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Test SQL Exception"));

        userDao.updateUser(user);
        verify(mockConnection).prepareStatement(anyString());
    }

    @Test
    public void testGetUserByEmail() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test SQL Exception"));

        userDao.getUserByEmail("test@example.com");

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void testGetUserById() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test SQL Exception"));

        userDao.getUserById(1L);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void GetUserBookCount() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test SQL Exception"));

        userDao.getUserBookCount(1L);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }
}