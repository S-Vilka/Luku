import model.dao.impl.AuthorDao;
import model.dao.impl.BaseDao;
import model.entity.Author;
import model.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import service.AuthorService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthorServiceTest {
    @Container
    private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.5.9")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private AuthorService authorService;
//    private AuthorDao authorDao;

    @BeforeEach
    public void setUp() {
        Connection connection = null;
        mariaDBContainer.start();
        try {
            connection = DriverManager.getConnection(mariaDBContainer.getJdbcUrl(), mariaDBContainer.getUsername(), mariaDBContainer.getPassword());
            BaseDao.setConnection(connection);
//            authorDao = new AuthorDao();
            authorService = new AuthorService();


            // Create the authors table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS authors (" +
                        "author_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "first_name VARCHAR(255) NOT NULL, " +
                        "last_name VARCHAR(255) NOT NULL, " +
                        "description TEXT, " +
                        "date_of_birth DATE, " +
                        "place_of_birth VARCHAR(255), " +
                        "profile_image VARCHAR(255)" +
                        ")");

                stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                        "book_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "title_en VARCHAR(255) NOT NULL, " +
                        "title_ur VARCHAR(255), " +
                        "title_ru VARCHAR(255), " +
                        "publication_date DATE, " +
                        "description TEXT, " +
                        "availability_status VARCHAR(255), " +
                        "category VARCHAR(255), " +
                        "language VARCHAR(255), " +
                        "isbn VARCHAR(255), " +
                        "location VARCHAR(255)" +
                        ")");

                stmt.execute("CREATE TABLE IF NOT EXISTS writes (" +
                        "author_id BIGINT, " +
                        "book_id BIGINT, " +
                        "PRIMARY KEY (author_id, book_id), " +
                        "FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE CASCADE, " +
                        "FOREIGN KEY (book_id) REFERENCES books(book_id)" +
                        ")");
            }
            // Insert a test author and books
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("INSERT INTO authors (first_name, last_name, description, date_of_birth, place_of_birth, profile_image) VALUES " +
                        "('John', 'Doe', 'A famous author', '1970-01-01', 'New York', 'path/to/john_doe.jpg')");
                stmt.execute("INSERT INTO authors (first_name, last_name, description, date_of_birth, place_of_birth, profile_image) VALUES " +
                        "('Jane', 'Austin', 'A very famous author', '1970-01-01', 'London', 'path/to/jane_austin.jpg')");
                stmt.execute("INSERT INTO books (title_en, title_ur, title_ru, publication_date, description, availability_status, category, language, isbn, location) VALUES " +
                        "('Effective Java', NULL, NULL, '2008-05-08', 'A comprehensive guide to best practices in Java programming', 'Available', 'Programming', 'English', '978-0134685991', 'Aisle 3'), " +
                        "('Clean Code', NULL, NULL, '2008-08-01', 'A handbook of agile software craftsmanship', 'Available', 'Programming', 'English', '978-0132350884', 'Aisle 4')");
                stmt.execute("INSERT INTO writes (author_id, book_id) VALUES " +
                        "(1, 1), (1, 2)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveAuthor() {
        Author author = new Author();
        author.setFirstName("Jane");
        author.setLastName("Smith");
        author.setDescription("Another famous author");
        author.setDateOfBirth(LocalDate.of(1980, 2, 2));
        author.setPlaceOfBirth("Los Angeles");
        author.setProfileImage("path/to/jane_smith.jpg");
        authorService.saveAuthor(author);
        Author fetchedAuthor = authorService.getAuthorById(author.getAuthorId());
        assertNotNull(fetchedAuthor);
        assertEquals("Jane", fetchedAuthor.getFirstName());
    }

    @Test
    public void testGetAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        assertNotNull(authors);
        assertEquals(2, authors.size());
    }

    @Test
    public void testUpdateAuthor() {
        Author author = authorService.getAuthorById(1L);
        assertNotNull(author);
        author.setDescription("Updated description");
        authorService.updateAuthor(author);

        Author updatedAuthor = authorService.getAuthorById(1L);
        assertEquals("Updated description", updatedAuthor.getDescription());
    }

    @Test
    public void testDeleteAuthor() {
        authorService.deleteAuthor(1L);
        Author deletedAuthor = authorService.getAuthorById(1L);
        assertEquals(null, deletedAuthor);
    }

    @Test
    public void testGetBooksByAuthorName() {
        List<Book> books = authorService.getBooksByAuthor("John", "Doe", "English");
        assertNotNull(books);
        assertEquals(2, books.size());
        assertEquals("Effective Java", books.get(0).getTitle("English"));
        assertEquals("Clean Code", books.get(1).getTitle("English"));
    }

    @Test
    public void testGetAuthorById() {
        Author author = authorService.getAuthorById(2L);
        assertNotNull(author);
        assertEquals("Jane", author.getFirstName());
    }

}