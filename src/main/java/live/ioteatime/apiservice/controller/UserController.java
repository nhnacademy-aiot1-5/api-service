package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.user.RegisterRequest;
import live.ioteatime.apiservice.dto.user.UpdateUserPasswordRequest;
import live.ioteatime.apiservice.dto.user.UserDto;
import live.ioteatime.apiservice.service.OrganizationService;
import live.ioteatime.apiservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@Tag(name = "User", description = "회원 API")
public class UserController {
    private final UserService userService;
    private final OrganizationService organizationService;
    private static final String X_USER_ID = "X-USER-ID";

    /**
     * 유저 아이디에 해당하는 유저의 정보를 반환하는 함수
     *
     * @param userId 유저 아이디
     * @return HttpStatus 200번 OK
     */
    @GetMapping
    @Operation(summary = "회원 단일 조회", description = "회원 아이디, 이름, 가입일, 권한, 조직 이름을 조회합니다.")
    public ResponseEntity<UserDto> getUserInfo(@RequestHeader(X_USER_ID) String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInfo(userId));
    }

    /**
     * 로그인 요청이 들어왔을 때 유저의 UserDetails 정보를 반환하는 함수이다.
     * @param userId 로그인 하는 유저의 아이디
     * @return HttpStatus 200번 OK
     */
    @GetMapping("/{userId}/details")
    @Operation(summary = "회원 아이디, 비밀번호 조회", description = "인증 서버가 로그인 요청을 처리할 때 사용합니다. 회원 아이디와 비밀번호를 반환합니다.")
    public ResponseEntity<UserDto> loadUserByUserName(@PathVariable String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.loadUserByUserName(userId));
    }

    /**
     * 프론트에서 받아온 유저의 정보를 가지고 새로운 유저를 데이터베이스에 등록해주는 컨트롤러다.
     * @param registerRequest 웹사이트에서 받아온 가입할 유저의 정보
     * @return HttpStatus 201번 Created
     */
    @PostMapping
    @Operation(summary = "회원 등록", description = "새로운 회원을 데이터베이스에 등록합니다.")
    public ResponseEntity<String> createUser(@RequestBody RegisterRequest registerRequest) {
        String createdUserId = userService.createUser(registerRequest);

        URI location = UriComponentsBuilder
                .fromUriString("https://www.ioteatime.live/mypage")
                .build().toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location).body("Successfully registered: userId=" + createdUserId);
    }

    /**
     * 유저 정보를 수정하는 컨트롤러
     * 경로 : /users
     * @param userDto 수정될 유저의 정보를 가지고 있는 Dto 클래스
     * @return HttpStatus 200 OK
     * 게이트웨이에서 엔드포인트를 확인할 때 필터에 따른 부득이한 이유로 update-user가 붙게 되었습니다.
     */
    @PutMapping("/update-user")
    @Operation(summary = "회원 정보 수정", description = "회원 이름을 수정합니다.")
    public ResponseEntity<String> updateUser(@RequestHeader(X_USER_ID) String userId, @RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userDto));
    }

    /**
     * 유저 비밀번호만 수정하는 핸들러
     * @param userId 유저아이디
     * @param updatePasswordRequest 기존 비밀번호, 새 비밀번호, 새 비밀번호 확인 이 바디에 실려서 들어옴
     * @return HttpStatus 200 OK
     */
    @PutMapping("/password")
    @Operation(summary = "회원 비밀번호 수정", description = "회원의 비밀번호를 수정합니다.")
    public ResponseEntity<String> updateUserPassword(@RequestHeader(X_USER_ID) String userId,
                                                     @RequestBody UpdateUserPasswordRequest updatePasswordRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserPassword(userId, updatePasswordRequest));
    }

    /**
     * 유저 소속 조직 정보를 리턴합니다.
     * @param userId 유저아이디
     * @return HttpStatus 200 OK
     */
    @GetMapping("/organization")
    @Operation(summary = "조직 단일 조회", description = "회원이 소속된 조직의 이름과 현재 목표 금액을 조회합니다.")
    public ResponseEntity<OrganizationDto> getOrganization(@RequestHeader(X_USER_ID) String userId) {
        OrganizationDto organizationDto = userService.getOrganizationByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(organizationDto);
    }

    /**
     * 유저 정보 조직의 예산을 리턴합니다.
     * @param userId
     * @return HttpStatus 200 Ok
     */
    @GetMapping("/budget")
    @Operation(summary = "조직 목표 요금 조회", description = "조직의 현재 목표 요금을 조회합니다.")
    public ResponseEntity<OrganizationDto> getBudget(@RequestHeader(X_USER_ID) String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.getBudget(userId));
    }

}
