package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Integer> {
}
