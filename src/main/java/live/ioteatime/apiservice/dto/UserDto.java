package live.ioteatime.apiservice.dto;

import live.ioteatime.apiservice.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String password;
    private String name;
    private LocalDateTime createdAt;
    private Role role;
}
