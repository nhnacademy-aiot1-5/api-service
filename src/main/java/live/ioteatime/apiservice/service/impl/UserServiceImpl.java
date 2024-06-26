package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.user.RegisterRequest;
import live.ioteatime.apiservice.dto.user.UpdateUserPasswordRequest;
import live.ioteatime.apiservice.dto.user.UserDto;
import live.ioteatime.apiservice.exception.*;
import live.ioteatime.apiservice.repository.OrganizationRepository;
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
    private final OrganizationRepository organizationRepository;
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
     * CreatedAt은 LocalDate를 통해 현재 날짜를 넣음
     * Role은 가입할때는 기본적으로 Guest로 설정
     * Password는 인코딩 작업 후에 추가되어야 하므로 BeanUtils로 추가하지 않았음
     *
     * @param registerRequest 받아온 유저 정보를 데이터베이스에 저장합니다.
     * @return userDto request 에서 받아온 유저 정보입니다.
     */
    @Override
    public String createUser(RegisterRequest registerRequest) {

        if (userRepository.existsById(registerRequest.getId())) {
            throw new UserAlreadyExistsException(registerRequest.getId());
        }

        Organization organization = verifyAndRetrieveOrganization(registerRequest.getOrganizationName(), registerRequest.getOrganizationCode());

        User user = new User();
        BeanUtils.copyProperties(registerRequest, user, "password", "organizationCode");

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        user.setPassword(encodedPassword);
        user.setCreatedAt(LocalDate.now());
        user.setRole(Role.GUEST);
        user.setOrganization(organization);

        userRepository.save(user);

        return user.getId();
    }

    /**
     * 회원가입시 사용자가 입력한 조직 이름과 조직 코드를 검증하는 메서드입니다.
     * 조직이 존재하고 조직 이름과 조직 코드가 일치하는 경우, 해당 조직 엔티티를 리턴합니다.
     * 그렇지 않으면 OrganizationCodeNameMismatchException 을 던집니다.
     * @param orgName 사용자 입력 조직이름
     * @param orgCode 사용자 입력 조직코드
     * @return Organization 엔티티
     */
    Organization verifyAndRetrieveOrganization(String orgName, String orgCode){
        Organization organization = organizationRepository.findByOrganizationCode(orgCode);
        if(Objects.isNull(organization) || (!organization.getName().equals(orgName))){
            throw new OrganizationCodeNameMismatchException();
        }
        return organization;
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
        OrganizationDto organizationDto = new OrganizationDto();
        BeanUtils.copyProperties(user.getOrganization(), organizationDto);
        userDto.setOrganization(organizationDto);
        return userDto;
    }


    /**
     * @param userDto 유저 정보
     * @return userId 유저아이디
     */
    @Override
    public String updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new UserNotFoundException(userDto.getId()));
        BeanUtils.copyProperties(userDto, user, "password",  "organization");
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
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException(userId));
        if(Objects.isNull(user.getOrganization())){
            throw new OrganizationNotFoundException();
        }
        BeanUtils.copyProperties(user.getOrganization(), organizationDto);
        return organizationDto;
    }

    /**
     * 회원 비밀번호를 변경합니다.
     * @param userId 유저아이디
     * @param updatePasswordRequest 비밀번호 변경 요청 폼 데이터
     * @return 성공시, 유저아이디를 리턴합니다.
     */
    @Override
    public String updateUserPassword(String userId, UpdateUserPasswordRequest updatePasswordRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        if (!passwordEncoder.matches(updatePasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new UnauthenticatedException();
        }

        String encodedNewPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        user.setPassword(encodedNewPassword);
        userRepository.save(user);

        return user.getId();
    }
}
