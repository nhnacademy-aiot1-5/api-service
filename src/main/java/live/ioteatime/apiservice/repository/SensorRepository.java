package live.ioteatime.apiservice.repository;


import live.ioteatime.apiservice.domain.Sensor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Integer> {
}
