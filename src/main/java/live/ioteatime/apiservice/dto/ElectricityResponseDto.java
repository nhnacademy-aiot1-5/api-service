package live.ioteatime.apiservice.dto;

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
}
