package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Organization findByName(String name);

    Optional<Organization> findById(int id);
}
