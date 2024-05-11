package live.ioteatime.apiservice.dto.electricity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityResponseDto {
    private LocalDateTime time;
    private long kwh;
    private Long bill;
}
