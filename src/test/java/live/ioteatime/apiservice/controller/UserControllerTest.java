package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.UserDto;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.properties.UserProperties;
import live.ioteatime.apiservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    UserProperties userProperties;

    User testUser;
    UserDto testUserDto;

    @BeforeEach
    void setUp() {

        this.testUser = new User();
        testUser.setId("testId");
        testUser.setName("testName");
        testUser.setPassword("12345");
        testUser.setRole(Role.GUEST);

        testUserDto = new UserDto();
        BeanUtils.copyProperties(testUser, testUserDto);
    }

    @Test
    @DisplayName("loadUserByUserName 성공")
    void loadUserByUserName() throws Exception {
        Mockito.when(userService.loadUserByUserName(anyString())).thenReturn(testUserDto);

        mockMvc.perform(get("/users/{userId}/details", "testId")
                        .header("X-USER-ID", "testId"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testUserDto.getId()))
                .andExpect(jsonPath("$.pw").value(testUserDto.getPassword()));
    }

    @Test
    @DisplayName("loadUserByUserName 실패 - user not found")
    void loadUserByUserNameFail() throws Exception {

        Mockito.when(userService.loadUserByUserName(anyString())).thenThrow(new UserNotFoundException(testUser.getId()));

        mockMvc.perform(get("/users/{userId}/details", "testId")
                        .header("X-USER-ID", "testId"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("User not found: testId"));
    }

    @Test
    @DisplayName("getUserInfo 성공")
    void getUserInfo() throws Exception {
        Mockito.when(userService.getUserInfo(anyString())).thenReturn(testUserDto);

        mockMvc.perform(get("/users/{userId}", "testId")
                        .header("X-USER-ID", "testId"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testUserDto.getId()))
                .andExpect(jsonPath("$.pw").value(testUserDto.getPassword()))
                .andExpect(jsonPath("$.name").value(testUserDto.getName()))
                .andExpect(jsonPath("$.role").value(testUserDto.getRole().toString()));
    }

    @Test
    @DisplayName("getUserInfo 실패 - user not found")
    void getUserInfoFail() throws Exception {

        Mockito.when(userService.getUserInfo(anyString())).thenThrow(new UserNotFoundException(testUser.getId()));

        mockMvc.perform(get("/users/{userId}", "testId")
                        .header("X-USER-ID", "testId"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("User not found: testId"));
    }

    @Test
    @DisplayName("createUser 성공")
    void createUser() throws Exception {
        Mockito.when(userService.createUser(any())).thenReturn(testUser.getId());
        Mockito.when(userProperties.getUserDetailUri()).thenReturn("http://localhost:8080/users");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"testId\",\"password\":\"12345\",\"name\":\"testName\",\"role\":\"USER\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost:8080/users/testId"))
                .andExpect(content().string(""))
                .andReturn();
    }

    @Test
    @DisplayName("updateUserRole 성공")
    void updateUserRole() throws Exception{
        testUserDto.setRole(Role.USER);
        Mockito.when(userService.updateUserRole(any())).thenReturn(testUserDto.getId());
        Mockito.when(userService.getUserInfo(anyString())).thenReturn(testUserDto);

        mockMvc.perform(put("/users/{userId}/roles", "testId")
                        .header("X-USER-ID", "testId"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string(testUserDto.getId()));

        mockMvc.perform(get("/users/{userId}", "testId")
                        .header("X-USER-ID", "testId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value(testUserDto.getRole().toString()));
    }

}