package live.ioteatime.apiservice.dto.place;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaceRequestDto {
    @JsonProperty("place_name")
    private String placeName;
    @JsonProperty("organization_id")
    private int organizationId;
}
