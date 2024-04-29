package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.AddMqttSensorRequest;
import live.ioteatime.apiservice.dto.MqttSensorDto;
import live.ioteatime.apiservice.dto.SensorRequest;

import java.util.List;

public interface MqttSensorService {
    List<MqttSensorDto> getAllSupportedSensors();
    List<MqttSensorDto> getOrganizationSensorsByUserId(String userId);
    MqttSensorDto getSensorById(String userId, int sensorId);
    int addMqttSensor(String userId, AddMqttSensorRequest request);
    int updateMqttSensor(String userId, int sensorId, SensorRequest sensorRequest);
    void deleteSensorById(String userId, int sensorId);
}
