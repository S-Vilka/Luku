package model.dao.impl;

import model.entity.Author;
import model.entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDao extends BaseDao {

    public Author getAuthorById(Long authorId) {
        String query = "SELECT * FROM authors WHERE author_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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

    public List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        String query = "SELECT * FROM authors";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                authors.add(mapRowToAuthor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    public void saveAuthor(Author author) {
        String query = "INSERT INTO authors (first_name, last_name, description, date_of_birth, place_of_birth, profile_image) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            stmt.setString(3, author.getDescription());
            stmt.setDate(4, author.getDateOfBirth() != null ? Date.valueOf(author.getDateOfBirth()) : null);
            stmt.setString(5, author.getPlaceOfBirth());
            stmt.setString(6, author.getProfileImage()); // Ensure profile_image is added
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                author.setAuthorId(generatedKeys.getLong(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAuthor(Author author) {
        String query = "UPDATE authors SET first_name = ?, last_name = ?, description = ?, date_of_birth = ?, place_of_birth = ?, profile_image = ? WHERE author_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            stmt.setString(3, author.getDescription());
            stmt.setDate(4, author.getDateOfBirth() != null ? Date.valueOf(author.getDateOfBirth()) : null);
            stmt.setString(5, author.getPlaceOfBirth());
            stmt.setString(6, author.getProfileImage()); // Ensure profile_image is updated
            stmt.setLong(7, author.getAuthorId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAuthor(Long authorId) {
        String query = "DELETE FROM authors WHERE author_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, authorId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Author mapRowToAuthor(ResultSet rs) throws SQLException {
        Author author = new Author();
        author.setAuthorId(rs.getLong("author_id"));
        author.setFirstName(rs.getString("first_name"));
        author.setLastName(rs.getString("last_name"));
        author.setDescription(rs.getString("description"));
        author.setDateOfBirth(rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth").toLocalDate() : null);
        author.setPlaceOfBirth(rs.getString("place_of_birth"));
        author.setProfileImage(rs.getString("profile_image")); // Ensure profile_image is mapped
        return author;
    }

    public List<Book> getBooksByAuthorName(String authorFirstName, String authorLastName) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT b.* FROM books b " +
                "JOIN writes w ON b.book_id = w.book_id " +
                "JOIN authors a ON w.author_id = a.author_id " +
                "WHERE a.first_name LIKE ? AND a.last_name LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + authorFirstName + "%");
            stmt.setString(2, "%" + authorLastName + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    private Book mapRowToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getLong("book_id"));
        book.setTitle(rs.getString("title"));
        book.setCategory(rs.getString("category"));
        book.setPublicationDate(rs.getDate("publication_date") != null ? rs.getDate("publication_date").toLocalDate() : null);
        book.setDescription(rs.getString("description"));
        book.setAvailabilityStatus(rs.getString("availability_status"));
        book.setLanguage(rs.getString("language"));
        book.setIsbn(rs.getString("isbn"));
        book.setLocation(rs.getString("location"));
        return book;
    }
}