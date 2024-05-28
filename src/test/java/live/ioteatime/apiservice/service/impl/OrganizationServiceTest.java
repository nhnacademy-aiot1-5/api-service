package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.BudgetHistory;
import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.repository.BudgetHistoryRepository;
import live.ioteatime.apiservice.repository.OrganizationRepository;
import live.ioteatime.apiservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

class OrganizationServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    OrganizationRepository organizationRepository;

    @Mock
    BudgetHistoryRepository budgetHistoryRepository;

    @Spy
    @InjectMocks
    OrganizationServiceImpl organizationService;

    User user;
    Organization organization;
    BudgetHistory budgetHistory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        organization = new Organization();
        organization.setId(1);
        organization.setName("nhnacademy");
        organization.setOrganizationCode("1234");

        user = new User();
        user.setId("asdf");
        user.setPassword("password");
        user.setRole(Role.USER);
        user.setOrganization(organization);

        budgetHistory = new BudgetHistory();
        budgetHistory.setId(1);
        budgetHistory.setOrganization(organization);
        budgetHistory.setBudget(1000L);

    }

    @Test
    void getBudget() {
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        doReturn(organization).when(organizationService).getOrganizationByUserId(anyString());
        OrganizationDto organizationDto = organizationService.getBudget(user.getId());

        assertThat(organizationDto).isNotNull();
    }

    @Test
    void updateBudget() {
        doReturn(organization).when(organizationService).getOrganizationByUserId(anyString());
        given(organizationRepository.save(organization)).willReturn(organization);
        given(budgetHistoryRepository.save(budgetHistory)).willReturn(budgetHistory);

        organizationService.updateBudget(user.getId(), 1000L);

        OrganizationDto organizationDto = new OrganizationDto();
        BeanUtils.copyProperties(organization, organizationDto);

        assertThat(organizationDto).isNotNull();
    }

    @Test
    void updateName() {
        doReturn(organization).when(organizationService).getOrganizationByUserId(anyString());
        given(organizationRepository.save(organization)).willReturn(organization);

        organizationService.updateName(user.getId(), organization.getName());

        OrganizationDto organizationDto = new OrganizationDto();
        BeanUtils.copyProperties(organization, organizationDto);

        assertThat(organizationDto).isNotNull();
    }

    @Test
    void updateCode() {
        doReturn(organization).when(organizationService).getOrganizationByUserId(anyString());
        given(organizationRepository.save(organization)).willReturn(organization);

        organizationService.updateCode(user.getId(), organization.getOrganizationCode());

        OrganizationDto organizationDto = new OrganizationDto();
        BeanUtils.copyProperties(organization, organizationDto);

        assertThat(organizationDto).isNotNull();
    }
}