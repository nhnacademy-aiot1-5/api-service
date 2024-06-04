package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.OrganizationDto;

/**
 * 조직 서비스에 필요안 기능을 구현한 서비스 입니다.
 */
public interface OrganizationService {
    /**
     * 예산을 가지고 오는 서비스 입니다.
     * @param userId 조직에 소속된 유저의 ID입니다.
     * @return 예산의 정보를 가진 조직 DTO를 반환합니다.
     */
    OrganizationDto getBudget(String userId);

    /**
     * 유저 아이디에 해당하는 조직의 예산을 변경합니다.
     * @param userId 조직에 소속된 유저의 ID입니다.
     * @param budget 변경할 예산의 금액입니다.
     * @return 수정된 예산의 정보를 가진 조직 DTO를 반환합니다.
     */
    OrganizationDto updateBudget(String userId, Long budget);

    /**
     * 유저 아이디에 해당하는 조직의 이름을 변경합니다.
     * @param userId 조직에 소속된 유저의 ID입니다.
     * @param name   변경할 조직의 이름입니다.
     * @return 수정된 조직 이름을 정보를 가진 조직 DTO를 반환합니다.
     */
    OrganizationDto updateName(String userId, String name);

    /**
     * 유저 아이디에 해당하는 조직의 조직코드를 변경합니다.
     * @param userId 조직에 소속된 유저의 ID입니다.
     * @param code   변경할 조직의 조직코드입니다.
     * @return 수정된 조직 코드 정보를 가진 DTO를 반환합니다.
     */
    OrganizationDto updateCode(String userId, String code);
}
