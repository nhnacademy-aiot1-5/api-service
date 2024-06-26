package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.annotation.VerifyOrganization;
import live.ioteatime.apiservice.dto.sensor.ModbusSensorDto;
import live.ioteatime.apiservice.dto.sensor.SensorRequest;
import live.ioteatime.apiservice.service.ModbusSensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sensors/modbus")
@Tag(name = "Modbus Sensor", description = "Modbus 센서 API")
public class ModbusSensorController {

    private static final String X_USER_ID = "X-USER-ID";
    private final ModbusSensorService modbusSensorService;

    /**
     * 등록 가능한 Modbus 센서 모델 리스트를 조회하는 메서드
     *
     * @return 200 OK, 등록 가능한 Modbus 센서 모델 리스트
     */
    @GetMapping("/supported")
    @AdminOnly
    @Operation(summary = "지원하는 Modbus 센서 리스트 조회", description = "서비스에서 지원하는 모든 Modbus 센서 리스트를 조회합니다.")
    public ResponseEntity<List<ModbusSensorDto>> getSupportedModbusSensors() {
        return ResponseEntity.status(HttpStatus.OK).body(modbusSensorService.getAllSupportedSensors());
    }


    /**
     * 회원이 소속한 조직의 Modbus 센서 리스트를 조회하는 메서드
     *
     * @return 200 OK, Modbus 센서 리스트
     */
    @GetMapping("/list")
    @Operation(summary = "Modbus 센서 리스트 조회", description = "소속 조직의 모든 Modbus 센서 리스트를 조회합니다.")
    public ResponseEntity<List<ModbusSensorDto>> getModbusSensors(@RequestHeader(X_USER_ID) String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(modbusSensorService.getOrganizationSensorsByUserId(userId));
    }

    /**
     * Modbus 센서 단일 조회 메서드
     *
     * @param sensorId 센서 아이디
     * @return 200 OK, 센서 정보
     */
    @GetMapping("/{sensorId}")
    @VerifyOrganization
    @Operation(summary = "Modbus 센서 단일 조회", description = "Modbus 센서를 조회합니다.")
    public ResponseEntity<ModbusSensorDto> getModbusSensor(@PathVariable("sensorId") int sensorId) {
        return ResponseEntity.status(HttpStatus.OK).body(modbusSensorService.getSensorById(sensorId));
    }

    /**
     * Modbus 센서를 등록하는 메서드
     *
     * @return 201 CREATED, 등록된 센서 아이디
     */
    @PostMapping
    @AdminOnly
    @Operation(summary = "Modbus 센서 등록", description = "Modbus 센서를 등록합니다.")
    public ResponseEntity<String> addModbusSensor(@RequestHeader(X_USER_ID) String userId,
                                                  @RequestBody SensorRequest addSensorRequest) {
        int createdSensorId = modbusSensorService.addModbusSensor(userId, addSensorRequest);

        URI location = UriComponentsBuilder
                .fromUriString("https://ioteatime.live/sensors/modbus/" + createdSensorId)
                .build().toUri();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Sensor registered: id=" + location);
    }

    /**
     * Modbus 센서 정보를 수정합니다.
     *
     * @param sensorId 센서 아이디
     * @param updateSensorRequest 센서 수정 요청
     * @return 200 OK
     */
    @PutMapping("/{sensorId}")
    @AdminOnly
    @VerifyOrganization
    @Operation(summary = "Modbus 센서 정보 수정", description = "Modbus 센서의 이름, ip, port를 수정합니다.")
    public ResponseEntity<String> updateModbusSensor(@PathVariable("sensorId") int sensorId,
                                                     @RequestBody SensorRequest updateSensorRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Sensor updated. id=" + modbusSensorService.updateModbusSensor(sensorId, updateSensorRequest));
    }

    @PutMapping("/health")
    @AdminOnly
    @VerifyOrganization
    @Operation(summary = "Modbus 센서 동작 상태 변경",
            description = "Modbus 센서 동작 상태를 변경합니다. 센서 동작 상태가 on이라면 off로 변경하고, off라면 on으로 변경합니다.")
    public ResponseEntity<String> updateHealth(int sensorId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Sensor health changed" + modbusSensorService.updateHealth(sensorId));
    }

    /**
     * Modbus 센서를 데이터베이스에서 삭제합니다.
     *
     * @param sensorId 센서아이디
     * @return 204 NO CONTENT
     */
    @DeleteMapping("/{sensorId}")
    @AdminOnly
    @VerifyOrganization
    @Operation(summary = "Modbus 센서 삭제", description = "Modbus 센서를 삭제합니다.")
    public ResponseEntity<String> deleteModbusSensor(@PathVariable("sensorId") int sensorId) {
        modbusSensorService.deleteSensorById(sensorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
