package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.UserDto;

import java.util.List;

public interface AdminService {
    List<UserDto> getGuestUsers();
    List<UserDto> getUsers();
    UserDto updateUserRole(String userId);
}
