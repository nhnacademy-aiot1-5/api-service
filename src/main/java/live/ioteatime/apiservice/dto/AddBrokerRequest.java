package live.ioteatime.apiservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter @Setter
public class AddBrokerRequest {
    private String mqttHost;
    private String mqttId;
    private List<String> mqttTopic;
}
