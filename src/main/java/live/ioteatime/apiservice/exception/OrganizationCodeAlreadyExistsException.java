package live.ioteatime.apiservice.exception;

public class OrganizationCodeAlreadyExistsException extends RuntimeException {

    public OrganizationCodeAlreadyExistsException(String code) {
        super("Organization code already exists: "+code);
    }
}
