package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Protocol;
import live.ioteatime.apiservice.domain.SupportedSensor;
import live.ioteatime.apiservice.dto.sensor.MqttSensorDto;
import live.ioteatime.apiservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class MqttSensorServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    TopicRepository topicRepository;
    @Mock
    PlaceRepository placeRepository;
    @Mock
    MqttSensorRepository mqttSensorRepository;
    @Mock
    SupportedSensorRepository supportedSensorRepository;

    @InjectMocks
    MqttSensorServiceImpl mqttSensorService;


    SupportedSensor supportedSensor1;
    SupportedSensor supportedSensor2;

    List<SupportedSensor> supportedSensors;


    @BeforeEach
    void setUp() {
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
    }

    @Test
    void getAllSupportedSensors() {
    }

    @Test
    void getOrganizationSensorsByUserId() {
    }

    @Test
    void getSensorById() {
    }

    @Test
    void addMqttSensor() {
    }

    @Test
    void updateMqttSensor() {
    }

    @Test
    void deleteSensorById() {
    }

    @Test
    void getSensorAdaptor() {
    }

    @Test
    void getUserRepository() {
    }

    @Test
    void getTopicRepository() {
    }

    @Test
    void getPlaceRepository() {
    }

    @Test
    void getSensorRepository() {
    }

    @Test
    void getSupportedSensorRepository() {
    }
}