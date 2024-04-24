package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.domain.BudgetHistory;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.UserDto;

import java.util.List;

public interface AdminService {
    List<UserDto> getGuestUsers(String userId);
    List<UserDto> getUsers(String userId);
    List<BudgetHistory> getBudgetHistory(String userId);
    OrganizationDto getOrganization(String userId);
    Boolean checkCode(String code);
    UserDto updateUserRole(String userId);

}
