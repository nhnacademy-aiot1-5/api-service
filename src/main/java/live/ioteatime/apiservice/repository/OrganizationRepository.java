package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Organization getByName(String name);
}
