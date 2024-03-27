package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.dto.UserCreateDto;
import live.ioteatime.apiservice.dto.UserGetDto;
import live.ioteatime.apiservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 유저와 관련된 요청을 처리하여 요청에 해당하는 응답을 반환하는 컨트롤러입니다.
 *
 * @author 유승진
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    /**
     * 유저 아이디를 가지고 있는 유저의 정보를 반환하는 함수
     *
     * @param userId 유저 아이디
     * @return HttpStatus 200번 OK
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserGetDto> loadUserByUserName(@PathVariable String userId) {
        return ResponseEntity.ok(userService.loadUserByUserName(userId));
    }

    /**
     *
     * @param userCreateDto 웹사이트에서 받아온 가입할 유저의 정보
     * @return HttpStatus 201번 Created
     */
    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserCreateDto userCreateDto) {
        userService.createUser(userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("성공");
    }
}
