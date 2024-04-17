package live.ioteatime.apiservice.exception;

public class OrganizationNotFoundException extends RuntimeException {
    public OrganizationNotFoundException(String userId) {
        super("Organization not found : userId=" + userId);
    }
}
