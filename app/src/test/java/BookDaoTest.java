import model.dao.impl.BookDao;
import model.entity.Author;
import model.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class BookDaoTest {

    private BookDao bookDao;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        bookDao = new BookDao();
        bookDao.setConnection(mockConnection);
    }



    @Test
    public void testGetBookById() throws SQLException {

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("book_id")).thenReturn(1L);
        when(mockResultSet.getString("title")).thenReturn("Title");
        when(mockResultSet.getDate("publication_date")).thenReturn(Date.valueOf("2008-05-08"));
        when(mockResultSet.getString("description")).thenReturn("description");
        when(mockResultSet.getString("availability_status")).thenReturn("Available");
        when(mockResultSet.getString("category")).thenReturn("category");
        when(mockResultSet.getString("language")).thenReturn("Language");
        when(mockResultSet.getString("isbn")).thenReturn("isbn");
        when(mockResultSet.getString("location")).thenReturn("location");
        // Mock the getAuthorsByBookId method
        Set<Author> mockAuthors = new HashSet<>();
        Author author = new Author();
        author.setAuthorId(1L);
        author.setFirstName("John");
        author.setLastName("Doe");
        mockAuthors.add(author);
        BookDao bookDaoSpy = spy(bookDao);
        doReturn(mockAuthors).when(bookDaoSpy).getAuthorsByBookId(1L);

        Book book = bookDaoSpy.getBookById(1L);


        assertNotNull(book);

        assertEquals(1L, book.getBookId());
        assertEquals("Title", book.getTitle());
        assertEquals("Language", book.getLanguage());
        assertEquals("category", book.getCategory());
        assertEquals("Available", book.getAvailabilityStatus());
    }

    @Test
    public void testGetBookByAuthorId() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("book_id")).thenReturn(1L);
        when(mockResultSet.getString("title")).thenReturn("Title");
        when(mockResultSet.getDate("publication_date")).thenReturn(Date.valueOf("2008-05-08"));
        when(mockResultSet.getString("description")).thenReturn("description");
        when(mockResultSet.getString("availability_status")).thenReturn("Available");
        when(mockResultSet.getString("category")).thenReturn("category");
        when(mockResultSet.getString("language")).thenReturn("Language");
        when(mockResultSet.getString("isbn")).thenReturn("isbn");
        when(mockResultSet.getString("location")).thenReturn("location");
        // Mock the getAuthorsByBookId method
        Set<Author> mockAuthors = new HashSet<>();
        Author author = new Author();
        author.setAuthorId(1L);
        author.setFirstName("John");
        author.setLastName("Doe");
        mockAuthors.add(author);
        BookDao bookDaoSpy = spy(bookDao);
        doReturn(mockAuthors).when(bookDaoSpy).getAuthorsByBookId(1L);


        Book book = bookDaoSpy.getBookById(1L);

        assertNotNull(book);

        assertEquals(1L, book.getBookId());

    }

    @Test
    public void testGetBookCount() throws SQLException {
        Statement mockStatement = mock(Statement.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        int count = bookDao.getBookCount();

        assertEquals(1, count);
    }

    @Test
    public void testGetBookByTitle() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("book_id")).thenReturn(1L);
        when(mockResultSet.getString("title")).thenReturn("Title");
        when(mockResultSet.getDate("publication_date")).thenReturn(Date.valueOf("2008-05-08"));
        when(mockResultSet.getString("description")).thenReturn("description");
        when(mockResultSet.getString("availability_status")).thenReturn("Available");
        when(mockResultSet.getString("category")).thenReturn("category");
        when(mockResultSet.getString("language")).thenReturn("Language");
        when(mockResultSet.getString("isbn")).thenReturn("isbn");
        when(mockResultSet.getString("location")).thenReturn("location");
        // Mock the getAuthorsByBookId method
        Set<Author> mockAuthors = new HashSet<>();
        Author author = new Author();
        author.setAuthorId(1L);
        author.setFirstName("John");
        author.setLastName("Doe");
        mockAuthors.add(author);
        BookDao bookDaoSpy = spy(bookDao);
        doReturn(mockAuthors).when(bookDaoSpy).getAuthorsByBookId(1L);

            List<Book> books = bookDaoSpy.getBooksByTitle("Title");
            assertNotNull(books);
            assertEquals(1, books.size());
            assertEquals("Title", books.getFirst().getTitle());

    }

    @Test
    public void testGetBookByCategory() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("book_id")).thenReturn(1L);
        when(mockResultSet.getString("title")).thenReturn("Title");
        when(mockResultSet.getDate("publication_date")).thenReturn(Date.valueOf("2008-05-08"));
        when(mockResultSet.getString("description")).thenReturn("description");
        when(mockResultSet.getString("availability_status")).thenReturn("Available");
        when(mockResultSet.getString("category")).thenReturn("category");
        when(mockResultSet.getString("language")).thenReturn("Language");
        when(mockResultSet.getString("isbn")).thenReturn("isbn");
        when(mockResultSet.getString("location")).thenReturn("location");
        // Mock the getAuthorsByBookId method
        Set<Author> mockAuthors = new HashSet<>();
        Author author = new Author();
        author.setAuthorId(1L);
        author.setFirstName("John");
        author.setLastName("Doe");
        mockAuthors.add(author);
        BookDao bookDaoSpy = spy(bookDao);
        doReturn(mockAuthors).when(bookDaoSpy).getAuthorsByBookId(1L);

        List<Book> books = bookDaoSpy.getBooksByCategory("category");
        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("category", books.getFirst().getCategory());

    }

    @Test
    public void testGetBookByLanguage() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("book_id")).thenReturn(1L);
        when(mockResultSet.getString("title")).thenReturn("Title");
        when(mockResultSet.getDate("publication_date")).thenReturn(Date.valueOf("2008-05-08"));
        when(mockResultSet.getString("description")).thenReturn("description");
        when(mockResultSet.getString("availability_status")).thenReturn("Available");
        when(mockResultSet.getString("category")).thenReturn("category");
        when(mockResultSet.getString("language")).thenReturn("Language");
        when(mockResultSet.getString("isbn")).thenReturn("isbn");
        when(mockResultSet.getString("location")).thenReturn("location");
        // Mock the getAuthorsByBookId method
        Set<Author> mockAuthors = new HashSet<>();
        Author author = new Author();
        author.setAuthorId(1L);
        author.setFirstName("John");
        author.setLastName("Doe");
        mockAuthors.add(author);
        BookDao bookDaoSpy = spy(bookDao);
        doReturn(mockAuthors).when(bookDaoSpy).getAuthorsByBookId(1L);

        List<Book> books = bookDaoSpy.getBooksByLanguage("Language");
        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Language", books.getFirst().getLanguage());
    }

    @Test
    public void testupdateBookAvailabilityStatus() throws SQLException {
        Long bookId = 1L;
        String availabilityStatus = "Available";

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        bookDao.updateBookAvailabilityStatus(bookId, availabilityStatus);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testSearchBooks() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("book_id")).thenReturn(1L);
        when(mockResultSet.getString("title")).thenReturn("Title");
        when(mockResultSet.getDate("publication_date")).thenReturn(Date.valueOf("2008-05-08"));
        when(mockResultSet.getString("description")).thenReturn("description");
        when(mockResultSet.getString("availability_status")).thenReturn("Available");
        when(mockResultSet.getString("category")).thenReturn("category");
        when(mockResultSet.getString("language")).thenReturn("Language");
        when(mockResultSet.getString("isbn")).thenReturn("isbn");
        when(mockResultSet.getString("location")).thenReturn("location");
        // Mock the getAuthorsByBookId method
        Set<Author> mockAuthors = new HashSet<>();
        Author author = new Author();
        author.setAuthorId(1L);
        author.setFirstName("John");
        author.setLastName("Doe");
        mockAuthors.add(author);
        BookDao bookDaoSpy = spy(bookDao);
        doReturn(mockAuthors).when(bookDaoSpy).getAuthorsByBookId(1L);

        List<Book> books = bookDaoSpy.searchBooks("Title");
        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Title", books.getFirst().getTitle());
    }

    @Test
    public void testGetAuthorsByBookId() throws SQLException {
        // Mock the database response
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("author_id")).thenReturn(1L);
        when(mockResultSet.getString("first_name")).thenReturn("John");
        when(mockResultSet.getString("last_name")).thenReturn("Doe");

        Set<Author> authors = bookDao.getAuthorsByBookId(1L);
        assertNotNull(authors);
        assertEquals(1, authors.size());

        Author author = authors.iterator().next();
        assertEquals(1L, author.getAuthorId());
        assertEquals("John", author.getFirstName());
        assertEquals("Doe", author.getLastName());
    }



    @Test
    public void testGetBookByIdSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test SQL Exception"));

        bookDao.getBookById(1L);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void testGetBooksByAuthorIdSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test SQL Exception"));

        bookDao.getBooksByAuthorId(1L);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void testGetBooksByTitleSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test SQL Exception"));

        bookDao.getBooksByTitle("title");

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void testGetBooksByCategorySQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test SQL Exception"));

        bookDao.getBooksByCategory("category");

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void testGetBooksByLanguageSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test SQL Exception"));

        bookDao.getBooksByLanguage("language");

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }


}