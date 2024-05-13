package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.user.RegisterRequest;
import live.ioteatime.apiservice.dto.user.UserDto;
import live.ioteatime.apiservice.exception.UserAlreadyExistsException;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    User user;
    UserDto userDto;
    RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();

        user = new User();
        user.setId("ryu");
        user.setPassword(passwordEncoder.encode("password"));
        user.setName("seungjin");
        user.setRole(Role.GUEST);
        user.setOrganization(new Organization());

        userDto = new UserDto();
        userDto.setId("ryu");
        userDto.setPassword(passwordEncoder.encode("password"));
        userDto.setName("seungjin");
        userDto.setRole(Role.GUEST);

        registerRequest = new RegisterRequest();
        registerRequest.setId("ryu");
        registerRequest.setPassword(passwordEncoder.encode("password"));
        registerRequest.setName("seungjin");
        registerRequest.setOrganizationName("nhnacademy");
        registerRequest.setOrganizationCode("1234");

    }

    @Test
    @DisplayName("로그인 요청시 유저 정보 확인")
    void loadUserByUserName() {

        String id = "ryu";
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        UserDto response = userService.loadUserByUserName(id);

        assertThat(response.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("로그인 요청시 유저 정보를 확인하는데 실패했을때")
    void loadUserByUserNameFail() {
        String id = "ryu";
        given(userRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUserName(id))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("createUser 실패 - 이미 존재하는 유저일 때")
    void createUserFail() {

        given(userRepository.existsById(any())).willReturn(true);

        assertThatThrownBy(() -> userService.createUser(registerRequest))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    @DisplayName("getUserInfo 성공")
    void getUserInfo() {
        String id = "ryu";
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        UserDto response = userService.getUserInfo(id);

        assertThat(response.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("getUserInfo 실패 - user not found")
    void getUserInfoFail() {
        String id = "ryu";
        given(userRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserInfo(id))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("updateUserRole 성공")
    void updateUserRole() {

        String id = "ryu";
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        String response = userService.updateUserRole(id);
        assertThat(response).isEqualTo("ryu");


        user.setRole(Role.USER);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        User result = userRepository.findById(id).get();
        assertThat(result.getRole()).isEqualTo(Role.USER);

    }

    @Test
    @DisplayName("updateUserRole 실패 - user not found")
    void updateUserRoleFail() {
        String id = "ryu";
        given(userRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUserRole(id))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("updateUser 성공")
    void updateUser() {
        userDto.setName("changed");

        User updatedUser = new User();
        BeanUtils.copyProperties(userDto, updatedUser);
        updatedUser.setOrganization(new Organization());

        when(userRepository.findById(anyString())).thenReturn(Optional.of(updatedUser));

        given(userRepository.save(any())).willReturn(updatedUser);
        userService.updateUser(userDto);

        UserDto result = userService.getUserInfo("ryu");

        assertThat(result.getId()).isEqualTo("ryu");
        assertThat(updatedUser.getName()).isEqualTo(result.getName());
    }

    @Test
    @DisplayName("updateUser 실패 - user not found")
    void updateUserFail() {

        given(userRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(userDto))
                .isInstanceOf(UserNotFoundException.class);
    }

}
