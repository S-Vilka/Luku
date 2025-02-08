package model.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.entity.Author;
import model.entity.Book;
import model.entity.Writes;


public class WritesDao extends BaseDao {

    public Writes getWritesById(Long writesId) {
        String query = "SELECT * FROM writes WHERE writes_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, writesId);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return mapRowToWrites(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Writes> getWritesByAuthorId(Long authorId) {
        List<Writes> writes = new ArrayList<>();
        String query = "SELECT * FROM writes WHERE author_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
//                stmt.setLong(1, writesId);
            stmt.setLong(1, authorId);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                writes.add(mapRowToWrites(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return writes;
    }

    public List<Writes> getWritesByBookId(Long bookId) {
        List<Writes> writes = new ArrayList<>();
        String query = "SELECT * FROM writes WHERE book_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, bookId);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                writes.add(mapRowToWrites(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return writes;
    }

    public void saveWrites(Writes writes) {
        String query = "INSERT INTO writes (author_id, book_id) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, writes.getAuthor().getAuthorId());
            stmt.setLong(2, writes.getBook().getBookId());
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWrites(Writes writes) {
        String query = "UPDATE writes SET author_id = ?, book_id = ? WHERE writes_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, writes.getAuthor().getAuthorId());
            stmt.setLong(2, writes.getBook().getBookId());
            stmt.setLong(3, writes.getWritesId());
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWrites(Long writesId) {
        String query = "DELETE FROM writes WHERE writes_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, writesId);
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Writes mapRowToWrites(ResultSet rs) throws SQLException {
        Writes writes = new Writes();
        writes.setWritesId(rs.getLong("writes_id"));

        // Fetch author
        Long authorId = rs.getLong("author_id");
        AuthorDao authorDao = new AuthorDao();
        Author author = authorDao.getAuthorById(authorId);
        writes.setAuthor(author);

        // Fetch book
        Long bookId = rs.getLong("book_id");
        BookDao bookDao = new BookDao();
        Book book = bookDao.getBookById(bookId);
        writes.setBook(book);

        return writes;
    }
}
