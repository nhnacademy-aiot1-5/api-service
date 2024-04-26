package live.ioteatime.apiservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.dto.MqttSensorDto;
import live.ioteatime.apiservice.dto.SensorRequest;
import live.ioteatime.apiservice.service.MqttSensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/sensors/mqtt")
@Tag(name = "Mqtt 센서 컨트롤러", description = "Mqtt 센서 관리에 사용하는 컨트롤러입니다.")
@RequiredArgsConstructor
public class MqttSensorController {

    private static final String X_USER_ID = "X-USER-ID";
    private final MqttSensorService mqttSensorService;


    /**
     * 등록 가능한 MQTT 센서 모델 리스트를 반환합니다.
     * @param userId 요청을 보내는 ADMIN 유저의 아이디
     * @return 200 OK
     */
    @GetMapping("/supported")
    @AdminOnly
    @Operation(summary = "지원하는 MQTT 센서 리스트를 가져오는 API", description = "지원하는 모든 MQTT 센서 리스트를 반환합니다.")
    public ResponseEntity<List<MqttSensorDto>> getSupportedMqttSensors(@RequestHeader(X_USER_ID) String userId) {
        List<MqttSensorDto> sensorList = mqttSensorService.getAllSupportedSensors();
        return ResponseEntity.ok(sensorList);
    }

    /**
     * 유저가 소속한 조직의 Mqtt 센서 리스트를 반환합니다.
     * @return 200 OK
     */
    @GetMapping("/list")
    @Operation(summary = "모든 mqtt 센서들의 리스트를 가져오는 API", description = "소속 조직의 모든 mqtt 센서 리스트를 반환합니다.")
    public ResponseEntity<List<MqttSensorDto>> getMqttSensors(@RequestHeader(X_USER_ID) String userId){
        return ResponseEntity.ok(mqttSensorService.getOrganizationSensorsByUserId(userId));
    }

    /**
     * 단일 mqtt 센서 정보를 반환합니다.
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @return 성공 - 200 OK, 실패 - 404 NOT FOUND
     */
    @GetMapping("/{sensorId}")
    @Operation(summary = "MQTT 센서 단일 조회 API")
    public ResponseEntity<MqttSensorDto> getMqttSensor(@RequestHeader(X_USER_ID) String userId, @PathVariable("sensorId") int sensorId) {
        return ResponseEntity.ok(mqttSensorService.getSensorById(sensorId));
    }

    /**
     * 새 MQTT 센서를 추가합니다.
     * @return 성공 - 201 CREATED, 실패 - 400 BAD REQUEST
     */
    @PostMapping
    @AdminOnly
    @Transactional
    @Operation(summary = "MQTT 센서를 추가하는 API", description = "MQTT 센서를 추가합니다.")
    public ResponseEntity<String> addMqttSensor(@RequestHeader(X_USER_ID) String userId, @RequestBody SensorRequest addSensorRequest){
        int registeredSensorId = mqttSensorService.addMqttSensor(userId, addSensorRequest);
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
                                                   @RequestBody SensorRequest updateSensorRequest) {

        int updatedSensorId = mqttSensorService.updateMqttSensor(sensorId, updateSensorRequest);
        return ResponseEntity.ok().body("Sensor updated. id=" + updatedSensorId);
    }

    /**
     * Mqtt 센서를 데이터베이스에서 삭제합니다.
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @return 204 NO CONTENT
     */
    @DeleteMapping("/{sensorId}")
    @AdminOnly
    public ResponseEntity<String> deleteMqttSensor(@RequestHeader(X_USER_ID) String userId, @PathVariable("sensorId") int sensorId) {
        mqttSensorService.deleteSensorById(sensorId);
        return ResponseEntity.noContent().build();
    }


}
