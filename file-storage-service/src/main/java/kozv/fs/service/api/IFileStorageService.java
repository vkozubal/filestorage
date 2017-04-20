package kozv.fs.service.api;

import kozv.fs.api.model.DataFile;
import kozv.fs.api.model.FileAttributes;

import java.util.List;

public interface IFileStorageService {

    /**
     * Saves the given file which may contain data along with given file attributes and metadata
     *
     * @param file to store
     * @return the saved entity
     */
    FileAttributes save(DataFile file);

    /**
     * @param id id must not be {@literal null}.
     * @return an {@link FileAttributes} by its id, or {@literal null} if none found
     */
    DataFile findOne(String id);

    /**
     * @return all user files
     */
    List<FileAttributes> findAll();

    /**
     * remove File by id
     */
    void deleteFile(String id);
}