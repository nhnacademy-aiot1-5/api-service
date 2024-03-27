package live.ioteatime.apiservice.dto;

import live.ioteatime.apiservice.domain.Role;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 유저 정보를 데이터베이스에서 불러오는 정보를 가지고 있는 DTO입니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserGetDto {
    private String id;
    private String name;
    private LocalDateTime createdAt;
    private Role role;

}
