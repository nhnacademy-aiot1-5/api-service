package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.Outlier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutlierRepository extends JpaRepository<Outlier, Integer> {
    List<Outlier> findAllByFlag(int flag);
}
