package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    Organization findByOrganizationCode(String organizationCode);
    boolean existsByOrganizationCode(String organizationCode);
    Organization findByName(String name);
}
