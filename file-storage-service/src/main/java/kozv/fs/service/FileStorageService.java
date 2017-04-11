package kozv.fs.service;

import com.mongodb.gridfs.GridFSDBFile;
import kozv.fs.api.model.DataFile;
import kozv.fs.api.model.FileAttributes;
import kozv.fs.api.service.IFileStorageService;
import kozv.fs.api.service.exception.PersistentFileNotFoundException;
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
        String newId = gridFsOperations
                .store(file.getData(), attrs.getFileName(),
                        attrs.getContentType())
                .getId()
                .toString();

        // update id in the given entity
        attrs.setFileId(newId);
        return file.getFileAttrs();
    }

    @Override
    public DataFile findOne(String id) {
        GridFSDBFile result = findOneById(id);
        DataFile file = new DataFile();
        file.setFileAttrs(GRID_FSDB_TO_FILE_ATTRIBUTES.apply(result));
        file.setData(result.getInputStream());
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