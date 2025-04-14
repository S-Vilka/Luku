package service;

import model.dao.impl.WritesDao;
import model.entity.Writes;

import java.util.List;

/**
 * Service class for handling operations
 * related to author-book relationships (Writes).
 */
public final class WritesService {

    /**
     * DAO for accessing Writes data.
     */
    private final WritesDao writesDao;

    /**
     * Constructs a new WritesService with a default WritesDao.
     */
    public WritesService() {
        writesDao = new WritesDao();
    }

    /**
     * Retrieves a Writes record by its ID.
     *
     * @param writesId the ID of the Writes record
     * @return the Writes object if found, otherwise null
     */
    public Writes getWritesById(final Long writesId) {
        return writesDao.getWritesById(writesId);
    }

    /**
     * Retrieves all Writes records associated with a specific book ID.
     *
     * @param bookId the ID of the book
     * @return a list of Writes records for that book
     */
    public List<Writes> getWritesByBookId(final Long bookId) {
        return writesDao.getWritesByBookId(bookId);
    }

    /**
     * Retrieves all Writes records associated with a specific author ID.
     *
     * @param authorId the ID of the author
     * @return a list of Writes records for that author
     */
    public List<Writes> getWritesByAuthorId(final Long authorId) {
        return writesDao.getWritesByAuthorId(authorId);
    }
}