package live.ioteatime.apiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter @Setter
public class SensorRequest {
    @JsonProperty("sensor_name")
    private String name;
    @JsonProperty("sensor_model_name")
    @NotBlank private String modelName;
    @NotBlank private String ip;
    @NotBlank private String port;
    @NotBlank private String exchange;
    @JsonProperty("routing_key")
    @NotBlank private String routingKey;
}
