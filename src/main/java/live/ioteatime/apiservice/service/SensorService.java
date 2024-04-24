package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.SensorRequest;
import live.ioteatime.apiservice.dto.SensorDto;

import java.util.List;

public interface SensorService {
    List<SensorDto> getAllSupportedSensors();
    List<SensorDto> getOrganizationSensorsByUserId(String userId);
    SensorDto getSensorById(int sensorId);
    int addMqttSensor(String userId, SensorRequest request);
    int updateMqttSensor(int sensorId, SensorRequest sensorRequest);
    void deleteSensorById(int sensorId);
}
