package live.ioteatime.apiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.apiservice.domain.Alive;
import live.ioteatime.apiservice.domain.Organization;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SensorDto {
    private int id;
    @JsonProperty("sensor_name")
    private String name;
    @JsonProperty("sensor_model_name")
    private String modelName;
    private int channel;
    private String ip;
    private String port;
    private Alive alive;
    private Organization organization;
}
