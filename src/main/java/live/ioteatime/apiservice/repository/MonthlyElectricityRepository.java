package live.ioteatime.apiservice.repository;


import live.ioteatime.apiservice.domain.MonthlyElectricity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MonthlyElectricityRepository extends JpaRepository<MonthlyElectricity, Long> {
    Optional<MonthlyElectricity> findMonthlyElectricityByTime(LocalDate localDate);
}
