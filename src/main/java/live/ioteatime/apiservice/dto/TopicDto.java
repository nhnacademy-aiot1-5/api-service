package live.ioteatime.apiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TopicDto {
    @JsonProperty("topic_id")
    private int id;
    private String topic;
    private String description;
}
