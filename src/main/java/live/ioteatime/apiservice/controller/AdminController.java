package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.domain.Sensor;
import live.ioteatime.apiservice.dto.UserDto;
import live.ioteatime.apiservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final String X_USER_ID = "X-USER-ID";

    /**
     * 어드민만 사용할 수 있는 명령어이며 GUEST 권한을 가진 유저의 리스트를 가져오는 컨트롤러다.
     * @return GUEST 권한을 가진 유러의 리스트를 반환한다. HttpStatus 200번 OK
     */
    @GetMapping("/guests")
    @AdminOnly
    public ResponseEntity<List<UserDto>> getGuestUsers() {
        return ResponseEntity.ok(adminService.getGuestUsers());
    }

    /**
     * 어드민만 사용할 수 있는 명령어이며 모든 유저의 리스트를 가져오는 컨트롤러다.
     * @return 모든 유저의 리스트를 반환한다
     */
    @GetMapping("/users")
    @AdminOnly
    public ResponseEntity<List<UserDto>> getUsers(){
        return ResponseEntity.ok(adminService.getUsers());
    }

    /**
     * 어드민만 사용할 수 있는 명령어이며 연결된 센서의 리스트를 가져오는 컨트롤러다.
     * @return 연결된 센서들의 리스트를 반환한다.
     */
    @GetMapping("/sensors")
    @AdminOnly
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
    @Operation(summary = "유저 권한을 수정하는 API", description = "ADMIN 유저가 승인 대기중인 유저의 권한을 GUEST에서 USER로 수정합니다.")
    public ResponseEntity<UserDto> updateUserRole(@RequestHeader(X_USER_ID) String checkUserId, String userId){
        return ResponseEntity.ok(adminService.updateUserRole(userId));
    }
}
