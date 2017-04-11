package kozv.fs.api.service.exception;

/**
 * Any kind of service exception
 */
public class FSServiceException extends RuntimeException{
    public FSServiceException(String message) {
        super(message);
    }
    public FSServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}