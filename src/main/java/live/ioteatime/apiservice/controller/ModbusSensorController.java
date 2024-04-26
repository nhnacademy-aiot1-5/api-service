package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.dto.ModbusSensorDto;
import live.ioteatime.apiservice.dto.SensorRequest;
import live.ioteatime.apiservice.service.ModbusSensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensors/modbus")
@Tag(name = "Modbus 센서 컨트롤러", description = "Modbus 센서 관리에 사용하는 컨트롤러입니다.")
@RequiredArgsConstructor
public class ModbusSensorController {

    private static final String X_USER_ID = "X-USER-ID";
    private final ModbusSensorService modbusSensorService;


    /**
     * 등록 가능한 Modbus 센서 리스트를 반환합니다.
     * @param userId 요청을 보내는 ADMIN 유저의 아이디
     * @return 200 OK
     */
    @GetMapping("/supported")
    @AdminOnly
    @Operation(summary = "지원하는 Modbus 센서 리스트를 가져오는 API", description = "지원하는 모든 Modbus 센서 리스트를 반환합니다.")
    public ResponseEntity<List<ModbusSensorDto>> getSupportedModbusSensors(@RequestHeader(X_USER_ID) String userId) {
        List<ModbusSensorDto> sensorList = modbusSensorService.getAllSupportedSensors();
        return ResponseEntity.ok(sensorList);
    }


    /**
     * 유저가 소속한 조직의 Modbus 센서 리스트를 반환합니다.
     * @return 200 OK
     */
    @GetMapping("/list")
    @Operation(summary = "모든 modbus 센서들의 리스트를 가져오는 API", description = "소속 조직의 모든 modbus 센서 리스트를 반환합니다.")
    public ResponseEntity<List<ModbusSensorDto>> getModbusSensors(@RequestHeader(X_USER_ID) String userId){
        return ResponseEntity.ok(modbusSensorService.getOrganizationSensorsByUserId(userId));
    }

    /**
     * 단일 modbus 센서 정보를 반환합니다.
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @return 성공 - 200 OK, 실패 - 404 NOT FOUND
     */
    @GetMapping("/{sensorId}")
    @Operation(summary = "Modbus 센서 단일 조회 API")
    public ResponseEntity<ModbusSensorDto> getModbusSensor(@RequestHeader(X_USER_ID) String userId, @PathVariable("sensorId") int sensorId) {
        return ResponseEntity.ok(modbusSensorService.getSensorById(sensorId));
    }

    /**
     * 새 Modbus 센서를 추가합니다.
     * @return 성공 - 201 CREATED, 실패 - 400 BAD REQUEST
     */
    @PostMapping
    @AdminOnly
    @Operation(summary = "Modbus 센서를 추가하는 API", description = "Modbus 센서를 추가합니다.")
    public ResponseEntity<String> addModbusSensor(@RequestHeader(X_USER_ID) String userId, @RequestBody SensorRequest addSensorRequest){
        int registeredSensorId = modbusSensorService.addModbusSensor(userId, addSensorRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Sensor registered. id=" + registeredSensorId);
    }

    /**
     * Modbus 센서 정보를 수정합니다.
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @param updateSensorRequest 센서 수정 요청
     * @return 200 OK
     */
    @PutMapping("/{sensorId}/update")
    @AdminOnly
    public ResponseEntity<String> updateModbusSensor(@RequestHeader(X_USER_ID) String userId, @PathVariable("sensorId") int sensorId,
                                                    @RequestBody SensorRequest updateSensorRequest) {

        int updatedSensorId = modbusSensorService.updateMobusSensor(sensorId, updateSensorRequest);
        return ResponseEntity.ok().body("Sensor updated. id=" + updatedSensorId);
    }

    /**
     * Modbus 센서를 데이터베이스에서 삭제합니다.
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @return 204 NO CONTENT
     */
    @DeleteMapping("/{sensorId}")
    @AdminOnly
    public ResponseEntity<String> deleteModbusSensor(@RequestHeader(X_USER_ID) String userId, @PathVariable("sensorId") int sensorId) {
        modbusSensorService.deleteSensorById(sensorId);
        return ResponseEntity.noContent().build();
    }

}
