package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.RegisterRequest;
import live.ioteatime.apiservice.dto.UpdateUserPasswordRequest;
import live.ioteatime.apiservice.dto.UserDto;

public interface UserService {
    UserDto loadUserByUserName(String userId);

    String createUser(RegisterRequest registerRequest);

    UserDto getUserInfo(String userId);

    String updateUserRole(String userId);

    String updateUser(UserDto userDto);

    OrganizationDto getOrganizationByUserId(String userId);

    String updateUserPassword(String userId, UpdateUserPasswordRequest updatePasswordRequest);
}
