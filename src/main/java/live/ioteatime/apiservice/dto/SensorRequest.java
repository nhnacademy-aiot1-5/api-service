package live.ioteatime.apiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class SensorRequest {
    @JsonProperty("sensor_name")
    private String name;
    @JsonProperty("sensor_model_name")
    private String modelName;
    private String ip;
    private String port;
    @JsonProperty("place_id")
    private int placeId;
}
