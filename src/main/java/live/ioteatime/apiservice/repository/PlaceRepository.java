package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
    List<Place> findAllByOrganization_Id(int organizationId);
}
