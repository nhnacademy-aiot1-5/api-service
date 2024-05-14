package live.ioteatime.apiservice.dto.topic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TopicDto {
    @JsonProperty("topic_id")
    private int id;
    private String topic;
    private String description;
}
