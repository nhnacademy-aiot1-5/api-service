package live.ioteatime.apiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "조직 정보를 담는 DTO입니다.")
public class OrganizationDto {
    @Schema(description = "조직 id입니다.")
    private int id;
    @Schema(description = "조직 이름입니다.")
    private String name;
    @Schema(description = "조직 ADMIN이 설정한 이번 달 목표 전기 요금 값입니다.")
    @JsonProperty("electricity_budget")
    private Long electricityBudget;
    @Schema(description = "조직 코드입니다.")
    @JsonProperty("organization_code")
    private String organizationCode;
}
