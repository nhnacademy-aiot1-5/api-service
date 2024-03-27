package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.UserCreateDto;
import live.ioteatime.apiservice.dto.UserGetDto;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.repository.UserRepository;
import live.ioteatime.apiservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    /**
     * @param userId 유저아이디
     * @return userDTO 패스워드가 빠진 유저 정보를 반환합니다.
     */
    @Override
    public UserGetDto loadUserByUserName(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserGetDto userDto = new UserGetDto();
        BeanUtils.copyProperties(user, userDto);

        return userDto;
    }

    /**
     * request에서 받아온 유저 정보를 데이터베이스에 저장합니다.
     * @param userCreateDto request에서 받아온 유저 정보입니다.
     */
    @Override
    public void createUser(UserCreateDto userCreateDto) {
        User user = new User();
        BeanUtils.copyProperties(userCreateDto, user);
        user.setRole(Role.USER);
        userRepository.save(user);
    }
}
