package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.BudgetHistory;
import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.exception.OrganizationCodeAlreadyExistsException;
import live.ioteatime.apiservice.exception.OrganizationNotFoundException;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.repository.BudgetHistoryRepository;
import live.ioteatime.apiservice.repository.OrganizationRepository;
import live.ioteatime.apiservice.repository.UserRepository;
import live.ioteatime.apiservice.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final BudgetHistoryRepository organizationBudgetHistoryRepository;

    Organization getOrganizationByUserId(String userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Organization organization = user.getOrganization();

        if(Objects.isNull(organization)){
            throw new OrganizationNotFoundException();
        }

        return organization;
    }

    @Override
    public OrganizationDto getBudget(String userId) {
        Organization organization = getOrganizationByUserId(userId);

        OrganizationDto organizationDto = new OrganizationDto();
        BeanUtils.copyProperties(organization, organizationDto, "id", "name");
        return organizationDto;
    }

    @Override
    public OrganizationDto updateBudget(String userId, Long budget) {

        Organization organization = getOrganizationByUserId(userId);

        BudgetHistory organizationBudgetHistory = new BudgetHistory();
        organizationBudgetHistory.setOrganization(organization);
        organizationBudgetHistory.setBudget(budget);
        organizationBudgetHistory.setChangeTime(LocalDateTime.now());
        organizationBudgetHistoryRepository.save(organizationBudgetHistory);

        organization.setElectricityBudget(budget);
        organizationRepository.save(organization);

        OrganizationDto organizationDto = new OrganizationDto();
        BeanUtils.copyProperties(organization, organizationDto, "id", "name", "organizationCode");
        return organizationDto;
    }

    @Override
    public OrganizationDto updateName(String userId, String name) {
        Organization organization = getOrganizationByUserId(userId);
        organization.setName(name);
        organizationRepository.save(organization);

        OrganizationDto organizationDto = new OrganizationDto();
        BeanUtils.copyProperties(organization, organizationDto);
        return organizationDto;
    }

    @Override
    public OrganizationDto updateCode(String userId, String code) {

        Organization organization = getOrganizationByUserId(userId);

        if(organizationRepository.existsByOrganizationCode(code)){
            throw new OrganizationCodeAlreadyExistsException(code);
        }
        organization.setOrganizationCode(code);
        organizationRepository.save(organization);

        OrganizationDto organizationDto = new OrganizationDto();
        BeanUtils.copyProperties(organization, organizationDto);
        return organizationDto;
    }
}
