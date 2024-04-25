package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.TopicDto;

public interface TopicService {
    String addTopic(int sensorId, TopicDto topicDto);

    void deleteTopic(int sensorId, int topicId);
}
