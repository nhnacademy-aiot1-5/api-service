package live.ioteatime.apiservice.exception;

public class UnauthenticatedException extends RuntimeException {
    public UnauthenticatedException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}
