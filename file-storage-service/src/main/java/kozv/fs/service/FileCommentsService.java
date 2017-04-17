package kozv.fs.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import kozv.fs.api.model.FileComment;
import kozv.fs.api.service.ICommentsService;
import kozv.fs.api.service.exception.PersistentCommentNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class FileCommentsService implements ICommentsService {
    private final GridFsOperations gridFsOperations;
    private final MongoConverter mongoConverter;

    @Override
    public Collection<FileComment> getComments(String id) {
        final DBObject metaData = findById(id).getMetaData();
        if (metaData == null) {
            return Collections.emptyList();
        }
        return fromDBObject(metaData).getComments();
    }

    @Override
    public FileComment getComment(String fileId, String commentId) {
        return getCommentById(commentId, findById(fileId));
    }

    @Override
    public FileComment createComment(String id, FileComment comment) {
        final GridFSDBFile file = findById(id);
        GridFileMetadata oldMetaData = fromDBObject(file.getMetaData());

        if (oldMetaData == null) {
            oldMetaData = new GridFileMetadata();
        }

        // change some fields
        comment.setCreationDate(new Date());
        comment.setCommentId(new ObjectId().toString());
        comment.setUpdateDate(null);

        if (oldMetaData.getComments() == null) {
            oldMetaData.setComments(new ArrayList<>());
        }
        oldMetaData.getComments().add(comment);

        file.setMetaData(toDBObject(oldMetaData));
        file.save();
        return comment;
    }

    @Override
    public void deleteComment(String fileId, String commentId) {
        final GridFSDBFile file = findById(fileId);

        final GridFileMetadata metadata = fromDBObject(file.getMetaData());
        final FileComment fileComment = findComment(commentId, metadata.getComments(), fileId);

        metadata.getComments().remove(fileComment);
        file.setMetaData(toDBObject(metadata));
        file.save();
    }

    @Override
    public FileComment updateComment(String fileId, FileComment comment) {
        final GridFSDBFile file = findById(fileId);
        final String commentId = comment.getCommentId();
        final GridFileMetadata metadata = fromDBObject(file.getMetaData());
        final FileComment fileComment = findComment(commentId, metadata.getComments(), fileId);
        fileComment.setUpdateDate(new Date());
        fileComment.setText(comment.getText());
        file.setMetaData(toDBObject(metadata));
        file.save();
        return fileComment;
    }

    private BasicDBObject toDBObject(GridFileMetadata oldMetaData) {
        final BasicDBObject sink = new BasicDBObject();
        mongoConverter.write(oldMetaData, sink);
        return sink;
    }

    private GridFileMetadata fromDBObject(DBObject metaData) {
        return mongoConverter.read(GridFileMetadata.class, metaData);
    }

    private Collection<FileComment> retrieveComments(GridFSDBFile fileById) {
        return fromDBObject(fileById.getMetaData()).getComments();
    }

    private FileComment getCommentById(String commentId, GridFSDBFile file) {
        final Collection<FileComment> fileComments = retrieveComments(file);
        return findComment(commentId, fileComments, file.getId().toString());
    }

    private FileComment findComment(final String commentId, Collection<FileComment> fileComments, String fileId) {
        final Optional<FileComment> first = fileComments
                .stream()
                .filter(x -> x.getCommentId().equals(commentId))
                .findFirst();

        if (!first.isPresent()) {
            final String message = String.format("Comment identified by %s not found in the storage for file %s.", commentId, fileId);
            throw new PersistentCommentNotFoundException(message);
        }
        return first.get();
    }

    private GridFSDBFile findById(String id) {
        return gridFsOperations.findOne(getByIdQuery(id));
    }

    private Query getByIdQuery(String id) {
        return new Query(Criteria.where("_id").is(id));
    }
}