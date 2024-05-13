package live.ioteatime.apiservice.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "유저의 정보를 담는 DTO입니다.")
public class UserDto {
    @Schema(description = "유저의 아이디입니다.")
    private String id;
    @JsonProperty("pw")
    @Schema(description = "유저의 비밀번호입니다.")
    private String password;
    @Schema(description = "유저의 이름입니다.")
    private String name;
    @Schema(description = "유저의 생성일입니다.")
    @JsonProperty("created_at")
    private LocalDate createdAt;
    @Schema(description = "유저의 권한입니다.")
    private Role role;
    @Schema(description = "유저의 소속 조직입니다.")
    private OrganizationDto organization;
}
