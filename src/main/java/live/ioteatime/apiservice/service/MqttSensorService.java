package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.sensor.MqttSensorDto;
import live.ioteatime.apiservice.dto.sensor.MqttSensorRequest;

import java.util.List;

public interface MqttSensorService {
    List<MqttSensorDto> getAllSupportedSensors();

    List<MqttSensorDto> getOrganizationSensorsByUserId(String userId);

    MqttSensorDto getSensorById(int sensorId);

    int addMqttSensor(String userId, MqttSensorRequest request);

    int updateMqttSensor(int sensorId, MqttSensorRequest request);

    void deleteSensorById(int sensorId);
}
