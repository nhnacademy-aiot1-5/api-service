package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.dto.SensorRequest;
import live.ioteatime.apiservice.dto.SensorDto;
import live.ioteatime.apiservice.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/sensors")
@Tag(name = "센서 컨트롤러", description = "센서 및 토픽 관리에 사용하는 컨트롤러입니다.")
@RequiredArgsConstructor
public class SensorController {

    private static final String X_USER_ID = "X-USER-ID";
    private final SensorService sensorService;

    /**
     * 등록 가능한 센서 모델 리스트를 반환합니다.
     * @param userId 요청을 보내는 ADMIN 유저의 아이디
     * @return 200 OK
     */
    @GetMapping("/supported")
    @AdminOnly
    @Operation(summary = "지원하는 센서 리스트를 가져오는 API", description = "지원하는 모든 센서 리스트를 반환합니다.")
    public ResponseEntity<List<SensorDto>> getSupportedSensors(@RequestHeader(X_USER_ID) String userId) {
        List<SensorDto> sensorList = sensorService.getAllSupportedSensors();
        return ResponseEntity.ok(sensorList);
    }

    /**
     * 유저가 소속한 조직의 센서 리스트를 반환합니다.
     * @return 200 OK
     */
    @GetMapping("/list")
    @Operation(summary = "모든 센서들의 리스트를 가져오는 API", description = "소속 조직의 모든 센서 리스트를 반환합니다.")
    public ResponseEntity<List<SensorDto>> getSensors(@RequestHeader(X_USER_ID) String userId){
        return ResponseEntity.ok(sensorService.getOrganizationSensorsByUserId(userId));
    }

    /**
     * 단일 센서 정보를 반환합니다.
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @return 200 OK
     */
    @GetMapping("/{sensorId}")
    @Operation(summary = "센서 단일 조회 API")
    public ResponseEntity<SensorDto> getSensor(@RequestHeader(X_USER_ID) String userId, @PathVariable("sensorId") int sensorId) {
        return ResponseEntity.ok(sensorService.getSensorById(sensorId));
    }

    /**
     * 새 MQTT 센서를 추가합니다.
     * @return 201 CREATED
     */
    @PostMapping
    @AdminOnly
    @Operation(summary = "MQTT 센서를 추가하는 API", description = "MQTT 센서를 추가합니다.")
    public ResponseEntity<String> addMqttSensor(@RequestHeader(X_USER_ID) String userId, @RequestBody SensorRequest addSensorRequest){
        int registeredSensorId = sensorService.addMqttSensor(userId, addSensorRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Sensor registered. id=" + registeredSensorId);
    }

    /**
     * MQTT 센서 정보를 수정합니다.
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @param updateSensorRequest 센서 수정 요청
     * @return 200 OK
     */
    @PutMapping("/{sensorId}/update")
    @AdminOnly
    public ResponseEntity<String> updateMqttSensor(@RequestHeader(X_USER_ID) String userId, @PathVariable("sensorId") int sensorId,
                                                   @Valid @RequestBody SensorRequest updateSensorRequest, BindingResult bindingResult) {

        if(bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().build();
        }
        int updatedSensorId = sensorService.updateMqttSensor(sensorId, updateSensorRequest);
        return ResponseEntity.ok().body("Sensor updated. id=" + updatedSensorId);
    }

    /**
     * 센서를 데이터베이스에서 삭제합니다.
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @return 204 NO CONTENT
     */
    @DeleteMapping("/{sensorId}")
    @AdminOnly
    public ResponseEntity<String> deleteSensor(@RequestHeader(X_USER_ID) String userId, @PathVariable("sensorId") int sensorId) {
        sensorService.deleteSensorById(sensorId);
        return ResponseEntity.noContent().build();
    }

}
