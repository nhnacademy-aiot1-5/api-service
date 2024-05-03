package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.topic.TopicDto;
import live.ioteatime.apiservice.dto.topic.TopicRequest;

import java.util.List;

public interface TopicService {
    List<TopicDto> getTopicsBySensorId(int sensorId);
    TopicDto getTopicByTopicId(int topicId);
    int addTopic(int sensorId, TopicDto topicDto);
    void updateTopic(int sensorId, int topicId, TopicRequest topicRequest);
    void deleteTopic(int sensorId, int topicId);
}
