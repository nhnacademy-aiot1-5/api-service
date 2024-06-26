package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.adaptor.ModbusSensorAdaptor;
import live.ioteatime.apiservice.domain.*;
import live.ioteatime.apiservice.dto.sensor.ModbusSensorDto;
import live.ioteatime.apiservice.dto.sensor.SensorRequest;
import live.ioteatime.apiservice.exception.SensorNotFoundException;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.repository.ModbusSensorRepository;
import live.ioteatime.apiservice.repository.SupportedSensorRepository;
import live.ioteatime.apiservice.repository.UserRepository;
import live.ioteatime.apiservice.service.ModbusSensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ModbusSensorServiceImpl implements ModbusSensorService {

    private final ModbusSensorRepository sensorRepository;
    private final UserRepository userRepository;
    private final SupportedSensorRepository supportedSensorRepository;
    private final ModbusSensorAdaptor modbusSensorAdaptor;

    @Override
    public List<ModbusSensorDto> getAllSupportedSensors() {
        List<SupportedSensor> supportedSensorList = supportedSensorRepository.findAllByProtocol(Protocol.MODBUS);

        List<ModbusSensorDto> sensorDtoList = new ArrayList<>();
        for (SupportedSensor supportedSensor : supportedSensorList) {
            ModbusSensorDto sensorDto = new ModbusSensorDto();
            BeanUtils.copyProperties(supportedSensor, sensorDto);
            sensorDtoList.add(sensorDto);
        }

        return sensorDtoList;
    }

    @Override
    public List<ModbusSensorDto> getOrganizationSensorsByUserId(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        int organizationId = user.getOrganization().getId();

        List<ModbusSensor> sensorList = sensorRepository.findAllByOrganization_Id(organizationId);

        List<ModbusSensorDto> sensorDtoList = new ArrayList<>();
        for (ModbusSensor sensor : sensorList) {
            ModbusSensorDto sensorDto = new ModbusSensorDto();
            BeanUtils.copyProperties(sensor, sensorDto);
            sensorDtoList.add(sensorDto);
        }

        return sensorDtoList;
    }

    @Override
    public ModbusSensorDto getSensorById(int sensorId) {
        ModbusSensor sensor = sensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);
        ModbusSensorDto sensorDto = new ModbusSensorDto();
        BeanUtils.copyProperties(sensor, sensorDto);
        return sensorDto;
    }

    @Override
    public int addModbusSensor(String userId, SensorRequest addSensorRequest) {

        ModbusSensor modbusSensor = new ModbusSensor();
        BeanUtils.copyProperties(addSensorRequest, modbusSensor);
        modbusSensor.setAlive(Alive.DOWN);
        Organization organization = userRepository.findById(userId).get().getOrganization();
        modbusSensor.setOrganization(organization);

        ModbusSensor savedSensor = sensorRepository.save(modbusSensor);

        return savedSensor.getId();
    }

    @Override
    public int updateModbusSensor(int sensorId, SensorRequest updateSensorRequest) {
        ModbusSensor sensor = sensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);

        sensor.setSensorName(updateSensorRequest.getSensorName());
        sensor.setIp(updateSensorRequest.getIp());
        sensor.setPort(updateSensorRequest.getPort());

        sensorRepository.save(sensor);

        return sensor.getId();
    }

    @Override
    public int updateHealth(int sensorId) {
        ModbusSensor modbusSensor = sensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);
        if ((modbusSensor.getAlive().equals(Alive.UP))) {
            modbusSensor.setAlive(Alive.DOWN);
        } else {
            modbusSensor.setAlive(Alive.UP);
        }
        sensorRepository.save(modbusSensor);
        return sensorId;
    }

    @Override
    public void deleteSensorById(int sensorId) {
        sensorRepository.deleteById(sensorId);
        modbusSensorAdaptor.deleteModbusSensor("modbus"+sensorId);
        modbusSensorAdaptor.getUpdateCheck();
        log.info("Send request to Rule Engine: URL=/delete/modbus/modbus{}, method=GET", sensorId);
    }
}
