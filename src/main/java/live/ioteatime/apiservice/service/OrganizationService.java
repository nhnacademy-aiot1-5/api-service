package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.dto.OrganizationDto;

public interface OrganizationService {
    OrganizationDto getBudget(String userId);

    OrganizationDto updateBudget(String userId, Long budget);

    Organization updateName(String userId, String name);

    Organization updateCode(String userId, String code);
}
