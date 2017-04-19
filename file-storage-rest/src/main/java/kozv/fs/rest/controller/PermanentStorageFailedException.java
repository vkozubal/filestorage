package kozv.fs.rest.controller;

public class PermanentStorageFailedException extends RuntimeException{
    public PermanentStorageFailedException(String message) {
        super(message);
    }
}
