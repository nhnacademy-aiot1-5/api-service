package live.ioteatime.apiservice.dto.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorRequest {

    @JsonProperty("sensor_name")
    private String sensorName;

    @JsonProperty("sensor_model_name")
    private String modelName;

    private String ip;

    private String port;

    @JsonProperty("place_id")
    private int placeId;

    @JsonProperty("channel_count")
    private int channelCount;

}
