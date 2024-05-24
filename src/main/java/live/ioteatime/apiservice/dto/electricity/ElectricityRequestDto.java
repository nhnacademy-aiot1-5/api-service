package live.ioteatime.apiservice.dto.electricity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityRequestDto {
    private LocalDateTime time;
    private int channelId;
}
