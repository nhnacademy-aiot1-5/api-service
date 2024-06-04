package live.ioteatime.apiservice.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String id) {
        super("이미 존재하는 회원입니다: " + id);
    }

}
