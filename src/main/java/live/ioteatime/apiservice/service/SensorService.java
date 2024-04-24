package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.domain.Sensor;
import live.ioteatime.apiservice.dto.AddSensorRequest;

import java.util.List;

public interface SensorService {
    List<Sensor> getSensors();
    int addMqttSensor(String userId, AddSensorRequest request);
}
