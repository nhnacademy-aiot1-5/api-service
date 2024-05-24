package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.adaptor.MqttSensorAdaptor;
import live.ioteatime.apiservice.domain.MqttSensor;
import live.ioteatime.apiservice.domain.Topic;
import live.ioteatime.apiservice.dto.topic.TopicDto;
import live.ioteatime.apiservice.repository.MqttSensorRepository;
import live.ioteatime.apiservice.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class TopicServiceTest {

    @Mock
    TopicRepository topicRepository;

    @Mock
    MqttSensorRepository mqttSensorRepository;

    @Mock
    MqttSensorAdaptor sensorAdaptor;

    @InjectMocks
    TopicServiceImpl topicService;

    MqttSensor mqttSensor;
    Topic topic1;
    Topic topic2;
    TopicDto topicDto;
    List<Topic> topics;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mqttSensor = new MqttSensor();
        mqttSensor.setId(1);
        mqttSensor.setName("test");

        topic1 = new Topic();
        topic1.setId(1);
        topic1.setTopic("#");
        topic1.setDescription("개쩌는 설명");
        topic1.setMqttSensor(mqttSensor);

        topicDto = new TopicDto();
        BeanUtils.copyProperties(topic1, topicDto);

        topic2 = new Topic();
        topic2.setId(2);
        topic2.setTopic("#");
        topic2.setDescription("개쩌는 설명2");
        topic2.setMqttSensor(mqttSensor);

        topics = new ArrayList<>();
        topics.add(topic1);
        topics.add(topic2);
    }


    @Test
    @DisplayName("센서아이디로 토픽 리스트를 가져옴")
    void getTopicsBySensorId() {
        int id = 1;

        given(topicRepository.findAllByMqttSensor_Id(anyInt())).willReturn(topics);

        List<TopicDto> resultTopics = topicService.getTopicsBySensorId(id);

        assertThat(resultTopics.get(0).getTopic()).isEqualTo(topic1.getTopic());
        assertThat(resultTopics.get(1).getTopic()).isEqualTo(topic2.getTopic());
    }

    @Test
    @DisplayName("토픽 아이디로 토픽을 가져옴")
    void getTopicByTopicId() {
        int id = 1;

        given(topicRepository.findById(anyInt())).willReturn(Optional.of(topic1));

        TopicDto resultTopic = topicService.getTopicByTopicId(id);

        assertThat(resultTopic.getTopic()).isEqualTo(topic1.getTopic());
    }

    @Test
    void addTopic() {
        given(mqttSensorRepository.findById(anyInt())).willReturn(Optional.of(mqttSensor));
        given(topicRepository.save(any())).willReturn(topic1);
        given(sensorAdaptor.addMqttBrokers(any())).willReturn(ResponseEntity.ok().body("해치웠나"));

        assertThat(topicService.addTopic(mqttSensor.getId(), topicDto)).isEqualTo(topic1.getId());
    }

    @Test
    void updateTopic() {
        given(topicRepository.findById(anyInt())).willReturn(Optional.of(topic1));
        given(topicRepository.save(any())).willReturn(topic2);
        given(mqttSensorRepository.findById(anyInt())).willReturn(Optional.of(mqttSensor));

        topicService.updateTopic(mqttSensor.getId(), topic1.getId(), topicDto);

        then(topicRepository).should().save(topic1);
        then(mqttSensorRepository).should().findById(mqttSensor.getId());
        then(topicRepository).should().findById(topic1.getId());

    }

    @Test
    void deleteTopic() {
    }
}