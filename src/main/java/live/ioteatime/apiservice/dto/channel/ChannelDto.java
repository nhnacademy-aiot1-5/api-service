package live.ioteatime.apiservice.dto.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.apiservice.dto.ModbusSensorDto;
import live.ioteatime.apiservice.dto.PlaceWithoutOrganizationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDto {
    @JsonProperty("channel_id")
    private int id;
    @JsonProperty("channel_name")
    private String channelName;
    private int address;
    private int quantity;
    @JsonProperty("channel_name")
    private int functionCode;
    private ModbusSensorDto sensor;
    private PlaceWithoutOrganizationDto place;
}
