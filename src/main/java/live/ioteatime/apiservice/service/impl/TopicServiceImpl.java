package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.adaptor.SensorAdaptor;
import live.ioteatime.apiservice.domain.MqttSensor;
import live.ioteatime.apiservice.domain.Topic;
import live.ioteatime.apiservice.dto.AddBrokerRequest;
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
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TopicServiceImpl implements TopicService {

    private final MqttSensorRepository mqttSensorRepository;
    private final TopicRepository topicRepository;
    private final SensorAdaptor sensorAdaptor;

    /**
     * 센서의 토픽 리스트를 모두 반환합니다.
     * @param sensorId 센서아이디
     * @return 토픽리스트
     */
    @Override
    public List<TopicDto> getTopicsBySensorId(int sensorId) {
        List<Topic> topicList = topicRepository.findByMqttSensor_Id(sensorId);
        List<TopicDto> topicDtoList = new ArrayList<>();
        for(Topic topic : topicList){
            TopicDto topicDto = new TopicDto();
            BeanUtils.copyProperties(topic, topicDto);
            topicDtoList.add(topicDto);
        }
        return topicDtoList;
    }

    /**
     * 토픽 아이디로 토픽을 단일 조회합니다.
     * @param topicId 토픽아이디
     * @return 토픽 정보
     */
    @Override
    public TopicDto getTopicByTopicId(int topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(TopicNotFoundException::new);
        TopicDto topicDto = new TopicDto();
        BeanUtils.copyProperties(topic,topicDto);
        return topicDto;
    }

    /**
     * 조직의 ADMIN 유저가 센서에 토픽을 추가합니다.
     * @param sensorId 센서아이디
     * @param topicDto 추가할 토픽 정보
     * @return 추가한 토픽
     */
    @Override
    public int addTopic(int sensorId, TopicDto topicDto) {

        MqttSensor sensor = mqttSensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);

        Topic topic = new Topic();
        topic.setTopic(topicDto.getTopic());
        topic.setDescription(topicDto.getDescription());
        topic.setMqttSensor(sensor);

        Topic savedTopic = topicRepository.save(topic);

        requestAddBrokerToRuleEngine(sensor);

        return savedTopic.getId();
    }

    /**
     * 토픽 정보를 수정합니다.
     * @param topicId 토픽아이디
     * @param topicRequest 수정 요청
     */
    @Override
    public void updateTopic(int sensorId, int topicId, TopicRequest topicRequest) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(TopicNotFoundException::new);
        topic.setTopic(topicRequest.getTopic());
        topic.setDescription(topicRequest.getDescription());
        topicRepository.save(topic);

        MqttSensor sensor = mqttSensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);
        requestAddBrokerToRuleEngine(sensor);

    }

    /**
     * 토픽을 삭제합니다.
     * @param sensorId 센서아이디
     * @param topicId 토픽아이디
     */
    @Override
    public void deleteTopic(int sensorId, int topicId) {

        if(topicRepository.countAllByMqttSensor_Id(sensorId) <= 1) {
            throw new IllegalStateException("토픽 삭제 실패. 토픽은 최소 1개 이상 등록하여야 합니다.");
        }

        topicRepository.deleteById(topicId);

        MqttSensor sensor = mqttSensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);
        requestAddBrokerToRuleEngine(sensor);
    }

    /**
     * 토픽을 추가, 수정, 삭제할 경우
     * 룰엔진에 토픽 추가, 수정, 삭제 요청을 만들어 보냅니다.
     * @param sensor 센서 엔티티
     */
    private void requestAddBrokerToRuleEngine(MqttSensor sensor) {

        String mqttHost = "tcp://" + sensor.getIp() + ":" + sensor.getPort();
        String mqttId = "mqtt" +  sensor.getId();
        List<Topic> topicList = topicRepository.findByMqttSensor_Id(sensor.getId());
        List<String> topicValueList = new ArrayList<>();
        for(Topic topicEntity : topicList){
            topicValueList.add(topicEntity.getTopic());
        }

        AddBrokerRequest addBrokerRequest = new AddBrokerRequest();
        addBrokerRequest.setMqttHost(mqttHost);
        addBrokerRequest.setMqttId(mqttId);
        addBrokerRequest.setMqttTopic(topicValueList);

        sensorAdaptor.addBrokers(addBrokerRequest);
    }

}
