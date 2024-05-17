package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.user.BudgetHistoryDto;
import live.ioteatime.apiservice.dto.user.UserDto;
import live.ioteatime.apiservice.service.impl.AdminServiceImpl;
import live.ioteatime.apiservice.service.impl.OrganizationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AdminServiceImpl adminService;
    @MockBean
    OrganizationServiceImpl organizationService;

    User guest;
    User user;

    UserDto guestUserDto;
    UserDto userDto;
    OrganizationDto organizationDto;

    @BeforeEach
    void setUp() {
        guest = new User();
        guest.setId("guest");
        guest.setName("guest");
        guest.setPassword("1234");
        guest.setRole(Role.GUEST);

        user = new User();
        user.setId("user");
        user.setName("user");
        user.setPassword("5678");
        user.setRole(Role.USER);

        guestUserDto = new UserDto();
        userDto = new UserDto();

        BeanUtils.copyProperties(guest, guestUserDto);
        BeanUtils.copyProperties(user, userDto);

        organizationDto = new OrganizationDto();
        organizationDto.setName("ioteatime");
        organizationDto.setOrganizationCode("asdf1234");
        organizationDto.setElectricityBudget(100L);
    }

    @Test
    void getGuestUsers() throws Exception {
        given(adminService.getGuestUsers(any())).willReturn(List.of(guestUserDto));

        ResultActions result = mockMvc.perform(get("/admin/guests")
                .header("X-USER-ID", "admin"));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("guest"));
    }

    @Test
    void getUsers() throws Exception {
        given(adminService.getUsers(anyString())).willReturn(List.of(guestUserDto, userDto));

        ResultActions result = mockMvc.perform(get("/admin/users")
                .header("X-USER-ID", "admin"));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getBudgetHistory() throws Exception {
        BudgetHistoryDto budgetHistoryDto = new BudgetHistoryDto();
        budgetHistoryDto.setId(1);
        budgetHistoryDto.setBudget(50_000L);
        budgetHistoryDto.setChangeTime(LocalDateTime.of(2024,5,27,0,0,0,0));
        given(adminService.getBudgetHistory(anyString())).willReturn(List.of(budgetHistoryDto));

        ResultActions result = mockMvc.perform(get("/admin/budget-histories")
                .header("X-USER-ID", "admin"));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].budget").value(50_000L));
    }

    @Test
    void getOrganization() throws Exception {
        given(adminService.getOrganization(anyString())).willReturn(organizationDto);

        ResultActions result = mockMvc.perform(get("/admin/organization")
                .header("X-USER-ID", "admin"));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("ioteatime"))
                .andExpect(jsonPath("$.organization_code").value("asdf1234"));
    }

    @Test
    void checkCode() throws Exception {
        given(adminService.isOrganizationCodeDuplicate(anyString())).willReturn(true);

        ResultActions result = mockMvc.perform(get("/admin/check-code")
                .header("X-USER-ID", "admin")
                .param("code", "asdf1234"));

        result.andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void updateUserRole() throws Exception {
        given(adminService.updateUserRole(anyString())).willReturn(guestUserDto);
        guestUserDto.setRole(Role.USER);

        ResultActions result = mockMvc.perform(put("/admin/role")
                .header("X-USER-ID", "admin")
                .param("userId", "guest"));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("guest"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void updateBudget() throws Exception {
        organizationDto.setElectricityBudget(12345L);
        given(organizationService.updateBudget(anyString(), anyLong())).willReturn(organizationDto);

        ResultActions result = mockMvc.perform(put("/admin/budget")
                .header("X-USER-ID", "admin")
                .param("budget", String.valueOf(12345L)));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.electricity_budget").value(12345L));
    }

    @Test
    void updateOrganizationName() throws Exception {
        String newName = "changed-name";
        organizationDto.setName(newName);
        given(organizationService.updateName(anyString(), anyString())).willReturn(organizationDto);

        ResultActions result = mockMvc.perform(put("/admin/organization-name")
                .header("X-USER-ID", "admin")
                .param("name", newName));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(newName));
    }

    @Test
    void updateOrganizationCode() throws Exception {
        String newCode = "5678qwer";
        organizationDto.setOrganizationCode(newCode);
        given(organizationService.updateCode(anyString(), anyString())).willReturn(organizationDto);

        ResultActions result = mockMvc.perform(put("/admin/organization-code")
                .header("X-USER-ID", "admin")
                .param("code", newCode));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.organization_code").value(newCode));
    }
}