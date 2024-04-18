package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.*;
import live.ioteatime.apiservice.dto.UserDto;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.repository.AdminRepository;
import live.ioteatime.apiservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public List<UserDto> getGuestUsers() {
        List<User> users = adminRepository.findAllByRole(Role.GUEST);
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto, "password");
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = adminRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto, "password", "");
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public UserDto updateUserRole(String userId) {
        User user = adminRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        user.setRole(Role.USER);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto, "password");
        adminRepository.save(user);
        return userDto;
    }

}
