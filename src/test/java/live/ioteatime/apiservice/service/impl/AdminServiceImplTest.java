package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.BudgetHistory;
import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.user.BudgetHistoryDto;
import live.ioteatime.apiservice.dto.user.UserDto;
import live.ioteatime.apiservice.repository.BudgetHistoryRepository;
import live.ioteatime.apiservice.repository.OrganizationRepository;
import live.ioteatime.apiservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

class AdminServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    BudgetHistoryRepository budgetHistoryRepository;
    @Mock
    OrganizationRepository organizationRepository;
    @InjectMocks
    AdminServiceImpl adminService;

    User user;
    Organization organization;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId("testId");
        user.setPassword(new BCryptPasswordEncoder().encode("1234"));
        user.setName("testName");

        organization = new Organization();
        organization.setId(1);
        organization.setName("ioteatime");
        organization.setElectricityBudget(50_000L);
        user.setOrganization(organization);
    }

    @Test
    void getGuestUsers() {
        user.setRole(Role.GUEST);
        given(userRepository.findById(anyString())).willReturn(Optional.ofNullable(user));
        given(userRepository.findAllByRoleAndOrganization_Id(any(), anyInt())).willReturn(List.of(user));

        List<UserDto> result = adminService.getGuestUsers("admin");

        assertEquals("testId", result.get(0).getId());
        assertEquals("testName", result.get(0).getName());
        assertEquals(Role.GUEST, result.get(0).getRole());
    }

    @Test
    void getUsers() {
        given(userRepository.findById(anyString())).willReturn(Optional.ofNullable(user));
        given(userRepository.findAllByRoleAndOrganization_Id(any(), anyInt())).willReturn(List.of(user));

        List<UserDto> result = adminService.getGuestUsers("admin");

        assertEquals("testId", result.get(0).getId());
        assertEquals("testName", result.get(0).getName());
    }

    @Test
    void getBudgetHistory() {
        given(userRepository.findById(anyString())).willReturn(Optional.ofNullable(user));
        BudgetHistory budgetHistory = new BudgetHistory();
        budgetHistory.setBudget(50_000L);
        given(budgetHistoryRepository.findAllByOrganization_IdOrderByChangeTimeDesc(anyInt()))
                .willReturn(List.of(budgetHistory));

        List<BudgetHistoryDto> result = adminService.getBudgetHistory(anyString());

        assertEquals(50_000L, result.get(0).getBudget());
    }

    @Test
    void getOrganization() {
        given(userRepository.findById(anyString())).willReturn(Optional.ofNullable(user));

        OrganizationDto result = adminService.getOrganization("admin");

        assertEquals("ioteatime", result.getName());
    }

    @Test
    void isOrganizationCodeDuplicate() {
        given(organizationRepository.existsByOrganizationCode(anyString())).willReturn(false);

        Boolean result = adminService.isOrganizationCodeDuplicate("12345asdfg");

        assertEquals(false, result);
    }

    @Test
    void updateUserRole() {
        user.setRole(Role.GUEST);
        given(userRepository.findById(anyString())).willReturn(Optional.ofNullable(user));
        given(userRepository.save(any())).willReturn(user);

        UserDto result = adminService.updateUserRole("testUser");

        assertEquals(Role.USER, result.getRole());
    }
}