package kozv.fs.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import kozv.fs.api.model.FileComment;
import kozv.fs.api.model.GridFileComments;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class FileCommentsService implements ICommentsService {
    private final GridFsOperations gridFsOperations;

    @Override
    public GridFileComments getComments(String id) {
        final GridFSDBFile oneById = findOneById(id);
        return (GridFileComments) oneById.getMetaData();
    }

    @Override
    public FileComment getComment(String fileId, String commentId) {
        return getCommentById(fileId, findOneById(fileId));
    }

    @Override
    public FileComment createComment(String id, FileComment comment) {
        final GridFSDBFile file = findOneById(id);
        GridFileComments oldMetaData = (GridFileComments) file.getMetaData();

        if (oldMetaData == null) {
            oldMetaData = new GridFileComments();
            DBObject metaData = new BasicDBObject();
            metaData.put("comments", oldMetaData.getComments());
            file.setMetaData(metaData);
        }

        // change some fields
        comment.setCreationDate(new Date());
        comment.setCommentId(new ObjectId().toString());
        comment.setUpdateDate(null);

        if (oldMetaData.getComments() == null) {
            oldMetaData.setComments(new ArrayList<>());
        }
        oldMetaData.getComments().add(comment);
        file.save();
        return comment;
    }

    @Override
    public void deleteComment(String fileId, String commentId) {
        final GridFSDBFile file = findOneById(fileId);
        final FileComment commentById = getCommentById(commentId, file);

        final Collection<FileComment> comments = retrieveComments(file);
        comments.remove(commentById);
        file.save();
    }

    @Override
    public FileComment updateComment(String fileId, FileComment comment) {
        final GridFSDBFile file = findOneById(fileId);
        final FileComment fileComment = getCommentById(comment.getCommentId(), file);
        fileComment.setUpdateDate(new Date());
        fileComment.setText(comment.getText());
        file.save();
        return fileComment;
    }

    private Collection<FileComment> retrieveComments(GridFSDBFile fileById) {
        return ((GridFileComments) fileById.getMetaData()).getComments();
    }

    private FileComment getCommentById(String commentId, GridFSDBFile file) {
        final Optional<FileComment> first = retrieveComments(file)
                .stream()
                .filter(x -> x.getCommentId().equals(commentId))
                .findFirst();

        if (!first.isPresent()) {
            final String message = String.format("Comment identified by %s not found in the storage for file %s.", commentId, file.getId());
            throw new PersistentCommentNotFoundException(message);
        }
        return first.get();
    }

    private GridFSDBFile findOneById(String id) {
        return gridFsOperations.findOne(getByIdQuery(id));
    }

    private Query getByIdQuery(String id) {
        return new Query(Criteria.where("_id").is(id));
    }
}