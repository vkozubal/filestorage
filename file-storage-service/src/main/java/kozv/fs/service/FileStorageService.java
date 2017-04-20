package kozv.fs.service;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import kozv.fs.api.model.DataFile;
import kozv.fs.api.model.FileAttributes;
import kozv.fs.service.api.IFileStorageService;
import kozv.fs.service.exception.PersistentFileNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@ComponentScan
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class FileStorageService implements IFileStorageService {

    private static final Function<GridFSDBFile, FileAttributes> GRID_FSDB_TO_FILE_ATTRIBUTES = gridFSDBFile -> {
        FileAttributes attrs = new FileAttributes();
        attrs.setFileName(gridFSDBFile.getFilename());
        attrs.setFileId(gridFSDBFile.getId().toString());
        attrs.setContentType(gridFSDBFile.getContentType());
        attrs.setUploadDate(gridFSDBFile.getUploadDate());
        return attrs;
    };

    private final GridFsOperations gridFsOperations;

    @Override
    public FileAttributes save(DataFile file) {
        FileAttributes attrs = file.getFileAttrs();
        // store the entity to the database and get an id
        GridFSFile newFile = gridFsOperations
                .store(file.getDataStream(), attrs.getFileName(),
                        attrs.getContentType());

        // update id in the given entity
        attrs.setFileId(newFile.getId().toString());
        attrs.setUploadDate(newFile.getUploadDate());
        return file.getFileAttrs();
    }

    @Override
    public DataFile findOne(String id) {
        GridFSDBFile result = findOneById(id);
        DataFile file = new DataFile();
        file.setFileAttrs(GRID_FSDB_TO_FILE_ATTRIBUTES.apply(result));
        file.setDataStream(result.getInputStream());
        return file;
    }

    @Override
    public List<FileAttributes> findAll() {
        return gridFsOperations
                .find(null)
                .parallelStream()
                .map(GRID_FSDB_TO_FILE_ATTRIBUTES)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFile(String id) {
        gridFsOperations.delete(getByIdQuery(id));
    }

    private GridFSDBFile findOneById(String id) {
        final GridFSDBFile persistedFile = gridFsOperations.findOne(getByIdQuery(id));
        if (persistedFile == null) {
            final String message = String.format("File identified by %s not found in the storage.", id);
            throw new PersistentFileNotFoundException(message);
        }
        return persistedFile;
    }

    private Query getByIdQuery(String id) {
        return new Query(Criteria.where("_id").is(id));
    }
}