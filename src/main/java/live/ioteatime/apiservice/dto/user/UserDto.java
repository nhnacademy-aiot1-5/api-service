package live.ioteatime.apiservice.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.dto.OrganizationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String id;

    @JsonProperty("pw")
    private String password;

    private String name;

    @JsonProperty("created_at")
    private LocalDate createdAt;

    private Role role;

    private OrganizationDto organization;

}
