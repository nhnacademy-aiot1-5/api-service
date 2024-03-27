package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.UserCreateDto;
import live.ioteatime.apiservice.dto.UserGetDto;

public interface UserService {
    UserGetDto loadUserByUserName(String userId);

    void createUser(UserCreateDto userCreateDto);
}
