package live.ioteatime.apiservice.dto.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.apiservice.domain.Alive;
import live.ioteatime.apiservice.dto.place.PlaceDto;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MqttSensorDto {
    private int id;
    @JsonProperty("sensor_name")
    private String name;
    @JsonProperty("sensor_model_name")
    private String modelName;
    private String ip;
    private String port;
    private Alive alive;
    private PlaceDto place;
}
