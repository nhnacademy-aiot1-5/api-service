package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Integer> {
    List<Topic> findAllByMqttSensor_Id(int sensorId);

    int countAllByMqttSensor_Id(int sensorId);
}
