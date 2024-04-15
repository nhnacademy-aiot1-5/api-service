package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.UserDto;
import live.ioteatime.apiservice.exception.OrganizationNotFoundException;
import live.ioteatime.apiservice.exception.UserAlreadyExistsException;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.repository.UserRepository;
import live.ioteatime.apiservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * @param userId 유저아이디
     * @return userDTO 유저 id, pw를 반환합니다.
     */
    @Override
    public UserDto loadUserByUserName(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto, "name", "createdAt", "role", "organization");

        return userDto;
    }

    /**
     * CreatedAt은 LocalDate를 통해 현재 날짜를 넣음<p>
     * Role은 가입할때는 기본적으로 User로 설정<p>
     * Password는 인코딩 작업 후에 추가되어야 하므로 BeanUtils로 추가하지 않았음
     *
     * @param userDto 받아온 유저 정보를 데이터베이스에 저장합니다.
     * @return userDto request 에서 받아온 유저 정보입니다.
     */
    @Override
    public String createUser(UserDto userDto) {

        if (userRepository.existsById(userDto.getId())) {
            throw new UserAlreadyExistsException(userDto.getId());
        }
        User user = new User();
        BeanUtils.copyProperties(userDto, user, "createdAt", "role", "password");
        String encodingPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encodingPassword);
        user.setCreatedAt(LocalDate.now());
        user.setRole(Role.GUEST);
        userRepository.save(user);
        return user.getId();
    }

    /**
     * @param userId 유저아이디
     * @return userDTO pw 제외한 유저 정보를 반환합니다.
     */
    @Override
    public UserDto getUserInfo(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto, "password");
        return userDto;
    }

/**
     * @param userId 유저아이디
     * @return userId 유저아이디
     */
    @Override
    public String updateUserRole(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        user.setRole(Role.USER);
        userRepository.save(user);
        return user.getId();
    }

    /**
     * @param userDto 유저 정보
     * @return userId 유저아이디
     */
    @Override
    public String updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new UserNotFoundException(userDto.getId()));
        BeanUtils.copyProperties(userDto, user);
        userRepository.save(user);
        return user.getId();
    }

    /**
     * 유저가 소속한 조직 정보를 리턴합니다.
     * @param userId 유저아이디
     * @return 유저의 조직 정보
     */
    @Override
    public OrganizationDto getOrganizationByUserId(String userId) {
        OrganizationDto organizationDto = new OrganizationDto();
        UserDto user = getUserInfo(userId);
        if(Objects.isNull(user.getOrganization())){
            throw new OrganizationNotFoundException(user.getId());
        }
        BeanUtils.copyProperties(user.getOrganization(), organizationDto);
        return organizationDto;
    }
}
