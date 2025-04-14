package service;

import model.dao.impl.BookDao;
import model.entity.Author;
import model.entity.Book;

import java.util.List;
import java.util.Set;


public final class BookService {
    /**
     * BookService is a service class that provides methods to interact with the
     * BookDao class for performing CRUD operations on books.
     */
    private final BookDao bookDao;

    /**
     * Constructor initializes the BookDao instance.
     */
    public BookService() {
        bookDao = new BookDao();
    }

    /**
     * Adds a new book to the database.
     * @param bookId the ID of the book to be added'
     * @return the ID of the added book
     */
    public Book getBookById(final Long bookId) {
        return bookDao.getBookById(bookId);
    }

    /**
     * Adds a new book to the database.
     * @param authorId the ID of the author to be added'
     * @return the book written by the author
     */
    public List<Book> getBooksByAuthorId(final Long authorId) {

        return bookDao.getBooksByAuthorId(authorId);
    }

    /**
     * Adds a new book to the database.
     * @return all the books in the database
     */
    public List<Book> getAllBooks() {

        return bookDao.getAllBooks();
    }

    /**
     * Adds a new book to the database.
     * @return book count
     */
    public int getBookCount() {

        return bookDao.getBookCount();
    }

    /**
     * Adds a new book to the database.
     * @param title the title of the book to be added
     * @param currentLanguage the language of the book to be added
     * @return the books with the specified title and language
     */
    public List<Book> getBooksByTitle(final String title,
                                      final String currentLanguage) {

        return bookDao.getBooksByTitle(title, currentLanguage);
    }

    /**
     * Adds a new book to the database.
     * @param genre the genre of the book to be added
     * @return the books with the specified genre
     */
    public List<Book> getBooksByCategory(final String genre) {

        return bookDao.getBooksByCategory(genre);
    }

    /**
     * Adds a new book to the database.
     * @param bookId the ID of the book to be added'
     * @return the book with the specified ID
     */
    public Set<Author> getAuthorsByBookId(final Long bookId) {
        Book book = bookDao.getBookById(bookId);
        return book != null ? book.getAuthors() : null;
    }

    /**
     * Adds a new book to the database.
     * @param language the language of the book to be added
     * @return the books with the specified language
     */
    public List<Book>  getBooksByLanguage(final String language) {

        return bookDao.getBooksByLanguage(language);
    }

    /**
     * Adds a new book to the database.
     * @param bookId the ID of the book to be added'
     * @param isAvailable the availability of the book to be added
     */
    public void setBookAvailability(final Long bookId,
                                    final String isAvailable) {
        bookDao.updateBookAvailabilityStatus(bookId, isAvailable);
    }

    /**
     * Adds a new book to the database.
     * @param searchTerm the search term to be used for searching books
     * @param currentLanguage the language of the book to be added
     * @return the books with the specified search term and language
     */
    public List<Book> searchBooks(final String searchTerm,
                                  final String currentLanguage) {

        return bookDao.searchBooks(searchTerm, currentLanguage);
    }

}
