package service;

import model.dao.impl.AuthorDao;
import model.entity.Author;

import java.util.List;

public class AuthorService {

    private final AuthorDao authorDao;

    public AuthorService() {
        this.authorDao = new AuthorDao();
    }

    public void saveAuthor(Author author) {
        authorDao.saveAuthor(author);
    }

    public List<Author> getAllAuthors() {
        return authorDao.getAllAuthors();
    }

    public Author getAuthorById(Long authorId) {
        return authorDao.getAuthorById(authorId);
    }

    public void updateAuthor(Author author) {
        authorDao.updateAuthor(author);
    }

    public void deleteAuthor(Long authorId) {
        authorDao.deleteAuthor(authorId);
    }
}
