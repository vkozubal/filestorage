package kozv.fs.api.service.exception;


/**
 *  Is thrown when requested comment is not found in the storage
 */
public class PersistentCommentNotFoundException extends FSServiceException {
    public PersistentCommentNotFoundException(String message) {
        super(message);
    }
}