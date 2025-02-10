package model.dao.impl;
import model.entity.Author;
import model.entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookDao extends BaseDao {

    public Book getBookById(Long bookId) {
        String query = "SELECT * FROM books WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToBook(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public List<Book> getBooksByCategoryId(Long categoryId) {
//        List<Book> books = new ArrayList<>();
//        String query = "SELECT * FROM books WHERE category_id = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setLong(1, categoryId);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                books.add(mapRowToBook(rs));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return books;
//    }

//    public List<Book> getBooksByLanguageId(Long languageId) {
//        List<Book> books = new ArrayList<>();
//        String query = "SELECT * FROM books WHERE language_id = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setLong(1, languageId);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                books.add(mapRowToBook(rs));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return books;
//    }

    public List<Book> getBooksByAuthorId(Long authorId) {
        List<Book> books = new ArrayList<>();
//        String query = "SELECT * FROM books WHERE author_id = ?";
        String query = "SELECT b.* FROM books b JOIN writes w ON b.book_id = w.book_id WHERE w.author_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, authorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public int getBookCount() {
        String query = "SELECT COUNT(*) FROM books";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
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
        book.setPublicationDate(rs.getDate("publication_date").toLocalDate());
        book.setDescription(rs.getString("description"));
        book.setAvailabilityStatus(rs.getString("availability_status"));
        book.setCategory(rs.getString("category"));
        book.setLanguage(rs.getString("language"));
        book.setIsbn(rs.getString("isbn"));
        book.setLocation(rs.getString("location"));

        // Fetch authors for the book
        book.setAuthors(getAuthorsByBookId(book.getBookId()));

        return book;
    }

    private Set<Author> getAuthorsByBookId(Long bookId) {
        Set<Author> authors = new HashSet<>();
        String query = "SELECT a.* FROM authors a JOIN writes w ON a.author_id = w.author_id WHERE w.book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, bookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Author author = new Author();
                author.setAuthorId(rs.getLong("author_id"));
//                author.setFirstName(rs.getString("firstName"));
                // Set other fields of Author as needed for the frontend
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    public List<Book> getBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE title LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> getBooksByCategory(String genre) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE category LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + genre + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book>  getBooksByLanguage(String language) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE language LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + language + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
}