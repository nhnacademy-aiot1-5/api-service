package live.ioteatime.apiservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "조직 정보를 담는 DTO입니다.")
public class OrganizationDto {
    @Schema(description = "조직 id입니다.")
    private int id;
    @Schema(description = "조직 이름입니다.")
    private String name;
    @Schema(description = "조직 ADMIN이 설정한 이번 달 목표 전기 요금 값입니다.")
    private BigInteger electricityBudget;
}
