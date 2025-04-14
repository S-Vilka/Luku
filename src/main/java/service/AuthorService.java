package service;

import model.dao.impl.AuthorDao;
import model.entity.Author;
import model.entity.Book;

import java.util.List;

/**
 * Service class for managing authors and their books.
 */
public final class AuthorService {

    /**
     * DAO for accessing author data.
     */
    private final AuthorDao authorDao;

    /**
     * Constructs an AuthorService instance.
     */
    public AuthorService() {
        this.authorDao = new AuthorDao();
    }

    /**
     * Saves a new author.
     *
     * @param author the author to save
     */
    public void saveAuthor(final Author author) {
        authorDao.saveAuthor(author);
    }

    /**
     * Retrieves all authors.
     *
     * @return a list of all authors
     */
    public List<Author> getAllAuthors() {
        return authorDao.getAllAuthors();
    }

    /**
     * Retrieves an author by their ID.
     *
     * @param authorId the ID of the author
     * @return the author with the specified ID
     */
    public Author getAuthorById(final Long authorId) {
        return authorDao.getAuthorById(authorId);
    }

    /**
     * Updates an existing author.
     *
     * @param author the author to update
     */
    public void updateAuthor(final Author author) {
        authorDao.updateAuthor(author);
    }

    /**
     * Deletes an author by their ID.
     *
     * @param authorId the ID of the author to delete
     */
    public void deleteAuthor(final Long authorId) {
        authorDao.deleteAuthor(authorId);
    }

    /**
     * Retrieves books by the author's name and language.
     *
     * @param authorFirstName the first name of the author
     * @param authorLastName the last name of the author
     * @param currentLanguage the language of the books
     * @return a list of books by the author in the specified language
     */
    public List<Book> getBooksByAuthor(
            final String authorFirstName,
            final String authorLastName,
            final String currentLanguage
    ) {
        return authorDao.getBooksByAuthorName(
                authorFirstName, authorLastName, currentLanguage
        );
    }
}
