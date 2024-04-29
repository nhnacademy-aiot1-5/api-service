package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.TopicDto;
import live.ioteatime.apiservice.dto.TopicRequest;

import java.util.List;

public interface TopicService {
    List<TopicDto> getTopicsBySensorId(int sensorId);
    TopicDto getTopicByTopicId(int topicId);
    int addTopic(int sensorId, TopicDto topicDto);
    void updateTopic(int topicId, TopicRequest topicRequest);
    void deleteTopic(int sensorId, int topicId);
}
