package live.ioteatime.apiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.apiservice.domain.Organization;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaceDto {
    private int id;
    @JsonProperty("place_name")
    private String placeName;
    private Organization organization;
}
