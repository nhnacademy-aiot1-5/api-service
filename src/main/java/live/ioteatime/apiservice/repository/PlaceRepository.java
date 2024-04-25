package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
}
