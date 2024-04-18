package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Alive;
import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Sensor;
import live.ioteatime.apiservice.domain.SupportedSensor;
import live.ioteatime.apiservice.dto.AddSensorRequest;
import live.ioteatime.apiservice.dto.SensorDto;
import live.ioteatime.apiservice.exception.OrganizationNotFoundException;
import live.ioteatime.apiservice.exception.SensorNotFoundException;
import live.ioteatime.apiservice.exception.SensorNotSupportedException;
import live.ioteatime.apiservice.repository.AdminRepository;
import live.ioteatime.apiservice.repository.SensorRepository;
import live.ioteatime.apiservice.repository.SupportedSensorRepository;
import live.ioteatime.apiservice.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;
    private final SupportedSensorRepository supportedSensorRepository;
    private final AdminRepository adminRepository;

    /**
     *
     * @return 지원하는 센서 목록을 반환합니다. 없다면 null을 리턴합니다.
     */
    @Override
    public List<SensorDto> getAllSupportedSensors() {
        List<SupportedSensor> supportedSensorList = supportedSensorRepository.findAll();

        List<SensorDto> sensorDtoList = new ArrayList<>();
        for(SupportedSensor supportedSensor : supportedSensorList) {
            SensorDto sensorDto = new SensorDto();
            BeanUtils.copyProperties(supportedSensor, sensorDto);
            sensorDtoList.add(sensorDto);
        }

        return sensorDtoList;
    }

    /**
     *
     * @param organizationId 소속 조직 아이디
     * @return 조직이 보유한 센서 리스트를 반환합니다. 없다면 null을 리턴합니다.
     */
    @Override
    public List<SensorDto> getSensorsByOrganizationId(int organizationId) {
        List<Sensor> sensorList = sensorRepository.findAllByOrganization_Id(organizationId);

        List<SensorDto> sensorDtoList = new ArrayList<>();
        for(Sensor sensor : sensorList) {
            SensorDto sensorDto = new SensorDto();
            BeanUtils.copyProperties(sensor, sensorDto);
            sensorDtoList.add(sensorDto);
        }

        return sensorDtoList;
    }

    /**
     *
     * @param sensorId 센서 아이디
     * @return 센서 정보를 리턴합니다. 없다면 SensorNotFoundException을 던집니다.
     */
    @Override
    public SensorDto getSensorById(int sensorId) {
        Sensor sensor = sensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);
        SensorDto sensorDto = new SensorDto();
        BeanUtils.copyProperties(sensor, sensorDto);
        return sensorDto;
    }

    /**
     * 서버에서 지원하는 센서 모델인지 체크 후 등록 가능한 경우, 센서를 데이터베이스에 등록합니다.
     * @param userId 어드민 유저 아이디
     * @param request 센서 등록 요청 폼 데이터
     * @return 등록한 센서 아이디
     */
    @Override
    public int addMqttSensor(String userId, AddSensorRequest request) {

        log.debug("Add sensor request - model_name: {}", request.getModelName());
        if(!supportedSensorRepository.existsByModelName(request.getModelName())){
            throw new SensorNotSupportedException();
        }
        Organization organization = adminRepository.findById(userId).get().getOrganization();
        if(Objects.isNull(organization)) {
            throw new OrganizationNotFoundException();
        }

        Sensor sensor = new Sensor();
        BeanUtils.copyProperties(request, sensor);
        sensor.setAlive(Alive.DOWN);
        sensor.setOrganization(organization);

        sensorRepository.save(sensor);

        return sensor.getId();
    }

    /**
     * 센서 정보를 수정합니다. sensor_name, ip, port만 수정 가능합니다.
     * @param sensorId 센서아이디
     * @param sensorRequest 센서 수정 요청
     * @return 수정한 센서의 아이디를 반환합니다.
     */
    @Override
    public int updateMqttSensor(int sensorId, AddSensorRequest sensorRequest) {
        Sensor sensor = sensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);

        // todo sensorRequest.getId != sensorId 인 경우, 400 bad request
        // sensorRequest의 필드 중 하나라도 null인 경우, 400 bad request

        sensor.setName(sensorRequest.getName());
        sensor.setIp(sensorRequest.getIp());
        sensor.setPort(sensorRequest.getPort());

        sensorRepository.save(sensor);

        return sensor.getId();
    }

    @Override
    public void deleteSensorById(int sensorId) {
        sensorRepository.deleteById(sensorId);
    }


}
