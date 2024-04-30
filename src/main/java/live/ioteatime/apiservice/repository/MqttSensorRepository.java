package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.MqttSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MqttSensorRepository extends JpaRepository<MqttSensor, Integer> {
    List<MqttSensor> findAllByOrganization_Id(int organizationId);

    @Query("select s from MqttSensor s join fetch s.place where s.organization.id=?1")
    List<MqttSensor> findAllByOrganizationIdWithPlace(int organizationId);

}
