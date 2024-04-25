package live.ioteatime.apiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.apiservice.domain.Alive;
import live.ioteatime.apiservice.domain.Organization;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MqttSensorDto {
    private int id;
    @JsonProperty("sensor_name")
    private String name;
    @JsonProperty("sensor_model_name")
    private String modelName;
    private String ip;
    private String port;
    @JsonProperty("rabbitmqExchange")
    private String exchange;
    @JsonProperty("rabbitmqRoutingKey")
    private String routingKey;
    private Alive alive;
    private Organization organization;
}
