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

    /**
     * 유저 아이디를 가지고 있는 유저의 정보를 반환하는 함수
     *
     * @param userId 유저 아이디
     * @return HttpStatus 200번 OK
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    /**
     * 유저의 UserDetail 정보를 반환하는 함수
     *
     * @return HttpStatus 200번 OK
     */
    @GetMapping("/{userId}/details")
    public ResponseEntity<UserDto> loadUserByUserName(@PathVariable String userId){
        return ResponseEntity.ok(userService.loadUserByUserName(userId));
    }

    /**
     *
     * @param userDto 웹사이트에서 받아온 가입할 유저의 정보
     * @return HttpStatus 201번 Created
     */
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDto userDto) {
        String createdUserId = userService.createUser(userDto);

        URI location = UriComponentsBuilder
                .fromUriString(userProperties.getUserDetailUri())
                .pathSegment(createdUserId)
                .build().toUri();

        return ResponseEntity.created(location).build();
    }


}
