package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.service.ControllerStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/controller")
public class ControllerStatusController {

    private final ControllerStatusService controllerStatusService;

    @GetMapping("/status")
    public ResponseEntity<Integer> getStatus(String controllerId) {
        return ResponseEntity.status(HttpStatus.OK).body(controllerStatusService.getStatus(controllerId));
    }

    @PutMapping("/enable")
    public ResponseEntity<Integer> enableController(String controllerId) {
        return ResponseEntity.status(HttpStatus.OK).body(controllerStatusService.enableController(controllerId));
    }

    @PutMapping("/disable")
    public ResponseEntity<Integer> disableController(String controllerId) {
        return ResponseEntity.status(HttpStatus.OK).body(controllerStatusService.disableController(controllerId));
    }

}
