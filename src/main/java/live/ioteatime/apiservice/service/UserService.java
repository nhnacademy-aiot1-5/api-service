package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.user.RegisterRequest;
import live.ioteatime.apiservice.dto.user.UpdateUserPasswordRequest;
import live.ioteatime.apiservice.dto.user.UserDto;

public interface UserService {
    UserDto loadUserByUserName(String userId);

    String createUser(RegisterRequest registerRequest);

    UserDto getUserInfo(String userId);

    String updateUser(UserDto userDto);

    OrganizationDto getOrganizationByUserId(String userId);

    String updateUserPassword(String userId, UpdateUserPasswordRequest updatePasswordRequest);
}
