package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.dto.BudgetHistoryDto;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.UserDto;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final BudgetHistoryRepository budgetHistoryRepository;
    private final OrganizationRepository organizationRepository;

    private List<UserDto> getUserDtos(List<User> users) {
        List<UserDto> userDtos = new ArrayList<>();
        for (User findUser : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(findUser, userDto, "password");
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public List<UserDto> getGuestUsers(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        Organization organization = user.getOrganization();
        List<User> users = userRepository.findAllByRoleAndOrganization_Id(Role.GUEST, organization.getId());

        return getUserDtos(users);
    }

    @Override
    public List<UserDto> getUsers(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        Organization organization = user.getOrganization();
        List<User> users = userRepository.findAllByOrganization_Id(organization.getId());

        return getUserDtos(users);
    }

    @Override
    public List<BudgetHistoryDto> getBudgetHistory(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        Organization organization = user.getOrganization();

        return budgetHistoryRepository.findAllByOrganization_IdOrderByChangeTimeDesc(organization.getId());
    }

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

    @Override
    public Boolean isOrganizationCodeDuplicate(String code) {
        return organizationRepository.existsByOrganizationCode(code);
    }

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
