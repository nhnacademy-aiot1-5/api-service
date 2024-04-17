package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.domain.Sensor;
import live.ioteatime.apiservice.dto.UserDto;

import java.util.List;

public interface AdminService {
    List<UserDto> getGuestUsers();

    List<UserDto> getUsers();

    List<Sensor> getSensors();

    UserDto updateUserRole(String userId);
}
