package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.repository.AdminRepository;
import live.ioteatime.apiservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public List<User> getGuestUser() {
        return adminRepository.findAllByRole(Role.GUEST);
    }

    @Override
    public String updateUserRole(String userId) {
        return "";
    }
}
