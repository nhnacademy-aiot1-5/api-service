package live.ioteatime.apiservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class UpdateUserPasswordRequest {
    private String currentPassword;
    private String newPassword;
    private String passwordCheck;
}
