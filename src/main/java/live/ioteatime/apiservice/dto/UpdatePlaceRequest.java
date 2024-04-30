package live.ioteatime.apiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdatePlaceRequest {
    @JsonProperty("sensor_id")
    private int sensorId;
    @JsonProperty("place_id")
    private int placeId;
}
