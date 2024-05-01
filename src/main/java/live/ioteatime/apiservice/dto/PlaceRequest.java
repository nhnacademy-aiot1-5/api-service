package live.ioteatime.apiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class PlaceRequest {
    @JsonProperty("place_name")
    private String placeName;
    @JsonProperty("organization_id")
    private int organizationId;
}
