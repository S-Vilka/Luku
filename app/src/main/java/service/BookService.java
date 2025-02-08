package service;

import model.dao.impl.BookDao;
import model.entity.Author;
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

    public List<Book> getBooksByCategoryId(Long categoryId) {
        return bookDao.getBooksByCategoryId(categoryId);
    }

    public List<Book> getBooksByLanguageId(Long languageId) {
        return bookDao.getBooksByLanguageId(languageId);
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

}
