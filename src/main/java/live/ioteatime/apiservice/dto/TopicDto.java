package live.ioteatime.apiservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TopicDto {
    private int topicId;
    private String topic;
    private String description;
    private int sensorId;
}
