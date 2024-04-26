package live.ioteatime.apiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceRequestDto {
    @JsonProperty("place_name")
    private String placeName;
    @JsonProperty("organization_id")
    private int organizationId;
}
