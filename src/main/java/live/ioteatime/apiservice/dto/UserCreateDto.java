package live.ioteatime.apiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 유저 정보를 데이터베이스에 넣을 때 필요로 하는 DTO입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    private String id;
    private String password;
    private String name;
    private LocalDateTime createdAt;
}
