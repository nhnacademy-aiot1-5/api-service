package live.ioteatime.apiservice.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userId) {
        super("회원을 찾을 수 없습니다: " + userId);
    }

}
