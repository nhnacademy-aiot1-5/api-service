package live.ioteatime.apiservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class AddBrokerRequest {
    private String mqttHost;
    private String mqttId;
}
