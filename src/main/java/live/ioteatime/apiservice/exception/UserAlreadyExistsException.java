package live.ioteatime.apiservice.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String id) {
        super("User Already Exists: " + id);
    }
}
