package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.TopicDto;
import live.ioteatime.apiservice.dto.TopicRequest;

import java.util.List;

public interface TopicService {
    String addTopic(int sensorId, TopicDto topicDto);

    void deleteTopic(int sensorId, int topicId);

    void updateTopic(int topicId, TopicRequest topicRequest);

    List<TopicDto> getTopicsBySensorId(int sensorId);

    TopicDto getTopicByTopicId(int topicId);
}