package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.*;
import live.ioteatime.apiservice.dto.ModbusSensorDto;
import live.ioteatime.apiservice.dto.SensorRequest;
import live.ioteatime.apiservice.exception.SensorNotFoundException;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.repository.ModbusSensorRepository;
import live.ioteatime.apiservice.repository.SupportedSensorRepository;
import live.ioteatime.apiservice.repository.UserRepository;
import live.ioteatime.apiservice.service.ChannelService;
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
    private final ChannelService channelService;


    /**
     *
     * @param userId 유저아이디
     * @return 조직이 보유한 센서 리스트를 반환합니다. 없다면 null을 리턴합니다.
     */
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

    /**
     * modbus 센서를 단일 조회합니다.
     * @param sensorId 센서아이디
     * @return 센서 정보
     */
    @Override
    public ModbusSensorDto getSensorById(int sensorId) {
        ModbusSensor sensor = sensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);
        ModbusSensorDto sensorDto = new ModbusSensorDto();
        BeanUtils.copyProperties(sensor, sensorDto);
        return sensorDto;
    }

    /**
     *
     * @return 지원하는 센서 목록을 반환합니다. 없다면 null을 리턴합니다.
     */
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

    /**
     * 센서 정보를 수정합니다. 센서 이름, ip, port만 수정 가능합니다.
     * @param sensorId
     * @param updateSensorRequest
     * @return
     */
    @Override
    public int updateMobusSensor(int sensorId, SensorRequest updateSensorRequest) {
        ModbusSensor sensor = sensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);

        sensor.setName(updateSensorRequest.getName());
        sensor.setIp(updateSensorRequest.getIp());
        sensor.setPort(updateSensorRequest.getPort());

        sensorRepository.save(sensor);

        return sensor.getId();
    }

    @Override
    public void deleteSensorById(int sensorId) {
        sensorRepository.deleteById(sensorId);
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
    public int addSensorWithChannels(String userId, SensorRequest addSensorRequest) {
        try {
            int sensorId = addModbusSensor(userId, addSensorRequest);
            channelService.createChannel(sensorId);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int updateWork(int sensorId) {
        ModbusSensor modbusSensor = sensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);
        if ((modbusSensor.getAlive().equals(Alive.UP))) {
            modbusSensor.setAlive(Alive.DOWN);
        } else {
            modbusSensor.setAlive(Alive.UP);
        }
        sensorRepository.save(modbusSensor);
        return sensorId;
    }
}
