package live.ioteatime.apiservice.dto.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MqttSensorRequest {
    @JsonProperty("sensor_name")
    private String name;
    @JsonProperty("sensor_model_name")
    private String modelName;
    private String ip;
    private String port;
    @JsonProperty("place_id")
    private int placeId;
    private String topic;
    private String description;
}
