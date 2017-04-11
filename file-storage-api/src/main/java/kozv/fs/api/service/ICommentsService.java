package kozv.fs.api.service;

import kozv.fs.api.model.FileComment;
import kozv.fs.api.model.GridFileComments;

public interface ICommentsService {

    /**
     * @param fileId unique file identification
     */
    GridFileComments getComments(String fileId);


    /**
     * @param fileId unique file identification
     */
    FileComment getComment(String fileId, String commentId);

    /**
     * @param fileId  unique file identification
     * @param comment comment entity to persist
     */
    FileComment createComment(String fileId, FileComment comment);

    /**
     * @param fileId    unique file identification
     * @param commentId id of comment entity to delete
     */
    void deleteComment(String fileId, String commentId);

    /**
     * @param fileId  unique file identification
     * @param comment comment entity to update with
     */
    FileComment updateComment(String fileId, FileComment comment);
}