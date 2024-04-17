package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.OrganizationDto;

public interface OrganizationService {
    OrganizationDto updateBudget(String userId, Long budget);

    OrganizationDto getOrganization(String userId);
}
