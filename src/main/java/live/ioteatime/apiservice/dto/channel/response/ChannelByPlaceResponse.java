package live.ioteatime.apiservice.dto.channel.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChannelByPlaceResponse {
    @JsonProperty("channel_id")
    private int id;
    @JsonProperty("channel_name")
    private String channelName;
    @JsonProperty("sensor_id")
    private int sensorId;
    @JsonProperty("place_id")
    private int placeId;
}
