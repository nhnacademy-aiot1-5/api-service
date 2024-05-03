package live.ioteatime.apiservice.dto.place;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceResponseDto {
    private int id;
    @JsonProperty("place_name")
    private String placeName;
}
