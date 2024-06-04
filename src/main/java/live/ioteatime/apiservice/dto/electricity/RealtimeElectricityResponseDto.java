package live.ioteatime.apiservice.dto.electricity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealtimeElectricityResponseDto {

    private String place;

    private String channel;

    private Double w;

}
