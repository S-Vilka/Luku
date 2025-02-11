import model.dao.impl.*;
import model.entity.Writes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import service.WritesService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
public class WritesServiceTest {
    @Container
    private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.5.9")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private WritesService writesService;
    private WritesDao writesDao;
    private AuthorDao authorDao;
    private BookDao bookDao;

    @BeforeEach
    public void setUp() throws SQLException {
        mariaDBContainer.start();
        Connection connection = DriverManager.getConnection(mariaDBContainer.getJdbcUrl(), mariaDBContainer.getUsername(), mariaDBContainer.getPassword());
        BaseDao.setConnection(connection);
        writesService = new WritesService();
        writesDao = new WritesDao();
        authorDao = new AuthorDao();
        bookDao = new BookDao();

        // Drop and create tables
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS writes");
            stmt.execute("DROP TABLE IF EXISTS authors");
            stmt.execute("DROP TABLE IF EXISTS books");

            stmt.execute("CREATE TABLE authors (" +
                    "author_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "first_name VARCHAR(255), " +
                    "last_name VARCHAR(255), " +
                    "description TEXT, " +
                    "date_of_birth DATE, " +
                    "place_of_birth VARCHAR(255))");

            stmt.execute("CREATE TABLE books (" +
                    "book_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(255), " +
                    "publication_date DATE, " +
                    "description TEXT, " +
                    "availability_status VARCHAR(255), " +
                    "category VARCHAR(255), " +
                    "language VARCHAR(255), " +
                    "isbn VARCHAR(255), " +
                    "location VARCHAR(255))");

            stmt.execute("CREATE TABLE writes (" +
                    "writes_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "author_id BIGINT, " +
                    "book_id BIGINT, " +
                    "FOREIGN KEY (author_id) REFERENCES authors(author_id), " +
                    "FOREIGN KEY (book_id) REFERENCES books(book_id))");
        }

        // Insert test data
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO authors (first_name, last_name, description, date_of_birth, place_of_birth) VALUES ('AuthorFirstName', 'AuthorLastName', 'AuthorDescription', '1970-01-01', 'AuthorPlaceOfBirth')");
            stmt.execute("INSERT INTO books (title, publication_date, description, availability_status, category, language, isbn, location) VALUES ('BookTitle', '2000-01-01', 'BookDescription', 'Available', 'Category', 'Language', 'ISBN', 'Location')");
            stmt.execute("INSERT INTO writes (author_id, book_id) VALUES (1, 1)");
        }
    }

    @Test
    public void testGetWritesById() {
        Writes writes = writesService.getWritesById(1L);
        assertEquals(1L, writes.getWritesId());
        assertEquals("AuthorFirstName", writes.getAuthor().getFirstName());
        assertEquals("AuthorLastName", writes.getAuthor().getLastName());
        assertEquals("AuthorDescription", writes.getAuthor().getDescription());
        assertEquals("1970-01-01", writes.getAuthor().getDateOfBirth().toString());
        assertEquals("AuthorPlaceOfBirth", writes.getAuthor().getPlaceOfBirth());
        assertEquals("BookTitle", writes.getBook().getTitle());
        assertEquals("2000-01-01", writes.getBook().getPublicationDate().toString());
        assertEquals("BookDescription", writes.getBook().getDescription());
        assertEquals("Available", writes.getBook().getAvailabilityStatus());
        assertEquals("Category", writes.getBook().getCategory());
        assertEquals("Language", writes.getBook().getLanguage());
        assertEquals("ISBN", writes.getBook().getIsbn());
        assertEquals("Location", writes.getBook().getLocation());
    }

    @Test
    public void testGetWritesByBookId() {
        List<Writes> writesList = writesService.getWritesByBookId(1L);
        assertNotNull(writesList);
        assertEquals(1, writesList.size());
        Writes writes = writesList.get(0);
        assertEquals(1L, writes.getWritesId());
        assertEquals("AuthorFirstName", writes.getAuthor().getFirstName());
        assertEquals("AuthorLastName", writes.getAuthor().getLastName());
        assertEquals("AuthorDescription", writes.getAuthor().getDescription());
        assertEquals("1970-01-01", writes.getAuthor().getDateOfBirth().toString());
        assertEquals("AuthorPlaceOfBirth", writes.getAuthor().getPlaceOfBirth());
        assertEquals("BookTitle", writes.getBook().getTitle());
        assertEquals("2000-01-01", writes.getBook().getPublicationDate().toString());
        assertEquals("BookDescription", writes.getBook().getDescription());
        assertEquals("Available", writes.getBook().getAvailabilityStatus());
        assertEquals("Category", writes.getBook().getCategory());
        assertEquals("Language", writes.getBook().getLanguage());
        assertEquals("ISBN", writes.getBook().getIsbn());
        assertEquals("Location", writes.getBook().getLocation());
    }

    @Test
    public void testGetWritesByAuthorId() {
        List<Writes> writesList = writesService.getWritesByAuthorId(1L);
        assertNotNull(writesList);
        assertEquals(1, writesList.size());
        Writes writes = writesList.get(0);
        assertEquals(1L, writes.getWritesId());
        assertEquals("AuthorFirstName", writes.getAuthor().getFirstName());
        assertEquals("AuthorLastName", writes.getAuthor().getLastName());
        assertEquals("AuthorDescription", writes.getAuthor().getDescription());
        assertEquals("1970-01-01", writes.getAuthor().getDateOfBirth().toString());
        assertEquals("AuthorPlaceOfBirth", writes.getAuthor().getPlaceOfBirth());
        assertEquals("BookTitle", writes.getBook().getTitle());
        assertEquals("2000-01-01", writes.getBook().getPublicationDate().toString());
        assertEquals("BookDescription", writes.getBook().getDescription());
        assertEquals("Available", writes.getBook().getAvailabilityStatus());
        assertEquals("Category", writes.getBook().getCategory());
        assertEquals("Language", writes.getBook().getLanguage());
        assertEquals("ISBN", writes.getBook().getIsbn());
        assertEquals("Location", writes.getBook().getLocation());
    }
}