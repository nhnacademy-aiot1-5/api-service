package live.ioteatime.apiservice.dto;

import live.ioteatime.apiservice.domain.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String id;
    private String name;
    private LocalDateTime createdAt;
    private Role role;

}
