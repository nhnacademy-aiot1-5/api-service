package live.ioteatime.apiservice.exception;

public class OrganizationNotFoundException extends RuntimeException {

    public OrganizationNotFoundException() {
        super("Organization not found");
    }

}
