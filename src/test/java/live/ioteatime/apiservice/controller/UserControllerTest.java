package live.ioteatime.apiservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.user.UserDto;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.service.impl.OrganizationServiceImpl;
import live.ioteatime.apiservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserServiceImpl userService;
    @MockBean
    OrganizationServiceImpl organizationService;

    User testUser;
    UserDto testUserDto;
    Organization testOrganization;
    OrganizationDto testOrganizationDto;

    @BeforeEach
    void setUp() {

        testOrganization = new Organization();
        testOrganization.setId(1);
        testOrganization.setName("nhnacademy");
        testOrganization.setElectricityBudget(100000L);
        testOrganization.setOrganizationCode("1234");

        testOrganizationDto = new OrganizationDto();
        BeanUtils.copyProperties(testOrganization, testOrganizationDto);

        testUser = new User();
        testUser.setId("testId");
        testUser.setName("testName");
        testUser.setPassword("12345");
        testUser.setRole(Role.GUEST);
        testUser.setOrganization(testOrganization);

        testUserDto = new UserDto();
        BeanUtils.copyProperties(testUser, testUserDto);
        testUserDto.setOrganization(testOrganizationDto);

    }

    @Test
    @DisplayName("getUserInfo 성공")
    void getUserInfo() throws Exception {
        given(userService.getUserInfo(anyString())).willReturn(testUserDto);

        ResultActions result = mockMvc.perform(get("/users")
                .header("X-USER-ID", "testId"));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.pw").value(testUser.getPassword()))
                .andExpect(jsonPath("$.name").value(testUser.getName()))
                .andExpect(jsonPath("$.role").value(testUser.getRole().toString()))
                .andExpect(jsonPath("$.organization.id").value(testOrganizationDto.getId()));


    }

    @Test
    @DisplayName("getUserInfo 실패 - user not found")
    void getUserInfoFail() throws Exception {

        given(userService.getUserInfo(anyString())).willThrow(new UserNotFoundException(testUser.getId()));

        ResultActions result = mockMvc.perform(get("/users")
                .header("X-USER-ID", "testId"));

        result.andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("User not found: testId"));
    }

    @Test
    @DisplayName("loadUserByUserName 성공")
    void loadUserByUserName() throws Exception {
        given(userService.loadUserByUserName(anyString())).willReturn(testUserDto);

        ResultActions result = mockMvc.perform(get("/users/{userId}/details", "testId")
                .header("X-USER-ID", "testId"));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testUserDto.getId()))
                .andExpect(jsonPath("$.pw").value(testUserDto.getPassword()));
    }

    @Test
    @DisplayName("loadUserByUserName 실패 - user not found")
    void loadUserByUserNameFail() throws Exception {
        given(userService.loadUserByUserName(anyString())).willThrow(new UserNotFoundException(testUser.getId()));

        ResultActions result = mockMvc.perform(get("/users/{userId}/details", "testId")
                .header("X-USER-ID", "testId"));

        result.andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("User not found: testId"));
    }

    @Test
    @DisplayName("createUser 성공")
    void createUser() throws Exception {
        given(userService.createUser(any())).willReturn(testUser.getId());

        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"testId\",\"password\":\"12345\",\"name\":\"testName\"," +
                        "\"organizationName\":\"nhnacademy\",\"organizationCode\":\"1234\"}"));

        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "https://www.ioteatime.live/mypage"))
                .andExpect(content().string("Successfully registered: userId=testId"))
                .andReturn();
    }

    @Test
    @DisplayName("updateUser 성공")
    void updateUserRole() throws Exception {
        testUserDto.setRole(Role.USER);
        given(userService.updateUser(any())).willReturn(testUser.getId());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(testUserDto);
        ResultActions result = mockMvc.perform(put("/users/update-user")
                .header("X-USER-ID", "testId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpect(status().isOk())
                .andExpect(content().string(testUserDto.getId()));
    }


    @Test
    @DisplayName("updateUserPassword 성공")
    void updateUserPassword() throws Exception {
        testUserDto.setPassword("123456789");

        given(userService.updateUserPassword(anyString(), any())).willReturn(testUser.getId());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(testUserDto);
        ResultActions result = mockMvc.perform(put("/users/password")
                .header("X-USER-ID", "testId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpect(status().isOk())
                .andExpect(content().string(testUserDto.getId()));
    }

}