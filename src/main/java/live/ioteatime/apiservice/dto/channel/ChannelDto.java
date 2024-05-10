package live.ioteatime.apiservice.dto.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.apiservice.dto.place.PlaceDto;
import live.ioteatime.apiservice.dto.sensor.ModbusSensorDto;
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
    private String type;
    @JsonProperty("function_code")
    private int functionCode;
    @JsonProperty("sensor_id")
    private int sensorId;
    @JsonProperty("place_id")
    private int placeId;
    @JsonProperty("place_name")
    private String placeName;
    private ModbusSensorDto sensor;
    private PlaceDto place;
}
