package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.UserDto;
import live.ioteatime.apiservice.service.impl.AdminServiceImpl;
import live.ioteatime.apiservice.service.impl.OrganizationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AdminServiceImpl adminService;
    @MockBean
    OrganizationServiceImpl organizationService;

    User user1;
    User user2;

    UserDto userDto1;
    UserDto userDto2;

    List<UserDto> userDtoList;

    @BeforeEach
    void setUp() throws Exception {
        user1 = new User();
        user1.setId("testId1");
        user1.setName("testName1");
        user1.setPassword("password1");
        user1.setRole(Role.GUEST);

        user2 = new User();
        user2.setId("testId2");
        user2.setName("testName2");
        user2.setPassword("password2");
        user2.setRole(Role.GUEST);

        userDto1 = new UserDto();
        userDto2 = new UserDto();

        BeanUtils.copyProperties(user1, userDto1);
        BeanUtils.copyProperties(user2, userDto2);

        userDtoList = new ArrayList<>();
        userDtoList.add(userDto1);
        userDtoList.add(userDto2);
    }

    @Test
    void getGuestUsers() throws Exception {
        Mockito.when(adminService.getGuestUsers(any())).thenReturn(userDtoList);

        mockMvc.perform(get("/admin/guests").header("X-USER-ID", "testId"))
                .andExpect(status().isOk());
    }

    @Test
    void getUsers() throws Exception {
    }

    @Test
    void getBudgetHistory() throws Exception {
    }

    @Test
    void getOrganization() throws Exception {
    }

    @Test
    void checkCode() throws Exception {
    }

    @Test
    void updateUserRole() throws Exception {
    }

    @Test
    void updateBudget() throws Exception {
    }

    @Test
    void updateOrganizationName() throws Exception {
    }

    @Test
    void updateOrganizationCode() throws Exception {
    }
}