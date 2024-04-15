package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@AdminOnly
public class AdminController {
    private final AdminService adminService;
    private final String X_USER_ID = "X-USER-ID";

    @GetMapping("/guest")
    public ResponseEntity<List<User>> getGuestUser(){
        return ResponseEntity.ok(adminService.getGuestUser());
    }


    /**
     * 어드민만 사용할 수 있는 명령어이며 회원가입한 유저의 권한을 GUEST에서 USER로 바꿔주는 컨트롤러다.
     * @param userId 유저 권한을 GUEST -> USER로 바꿔줄 유저의 아이디
     * @return HttpStatus 200번 OK
     */
    @PutMapping("/roles")
    @Operation(summary = "유저 권한을 수정하는 API", description = "ADMIN 유저가 승인 대기중인 유저의 권한을 GUEST에서 USER로 수정합니다.")
    public ResponseEntity<String> updateUserRole(@RequestHeader(X_USER_ID) String userId){
        return ResponseEntity.ok(adminService.updateUserRole(userId));
    }
}
