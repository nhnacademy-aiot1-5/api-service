package live.ioteatime.apiservice.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("UnauthorizedException : 권한이 없는 접근입니다.");
    }
}
