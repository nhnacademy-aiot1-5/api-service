package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.dto.BudgetHistoryDto;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.UserDto;
import live.ioteatime.apiservice.service.AdminService;
import live.ioteatime.apiservice.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 어드민만 사용할 수 있는 컨트롤러들 입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "ADMIN 컨트롤러", description = "어드민만 사용할 수 있는 컨트롤러입니다.")
public class AdminController {
    private final AdminService adminService;
    private final OrganizationService organizationService;
    private final String X_USER_ID = "X-USER-ID";

    /**
     * 어드민만 사용할 수 있는 컨트롤러로 GUEST 권한을 가진 유저의 리스트를 가져옵니다.
     * @param userId 어드민의 아이디를 가져와 권한을 체크합니다.
     * @return GUEST 권한을 가진 유러의 리스트를 반환합니다. HttpStatus 200번 OK
     */
    @GetMapping("/guests")
    @AdminOnly
    @Operation(summary = "소직에 속한 유저중 유저의 권한이 GUEST인 유저들의 리스트를 가져오는 API",
            description = "저직에 속한 유저중 유저의 권한이 GUEST인 유저들의 리스트를 가져옵니다.")
    public ResponseEntity<List<UserDto>> getGuestUsers(@RequestHeader(X_USER_ID) String userId) {
        return ResponseEntity.ok(adminService.getGuestUsers(userId));
    }

    /**
     * 어드민만 사용할 수 있는 컨트롤러로 조직에 속한 모든 유저의 리스트를 가져옵니다.
     * @param userId 어드민의 아이디를 가져와 권한을 체크합니다.
     * @return 모든 유저의 리스트를 반환합니다.
     */
    @GetMapping("/users")
    @AdminOnly
    @Operation(summary = "어드민이 속한 조직의 모든 유저들의 리스트를 가져오는 API",
            description = "어드민이 속한 조직의 모든 유저들의 리스트를 가져옵니다.")
    public ResponseEntity<List<UserDto>> getUsers(@RequestHeader(X_USER_ID) String userId) {
        return ResponseEntity.ok(adminService.getUsers(userId));
    }

    /**
     * 어드민만 사용할 수 있는 컨트롤러로 어드민이 속한 조직의 요금 변경 내역 리스트를 가져옵니다.
     * @param userId 어드민의 아이디를 가져와 권한을 체크합니다.
     * @return 요금 변경 내역리스트를 반환합니다.
     */
    @GetMapping("/budgethistory")
    @AdminOnly
    @Operation(summary = "어드민이 속한 조직의 요금 변경 내역 리스트를 가져오는 API",
            description = "어드민이 속한 조직의 요금 변경 내역 리스트를 가져옵니다.")
    public ResponseEntity<List<BudgetHistoryDto>> getBudgetHistory(@RequestHeader(X_USER_ID) String userId) {
        return ResponseEntity.ok(adminService.getBudgetHistory(userId));
    }

    /**
     * 어드민만 사용할 수 있는 컨트롤러로 어드민이 속한 조직의 요금 변경 내역 리스트를 가져옵니다.
     * @param userId 어드민의 아이디를 가져와 권한을 체크합니다.
     * @return 조직이름과 조직 코드를 반환합니다.
     */
    @GetMapping("/organization")
    @AdminOnly
    @Operation(summary = "어드민이 속한 조직이름과 조직 코드를 확인하는 API",
            description = "어드민이 속한 조직이름과 조직 코드를 가져옵니다.")
    public ResponseEntity<OrganizationDto> getOrganization(@RequestHeader(X_USER_ID) String userId) {
        return ResponseEntity.ok(adminService.getOrganization(userId));
    }

    /**
     * 어드민만 사용할 수 있는 컨트롤러로 조직 코드를 수정하기 전 중복 체크를 하는 메서드입니다.
     * @param code 클라이언트에서 입력한 조직 코드 값입니다.
     * @return 데이터베이스에 중복되는 code값이 있으면 true, 없으면 false를 반환합니다.
     */
    @GetMapping("/checkcode")
    @AdminOnly
    @Operation(summary = "조직 코드가 중복이 있는지 확인하는 API", description = "조직 코드가 이미 중복된 코드인지 확인합니다.")
    public ResponseEntity<Boolean> checkCode(String code) {
        return ResponseEntity.ok(adminService.checkCode(code));
    }

    /**
     * 어드민만 사용할 수 있는 컨트롤러로 회원가입한 유저의 권한을 GUEST에서 USER로 바꿔주는 메서드입니다.
     * @param userId 유저 권한을 GUEST -> USER로 바꿔줄 유저의 아이디
     * @return HttpStatus 200번 OK
     */
    @PutMapping("/roles")
    @AdminOnly
    @Operation(summary = "유저 권한을 수정하는 API",
            description = "ADMIN 유저가 GUEST 권한을 가진 유저의 권한을 GUEST에서 USER로 수정합니다.")
    public ResponseEntity<UserDto> updateUserRole(String userId) {
        return ResponseEntity.ok(adminService.updateUserRole(userId));
    }

    /**
     * 어드민만 사용할 수 있는 컨트롤러로 어드민이 속한 조직의 요금을 수정하는 메서드입니다.
     * @param userId 조직을 가져오기 위한 어드민의 아이디
     * @param budget 수정할 요금
     * @return HttpStatus 200번 OK
     */
    @PutMapping("/budget")
    @AdminOnly
    @Operation(summary = "어드민이 속한 조직의 요금을 설정하는 API", description = "어드민이 속한 조직의 요금을 설정합니다.")
    public ResponseEntity<OrganizationDto> updateBudget(@RequestHeader(X_USER_ID) String userId, Long budget) {
        return ResponseEntity.ok(organizationService.updateBudget(userId, budget));
    }

    /**
     * 어드민만 사용할 수 있는 컨트롤러로 어드민이 속한 조직의 이름을 수정하는 메서드 입니다.
     * @param userId 조직을 가져오기 위한 어드민의 아이디
     * @param name   수정할 조직 이름
     * @return HttpStats 200번 OK
     */
    @PutMapping("/organizationname")
    @AdminOnly
    @Operation(summary = "어드민이 속한 조직의 이름을 변경하는 API", description = "어드민이 속한 조직의 이름을 변경합니다.")
    public ResponseEntity<Organization> updateOrganizationName(@RequestHeader(X_USER_ID) String userId, String name) {
        return ResponseEntity.ok(organizationService.updateName(userId, name));
    }

    @PutMapping("/organizationcode")
    @AdminOnly
    @Operation(summary = "어드민이 속한 조직의 조직코드를 변경하는 API", description = "어드민이 속한 조직의 조직코드를 변경합니다.")
    public ResponseEntity<Organization> updateOrganizationCode(@RequestHeader(X_USER_ID) String userId, String code) {
        return ResponseEntity.ok(organizationService.updateCode(userId, code));
    }
}
