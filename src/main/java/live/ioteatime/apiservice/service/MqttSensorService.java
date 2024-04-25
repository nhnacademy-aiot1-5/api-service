package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.MqttSensorDto;
import live.ioteatime.apiservice.dto.SensorRequest;

import java.util.List;

public interface MqttSensorService {
    List<MqttSensorDto> getAllSupportedSensors();
    List<MqttSensorDto> getOrganizationSensorsByUserId(String userId);
    int addMqttSensor(String userId, SensorRequest request);
    int updateMqttSensor(int sensorId, SensorRequest sensorRequest);
    MqttSensorDto getSensorById(int sensorId);
    void deleteSensorById(int sensorId);
}
