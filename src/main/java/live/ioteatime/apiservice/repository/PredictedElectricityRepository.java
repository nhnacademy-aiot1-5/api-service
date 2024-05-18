package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.PredictedDailyElectricity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PredictedElectricityRepository extends JpaRepository<PredictedDailyElectricity, LocalDateTime> {

    List<PredictedDailyElectricity> findAllByTimeBetweenAndOrganizationIdAndChannelIdOrderByTimeAsc(
            LocalDateTime start, LocalDateTime end, int organizationId, int channelId);
}
