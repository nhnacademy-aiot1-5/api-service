package live.ioteatime.apiservice.exception;

public class OrganizationCodeNotMatchesException extends RuntimeException {
    public OrganizationCodeNotMatchesException() {
        super("조직 코드가 일치하지 않습니다.");
    }
}
