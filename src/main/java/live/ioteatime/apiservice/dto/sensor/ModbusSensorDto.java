package live.ioteatime.apiservice.dto.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.apiservice.domain.Alive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModbusSensorDto {

    @JsonProperty("sensor_id")
    private int id;

    @JsonProperty("sensor_name")
    private String sensorName;

    @JsonProperty("sensor_model_name")
    private String modelName;

    private String ip;

    private String port;

    @JsonProperty("channel_count")
    private int channelCount;

    private Alive alive;

}