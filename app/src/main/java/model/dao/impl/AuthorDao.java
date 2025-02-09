// src/main/java/model/dao/impl/AuthorDaoImpl.java
package model.dao.impl;

import model.entity.Author;
import model.entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDao extends BaseDao {
    private Connection connection;


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
        String query = "INSERT INTO authors (first_name, last_name, description, date_of_birth, place_of_birth) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            stmt.setString(3, author.getDescription());
            stmt.setDate(4, Date.valueOf(author.getDateOfBirth()));
            stmt.setString(5, author.getPlaceOfBirth());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAuthor(Author author) {
        String query = "UPDATE authors SET first_name = ?, last_name = ?, description = ?, date_of_birth = ?, place_of_birth = ? WHERE author_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            stmt.setString(3, author.getDescription());
            stmt.setDate(4, Date.valueOf(author.getDateOfBirth()));
            stmt.setString(5, author.getPlaceOfBirth());
            stmt.setLong(6, author.getAuthorId());
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

//    public Book getBookByAuthorId(Long authorId) {
//        String query = "SELECT * FROM books WHERE author_id = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setLong(1, authorId);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                return mapRowToBook(rs);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private Book mapRowToBook(ResultSet rs) {
//
//    }

    private Author mapRowToAuthor(ResultSet rs) throws SQLException {
        Author author = new Author();
        author.setAuthorId(rs.getLong("author_id"));
        author.setFirstName(rs.getString("first_name"));
        author.setLastName(rs.getString("last_name"));
        author.setDescription(rs.getString("description"));
        author.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        author.setPlaceOfBirth(rs.getString("place_of_birth"));
        return author;
    }
}