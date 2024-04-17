package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.domain.Sensor;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.UserDto;
import live.ioteatime.apiservice.service.AdminService;
import live.ioteatime.apiservice.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "ADMIN 컨트롤러", description = "어드민만 사용할 수 있는 컨트롤러입니다.")
public class AdminController {
    private final AdminService adminService;
    private final OrganizationService organizationService;
    private final String X_USER_ID = "X-USER-ID";

    /**
     * 어드민만 사용할 수 있는 명령어이며 GUEST 권한을 가진 유저의 리스트를 가져오는 컨트롤러다.
     * @return GUEST 권한을 가진 유러의 리스트를 반환한다. HttpStatus 200번 OK
     */
    @GetMapping("/guests")
    @AdminOnly
    @Operation(summary = "유저의 권한이 GUEST인 유저들의 리스트를 가져오는 API", description = "유저의 권한이 GUEST인 유저들의 리스트를 가져옵니다.")
    public ResponseEntity<List<UserDto>> getGuestUsers() {
        return ResponseEntity.ok(adminService.getGuestUsers());
    }

    /**
     * 어드민만 사용할 수 있는 명령어이며 모든 유저의 리스트를 가져오는 컨트롤러다.
     * @return 모든 유저의 리스트를 반환한다
     */
    @GetMapping("/users")
    @AdminOnly
    @Operation(summary = "모든 유저들의 리스트를 가져오는 API", description = "모든 유저들의 리스트를 가져옵니다.")
    public ResponseEntity<List<UserDto>> getUsers(){
        return ResponseEntity.ok(adminService.getUsers());
    }

    @GetMapping("/budget")
    @AdminOnly
    @Operation(summary = "조직의 현재 설정 금액을 가져오는 API", description = "조직의 현재 설정금액을 가져옵니다.")
    public ResponseEntity<OrganizationDto> getBudget(@RequestHeader(X_USER_ID) String userId) {
        return ResponseEntity.ok(organizationService.getOrganization(userId));
    }

    /**
     * 어드민만 사용할 수 있는 명령어이며 연결된 센서의 리스트를 가져오는 컨트롤러다.
     * @return 연결된 센서들의 리스트를 반환한다.
     */
    @GetMapping("/sensors")
    @AdminOnly
    @Operation(summary = "모든 유저들의 리스트를 가져오는 API", description = "모든 유저들의 리스트를 가져옵니다.")
    public ResponseEntity<List<Sensor>> getSensors(){
        return ResponseEntity.ok(adminService.getSensors());
    }


    /**
     * 어드민만 사용할 수 있는 명령어이며 회원가입한 유저의 권한을 GUEST에서 USER로 바꿔주는 컨트롤러다.
     * @param userId 유저 권한을 GUEST -> USER로 바꿔줄 유저의 아이디
     * @return HttpStatus 200번 OK
     */
    @PutMapping("/roles")
    @AdminOnly
    @Operation(summary = "유저 권한을 수정하는 API", description = "ADMIN 유저가 GUEST 권한을 가진 유저의 권한을 GUEST에서 USER로 수정합니다.")
    public ResponseEntity<UserDto> updateUserRole(String userId){
        return ResponseEntity.ok(adminService.updateUserRole(userId));
    }


    @PutMapping("/budget")
    @AdminOnly
    @Operation(summary = "어드민이 속한 조직의 요금을 설정하는 API", description = "어드민이 속한 조직의 요금을 설정합니다.")
    public ResponseEntity<OrganizationDto> updateBudget(@RequestHeader(X_USER_ID) String userId, Long budget){
        return ResponseEntity.ok(organizationService.updateBudget(userId, budget));
    }
}
