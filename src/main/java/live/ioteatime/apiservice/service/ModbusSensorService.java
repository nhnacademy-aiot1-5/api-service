package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.ModbusSensorDto;
import live.ioteatime.apiservice.dto.SensorRequest;
import live.ioteatime.apiservice.dto.SensorDto;

import java.util.List;

public interface ModbusSensorService {
    List<ModbusSensorDto> getOrganizationSensorsByUserId(String userId);



    ModbusSensorDto getSensorById(int sensorId);
}
