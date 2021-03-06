package kozv.fs.service.exception;


/**
 *  Is thrown when requested file is not found in the storage.
 */
public class PersistentFileNotFoundException extends FSServiceException {
    public PersistentFileNotFoundException(String message) {
        super(message);
    }
}