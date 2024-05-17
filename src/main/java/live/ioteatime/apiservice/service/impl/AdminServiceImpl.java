package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.user.BudgetHistoryDto;
import live.ioteatime.apiservice.dto.user.UserDto;
import live.ioteatime.apiservice.exception.OrganizationNotFoundException;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.repository.BudgetHistoryRepository;
import live.ioteatime.apiservice.repository.OrganizationRepository;
import live.ioteatime.apiservice.repository.UserRepository;
import live.ioteatime.apiservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final BudgetHistoryRepository budgetHistoryRepository;
    private final OrganizationRepository organizationRepository;

    /**
     * UserList를 UserDtoList 로 변환하는 서비스 입니다.
     * @param users 유저의 리스트를 받아옵니다.
     * @return 변환한 UserDto의 리스트를 반환합니다.
     */
    private List<UserDto> getUserDtos(List<User> users) {
        List<UserDto> userDtos = new ArrayList<>();
        for (User findUser : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(findUser, userDto, "password");
            userDtos.add(userDto);
        }
        return userDtos;
    }

    /**
     * Controller의 getGuestUsers메서드에 사용되는 서비스로 GUEST 권한을 가진 유저의 리스트를 반환하는 서비스 입니다.
     * @param userId 어드민의 아이디를가져와 어드민이 소속된 조직을 불러옵니다.
     * @return 조직에한 속한 GUEST 권한을 가진 UserDtoList를 반환합니다.
     */
    @Override
    public List<UserDto> getGuestUsers(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        Organization organization = user.getOrganization();
        List<User> users = userRepository.findAllByRoleAndOrganization_Id(Role.GUEST, organization.getId());

        return getUserDtos(users);
    }

    /**
     * Controller의 getUsers메서드에 사용되는 서비스로 어드민이 속한 모든 유저의 리스트를 반환합니다.
     * @param userId 어드민의 아이디를 가져와 어드민이 소속된 조직을 불러옵니다.
     * @return 조직에 속한 모든 유저를 반환합니다.
     */
    @Override
    public List<UserDto> getUsers(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        Organization organization = user.getOrganization();
        List<User> users = userRepository.findAllByOrganization_Id(organization.getId());

        return getUserDtos(users);
    }

    /**
     * Controller의 getBudgetHistory에 사용되는 서비스로 어드민이 속한 조직의 요금 변경 내역 리스트를 반환합니다.
     * @param userId 어드민의 아이디를 가져와 어드민이 소속된 조직의 내역을 불러옵니다.
     * @return 조직의 요금 변경 내역 리스트를 반환합니다.
     */
    @Override
    public List<BudgetHistoryDto> getBudgetHistory(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        Organization organization = user.getOrganization();

        return budgetHistoryRepository.findAllByOrganization_IdOrderByChangeTimeDesc(organization.getId())
                .stream().map(b -> {
                    BudgetHistoryDto budgetHistoryDto = new BudgetHistoryDto();
                    BeanUtils.copyProperties(b, budgetHistoryDto);
                    return budgetHistoryDto;
                }).collect(Collectors.toList());
    }

    /**
     * Controller의 getOrganization에 사용되는 서비스로 어드민이 속한 조직의 조직이름과 조직코드를 반환합니다.
     * @param userId 어드민의 아이디를 가져와 어드민이 소속된 조직의 이름과 코드를 불러옵니다.
     * @return 조직의 조직이름과 조직 코드를 반환합니다.
     */
    @Override
    public OrganizationDto getOrganization(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        Organization organization = user.getOrganization();
        if(Objects.isNull(organization)){
            throw new OrganizationNotFoundException();
        }
        OrganizationDto organizationDto = new OrganizationDto();
        BeanUtils.copyProperties(organization, organizationDto, "id", "electricityBudget");
        return organizationDto;
    }

    /**
     * Controller의 isOrganizationCodeDuplicate에 사용되는 서비스로 조직 코드를 수정하기 전 DB에 중복되는값이 있는지 확인합니다.
     * @param code 중복체크를 하는데 사용될 코드입니다.
     * @return 있으면 true, 없으면 false를 반환합니다.
     */
    @Override
    public Boolean isOrganizationCodeDuplicate(String code) {
        return organizationRepository.existsByOrganizationCode(code);
    }

    /**
     * controller의 updateUserRole에 사용되는 서비스로 GUEST인 유저의 권한을 Useer로 수정합니다.
     * @param userId 권한을 바꿀 여저의 아이디입니다.
     * @return 수정한 유저의 DTO를 반환합니다.
     */
    @Override
    public UserDto updateUserRole(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        user.setRole(Role.USER);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto, "password");
        userRepository.save(user);
        return userDto;
    }


}
