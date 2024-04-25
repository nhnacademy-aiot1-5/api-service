package live.ioteatime.apiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class BudgetHistoryDto {

    private int id;
    @JsonProperty("change_time")
    private LocalDateTime changeTime;
    private Long budget;
    private OrganizationDto organization;
}
