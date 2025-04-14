import model.dao.impl.AuthorDao;
import model.dao.impl.BaseDao;
import model.entity.Author;
import model.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthorDaoTest {

    private AuthorDao authorDao;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        BaseDao.setConnection(mockConnection);
        authorDao = new AuthorDao();
//        authorDao.setConnection(mockConnection);
    }

    @Test
    public void testGetAuthorById() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("author_id")).thenReturn(1L);
        when(mockResultSet.getString("first_name")).thenReturn("John");
        when(mockResultSet.getString("last_name")).thenReturn("Doe");
        when(mockResultSet.getString("description")).thenReturn("A famous author");
        when(mockResultSet.getDate("date_of_birth")).thenReturn(Date.valueOf(LocalDate.of(1970, 1, 1)));
        when(mockResultSet.getString("place_of_birth")).thenReturn("New York");

        Author author = authorDao.getAuthorById(1L);

        assertNotNull(author);
        assertEquals(1L, author.getAuthorId());
        assertEquals("John", author.getFirstName());
        assertEquals("Doe", author.getLastName());
        assertEquals("A famous author", author.getDescription());
        assertEquals(LocalDate.of(1970, 1, 1), author.getDateOfBirth());
        assertEquals("New York", author.getPlaceOfBirth());
    }

    @Test
    public void testGetAllAuthors() throws SQLException {
        when(mockConnection.createStatement()).thenReturn(mock(Statement.class));
        when(mockConnection.createStatement().executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getLong("author_id")).thenReturn(1L, 2L);
        when(mockResultSet.getString("first_name")).thenReturn("John", "Jane");
        when(mockResultSet.getString("last_name")).thenReturn("Doe", "Austin");
        when(mockResultSet.getString("description")).thenReturn("A famous author", "A very famous author");
        when(mockResultSet.getDate("date_of_birth")).thenReturn(Date.valueOf(LocalDate.of(1970, 1, 1)), Date.valueOf(LocalDate.of(1970, 1, 1)));
        when(mockResultSet.getString("place_of_birth")).thenReturn("New York", "London");

        List<Author> authors = authorDao.getAllAuthors();

        assertNotNull(authors);
        assertEquals(2, authors.size());
        assertEquals("John", authors.get(0).getFirstName());
        assertEquals("Jane", authors.get(1).getFirstName());
    }

    @Test
    public void testSaveAuthor() throws SQLException {
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(1L);

        Author author = new Author();
        author.setFirstName("Jane");
        author.setLastName("Smith");
        author.setDescription("Another famous author");
        author.setDateOfBirth(LocalDate.of(1980, 2, 2));
        author.setPlaceOfBirth("Los Angeles");

        authorDao.saveAuthor(author);

        assertEquals(1L, author.getAuthorId());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateAuthor() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        Author author = new Author();
        author.setAuthorId(1L);
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setDescription("Updated description");
        author.setDateOfBirth(LocalDate.of(1970, 1, 1));
        author.setPlaceOfBirth("New York");

        authorDao.updateAuthor(author);

        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testDeleteAuthor() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        authorDao.deleteAuthor(1L);

        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testGetBooksByAuthorName() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getLong("book_id")).thenReturn(1L, 2L);
        when(mockResultSet.getString("title_en")).thenReturn("Effective Java", "Clean Code");
        when(mockResultSet.getString("title_ur")).thenReturn("موثر جاوا", "صاف کوڈ");
        when(mockResultSet.getString("title_ru")).thenReturn("Эффективная Java", "Чистый код");
        when(mockResultSet.getString("category")).thenReturn("Programming", "Programming");
        when(mockResultSet.getDate("publication_date")).thenReturn(Date.valueOf(LocalDate.of(2008, 5, 8)), Date.valueOf(LocalDate.of(2008, 8, 1)));
        when(mockResultSet.getString("description")).thenReturn("A comprehensive guide to best practices in Java programming", "A handbook of agile software craftsmanship");
        when(mockResultSet.getString("availability_status")).thenReturn("Available", "Available");
        when(mockResultSet.getString("language")).thenReturn("English", "English");
        when(mockResultSet.getString("isbn")).thenReturn("978-0134685991", "978-0132350884");
        when(mockResultSet.getString("location")).thenReturn("Aisle 3", "Aisle 4");

        String currentLanguage = "English";
        List<Book> books = authorDao.getBooksByAuthorName("John", "Doe", "English");

        assertNotNull(books);
        assertEquals(2, books.size());
        assertEquals("Effective Java", books.get(0).getTitle(currentLanguage));
        assertEquals("Clean Code", books.get(1).getTitle(currentLanguage));
    }


    @Test
    public void testGetAuthorByIdSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        Author author = authorDao.getAuthorById(1L);

        assertNull(author);
    }

    @Test
    public void testGetAllAuthorsSQLException() throws SQLException {
        when(mockConnection.createStatement()).thenThrow(new SQLException("Database error"));

        List<Author> authors = authorDao.getAllAuthors();

        assertTrue(authors.isEmpty());
    }

    @Test
    public void testSaveAuthorSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenThrow(new SQLException("Database error"));

        Author author = new Author();
        author.setFirstName("Jane");
        author.setLastName("Smith");
        author.setDescription("Another famous author");
        author.setDateOfBirth(LocalDate.of(1980, 2, 2));
        author.setPlaceOfBirth("Los Angeles");

        authorDao.saveAuthor(author);

        assertNull(author.getAuthorId());
    }

    @Test
    public void testUpdateAuthorSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        Author author = new Author();
        author.setAuthorId(1L);
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setDescription("Updated description");
        author.setDateOfBirth(LocalDate.of(1970, 1, 1));
        author.setPlaceOfBirth("New York");

        authorDao.updateAuthor(author);

        // No exception should be thrown, but the update should fail silently
    }

    @Test
    public void testDeleteAuthorSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        authorDao.deleteAuthor(1L);

        // No exception should be thrown, but the delete should fail silently
    }

    @Test
    public void testGetBooksByAuthorNameSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        List<Book> books = authorDao.getBooksByAuthorName("John", "Doe", "English");

        assertTrue(books.isEmpty());
    }
}
