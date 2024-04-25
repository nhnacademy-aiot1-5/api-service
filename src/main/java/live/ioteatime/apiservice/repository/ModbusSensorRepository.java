package live.ioteatime.apiservice.repository;


import live.ioteatime.apiservice.domain.ModbusSensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModbusSensorRepository extends JpaRepository<ModbusSensor, Integer> {
    List<ModbusSensor> findAllByOrganization_Id(int organizationId);
}
