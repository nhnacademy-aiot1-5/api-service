package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.MqttSensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MqttSensorRepository extends JpaRepository<MqttSensor, Integer> {
    List<MqttSensor> findAllByOrganization_Id(int organizationId);

}
