package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.ControllerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ControllerStatusRepository extends JpaRepository<ControllerStatus, String> {
}
