package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.OrganizationDto;

public interface OrganizationService {
    OrganizationDto getBudget(String userId);

    OrganizationDto updateBudget(String userId, Long budget);

    OrganizationDto updateName(String userId, String name);

    OrganizationDto updateCode(String userId, String code);
}
