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

    /**
     * Modbus프로토콜을 지원하는 센서 리스트를 반환합니다.
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
     * Controller의 getMoubusSensors에 사용되는 메서드로 userId에서 조직 정보를 가져와 조직이 보유한 센서 리스트를 가져옵니다.
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
     * Controller의 getModbusSensor에 사용되는 메서드로 sensorId에 해당하는 modbus 센서를 단일 조회합니다.
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
     * 모드버스 센서를 추가하는 서비스로 센서의 정보를 통해서 센서를 추가하는 서비스이빈다.
     * @param userId           userId의 조직 정보를 통해 해당 하는 조직을 가져옵니다.
     * @param addSensorRequest 추가할 센서의 정보를 담고 있습니다.
     * @return 추가된 센서의 sensorId를 반환합니다.
     */
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

    /**
     * Controller의 updateModbus에 사용되는 메서드로 센서 정보를 수정하며, 센서 이름, ip, port만 수정 가능합니다.
     * @param sensorId            수정할 센서의 아이디입니다.
     * @param updateSensorRequest 수정할 센서의 정보가 담겨있는 Request입니다.
     * @return
     */
    @Override
    public int updateModbusSensor(int sensorId, SensorRequest updateSensorRequest) {
        ModbusSensor sensor = sensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);

        sensor.setSensorName(updateSensorRequest.getSensorName());
        sensor.setIp(updateSensorRequest.getIp());
        sensor.setPort(updateSensorRequest.getPort());

        sensorRepository.save(sensor);

        return sensor.getId();
    }

    /**
     * Controller의 updateHealth에 사용되는 메서드로 센서의 작동상태를 수정합니다.
     * @param sensorId 작동상태를 수정할 센서의 아이디입니다.
     * @return 수정한 센서의 아이디를 리턴합니다.
     */
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

    /**
     * Controller의 deleteModbusSensor에 해당하는 메서드로 센서 아이디에 해당하는 센서를 삭제합니다.
     * @param sensorId 삭제할 센서의 아이디입니다.
     */
    @Override
    public void deleteSensorById(int sensorId) {
        sensorRepository.deleteById(sensorId);
        modbusSensorAdaptor.deleteModbusSensor("modbus"+sensorId);
        modbusSensorAdaptor.getUpdateCheck();
        log.debug("Send request to Rule Engine: URL=/delete/modbus/modbus{}, method=GET", sensorId);
    }
}
