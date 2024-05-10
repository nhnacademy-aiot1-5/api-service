package live.ioteatime.apiservice.dto.electricity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KwhResponseDto {
    private LocalDateTime time;
    private Double kwh;
}
