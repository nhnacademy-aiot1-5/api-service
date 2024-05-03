package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.BudgetHistoryDto;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.UserDto;

import java.util.List;

public interface AdminService {
    List<UserDto> getGuestUsers(String userId);
    List<UserDto> getUsers(String userId);
    List<BudgetHistoryDto> getBudgetHistory(String userId);
    OrganizationDto getOrganization(String userId);
    Boolean isOrganizationCodeDuplicate(String code);
    UserDto updateUserRole(String userId);

}
