package live.ioteatime.apiservice.dto.place;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.apiservice.dto.OrganizationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDto {

    @JsonProperty("place_id")
    private int id;

    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("organization_id")
    private int OrganizationId;

    private OrganizationDto organizationDto;

}
