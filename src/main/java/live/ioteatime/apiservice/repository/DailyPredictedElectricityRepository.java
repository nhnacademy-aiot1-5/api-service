package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.DailyPredictedElectricity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyPredictedElectricityRepository extends JpaRepository<DailyPredictedElectricity, LocalDateTime> {

    List<DailyPredictedElectricity> findAllByTimeBetweenAndOrganizationIdAndChannelIdOrderByTimeAsc(
            LocalDateTime start, LocalDateTime end, int organizationId, int channelId);
}
