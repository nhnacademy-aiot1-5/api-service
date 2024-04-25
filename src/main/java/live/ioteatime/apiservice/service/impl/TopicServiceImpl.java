package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.adaptor.SensorAdaptor;
import live.ioteatime.apiservice.domain.MqttSensor;
import live.ioteatime.apiservice.domain.Topic;
import live.ioteatime.apiservice.dto.TopicDto;
import live.ioteatime.apiservice.dto.TopicRequest;
import live.ioteatime.apiservice.exception.SensorNotFoundException;
import live.ioteatime.apiservice.exception.TopicNotFoundException;
import live.ioteatime.apiservice.repository.MqttSensorRepository;
import live.ioteatime.apiservice.repository.TopicRepository;
import live.ioteatime.apiservice.service.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TopicServiceImpl implements TopicService {

    private final MqttSensorRepository mqttSensorRepository;
    private final TopicRepository topicRepository;
    private final SensorAdaptor sensorAdaptor;

    @Override
    public String addTopic(int sensorId, TopicDto topicDto) {

        MqttSensor sensor = mqttSensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);

        Topic topic = new Topic();
        BeanUtils.copyProperties(topicDto, topic);
        topic.setMqttSensor(sensor);

        Topic savedTopic = topicRepository.save(topic);

        String mqttBrokerId = "mqtt" + sensor.getId();
        Map<String, String> topicRequest = new HashMap<>();
        topicRequest.put("mqttTopic", topic.getTopic());

        sensorAdaptor.addTopics(mqttBrokerId, topicRequest);

        return savedTopic.getTopic();
    }

    @Override
    public void deleteTopic(int sensorId, int topicId) {

        Topic topic = topicRepository.findById(topicId).orElseThrow(TopicNotFoundException::new);

        String mqttBrokerId = "mqtt" + sensorId;
        Map<String, String> topicRequest = new HashMap<>();
        topicRequest.put("mqttTopic", topic.getTopic());

        mqttSensorRepository.deleteById(topicId);
        sensorAdaptor.deleteTopics(mqttBrokerId, topicRequest);

    }

    @Override
    public void updateTopic(int topicId, TopicRequest topicRequest) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(TopicNotFoundException::new);
        topic.setTopic(topicRequest.getTopic());
        topic.setDescription(topicRequest.getDescription());
        topicRepository.save(topic);
    }

    @Override
    public List<TopicDto> getTopicsBySensorId(int sensorId) {
        List<Topic> topicList = topicRepository.findBySensorId(sensorId);
        List<TopicDto> topicDtoList = new ArrayList<>();
        for(Topic topic : topicList){
            TopicDto topicDto = new TopicDto();
            BeanUtils.copyProperties(topic, topicDto);
            topicDtoList.add(topicDto);
        }
        return topicDtoList;
    }

    @Override
    public TopicDto getTopicByTopicId(int topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(TopicNotFoundException::new);
        TopicDto topicDto = new TopicDto();
        BeanUtils.copyProperties(topic, topicDto);
        return topicDto;
    }

}
