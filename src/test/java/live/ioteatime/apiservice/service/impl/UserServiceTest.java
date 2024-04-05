package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.UserDto;
import live.ioteatime.apiservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUserName() {
        User user = new User();
        user.setId("ryu");


        String id = "ryu";
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        UserDto response = userService.loadUserByUserName(id);

        assertThat(response.getId()).isEqualTo(id);
        //이 테스트를 통과시키기 위해서 userGetDto에 setter를 넣었음
        //BeanUtils는 getter와 setter 메서드를 통해 값을 복사하기 때문임
    }

    @Test
    void createUser() {
        UserDto userDto = new UserDto();
        userDto.setId("ryu");
        userDto.setPassword("password");
        userDto.setName("seungjin");

        User user = new User();
        user.setId("ryu");
        user.setPassword("password");
        user.setName("seungjin");

        given(userRepository.save(user)).willReturn(user);
        String saveUser = userService.createUser(userDto);
        assertThat("ryu").isEqualTo(saveUser);
    }
}
