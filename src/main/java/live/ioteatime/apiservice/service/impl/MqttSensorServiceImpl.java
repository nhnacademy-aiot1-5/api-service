package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.adaptor.SensorAdaptor;
import live.ioteatime.apiservice.domain.*;
import live.ioteatime.apiservice.dto.AddBrokerRequest;
import live.ioteatime.apiservice.dto.MqttSensorDto;
import live.ioteatime.apiservice.dto.SensorRequest;
import live.ioteatime.apiservice.exception.*;
import live.ioteatime.apiservice.repository.MqttSensorRepository;
import live.ioteatime.apiservice.repository.PlaceRepository;
import live.ioteatime.apiservice.repository.SupportedSensorRepository;
import live.ioteatime.apiservice.repository.UserRepository;
import live.ioteatime.apiservice.service.MqttSensorService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Getter @Setter
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MqttSensorServiceImpl implements MqttSensorService {

    private final SupportedSensorRepository supportedSensorRepository;
    private final UserRepository userRepository;
    private final MqttSensorRepository sensorRepository;
    private final PlaceRepository placeRepository;
    private final SensorAdaptor sensorAdaptor;

    /**
     *
     * @return 지원하는 센서 목록을 반환합니다. 없다면 null을 리턴합니다.
     */
    @Override
    public List<MqttSensorDto> getAllSupportedSensors() {
        List<SupportedSensor> supportedSensorList = supportedSensorRepository.findAllByProtocol(Protocol.MQTT);

        List<MqttSensorDto> sensorDtoList = new ArrayList<>();
        for(SupportedSensor supportedSensor : supportedSensorList) {
            MqttSensorDto sensorDto = new MqttSensorDto();
            BeanUtils.copyProperties(supportedSensor, sensorDto);
            sensorDtoList.add(sensorDto);
        }

        return sensorDtoList;
    }

    /**
     *
     * @param userId 유저아이디
     * @return 조직이 보유한 MQTT 센서 리스트를 반환합니다. 없다면 null을 리턴합니다.
     */
    @Override
    public List<MqttSensorDto> getOrganizationSensorsByUserId(String userId) {

        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        int organizationId = user.getOrganization().getId();

        List<MqttSensor> sensorList = sensorRepository.findAllByOrganization_Id(organizationId);

        List<MqttSensorDto> sensorDtoList = new ArrayList<>();
        for(MqttSensor sensor : sensorList) {
            MqttSensorDto sensorDto = new MqttSensorDto();
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
    public MqttSensorDto getSensorById(int sensorId) {
        MqttSensor sensor = sensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);
        MqttSensorDto sensorDto = new MqttSensorDto();
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
    public int addMqttSensor(String userId, SensorRequest request) {

        if(!supportedSensorRepository.existsByModelName(request.getModelName())){
            throw new SensorNotSupportedException();
        }
        Organization organization = userRepository.findById(userId).get().getOrganization();
        if(Objects.isNull(organization)) {
            throw new OrganizationNotFoundException();
        }
        Place place = placeRepository.findById(request.getPlaceId()).orElseThrow(()->new RuntimeException());

        MqttSensor sensor = new MqttSensor();
        BeanUtils.copyProperties(request, sensor);
        sensor.setAlive(Alive.DOWN);
        sensor.setOrganization(organization);
        sensor.setPlace(place);

        MqttSensor savedSensor = sensorRepository.save(sensor);

        AddBrokerRequest addBrokerRequest = new AddBrokerRequest();
        String mqttHost = "tcp://" + request.getIp() + ":" + request.getPort();
        String mqttId = "mqtt" +  savedSensor.getId();
        addBrokerRequest.setMqttHost(mqttHost);
        addBrokerRequest.setMqttId(mqttId);

        sensorAdaptor.addBrokers(addBrokerRequest);

        return savedSensor.getId();
    }

    /**
     * 센서 정보를 수정합니다. sensor_name, ip, port, place 만 수정 가능합니다.
     * @param sensorId 센서아이디
     * @param sensorRequest 센서 수정 요청
     * @return 수정한 센서의 아이디를 반환합니다.
     */
    @Override
    public int updateMqttSensor(int sensorId, SensorRequest sensorRequest) {
        MqttSensor sensor = sensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);

        sensor.setName(sensorRequest.getName());
        sensor.setIp(sensorRequest.getIp());
        sensor.setPort(sensorRequest.getPort());

        Place place = placeRepository.findById(sensorRequest.getPlaceId()).orElseThrow(PlaceNotFoundException::new);
        sensor.setPlace(place);

        sensorRepository.save(sensor);

        return sensor.getId();
    }

    @Override
    public void deleteSensorById(int sensorId) {
        sensorRepository.deleteById(sensorId);
    }

}
