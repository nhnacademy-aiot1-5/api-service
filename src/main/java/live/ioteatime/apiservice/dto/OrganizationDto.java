package live.ioteatime.apiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationDto {

    private int id;

    private String name;

    @JsonProperty("electricity_budget")
    private Long electricityBudget;

    @JsonProperty("organization_code")
    private String organizationCode;

}
