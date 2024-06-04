package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.sensor.ModbusSensorDto;
import live.ioteatime.apiservice.dto.sensor.SensorRequest;

import java.util.List;

/**
 * 모드버스 센서 서비스에 필요한 기능을 구현한 서비스 입니다.
 */
public interface ModbusSensorService {

    /**
     * Modbus프로토콜을 지원하는 센서 리스트를 반환합니다.
     * @return 지원하는 센서 목록을 반환합니다. 없다면 null을 리턴합니다.
     */
    List<ModbusSensorDto> getAllSupportedSensors();

    /**
     * Controller의 getMoubusSensors에 사용되는 메서드로 userId에서 조직 정보를 가져와 조직이 보유한 센서 리스트를 가져옵니다.
     * @param userId 유저아이디
     * @return 조직이 보유한 센서 리스트를 반환합니다. 없다면 null을 리턴합니다.
     */
    List<ModbusSensorDto> getOrganizationSensorsByUserId(String userId);

    /**
     * Controller의 getModbusSensor에 사용되는 메서드로 sensorId에 해당하는 modbus 센서를 단일 조회합니다.
     * @param sensorId 센서아이디
     * @return 센서 정보
     */
    ModbusSensorDto getSensorById(int sensorId);

    /**
     * 모드버스 센서를 추가하는 서비스로 센서의 정보를 통해서 센서를 추가하는 서비스이빈다.
     * @param userId           userId의 조직 정보를 통해 해당 하는 조직을 가져옵니다.
     * @param addSensorRequest 추가할 센서의 정보를 담고 있습니다.
     * @return 추가된 센서의 sensorId를 반환합니다.
     */
    int addModbusSensor(String userId, SensorRequest addSensorRequest);

    /**
     * Controller의 updateModbus에 사용되는 메서드로 센서 정보를 수정하며, 센서 이름, ip, port만 수정 가능합니다.
     * @param sensorId            수정할 센서의 아이디입니다.
     * @param updateSensorRequest 수정할 센서의 정보가 담겨있는 Request입니다.
     * @return
     */
    int updateModbusSensor(int sensorId, SensorRequest updateSensorRequest);

    /**
     * Controller의 updateHealth에 사용되는 메서드로 센서의 작동상태를 수정합니다.
     * @param sensorId 작동상태를 수정할 센서의 아이디입니다.
     * @return 수정한 센서의 아이디를 리턴합니다.
     */
    int updateHealth(int sensorId);

    /**
     * Controller의 deleteModbusSensor에 해당하는 메서드로 센서 아이디에 해당하는 센서를 삭제합니다.
     * @param sensorId 삭제할 센서의 아이디입니다.
     */
    void deleteSensorById(int sensorId);
}
