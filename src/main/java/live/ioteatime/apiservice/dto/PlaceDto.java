package live.ioteatime.apiservice.dto;

import live.ioteatime.apiservice.domain.Organization;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaceDto {
    private int id;
    private String placeName;
    private Organization organization;
}
