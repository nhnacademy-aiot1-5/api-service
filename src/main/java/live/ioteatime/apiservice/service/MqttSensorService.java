package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.sensor.AddMqttSensorRequest;
import live.ioteatime.apiservice.dto.sensor.MqttSensorDto;
import live.ioteatime.apiservice.dto.sensor.SensorRequest;

import java.util.List;

public interface MqttSensorService {
    List<MqttSensorDto> getAllSupportedSensors();

    List<MqttSensorDto> getOrganizationSensorsByUserId(String userId);

    MqttSensorDto getSensorById(int sensorId);

    int addMqttSensor(String userId, AddMqttSensorRequest request);

    int updateMqttSensor(int sensorId, SensorRequest sensorRequest);

    void deleteSensorById(int sensorId);
}
