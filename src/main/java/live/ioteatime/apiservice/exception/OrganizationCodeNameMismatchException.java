package live.ioteatime.apiservice.exception;

public class OrganizationCodeNameMismatchException extends RuntimeException {
    public OrganizationCodeNameMismatchException() {
        super("조직이 존재하지 않거나 조직 코드가 일치하지 않습니다.");
    }
}
