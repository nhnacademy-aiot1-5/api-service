package live.ioteatime.apiservice.dto.topic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TopicRequest {
    String topic;
    String description;
}
