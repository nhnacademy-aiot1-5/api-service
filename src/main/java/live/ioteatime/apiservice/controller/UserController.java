package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.dto.UserDto;
import live.ioteatime.apiservice.properties.UserProperties;
import live.ioteatime.apiservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * 유저와 관련된 요청을 처리하여 요청에 해당하는 응답을 반환하는 컨트롤러입니다.
 *
 * @author 유승진
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserProperties userProperties;
    private final String X_USER_ID = "X-USER-ID";

    /**
     * 유저 아이디에 해당하는 유저의 정보를 반환하는 함수
     *
     * @param userId 유저 아이디
     * @return HttpStatus 200번 OK
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserInfo(/*@RequestHeader(X_USER_ID) String xUserID, */@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    /**
     * 로그인 요청이 들어왔을 때 유저의 UserDetails 정보를 반환하는 함수이다.
     * @param userId 로그인 하는 유저의 아이디
     * @return HttpStatus 200번 OK
     */
    @GetMapping("/{userId}/details")
    public ResponseEntity<UserDto> loadUserByUserName(/*@RequestHeader(X_USER_ID) String xUserID, */@PathVariable String userId){
        return ResponseEntity.ok(userService.loadUserByUserName(userId));
    }

    /**
     * 프론트에서 받아온 유저의 정보를 가지고 새로운 유저를 데이터베이스에 등록해주는 컨트롤러다.
     * @param userDto 웹사이트에서 받아온 가입할 유저의 정보
     * @return HttpStatus 201번 Created
     */
    @PostMapping
    public ResponseEntity<Void> createUser(/*@RequestHeader(X_USER_ID) String xUserID, */@RequestBody UserDto userDto) {
        String createdUserId = userService.createUser(userDto);

        URI location = UriComponentsBuilder
                .fromUriString(userProperties.getUserDetailUri())
                .pathSegment(createdUserId)
                .build().toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * 어드민만 사용할 수 있는 명령어이며 회원가입한 유저의 권한을 GUEST에서 USER로 바꿔주는 컨트롤러다.
     * @param userId 유저 권한을 GUEST -> USER로 바꿔줄 유저의 아이디
     * @return HttpStatus 200번 OK
     */
    @PutMapping("/{userId}/roles")
    public ResponseEntity<String> updateUserRole(/*@RequestHeader(X_USER_ID) String xUserID, */@PathVariable("userId") String userId){
        return ResponseEntity.ok(userService.updateUserRole(userId));
    }

    /**
     * 유저 정보를 수정하는 컨트롤러
     * 경로 : "/users/{userId}
     * @param userDto 수정될 유저의 정보를 가지고 있는 Dto 클래스
     * @return HttpStatus 200 OK
     */
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(/*@RequestHeader(X_USER_ID) String xUserID, */@PathVariable("userId") UserDto userDto){
        return ResponseEntity.ok(userService.updateUser(userDto));
    }

}
