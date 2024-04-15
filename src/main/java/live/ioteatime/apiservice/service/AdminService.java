package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.domain.User;

import java.util.List;

public interface AdminService {
    List<User> getGuestUser();

    String updateUserRole(String userId);
}
