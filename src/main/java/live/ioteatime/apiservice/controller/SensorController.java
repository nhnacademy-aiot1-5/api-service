package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.domain.Sensor;
import live.ioteatime.apiservice.dto.AddSensorRequest;
import live.ioteatime.apiservice.dto.SensorDto;
import live.ioteatime.apiservice.dto.TopicDto;
import live.ioteatime.apiservice.service.SensorService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensors")
@Tag(name = "센서 컨트롤러", description = "센서 사용할 수 있는 컨트롤러입니다.")
@RequiredArgsConstructor
public class SensorController {

    private final static String X_USER_ID = "X-USER-ID";
    private final SensorService sensorService;

    /**
     * 등록 가능한 센서 모델 리스트를 반환합니다.
     * @param userId 요청을 보내는 ADMIN 유저의 아이디
     * @return 서비스에서 지원하는 센서 리스트
     */
    @GetMapping("/supported")
    @AdminOnly
    @Operation(summary = "지원하는 센서 리스트를 가져오는 API", description = "지원하는 모든 센서 리스트를 반환합니다.")
    public ResponseEntity<List<SensorDto>> getSupportedSensors(@RequestHeader(X_USER_ID) String userId) {
        List<SensorDto> sensorList = sensorService.getAllSupportedSensors();
        return ResponseEntity.ok(sensorList);
    }

    /**
     * 소속 조직의 센서 리스트를 반환합니다.
     * @param organizationId 소속 조직 아이디
     * @return 센서 리스트
     */
    @GetMapping
    @Operation(summary = "모든 센서들의 리스트를 가져오는 API", description = "소속 조직의 모든 센서 리스트를 반환합니다.")
    public ResponseEntity<List<SensorDto>> getSensors(@RequestHeader(X_USER_ID) String userId, int organizationId){
        return ResponseEntity.ok(sensorService.getSensorsByOrganizationId(organizationId));
    }

    /**
     *
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @return 해당 센서 정보를 반환합니다.
     */
    @GetMapping("/{sensorId}")
    @Operation(summary = "센서 단일 조회 API")
    public ResponseEntity<SensorDto> getSensor(@RequestHeader(X_USER_ID) String userId, @PathVariable("sensorId") int sensorId) {
        return ResponseEntity.ok(sensorService.getSensorById(sensorId));
    }

    /**
     * 새 MQTT 센서를 추가합니다.
     * @return 등록 완료한 센서 아이디
     */
    @PostMapping
    @AdminOnly
    @Operation(summary = "MQTT 센서를 추가하는 API", description = "MQTT 센서를 추가합니다.")
    public ResponseEntity<String> addMqttSensor(@RequestHeader(X_USER_ID) String userId, @RequestBody AddSensorRequest addSensorRequest){
        int registeredSensorId = sensorService.addMqttSensor(userId, addSensorRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Sensor registered. id=" + registeredSensorId);
    }

    /**
     * MQTT 센서 정보를 수정합니다.
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @param sensorRequest
     * @return 수정 완료한 센서 아이디
     */
    @PutMapping("/{sensorId}/update")
    @AdminOnly
    public ResponseEntity<String> updateMqttSensor(@RequestHeader(X_USER_ID) String userId, @PathVariable("sensorId") int sensorId, @RequestBody AddSensorRequest sensorRequest) {
        int updatedSensorId = sensorService.updateMqttSensor(sensorId, sensorRequest);
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
