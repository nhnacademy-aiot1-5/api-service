package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.UserDto;
import live.ioteatime.apiservice.exception.UserAlreadyExistsException;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.repository.UserRepository;
import live.ioteatime.apiservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    /**
     * @param userId 유저아이디
     * @return userDTO 패스워드가 빠진 유저 정보를 반환합니다.
     */
    @Override
    public UserDto loadUserByUserName(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto, "password");

        return userDto;
    }

    /**
     * request에서 받아온 유저 정보를 데이터베이스에 저장합니다.
     * @param userDto request에서 받아온 유저 정보입니다.
     */
    @Override
    public String createUser(UserDto userDto) {

        if(userRepository.existsById(userDto.getId())) {
            throw new UserAlreadyExistsException(userDto.getId());
        }
        User user = new User();
        BeanUtils.copyProperties(userDto, user, "createdAt");
        user.setCreatedAt(LocalDate.now());
        userRepository.save(user);
        return user.getId();
    }
}
