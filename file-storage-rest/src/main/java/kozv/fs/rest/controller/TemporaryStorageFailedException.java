package kozv.fs.rest.controller;

public class TemporaryStorageFailedException extends RuntimeException{
    public TemporaryStorageFailedException(String message) {
        super(message);
    }
}
