package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.SupportedSensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportedSensorRepository extends JpaRepository<SupportedSensor, Integer> {
    boolean existsByModelName(String modelName);
}
