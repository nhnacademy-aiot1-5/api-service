package live.ioteatime.apiservice.repository;


import live.ioteatime.apiservice.domain.MonthlyElectricity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonthlyElectricityRepository extends JpaRepository<MonthlyElectricity, MonthlyElectricity.Pk> {
    Optional<MonthlyElectricity> findMonthlyElectricityByPk(MonthlyElectricity.Pk pk);
}
