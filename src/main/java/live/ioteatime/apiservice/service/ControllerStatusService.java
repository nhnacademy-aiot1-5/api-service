package live.ioteatime.apiservice.service;

public interface ControllerStatusService {
    int getStatus(String controllerId);

    int enableController(String controllerId);

    int disableController(String controllerId);
}
