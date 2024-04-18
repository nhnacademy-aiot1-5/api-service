package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.AddSensorRequest;
import live.ioteatime.apiservice.dto.SensorDto;

import java.util.List;

public interface SensorService {
    List<SensorDto> getAllSupportedSensors();
    List<SensorDto> getSensorsByOrganizationId(int organizationId);
    SensorDto getSensorById(int sensorId);
    int addMqttSensor(String userId, AddSensorRequest request);
    int updateMqttSensor(int sensorId, AddSensorRequest sensorRequest);

    void deleteSensorById(int sensorId);
}
