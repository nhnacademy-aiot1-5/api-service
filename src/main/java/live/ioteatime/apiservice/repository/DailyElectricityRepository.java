package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.DailyElectricity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DailyElectricityRepository extends JpaRepository<DailyElectricity, DailyElectricity.Pk> {
    Optional<DailyElectricity> findByPk(DailyElectricity.Pk pk);

    List<DailyElectricity> findAllByPkChannelIdAndPkTimeBetween(int channelId, LocalDateTime start, LocalDateTime end);
}

