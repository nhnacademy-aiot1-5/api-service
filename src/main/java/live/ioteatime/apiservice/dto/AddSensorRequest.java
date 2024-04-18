package live.ioteatime.apiservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class AddSensorRequest {
    private String name;
    private String modelName;
    private int channel;
    private String ip;
    private String port;
}
