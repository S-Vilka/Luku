package service;

import model.dao.impl.WritesDao;
import model.entity.Writes;
import java.util.List;

public class WritesService {
    private WritesDao writesDao;

    public WritesService() {
        writesDao = new WritesDao();
    }

    public Writes getWritesById(Long writesId) {

        return writesDao.getWritesById(writesId);
    }


    public List<Writes> getWritesByBookId(Long bookId) {

        return writesDao.getWritesByBookId(bookId);
    }

    public List<Writes> getWritesByAuthorId(Long authorId) {

        return writesDao.getWritesByAuthorId(authorId);
    }
}
