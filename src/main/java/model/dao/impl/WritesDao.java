package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.entity.Author;
import model.entity.Book;
import model.entity.Writes;

/**
 * Data access object for the Writes entity.
 * Provides methods to retrieve Writes records from the database.
 */
public final class WritesDao {

    /**
     * Retrieves a Writes object by its ID.
     *
     * @param writesId the ID of the Writes record
     * @return the Writes object if found, otherwise null
     */
    public Writes getWritesById(final Long writesId) {
        String query = "SELECT * FROM writes WHERE writes_id = ?";
        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query)) {
            stmt.setLong(1, writesId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToWrites(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all Writes records associated with a given author ID.
     *
     * @param authorId the author's ID
     * @return a list of Writes records
     */
    public List<Writes> getWritesByAuthorId(final Long authorId) {
        List<Writes> writes = new ArrayList<>();
        String query = "SELECT * FROM writes WHERE author_id = ?";
        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query)) {
            stmt.setLong(1, authorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                writes.add(mapRowToWrites(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return writes;
    }

    /**
     * Retrieves all Writes records associated with a given book ID.
     *
     * @param bookId the book's ID
     * @return a list of Writes records
     */
    public List<Writes> getWritesByBookId(final Long bookId) {
        List<Writes> writes = new ArrayList<>();
        String query = "SELECT * FROM writes WHERE book_id = ?";
        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query)) {
            stmt.setLong(1, bookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                writes.add(mapRowToWrites(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return writes;
    }

    /**
     * Maps a result set row to a Writes object.
     *
     * @param rs the result set
     * @return a Writes object with populated data
     * @throws SQLException if a database access error occurs
     */
    private Writes mapRowToWrites(final ResultSet rs) throws SQLException {
        Writes writes = new Writes();
        writes.setWritesId(rs.getLong("writes_id"));

        Long authorId = rs.getLong("author_id");
        AuthorDao authorDao = new AuthorDao();
        Author author = authorDao.getAuthorById(authorId);
        writes.setAuthor(author);

        Long bookId = rs.getLong("book_id");
        BookDao bookDao = new BookDao();
        Book book = bookDao.getBookById(bookId);
        writes.setBook(book);

        return writes;
    }
}
