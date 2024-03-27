package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.UserDto;
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


    @Override
    public UserDto loadUserByUserName(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);

        return userDto;
    }

    @Override
    public void createUser(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        userRepository.save(user);
    }
}
