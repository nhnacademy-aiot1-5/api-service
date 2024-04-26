package live.ioteatime.apiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChannelDto {

    private int id;
    @JsonProperty("channel_name")
    private String channelName;
    private ModbusSensorDto sensor;
    private PlaceDto place;
}
