package live.ioteatime.apiservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.dto.sensor.MqttSensorDto;
import live.ioteatime.apiservice.dto.sensor.MqttSensorRequest;
import live.ioteatime.apiservice.service.MqttSensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sensors/mqtt")
@Tag(name = "Mqtt Sensor", description = "MQTT 센서 API")
@RequiredArgsConstructor
public class MqttSensorController {

    private static final String X_USER_ID = "X-USER-ID";
    private final MqttSensorService mqttSensorService;

    /**
     * 등록 가능한 MQTT 센서 모델 리스트를 조회하는 메서드
     *
     * @return 200 OK, 등록 가능한 MQTT 센서 모델 리스트
     */
    @GetMapping("/supported")
    @AdminOnly
    @Operation(summary = "지원하는 MQTT 센서 리스트 조회", description = "서비스에서 지원하는 모든 MQTT 센서 리스트를 조회합니다.")
    public ResponseEntity<List<MqttSensorDto>> getSupportedMqttSensors() {
        return ResponseEntity.status(HttpStatus.OK).body(mqttSensorService.getAllSupportedSensors());
    }

    /**
     * 회원이 소속된 조직의 MQTT 센서 리스트를 조회하는 메서드
     *
     * @return 200 OK, MQTT 센서 리스트
     */
    @GetMapping("/list")
    @Operation(summary = "MQTT 센서 리스트 조회", description = "소속 조직의 모든 MQTT 센서 리스트를 조회합니다.")
    public ResponseEntity<List<MqttSensorDto>> getMqttSensors(@RequestHeader(X_USER_ID) String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                mqttSensorService.getOrganizationSensorsByUserId(userId));
    }

    /**
     * MQTT 센서 단일 조회 메서드
     *
     * @param sensorId 센서 아이디
     * @return 200 OK, 센서 정보
     */
    @GetMapping("/{sensorId}")
    @Operation(summary = "MQTT 센서 단일 조회", description = "MQTT 센서를 조회합니다.")
    public ResponseEntity<MqttSensorDto> getMqttSensor(@PathVariable("sensorId") int sensorId) {
        return ResponseEntity.status(HttpStatus.OK).body(mqttSensorService.getSensorById(sensorId));
    }

    /**
     * MQTT 센서를 등록하는 메서드
     *
     * @return 201 CREATED, 등록된 센서 아이디
     */
    @PostMapping
    @AdminOnly
    @Operation(summary = "MQTT 센서 등록", description = "MQTT 센서를 등록합니다.")
    public ResponseEntity<String> createMqttSensor(@RequestHeader(X_USER_ID) String userId,
                                                   @RequestBody MqttSensorRequest addSensorRequest) {
        int createdSensorId = mqttSensorService.addMqttSensor(userId, addSensorRequest);

        URI location = UriComponentsBuilder
                .fromUriString("https://ioteatime.live/sensors/mqtt/" + createdSensorId)
                .build().toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location)
                .body("Sensor registered: id=" + createdSensorId);
    }

    /**
     * MQTT 센서 정보를 수정하는 메서드
     *
     * @param sensorId 센서 아이디
     * @param updateSensorRequest 수정할 센서의 정보
     * @return 200 OK, 수정된 센서 아이디
     */
    @PutMapping("/{sensorId}")
    @AdminOnly
    @Operation(summary = "MQTT 센서 정보 수정", description = "MQTT 센서의 이름, ip, port, 장소를 수정합니다.")
    public ResponseEntity<String> updateMqttSensor(@PathVariable("sensorId") int sensorId,
                                                   @RequestBody MqttSensorRequest updateSensorRequest) {
        return ResponseEntity.ok().body("Sensor updated: id=" +
                mqttSensorService.updateMqttSensor(sensorId, updateSensorRequest));
    }

    /**
     * MQTT 센서를 삭제하는 메서드
     *
     * @param sensorId 센서 아이디
     * @return 204 NO CONTENT
     */
    @DeleteMapping("/{sensorId}")
    @AdminOnly
    @Operation(summary = "MQTT 센서 삭제", description = "MQTT 센서를 삭제합니다.")
    public ResponseEntity<String> deleteMqttSensor(@PathVariable("sensorId") int sensorId) {
        mqttSensorService.deleteSensorById(sensorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
