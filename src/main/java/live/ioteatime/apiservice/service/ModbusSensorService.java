package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.sensor.ModbusSensorDto;
import live.ioteatime.apiservice.dto.sensor.SensorRequest;

import java.util.List;

public interface ModbusSensorService {
    List<ModbusSensorDto> getAllSupportedSensors();

    List<ModbusSensorDto> getOrganizationSensorsByUserId(String userId);

    ModbusSensorDto getSensorById(int sensorId);

    int addModbusSensor(String userId, SensorRequest addSensorRequest);

    int updateModbusSensor(int sensorId, SensorRequest updateSensorRequest);

    int updateHealth(int sensorId);

    void deleteSensorById(int sensorId);
}
