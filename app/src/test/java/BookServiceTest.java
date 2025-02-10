import model.dao.impl.BookDao;
import model.dao.impl.BaseDao;
import model.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import service.AuthorService;
import service.BookService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookServiceTest {
    @Container
    private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.5.9")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private BookService bookService;
    private BookDao bookDao;


    @BeforeEach
    public void setUp() {
        Connection connection = null;
        mariaDBContainer.start();
        try {
            connection = DriverManager.getConnection(mariaDBContainer.getJdbcUrl(), mariaDBContainer.getUsername(), mariaDBContainer.getPassword());
            BaseDao.setConnection(connection);
            bookDao = new BookDao();
            bookService = new BookService();

            // Create the authors table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS authors (" +
                        "author_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "first_name VARCHAR(255) NOT NULL, " +
                        "last_name VARCHAR(255) NOT NULL, " +
                        "description TEXT, " +
                        "date_of_birth DATE, " +
                        "place_of_birth VARCHAR(255)" +
                        ")");
            }

            // Create the books table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                        "book_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "title VARCHAR(255) NOT NULL, " +
                        "publication_date DATE, " +
                        "description TEXT, " +
                        "availability_status VARCHAR(50), " +
                        "category VARCHAR(50), " +
                        "language VARCHAR(50), " +
                        "isbn VARCHAR(20), " +
                        "location VARCHAR(255)" +
                        ")");
            }

            // Insert a sample author
            // Create the writes table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS writes (" +
                        "book_id BIGINT, " +
                        "author_id BIGINT, " +
                        "PRIMARY KEY (book_id, author_id), " +
                        "FOREIGN KEY (book_id) REFERENCES books(book_id), " +
                        "FOREIGN KEY (author_id) REFERENCES authors(author_id)" +
                        ")");
            }

            // Insert a sample author
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("INSERT INTO authors (first_name, last_name, description, date_of_birth, place_of_birth) VALUES " +
                        "('John', 'Doe', 'A famous author', '1970-01-01', 'New York')");
            }

            // Insert a sample book
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("INSERT INTO books (title, publication_date, description, availability_status, category, language, isbn, location) VALUES " +
                        "('Sample Book', '2023-01-01', 'Sample Description', 'Available', 'Fiction', 'English', '1234567890', 'A1')");
            }

            // Insert a writes table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("INSERT INTO writes (author_id, book_id) VALUES " +
                        "(1, 1)");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetBookById() {
        Book book = bookService.getBookById(1L);
        assertNotNull(book);
        assertEquals("Sample Book", book.getTitle());
    }

    @Test
    public void testGetBooksByAuthorId() {
        List<Book> books = bookService.getBooksByAuthorId(1L);
        assertNotNull(books);
        assertEquals(1, books.size());
    }

    @Test
    public void testGetBookCount() {
        int count = bookService.getBookCount();
        assertEquals(1, count);
    }

    @Test
    public void testGetAllBooks() {
        List<Book> books = bookService.getAllBooks();
        assertNotNull(books);
        assertEquals(1, books.size());
    }

    @Test
    public void testGetBooksByTitle() {
        List<Book> books = bookService.getBooksByTitle("Sample");
        assertNotNull(books);
        assertEquals(1, books.size());
    }


    @Test
    public void testGetBooksByCategory() {
        List<Book> books = bookService.getBooksByCategory("Fiction");
        assertNotNull(books);
        assertEquals(1, books.size());
    }

    @Test
    public void testGetBooksByLanguage() {
        List<Book> books = bookService.getBooksByLanguage("English");
        assertNotNull(books);
        assertEquals(1, books.size());
    }
}

