package live.ioteatime.apiservice.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
public class AddBrokerRequest {
    private String mqttHost;
    private String mqttId;
    private List<String> mqttTopic;
}
