package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.RegisterRequest;
import live.ioteatime.apiservice.dto.UpdateUserPasswordRequest;
import live.ioteatime.apiservice.dto.UserDto;
import live.ioteatime.apiservice.service.OrganizationService;
import live.ioteatime.apiservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * 유저와 관련된 요청을 처리하여 요청에 해당하는 응답을 반환하는 컨트롤러입니다.
 *
 * @author 유승진, 임세연
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "USER 컨트롤러", description = "유저정보와 관련된 정보를 처리하는 컨트롤러입니다.")
public class UserController {
    private final UserService userService;
    private final OrganizationService organizationService;
    private final String X_USER_ID = "X-USER-ID";

    /**
     * 유저 아이디에 해당하는 유저의 정보를 반환하는 함수
     *
     * @param userId 유저 아이디
     * @return HttpStatus 200번 OK
     */
    @GetMapping
    @Operation(summary = "유저 정보를 가져오는 API", description = "유저의 정보를 가져옵니다.")
    public ResponseEntity<UserDto> getUserInfo(@RequestHeader(X_USER_ID) String userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    /**
     * 로그인 요청이 들어왔을 때 유저의 UserDetails 정보를 반환하는 함수이다.
     * @param userId 로그인 하는 유저의 아이디
     * @return HttpStatus 200번 OK
     */
    @GetMapping("/{userId}/details")
    @Operation(summary = "유저 인증을 담당하는 API", description = "로그인 요청을 받았을 때 유저 ID와 유저 PASSWORD를 반환합니다.")
    public ResponseEntity<UserDto> loadUserByUserName(@PathVariable String userId){
        return ResponseEntity.ok(userService.loadUserByUserName(userId));
    }

    /**
     * 프론트에서 받아온 유저의 정보를 가지고 새로운 유저를 데이터베이스에 등록해주는 컨트롤러다.
     * @param registerRequest 웹사이트에서 받아온 가입할 유저의 정보
     * @return HttpStatus 201번 Created
     */
    @PostMapping
    @Operation(summary = "회원정보를 생성하는 API", description = "회원가입 페이지에서 받은 정보를 데이터베이스에 저장합니다.")
    public ResponseEntity<String> createUser(@RequestBody RegisterRequest registerRequest) {
        String createdUserId = userService.createUser(registerRequest);

        URI location = UriComponentsBuilder
                .fromUriString("https://ioteatime.live/mypage")
                .build().toUri();

        return ResponseEntity.created(location).body("Successfully registered: userId="+ createdUserId);
    }

    /**
     * 유저 정보를 수정하는 컨트롤러
     * 경로 : /users
     * @param userDto 수정될 유저의 정보를 가지고 있는 Dto 클래스
     * @return HttpStatus 200 OK
     */
    @PutMapping("/update-user")
    @Operation(summary = "유저 정보를 업데이트하는 API", description = "유저 정보를 업데이트합니다.")
    public ResponseEntity<String> updateUser(@RequestHeader(X_USER_ID) String userId, @RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.updateUser(userDto));
    }

    /**
     * 유저 비밀번호만 수정하는 핸들러
     * @param userId 유저아이디
     * @param updatePasswordRequest 기존 비밀번호, 새 비밀번호, 새 비밀번호 확인 이 바디에 실려서 들어옴
     * @return HttpStatus 20 OK
     */
    @PutMapping("/password")
    @Operation(summary = "유저의 비밀번호를 변경하는 API", description = "유저 비밀번호를 변경합니다.")
    public ResponseEntity<String> updateUserPassword(@RequestHeader(X_USER_ID) String userId, @RequestBody UpdateUserPasswordRequest updatePasswordRequest){
        userService.updateUserPassword(userId, updatePasswordRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * 유저 소속 조직 정보를 리턴합니다.
     * @param userId 유저아이디
     * @return HttpStatus 200 OK
     */
    @GetMapping("/organization")
    @Operation(summary = "유저가 소속된 조직의 정보를 반환하는 API", description = "유저가 소속된 조직의 정보를 반환합니다.")
    public ResponseEntity<OrganizationDto> getOrganization(@RequestHeader(X_USER_ID) String userId){
        OrganizationDto organizationDto = userService.getOrganizationByUserId(userId);
        return ResponseEntity.ok(organizationDto);
    }

    @GetMapping("/budget")
    @Operation(summary = "조직의 현재 설정 금액을 가져오는 API", description = "조직의 현재 설정금액을 가져옵니다.")
    public ResponseEntity<OrganizationDto> getBudget(@RequestHeader(X_USER_ID) String userId) {
        return ResponseEntity.ok(organizationService.getBudget(userId));
    }

}
