package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.user.BudgetHistoryDto;
import live.ioteatime.apiservice.dto.user.UserDto;

import java.util.List;

/**
 * 어드민 서비스에 필요한 기능을 구현한 서비스 입니다.
 */
public interface AdminService {

    /**
     * Controller의 getGuestUsers메서드에 사용되는 서비스로 GUEST 권한을 가진 유저의 리스트를 반환하는 서비스 입니다.
     * @param userId 어드민의 아이디를가져와 어드민이 소속된 조직을 불러옵니다.
     * @return 조직에한 속한 GUEST 권한을 가진 UserDtoList를 반환합니다.
     */
    List<UserDto> getGuestUsers(String userId);

    /**
     * Controller의 getUsers메서드에 사용되는 서비스로 어드민이 속한 모든 유저의 리스트를 반환합니다.
     * @param userId 어드민의 아이디를 가져와 어드민이 소속된 조직을 불러옵니다.
     * @return 조직에 속한 모든 유저를 반환합니다.
     */
    List<UserDto> getUsers(String userId);

    /**
     * Controller의 getBudgetHistory에 사용되는 서비스로 어드민이 속한 조직의 요금 변경 내역 리스트를 반환합니다.
     * @param userId 어드민의 아이디를 가져와 어드민이 소속된 조직의 내역을 불러옵니다.
     * @return 조직의 요금 변경 내역 리스트를 반환합니다.
     */
    List<BudgetHistoryDto> getBudgetHistory(String userId);

    /**
     * Controller의 getOrganization에 사용되는 서비스로 어드민이 속한 조직의 조직이름과 조직코드를 반환합니다.
     * @param userId 어드민의 아이디를 가져와 어드민이 소속된 조직의 이름과 코드를 불러옵니다.
     * @return 조직의 조직이름과 조직 코드를 반환합니다.
     */
    OrganizationDto getOrganization(String userId);

    /**
     * Controller의 isOrganizationCodeDuplicate에 사용되는 서비스로 조직 코드를 수정하기 전 DB에 중복되는값이 있는지 확인합니다.
     * @param code 중복체크를 하는데 사용될 코드입니다.
     * @return 있으면 true, 없으면 false를 반환합니다.
     */
    Boolean isOrganizationCodeDuplicate(String code);

    /**
     * controller의 updateUserRole에 사용되는 서비스로 GUEST인 유저의 권한을 Useer로 수정합니다.
     * @param userId 권한을 바꿀 여저의 아이디입니다.
     * @return 수정한 유저의 DTO를 반환합니다.
     */
    UserDto updateUserRole(String userId);

}
