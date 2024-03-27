package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.dto.UserDto;
import live.ioteatime.apiservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> loadUserByUserName(@PathVariable String userId) {
        return ResponseEntity.ok(userService.loadUserByUserName(userId));
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("성공");
    }
}
