package live.ioteatime.apiservice.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter @Setter
public class RegisterRequest {
    private String id;
    @JsonProperty("pw")
    private String password;
    private String name;
    @JsonProperty("organization_name")
    private String organizationName;
    @JsonProperty("organization_code")
    private String organizationCode;
}
