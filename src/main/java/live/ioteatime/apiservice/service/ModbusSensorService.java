package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.ModbusSensorDto;
import live.ioteatime.apiservice.dto.SensorRequest;

import java.util.List;

public interface ModbusSensorService {
    List<ModbusSensorDto> getOrganizationSensorsByUserId(String userId);

    ModbusSensorDto getSensorById(int sensorId);

    List<ModbusSensorDto> getAllSupportedSensors();

    int updateMobusSensor(int sensorId, SensorRequest updateSensorRequest);

    void deleteSensorById(int sensorId);

    int addModbusSensor(String userId, SensorRequest addSensorRequest);

    int addSensorWithChannels(String userId, SensorRequest addSensorRequest);

    int updateWork(int sensorId);
}
