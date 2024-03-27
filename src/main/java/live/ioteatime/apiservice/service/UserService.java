package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.UserDto;

public interface UserService {
    UserDto loadUserByUserName(String userId);

    void createUser(UserDto userDto);
}
