package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.DailyElectricity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyElectricityRepository extends JpaRepository<DailyElectricity, Long> {
    Optional<DailyElectricity> findByTime(LocalDate localDate);

    List<DailyElectricity> findAllByTimeBetween(LocalDate start, LocalDate end);
}
