package model.dao.impl;

import model.entity.Author;
import model.entity.Book;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Author entity.
 */
public final class AuthorDao {

    /**
     * Index for the description column in SQL queries.
     */
    private static final int DESCRIPTION_INDEX = 3;

    /**
     * Index for the date of birth column in SQL queries.
     */
    private static final int DATE_OF_BIRTH_INDEX = 4;

    /**
     * Index for the place of birth column in SQL queries.
     */
    private static final int PLACE_OF_BIRTH_INDEX = 5;

    /**
     * Index for the profile image column in SQL queries.
     */
    private static final int PROFILE_IMAGE_INDEX = 6;

    /**
     * Index for the author ID column in SQL queries.
     */
    private static final int AUTHOR_ID_INDEX = 7;

    /**
     * Retrieves an author by their ID.
     *
     * @param authorId the ID of the author
     * @return the Author object, or null if not found
     */
    public Author getAuthorById(final Long authorId) {
        String query = "SELECT * FROM authors WHERE author_id = ?";
        try (Connection connection = BaseDao.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, authorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToAuthor(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all authors.
     *
     * @return a list of all authors
     */
    public List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        String query = "SELECT * FROM authors";
        try (Statement stmt = BaseDao.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                authors.add(mapRowToAuthor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    /**
     * Saves a new author.
     *
     * @param author the author to save
     */
    public void saveAuthor(final Author author) {
        String query = "INSERT INTO authors (first_name, last_name,"
                + "description, date_of_birth, place_of_birth, "
                + "profile_image) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = BaseDao.getConnection().prepareStatement(
                query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            stmt.setString(DESCRIPTION_INDEX, author.getDescription());
            stmt.setDate(DATE_OF_BIRTH_INDEX, author.getDateOfBirth() != null
                    ? Date.valueOf(author.getDateOfBirth())
                    : null);
            stmt.setString(PLACE_OF_BIRTH_INDEX, author.getPlaceOfBirth());
            stmt.setString(PROFILE_IMAGE_INDEX, author.getProfileImage());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                author.setAuthorId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing author.
     *
     * @param author the author to update
     */
    public void updateAuthor(final Author author) {
        String query = "UPDATE authors SET first_name = ?, last_name = ?,"
                + "description = ?, date_of_birth = ?, place_of_birth = ?,"
                + "profile_image = ? WHERE author_id = ?";
        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query)) {
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            stmt.setString(DESCRIPTION_INDEX, author.getDescription());
            stmt.setDate(DATE_OF_BIRTH_INDEX, author.getDateOfBirth() != null
                    ? Date.valueOf(author.getDateOfBirth())
                    : null);
            stmt.setString(PLACE_OF_BIRTH_INDEX, author.getPlaceOfBirth());
            stmt.setString(PROFILE_IMAGE_INDEX, author.getProfileImage());
            stmt.setLong(AUTHOR_ID_INDEX, author.getAuthorId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes an author by their ID.
     *
     * @param authorId the ID of the author to delete
     */
    public void deleteAuthor(final Long authorId) {
        String query = "DELETE FROM authors WHERE author_id = ?";
        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query)) {
            stmt.setLong(1, authorId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves books by the author's name and language.
     *
     * @param authorFirstName the first name of the author
     * @param authorLastName the last name of the author
     * @param currentLanguage the language of the books
     * @return a list of books by the author in the specified language
     */
    public List<Book> getBooksByAuthorName(
            final String authorFirstName,
            final String authorLastName,
            final String currentLanguage
    ) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT b.* FROM books b "
                + "JOIN writes w ON b.book_id = w.book_id "
                + "JOIN authors a ON w.author_id = a.author_id "
                + "WHERE a.first_name LIKE ? AND a.last_name LIKE ?";
        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query)) {
            stmt.setString(1, "%" + authorFirstName + "%");
            stmt.setString(2, "%" + authorLastName + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(mapRowToBook(rs, currentLanguage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    private Author mapRowToAuthor(final ResultSet rs) throws SQLException {
        Author author = new Author();
        author.setAuthorId(rs.getLong("author_id"));
        author.setFirstName(rs.getString("first_name"));
        author.setLastName(rs.getString("last_name"));
        author.setDescription(rs.getString("description"));
        author.setDateOfBirth(rs.getDate("date_of_birth") != null
                ? rs.getDate("date_of_birth").toLocalDate()
                : null);
        author.setPlaceOfBirth(rs.getString("place_of_birth"));
        author.setProfileImage(rs.getString("profile_image"));
        return author;
    }

    private Book mapRowToBook(final ResultSet rs,
                              final String currentLanguage)throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getLong("book_id"));
        book.setTitleEn(rs.getString("title_en"));
        book.setTitleUr(rs.getString("title_ur"));
        book.setTitleRu(rs.getString("title_ru"));
        book.setTitle(book.getTitle(currentLanguage), currentLanguage);
        book.setCategory(rs.getString("category"));
        book.setPublicationDate(rs.getDate("publication_date") != null
                ? rs.getDate("publication_date").toLocalDate()
                : null);
        book.setDescription(rs.getString("description"));
        book.setAvailabilityStatus(rs.getString("availability_status"));
        book.setLanguage(rs.getString("language"));
        book.setIsbn(rs.getString("isbn"));
        book.setLocation(rs.getString("location"));
        return book;
    }
}
