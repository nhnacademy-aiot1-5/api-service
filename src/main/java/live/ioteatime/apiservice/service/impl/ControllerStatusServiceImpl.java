package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.ControllerStatus;
import live.ioteatime.apiservice.repository.ControllerStatusRepository;
import live.ioteatime.apiservice.service.ControllerStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ControllerStatusServiceImpl implements ControllerStatusService {

    private final ControllerStatusRepository controllerStatusRepository;

    @Override
    public int getStatus(String controllerId) {
        ControllerStatus controllerStatus = controllerStatusRepository.findById(controllerId).orElse(null);
        return controllerStatus.getStatus();
    }

    @Override
    public int enableController(String controllerId) {
        ControllerStatus controllerStatus = controllerStatusRepository.findById(controllerId).orElse(null);
        controllerStatus.setStatus(1);
        controllerStatusRepository.save(controllerStatus);
        return controllerStatus.getStatus();
    }

    @Override
    public int disableController(String controllerId) {
        ControllerStatus controllerStatus = controllerStatusRepository.findById(controllerId).orElse(null);
        controllerStatus.setStatus(0);
        controllerStatusRepository.save(controllerStatus);
        return controllerStatus.getStatus();
    }
}
