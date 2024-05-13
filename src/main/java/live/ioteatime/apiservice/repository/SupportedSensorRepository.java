package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.Protocol;
import live.ioteatime.apiservice.domain.SupportedSensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportedSensorRepository extends JpaRepository<SupportedSensor, Integer> {
    boolean existsByModelName(String modelName);
    List<SupportedSensor> findAllByProtocol(Protocol protocol);
}
