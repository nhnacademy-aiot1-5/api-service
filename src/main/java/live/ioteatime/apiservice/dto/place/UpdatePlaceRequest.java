package live.ioteatime.apiservice.dto.place;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdatePlaceRequest {
    @JsonProperty("place_id")
    private int placeId;
}
