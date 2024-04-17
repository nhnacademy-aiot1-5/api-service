package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.repository.OrganizationRepository;
import live.ioteatime.apiservice.repository.UserRepository;
import live.ioteatime.apiservice.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    @Override
    public OrganizationDto updateBudget(String userId, Long budget) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Organization organization = user.getOrganization();

        organization.setElectricityBudget(budget);
        organizationRepository.save(organization);

        OrganizationDto organizationDto = new OrganizationDto();
        BeanUtils.copyProperties(organization,organizationDto, "id", "name");
        return organizationDto;
    }

    @Override
    public OrganizationDto getOrganization(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Organization organization = user.getOrganization();
        OrganizationDto organizationDto = new OrganizationDto();
        BeanUtils.copyProperties(organization, organizationDto);
        return organizationDto;
    }


}
