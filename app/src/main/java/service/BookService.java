package service;

import model.dao.impl.BookDao;
import model.entity.Book;

import java.util.List;


public class BookService {
    private final BookDao bookDao;

    public BookService() {
        bookDao = new BookDao();
    }

    public Book getBookById(Long bookId) {
        return bookDao.getBookById(bookId);
    }


    public List<Book> getBooksByAuthorId(Long authorId) {
        return bookDao.getBooksByAuthorId(authorId);
    }

    public List<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }

    public int getBookCount() {
        return bookDao.getBookCount();
    }

    public List<Book> getBooksByTitle(String title) {
        return bookDao.getBooksByTitle(title);
    }

    public List<Book> getBooksByCategory(String genre) {
        return bookDao.getBooksByCategory(genre);
    }

    public List<Book>  getBooksByLanguage(String language) {
        return bookDao.getBooksByLanguage(language);
    }
}
