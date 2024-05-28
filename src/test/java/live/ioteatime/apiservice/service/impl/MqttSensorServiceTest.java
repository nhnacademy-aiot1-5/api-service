package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.adaptor.MqttSensorAdaptor;
import live.ioteatime.apiservice.domain.*;
import live.ioteatime.apiservice.dto.sensor.MqttSensorDto;
import live.ioteatime.apiservice.dto.sensor.MqttSensorRequest;
import live.ioteatime.apiservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

class MqttSensorServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    TopicRepository topicRepository;
    @Mock
    PlaceRepository placeRepository;
    @Mock
    MqttSensorRepository sensorRepository;
    @Mock
    SupportedSensorRepository supportedSensorRepository;
    @Mock
    MqttSensorAdaptor mqttSensorAdaptor;

    @InjectMocks
    MqttSensorServiceImpl mqttSensorService;


    Organization organization;
    User user;

    SupportedSensor supportedSensor1;
    SupportedSensor supportedSensor2;

    Place place;

    MqttSensor mqttSensor1;
    MqttSensor mqttSensor2;
    List<MqttSensor> mqttSensorList;

    List<SupportedSensor> supportedSensors;
    List<MqttSensorDto> supportedSensorsDto;

    MqttSensorDto mqttSensorDto1;
    MqttSensorDto mqttSensorDto2;

    Topic topic1;

    MqttSensorRequest mqttSensorRequest;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        organization = new Organization();
        organization.setName("nhnacademy");
        organization.setOrganizationCode("1234");

        user = new User();
        user.setId("ryu");
        user.setPassword("password");
        user.setName("seungjin");
        user.setRole(Role.GUEST);
        user.setOrganization(organization);

        supportedSensor1 = new SupportedSensor();
        supportedSensor1.setId(1);
        supportedSensor1.setProtocol(Protocol.MQTT);
        supportedSensor1.setModelName("vs-121");


        supportedSensor2 = new SupportedSensor();
        supportedSensor2.setId(2);
        supportedSensor2.setProtocol(Protocol.MQTT);
        supportedSensor2.setModelName("uc-300");

        supportedSensors = new ArrayList<>();
        supportedSensors.add(supportedSensor1);
        supportedSensors.add(supportedSensor2);

        place = new Place();
        place.setId(1);
        place.setPlaceName("office");
        place.setOrganization(organization);

        mqttSensor1 = new MqttSensor();
        mqttSensor1.setId(1);
        mqttSensor1.setIp("1.1.1.1");
        mqttSensor1.setOrganization(organization);
        mqttSensor1.setPlace(place);

        mqttSensor2 = new MqttSensor();
        mqttSensor2.setId(2);
        mqttSensor2.setIp("1.1.1.1");
        mqttSensor2.setOrganization(organization);
        mqttSensor2.setPlace(place);

        mqttSensorList = new ArrayList<>();
        mqttSensorList.add(mqttSensor1);
        mqttSensorList.add(mqttSensor2);

        mqttSensorDto1 = new MqttSensorDto();
        mqttSensorDto2 = new MqttSensorDto();

        BeanUtils.copyProperties(supportedSensor1, mqttSensorDto1);
        BeanUtils.copyProperties(supportedSensor2, mqttSensorDto2);

        supportedSensorsDto = new ArrayList<>();
        supportedSensorsDto.add(mqttSensorDto1);
        supportedSensorsDto.add(mqttSensorDto2);

        topic1 = new Topic();
        topic1.setId(1);
        topic1.setTopic("#");
        topic1.setDescription("개쩌는 설명");
        topic1.setMqttSensor(mqttSensor1);

        mqttSensorRequest = new MqttSensorRequest();
        mqttSensorRequest.setTopic("해위");
        mqttSensorRequest.setDescription("개쩌는 설명");
    }

    @Test
    void getAllSupportedSensors() {
        given(supportedSensorRepository.findAll()).willReturn(supportedSensors);

        when(mqttSensorService.getAllSupportedSensors()).thenReturn(supportedSensorsDto);
        assertThat(supportedSensorsDto).isNotNull();
        assertThat(supportedSensorsDto.get(0).getModelName()).isEqualTo("vs-121");
        assertThat(supportedSensorsDto.get(1).getModelName()).isEqualTo("uc-300");
    }

    @Test
    void getOrganizationSensorsByUserId() {
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(sensorRepository.findAllByOrganization_Id(user.getOrganization().getId())).willReturn(mqttSensorList);

        when(mqttSensorService.getOrganizationSensorsByUserId(user.getId())).thenReturn(supportedSensorsDto);
        assertThat(supportedSensorsDto).isNotNull();
        assertThat(supportedSensorsDto.get(0).getModelName()).isEqualTo("vs-121");
        assertThat(supportedSensorsDto.get(1).getModelName()).isEqualTo("uc-300");

    }

    @Test
    void getSensorById() {
        given(sensorRepository.findById(any())).willReturn(Optional.of(mqttSensor1));

        MqttSensorDto sensorDto = new MqttSensorDto();
        BeanUtils.copyProperties(mqttSensor1, sensorDto);

        MqttSensorDto result = mqttSensorService.getSensorById(mqttSensor1.getId());
        assertThat(result).isNotNull();
        assertThat(result.getIp()).isEqualTo("1.1.1.1");
    }

    @Test
    void addMqttSensor() {
        given(supportedSensorRepository.existsByModelName(any())).willReturn(true);
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(placeRepository.findById(any())).willReturn(Optional.of(place));
        given(sensorRepository.save(any())).willReturn(mqttSensor1);
        given(topicRepository.save(any())).willReturn(topic1);
        given(mqttSensorAdaptor.addMqttBrokers(any())).willReturn(ResponseEntity.ok().body("good"));

        int sensorId = mqttSensorService.addMqttSensor(user.getId(), mqttSensorRequest);

        assertThat(sensorId).isEqualTo(1);
    }

    @Test
    void updateMqttSensor() {
    }

    @Test
    void deleteSensorById() {
    }
}