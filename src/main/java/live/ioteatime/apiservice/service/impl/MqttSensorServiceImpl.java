package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.adaptor.MqttSensorAdaptor;
import live.ioteatime.apiservice.domain.*;
import live.ioteatime.apiservice.dto.AddBrokerRequest;
import live.ioteatime.apiservice.dto.sensor.MqttSensorDto;
import live.ioteatime.apiservice.dto.sensor.MqttSensorRequest;
import live.ioteatime.apiservice.exception.*;
import live.ioteatime.apiservice.repository.*;
import live.ioteatime.apiservice.service.MqttSensorService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Getter @Setter
@Transactional
@RequiredArgsConstructor
public class MqttSensorServiceImpl implements MqttSensorService {

    private final MqttSensorAdaptor sensorAdaptor;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final PlaceRepository placeRepository;
    private final MqttSensorRepository sensorRepository;
    private final SupportedSensorRepository supportedSensorRepository;

    /**
     *
     * @return 지원하는 센서 목록을 반환합니다. 없다면 null 을 리턴합니다.
     */
    @Override
    public List<MqttSensorDto> getAllSupportedSensors() {
        return supportedSensorRepository.findAllByProtocol(Protocol.MQTT)
                .stream()
                .map(sensor -> {
                    MqttSensorDto sensorDto = new MqttSensorDto();
                    BeanUtils.copyProperties(sensor, sensorDto);
                    return sensorDto;
                })
                .collect(Collectors.toList());
    }

    /**
     *
     * @param userId 유저아이디
     * @return 조직이 보유한 MQTT 센서 리스트를 반환합니다. 없다면 null 을 리턴합니다.
     */
    @Override
    public List<MqttSensorDto> getOrganizationSensorsByUserId(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));
        Organization organization = user.getOrganization();
        if(Objects.isNull(organization)){
            throw new OrganizationNotFoundException();
        }

        return sensorRepository.findAllByOrganization_Id(organization.getId())
                .stream()
                .map(sensor -> {
                    MqttSensorDto sensorDto = new MqttSensorDto();
                    BeanUtils.copyProperties(sensor, sensorDto);
                    BeanUtils.copyProperties(sensor.getPlace(), sensorDto.getPlace());
                    return sensorDto;
                })
                .collect(Collectors.toList());

    }

    /**
     *
     * @param sensorId 센서 아이디
     * @return 센서 정보를 리턴합니다. 없다면 SensorNotFoundException 을 던집니다.
     */
    @Override
    public MqttSensorDto getSensorById(int sensorId) {
        MqttSensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(SensorNotFoundException::new);

        MqttSensorDto sensorDto = new MqttSensorDto();
        BeanUtils.copyProperties(sensor, sensorDto);
        BeanUtils.copyProperties(sensor.getPlace(), sensorDto.getPlace());

        return sensorDto;

    }

    /**
     * 서버에서 지원하는 센서 모델인지 체크 후 등록 가능한 경우, 센서를 데이터베이스에 등록합니다.
     * @param userId 어드민 유저 아이디
     * @param request 센서 등록 요청 폼 데이터
     * @return 등록한 센서 아이디
     */
    @Override
    public int addMqttSensor(String userId, MqttSensorRequest request) {

        if(!supportedSensorRepository.existsByModelName(request.getModelName())){
            throw new SensorNotSupportedException();
        }

        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException(userId));
        Organization organization = user.getOrganization();
        if(organization == null){
            throw new OrganizationNotFoundException();
        }

        Place place = placeRepository.findById(request.getPlaceId()).orElseThrow(PlaceNotFoundException::new);

        if(place.getOrganization().getId() != organization.getId()){
            throw new UnauthorizedException();
        }

        MqttSensor sensor = new MqttSensor();
        sensor.setInitialValues(request, organization, place);
        MqttSensor savedSensor = sensorRepository.save(sensor);

        String topic = request.getTopic();
        String desc = request.getDescription();
        if (Objects.isNull(topic)){
            throw new IllegalArgumentException();
        }
        topicRepository.save(new Topic(topic, desc, savedSensor));

        sendRequestToRuleEngine(savedSensor);

        return savedSensor.getId();
    }

    /**
     * 센서 정보를 수정합니다. sensor_name, ip, port, place 만 수정 가능합니다.
     * @param sensorId 센서아이디
     * @param updateRequest 센서 수정 요청
     * @return 수정한 센서의 아이디를 반환합니다.
     */
    @Override
    public int updateMqttSensor(int sensorId, MqttSensorRequest updateRequest) {
        Place place = placeRepository.findById(updateRequest.getPlaceId()).orElseThrow(PlaceNotFoundException::new);
        MqttSensor sensor = sensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);
        sensor.updateSensorInfo(updateRequest.getName(), updateRequest.getIp(), updateRequest.getPort(), place);
        MqttSensor savedSensor = sensorRepository.save(sensor);
        sendRequestToRuleEngine(savedSensor);

        return sensor.getId();
    }

    @Override
    public void deleteSensorById(int sensorId) {

        topicRepository.findAllByMqttSensor_Id(sensorId)
                                .forEach(t -> topicRepository.deleteById(t.getId()));

        sensorRepository.deleteById(sensorId);
    }

    /**
     * 센서 등록, 수정, 토픽 등록, 수정, 삭제시 룰엔진에 요청을 보냅니다.
     * @param sensor 센서 엔티티
     */
    private void sendRequestToRuleEngine(MqttSensor sensor){
        AddBrokerRequest addBrokerRequest = new AddBrokerRequest();
        String mqttHost = "tcp://" + sensor.getIp() + ":" + sensor.getPort();
        String mqttId = "mqtt" +  sensor.getId();
        addBrokerRequest.setMqttHost(mqttHost);
        addBrokerRequest.setMqttId(mqttId);

        List<String> topicValueList = topicRepository.findAllByMqttSensor_Id(sensor.getId())
                .stream()
                .map(Topic::getTopic)
                .collect(Collectors.toList());

        addBrokerRequest.setMqttTopic(topicValueList);

        sensorAdaptor.addMqttBrokers(addBrokerRequest);

    }

}
